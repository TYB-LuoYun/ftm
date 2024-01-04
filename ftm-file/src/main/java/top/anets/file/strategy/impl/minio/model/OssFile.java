package top.anets.file.strategy.impl.minio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ftm
 * @date 2024-01-03 17:41
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OssFile {
    /**
     * OSS 存储时文件路径
     */
    private String ossFilePath;
    /**
     * 原始文件名
     */
    private String originalFileName;
}