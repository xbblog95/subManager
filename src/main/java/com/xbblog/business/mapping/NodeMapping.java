package com.xbblog.business.mapping;

import com.xbblog.business.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface NodeMapping {

    List<NodeDto> getShadowsocksNodes(Map<String, Object> map);


    List<NodeDto> getV2rayNodes(Map<String, Object> map);

    List<NodeDto> getShadowsocksRNodes(Map<String, Object> paramMap);

    List<NodeDto> getTrojanNodes(Map<String, Object> paramMap);

    void modNode(Node node);

    void insertNode(Node node);

    void deleteAll();

    void insertV2ray(NodeDetail nodeDetail);

    void insertShadowsocks(NodeDetail nodeDetail);

    ShadowsocksNode getShadowsocks(NodeDetail detail);

    void updateShadowsocks(NodeDetail nodeDetail);

    List<String> getCustomNodes();

    void insertShadowsocksR(ShadowsocksRNode node);

    void updateShadowsocksR(ShadowsocksRNode node);

    List<SubscribeKeyConfig> querySubscribeKeyConfig();

    void insertSnell(SnellNode node);

    void insertTrojan(TrojanNode node);


    List<NodeStatus> queryNodeStatus(NodeStatus nodeStatus);

    int insertNodeStatus(NodeStatus nodeStatus);

    int updateNodeStatus(NodeStatus nodeStatus);

    int deleteAllNodeStatus();

    int deleteAllNodeDetail();

    int deleteAllNode();

    List<NodeStatusVo> queryNodeStatusPage();
}
