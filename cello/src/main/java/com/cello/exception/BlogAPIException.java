package com.cello.exception;

import org.springframework.http.HttpStatus;

public class BlogAPIException  extends Throwable{
    public BlogAPIException(HttpStatus httpStatus,String InvalidJwtSignature){

    }
}