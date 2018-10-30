package com.example.securitycore.validate.code;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

@Data
public class ImageCode {


    private BufferedImage image;

    private String code;

    private LocalDateTime expireTime;

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        this.image=image;
        this.code=code;
        this.expireTime=expireTime;
    }


    public boolean isExpired(){  //当前时间是否在过期时间之后
        return LocalDateTime.now().isAfter(expireTime);
    }
    /**
     * 在多少秒过期
     * @param image
     * @param code
     * @param expireIn
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.image=image;
        this.code=code;
        this.expireTime=LocalDateTime.now().plusSeconds(expireIn);
    }
}
