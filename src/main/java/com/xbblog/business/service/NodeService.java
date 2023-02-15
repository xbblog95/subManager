package com.xbblog.business.service;

import com.xbblog.base.utils.CustomRepresenter;
import com.xbblog.base.utils.YamlProPertyUtils;
import com.xbblog.business.dto.*;
import com.xbblog.business.dto.clash.ClashConfigDto;
import com.xbblog.business.dto.clash.ClashNodeConfigDto;
import com.xbblog.business.dto.clash.ClashProxyGroupsConfigDto;
import com.xbblog.business.dto.clash.ClashProxyProvidersConfigDto;
import com.xbblog.business.handler.NodeHandler;
import com.xbblog.business.mapping.NodeGroupMapping;
import com.xbblog.business.mapping.NodeMapping;
import com.xbblog.business.mapping.RuleMapping;
import com.xbblog.business.mapping.SubscribeMapping;
import com.xbblog.config.NormalConfiguration;
import com.xbblog.utils.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class NodeService {

    private static Logger logger = LoggerFactory.getLogger(NodeService.class);
    @Autowired
    private NodeMapping nodeMapping;

    @Autowired
    private SubscribeMapping subscribeMapping;

    @Autowired
    private NodeGroupMapping nodeGroupMapping;

    @Autowired
    private RuleMapping ruleMapping;


    @Transactional(propagation = Propagation.REQUIRED, isolation= Isolation.REPEATABLE_READ)
    public void insertAll(List<NodeBo> list)
    {
        deleteAll();
        for(NodeBo node : list)
        {
            NodeDetail detail = node.getNodeDetail();
            if(detail.getClass() == ShadowsocksNode.class)
            {
                insertNode(node.getNode());
                detail.setNodeId(node.getNode().getId());
                try
                {
                    insertShadowsocks(node.getNodeDetail());
                }
                catch (Exception e)
                {
                    logger.info("节点插入异常 订阅id {} 原因{} ", node.getNode().getSubscribeId(), ExceptionUtils.getStackTrace(e));
                }
            }
            else if(detail.getClass() == V2rayNodeDetail.class)
            {
                insertNode(node.getNode());
                detail.setNodeId(node.getNode().getId());
                try
                {
                    insertV2ray(node.getNodeDetail());
                }
                catch (Exception e)
                {
                    logger.info("节点插入异常 订阅id {} 原因{} ", node.getNode().getSubscribeId(), ExceptionUtils.getStackTrace(e));
                }
            }
            else if(detail.getClass() == ShadowsocksRNode.class)
            {
                //ssr节点
                ShadowsocksRNode shadowsocksRNode = (ShadowsocksRNode)detail;
                insertNode(node.getNode());
                detail.setNodeId(node.getNode().getId());
                try
                {
                    insertShadowsocksR(shadowsocksRNode);
                }
                catch (Exception e)
                {
                    logger.info("节点插入异常 订阅id {} 原因{} ", node.getNode().getSubscribeId(), ExceptionUtils.getStackTrace(e));
                }
            }
            else if(detail.getClass() == SnellNode.class)
            {
                //snell节点
                SnellNode snellNode = (SnellNode)detail;
                insertNode(node.getNode());
                detail.setNodeId(node.getNode().getId());
                try
                {
                    insertSnell(snellNode);
                }
                catch (Exception e)
                {
                    logger.info("节点插入异常 订阅id {} 原因{} ", node.getNode().getSubscribeId(), ExceptionUtils.getStackTrace(e));
                }
            }
            else if(detail.getClass() == TrojanNode.class)
            {
                //trojan节点
                TrojanNode trojanNode = (TrojanNode)detail;
                insertNode(node.getNode());
                detail.setNodeId(node.getNode().getId());
                try
                {
                    insertTrojan(trojanNode);
                }
                catch (Exception e)
                {
                    logger.info("节点插入异常 订阅id {} 原因{} ", node.getNode().getSubscribeId(), ExceptionUtils.getStackTrace(e));
                }
            }
        }
    }

    private void insertShadowsocksR(ShadowsocksRNode node) {
        //如果混淆参数是plain并且协议是origin的，可以认为是ss节点
        if("plain".equals(node.getObfs()) && "origin".equals(node.getProtocol()))
        {
            insertShadowsocks(node);
            return;
        }
        nodeMapping.insertShadowsocksR(node);
    }

    private void insertSnell(SnellNode node) {
        //如果混淆参数是plain并且协议是origin的，可以认为是ss节点
        nodeMapping.insertSnell(node);
    }


    private void insertTrojan(TrojanNode node) {
        nodeMapping.insertTrojan(node);
    }

    private void updateShadowsocksR(ShadowsocksRNode node) {
        if("plain".equals(node.getObfs()) && "origin".equals(node.getProtocol()))
        {
            updateShadowsocks(node);
            return;
        }
        nodeMapping.updateShadowsocksR(node);
    }
    private void updateShadowsocks(NodeDetail nodeDetail) {
        nodeMapping.updateShadowsocks(nodeDetail);
    }

    private boolean shadowsocksRIsExists(NodeDetail detail) {
        ShadowsocksNode node = nodeMapping.getShadowsocks(detail);
        return  node != null;
    }

    private void insertV2ray(NodeDetail nodeDetail) {
        nodeMapping.insertV2ray(nodeDetail);
    }

    private void insertShadowsocks(NodeDetail nodeDetail) {
        nodeMapping.insertShadowsocks(nodeDetail);
    }



    public void insertNode(Node node) {
        nodeMapping.insertNode(node);
        NodeStatus status = new NodeStatus();
        status.setNodeId(node.getId());
        nodeMapping.insertNodeStatus(status);
    }

    public List<NodeBo> getAllssLink() throws Exception  {
        List<NodeBo> nodeList = new ArrayList<NodeBo>();
        //自己自定义的节点解析
        List<String> customList = nodeMapping.getCustomNodes();
        for(String str : customList)
        {
            NodeDetail nodeDetail;
            if(str.startsWith("ss://"))
            {
                nodeDetail = AnalysisUtils.analysisShadowsocks(str);
            }
            else if(str.startsWith("vmess://"))
            {
                nodeDetail = AnalysisUtils.analysisVmess(str);
            }
            else
            {
                nodeDetail = AnalysisUtils.analysisShadowsocksR(str);
            }
            if(nodeDetail != null)
            {
                Node node = new Node("own", 0);
                nodeList.add(new NodeBo(node, nodeDetail));
            }
        }
        //订阅的节点
        List<Subscribe> list =  subscribeMapping.getAllSubscribe();
        if(CollectionUtils.isEmpty(list))
        {
            return nodeList;
        }
        for(Subscribe subscribe : list)
        {
            //获取节点Handler
            Map<String, Object> handlerReqMap = new HashMap<String, Object>();
            handlerReqMap.put("subscribeId", subscribe.getId());
            List<SubscribeHandler> nodeHandler = subscribeMapping.getNodeHandler(handlerReqMap);
            if(SubscribeType.SSSTRING.getCode().equals(subscribe.getType()))
            {
                String[] arr = subscribe.getSubscribe().split(" ");
                for(String ss : arr)
                {
                    NodeDetail nodeDetail = AnalysisUtils.analysisShadowsocks(ss);
                    if(nodeDetail != null)
                    {
                        Node node = new Node("subscribe", subscribe.getId());
                        NodeBo bo = new NodeBo(node, nodeDetail);
                        bo = nodeHandler(nodeHandler, bo);
                        nodeList.add(bo);
                    }
                }
            }
            else if(SubscribeType.SSDSTRING.getCode().equals(subscribe.getType()))
            {
                List<NodeDetail> nodes = AnalysisUtils.analysisShadowsocksD(subscribe.getSubscribe());
                for(NodeDetail nodeDetail : nodes)
                {
                    Node node = new Node("subscribe", subscribe.getId());
                    NodeBo bo = new NodeBo(node, nodeDetail);
                    bo = nodeHandler(nodeHandler, bo);
                    nodeList.add(bo);
                }
            }
            else if(SubscribeType.SSDSUBSCRIBE.getCode().equals(subscribe.getType()))
            {
                String text = null;
                try {
                    text = HttpUtils.sendGet(subscribe.getSubscribe(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<NodeDetail> nodes = AnalysisUtils.analysisShadowsocksD(text);
                for(NodeDetail nodeDetail : nodes)
                {
                    Node node = new Node("subscribe", subscribe.getId());
                    NodeBo bo = new NodeBo(node, nodeDetail);
                    bo = nodeHandler(nodeHandler, bo);
                    nodeList.add(bo);
                }
            }
            else if(SubscribeType.V2RAYNGSUBSCRIBE.getCode().equals(subscribe.getType()))
            {
                String text = "";
                try {
                    text = HttpUtils.sendGet(subscribe.getSubscribe(), null);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                List<NodeDetail> nodes = AnalysisUtils.analysisV2raySubscribe(text);
                for(NodeDetail nodeDetail : nodes)
                {
                    Node node = new Node("subscribe", subscribe.getId());
                    NodeBo bo = new NodeBo(node, nodeDetail);
                    bo = nodeHandler(nodeHandler, bo);
                    nodeList.add(bo);
                }
            }
            else if(SubscribeType.SSRSUBSCRIBE.getCode().equals(subscribe.getType()))
            {
                String text = null;
                try {
                    text = HttpUtils.sendGet(subscribe.getSubscribe(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<NodeDetail> nodes = AnalysisUtils.analysisSSRSubscribe(text);
                for(NodeDetail nodeDetail : nodes)
                {
                    Node node = new Node("subscribe", subscribe.getId());
                    NodeBo bo = new NodeBo(node, nodeDetail);
                    bo = nodeHandler(nodeHandler, bo);
                    nodeList.add(bo);
                }
            }
            else if(SubscribeType.CLASHSUBSCRIBE.getCode().equals(subscribe.getType()))
            {
                String text = null;
                try {
                    Header[] headers = new Header[]{new BasicHeader("user-agent", "ClashforWindows/0.19.7")};
                    text = HttpUtils.sendGet(subscribe.getSubscribe(), null, headers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<NodeDetail> nodes = AnalysisUtils.analysisClashSubscribe(text);
                for(NodeDetail nodeDetail : nodes)
                {
                    Node node = new Node("subscribe", subscribe.getId());
                    NodeBo bo = new NodeBo(node, nodeDetail);
                    bo = nodeHandler(nodeHandler, bo);
                    nodeList.add(bo);
                }
            }
            Thread.sleep(3000);
        }
        return nodeList;
    }

    private NodeBo nodeHandler( List<SubscribeHandler> nodeHandlers, NodeBo bo) throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        for(SubscribeHandler subscribeHandler : nodeHandlers)
        {
            Class<NodeHandler> clazz = (Class<NodeHandler>) Class.forName(subscribeHandler.getClassName());
            clazz.newInstance().beforeInsertDBHandler(bo);
        }
        return bo;
    }


    public void modNode(Node node)
    {
        nodeMapping.modNode(node);
    }

    public void deleteAll() {
        nodeMapping.deleteAllNodeStatus();
        nodeMapping.deleteAllNodeDetail();
        nodeMapping.deleteAllNode();
    }

    public List<NodeDto> getAllShadowsocksNodes() {
        return nodeMapping.getShadowsocksNodes(new HashMap<>());
    }

    public List<NodeDto> getV2rayNodes() {
        return nodeMapping.getV2rayNodes(new HashMap<>());
    }

    public List<NodeDto> getAllShadowsocksRNodes() {
        return nodeMapping.getShadowsocksRNodes(new HashMap<>());
    }

    public String getV2rayNgSubscribe(String isp, int group) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        paramMap.put("group", group);
        //获取v2ray节点
        List<NodeDto> v2rayList = getV2rayNodes(paramMap);
        //转换成v2ray对象
        List<V2rayNodeDetail> v2rayNodeDetails = V2rayNodeDetail.toV2rayDetails(v2rayList);
        for(int i = 0; i < v2rayNodeDetails.size(); i++)
        {
            buffer.append(V2rayNodeDetail.parseToV2rayNgString(v2rayNodeDetails.get(i)));
            if(i != v2rayNodeDetails.size() - 1)
            {
                buffer.append("\n");
            }
        }
        //获取ss节点
        List<NodeDto> ssList = getShadowsocksNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssList))
        {
            buffer.append("\n");
        }
        //转换成ss对象
        List<ShadowsocksNode> shadowsocksNodes = ShadowsocksNode.toShadowsocksNodes(ssList);
        for(int i = 0; i < shadowsocksNodes.size(); i++)
        {
            buffer.append(ShadowsocksNode.parseToV2rayNgString(shadowsocksNodes.get(i)));
            if(i != shadowsocksNodes.size() - 1)
            {
                buffer.append("\n");
            }
        }
        //获取trojan节点
        List<NodeDto> trojanList = getTrojanNodes(paramMap);
        if(!CollectionUtils.isEmpty(trojanList))
        {
            buffer.append("\n");
        }
        for(int i = 0; i < trojanList.size(); i++)
        {
            buffer.append(TrojanNode.parseToV2rayNgString(trojanList.get(i)));
            if(i != trojanList.size() - 1)
            {
                buffer.append("\n");
            }
        }
        return Base64Util.encode(buffer.toString());
    }

    public String getShadowrocketSubscribe(String isp, int group) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        paramMap.put("group", group);
        //获取v2ray节点
        List<NodeDto> v2rayList = getV2rayNodes(paramMap);
        List<V2rayNodeDetail> v2rayNodeDetails = V2rayNodeDetail.toV2rayDetails(v2rayList);
        for(int i = 0; i < v2rayNodeDetails.size(); i++)
        {
            String sub = V2rayNodeDetail.parseToShadowrocketString(v2rayNodeDetails.get(i));
            if(StringUtil.isEmpty(sub))
            {
                continue;
            }
            buffer.append(sub);
            if(i != v2rayNodeDetails.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        //获取ss节点
        List<NodeDto> ssList = getShadowsocksNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssList))
        {
            buffer.append("\n");
        }
        //转换成ss对象
        List<ShadowsocksNode> shadowsocksNodes = ShadowsocksNode.toShadowsocksNodes(ssList);
        for(int i = 0; i < shadowsocksNodes.size(); i++)
        {
            String sub = ShadowsocksNode.parseToShadowrocketString(shadowsocksNodes.get(i));
            buffer.append(sub);
            if(i != shadowsocksNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        //获取ssr节点
        List<NodeDto> ssrList = getShadowsocksRNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssrList))
        {
            buffer.append("\n");
        }
        //转换成ssr对象
        List<ShadowsocksRNode> shadowsocksrNodes = ShadowsocksRNode.toShadowsocksRNodes(ssrList);
        for(int i = 0; i < shadowsocksrNodes.size(); i++)
        {
            String sub = ShadowsocksRNode.parseToShadowrocketString(shadowsocksrNodes.get(i));
            buffer.append(sub);
            if(i != shadowsocksrNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        //获取trojan节点
        List<NodeDto> trojanList = getTrojanNodes(paramMap);
        if(!CollectionUtils.isEmpty(trojanList))
        {
            buffer.append("\n");
        }
        for(int i = 0; i < trojanList.size(); i++)
        {
            String sub = TrojanNode.parseToShadowrocketString(trojanList.get(i));
            buffer.append(sub);
            if(i != trojanList.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        return Base64Util.encode(buffer.toString());
    }

    public String getQuantumultSubscribe(String isp, int group)
    {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        paramMap.put("group", group);
        //获取v2ray节点
        List<NodeDto> v2rayList = getV2rayNodes(paramMap);
        List<V2rayNodeDetail> v2rayNodeDetails = V2rayNodeDetail.toV2rayDetails(v2rayList);
        for(int i = 0; i < v2rayNodeDetails.size(); i++)
        {
            String sub = V2rayNodeDetail.parseToQuantumultString(v2rayNodeDetails.get(i));
            if(StringUtil.isEmpty(sub))
            {
                continue;
            }
            buffer.append(sub);
            if(i != v2rayNodeDetails.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        //获取ss节点
        List<NodeDto> ssList = getShadowsocksNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssList))
        {
            buffer.append("\n");
        }
        //转换成ss对象
        List<ShadowsocksNode> shadowsocksNodes = ShadowsocksNode.toShadowsocksNodes(ssList);
        for(int i = 0; i < shadowsocksNodes.size(); i++)
        {
            String sub = ShadowsocksNode.parseToQuantumultString(shadowsocksNodes.get(i));
            buffer.append(sub);
            if(i != shadowsocksNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        //获取ssr节点
        List<NodeDto> ssrList = getShadowsocksRNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssrList))
        {
            buffer.append("\n");
        }
        //转换成ssr对象
        List<ShadowsocksRNode> shadowsocksRNodes = ShadowsocksRNode.toShadowsocksRNodes(ssrList);
        for(int i = 0; i < shadowsocksRNodes.size(); i++)
        {
            String sub = ShadowsocksRNode.parseToQuantumultString(shadowsocksRNodes.get(i));
            buffer.append(sub);
            if(i != shadowsocksRNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        return Base64Util.encodeURLSafe(buffer.toString());
    }

    public String getShadowsocksRSubscribe(String isp, int group) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        paramMap.put("group", group);
        //获取ss节点
        List<NodeDto> ssList = getShadowsocksNodes(paramMap);
        //转换成ss对象
        List<ShadowsocksNode> shadowsocksNodes = ShadowsocksNode.toShadowsocksNodes(ssList);
        for(int i = 0; i < shadowsocksNodes.size(); i++)
        {
            String sub = ShadowsocksNode.parseToShadowsocksRString(shadowsocksNodes.get(i));
            if(StringUtil.isEmpty(sub))
            {
                continue;
            }
            buffer.append(sub);
            if(i != shadowsocksNodes.size() - 1)
            {
                buffer.append("\n");
            }
        }
        //获取ssr节点
        List<NodeDto> ssrList = getShadowsocksRNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssrList))
        {
            buffer.append("\n");
        }
        //转换成ssr对象
        List<ShadowsocksRNode> shadowsocksRNodes = ShadowsocksRNode.toShadowsocksRNodes(ssrList);
        for(int i = 0; i < shadowsocksRNodes.size(); i++)
        {
            String sub = ShadowsocksRNode.parseToShadowsocksRString(shadowsocksRNodes.get(i));
            buffer.append(sub);
            if(i != shadowsocksRNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        return Base64Util.encode(buffer.toString());
    }

    public void getClashSubscribe(Writer writer, String isp, int group) {
        Map<String, Object> paramMap = new HashMap<String, Object>(2);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        paramMap.put("group", group);
        //获取v2ray节点
        List<NodeDto> v2rayList = getV2rayNodes(paramMap);
        //获取ss节点
        List<NodeDto> ssList = getShadowsocksNodes(paramMap);
        //获取ssr节点
        List<NodeDto> ssrList = getShadowsocksRNodes(paramMap);
        //torjan节点
        List<NodeDto> trojanList = getTrojanNodes(paramMap);

        //proxies属性
        List<ClashNodeConfigDto> allNodes = new ArrayList<>();
        //proxyGroups属性
        List<ClashProxyGroupsConfigDto> groupsConfigDtos = new ArrayList<>();

        //重命名重名的备注
        Map<String, Object> filter = new HashMap<String, Object>();
        for(NodeDto nodeDto : v2rayList)
        {
            if(filter.get(nodeDto.getRemarks()) != null)
            {
                nodeDto.setRemarks(nodeDto.getRemarks() + "(" + UUID.randomUUID().toString().replaceAll("-","") + ")");
            }
            filter.put(nodeDto.getRemarks(), nodeDto);
        }
        for(NodeDto nodeDto : ssList)
        {
            if(filter.get(nodeDto.getRemarks()) != null)
            {
                nodeDto.setRemarks(nodeDto.getRemarks() + "(" + UUID.randomUUID().toString().replaceAll("-","") + ")");
            }
            filter.put(nodeDto.getRemarks(), nodeDto);
        }
        for(NodeDto nodeDto : ssrList)
        {
            if(filter.get(nodeDto.getRemarks()) != null)
            {
                nodeDto.setRemarks(nodeDto.getRemarks() + "(" + UUID.randomUUID().toString().replaceAll("-","") + ")");
            }
            filter.put(nodeDto.getRemarks(), nodeDto);
        }
        for(NodeDto nodeDto : trojanList)
        {
            if(filter.get(nodeDto.getRemarks()) != null)
            {
                nodeDto.setRemarks(nodeDto.getRemarks() + "(" + UUID.randomUUID().toString().replaceAll("-","") + ")");
            }
            filter.put(nodeDto.getRemarks(), nodeDto);
        }
        allNodes.addAll(parseClashNodeList(v2rayList));
        allNodes.addAll(parseClashNodeList(ssrList));
        allNodes.addAll(parseClashNodeList(ssList));
        allNodes.addAll(parseClashNodeList(trojanList));
        Collections.shuffle(allNodes);

        /*************************处理全部分组***************************/
        ClashProxyGroupsConfigDto allProxyGroupsConfigDto = new ClashProxyGroupsConfigDto();
        allProxyGroupsConfigDto.setName("PROXY");
        allProxyGroupsConfigDto.setType("select");

        List<NodeGroup> groups = nodeGroupMapping.getGroups(new NodeGroup());
        List<String> allGroupNames = new ArrayList<>();
        for(NodeGroup nodeGroup : groups)
        {
            allGroupNames.add(nodeGroup.getName());
        }
        allGroupNames.add("其他");
        allProxyGroupsConfigDto.setProxies(allGroupNames);
        groupsConfigDtos.add(allProxyGroupsConfigDto);

        /*************************处理在分组内节点***************************/
        Map<Integer, Boolean> nodeInGroupStatusMap = new HashMap<>();
        for(NodeGroup nodeGroup : groups)
        {
            ClashProxyGroupsConfigDto clashProxyGroupsConfigDto = new ClashProxyGroupsConfigDto();
            clashProxyGroupsConfigDto.setName(nodeGroup.getName());
            clashProxyGroupsConfigDto.setType("select");
            List<String> nodeNames = new ArrayList<>();

            //获取key
            NodeGroupKey nodeGroupKey = new NodeGroupKey();
            nodeGroupKey.setGroupId(nodeGroup.getId());
            List<NodeGroupKey> groupKeys = nodeGroupMapping.getGroupKeys(nodeGroupKey);
//            ************************ 比对每个节点备注与关键字的差异，判断是否应该进入该组
            for(NodeDto nodeDto : v2rayList)
            {
                if(isInGroup(groupKeys, nodeDto))
                {
                    //标识该节点已被编入组
                    nodeInGroupStatusMap.put(nodeDto.getId(), true);
                    nodeNames.add(nodeDto.getRemarks());
                }
            }
            for(NodeDto nodeDto : ssList)
            {
                if(isInGroup(groupKeys, nodeDto))
                {
                    //标识该节点已被编入组
                    nodeInGroupStatusMap.put(nodeDto.getId(), true);
                    nodeNames.add(nodeDto.getRemarks());
                }
            }
            for(NodeDto nodeDto : ssrList)
            {
                if(isInGroup(groupKeys, nodeDto))
                {
                    //标识该节点已被编入组
                    nodeInGroupStatusMap.put(nodeDto.getId(), true);
                    nodeNames.add(nodeDto.getRemarks());
                }
            }
            for(NodeDto nodeDto : trojanList)
            {
                if(isInGroup(groupKeys, nodeDto))
                {
                    //标识该节点已被编入组
                    nodeInGroupStatusMap.put(nodeDto.getId(), true);
                    nodeNames.add(nodeDto.getRemarks());
                }
            }
            nodeNames.add("DIRECT");
            clashProxyGroupsConfigDto.setProxies(nodeNames);
            groupsConfigDtos.add(clashProxyGroupsConfigDto);
        }
        /*************************处理其他节点***************************/
        List<String> otherNodeNames = new ArrayList<>();
        for(NodeDto nodeDto : v2rayList)
        {
            //判断标识该节点已被编入组
            if(nodeInGroupStatusMap.get(nodeDto.getId()) == null)
            {
                otherNodeNames.add(nodeDto.getRemarks());
            }
        }
        for(NodeDto nodeDto : ssList)
        {
            //判断标识该节点已被编入组
            if(nodeInGroupStatusMap.get(nodeDto.getId()) == null)
            {
                otherNodeNames.add(nodeDto.getRemarks());
            }
        }
        for(NodeDto nodeDto : ssrList)
        {
            //判断标识该节点已被编入组
            if(nodeInGroupStatusMap.get(nodeDto.getId()) == null)
            {
                otherNodeNames.add(nodeDto.getRemarks());
            }
        }
        for(NodeDto nodeDto : trojanList)
        {
            //判断标识该节点已被编入组
            if(nodeInGroupStatusMap.get(nodeDto.getId()) == null)
            {
                otherNodeNames.add(nodeDto.getRemarks());
            }
        }
        otherNodeNames.add("DIRECT");
        ClashProxyGroupsConfigDto otherProxyGroupConfig = new ClashProxyGroupsConfigDto();
        otherProxyGroupConfig.setName("其他");
        otherProxyGroupConfig.setType("select");
        otherProxyGroupConfig.setProxies(otherNodeNames);
        groupsConfigDtos.add(otherProxyGroupConfig);
        /*********************************处理rule-providers**********************/
        List<ClashRuleProvider> clashRuleProviders = ruleMapping.queryAllClashRuleProviders();
        Map<String, ClashProxyProvidersConfigDto> ClashProxyProviderMap = new HashMap<>();
        List<String> rules = new ArrayList<>();
        for(ClashRuleProvider clashRuleProvider : clashRuleProviders)
        {
            ClashProxyProvidersConfigDto clashProxyProvidersConfigDto = new ClashProxyProvidersConfigDto();
            clashProxyProvidersConfigDto.setType(clashRuleProvider.getType());
            clashProxyProvidersConfigDto.setPath("./ruleset/" + clashRuleProvider.getName() + ".yaml");
            clashProxyProvidersConfigDto.setInterval(clashRuleProvider.getInterval());
            clashProxyProvidersConfigDto.setUrl(clashRuleProvider.getUrl());
            clashProxyProvidersConfigDto.setBehavior(clashRuleProvider.getBehavior());
            ClashProxyProviderMap.put(clashRuleProvider.getName(), clashProxyProvidersConfigDto);
            rules.add("RULE-SET,"+ clashRuleProvider.getName() + "," + clashRuleProvider.getRuleType());
        }
        /*********************************处理自定义rule**********************/
        List<ClashCustomRule> clashCustomRules = ruleMapping.queryAllCustomRules();
        for(ClashCustomRule clashCustomRule : clashCustomRules)
        {
            rules.add(clashCustomRule.getText());
        }
        rules.add("GEOIP,CN,DIRECT");
        rules.add("MATCH, ,PROXY");
        ClashConfigDto clashConfigDto = ClashConfigDto.newInstance();
        clashConfigDto.setProxies(allNodes);
        clashConfigDto.setProxyGroups(groupsConfigDtos);
        clashConfigDto.setProxyProviders(ClashProxyProviderMap);
        clashConfigDto.setRules(rules);
        Constructor constructor = new Constructor(ClashConfigDto.class);
        YamlProPertyUtils yamlProPertyUtils = new YamlProPertyUtils();
        yamlProPertyUtils.setSkipMissingProperties(true);
        constructor.setPropertyUtils(yamlProPertyUtils);
        Yaml yaml = new Yaml(constructor, new CustomRepresenter());
        yaml.dump(clashConfigDto, writer);
    }


    private List<ClashNodeConfigDto> parseClashNodeList(List<NodeDto> nodes)
    {
        List<ClashNodeConfigDto> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(nodes))
        {
            return result;
        }
        for(NodeDto node : nodes)
        {
            if("v2ray".equals(node.getType()))
            {
                ClashNodeConfigDto clashNodeConfigDto = V2rayNodeDetail.parseToClash(V2rayNodeDetail.toV2rayDetail(node));
                if(clashNodeConfigDto != null)
                {
                    result.add(clashNodeConfigDto);
                }
            }
            else if("ss".equals(node.getType()))
            {
                result.add(ShadowsocksNode.shadowsocksNodeparseToClash(ShadowsocksNode.toShadowsocksNode(node)));
            }
            else if("ssr".equals(node.getType()))
            {
                ClashNodeConfigDto clashNodeConfigDto = ShadowsocksRNode.shadowsocksRNodeparseToClash(ShadowsocksRNode.toShadowsocksNodeR(node));
                if(clashNodeConfigDto != null)
                {
                    result.add(clashNodeConfigDto);
                }
            }
            else
            {
                ClashNodeConfigDto clashNodeConfigDto = TrojanNode.trojanNodeparseToClash(TrojanNode.toTrojanNode(node));
                if(clashNodeConfigDto != null)
                {
                    result.add(clashNodeConfigDto);
                }
            }
        }
        return result;
    }
    private Boolean isInGroup(List<NodeGroupKey> groupKeys, NodeDto nodeDto)
    {
        Boolean flag = false;
        for(NodeGroupKey key : groupKeys)
        {
            if(nodeDto.getRemarks().indexOf(key.getKey()) >= 0)
            {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public String getPotatsoLiteStr(String isp) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        //获取ss节点
        List<NodeDto> ssList = getShadowsocksNodes(paramMap);
        //转换成ss对象
        List<ShadowsocksNode> shadowsocksNodes = ShadowsocksNode.toShadowsocksNodes(ssList);
        for(int i = 0; i < shadowsocksNodes.size(); i++)
        {
            String sub = ShadowsocksNode.parseToShadowrocketString(shadowsocksNodes.get(i));
            buffer.append(sub);
            if(i != shadowsocksNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        //获取ssr节点
        List<NodeDto> ssrList = getShadowsocksRNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssrList))
        {
            buffer.append("\n");
        }
        //转换成ssr对象
        List<ShadowsocksRNode> shadowsocksrNodes = ShadowsocksRNode.toShadowsocksRNodes(ssrList);
        for(int i = 0; i < shadowsocksrNodes.size(); i++)
        {
            String sub = ShadowsocksRNode.parseToShadowrocketString(shadowsocksrNodes.get(i));
            buffer.append(sub);
            if(i != shadowsocksrNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        return Base64Util.encodeURLSafe(buffer.toString());
    }

    public String getPharosProSubscribe(String isp, int group) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        paramMap.put("group", group);
        //获取v2ray节点
        List<NodeDto> v2rayList = getV2rayNodes(paramMap);
        //转换成v2ray对象
        List<V2rayNodeDetail> v2rayNodeDetails = V2rayNodeDetail.toV2rayDetails(v2rayList);
        for(int i = 0; i < v2rayNodeDetails.size(); i++)
        {
            buffer.append(V2rayNodeDetail.parseToV2rayNgString(v2rayNodeDetails.get(i)));
            if(i != v2rayNodeDetails.size() - 1)
            {
                buffer.append("\n");
            }
        }
        //获取ss节点
        List<NodeDto> ssList = getShadowsocksNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssList))
        {
            buffer.append("\n");
        }
        //转换成ss对象
        List<ShadowsocksNode> shadowsocksNodes = ShadowsocksNode.toShadowsocksNodes(ssList);
        for(int i = 0; i < shadowsocksNodes.size(); i++)
        {
            buffer.append(ShadowsocksNode.parseToPharosProString(shadowsocksNodes.get(i)));
            if(i != shadowsocksNodes.size() - 1)
            {
                buffer.append("\n");
            }
        }
        //获取ssr节点
        List<NodeDto> ssrList = getShadowsocksRNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssrList))
        {
            buffer.append("\n");
        }
        //转换成ssr对象
        List<ShadowsocksRNode> shadowsocksRNodes = ShadowsocksRNode.toShadowsocksRNodes(ssrList);
        for(int i = 0; i < shadowsocksRNodes.size(); i++)
        {
            String sub = ShadowsocksRNode.parseToShadowsocksRString(shadowsocksRNodes.get(i));
            buffer.append(sub);
            if(i != shadowsocksRNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        //获取Trojan节点
        List<NodeDto> trojanNodes = getTrojanNodes(paramMap);
        if(!CollectionUtils.isEmpty(trojanNodes))
        {
            buffer.append("\n");
        }
        for(int i = 0; i < trojanNodes.size(); i++)
        {
            //转换成ssr对象
            String sub = TrojanNode.parseToPharosProString(trojanNodes.get(i));
            buffer.append(sub);
            if(i != trojanNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        return Base64Util.encodeURLSafe(buffer.toString());
    }

    public String getQuantumultXSubscribe(String isp, int group) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        paramMap.put("group", group);
        //获取v2ray节点
        List<NodeDto> v2rayList = getV2rayNodes(paramMap);
        List<V2rayNodeDetail> v2rayNodeDetails = V2rayNodeDetail.toV2rayDetails(v2rayList);
        for(int i = 0; i < v2rayNodeDetails.size(); i++)
        {
            String sub = V2rayNodeDetail.parseToQuantumultXString(v2rayNodeDetails.get(i));
            if(StringUtil.isEmpty(sub))
            {
                continue;
            }
            buffer.append(sub);
            if(i != v2rayNodeDetails.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        //获取ss节点
        List<NodeDto> ssList = getShadowsocksNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssList))
        {
            buffer.append("\n");
        }
        //转换成ss对象
        List<ShadowsocksNode> shadowsocksNodes = ShadowsocksNode.toShadowsocksNodes(ssList);
        for(int i = 0; i < shadowsocksNodes.size(); i++)
        {
            String sub = ShadowsocksNode.parseToQuantumultXString(shadowsocksNodes.get(i));
            buffer.append(sub);
            if(i != shadowsocksNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        //获取ssr节点
        List<NodeDto> ssrList = getShadowsocksRNodes(paramMap);
        if(!CollectionUtils.isEmpty(ssrList))
        {
            buffer.append("\n");
        }
        //转换成ssr对象
        List<ShadowsocksRNode> shadowsocksRNodes = ShadowsocksRNode.toShadowsocksRNodes(ssrList);
        for(int i = 0; i < shadowsocksRNodes.size(); i++)
        {
            String sub = ShadowsocksRNode.parseToQuantumultXString(shadowsocksRNodes.get(i));
            buffer.append(sub);
            if(i != shadowsocksRNodes.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        //获取trojan节点
        List<NodeDto> trojanList = getTrojanNodes(paramMap);
        if(!CollectionUtils.isEmpty(trojanList))
        {
            buffer.append("\n");
        }
        for(int i = 0; i < trojanList.size(); i++)
        {
            String sub = TrojanNode.parseToQuantumultXString(trojanList.get(i));
            buffer.append(sub);
            if(i != trojanList.size() - 1 && !StringUtil.isEmpty(sub))
            {
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }

    public List<NodeDto> getV2rayNodes(Map<String, Object> paramMap)
    {
        List<NodeDto> list = nodeMapping.getV2rayNodes(paramMap);
        List<NodeDto> result = new ArrayList<NodeDto>();
        for(NodeDto node : list)
        {
            if(filterNode(node))
            {
                result.add(node);
            }
        }
        Collections.shuffle(result);
        return result;
    }

    public List<NodeDto> getShadowsocksNodes(Map<String, Object> paramMap)
    {
        List<NodeDto> list = nodeMapping.getShadowsocksNodes(paramMap);
        List<NodeDto> result = new ArrayList<NodeDto>();
        for(NodeDto node : list)
        {
            if(filterNode(node))
            {
                result.add(node);
            }
        }
        Collections.shuffle(result);
        return result;
    }

    public List<NodeDto> getTrojanNodes(Map<String, Object> paramMap) {
        List<NodeDto> list = nodeMapping.getTrojanNodes(paramMap);
        List<NodeDto> result = new ArrayList<NodeDto>();
        for(NodeDto node : list)
        {
            if(filterNode(node))
            {
                result.add(node);
            }
        }
        Collections.shuffle(result);
        return result;
    }

    public List<NodeDto> getShadowsocksRNodes(Map<String, Object> paramMap)
    {
        List<NodeDto> list = nodeMapping.getShadowsocksRNodes(paramMap);
        List<NodeDto> result = new ArrayList<NodeDto>();
        for(NodeDto node : list)
        {
            if(filterNode(node))
            {
                result.add(node);
            }
        }
        Collections.shuffle(result);
        return result;
    }

    private boolean filterNode(NodeDto node)
    {
        Map<Integer, Map<Integer, List<SubscribeKeyConfig>>> fitlerkeyMap = querySubscribeKeyConfig();
        Map<Integer, List<SubscribeKeyConfig>> subscribeKeyConfigMap = fitlerkeyMap.get(node.getSubscribeId());
        Map<Integer, List<SubscribeKeyConfig>> normalSubscribeKeyConfigMap = fitlerkeyMap.get(0);
        if(normalSubscribeKeyConfigMap != null)
        {
            //先做排除通用版本節點配置
            List<SubscribeKeyConfig> normalExcludeKeys = normalSubscribeKeyConfigMap.get(SubscribeKeyConfigKind.ExcludeKey.getCode());
            for(SubscribeKeyConfig config : normalExcludeKeys)
            {
                if(node.getRemarks().indexOf(config.getKey()) >= 0)
                {
                    return false;
                }
            }
        }
        if(subscribeKeyConfigMap == null )
        {
            return true;
        }
//        再做單節點排除节点的配置
        List<SubscribeKeyConfig> excludeKeys = subscribeKeyConfigMap.get(SubscribeKeyConfigKind.ExcludeKey.getCode());
        for(SubscribeKeyConfig config : excludeKeys)
        {
            if(node.getRemarks().indexOf(config.getKey()) >= 0)
            {
                return false;
            }
        }
        // 在做过滤关键字
        List<SubscribeKeyConfig> filterKeys = subscribeKeyConfigMap.get(SubscribeKeyConfigKind.FilterKey.getCode());
        //没有过滤关键字，自动成功
        if(CollectionUtils.isEmpty(filterKeys))
        {
            return true;
        }
        for(SubscribeKeyConfig config : filterKeys)
        {
            if(node.getRemarks().indexOf(config.getKey()) >= 0)
            {
                return true;
            }
        }
        return false;
    }


    public Map<Integer, Map<Integer, List<SubscribeKeyConfig>>> querySubscribeKeyConfig()
    {
        Map<Integer, Map<Integer, List<SubscribeKeyConfig>>> result = new HashMap<Integer, Map<Integer, List<SubscribeKeyConfig>>>();
        List<SubscribeKeyConfig> subscribeKeyConfigs = nodeMapping.querySubscribeKeyConfig();
        for(SubscribeKeyConfig subscribeKeyConfig : subscribeKeyConfigs)
        {
            if(result.get(subscribeKeyConfig.getSubscribeId()) == null)
            {
                Map<Integer, List<SubscribeKeyConfig>> map = new HashMap<Integer, List<SubscribeKeyConfig>>();
                map.put(SubscribeKeyConfigKind.FilterKey.getCode(), new ArrayList<SubscribeKeyConfig>());
                map.put(SubscribeKeyConfigKind.ExcludeKey.getCode(), new ArrayList<SubscribeKeyConfig>());
                result.put(subscribeKeyConfig.getSubscribeId(), map);
            }
            result.get(subscribeKeyConfig.getSubscribeId()).get(subscribeKeyConfig.getKind()).add(subscribeKeyConfig);
        }
        return result;
    }

    public void updateNodeStatus(NodeStatus nodeStatus) {
        List<NodeStatus> nodeStatuses = nodeMapping.queryNodeStatus(nodeStatus);
        if(CollectionUtils.isEmpty(nodeStatuses))
        {
            nodeMapping.insertNodeStatus(nodeStatus);
        }
        else
        {
            nodeMapping.updateNodeStatus(nodeStatus);
        }
    }

    public List<NodeStatusVo> queryNodeStatusPage() {

        List<NodeStatusVo> nodeStatusVos = nodeMapping.queryNodeStatusPage();
        List<NodeStatusVo> list = new ArrayList<>();
        for(NodeStatusVo statusVo: nodeStatusVos)
        {
            NodeDto nodeDto = new NodeDto();
            nodeDto.setRemarks(statusVo.getName());
            if(filterNode(nodeDto))
            {
                list.add(statusVo);
            }
        }
        return list;
    }
}