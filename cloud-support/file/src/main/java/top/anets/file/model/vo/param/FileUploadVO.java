package top.anets.file.model.vo.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.anets.file.model.enumeration.FileStorageType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 附件上传
 *
 * @author zuihou
 * @date 2019-05-12 18:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "FileUploadVO", description = "附件上传")
public class FileUploadVO implements Serializable {

    @ApiModelProperty(value = "业务类型")
    @NotBlank(message = "请填写业务类型")
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


    @ApiModelProperty(value = "存储类型")
    private FileStorageType storageType;



}
