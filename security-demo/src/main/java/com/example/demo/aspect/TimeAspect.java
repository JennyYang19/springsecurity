package com.example.demo.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class TimeAspect {


    /**
     * 切入点 包围式
     * @param pt
     * @return
     */
    @Around("execution(* com.example.demo.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pt) throws Throwable {
        System.out.println("time spect start");
        long start = new Date().getTime();
        for (Object arg :
                pt.getArgs()) {
            System.out.println("arg is: "+arg);
        }

        Object object = pt.proceed();


        System.out.println("time aspect 耗时：" + (new Date().getTime() - start));

        System.out.println("time aspect end");
        return object;
    }

}
