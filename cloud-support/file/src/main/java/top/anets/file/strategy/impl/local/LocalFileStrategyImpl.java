package top.anets.file.strategy.impl.local;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.anets.file.model.common.StrPool;
import top.anets.file.model.domain.FileDeleteBO;
import top.anets.file.model.domain.FileGetUrlBO;
import top.anets.file.model.enumeration.FileStorageType;
import top.anets.file.model.vo.param.FileUploadVO;
import top.anets.file.model.vo.result.FileInfo;
import top.anets.file.properties.FileServerProperties;
import top.anets.file.strategy.impl.AbstractFileStrategy;
import top.anets.file.utils.CollHelper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zuihou
 * @date 2020/11/22 5:00 下午
 */
@Slf4j

@Component("LOCAL")
public class LocalFileStrategyImpl extends AbstractFileStrategy {
    public LocalFileStrategyImpl(FileServerProperties fileProperties ) {
        super(fileProperties );
    }

    @Override
    protected void uploadFile(FileInfo file, MultipartFile multipartFile,  FileUploadVO fileUploadVO) throws Exception {
        String bucket = fileUploadVO.getBucket();
        FileServerProperties.Local local = fileProperties.getLocal();
        bucket = StrUtil.isEmpty(bucket) ? local.getBucket() : bucket;

        //生成文件名
        String uniqueFileName = getUniqueFileName(file);
        // 相对路径
        String path = getPath(file.getBizType(), uniqueFileName);
        // web服务器存放的绝对路径
        String absolutePath = Paths.get(local.getStoragePath(), bucket, path).toString();

        // 存储文件
        java.io.File outFile = new java.io.File(absolutePath);
        FileUtils.writeByteArrayToFile(outFile, multipartFile.getBytes());

        // 返回数据
        String url = local.getUrlPrefix() + bucket + StrPool.SLASH + path;
        file.setUrl(url);
        file.setUniqueFileName(uniqueFileName);
        file.setPath(path);
        file.setBucket(bucket);
        file.setStorageType(FileStorageType.LOCAL);
    }

    @Override
    public boolean delete(FileDeleteBO file) {
        FileServerProperties.Local local = fileProperties.getLocal();
        java.io.File ioFile = new java.io.File(Paths.get(local.getStoragePath(), file.getBucket(), file.getPath()).toString());
        FileUtils.deleteQuietly(ioFile);
        return true;
    }

    @Override
    public Map<String, String> findUrl(List<FileGetUrlBO> fileGets) {
        Map<String, String> map = new LinkedHashMap<>(CollHelper.initialCapacity(fileGets.size()));
        // 方式1 取上传时存的url （多查询一次数据库）
//        List<String> paths = fileGets.stream().map(FileGetUrlBO::getPath).collect(Collectors.toList());
//        List<File> list = fileMapper.selectList(Wraps.<File>lbQ().eq(File::getPath, paths));
//        list.forEach(item -> map.put(item.getPath(), item.getUrl()));

        // 方式2 重新拼接 （urlPrefix 可能跟上传时不一样）
        FileServerProperties.Local local = fileProperties.getLocal();
        fileGets.forEach(item -> {
            StringBuilder url = new StringBuilder(local.getUrlPrefix())
                    .append(item.getBucket())
                    .append(StrPool.SLASH)
                    .append(item.getPath());
            map.put(item.getPath(), url.toString());
        });
        return map;
    }

    @Override
    public void fetch(HttpServletResponse response, String bucket, String path) {
        BufferedInputStream reader = null;
        ServletOutputStream outputStream = null;
        try {

            FileServerProperties.Local local = fileProperties.getLocal();
            bucket = StrUtil.isEmpty(bucket) ? local.getBucket() : bucket;
            String FILE_PATH = Paths.get(local.getStoragePath(), bucket, path).toString();
            java.io.File file = new File(FILE_PATH );
            System.out.println("目录地址："+file);
            outputStream = response.getOutputStream();

            long start = System.currentTimeMillis();
            reader = new BufferedInputStream(new FileInputStream(file));
            double begin=(double)reader.available();//获取可用字节

            //byte[]数组的大小，根据复制文件的大小可以调整，1G一下可以5M。1G以上150M，自己多试试
            byte[] b=new byte[1024*5];
            int len=0;
            String progress=null;
//        PrintProgressBar printProgressBar = new PrintProgressBar(reader.available()) ;
//        ConsoleProgressBarDemo cpb = new ConsoleProgressBarDemo(100, '░');
            while((len=reader.read(b))!=-1){
                outputStream.write(b,0,len);
                outputStream.flush();
                //显示进度
                if(!String.format("%.2f",(1-reader.available()/begin)*100).equals(progress)){
                    progress=String.format("%.2f",(1-reader.available()/begin)*100);
//                System.out.println("progress:"+progress+"%");
//                cpb.show((int) ((1-reader.available()/begin)*100));
                }
            }
//        cpb.show((int) ((1-0)*100));

            long end = System.currentTimeMillis();
            System.out.println("time consuming:"+(end-start)+"ms");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
