package com.zhsl.pcmsv2.mapper;
;
import com.zhsl.pcmsv2.model.OperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OperationLogMapper {
    int deleteByPrimaryKey(String operationLogId);

    int insert(OperationLog record);

    OperationLog selectByPrimaryKey(String operationLogId);

    List<OperationLog> selectAll();

    int updateByPrimaryKey(OperationLog record);

    List<OperationLog> findOperationLogsByUserId(@Param("userId") String userId);

    List<OperationLog> findByUserIdAndSearchParam(@Param("userId") String userId, @Param("searchParam") String searchParam);
}