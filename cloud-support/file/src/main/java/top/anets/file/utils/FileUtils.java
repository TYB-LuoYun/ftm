package top.anets.file.utils;

import org.apache.commons.lang3.StringUtils;
import top.anets.file.model.common.StrPool;
import top.anets.file.model.vo.result.FileInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.UUID;

import static top.anets.file.utils.DateUtils.SLASH_DATE_FORMAT;

/**
 * @author ftm
 * @date 2024-01-10 17:14
 */
public class FileUtils {
    /**
     * 企业/年/月/日/业务类型/唯一文件名
     */
    public static String getPath(String bizType, String originalFilename) {
        if(StringUtils.isBlank(bizType)){
            bizType = "ipfs";
        }
        return new StringJoiner(StrPool.SLASH).add(String.valueOf(ContextUtil.getTenant()))
                .add(bizType).add(getType(originalFilename.substring(originalFilename.lastIndexOf(".")+1))).add(getDateFolder()).add(originalFilename).toString();
    }

    public static String getUniqueFileName(FileInfo file) {
        return new StringJoiner(StrPool.DOT)
                .add(UUID.randomUUID().toString().replace("-", ""))
                .add(file.getSuffix()).toString();
    }
    /**
     * 获取年月日 2020/09/01
     *
     * @return
     */
    public static String getDateFolder() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(SLASH_DATE_FORMAT));
    }

    //  根据文件扩展名判断类型
    public static String getType(String extName){
        String fidCid =null;
        if("gif,jpg,bmp,png,psd,ico".indexOf(extName)!=-1){
            fidCid= Type.IMG.getDir();
        }else if("rar,7z,zip".indexOf(extName)!=-1){
            fidCid= Type.ZIP.getDir();
        }else if("exe,apk".indexOf(extName)!=-1){
            fidCid= Type.EXE.getDir();
        }else if("avi,rmvb,3gp,flv,mp4".indexOf(extName)!=-1){
            fidCid= Type.VIDEO.getDir();
        }else if("mp3,wav,krc,lrc".indexOf(extName)!=-1){
            fidCid= Type.AUDIO.getDir();
        }else if("doc,excel,ppt,pptx,pdf,chm,txt,md,docx,xls,xlsx,html,css,js,java".indexOf(extName)!=-1){
            fidCid= Type.OTHERS.getDir();
        }else{
            fidCid= Type.OTHERS.getDir();
        }
        return fidCid;
    }



    public enum Type {
        PDF("PDF", "pdf", 6),
        IMG("图片", "img", 7),
        DOCX("文档", "docx", 10),
        ZIP("压缩包", "zip", 20),
        EXE("可执行文件", "exe", 21),
        VIDEO("视频", "video", 22),
        AUDIO("音频", "audio", 23),
        OTHERS("其它", "other", 50);
        private String name;
        private String dir;
        private Integer value;
        Type(String name,String dir , int value) {
            this.dir = dir;
            this.name = name;
            this.value = value;
        }

        public String getDir() {
            return this.dir;
        }
        public String getName() {
            return this.name;
        }
        public Integer getValue() {
            return this.value;
        }

        public static String getDir(Integer type){
            Type[] values = Type.values();
            for (Type item:values) {
                if(item.getValue().equals(type)){
                    return  item.getDir();
                }
            }
            return "all";
        }
    }


}
