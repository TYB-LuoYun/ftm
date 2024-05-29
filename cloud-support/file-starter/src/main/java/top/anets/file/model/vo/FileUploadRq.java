package top.anets.file.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.anets.file.model.enumeration.FileStorageType;

import javax.validation.constraints.NotBlank;

/**
 * @author ftm
 * @date 2023-12-26 16:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadRq {
    private String bizType = "ipfs";

    @ApiModelProperty(value = "桶")
    private String bucket = null;

    @ApiModelProperty(value = "存储类型")
    private FileStorageType storageType = FileStorageType.LOCAL;


    private Boolean dbRecord = true;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 上传用户
     */
    private String userId = "-1";

    /**
     * 上传文件夹需要
     */
    private String webkitRelativePath;

    /**
     * 指定相对目录
     */
    private String path;
}
