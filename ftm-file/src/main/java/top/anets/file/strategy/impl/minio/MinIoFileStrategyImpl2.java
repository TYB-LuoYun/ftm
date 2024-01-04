//package top.anets.file.strategy.impl.minio;
//
//import cn.hutool.core.lang.Assert;
//import cn.hutool.core.util.StrUtil;
//import io.minio.MinioClient;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//import top.anets.file.properties.FileServerProperties;
//import top.anets.file.strategy.impl.minio.model.MergeInfo;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.util.UUID;
//
///**
// * @author zuihou
// * @date 2020/11/22 5:00 下午
// */
//@Slf4j
//@Component("MIN_IO")
//public class MinIoFileStrategyImpl2 {// extends AbstractFileStrategy {
//    private static final String MD5_KEY = "自定义前缀:minio:file:md5List";
//
//    @Resource
//    private MinioClient minioClient;
//    @Resource
//    private FileServerProperties minioConfig;
//    @Resource
//    private MinioTemplate minioTemplate;
//
//
//    public boolean checkFileExists(String md5) {
//        // 先从Redis中查询
//        String url = null;
////        String url = (String) redisTemplate.boundHashOps(MD5_KEY).get(md5);
//        // 文件不存在
//        if (StrUtil.isEmpty(url)) {
//            return false;
//        } else {
//            // 文件已经存在了
//            return true;
//        }
//    }
//
//    public void upload(String md5, MultipartFile file, String fileName, Integer index) {
//        // 上传过程中出现异常
//        Assert.notNull(file, "文件上传异常=>文件不能为空!");
//        // 创建文件桶
//        minioTemplate.makeBucket(md5);
//        String objectName = String.valueOf(index);
//        // 上传文件
//        try {
//            minioTemplate.putChunkObject(file.getInputStream(), md5, objectName);
//        } catch (IOException e) {
//           throw new RuntimeException(e.getMessage());
//        }
//        // 设置上传分片的状态
//    }
//
//    public void merge(MergeInfo mergeInfo) {
//        Assert.notNull(mergeInfo, "mergeInfo不能为空!");
//        String md5 = mergeInfo.getMd5();
//        String fileType = mergeInfo.getFileType();
//        try {
//            // 开始合并请求
//            String targetBucketName = minioConfig.getMinIo().getBucket();
//            String fileNameWithoutExtension = UUID.randomUUID().toString();
//            String objectName = fileNameWithoutExtension + "." + fileType;
//            // 合并文件
//            minioTemplate.composeObject(md5, targetBucketName, objectName);
//            log.info("桶：{} 中的分片文件，已经在桶：{},文件 {} 合并成功", md5, targetBucketName, objectName);
//
//            // 合并成功之后删除对应的临时桶
//            minioTemplate.removeBucket(md5, true);
//            log.info("删除桶 {} 成功", md5);
//
//            // 表示是同一个文件, 且文件后缀名没有被修改过
//            String url = minioTemplate.getPresignedObjectUrl(targetBucketName, objectName);
//
//            // 存入redis中
////            redisTemplate.boundHashOps(MD5_KEY).put(md5, url);
//
//
//        } catch (Exception e) {
//            log.error("文件合并执行异常 => ", e);
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//}
