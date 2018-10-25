package com.zhsl.pcmsv2.util;

import com.zhsl.pcmsv2.browser.enums.SysEnum;
import com.zhsl.pcmsv2.exception.SysException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class FileUtil {

    public static String tempSaveFile(String username, List<MultipartFile> files) {

        String tempFolderRelativePath = username+ "/" + System.currentTimeMillis()+"/";

        for (MultipartFile file : files) {
            String oriFileName = file.getOriginalFilename();
            String destFileName = System.currentTimeMillis() + "-" + oriFileName;
            File tempDest = new File(PathUtil.getFileBasePath(true) + tempFolderRelativePath + destFileName);
            if (!tempDest.getParentFile().exists()) {
                tempDest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(tempDest);
            } catch (IOException e) {
                log.error("【文件】 存储临时文件出错！");
                throw new SysException(SysEnum.INTERNAL_ERROR);
            }
        }

        return tempFolderRelativePath;
    }

}
