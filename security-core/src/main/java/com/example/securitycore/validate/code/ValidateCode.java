package com.example.securitycore.validate.code;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

@Data
public class ValidateCode {




    private String code;

    private LocalDateTime expireTime;

    public ValidateCode(String code, LocalDateTime expireTime) {

        this.code=code;
        this.expireTime=expireTime;
    }



    public boolean isExpired(){  //当前时间是否在过期时间之后
        return LocalDateTime.now().isAfter(expireTime);
    }
    /**
     * 在多少秒过期
     * @param code
     * @param expireIn
     */
    public ValidateCode(String code, int expireIn) {

        this.code=code;
        this.expireTime=LocalDateTime.now().plusSeconds(expireIn);
    }
}
