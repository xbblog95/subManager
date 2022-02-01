package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;


@Data
public class ClashNodeGrpcConfigDto {

    @YamlProperty("grpc-service-name")
    private String grpcServiceName;
}
