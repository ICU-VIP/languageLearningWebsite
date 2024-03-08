package org.cdtu.website.exception;


import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.cdtu.website.entity.HttpResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public HttpResult<Object> handleException(Exception e){
        e.printStackTrace();
        log.error(e.getMessage());
        //log.error(Arrays.toString(e.getStackTrace()));
       // return HttpResult.error(StringUtils.hasLength(e.getMessage())? e.getMessage() : "操作失败");
        return HttpResult.error("操作失败");
    }


}
