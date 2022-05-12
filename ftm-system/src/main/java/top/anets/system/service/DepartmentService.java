package top.anets.system.service;

import top.anets.entity.system.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
public interface DepartmentService extends IService<Department> {

    List<Department> querys(String companyId);
}
