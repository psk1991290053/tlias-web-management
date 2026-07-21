package com.itheima.exception;


import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//异常会按照继承关系从下往上匹配
//全局异常处理器
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result handleException(Exception e){
        log.error("程序出错啦~",e);
        return Result.error("出错啦，请联系管理员~");
    }

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e){
        log.error("程序出错啦~",e);
        String message = e.getMessage();
        int i = message.indexOf("Duplicate entry");
        String errMessage = message.substring(i);
        String[] arr = errMessage.split(" ");
        return Result.error(arr[2] + "已存在");
    }



}
