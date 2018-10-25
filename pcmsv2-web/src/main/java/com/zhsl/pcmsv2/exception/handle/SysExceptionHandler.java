package com.zhsl.pcmsv2.exception.handle;

import com.zhsl.pcmsv2.browser.support.ResultVO;
import com.zhsl.pcmsv2.browser.util.ResultUtil;
import com.zhsl.pcmsv2.exception.SysException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
@Slf4j
public class SysExceptionHandler {

    @ExceptionHandler({SysException.class})
    @ResponseBody
    public ResultVO sysExceptionHandle(SysException e){
        log.info("【系统异常】" + e);
        return ResultUtil.failed(((SysException)e).getCode(), e.getMessage());
    }

    @ExceptionHandler({MultipartException.class})
    @ResponseBody
    public ResultVO multipartExceptionHandle(MultipartException e){
        log.info("【文件】 文件系统异常，可能是上传文件的大小和格式出错！");
        return ResultUtil.failed("文件出错：" + e.getMessage());
    }
}
