package com.zhsl.pcmsv2.dto;

import com.zhsl.pcmsv2.model.AnnouncementFile;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class AnnouncementDTO {

    // 如果上一个操作有文件上传，则将上传的文件的临时相对路径路径赋在此属性上
    private String tempFolderRelativePath;

    private String announcementId;

    private Boolean hot;

    private String keyword;

    @NotBlank(message = "标题不能为空！")
    private String title;

    private String type;

    @NotBlank(message = "内容不能为空！")
    private String content;

    private List<AnnouncementFile> files;
}
