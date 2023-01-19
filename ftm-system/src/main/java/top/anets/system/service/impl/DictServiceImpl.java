package top.anets.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.anets.system.entity.Dict;
import top.anets.system.mapper.DictMapper;
import top.anets.system.service.DictService;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ftm
 * @since 2022-08-12
 */
@Service
@Transactional
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private  DictMapper dictMapper;
    @Override
    public IPage pages(QueryWrapper querys, IPage page) {
        return baseMapper.selectPage(page,querys );
    }

    @Override
    public List<Dict> getChildren(String parent) {
        return dictMapper.getChildren(parent);
    }

    @Override
    public void saveOrUpdateSmart(Dict dict) {

        if(dict.getId()!=null){//当前节点更新的话
            Dict old = this.getById(dict.getId());//旧节点
            if(old.getParentId()!=dict.getParentId()){//新节点和旧节点的父节点不同了
                log.info("新节点和旧节点的父节点不同了");
                List<Dict> lists=this.getChildren(old.getParentId());//旧节点的父节点
                if(lists==null||lists.size()==1){
                    log.info("修改旧节点的父节点");
                    Dict oldFather = this.getById(old.getParentId());
                    oldFather.setIsLeaf(true);
                    oldFather.setIsDir(false);
                    this.saveOrUpdate(oldFather);
                }
            }
        }

//      新节点的父节点
        if(dict.getParentId()!=null){//新节点的父节点
            Dict father= this.getById(dict.getParentId());
            father.setIsLeaf(false);
            father.setIsDir(true);
            log.info("新节点的父节点坑定不是叶子节点");
            this.saveOrUpdate(father);
        }






        if(dict.getId()==null){
            dict.setIsLeaf(true);
            dict.setIsDir(false);
            log.info("新节点 是叶子节点");
        }else{
            List<Dict> lists=this.getChildren(dict.getId());
            if(lists==null||lists.size()<=0){//查看是否是子节点
                dict.setIsLeaf(true);
                dict.setIsDir(false);
                log.info("当前节点是叶子节点");
            }
        }


//      查看等级
        dict.setLevel(this.getLevel(dict));



        this.saveOrUpdate(dict);
    }

    private Integer getLevel(Dict dict) {
        if(StringUtils.isBlank(dict.getParentId())){
            return 1;
        }
        Dict byId = this.getById(dict.getParentId());
        return 1+this.getLevel(byId);
    }
}

