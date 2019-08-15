package com.xbblog.business.mapping;

import com.xbblog.business.dto.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

public interface UserMapping {
    User getUser(Map<String, Object> map);

    User getUserByToken(String token);

    void modifyPassword(Map<String, Object> paramMap);

    void modifyToken(Map<String, Object> paramMap);
}
