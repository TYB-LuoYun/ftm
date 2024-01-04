package top.anets.oauth2.sentinel.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import top.anets.utils.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;



@Component
public class CustomBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {


        if(e instanceof FlowException){
           throw new ServiceException("已被限流");
        }
        else if(e instanceof DegradeException){
            throw new ServiceException("降级异常");
        }
        else if(e instanceof ParamFlowException){
            throw new ServiceException("热点参数异常");
        }
        else if(e instanceof SystemBlockException){
            throw new ServiceException("系统异常");
        }
        else if(e instanceof AuthorityException){
            throw new ServiceException("授权异常");
        }
    }
}
