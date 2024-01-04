package top.anets.file.entity.vo;

import lombok.Data;

/**
 * @author ftm
 * @date 2023-12-28 9:59
 */
@Data
public class FileChunkUploadRq extends FileUploadRq{
//    String filename;

    /**
     * 第几片
     */
    Integer chunkNumber;
    /**
     * 每片大小
     */
    Long chunkSize;
    /**
     * 分片总数量
     */
    Integer totalChunks;

    /**
     * 总大小
     */
    String totalSize;
    /**
     *
     */
    String identifier;

    String filename;
}
