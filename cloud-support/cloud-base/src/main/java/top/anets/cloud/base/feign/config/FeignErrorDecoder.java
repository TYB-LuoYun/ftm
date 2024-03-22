package top.anets.cloud.base.feign.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import top.anets.cloud.base.feign.exception.FeignException;

import java.io.IOException;
import java.io.Reader;
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
                String body = getResponseBody(response);
                log.error("feign error, request url:{} status:{}", response.request().toString(), response.status());
                JSONObject jsonObject = JSON.parseObject(body);
                if(jsonObject.get("success")!=null || jsonObject.get("code")!=null){
                    String message = jsonObject.getString("message");
                    if(StringUtils.isBlank(message)){
                        message = jsonObject.getString("msg");
                    }
                    return new FeignException("feign异常:"+message);
                }else{
                    return new FeignException("feign异常:"+body);
                }

            }
            HttpStatus httpStatus = HttpStatus.valueOf(response.status());
            if(httpStatus!=null){
                return new FeignException("feign异常:"+httpStatus.getReasonPhrase());
            }
            return errorStatus(s, response);
        }
        catch (Exception e){
            e.printStackTrace();
            return e;
        }
    }


    private String getResponseBody(Response response) {
        try {
            // 将原始的 Response 包装一下，防止在 ErrorDecoder 中读取主体内容时出现问题
            Response wrappedResponse = response.toBuilder().build();

            if (wrappedResponse.body() != null) {
                try (Reader reader = wrappedResponse.body().asReader(StandardCharsets.UTF_8)) {
                    return IOUtils.toString(reader);
                }
            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            // 关闭响应体
            response.close();
        }
        return "";
    }
}