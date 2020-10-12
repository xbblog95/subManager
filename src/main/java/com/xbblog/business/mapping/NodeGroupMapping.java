package com.xbblog.business.mapping;

import com.xbblog.business.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface NodeGroupMapping {

    List<NodeGroup> getGroups(NodeGroup nodeGroup);

    List<NodeGroupKey> getGroupKeys(NodeGroupKey nodeGroup);
}
