package com.example.demo.exception;

public class UserNotExistExceptioin extends RuntimeException {


    private static final long serialVersionUID = 4338084827790094708L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public UserNotExistExceptioin(String id){
        super("user not exist");
        this.id = id;
    }

}
