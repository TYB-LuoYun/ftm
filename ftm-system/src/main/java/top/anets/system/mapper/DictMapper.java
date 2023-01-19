package top.anets.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.anets.system.entity.Dict;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ftm
 * @since 2022-08-12
 */
public interface DictMapper extends BaseMapper<Dict> {
    List<Dict> getChildren(@Param("parent") String parent);
}
