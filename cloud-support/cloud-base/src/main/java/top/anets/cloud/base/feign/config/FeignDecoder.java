package top.anets.cloud.base.feign.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author ftm
 * @date 2024-01-19 17:09
 */
@Configuration
public class FeignDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        String body = getResponseBody(response);
        JSONObject jsonObject = JSON.parseObject(body);
        if(jsonObject.get("success")!=null || jsonObject.get("code")!=null){
            String message = jsonObject.getString("data");
            return JSON.parseObject(message,type );
        }else{
           return JSON.parseObject(body,type );
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
