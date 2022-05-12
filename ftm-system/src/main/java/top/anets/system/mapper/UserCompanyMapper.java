package top.anets.system.mapper;

import top.anets.entity.system.UserCompany;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.anets.vo.system.UserCompanyCondition;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
public interface UserCompanyMapper extends BaseMapper<UserCompany> {

    List<UserCompany> querys(UserCompanyCondition condition);

    List<UserCompany> getUserAndCompany(List<String> ids);
}
