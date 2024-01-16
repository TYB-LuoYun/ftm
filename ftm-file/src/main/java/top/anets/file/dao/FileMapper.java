package top.anets.file.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.anets.file.model.entity.File;

/**
 * <p>
 * Mapper 接口
 * 增量文件上传日志
 * </p>
 *
 * @author tangyh
 * @date 2021-06-30
 * @create [2021-06-30] [tangyh] [初始创建]
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

}
