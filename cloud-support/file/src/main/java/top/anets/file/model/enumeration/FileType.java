package top.anets.file.model.enumeration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

/**
 * @author ftm
 * @date 2023-12-18 15:33
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "FileType", description = "文件类型-枚举")
public enum FileType  {

    /**
     * IMAGE="图片"
     */
    IMAGE("图片"),
    /**
     * VIDEO="视频"
     */
    VIDEO("视频"),
    /**
     * AUDIO="音频"
     */
    AUDIO("音频"),
    /**
     * DOC="文档"
     */
    DOC("文档"),
    /**
     * OTHER="其他"
     */
    OTHER("其他"),
    ;

    @ApiModelProperty(value = "描述")
    private String desc;


    /**
     * 根据当前枚举的name匹配
     */
    public static FileType match(String val, FileType def) {
        return Stream.of(values()).parallel().filter(item -> item.name().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static FileType get(String val) {
        return match(val, null);
    }

//    public boolean eq(FileType val) {
//        return val != null && eq(val.name());
//    }

    public String getCode() {
        return this.name();
    }

}
