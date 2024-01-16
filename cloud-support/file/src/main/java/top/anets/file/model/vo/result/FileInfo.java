package top.anets.file.model.vo.result;

import lombok.Builder;
import lombok.Data;
import top.anets.file.model.enumeration.FileStorageType;
import top.anets.file.model.enumeration.FileType;

/**
 * @author ftm
 * @date 2023-12-26 15:28
 */
@Data
@Builder
public class FileInfo {
    /**
     * 业务类型
     */

    private String bizType;

    /**
     * 文件类型
     */

    private FileType fileType;

    /**
     * 存储类型
     * LOCAL FAST_DFS MIN_IO ALI
     */

    private FileStorageType storageType;

    /**
     * 桶
     */

    private String bucket;

    /**
     * 文件相对地址
     */
    private String path;

    /**
     * 文件访问地址
     */

    private String url;

    /**
     * 唯一文件名
     */

    private String uniqueFileName;

//    /**
//     * 文件md5
//     */
//
//    private String fileMd5;

    //原始文件名
    private String originalFileName;


    private String contentType;


    private String suffix;

    private Long size;
}
