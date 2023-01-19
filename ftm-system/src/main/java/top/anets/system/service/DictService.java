package top.anets.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.anets.system.entity.Dict;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ftm
 * @since 2022-08-12
 */
public interface DictService extends IService<Dict> {
    List<Dict> getChildren(String parent);

    void saveOrUpdateSmart(Dict dict);

    IPage pages(QueryWrapper querys, IPage page);
}
