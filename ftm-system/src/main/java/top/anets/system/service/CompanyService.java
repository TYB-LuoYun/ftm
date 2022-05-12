package top.anets.system.service;

import top.anets.entity.system.Company;
import com.baomidou.mybatisplus.extension.service.IService;
import top.anets.entity.system.SysUser;
import top.anets.vo.system.CompanyVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
public interface CompanyService extends IService<Company> {

    List<Company> getSelfAndChildren(String companyId);

    void querys(CompanyVo companyVo);
}
