package top.anets.file.strategy;


import org.springframework.web.multipart.MultipartFile;
import top.anets.file.model.chunk.*;
import top.anets.file.model.entity.File;
import top.anets.file.utils.R;

/**
 * 文件分片处理策略类
 *
 * @author zuihou
 * @date 2019/06/19
 */
public interface FileChunkStrategy {

    /**
     * 初始化上传
     * 1.MD5校验，如果已经上传则秒传
     *
     * @return 附件
     */
    FileChunkInitDTO initUploadChunk(FileChunkInitRq fileChunkInitRq);


    /**
     * 上传分片
     * @param file
     * @param chunkSize  分片大小
     * @param chunkPosition 第几片
     * @param uploadId 唯一id
     * @param bucketName
     * @param objectName
     */
    FileChunkUploadRes uploadPart(MultipartFile file, Long chunkSize, Integer totalChunks, Integer  chunkPosition, String uploadId, String bucketName, String objectName);



//    /**
//     * 合并文件
//     *
//     * @param merge 合并参数
//     * @return 附件
//     */
//    R<File> chunksMerge(FileChunksMergeDTO merge);
//
//
////=============================================================================================================
//
//    /**
//     *
//     * @param file
//     * @param chunkSize  分片大小
//     * @param chunkNumber  第几片
//     * @param path 路径
//     * @param identifier 唯一标识(md5)
//     * @param filename 原文件名
//     */
//    void uploadChunk(MultipartFile file,  Integer chunkNumber ,String identifier );
//
//
//    File md5Check(String md5, Long userId);
}

