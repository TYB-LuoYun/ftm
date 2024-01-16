package top.anets.boot.module.webservice.model;

import java.util.Date;

/**
 * @author ftm
 * @date 2024-01-09 9:54
 */
public class Teacher {
    private String name;
    private Integer age;
    private String sex;
    private Date birthday;

    // getter、setter

    public Teacher(String name, Integer age, String sex, Date birthday) {
        super();
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.birthday = birthday;
    }

}
