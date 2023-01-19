package top.anets.system.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import top.anets.system.entity.SysUser;
import top.anets.system.config.AuthUtil;
import top.anets.ifeign.system.IFeignSystem;
import top.anets.system.service.SysUserService;
import top.anets.utils.base.Result;
import top.anets.utils.exception.ServiceException;
import top.anets.system.vo.SysUserCondition;

import java.util.Base64;
import java.util.Collections;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 * @author ftm
 * @since 2021-07-26
 */
@Slf4j
@RestController
@RequestMapping("/sys-user")
public class SysUserController {
    @Autowired
    private IFeignSystem iFeignSystem;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/getUser")
    public Result getUser(){
        return  Result.success(AuthUtil.getUserInfo());
    }



    @RequestMapping("/getUsers")
    public Result getUsers(@RequestBody SysUserCondition condition){
        sysUserService.querysDetail(condition,AuthUtil.getUserInfo());
        return  Result.success(condition);
    }

    @RequestMapping("/saveUser")
    private  Result  saveUser(@RequestBody SysUser user){
        sysUserService.saveOrUpdateHandle(user);
        return Result.success();
    }


    @RequestMapping("/deleteUser")
    private  Result  deleteUser(String uid){

        sysUserService.removeById(uid);
        return Result.success();

    }



    /**
     * 根据用户名密码登录
     * @param sysUser
     * @return
     */
    @RequestMapping("/login")
    public Result login(@RequestBody SysUser sysUser){
//      根据用户名查看用户是否存在
        SysUser user = sysUserService.findByUsername(sysUser.getUsername());
        if(user==null) throw  new ServiceException(sysUser.getUsername()+"用户名不存在！");

//      校验密码,不用校验，交给oauth2，因为自己发现加密后的结果不一样但是同样是相同的密码
        log.info("原密码："+sysUser.getPassword());
//        log.info("原密码加密后："+passwordEncoder.encode(sysUser.getPassword()));
//        log.info("数据库密码："+user.getPassword());
//        $2a$10$2c6uqCzY3ObyCBU7WnY/AORFVU6ZAR.JfUnsogxX3ixgsszCJeiWW
//        if(!user.getPassword().equals(new BCryptPasswordEncoder().encode(sysUser.getPassword())))
//            throw  new ServiceException("密码错误！");

//      通过oauth2的密码模式进行登录
        String client_secret ="mxg-blog-admin" +":"+"123456";
        client_secret = "Basic "+ Base64.getEncoder().encodeToString(client_secret.getBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",client_secret);
        //授权请求信息
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("username", Collections.singletonList(sysUser.getUsername()));
        map.put("password", Collections.singletonList(sysUser.getPassword()));
        map.put("grant_type", Collections.singletonList("password"));
        map.put("scope",Collections.singletonList("all"));


        log.info("========================================================================");
        log.info("username:"+sysUser.getUsername());
        log.info("password:"+sysUser.getPassword());
        String url ="http://127.0.0.1:6001/auth/oauth/token";
        log.info("url:"+url);
        //获取 Token
        //HttpEntity
        HttpEntity httpEntity = new HttpEntity(map,httpHeaders);
        //获取 Token
        ResponseEntity<OAuth2AccessToken> json = restTemplate.postForEntity(url , httpEntity, OAuth2AccessToken.class);
        log.info("状态码："+json.getStatusCode());
        log.info("状态值："+json.getStatusCodeValue());
        log.info("状态体："+json.getBody());
        return Result.success(json.getBody());
    }









}

