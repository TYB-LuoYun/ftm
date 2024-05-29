package top.anets.file.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.anets.file.model.chunk.FileChunksMergeDTO;
import top.anets.file.model.common.StrPool;
import top.anets.file.model.domain.FileDeleteBO;
import top.anets.file.model.domain.FileGetUrlBO;
import top.anets.file.model.entity.File;
import top.anets.file.model.enumeration.FileStorageType;
import top.anets.file.model.vo.param.FileUploadVO;
import top.anets.file.model.vo.result.FileInfo;
import top.anets.file.properties.FileServerProperties;
import top.anets.file.utils.ArgumentAssert;
import top.anets.file.utils.CollHelper;
import top.anets.file.utils.R;
import top.anets.file.utils.ZipUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author zuihou
 * @date 2021/7/8 10:26
 */
@Slf4j
@Component
public class FileContext {
    private final Map<String, FileStrategy> contextStrategyMap = new ConcurrentHashMap<>();
    private final Map<String, FileChunkStrategy> contextChunkStrategyMap = new ConcurrentHashMap<>();
    private final FileServerProperties fileServerProperties;


    public FileContext(Map<String, FileStrategy> map,
                       Map<String, FileChunkStrategy> chunkMap,
                       FileServerProperties fileServerProperties ) {
        map.forEach(this.contextStrategyMap::put);
        chunkMap.forEach(this.contextChunkStrategyMap::put);
        this.fileServerProperties = fileServerProperties;

    }

    private static Predicate<FileInfo> getFilePredicate() {
        return file -> file != null && StrUtil.isNotEmpty(file.getUrl());
    }

    private static String buildNewFileName(String filename, Integer order) {
        return StrUtil.strBuilder(filename).insert(filename.lastIndexOf(StrPool.DOT), "(" + order + ")").toString();
    }

    /**
     * 文件上传
     *
     * @param file         文件
     * @param fileUploadVO 文件上传参数
     * @return 文件对象
     */
    public FileInfo upload(MultipartFile file, FileUploadVO fileUploadVO) {
        FileStrategy fileStrategy = getFileStrategy(fileUploadVO.getStorageType());
        return fileStrategy.upload(file, fileUploadVO );
    }


    public void download(HttpServletResponse response,String storageType,String bucket,String path){
        FileStorageType fileStorageType = FileStorageType.LOCAL;
        if(StringUtils.isNotBlank(storageType)){
           fileStorageType =  FileStorageType.valueOf(storageType);
        }
        FileStrategy fileStrategy = getFileStrategy( fileStorageType);
        fileStrategy.download(response, bucket, path);
    }

    public void get(HttpServletResponse response, String storageType, String bucket, String path) {
        FileStorageType fileStorageType = FileStorageType.LOCAL;
        if(StringUtils.isNotBlank(storageType)){
            fileStorageType =  FileStorageType.valueOf(storageType);
        }
        FileStrategy fileStrategy = getFileStrategy( fileStorageType);
        fileStrategy.get(response, bucket, path);
    }




    private FileStrategy getFileStrategy(FileStorageType storageType) {
        storageType = storageType == null ? fileServerProperties.getStorageType() : storageType;
        FileStrategy fileStrategy = contextStrategyMap.get(storageType.name());
        ArgumentAssert.notNull(fileStrategy, "请配置正确的文件存储类型");
        return fileStrategy;
    }

    /**
     * 删除源文件
     *
     * @param list 列表
     * @return 是否成功
     */
    public boolean delete(List<File> list) {
        if (!fileServerProperties.getDelFile()) {
            return false;
        }

        list.forEach(item -> {
            FileDeleteBO fileDeleteBO = FileDeleteBO.builder()
                    .bucket(item.getBucket())
                    .path(item.getPath())
                    .storageType(item.getStorageType())
                    .build();
            FileStrategy fileStrategy = getFileStrategy(item.getStorageType());
            fileStrategy.delete(fileDeleteBO);
        });
        return true;
    }

    public boolean delete(FileDeleteBO fileDeleteBO) {
        FileStrategy fileStrategy = getFileStrategy(fileDeleteBO.getStorageType());
        fileStrategy.delete(fileDeleteBO);
        return true;
    }

    /**
     * 根据路径获取访问地址
     *
     * @param paths 文件查询对象
     * @return
     */
    public Map<String, String> findUrlByPath(List<String> paths) {
//        List<File> pathFiles = fileMapper.selectList(Wrappers.<File>lambdaQuery() .in(File::getPath, paths));
//
//        return findUrl(pathFiles);
        return null;
    }

    private Map<String, String> findUrl(List<File> pathFiles) {
        Map<String, List<File>> pathMap = pathFiles.stream().collect(Collectors.groupingBy(File::getPath, LinkedHashMap::new, toList()));

        Map<String, String> map = new LinkedHashMap<>(CollHelper.initialCapacity(pathMap.size()));
        pathMap.forEach((path, files) -> {
            if (CollUtil.isEmpty(files)) {
                return;
            }
            File fileFile = files.get(0);

            if (FileStorageType.LOCAL.eq(fileFile.getStorageType())) {
                map.put(path, fileFile.getUrl());
            } else {
                FileStrategy fileStrategy = getFileStrategy(fileFile.getStorageType());
                map.put(path, fileStrategy.getUrl(FileGetUrlBO.builder()
                        .bucket(fileFile.getBucket())
                        .path(fileFile.getPath())
                        .originalFileName(fileFile.getOriginalFileName())
                        .build()));
            }
        });
        return map;
    }

    public Map<Long, String> findUrlById(List<Long> ids) {
//        List<File> pathFiles = fileMapper.selectList(Wrappers.<File>lambdaQuery().in(File::getFid, ids));
//
//        Map<Long, List<File>> pathMap = pathFiles.stream().collect(Collectors.groupingBy(File::getFid, LinkedHashMap::new, toList()));
//
//        Map<Long, String> map = new LinkedHashMap<>(CollHelper.initialCapacity(pathMap.size()));
//        pathMap.forEach((id, files) -> {
//            if (CollUtil.isEmpty(files)) {
//                return;
//            }
//            File fileFile = files.get(0);
//
//            FileStrategy fileStrategy = getFileStrategy(fileFile.getStorageType());
//            map.put(id, fileStrategy.getUrl(FileGetUrlBO.builder()
//                    .bucket(fileFile.getBucket())
//                    .path(fileFile.getPath())
//                    .originalFileName(fileFile.getOriginalFileName())
//                    .build()));
//        });
//        return map;
        return null;
    }

    public void download(HttpServletRequest request, HttpServletResponse response, List<FileInfo> list) throws Exception {
        for (FileInfo fileFile : list) {
            FileStrategy fileStrategy = getFileStrategy(fileFile.getStorageType());
            String url = fileStrategy.getUrl(FileGetUrlBO.builder()
                    .bucket(fileFile.getBucket())
                    .path(fileFile.getPath())
                    .build());
            fileFile.setUrl(url);
        }
        down(request, response, list);
    }

    public void down(HttpServletRequest request, HttpServletResponse response, List<FileInfo> list) throws Exception {
        long fileSize = list.stream().filter(getFilePredicate())
                .mapToLong(file -> Convert.toLong(file.getSize(), 0L)).sum();
        String packageName = list.get(0).getOriginalFileName();
        if (list.size() > 1) {
            packageName = StrUtil.subBefore(packageName, ".", true) + "等.zip";
        }

        Map<String, String> map = new LinkedHashMap<>(CollHelper.initialCapacity(list.size()));
        Map<String, Integer> duplicateFile = new HashMap<>(map.size());
        list.stream()
                //过滤不符合要求的文件
                .filter(getFilePredicate())
                //循环处理相同的文件名
                .forEach(file -> {
                    String originalFileName = file.getOriginalFileName();
                    if (map.containsKey(originalFileName)) {
                        if (duplicateFile.containsKey(originalFileName)) {
                            duplicateFile.put(originalFileName, duplicateFile.get(originalFileName) + 1);
                        } else {
                            duplicateFile.put(originalFileName, 1);
                        }
                        originalFileName = buildNewFileName(originalFileName, duplicateFile.get(originalFileName));
                    }
                    map.put(originalFileName, file.getUrl());
                });


        ZipUtils.zipFilesByInputStream(map, fileSize, packageName, request, response);
    }

    public FileChunkStrategy getFileChunkStrategy(FileStorageType storageType) {
        storageType = storageType == null ? fileServerProperties.getStorageType() : storageType;
        FileChunkStrategy fileStrategy = contextChunkStrategyMap.get(storageType.name()+"_CHUNK");
        ArgumentAssert.notNull(fileStrategy, "请配置正确的文件存储类型");
        return fileStrategy;
    }

//    /**
//     * 根据md5检测文件
//     *
//     * @param md5    md5
//     * @param userId 用户id
//     * @return 附件
//     */
//    public File md5Check(String md5, Long userId) {
//        FileChunkStrategy fileChunkStrategy = getFileChunkStrategy(fileServerProperties.getStorageType());
//        return fileChunkStrategy.md5Check(md5, userId);
//    }

//    /**
//     * 合并文件
//     *
//     * @param merge 合并参数
//     * @return 附件
//     */
//    public R<File> chunksMerge(FileChunksMergeDTO merge) {
//        FileChunkStrategy fileChunkStrategy = getFileChunkStrategy(fileServerProperties.getStorageType());
//        return fileChunkStrategy.chunksMerge(merge);
//    }


}
