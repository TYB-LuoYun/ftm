package top.anets.file.strategy.impl.ali;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.anets.file.dao.FileMapper;
import top.anets.file.entity.common.StrPool;
import top.anets.file.entity.domain.FileDeleteBO;
import top.anets.file.entity.domain.FileGetUrlBO;
import top.anets.file.entity.entity.File;
import top.anets.file.entity.enumeration.FileStorageType;
import top.anets.file.entity.vo.result.FileInfo;
import top.anets.file.properties.FileServerProperties;
import top.anets.file.strategy.impl.AbstractFileStrategy;
import top.anets.file.utils.CollHelper;

import java.net.URL;
import java.util.*;

/**
 * @author zuihou
 * @date 2020/11/22 4:57 下午
 */
@Slf4j

@Component("ALI_OSS")
public class AliFileStrategyImpl extends AbstractFileStrategy {
    public AliFileStrategyImpl(FileServerProperties fileProperties, FileMapper fileMapper) {
        super(fileProperties, fileMapper);
    }

    @Override
    protected void uploadFile(FileInfo file, MultipartFile multipartFile, String bucket) throws Exception {
        FileServerProperties.Ali ali = fileProperties.getAli();
        OSS ossClient = new OSSClientBuilder().build(ali.getEndpoint(), ali.getAccessKeyId(),
                ali.getAccessKeySecret());
        if (StrUtil.isEmpty(bucket)) {
            bucket = ali.getBucket();
        }
        if (!ossClient.doesBucketExist(bucket)) {
            ossClient.createBucket(bucket);
        }

        //生成文件名
        String uniqueFileName = getUniqueFileName(file);

        // 企业id/功能点/年/月/日/file
        String path = getPath(file.getBizType(), uniqueFileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentDisposition("attachment;fileName=" + file.getOriginalFileName());
        metadata.setContentType(file.getContentType());
        PutObjectRequest request = new PutObjectRequest(bucket, path, multipartFile.getInputStream(), metadata);
        PutObjectResult result = ossClient.putObject(request);

        log.info("result={}", JSON.toJSON(result));
        String url = ali.getUrlPrefix() + bucket + StrPool.SLASH + path;
        file.setUrl(url);
        file.setUniqueFileName(uniqueFileName);
        file.setBucket(bucket);
        file.setPath(path);
        file.setStorageType(FileStorageType.ALI_OSS);

        ossClient.shutdown();
    }

    @Override
    public boolean delete(FileDeleteBO file) {
        FileServerProperties.Ali ali = fileProperties.getAli();
        String bucketName = StrUtil.isEmpty(file.getBucket()) ? ali.getBucket() : file.getBucket();
        OSS ossClient = new OSSClientBuilder().build(ali.getEndpoint(), ali.getAccessKeyId(), ali.getAccessKeySecret());
        ossClient.deleteObject(bucketName, file.getPath());
        ossClient.shutdown();
        return true;
    }

    @Override
    public Map<String, String> findUrl(List<FileGetUrlBO> fileGets) {
        OSS ossClient = createOss();
        Set<String> publicBucket = fileProperties.getPublicBucket();
        FileServerProperties.Ali ali = fileProperties.getAli();
        Map<String, String> map = new LinkedHashMap<>(CollHelper.initialCapacity(fileGets.size()));


        for (FileGetUrlBO fileGet : fileGets) {
            String bucket = StrUtil.isEmpty(fileGet.getBucket()) ? ali.getBucket() : fileGet.getBucket();
            try {
                if (CollUtil.isNotEmpty(publicBucket) && publicBucket.contains(bucket)) {
                    StringBuilder url = new StringBuilder(ali.getUrlPrefix())
                            .append(fileGet.getBucket())
                            .append(StrPool.SLASH)
                            .append(fileGet.getPath());
                    map.put(fileGet.getPath(), url.toString());
                } else {
                    map.put(fileGet.getPath(), generatePresignedUrl(bucket, fileGet.getPath()));
                }
            } catch (Exception e) {
                log.warn("加载文件url地址失败，请确保yml中第三方存储参数配置正确. bucket={}, , 文件名={} path={}", bucket, fileGet.getOriginalFileName(), fileGet.getPath(), e);
                map.put(fileGet.getPath(), StrPool.EMPTY);
            }
        }
        ossClient.shutdown();
        return map;
    }

    /**
     * 获取一个ossclient
     *
     * @return
     */
    public OSS createOss() {
        FileServerProperties.Ali ali = fileProperties.getAli();
        String accessKeyId = ali.getAccessKeyId();
        String accessKeySecret = ali.getAccessKeySecret();
        String endPoint = ali.getEndpoint();
        return new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
    }

    /**
     * 获取有访问权限的路径地址
     *
     * @param bucketName
     * @param key
     * @return
     */
    private String generatePresignedUrl(String bucketName, String key) {
        FileServerProperties.Ali ali = fileProperties.getAli();
        OSS oss = createOss();
        Date date = new Date(System.currentTimeMillis() + ali.getExpiry());
        URL url = oss.generatePresignedUrl(bucketName, key, date);
        return url.toString();
    }
}
