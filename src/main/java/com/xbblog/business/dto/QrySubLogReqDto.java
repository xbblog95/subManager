package com.xbblog.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class QrySubLogReqDto {

    private int userId;

    private Date beginDate;

    private Date endDate;


}
