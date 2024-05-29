package top.anets.file.strategy.impl;

import cn.hutool.core.convert.Convert;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.Base64;
import com.say.common.oss.core.FileTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import top.anets.file.model.chunk.FileChunkInitDTO;
import top.anets.file.model.chunk.FileChunkInitRq;
import top.anets.file.model.chunk.FileChunkUploadRes;
import top.anets.file.model.chunk.FileChunksMergeDTO;
import top.anets.file.model.common.StrPool;
import top.anets.file.model.entity.File;
import top.anets.file.properties.FileServerProperties;
import top.anets.file.strategy.FileChunkStrategy;
import top.anets.file.strategy.FileLock;
import top.anets.file.utils.FileTypeUtil;
import top.anets.file.utils.FileUtils;
import top.anets.file.utils.R;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;


/**
 * 文件分片处理 抽象策略类
 * 参考资料: https://blog.csdn.net/licux/article/details/115123498
 *
 * @author tyb
 * @date 2019/06/19
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractFileChunkStrategy implements FileChunkStrategy {
    protected final FileServerProperties fileProperties;
    @Autowired
    private FileTemplate template;

    @Override
    public FileChunkInitDTO initUploadChunk(FileChunkInitRq fileChunkInitRq){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(fileChunkInitRq.getContentType());
        String bucketName = fileChunkInitRq.getBucket() == null?"dev":fileChunkInitRq.getBucket();
        String uniqueFileName = FileUtils.getUniqueFileName(fileChunkInitRq.getFileName());
        String objectName =  FileUtils.getPath(fileChunkInitRq.getBizType(), uniqueFileName);
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName,
                objectName, metadata);
        InitiateMultipartUploadResult initResponse = template.initiateMultipartUpload(initRequest);
        return FileChunkInitDTO.builder().uploadId(initResponse.getUploadId())
                .bucketName(bucketName)
                .objectName(objectName)
                .uniqueFileName(uniqueFileName)
                .fileName(fileChunkInitRq.getFileName())
                .size(fileChunkInitRq.getSize())
                .contentType(fileChunkInitRq.getContentType())
                .build();
    }






    public FileChunkUploadRes uploadPart(MultipartFile file, Long chunkSize,Integer totalChunks, Integer chunkPosition, String uploadId, String bucketName, String objectName){
        // 分片大小必须和前端匹配，否则上传会导致文件损坏
        chunkSize = chunkSize == 0L ? 20 * 1024 * 102 : chunkSize;
        // 偏移量
        long offset = chunkSize * (chunkPosition - 1);
        UploadPartRequest uploadRequest = null;
        byte[] md5s = null;
        try {
            md5s = MessageDigest.getInstance("MD5").digest(file.getBytes());
            uploadRequest = new UploadPartRequest()
                    .withBucketName(bucketName)
                    .withKey(objectName)
                    .withUploadId(uploadId)
                    .withPartNumber(chunkPosition)
                    .withMD5Digest(Base64.encodeAsString(md5s))
//                    .withFileOffset(offset)  //这个待考究，出过问题
    //                .withFile(toFile)
                    .withInputStream(file.getInputStream())
                    .withPartSize(file.getSize());
            PartETag partETag = template.uploadPart(uploadRequest).getPartETag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ListPartsRequest request = new ListPartsRequest(bucketName,objectName,uploadId);
        PartListing partListing = template.listParts(request);
        System.out.println(partListing.getParts().size());
        if(partListing.getParts().size()>=totalChunks){
            System.out.println("分片上传完成，开始合并");
            List<PartETag> collect = partListing.getParts().stream().map(e -> {
                PartETag partETag = new PartETag(e.getPartNumber(), e.getETag());
                return partETag;
            }).collect(Collectors.toList());
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, collect);
            template.completeMultipartUpload(compRequest);
            System.out.println("分片合并完成");
        }
        return FileChunkUploadRes.builder().totalChunks(totalChunks).successChunkSize(partListing.getParts().size()).build();

    }





    /**
     * 秒传验证
     * 根据文件的MD5签名判断该文件是否已经存在
     *
     * @param md5 文件的md5签名
     * @return 若存在则返回该文件的路径，不存在则返回null
     */
//    private File md5Check(String md5) {
////        List<File> files = fileMapper.selectList(Wrappers.<File>lambdaQuery().eq(File::getFileMd5, md5));
////        if (files.isEmpty()) {
////            return null;
////        }
////        return files.get(0);
//        return null;
//    }

//    public File md5Check(String md5, Long accountId) {
//        File file = md5Check(md5);
//        if (file == null) {
//            return null;
//        }
//
//        //分片存在，不需上传， 复制一条数据，重新插入
//        copyFile(file);
//
//        file.setFid(null)
//                .setCreatedBy(accountId)
//                .setCreateTime(LocalDateTime.now());
//        file.setUpdateTime(LocalDateTime.now())
//                .setUpdatedBy(accountId);
//
////        fileMapper.insert(file);
//        return file;
//    }

    /**
     * 让子类自己实现复制
     *
     * @param file 附件
     */
    protected abstract void copyFile(File file);


/*
    private R<File> chunksMerge(FileChunksMergeDTO info, String fileName) {
        String path = FileTypeUtil.getUploadPathPrefix(fileProperties.getLocal().getStoragePath());
        int chunks = info.getChunks();
        String folder = info.getName();
        String md5 = info.getMd5();

        int chunksNum = this.getChunksNum(Paths.get(path, folder).toString());
        log.info("chunks={}, chunksNum={}", chunks, chunksNum);
        //检查是否满足合并条件：分片数量是否足够
        if (chunks == chunksNum) {
            //同步指定合并的对象
            Lock lock = FileLock.getLock(folder);
            try {
                lock.lock();
                //检查是否满足合并条件：分片数量是否足够
                List<java.io.File> files = new ArrayList<>(Arrays.asList(this.getChunks(Paths.get(path, folder).toString())));
                if (chunks == files.size()) {
                    //按照名称排序文件，这里分片都是按照数字命名的

                    //这里存放的文件名一定是数字
                    files.sort(Comparator.comparingInt(f -> Convert.toInt(f.getName(), 0)));

                    R<File> result = merge(files, path, fileName, info);
                    files = null;

                    //清理：文件夹，tmp文件
                    this.cleanSpace(folder, path);
                    return result;
                }
            } catch (Exception ex) {
                log.error("数据分片合并失败", ex);
                return R.fail("数据分片合并失败");
            } finally {
                //解锁
                lock.unlock();
                //清理锁对象
                FileLock.removeLock(folder);
            }
        }
        //去持久层查找对应md5签名，直接返回对应path
        File file = this.md5Check(md5);
        if (file == null) {
            log.error("文件[签名:" + md5 + "]数据不完整，可能该文件正在合并中");
            return R.fail("数据不完整，可能该文件正在合并中, 也有可能是上传过程中某些分片丢失");
        }
        return R.success(file);
    }*/


    /**
     * 子类实现具体的合并操作
     *
     * @param files    文件
     * @param path     路径
     * @param fileName 唯一名 含后缀
     * @param info     文件信息
     * @return 附件信息
     * @throws IOException IO
     */
    protected abstract R<File> merge(List<java.io.File> files, String path, String fileName, FileChunksMergeDTO info) throws IOException;


//    /**
//     * 清理分片上传的相关数据
//     * 文件夹，tmp文件
//     *
//     * @param folder 文件夹名称
//     * @param path   上传文件根路径
//     * @return 是否成功
//     */
//    protected boolean cleanSpace(String folder, String path) {
//        //删除分片文件夹
//        java.io.File garbage = new java.io.File(Paths.get(path, folder).toString());
//        if (!FileUtils.deleteQuietly(garbage)) {
//            return false;
//        }
//        //删除tmp文件
//        garbage = new java.io.File(Paths.get(path, folder + ".tmp").toString());
//        return FileUtils.deleteQuietly(garbage);
//    }


    /**
     * 获取指定文件的分片数量
     *
     * @param folder 文件夹路径
     * @return 分片数量
     */
//    private int getChunksNum(String folder) {
//        return this.getChunks(folder).length;
//    }

//    /**
//     * 获取指定文件的所有分片
//     *
//     * @param folder 文件夹路径
//     * @return 分片文件
//     */
//    private java.io.File[] getChunks(String folder) {
//        java.io.File targetFolder = new java.io.File(folder);
//        return targetFolder.listFiles(file -> !file.isDirectory());
//    }

}
