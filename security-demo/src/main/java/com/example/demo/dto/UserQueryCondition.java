package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Administrator on 2018/10/3.
 */
public class UserQueryCondition {

    @ApiModelProperty(value = "用户名")
    private String username;
    private String passworld;
    private int age;
    private int ageTo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassworld() {
        return passworld;
    }

    public void setPassworld(String passworld) {
        this.passworld = passworld;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }
}
