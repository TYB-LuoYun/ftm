package top.anets.database.base;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ftm
 * @Date 2022-09-30 16:59:37
 * @Description Query条件构造器
 */
public class WrapperQuery {



    private static List<String> Exclude = Arrays.asList("current","size","total","serialVersionUID");

    public  static  QueryWrapper   query(Object vo){
        return query(objectToMap(vo));
    }

    public  static  QueryWrapper   query(Map<String,Object> map){
        if(map==null){
            return new QueryWrapper();
        }
        QueryWrapper wrapper = new QueryWrapper();
        map.entrySet().forEach(item->{
            String key = item.getKey();//字段名
            if(Exclude.contains(key)){
                return;
            }
            if(map.get(key)==null || map.get(key)==""){
                return;
            }
            String column="";
            if(key.contains("$like")){
                column=key.replace("$like", "");
                wrapper.like(column, map.get(key));
            }else if(key.contains("$lt")){
                column=key.replace("$lt", "");
                wrapper.lt(column, map.get(key));
            }else if(key.contains("$gt")){
                column=key.replace("$gt", "");
                wrapper.gt(column, map.get(key));
            }else if(key.contains("$desc")){
                wrapper.orderByDesc(fetchWord( map.get(key)));
            }else if(key.contains("$notNull")){
                List<String> strings = fetchWord(map.get(key));
                if(CollectionUtils.isEmpty(strings)){
                    return;
                }
                strings.forEach(each->{
                    wrapper.isNotNull(each);
                });
            }else if(key.contains("$isNull")){
                List<String> strings = fetchWord(map.get(key));
                if(CollectionUtils.isEmpty(strings)){
                    return;
                }
                strings.forEach(each->{
                    wrapper.isNull(each);
                });
            }else if(key.contains("$")){
                //               特殊字段什么都不用做
            }else {
                wrapper.eq(map.get(key)!=null, key, map.get(key));
            }



        });
        return  wrapper;
    }

    public static IPage page(Map<String, Object> params) {
        Long current = (Long) params.get("current");
        Long size = (Long) params.get("size");
        if(current==null||size==null){
            current=1L;
            size=Long.MAX_VALUE;
        }
        IPage page = new Page<>(current, size);
        return page;
    }


    /**
     * 拷贝实体到vo
     * @param source
     * @param t
     * @param <T>
     * @return
     */
    public  static <T> T from(Object source ,Class<T>   t){
        if(source == null){
            return null;
        }
        try {
            T o = t.newInstance();
            BeanUtils.copyProperties(source, o);
            return  o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> QueryWrapper<T> parse(Map<String, Object> params, Class<T> classz) {
        T t = map2Obj(params, classz);
        QueryWrapper<T> query =  WrapperQuery.query(t);
        return  query;
    }



    //java对象转map
    public static Map<String, Object> objectToMap(Object obj)   {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields =getAllDeclaredFields(obj.getClass());
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }


    public static <T> T map2Obj(Map<String,Object> map,Class<T> clz)  {
        try {
            T obj = clz.newInstance();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for(Field field:declaredFields){
                int mod = field.getModifiers();
                if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
            return obj;
        }catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T>  IPage<T> ipage(IPage pages,Class<T> t) {
        List<T> list = new LinkedList<>();
        pages.getRecords().forEach(item->{
            list.add(WrapperQuery.from(item, t));
        });
        pages.setRecords(list);
        return pages;
    }



    public static List<String>  fetchWord(Object str){
        if(str instanceof  String){
            List<String> strs = new ArrayList<String>();
            Pattern p = Pattern.compile("[_a-zA-Z0-9\\u4e00-\\u9fa5]+");
            Matcher m = p.matcher((String)str);
            while(m.find()) {
                strs.add(m.group());
            }
            return strs;
        }
        return (List<String>) str;
    }

    private static Field[] getAllDeclaredFields(Class<?> clazz) {

        List<Field> fieldList = new ArrayList<>();
        //      最多只遍历2层
        int i=0;
        while (clazz != null&&i<2){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
            i++;
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }

}
