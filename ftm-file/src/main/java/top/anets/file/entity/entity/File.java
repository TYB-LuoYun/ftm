package top.anets.file.entity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import top.anets.file.entity.enumeration.FileStorageType;
import top.anets.file.entity.enumeration.FileType;

import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;


/**
 * <p>
 * 实体类
 * 增量文件上传日志
 * </p>
 *
 *
 * -- ----------------------------
 * -- Table structure for c_file
 * -- ----------------------------
 * DROP TABLE IF EXISTS `c_file`;
 * CREATE TABLE `c_file` (
 *   `id` bigint(20) NOT NULL COMMENT 'ID',
 *   `biz_type` varchar(255) NOT NULL DEFAULT '' COMMENT '业务类型',
 *   `file_type` varchar(255) DEFAULT NULL COMMENT '文件类型',
 *   `storage_type` varchar(255) DEFAULT NULL COMMENT '存储类型\nLOCAL FAST_DFS MIN_IO ALI \n',
 *   `bucket` varchar(255) DEFAULT '' COMMENT '桶',
 *   `path` varchar(255) DEFAULT '' COMMENT '文件相对地址',
 *   `url` varchar(255) DEFAULT NULL COMMENT '文件访问地址',
 *   `unique_file_name` varchar(255) DEFAULT '' COMMENT '唯一文件名',
 *   `file_md5` varchar(255) DEFAULT NULL COMMENT '文件md5',
 *   `original_file_name` varchar(255) DEFAULT '' COMMENT '原始文件名',
 *   `content_type` varchar(255) DEFAULT '' COMMENT '文件类型',
 *   `suffix` varchar(255) DEFAULT '' COMMENT '后缀',
 *   `size` bigint(20) DEFAULT '0' COMMENT '大小',
 *   `create_time` datetime NOT NULL COMMENT '创建时间',
 *   `created_by` bigint(20) NOT NULL COMMENT '创建人',
 *   `update_time` datetime NOT NULL COMMENT '最后修改时间',
 *   `updated_by` bigint(20) NOT NULL COMMENT '最后修改人',
 *   PRIMARY KEY (`id`) USING BTREE
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='增量文件上传日志';
 * @author tangyh
 * @date 2021-06-30
 * @create [2021-06-30] [tangyh] [初始创建]
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("c_file")
@AllArgsConstructor
public class File   {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long fid;

    private String fname;

    /**
     * 业务类型
     */

    @TableField(value = "biz_type")
    private String bizType;

    /**
     * 文件类型
     */

    @TableField(value = "file_type", condition = LIKE)
    private FileType fileType;

    /**
     * 存储类型
     * LOCAL FAST_DFS MIN_IO ALI
     */

    @TableField(value = "storage_type", condition = LIKE)
    private FileStorageType storageType;

    /**
     * 桶
     */

    @TableField(value = "bucket", condition = LIKE)
    private String bucket;

    /**
     * 文件相对地址
     */

    @TableField(value = "path", condition = LIKE)
    private String path;

    /**
     * 文件访问地址
     */

    @TableField(value = "url", condition = LIKE)
    private String url;

    /**
     * 唯一文件名
     */

    @TableField(value = "unique_file_name", condition = LIKE)
    private String uniqueFileName;

    /**
     * 文件md5
     */

    @TableField(value = "file_md5", condition = LIKE)
    private String fileMd5;

    /**
     * 原始文件名
     */

    @TableField(value = "original_file_name", condition = LIKE)
    private String originalFileName;

    /**
     * 文件类型
     */

    @TableField(value = "content_type", condition = LIKE)
    private String contentType;

    /**
     * 后缀
     */
    @TableField(value = "suffix", condition = LIKE)
    private String suffix;

    /**
     * 大小
     */
    @TableField(value = "size")
    private Long size;




    private String fidUid;
    private  Integer fidCid;
    private Long parentId;
    private Integer isDir;
    private Integer isPublic;
    private Integer isShare;
    private String sharePassword;
    private String shareAddress;




    @TableField(value = "created_by")
    private Long createdBy;


    @TableField(value = "created_time")
    private LocalDateTime createTime;


    @TableField(value = "update_time")
    private LocalDateTime updateTime;


    @TableField(value = "update_by")
    private Long updatedBy;


    @Builder
    public File(Long id, LocalDateTime createTime, Long createdBy, LocalDateTime updateTime, Long updatedBy,
                String bizType, FileType fileType, FileStorageType storageType, String bucket,
                String path, String url, String uniqueFileName, String fileMd5, String originalFileName, String contentType,
                String suffix, Long size) {
//        this.id = id;
//        this.createTime = createTime;
//        this.createdBy = createdBy;
//        this.updateTime = updateTime;
//        this.updatedBy = updatedBy;
        this.bizType = bizType;
        this.fileType = fileType;
        this.storageType = storageType;
        this.bucket = bucket;
        this.path = path;
        this.url = url;
        this.uniqueFileName = uniqueFileName;
        this.fileMd5 = fileMd5;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.suffix = suffix;
        this.size = size;
    }

}
