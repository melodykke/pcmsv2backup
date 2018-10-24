package com.zhsl.pcmsv2.controller;

import com.zhsl.pcmsv2.browser.support.ResultVO;
import com.zhsl.pcmsv2.browser.util.ResultUtil;
import com.zhsl.pcmsv2.model.UserInfo;
import com.zhsl.pcmsv2.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequestMapping("file")
@RestController
public class FileController {

    /**
     * 上传临时文件的接口 文件名 name： uploadfile
     * @param userInfo
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultVO saveFiles(@AuthenticationPrincipal UserInfo userInfo, HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("uploadfile");

        if (files == null || files.size() == 0) {
            return ResultUtil.failed();
        }
        String midPath = FileUtil.tempSaveFile(userInfo.getUsername(), files);
        return ResultUtil.success(midPath);
    }
}
