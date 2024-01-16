package top.anets.file.strategy;


import org.springframework.web.multipart.MultipartFile;
import top.anets.file.model.chunk.FileChunksMergeDTO;
import top.anets.file.model.entity.File;
import top.anets.file.utils.R;

/**
 * 文件分片处理策略类
 *
 * @author zuihou
 * @date 2019/06/19
 */
public interface FileChunkStrategy {

    /**
     * 根据md5检测文件
     *
     * @param md5    md5
     * @param userId 用户id
     * @return 附件
     */
    File md5Check(String md5, Long userId);

    /**
     * 合并文件
     *
     * @param merge 合并参数
     * @return 附件
     */
    R<File> chunksMerge(FileChunksMergeDTO merge);


//=============================================================================================================

    /**
     *
     * @param file
     * @param chunkSize  分片大小
     * @param chunkNumber  第几片
     * @param path 路径
     * @param identifier 唯一标识(md5)
     * @param filename 原文件名
     */
    void uploadChunk(MultipartFile file,  Integer chunkNumber ,String identifier );



}

