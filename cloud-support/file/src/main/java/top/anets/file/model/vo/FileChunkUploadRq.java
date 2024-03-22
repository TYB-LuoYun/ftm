package top.anets.file.model.vo;

import lombok.Data;

/**
 * @author ftm
 * @date 2023-12-28 9:59
 */
@Data
public class FileChunkUploadRq extends FileUploadRq{
    /**
     * 第几片
     */
    Integer chunkNumber;
    /**
     * 每片大小
     */
    Long chunkSize;

    String uploadId;

    String bucketName;

    String objectName;

    Integer totalChunks;
}
