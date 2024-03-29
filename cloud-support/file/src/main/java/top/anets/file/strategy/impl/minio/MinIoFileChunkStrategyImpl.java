package top.anets.file.strategy.impl.minio;

import cn.hutool.core.lang.Assert;
import io.minio.MakeBucketArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import top.anets.file.model.chunk.FileChunksMergeDTO;
import top.anets.file.model.entity.File;
import top.anets.file.properties.FileServerProperties;
import top.anets.file.strategy.impl.AbstractFileChunkStrategy;
import top.anets.file.utils.R;

import java.io.IOException;
import java.util.List;

/**
 * 欢迎PR
 * <p>
 * 思路1：minIO的putObject自身就支持断点续传， 所以先将分片文件上传到文件服务器并合并成大文件后， 在将大文件通过putObject直接上传到minIO
 *
 * @author zuihou
 * @date 2020/11/22 5:02 下午
 */
@Slf4j
//@Component("MIN_IO_FileChunk")
public class MinIoFileChunkStrategyImpl extends AbstractFileChunkStrategy {
    private final MinioTemplate minioTemplate;

    public MinIoFileChunkStrategyImpl(FileServerProperties fileProperties, MinioTemplate minioTemplate) {
        super( fileProperties);
        this.minioTemplate = minioTemplate;
    }


    @Override
    protected void copyFile(File file) {

    }

    @Override
    protected R<File> merge(List<java.io.File> files, String path, String fileName, FileChunksMergeDTO info) throws IOException {
        return null;
    }


    protected void merge(String identifier, String path, String fileName) throws IOException {
        String md5 = identifier;
        try {
            // 开始合并请求
            String targetBucketName = fileProperties.getMinIo().getBucket();
//            String fileNameWithoutExtension = UUID.randomUUID().toString();
            String objectName = fileName;
            // 合并文件
            minioTemplate.composeObject(md5, targetBucketName, objectName);
            log.info("桶：{} 中的分片文件，已经在桶：{},文件 {} 合并成功", md5, targetBucketName, objectName);

            // 合并成功之后删除对应的临时桶
            minioTemplate.removeBucket(md5, true);
            log.info("删除桶 {} 成功", md5);

            // 表示是同一个文件, 且文件后缀名没有被修改过
            String url = minioTemplate.getPresignedObjectUrl(targetBucketName, objectName);

            // 存入redis中
//            redisTemplate.boundHashOps(MD5_KEY).put(md5, url);


        } catch (Exception e) {
            log.error("文件合并执行异常 => ", e);
            throw new RuntimeException(e.getMessage());
        }
    }


}
