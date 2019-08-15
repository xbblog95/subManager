package com.xbblog.business.dto;

public class NodeDto extends NodeDetail {

    //v2ray uuid
    private String uuid;

    //v2ray 额外id
    private int alterId;

    //v2ray 网络传输
    private String network;

    //v2ray 伪装类型
    private String camouflageType;

    //v2ray 伪装域名
    private String camouflageHost;

    //v2ray 伪装路径
    private String camouflagePath;

    //v2ray 底层传输协议
    private String camouflageTls;


    //ss/ssr密码
    private String password;

    //ssr混淆参数
    private String obfsparam;

    //ssr协议参数
    private String protoparam;

    //ssr协议
    private String protocol;

    //ssr混淆
    private String obfs;

    //来源
    private String source;

    //有效性
    private int flag;

    //订阅id
    private int subscribeId;

    public NodeDto(String ip, int port, String remarks, String uuid, int alterId,
                           String network, String camouflageType, String camouflageHost, String camouflagePath, String camouflageTls, String password) {
        super(ip, port, remarks, "auto", "v2ray");
        this.uuid = uuid;
        this.alterId = alterId;
        this.network = network == null ? "tcp" : network;
        this.camouflageType = camouflageType == null ? "" : camouflageType;
        this.camouflageHost = camouflageHost == null ? "" : camouflageHost;
        this.camouflagePath = camouflagePath == null ? "" : camouflagePath;
        this.camouflageTls = camouflageTls == null ? "" : camouflageTls;
        this.password = password == null ? "" : password;
    }



    public NodeDto(String ip, int port, String remarks, String security, String type) {
        super(ip, port, remarks, security, type);
    }

    public NodeDto() {
        super();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getAlterId() {
        return alterId;
    }

    public void setAlterId(int alterId) {
        this.alterId = alterId;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getCamouflageType() {
        return camouflageType;
    }

    public void setCamouflageType(String camouflageType) {
        this.camouflageType = camouflageType;
    }

    public String getCamouflageHost() {
        return camouflageHost;
    }

    public void setCamouflageHost(String camouflageHost) {
        this.camouflageHost = camouflageHost;
    }

    public String getCamouflagePath() {
        return camouflagePath;
    }

    public void setCamouflagePath(String camouflagePath) {
        this.camouflagePath = camouflagePath;
    }

    public String getCamouflageTls() {
        return camouflageTls;
    }

    public void setCamouflageTls(String camouflageTls) {
        this.camouflageTls = camouflageTls;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(int subscribeId) {
        this.subscribeId = subscribeId;
    }

    public String getObfsparam() {
        return obfsparam;
    }

    public void setObfsparam(String obfsparam) {
        this.obfsparam = obfsparam;
    }

    public String getProtoparam() {
        return protoparam;
    }

    public void setProtoparam(String protoparam) {
        this.protoparam = protoparam;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getObfs() {
        return obfs;
    }

    public void setObfs(String obfs) {
        this.obfs = obfs;
    }
}
