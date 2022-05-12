package top.anets.system.mapper;

import top.anets.entity.system.Company;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.anets.entity.system.SysUser;
import top.anets.vo.system.CompanyVo;

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
