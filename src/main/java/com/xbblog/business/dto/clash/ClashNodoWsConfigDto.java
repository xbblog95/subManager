package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

@Data
public class ClashNodoWsConfigDto {

   private String path;

   private ClashNodeWsHeadersConfigDto headers;

   @YamlProperty("max-early-data")
   private Integer maxEarlyData;

   @YamlProperty("early-data-header-name")
   private String earlyDataHeaderName;
}
