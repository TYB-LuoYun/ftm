package top.anets.file.model.chunk;

import lombok.Builder;
import lombok.Data;

/**
 * @author ftm
 * @date 2024-03-04 10:43
 */
@Data
@Builder
public class FileChunkInitDTO  {
    private String bucketName;
    private String objectName;
    private String uploadId;

    private String uniqueFileName;
    private String fileName;
    private Long size;
    private String contentType;
}
