package com.zhsl.pcmsv2.dto;

import com.zhsl.pcmsv2.model.AnnouncementFile;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class AnnouncementDTO {

    private String rtFileTempPath;

    private String announcementId;

    private Boolean hot;

    private String keyword;

    @NotBlank(message = "标题不能为空！")
    private String title;

    private String type;

    @NotBlank(message = "内容不能为空！")
    private String content;

    private List<AnnouncementFile> announcementFiles;
}
