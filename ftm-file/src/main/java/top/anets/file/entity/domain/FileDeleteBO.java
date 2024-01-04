package top.anets.file.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.anets.file.entity.enumeration.FileStorageType;

/**
 * 文件删除
 *
 * @author zuihou
 * @date 2019/05/07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDeleteBO {
    /**
     * 桶
     */
    private String bucket;
    /**
     * 相对路径
     */
    private String path;
    /**
     * 存储类型
     */
    private FileStorageType storageType;
}
