package top.anets.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.anets.system.entity.SysMenu;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenuVo extends SysMenu{

}