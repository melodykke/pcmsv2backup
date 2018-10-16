package com.zhsl.pcmsv2.browser.util;

import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.browser.support.ResultVO;

public class ResultUtil {
    public static ResultVO success(Integer code, String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
    public static ResultVO success(SysEnum sysEnum){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(sysEnum.getCode());
        resultVO.setMsg(sysEnum.getMsg());
        return resultVO;
    }

    public static ResultVO success(){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(SysEnum.DATA_CALLBACK_SUCCESS.getCode());
        resultVO.setMsg(SysEnum.DATA_CALLBACK_SUCCESS.getMsg());
        return resultVO;
    }

    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(SysEnum.DATA_CALLBACK_SUCCESS.getCode());
        resultVO.setMsg(SysEnum.DATA_CALLBACK_SUCCESS.getMsg());
        resultVO.setData(object);
        return resultVO;
    }
    public static ResultVO success(Integer code, String msg, Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        resultVO.setData(object);
        return resultVO;
    }

    public static ResultVO failed(){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(SysEnum.DATA_CALLBACK_FAILED.getCode());
        resultVO.setMsg(SysEnum.DATA_CALLBACK_FAILED.getMsg());
        return resultVO;
    }

    public static ResultVO failed(SysEnum sysEnum){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(sysEnum.getCode());
        resultVO.setMsg(sysEnum.getMsg());
        return resultVO;
    }
    public static ResultVO failed(Integer code, String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
    public static ResultVO failed(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(SysEnum.DATA_CALLBACK_FAILED.getCode());
        resultVO.setMsg(SysEnum.DATA_CALLBACK_FAILED.getMsg());
        resultVO.setData(object);
        return resultVO;
    }
}
