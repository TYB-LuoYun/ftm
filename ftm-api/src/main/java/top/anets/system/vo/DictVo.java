package top.anets.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.anets.system.entity.Dict;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class  DictVo extends Dict {

    private String $isNull = "parent_id";

}