package com.wezley.uploadImage.model;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：weazly
 * 版    本：1.0
 * 创建日期：16/9/28
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class SimpleResponse implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    public int code;
    public String message;

    public SimpleResponse(){

    }

    public SimpleResponse(int code, String message){
        this.code = code;
        this.message = message;
    }

    public ServerResponse toServerResponse() {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.code = code;
        serverResponse.message = message;
        return serverResponse;
    }
}