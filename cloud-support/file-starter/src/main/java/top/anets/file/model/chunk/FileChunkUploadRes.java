package top.anets.file.model.chunk;

import lombok.Builder;
import lombok.Data;

/**
 * @author ftm
 * @date 2024-03-05 11:06
 */
@Data
@Builder
public class FileChunkUploadRes {
    private Integer successChunkSize;
    private Integer totalChunks;
}
