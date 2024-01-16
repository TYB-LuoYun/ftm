package top.anets.boot.module.webservice.service;

import com.alibaba.fastjson.JSON;
import top.anets.boot.module.webservice.model.Teacher;

import javax.jws.WebService;
import java.util.Date;

/**
 * @author ftm
 * @date 2024-01-09 9:56
 */
@WebService(targetNamespace="http://service.webservice.module.boot.anets.top/",endpointInterface="top.anets.boot.module.webservice.service.TeacherService")
public class TeacherServiceImpl implements TeacherService{
    @Override
    public String getTeacherName(String teacherId) {

        return "我拿到了请求的teacherId"+teacherId;
    }
    @Override
    public String getUser(String teacherId) {
        System.out.println("此次请求的teacherId为"+teacherId);
        Teacher t = new Teacher("王老师",40,"男",new Date());
        return JSON.toJSONString(t);
    }
}