package top.anets.file.model.chunk;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ftm
 * @date 2024-03-05 14:00
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileChunkInitRq {
    private String fileName;
    private Long size;
    private String contentType;
    /**
     * 自定义业务类型，可不填
     */
    private String bizType;
    /**
     * 自定义桶，可不填
     */
    @ApiModelProperty(value = "桶")
    private String bucket;
    /**
     * 自定义路径，可不填
     */
    private String path;
}
