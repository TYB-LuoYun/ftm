package top.anets.system.service;

import top.anets.system.entity.Company;
import com.baomidou.mybatisplus.extension.service.IService;
import top.anets.system.vo.CompanyVo;

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
