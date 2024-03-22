package top.anets.config.feign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import top.anets.common.utils.exception.ServiceException;

import java.nio.charset.StandardCharsets;

import static feign.FeignException.errorStatus;

/**
 * @author ftm
 * @date 2023-08-24 17:31
 *
 *
 * 这个配置需要单独出来才生效，暂时不知道为啥
 */
@Slf4j
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {

        try {
            if (response.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                String body = IOUtils.toString(response.body().asReader(StandardCharsets.UTF_8));
                log.error("feign error, request url:{} status:{}", response.request().toString(), response.status());
                JSONObject jsonObject = JSON.parseObject(body);
                if(jsonObject.get("success")!=null || jsonObject.get("code")!=null){
                    return new ServiceException("feign异常:"+jsonObject.get("message"));
                }else{
                    return new ServiceException("feign异常:"+body);
                }

            }
            return errorStatus(s, response);
        }
        catch (Exception e){
            return e;
        }
    }
}