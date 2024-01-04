package top.anets.file.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.anets.file.dao.FileMapper;
import top.anets.file.entity.entity.File;
import top.anets.file.entity.vo.param.FileUploadVO;
import top.anets.file.entity.vo.result.FileInfo;
import top.anets.file.entity.vo.result.FileResultVO;
import top.anets.file.service.FileService;
import top.anets.file.strategy.FileContext;
import top.anets.file.utils.ArgumentAssert;
import top.anets.file.utils.BeanPlusUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务实现类
 * 增量文件上传日志
 * </p>
 *
 * @author tangyh
 * @date 2021-06-30
 * @create [2021-06-30] [tangyh] [初始创建]
 */
@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper,File> implements FileService {

    @Autowired
    private FileMapper fileMapper;
    @Override
    public Boolean upLoadFile(FileInfo fileInfo,String filename, long parentId, String userId) {
        File file = new File();
        BeanUtils.copyProperties(fileInfo,file );
        file.setParentId(parentId);
        file.setFidUid(userId);
        file.setFname(filename);
        String extName = file.getSuffix();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //to judge the file type
        System.out.println("suffix"+extName);
        Integer fidCid;
        if("gif,jpg,bmp,png,psd,ico".indexOf(extName)!=-1){
            fidCid=3;
        }else if("rar,7z,zip".indexOf(extName)!=-1){
            fidCid=7;
        }else if("exe,apk".indexOf(extName)!=-1){
            fidCid=6;
        }else if("avi,rmvb,3gp,flv,mp4".indexOf(extName)!=-1){
            fidCid=1;
        }else if("mp3,wav,krc,lrc".indexOf(extName)!=-1){
            fidCid=2;
        }else if("doc,excel,ppt,pptx,pdf,chm,txt,md,docx,xls,xlsx,html,css,js,java".indexOf(extName)!=-1){
            fidCid=5;
        }else if("torrent".indexOf(extName)!=-1){
            fidCid=4;
        }else{
            fidCid=8;
        }

        file.setFidCid(fidCid);
        fileMapper.insert(file);
        return true;
    }
    @Override
    public List<File> getUserFiles(String uid, long parentId) {
        LambdaQueryWrapper<File> example = Wrappers.<File>lambdaQuery().eq(File::getParentId, parentId).eq(File::getFidUid, uid);
        List<File> list = fileMapper.selectList(example);
        return list;
    }
    @Override
    public List<File> getRepeatFileByFname(long parentId, String userId, String fname) {
        LambdaQueryWrapper<File> example = Wrappers.<File>lambdaQuery().eq(File::getParentId, parentId).eq(File::getFidUid, userId).eq(File::getFname,fname ).eq(File::getIsDir, 0);
        List<File> list = fileMapper.selectList(example);
        return list;
    }
    @Override
    public void deleteFile(long fid) {
        fileMapper.deleteById(fid);

    }
    @Override
    public long saveDir(long parentId, String userId, String fname) {
        File file = new File();
        file.setParentId(parentId);
        file.setFidUid(userId);
        file.setOriginalFileName(fname);
        file.setFname(fname);
        file.setFidCid(9);
        file.setIsDir(1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = format.format(new Date());
        file.setUpdateTime(LocalDateTime.now());
        fileMapper.insert(file);
        System.out.println("==================返回的主键:"+file.getFid());
        return file.getFid();
    }
    @Override
    public List<File> getRepeatDirFname(long parentId, String userId, String fname) {
        LambdaQueryWrapper<File> example = Wrappers.<File>lambdaQuery().eq(File::getParentId, parentId).eq(File::getFidUid, userId).eq(File::getFname,fname ).eq(File::getIsDir, 1);
        List<File> list = fileMapper.selectList(example);
        return list;

    }
    @Override
    public File getFileByFid(long fid) {
        File file = fileMapper.selectById(fid);
        return file;
    }
    @Override
    public List<File> getChildren(long parentId, String userId) {
        LambdaQueryWrapper<File> example = Wrappers.<File>lambdaQuery().eq(File::getParentId, parentId).eq(File::getFidUid, userId);
        List<File> list = fileMapper.selectList(example);
        return list;
    }
    @Override
    public List<File> getClassifyedFiles(String userId, Integer fidCid) {
        LambdaQueryWrapper<File> example = Wrappers.<File>lambdaQuery().eq(File::getFidUid, userId).eq(File::getFidCid,fidCid );
        return fileMapper.selectList(example);
    }
    @Override
    public List<File> getFilesByKey(String userId, String key) {
        LambdaQueryWrapper<File> example = Wrappers.<File>lambdaQuery().eq(File::getFidUid, userId).like(File::getFname,"%"+key+"%" );
        return fileMapper.selectList(example);
    }
    @Override
    public void updateShare(Long fid, String address, String password) {
        File file = fileMapper.selectById(fid);
        file.setIsPublic(1);
        file.setIsShare(1);
        file.setShareAddress(address);
        file.setSharePassword(password);
        fileMapper.updateById(file);
    }
    @Override
    public File getFileByAddress(String address) {
        LambdaQueryWrapper<File> example = Wrappers.<File>lambdaQuery().eq(File::getShareAddress, address);
        File file = fileMapper.selectOne(example);
        if(file!=null){
            file.setSharePassword(null);
            file.setPath(null);
        }
        return file;
    }
    @Override
    public Long upLoadFileAndReturnFid(FileInfo fileInfo,String filename, long parentId,
                                       String userId) {
        File file = new File();
        BeanUtils.copyProperties(fileInfo,file );
        file.setParentId(parentId);
        file.setFidUid(userId);
        file.setFname(filename);
        String extName = file.getSuffix();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //to judge the file type
        System.out.println("suffix"+extName);
        Integer fidCid;
        if("gif,jpg,bmp,png,psd,ico".indexOf(extName)!=-1){
            fidCid=3;
        }else if("rar,7z,zip".indexOf(extName)!=-1){
            fidCid=7;
        }else if("exe,apk".indexOf(extName)!=-1){
            fidCid=6;
        }else if("avi,rmvb,3gp,flv,mp4".indexOf(extName)!=-1){
            fidCid=1;
        }else if("mp3,wav,krc,lrc".indexOf(extName)!=-1){
            fidCid=2;
        }else if("doc,excel,ppt,pptx,pdf,chm,txt,md,docx,xls,xlsx,html,css,js,java".indexOf(extName)!=-1){
            fidCid=5;
        }else if("torrent".indexOf(extName)!=-1){
            fidCid=4;
        }else{
            fidCid=8;
        }
        file.setFidCid(fidCid);
        fileMapper.insert(file);
        return file.getFid();
    }
}
