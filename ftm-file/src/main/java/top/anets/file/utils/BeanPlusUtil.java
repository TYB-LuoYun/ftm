package top.anets.file.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ftm
 * @date 2023-12-21 16:33
 */
public class BeanPlusUtil  extends BeanUtil {
    public BeanPlusUtil() {
    }

    public static <T, E> List<T> toBeanList(Collection<E> sourceList, Class<T> destinationClass) {
        return sourceList != null && !sourceList.isEmpty() && destinationClass != null ? (List)sourceList.parallelStream().filter(Objects::nonNull).map((source) -> {
            return toBean(source, destinationClass);
        }).collect(Collectors.toList()) : Collections.emptyList();
    }

    public static <T, E> IPage<T> toBeanPage(IPage<E> page, Class<T> destinationClass) {
        if (page != null && destinationClass != null) {
            IPage<T> newPage = new Page(page.getCurrent(), page.getSize());
            newPage.setPages(page.getPages());
            newPage.setTotal(page.getTotal());
            List<E> list = page.getRecords();
            if (CollUtil.isEmpty(list)) {
                return newPage;
            } else {
                List<T> destinationList = toBeanList(list, destinationClass);
                newPage.setRecords(destinationList);
                return newPage;
            }
        } else {
            return null;
        }
    }
}
