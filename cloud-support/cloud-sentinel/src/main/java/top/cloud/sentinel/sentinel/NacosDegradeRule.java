package top.cloud.sentinel.sentinel;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import lombok.Data;

import java.util.Date;

/**
 * @author ftm
 * @date 2023-08-30 15:33
 */
@Data
public class NacosDegradeRule extends DegradeRule {

    private String ip;
    private Integer port;
    private String app;
    /**
     * 下面是远程参数
     */
    private Integer id;
    private Long gmtCreate;
    private Long gmtModified;
}
