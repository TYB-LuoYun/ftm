package top.anets.system.mapper;

import top.anets.system.entity.UserOrgDept;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
    * mapper接口
    * </p>
 *
 * @author ftm
 * @since 2022-09-07
 */
public interface UserOrgDeptMapper extends BaseMapper<UserOrgDept> {
     IPage pagesAssociate(IPage page, @Param("param") Map<String, Object> params);


}