package top.anets.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.anets.file.model.entity.File;
import top.anets.file.model.vo.param.FileUploadVO;
import top.anets.file.model.vo.result.FileInfo;
import top.anets.file.model.vo.result.FileResultVO;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 增量文件上传日志
 * </p>
 *
 * @author tangyh
 * @date 2021-06-30
 * @create [2021-06-30] [tangyh] [初始创建]
 */
public interface FileService  extends IService<File> {
    public Boolean upLoadFile(FileInfo fileInfo,String filename, long parentId, String userId);
    public Long upLoadFileAndReturnFid(FileInfo fileInfo,String filename,long parentId,String userId);
    public List<File> getUserFiles(String uid , long parentId);


    public File getFileByFid(long fid);

    public List<File> getRepeatFileByFname(long parentId,String userId,String fname);


    public void deleteFile(long fid);


    public long saveDir(long parentId,String userId,String fname);


    public List<File> getRepeatDirFname(long parentId,String userId,String fname);


    public List<File> getChildren(long parentId,String userId);



    public List<File> getClassifyedFiles(String userId,Integer fidCid);


    public List<File> getFilesByKey(String userId,String key);

    public void updateShare(Long fid, String address, String password);

    public File getFileByAddress(String address);

    long mkdirs(Long parentId,String userId, String path);
}
