package com.example.securitycore.validate.code.image;

import com.example.securitycore.validate.code.ValidateCode;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

@Data
public class ImageCode extends ValidateCode {


    private BufferedImage image;


    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        super(code,expireTime);
        this.image=image;

    }



    /**
     * 在多少秒过期
     * @param image
     * @param code
     * @param expireIn
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code,expireIn);
        this.image=image;

    }
}
