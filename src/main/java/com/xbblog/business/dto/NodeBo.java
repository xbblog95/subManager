package com.xbblog.business.dto;

public class NodeBo {

    private Node node;

    private NodeDetail nodeDetail;


    public NodeBo(Node node, NodeDetail nodeDetail) {
        this.node = node;
        this.nodeDetail = nodeDetail;
    }

    public Node getNode() {
        return node;
    }

    public NodeDetail getNodeDetail() {
        return nodeDetail;
    }
}
