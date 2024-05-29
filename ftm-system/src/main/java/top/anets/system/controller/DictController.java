package top.anets.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.anets.boot.model.Result;
import top.anets.database.base.PageQuery;
import top.anets.database.base.WrapperQuery;
import top.anets.system.entity.Dict;
import top.anets.system.service.DictService;
import top.anets.system.vo.DictVo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ftm
 * @since 2022-08-12
 */
@RestController
@RequestMapping("/dict")
@CrossOrigin
public class DictController {
    @Autowired
    private DictService dictService;


    @ApiOperation(value = "查询-分页-查询和返回新增字段或特殊处理")
    @RequestMapping("lists")
    public IPage lists(  DictVo dictVo, PageQuery query){
        //只查询一级的
        QueryWrapper wrapper = WrapperQuery.query(dictVo);
        IPage  pages = dictService.pages(wrapper, query.Page());
        WrapperQuery.ipage(pages,DictVo.class).getRecords().forEach(item->{
            //         todo    item.get...
            item.setChildren(this.getChildrenAll(item.getId()));
        });
        return pages;
    }



    @RequestMapping("getChildren")
    public Result getChildren(String parent){
        List<Dict> lists=dictService.getChildren(parent);
        return Result.Success(lists);
    }


    //查询后代
    @RequestMapping("getProgeny")
    public Result progeny(String parent){
        Dict self = dictService.getById(parent);
        if(self.getState()!=null&&self.getState()==false){
            return Result.Error("已被禁用，请先启用再试!");
        }
        List<Dict> lists=this.getChildrenAll(parent);
        return Result.Success(lists);
    }


    //查询当代和后代
    @RequestMapping("getSelfAndProgeny")
    public Result getSelfAndProgeny(String id){
        Dict self = dictService.getById(id);
        if(self.getState()!=null&&self.getState()==false){
            return Result.Error("已被禁用，请先启用再试!");
        }else{
            List<Dict> lists=this.getChildrenAll(id);
            self.setChildren(lists);
            return Result.Success(self);
        }

    }

    /**
     * 只需要code-value
     * @return
     */
    @RequestMapping("getTree")
    public Result getTree(){
        List<Dict> lists=this.getTreeAll(null);
        return Result.Success(lists);
    }

    private List<Dict>  getTreeAll(String parent) {
        List<Dict> lists=dictService.getChildren(parent);
        if(lists==null||lists.size()<=0){
            return lists;
        }else{
            for (Dict item:lists) {
                item.setLabel(item.getName());
                item.setValue(item.getId());
                item.setChildren(this.getTreeAll(item.getId()));
            }
            return  lists;
        }
    }




    @RequestMapping("getAll")
    public Result getAll(){
        List<Dict> lists=this.getChildrenAll(null);
        return Result.Success(lists);
    }

    private List<Dict> getChildrenAll(String parent) {
        List<Dict> lists=dictService.getChildren(parent);
        if(lists==null||lists.size()<=0){
            return lists;
        }else{
            for (Dict item:lists) {
                item.setChildren(this.getChildrenAll(item.getId()));
            }
            return  lists;
        }
    }


    @RequestMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Dict dict){
        dict.setUpdateTime(new Date());
//      修改之前查询下之前的父亲
        dictService.saveOrUpdateSmart(dict);
        return Result.Success("成功");
    }

    @RequestMapping("deletes")
    public Result delete(String... ids ){
        dictService.removeByIds(Arrays.asList(ids));
        return Result.Success("成功");
    }


}



