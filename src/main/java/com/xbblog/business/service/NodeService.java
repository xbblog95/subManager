package com.xbblog.business.service;

import com.xbblog.business.dto.*;
import com.xbblog.business.mapping.NodeMapping;
import com.xbblog.business.mapping.SubscribeMapping;
import com.xbblog.config.NormalConfiguration;
import com.xbblog.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.*;

@Service
public class NodeService {

    private static Logger logger = LoggerFactory.getLogger(NodeService.class);
    @Autowired
    private NodeMapping nodeMapping;

    @Autowired
    private SubscribeMapping subscribeMapping;


    @Transactional(propagation = Propagation.REQUIRED, isolation= Isolation.REPEATABLE_READ)
    public void insertAll(List<NodeBo> list)
    {
        deleteAllV2ray();
        for(NodeBo node : list)
        {
            NodeDetail detail = node.getNodeDetail();
            if(detail.getClass() == ShadowsocksNode.class)
            {
                if(!shadowsocksRIsExists(detail))
                {
                    insertNode(node.getNode());
                    detail.setNodeId(node.getNode().getId());
                    insertShadowsocks(node.getNodeDetail());
                }
                else
                {
                    updateShadowsocks(node.getNodeDetail());
                }
            }
            else if(detail.getClass() == V2rayNodeDetail.class)
            {
                insertNode(node.getNode());
                detail.setNodeId(node.getNode().getId());
                insertV2ray(node.getNodeDetail());
            }
            else
            {
                //ssr节点
                ShadowsocksRNode shadowsocksRNode = (ShadowsocksRNode)detail;
                if(!shadowsocksRIsExists(shadowsocksRNode))
                {
                    insertNode(node.getNode());
                    detail.setNodeId(node.getNode().getId());
                    insertShadowsocksR(shadowsocksRNode);
                }
                else
                {
                    updateShadowsocksR(shadowsocksRNode);
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

    public List<NodeBo> getAllssLink() throws Exception {
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
            else
            {
                nodeDetail = AnalysisUtils.analysisVmess(str);
            }
            if(nodeDetail != null)
            {
                Node node = new Node("own", 0);
                nodeList.add(new NodeBo(node, nodeDetail));
            }
        }
        //订阅的节点
        List<Subscribe> list =  subscribeMapping.getAllSubscribe();
        if(list != null || list.size() != 0)
        {
            for(Subscribe subscribe : list)
            {
                if(SubscribeType.SSSTRING.getCode().equals(subscribe.getType()))
                {
                    String[] arr = subscribe.getSubscribe().split(" ");
                    for(String ss : arr)
                    {
                        NodeDetail nodeDetail = AnalysisUtils.analysisShadowsocks(ss);
                        if(nodeDetail != null)
                        {
                            Node node = new Node("subscribe", subscribe.getId());
                            nodeList.add(new NodeBo(node, nodeDetail));
                        }
                    }
                }
                else if(SubscribeType.SSDSTRING.getCode().equals(subscribe.getType()))
                {
                    List<NodeDetail> nodes = AnalysisUtils.analysisShadowsocksD(subscribe.getSubscribe());
                    for(NodeDetail nodeDetail : nodes)
                    {
                        Node node = new Node("subscribe", subscribe.getId());
                        nodeList.add(new NodeBo(node, nodeDetail));
                    }
                }
                else if(SubscribeType.SSDSUBSCRIBE.getCode().equals(subscribe.getType()))
                {
                    String text = HttpUtils.sendGet(subscribe.getSubscribe(), null);
                    List<NodeDetail> nodes = AnalysisUtils.analysisShadowsocksD(text);
                    for(NodeDetail nodeDetail : nodes)
                    {
                        Node node = new Node("subscribe", subscribe.getId());
                        nodeList.add(new NodeBo(node, nodeDetail));
                    }
                }
                else if(SubscribeType.V2RAYNGSUBSCRIBE.getCode().equals(subscribe.getType()))
                {
                    String text = HttpUtils.sendGet(subscribe.getSubscribe(), null);
                    List<NodeDetail> nodes = AnalysisUtils.analysisV2raySubscribe(text);
                    for(NodeDetail nodeDetail : nodes)
                    {
                        Node node = new Node("subscribe", subscribe.getId());
                        nodeList.add(new NodeBo(node, nodeDetail));
                    }
                }
                else if(SubscribeType.SSRSUBSCRIBE.getCode().equals(subscribe.getType()))
                {
                    String text = HttpUtils.sendGet(subscribe.getSubscribe(), null);
                    List<NodeDetail> nodes = AnalysisUtils.analysisSSRSubscribe(text);
                    for(NodeDetail nodeDetail : nodes)
                    {
                        Node node = new Node("subscribe", subscribe.getId());
                        nodeList.add(new NodeBo(node, nodeDetail));
                    }
                }

            }
        }
        return nodeList;
    }


    public void modNode(Node node)
    {
        nodeMapping.modNode(node);
    }

    public void deleteAllV2ray() {
        nodeMapping.deleteAllV2ray();
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

    public String getV2rayNgSubscribe(String isp) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        //获取v2ray节点
        List<NodeDto> v2rayList = nodeMapping.getV2rayNodes(paramMap);
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
        List<NodeDto> ssList = nodeMapping.getShadowsocksNodes(paramMap);
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
        return Base64Util.encodeURLSafe(buffer.toString());
    }

    public String getShadowrocketSubscribe(String isp) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        //获取v2ray节点
        List<NodeDto> v2rayList = nodeMapping.getV2rayNodes(paramMap);
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
        List<NodeDto> ssList = nodeMapping.getShadowsocksNodes(paramMap);
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
        List<NodeDto> ssrList = nodeMapping.getShadowsocksRNodes(paramMap);
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

    public String getQuantumultSubscribe(String isp)
    {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        //获取v2ray节点
        List<NodeDto> v2rayList = nodeMapping.getV2rayNodes(paramMap);
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
        List<NodeDto> ssList = nodeMapping.getShadowsocksNodes(paramMap);
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
        List<NodeDto> ssrList = nodeMapping.getShadowsocksRNodes(paramMap);
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

    public String getShadowsocksRSubscribe(String isp) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
//        //获取ss节点
//        List<NodeDto> ssList = nodeMapping.getShadowsocksNodes(paramMap);
//        //转换成ss对象
//        List<ShadowsocksNode> shadowsocksNodes = ShadowsocksNode.toShadowsocksNodes(ssList);
//        for(int i = 0; i < shadowsocksNodes.size(); i++)
//        {
//            String sub = ShadowsocksNode.parseToShadowsocksRString(shadowsocksNodes.get(i));
//            if(StringUtil.isEmpty(sub))
//            {
//                continue;
//            }
//            buffer.append(sub);
//            if(i != shadowsocksNodes.size() - 1)
//            {
//                buffer.append("\n");
//            }
//        }
        //获取ssr节点
        List<NodeDto> ssrList = nodeMapping.getShadowsocksRNodes(paramMap);
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

    public void getClashSubscribe(OutputStream os, String isp) {
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        //获取v2ray节点
        List<NodeDto> v2rayList = nodeMapping.getV2rayNodes(paramMap);
        //获取ss节点
        List<NodeDto> ssList = nodeMapping.getShadowsocksNodes(paramMap);
        //组名
        String group = NormalConfiguration.webGroup;
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
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("v2rayNode", V2rayNodeDetail.parseToClashMap(V2rayNodeDetail.toV2rayDetails(v2rayList)));
        map.put("ssNode", ShadowsocksNode.parseToClashMap(ShadowsocksNode.toShadowsocksNodes(ssList)));
        map.put("group", group);
        TemplateUtils.format("clash.ftl", map, os);
    }

    public String getPotatsoLiteStr(String isp) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        //获取ss节点
        List<NodeDto> ssList = nodeMapping.getShadowsocksNodes(paramMap);
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
        List<NodeDto> ssrList = nodeMapping.getShadowsocksRNodes(paramMap);
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

    public String getPharosProSubscribe(String isp) {
        StringBuffer buffer = new StringBuffer();
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("flag", 1);
        paramMap.put("isp", isp);
        //获取v2ray节点
        List<NodeDto> v2rayList = nodeMapping.getV2rayNodes(paramMap);
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
        List<NodeDto> ssList = nodeMapping.getShadowsocksNodes(paramMap);
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
        List<NodeDto> ssrList = nodeMapping.getShadowsocksRNodes(paramMap);
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
}