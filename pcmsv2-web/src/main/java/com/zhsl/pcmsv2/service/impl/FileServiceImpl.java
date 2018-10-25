package com.zhsl.pcmsv2.service.impl;

import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.exception.SysException;
import com.zhsl.pcmsv2.service.FileService;
import com.zhsl.pcmsv2.util.FileUtil;
import com.zhsl.pcmsv2.util.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
public class FileServiceImpl implements FileService {

    @Override
    public String uploadTempFiles(String username, HttpServletRequest request) {

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("uploadfile");

        if (files == null || files.size() == 0) {
            log.error("【文件】 暂存文件失败，没有接收到有效的文件");
            throw new SysException(SysEnum.INVALID_INFO_RECEIVED_ERROR);
        }
        String tempFolderRelativePath = FileUtil.tempSaveFile(username, files);
        return tempFolderRelativePath;
    }

    /**
     *  剪切临时文件到目标地址
     * @param sourceTempFolderRelativePath 临时文件上一级文件夹的相对路径
     * @param targetFolderRelativePath 目标文件上一级文件夹的相对路径
     * @return
     */
    public List<String> transferFile(String sourceTempFolderRelativePath, String targetFolderRelativePath) {

        List<String> targetFileRelativePaths = Collections.emptyList();

        // 获取临时文件夹的绝对路径
        String sourceTempFolderBasePath = PathUtil.getFileBasePath(true) + sourceTempFolderRelativePath;

        // 生成临时文件夹对象
        File sourceTempFolder = new File(sourceTempFolderBasePath);

        // 获取临时文件们
        File[] sourceTempFiles = sourceTempFolder.listFiles();

        if (sourceTempFiles.length > 0) {
            for (File sourceTempFile : sourceTempFiles) {
                // 拟生成文件的相对路劲+文件名
                String targetFileRelativePath = targetFolderRelativePath + sourceTempFile.getName();
                // 你生成文件的绝对路径
                String targetFileBasePath = PathUtil.getFileBasePath(false) + targetFolderRelativePath;
                // 生成空文件
                File targetFile = new File(targetFileBasePath);
                if (!targetFile.exists()) {
                    targetFile.getParentFile().mkdirs();
                }
                // 将临时文件的文件内容剪切至生成的空文件中
                sourceTempFile.renameTo(targetFile);
                // 将生成的文件的相对路径放入list
                targetFileRelativePaths.add(targetFileRelativePath);
            }
        }

        return targetFileRelativePaths;
    }

}
