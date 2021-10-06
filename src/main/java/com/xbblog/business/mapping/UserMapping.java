package com.xbblog.business.mapping;

import com.xbblog.business.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserMapping {
    User getUser(Map<String, Object> map);

    User getUserById(GetUserByIdReqDto reqDto);

    User getUserByToken(String token);

    void modifyPassword(Map<String, Object> paramMap);

    void modifyToken(Map<String, Object> paramMap);

    void saveSubLog(SubLog subLog);

    List<SubLog> qrySubLog(QrySubLogReqDto subLog);

    void insertRiskAction(RiskAction riskAction);

    void modifyGroup(Map<String, Object> paramMap);
}
