package top.anets.file.strategy;

import org.springframework.web.multipart.MultipartFile;
import top.anets.file.entity.domain.FileDeleteBO;
import top.anets.file.entity.domain.FileGetUrlBO;
import top.anets.file.entity.entity.File;
import top.anets.file.entity.vo.result.FileInfo;


import java.util.List;
import java.util.Map;

/**
 * 文件策略接口
 *
 * @author zuihou
 * @date 2019/06/17
 */
public interface FileStrategy {
    /**
     * 文件上传
     *
     * @param file    文件
     * @param bucket  桶
     * @param bizType 业务类型
     * @return 文件对象
     */
    FileInfo upload(MultipartFile file, String bucket, String bizType);

    /**
     * 删除源文件
     *
     * @param fileDeleteBO 待删除文件
     * @return 是否成功
     */
    boolean delete(FileDeleteBO fileDeleteBO);

    /**
     * 根据路径获取访问地址
     *
     * @param fileGets 文件查询对象
     * @return
     */
    Map<String, String> findUrl(List<FileGetUrlBO> fileGets);

    /**
     * 根据路径获取访问地址
     *
     * @param fileGet 文件查询对象
     * @return
     */
    String getUrl(FileGetUrlBO fileGet);
}
