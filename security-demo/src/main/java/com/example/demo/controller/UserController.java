package com.example.demo.controller;

import com.example.demo.dto.User;
import com.example.demo.dto.UserQueryCondition;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/3.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * @valid非空验证  bindingResults 校验逻辑，不返回
     * @param user
     * @return
     */
    @PostMapping
    public User create(@RequestBody @Valid User user, BindingResult errors) {

        if (errors.hasErrors()) {
           errors.getAllErrors().forEach(e-> System.out.println(e.getDefaultMessage()));
        }
        System.out.println(user.getUsername());
        System.out.println(user.getAge());
        System.out.println(user.getBirthday());
        user.setId("1");
        return user;
    }

    @PutMapping("/{id:\\d+}")
    public User update(@RequestBody @Valid User user, BindingResult errors) {
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e->{
//                FieldError fieldError=(FieldError)e;
//                String message = fieldError.getField() +" "+ e.getDefaultMessage();
                System.out.println(e.getDefaultMessage());} );
        }
        System.out.println(user.getUsername());
        System.out.println(user.getAge());
        System.out.println(user.getBirthday());
        user.setId("1");
        return user;
    }


    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id) {
        System.out.println(id);
    }

//    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @GetMapping
    @JsonView(User.UserSimpleView.class)
    public List<User> getUser(UserQueryCondition condition,@PageableDefault(page = 1,size = 10,sort = "username,asc") Pageable pageable){
//        System.out.println("==============="+username);
        System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));
        System.out.println(pageable.getPageNumber());
        System.out.println(pageable.getPageSize());
        System.out.println(pageable.getSort());
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        return users;
    }

    /**
     * 只能接受数字
     * @param id
     * @return
     */
//    @RequestMapping(value = "/user/{id:\\d+}", method = RequestMethod.GET)
    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getUserInfo(@PathVariable String id){

        System.out.println(id);
        User user = new User();
        user.setUsername("Tom");
        user.setAge(18);
        return user;
    }






}

