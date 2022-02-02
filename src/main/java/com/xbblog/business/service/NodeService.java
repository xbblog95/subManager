package com.xbblog.business.service;

import com.xbblog.business.dto.*;
import com.xbblog.business.handler.NodeHandler;
import com.xbblog.business.mapping.NodeGroupMapping;
import com.xbblog.business.mapping.NodeMapping;
import com.xbblog.business.mapping.SubscribeMapping;
import com.xbblog.config.NormalConfiguration;
import com.xbblog.utils.*;
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
                insertShadowsocks(node.getNodeDetail());
            }
            else if(detail.getClass() == V2rayNodeDetail.class)
            {
                insertNode(node.getNode());
                detail.setNodeId(node.getNode().getId());
                insertV2ray(node.getNodeDetail());
            }
            else if(detail.getClass() == ShadowsocksRNode.class)
            {
                //ssr节点
                ShadowsocksRNode shadowsocksRNode = (ShadowsocksRNode)detail;
                insertNode(node.getNode());
                detail.setNodeId(node.getNode().getId());
                insertShadowsocksR(shadowsocksRNode);
            }
            else if(detail.getClass() == SnellNode.class)
            {
                //snell节点
                SnellNode snellNode = (SnellNode)detail;
                insertNode(node.getNode());
                detail.setNodeId(node.getNode().getId());
                insertSnell(snellNode);
            }
            else if(detail.getClass() == TrojanNode.class)
            {
                //trojan节点
                TrojanNode trojanNode = (TrojanNode)detail;
                insertNode(node.getNode());
                detail.setNodeId(node.getNode().getId());
                insertTrojan(trojanNode);
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
        nodeMapping.deleteAll();
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

    public void getClashSubscribe(OutputStream os, String isp, int group) {
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

        //组名
//        String group = NormalConfiguration.webGroup;
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
        Map<String, Object> map = new HashMap<String, Object>();
        List<NodeGroup> groups = nodeGroupMapping.getGroups(new NodeGroup());
        List<Map<String, Object>> clashMapList = new ArrayList<>();
        Map<Integer, Boolean> nodeInGroupStatusMap = new HashMap<>();
        for(NodeGroup nodeGroup : groups)
        {
            //获取key
            NodeGroupKey nodeGroupKey = new NodeGroupKey();
            nodeGroupKey.setGroupId(nodeGroup.getId());
            List<NodeGroupKey> groupKeys = nodeGroupMapping.getGroupKeys(nodeGroupKey);
            Map<String, Object> groupMap = new HashMap<>();
//            ************************ 比对每个节点备注与关键字的差异，判断是否应该进入该组
            List<NodeDto> nodes = new ArrayList<>();
            for(NodeDto nodeDto : v2rayList)
            {
                if(isInGroup(groupKeys, nodeDto))
                {
                    //标识该节点已被编入组
                    nodeInGroupStatusMap.put(nodeDto.getId(), true);
                    nodes.add(nodeDto);
                }
            }
            for(NodeDto nodeDto : ssList)
            {
                if(isInGroup(groupKeys, nodeDto))
                {
                    //标识该节点已被编入组
                    nodeInGroupStatusMap.put(nodeDto.getId(), true);
                    nodes.add(nodeDto);
                }
            }
            for(NodeDto nodeDto : ssrList)
            {
                if(isInGroup(groupKeys, nodeDto))
                {
                    //标识该节点已被编入组
                    nodeInGroupStatusMap.put(nodeDto.getId(), true);
                    nodes.add(nodeDto);
                }
            }
            for(NodeDto nodeDto : trojanList)
            {
                if(isInGroup(groupKeys, nodeDto))
                {
                    //标识该节点已被编入组
                    nodeInGroupStatusMap.put(nodeDto.getId(), true);
                    nodes.add(nodeDto);
                }
            }
            Collections.shuffle(nodes);
            groupMap.put("name", nodeGroup.getName());
            groupMap.put("nodes",  parseClashNodeList(nodes));
            clashMapList.add(groupMap);
        }
        //处理其他节点
        Map<String, Object> otherMap = new HashMap<>();
        List<NodeDto> nodes = new ArrayList<>();
        for(NodeDto nodeDto : v2rayList)
        {
            //判断标识该节点已被编入组
            if(nodeInGroupStatusMap.get(nodeDto.getId()) == null)
            {
                nodes.add(nodeDto);
            }
        }
        for(NodeDto nodeDto : ssList)
        {
            //判断标识该节点已被编入组
            if(nodeInGroupStatusMap.get(nodeDto.getId()) == null)
            {
                nodes.add(nodeDto);
            }
        }
        for(NodeDto nodeDto : ssrList)
        {
            //判断标识该节点已被编入组
            if(nodeInGroupStatusMap.get(nodeDto.getId()) == null)
            {
                nodes.add(nodeDto);
            }
        }
        for(NodeDto nodeDto : trojanList)
        {
            //判断标识该节点已被编入组
            if(nodeInGroupStatusMap.get(nodeDto.getId()) == null)
            {
                nodes.add(nodeDto);
            }
        }
        Collections.shuffle(nodes);
        otherMap.put("name", "其他");
        otherMap.put("nodes",  parseClashNodeList(nodes));
        clashMapList.add(otherMap);
        map.put("group", clashMapList);
        List<NodeDto> allNode = new ArrayList<NodeDto>();
        for(NodeDto node : v2rayList)
        {
            allNode.add(node);
        }
        for(NodeDto node : ssList)
        {
            allNode.add(node);
        }
        for(NodeDto node : ssrList)
        {
            allNode.add(node);
        }
        for(NodeDto node : trojanList)
        {
            allNode.add(node);
        }
        map.put("nodes", parseClashNodeList(allNode));
        TemplateUtils.format("clash.ftl", map, os);
    }


    private List<Map<String, String>> parseClashNodeList(List<NodeDto> nodes)
    {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        if(CollectionUtils.isEmpty(nodes))
        {
            return result;
        }
        for(NodeDto node : nodes)
        {
            if("v2ray".equals(node.getType()))
            {
                Map<String, String> map = V2rayNodeDetail.parseToClashMap(V2rayNodeDetail.toV2rayDetail(node));
                if(map != null)
                {
                    result.add(map);
                }
            }
            else if("ss".equals(node.getType()))
            {
                result.add(ShadowsocksNode.shadowsocksNodeparseToClashMap(ShadowsocksNode.toShadowsocksNode(node)));
            }
            else if("ssr".equals(node.getType()))
            {
                Map<String, String> map = ShadowsocksRNode.shadowsocksRNodeparseToClashMap(ShadowsocksRNode.toShadowsocksNodeR(node));
                if(map != null)
                {
                    result.add(map);
                }
            }
            else
            {
                Map<String, String> map = TrojanNode.trojanNodeparseToClashMap(TrojanNode.toTrojanNode(node));
                if(map != null)
                {
                    result.add(map);
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
            buffer.append(ShadowsocksNode.parseToV2rayNgString(shadowsocksNodes.get(i)));
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
        return Base64Util.encodeURLSafe(buffer.toString());
    }

    public String getQuantumultXSubscribe(String isp, int group) throws UnsupportedEncodingException {
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

    private List<NodeDto> getV2rayNodes(Map<String, Object> paramMap)
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

    private List<NodeDto> getShadowsocksNodes(Map<String, Object> paramMap)
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

    private List<NodeDto> getTrojanNodes(Map<String, Object> paramMap) {
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

    private List<NodeDto> getShadowsocksRNodes(Map<String, Object> paramMap)
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

    public void getClashRSubscribe(OutputStream os, String isp, int group) {
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        paramMap.put("group", group);
        //获取v2ray节点
        List<NodeDto> v2rayList = getV2rayNodes(paramMap);
        //获取ss节点
        List<NodeDto> ssList = getShadowsocksNodes(paramMap);
        //获取ssr节点
        List<NodeDto> ssrList = getShadowsocksRNodes(paramMap);
        //组名
//        String group = NormalConfiguration.webGroup;
        //重命名重名的备注
        Map<String, Object> filter = new HashMap<String, Object>();
        //流媒体v2ray节点
        List<NodeDto> netflixV2rayList = new ArrayList<NodeDto>();
        //流媒体ss节点
        List<NodeDto> netflixSsList = new ArrayList<NodeDto>();
        //流媒体ssr节点
        List<NodeDto> netflixSsrList = new ArrayList<NodeDto>();
        for(NodeDto nodeDto : v2rayList)
        {
            if(filter.get(nodeDto.getRemarks()) != null)
            {
                nodeDto.setRemarks(nodeDto.getRemarks() + "(" + UUID.randomUUID().toString().replaceAll("-","") + ")");
            }
            filter.put(nodeDto.getRemarks(), nodeDto);
            if(isNetflix(nodeDto))
            {
                netflixV2rayList.add(nodeDto);
            }
        }
        for(NodeDto nodeDto : ssList)
        {
            if(filter.get(nodeDto.getRemarks()) != null)
            {
                nodeDto.setRemarks(nodeDto.getRemarks() + "(" + UUID.randomUUID().toString().replaceAll("-","") + ")");
            }
            filter.put(nodeDto.getRemarks(), nodeDto);
            if(isNetflix(nodeDto))
            {
                netflixSsList.add(nodeDto);
            }
        }
        for(NodeDto nodeDto : ssrList)
        {
            if(filter.get(nodeDto.getRemarks()) != null)
            {
                nodeDto.setRemarks(nodeDto.getRemarks() + "(" + UUID.randomUUID().toString().replaceAll("-","") + ")");
            }
            filter.put(nodeDto.getRemarks(), nodeDto);
            if(isNetflix(nodeDto))
            {
                netflixSsrList.add(nodeDto);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("v2rayNode", V2rayNodeDetail.parseToClashMap(V2rayNodeDetail.toV2rayDetails(v2rayList)));
        map.put("ssNode", ShadowsocksNode.shadowsocksNodeparseToClashMap(ShadowsocksNode.toShadowsocksNodes(ssList)));
        map.put("ssrNode", ShadowsocksRNode.parseShadowsocksRToClashMap(ShadowsocksRNode.toShadowsocksRNodes(ssrList)));
        map.put("netflixV2rayList", V2rayNodeDetail.parseToClashMap(V2rayNodeDetail.toV2rayDetails(netflixV2rayList)));
        map.put("netflixSsList", ShadowsocksNode.shadowsocksNodeparseToClashMap(ShadowsocksNode.toShadowsocksNodes(netflixSsList)));
        map.put("netflixSsrList", ShadowsocksRNode.parseShadowsocksRToClashMap(ShadowsocksRNode.toShadowsocksRNodes(netflixSsrList)));
        map.put("group", "clash");
        TemplateUtils.format("clashr.ftl", map, os);
    }

    public Boolean isNetflix(NodeDto nodeDto)
    {
        if(nodeDto == null)
        {
            return false;
        }
        String[] keyWords = new  String[]{"netflix", "nf",  "流媒体"};
        for(String key : keyWords)
        {
            if(nodeDto.getRemarks().toLowerCase().indexOf(key) >= 0)
            {
                return true;
            }
        }
        return false;
    }
}