package com.zhsl.pcmsv2.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 处理文件的服务接口
 */
public interface FileService {

    /**
     * 临时文件的上传方法
     * @param request
     * @return 文件暂存的中间路径
     */
    String uploadTempFiles(String username, HttpServletRequest request);

    /**
     *  剪切临时文件到目标地址
     * @param sourceTempFolderRelativePath 临时文件上一级文件夹的相对路径
     * @param targetFolderRelativePath 目标文件上一级文件夹的相对路径
     * @return
     */
    List<String> transferFile(String sourceTempFolderRelativePath, String targetFolderRelativePath);
}
