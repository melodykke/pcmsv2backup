package com.zhsl.pcmsv2.mapper;

import com.zhsl.pcmsv2.model.Feedback;

import java.util.List;

public interface FeedbackMapper {
    int deleteByPrimaryKey(String feedbackId);

    int insert(Feedback record);

    Feedback selectByPrimaryKey(String feedbackId);

    List<Feedback> selectAll();

    int updateByPrimaryKey(Feedback record);
}