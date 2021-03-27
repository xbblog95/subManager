package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds;


import lombok.Data;

@Data
public class MuxDTO {



    private Boolean enabled;
    private int concurrency;


    public static MuxDTO getInstance()
    {
        MuxDTO muxDTO = new MuxDTO();
        muxDTO.setEnabled(false);
        return muxDTO;
    }
}

