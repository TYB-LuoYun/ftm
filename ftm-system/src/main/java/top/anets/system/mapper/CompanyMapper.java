package top.anets.system.mapper;

import top.anets.system.entity.Company;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.anets.system.entity.SysUser;
import top.anets.system.vo.CompanyVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
public interface CompanyMapper extends BaseMapper<Company> {
    SysUser queryOne(String id);

    List<Company>  queryListIn( String... companyIds);

    List<Company> querys(CompanyVo condition);
}
