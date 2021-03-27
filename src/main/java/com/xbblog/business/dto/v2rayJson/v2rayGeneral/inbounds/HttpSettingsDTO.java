package com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds.httpSettings.AccountsDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class HttpSettingsDTO extends SettingsDTO{

    private Integer timeout;
    private List<AccountsDTO> accounts;
    private Boolean allowTransparent;
    private Integer userLevel;

}
