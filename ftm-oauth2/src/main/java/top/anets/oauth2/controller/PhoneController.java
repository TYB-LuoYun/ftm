package top.anets.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.boot.redis.RedisConstant;

/**
 * @author ftm
 * @date 2024-03-22 11:01
 */
@RestController
@RequestMapping("open/phone")
public class PhoneController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @RequestMapping("sendSms")
    public void sendSms(String mobile){
        String code = "1399";
        redisTemplate.opsForValue().set(RedisConstant.SMS_CODE_PREFIX + mobile,code);
    }
}
