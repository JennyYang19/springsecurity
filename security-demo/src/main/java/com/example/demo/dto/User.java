package com.example.demo.dto;

import com.example.demo.validator.MyConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * Created by Administrator on 2018/10/3.
 */
public class User {

    //1 使用接口来申明多个视图
    public interface UserSimpleView{};

    public interface UserDetailView extends UserSimpleView{};
    @MyConstraint(message = "这是一个constraint")
    private String username;

    @NotBlank(message = "passworld不能为空")
    private String passworld;
    private int age;

    private String id;

    @Past(message = "生日不能为未来的时间")
    private Date birthday;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonView(UserSimpleView.class)
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassworld() {
        return passworld;
    }

    @JsonView(UserDetailView.class)
    public void setPassworld(String passworld) {
        this.passworld = passworld;
    }
}
