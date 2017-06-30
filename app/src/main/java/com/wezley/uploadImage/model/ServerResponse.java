package com.wezley.uploadImage.model;

import java.io.Serializable;


public class ServerResponse<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;
    public int code;
    public String message;
    public String state;
    public T data;

}