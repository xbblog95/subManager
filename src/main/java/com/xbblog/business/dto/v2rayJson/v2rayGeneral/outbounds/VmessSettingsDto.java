package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.vmessSettings.VnextDTO;
import lombok.Data;

import java.util.List;

@Data
public class VmessSettingsDto extends SettingsDTO{

    private List<VnextDTO> vnext;

}
