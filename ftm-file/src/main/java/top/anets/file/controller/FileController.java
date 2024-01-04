package top.anets.file.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.anets.common.utils.exception.ServiceException;
import top.anets.file.entity.common.ShortUrlUtils;
import top.anets.file.entity.domain.FileDeleteBO;
import top.anets.file.entity.entity.File;
import top.anets.file.entity.enumeration.FileStorageType;
import top.anets.file.entity.vo.FileChunkUploadRq;
import top.anets.file.entity.vo.FileUploadRq;
import top.anets.file.entity.vo.param.FileUploadVO;
import top.anets.file.entity.vo.result.FileInfo;
import top.anets.file.service.FileService;
import top.anets.file.strategy.FileChunkStrategy;
import top.anets.file.strategy.FileContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author ftm
 * @date 2023-12-22 13:51
 */
@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private FileContext fileContext;

    @Autowired
    @Qualifier("MIN_IO_FileChunk")
    FileChunkStrategy fileChunkStrategy ;

    /**
     * 分片上传
     */
   @RequestMapping("/file/fileChunkUpload")
   public void  fileChunkUpload(FileChunkUploadRq rq, MultipartFile file){
//      this.uploadFileByRandomAccessFile("D:\\work\\project\\检查互认\\a"+rq.getFilename(), rq.getChunkSize(), rq.getChunkNumber(), file);
//       FileChunkStrategy fileChunkStrategy = fileContext.getFileChunkStrategy(FileStorageType.MIN_IO);
       fileChunkStrategy.uploadChunk(file, rq.getChunkNumber() ,   rq.getIdentifier() );
   }


    private boolean uploadFileByRandomAccessFile(String resultFileName, Long chunkSize, Integer chunkNumber , MultipartFile file) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(resultFileName, "rw")) {
            // 分片大小必须和前端匹配，否则上传会导致文件损坏
            chunkSize = chunkSize == 0L ? 20 * 1024 * 102 : chunkSize;
            // 偏移量
            long offset = chunkSize * (chunkNumber - 1);
            // 定位到该分片的偏移量
            randomAccessFile.seek(offset);
            // 写入
            randomAccessFile.write(file.getBytes());
        } catch (IOException e) {
//            log.error("文件上传失败：" + e);
            return false;
        }
        return true;
    }








    @RequestMapping("/file/upload.action")
    public void upload(MultipartFile file, FileUploadRq upload){
        /**
         * 查看文件是否重复
         */
        //check if it is repeated
        List<File> list = fileService.getRepeatFileByFname(upload.getParentId(), upload.getUserId(), file.getOriginalFilename());


        FileUploadVO fileUploadVO = new FileUploadVO();
        BeanUtils.copyProperties(upload,fileUploadVO);
        if(list!=null&&list.size()>0){
            for (File filer : list) {
                //delete repeat files
                System.out.println("repeated files,deleting...:"+filer.getFid());
                FileDeleteBO deleteBO = new FileDeleteBO();
                BeanUtils.copyProperties(filer,deleteBO );
                fileContext.delete(deleteBO);
                //delete data in database
                fileService.deleteFile(filer.getFid());
            }
            //replace
            FileInfo fileInfo = fileContext.upload(file, fileUploadVO);
            fileService.upLoadFile(fileInfo ,file.getOriginalFilename(),upload.getParentId(), upload.getUserId());
        }else {
            FileInfo fileInfo = fileContext.upload(file, fileUploadVO);
            fileService.upLoadFile(fileInfo ,file.getOriginalFilename(),upload.getParentId(), upload.getUserId());
        }

    }


    @RequestMapping("/file/uploadDir.action")
    public void fileUploadDir(MultipartFile file, HttpServletRequest request, FileUploadRq upload){


        try {
            String  originalFilename= file.getOriginalFilename();


            long parentId = upload.getParentId();
            String userId = upload.getUserId();
            String path = upload.getWebkitRelativePath();

            System.out.println("filename:"+originalFilename);
            System.out.println("path:"+path);

            String nextpath=path;
            //解析路径
            while(nextpath.indexOf("/")!=-1){
                String dir =nextpath.substring(0, nextpath.indexOf("/"));
//		    	Check if  it is repeated
                List<File> repeatedDirs = fileService.getRepeatDirFname(parentId, userId,dir);
                if(repeatedDirs!=null&&repeatedDirs.size()>0){
                    parentId=repeatedDirs.get(0).getFid();
                }else{
                    //		    	create directory
                    parentId = fileService.saveDir(parentId, userId, dir);
                }
                nextpath= nextpath.substring(nextpath.indexOf("/")+1);
            }

            String filename = nextpath;
            //文件上传
            //check if it is repeated
            List<File> list = fileService.getRepeatFileByFname(parentId, userId, filename);
            FileUploadVO fileUploadVO = new FileUploadVO();
            BeanUtils.copyProperties(upload,fileUploadVO);
            if(list!=null&&list.size()>0){
                for (File filer : list) {
                    //delete repeat files
                    System.out.println("repeated files,deleting...:"+filer.getFid());
                    FileDeleteBO deleteBO = new FileDeleteBO();
                    BeanUtils.copyProperties(filer,deleteBO );
                    fileContext.delete(deleteBO);
                    //delete data in database
                    fileService.deleteFile(filer.getFid());
                }
                //replace
                FileInfo fileInfo = fileContext.upload(file, fileUploadVO);
                fileService.upLoadFile(fileInfo ,filename ,parentId,userId);

            }else {
                FileInfo fileInfo = fileContext.upload(file, fileUploadVO);
                fileService.upLoadFile(fileInfo,filename ,parentId,userId);
            }


        } catch (Exception e) {
          throw new ServiceException(e.getMessage());
        }
    }



    @RequestMapping("/file/list.action")
    public List<File> getUserFiles(String uid,long parentId){
        List<File> list =  fileService.getUserFiles(uid, parentId);
        return   list ;
    }

    @RequestMapping("/file/delete.action")
    public void deleteFile(long fid){
        File file= fileService.getFileByFid(fid);
        if(file!=null){
            if(!StringUtils.isNoneBlank(file.getPath())){
                fileService.deleteFile(fid);
                return  ;
            }
            FileDeleteBO deleteBO = new FileDeleteBO();
            BeanUtils.copyProperties(file,deleteBO );
            fileContext.delete(deleteBO);
            fileService.deleteFile(fid);

        }

    }
    @RequestMapping("/dir/delete.action")
    public void deleteDir(long fid,String userId) throws Exception {
        //get in floder
            List<File> list = fileService.getChildren(fid,userId);
            //递归删除文件夹里面的东西
            this.Recursive(list, userId );
            //删除完后，就可以删除本身了
            fileService.deleteFile(fid);

    }

    //递归删除文件夹
    private void Recursive(List<File> list,String userId) throws Exception{
        for (File file : list) {
            //对于每次循环 ,比如荡当前有   dir  rys.txt
            if(file.getIsDir()==0){
                //是文件，删除文件
                FileDeleteBO deleteBO = new FileDeleteBO();
                BeanUtils.copyProperties(file,deleteBO );
                fileContext.delete(deleteBO);
                fileService.deleteFile(file.getFid());
            }else{
                list = fileService.getChildren(file.getFid(),userId);
                //进行递归
                this.Recursive(list, userId );
                //文件夹里面的递归完了，就删除文件夹
                fileService.deleteFile(file.getFid());
            }
        }
    }


    @RequestMapping(value="/file/query.action")
    public List<File> queryFiles(String key,String userId,Integer fidCid){
        if(userId==null){
            throw  new ServiceException("500", "没有用户id" );
        }
        if(StringUtils.isNoneBlank(key)){
            try {
                key=new String(key.getBytes("iso-8859-1"),"utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            List<File> list = fileService.getFilesByKey(userId, key);
            return  list ;
        }
        if(fidCid!=null){
            List<File> list = fileService.getClassifyedFiles(userId, fidCid);
            return   list ;
        }
        List<File> files = fileService.getUserFiles(userId, 0);
        return  files ;

    }

    //生成分享地址密码
    @RequestMapping("/file/share.action")
    public Map share(Long fid, Integer uid){
        String address=fid+""+uid;
        String[] urls = ShortUrlUtils.toShortUrl(address);
        Random random = new Random();
        int index = random.nextInt(4);
        address=urls[index];
        if(index==0){
            index=random.nextInt(3)+1;
        }else if(index==3){
            index=random.nextInt(3);
        }else{
            index=index+1;
        }
        String password=urls[index].substring(0, 4);


        HashMap<String, String> map = new HashMap<>();

        fileService.updateShare(fid,address,password);

        map.put("address",address);
        map.put("password", password);

        return  map ;
    }

    //根据分享查询文件
    @RequestMapping("/file/getShareFile.action")
    public File getShareFile(HttpServletRequest request,String address){
        File file=fileService.getFileByAddress(address);
        if(file!=null&&request.getSession().getAttribute("fileAccess")!=null&&request.getSession().getAttribute("fileAccess").equals(file.getFid()+"")){
            //说明已经访问过
            File detailFile = fileService.getFileByFid(file.getFid());
            return detailFile;
        }else{
            return file;
        }

    }

    //根据分享密码查询文件详情
    @RequestMapping("/file/getShareDetailFile.action")
    public File getDetailFile(HttpServletRequest request,Integer fid,String password){
        File file=fileService.getFileByFid(fid);
        if(file!=null&&file.getSharePassword().equals(password)){
            HttpSession session = request.getSession();
            session.setAttribute("fileAccess",fid+"");
            return file;
        }else{
            return null;
        }

    }

    @RequestMapping("/file/space.action")
    public Map space() {
        java.io.File[] roots =java.io.File.listRoots();
        double constm = 1024 * 1024 * 1024 ;
        double total = 0d;
        double free = 0d;
        for (java.io.File file : roots) {
            System.out.println(file.getPath());
            System.out.println("剩余空间 = " + doubleFormat(file.getFreeSpace()/constm)+" G");
            System.out.println("已使用空间 = " + doubleFormat(file.getUsableSpace()/constm)+" G");
            System.out.println(file.getPath()+"盘总大小 = " + doubleFormat(file.getTotalSpace()/constm)+" G");
            System.out.println();
            total += file.getTotalSpace();
            free+=file.getFreeSpace();
        }
        System.out.println("你的硬盘总大小 = "+doubleFormat(total/constm));
        System.out.println("剩余空间 = "+doubleFormat(free/constm));


        HashMap<String, String> map = new HashMap<>();
        map.put("total", doubleFormat(total/constm));
        map.put("free", doubleFormat(free/constm));
        return  map ;
    }


    private static String doubleFormat(double d){
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(d);
    }
}
