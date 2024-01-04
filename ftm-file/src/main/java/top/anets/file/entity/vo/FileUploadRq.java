package top.anets.file.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.anets.file.entity.enumeration.FileStorageType;

import javax.validation.constraints.NotBlank;

/**
 * @author ftm
 * @date 2023-12-26 16:31
 */
@Data
public class FileUploadRq {
    private String bizType = "ipfs";

    @ApiModelProperty(value = "桶")
    private String bucket = null;

    @ApiModelProperty(value = "存储类型")
    private FileStorageType storageType = FileStorageType.LOCAL;


    /**
     * 父级ID
     */
    private long parentId= -1;

    /**
     * 上传用户
     */
    private String userId = "-1";

    /**
     * 上传文件夹需要
     */
    private String webkitRelativePath;
}
