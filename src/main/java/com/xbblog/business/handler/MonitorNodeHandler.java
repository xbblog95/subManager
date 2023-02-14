package com.xbblog.business.handler;

import com.xbblog.business.dto.NodeDto;

import java.util.List;

public interface MonitorNodeHandler {

    public void monitor(final List<NodeDto> nodes);
}
