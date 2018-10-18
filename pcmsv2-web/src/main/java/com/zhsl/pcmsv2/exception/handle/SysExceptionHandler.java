package com.zhsl.pcmsv2.exception.handle;

import com.zhsl.pcmsv2.browser.support.ResultVO;
import com.zhsl.pcmsv2.browser.util.ResultUtil;
import com.zhsl.pcmsv2.exception.SysException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class SysExceptionHandler {

    @ExceptionHandler({SysException.class})
    @ResponseBody
    public ResultVO sysExceptionHandle(Exception e){
        if(e instanceof SysException){
            log.info("【系统异常】" + e);
            return ResultUtil.failed(((SysException)e).getCode(), e.getMessage());
        } else{
            log.info("【未知异常】" + e);
            return ResultUtil.failed();
        }
    }

}
