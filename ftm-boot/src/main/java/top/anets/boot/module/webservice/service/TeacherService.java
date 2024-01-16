package top.anets.boot.module.webservice.service;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author ftm
 * @date 2024-01-09 9:55
 */

@WebService
public interface TeacherService {
    // @WebMethod：表示要发布的方法
    @WebMethod
    String getTeacherName(@WebParam(name="teacherId") String teacherId);

    // @WebParam：表示要发布方法的参数，默认的参数名称是args0、args1等，如果有这个注解，teacherId替代args0。
    @WebMethod
    String getUser(String teacherId);

}
