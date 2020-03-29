#Config File For ${group}
# port of HTTP
port: 7890

# port of SOCKS5
socks-port: 7891

# redir port for Linux and macOS
# redir-port: 7892

allow-lan: false

# Rule / Global/ Direct (default is Rule)
mode: Rule

# set log level to stdout (default is info)
# info / warning / error / debug / silent
log-level: info

# A RESTful API for clash
external-controller: 127.0.0.1:9090

# you can put the static web resource (such as clash-dashboard) to a directory, and clash would serve in `${r'${API}'}/ui`
# input is a relative path to the configuration directory or an absolute path
# external-ui: folder

# Secret for RESTful API (Optional)
secret: ""

dns:
  enable: false # set true to enable dns (default is false)
  ipv6: false # default is false
  listen: 0.0.0.0:53
  enhanced-mode: redir-host
  nameserver:
  - 223.5.5.5
  - 119.29.29.29
  - tls://dns.rubyfish.cn:853
  fallback:
  - tls://1.0.0.1:853
  - tls://1.1.1.1:853
  - tls://dns.google:853

Proxy:
<#list v2rayNode as vmess>
  - name: "${vmess.remarks}"
    type: "vmess"
    server: "${vmess.ip}"
    port: "${vmess.port}"
    uuid: "${vmess.uuid}"
    alterId: "${vmess.alterId}"
    cipher: "auto"
    <#if vmess.camouflageTls?length gt 0 >tls: true</#if>
    <#if (vmess.network!"tcp") != "tcp" >network: "${vmess.network}"</#if>
    <#if vmess.camouflageHost?length gt 0 >ws-headers: {Host: "${vmess.camouflageHost}"}</#if>
    <#if vmess.camouflagePath?length gt 0 >ws-path: "${vmess.camouflagePath}"</#if>
</#list>
<#list ssNode as ss>
  - name: "${ss.remarks}"
    type: "ss"
    server: "${ss.ip}"
    port: "${ss.port}"
    cipher: "${ss.security}"
    password: "${ss.password}"
</#list>
<#list ssrNode as ssr>
  - name: "${ssr.remarks}"
    type: "ssr"
    server: "${ssr.ip}"
    port: "${ssr.port}"
    cipher: "${ssr.security}"
    password: "${ssr.password}"
    protocol: "${ssr.protocol}"
    protocolparam: "${ssr.protocolparam}"
    obfs: "${ssr.obfs}"
    obfsparam: "${ssr.obfsparam}"
</#list>

Proxy Group:
  - name: Proxy
    type: select
    proxies:
    <#list v2rayNode as vmess>
      - "${vmess.remarks}"
    </#list>
    <#list ssNode as ss>
      - "${ss.remarks}"
    </#list>
    <#list ssrNode as ssr>
      - "${ssr.remarks}"
    </#list>
  - name: GlobalTV
    type: select
    proxies:
    <#list netflixV2rayList as vmess>
      - "${vmess.remarks}"
    </#list>
    <#list netflixSsList as ss>
      - "${ss.remarks}"
    </#list>
    <#list netflixSsrList as ssr>
      - "${ssr.remarks}"
    </#list>

# 规则
Rule:
## 国内网站
- DOMAIN-SUFFIX,cn,DIRECT
- DOMAIN-KEYWORD,-cn,DIRECT
- DOMAIN-SUFFIX,126.com,DIRECT
- DOMAIN-SUFFIX,126.net,DIRECT
- DOMAIN-SUFFIX,127.net,DIRECT
- DOMAIN-SUFFIX,163.com,DIRECT
- DOMAIN-SUFFIX,360buyimg.com,DIRECT
- DOMAIN-SUFFIX,36kr.com,DIRECT
- DOMAIN-SUFFIX,acfun.tv,DIRECT
- DOMAIN-SUFFIX,air-matters.com,DIRECT
- DOMAIN-SUFFIX,aixifan.com,DIRECT
- DOMAIN-SUFFIX,akamaized.net,DIRECT
- DOMAIN-KEYWORD,alicdn,DIRECT
- DOMAIN-KEYWORD,alipay,DIRECT
- DOMAIN-KEYWORD,taobao,DIRECT
- DOMAIN-SUFFIX,amap.com,DIRECT
- DOMAIN-SUFFIX,autonavi.com,DIRECT
- DOMAIN-KEYWORD,baidu,DIRECT
- DOMAIN-SUFFIX,bdimg.com,DIRECT
- DOMAIN-SUFFIX,bdstatic.com,DIRECT
- DOMAIN-SUFFIX,bilibili.com,DIRECT
- DOMAIN-SUFFIX,caiyunapp.com,DIRECT
- DOMAIN-SUFFIX,clouddn.com,DIRECT
- DOMAIN-SUFFIX,cnbeta.com,DIRECT
- DOMAIN-SUFFIX,cnbetacdn.com,DIRECT
- DOMAIN-SUFFIX,cootekservice.com,DIRECT
- DOMAIN-SUFFIX,csdn.net,DIRECT
- DOMAIN-SUFFIX,ctrip.com,DIRECT
- DOMAIN-SUFFIX,dgtle.com,DIRECT
- DOMAIN-SUFFIX,dianping.com,DIRECT
- DOMAIN-SUFFIX,douban.com,DIRECT
- DOMAIN-SUFFIX,doubanio.com,DIRECT
- DOMAIN-SUFFIX,duokan.com,DIRECT
- DOMAIN-SUFFIX,easou.com,DIRECT
- DOMAIN-SUFFIX,ele.me,DIRECT
- DOMAIN-SUFFIX,feng.com,DIRECT
- DOMAIN-SUFFIX,fir.im,DIRECT
- DOMAIN-SUFFIX,frdic.com,DIRECT
- DOMAIN-SUFFIX,g-cores.com,DIRECT
- DOMAIN-SUFFIX,godic.net,DIRECT
- DOMAIN-SUFFIX,gtimg.com,DIRECT
- DOMAIN-SUFFIX,hongxiu.com,DIRECT
- DOMAIN-SUFFIX,hxcdn.net,DIRECT
- DOMAIN-SUFFIX,iciba.com,DIRECT
- DOMAIN-SUFFIX,ifeng.com,DIRECT
- DOMAIN-SUFFIX,ifengimg.com,DIRECT
- DOMAIN-SUFFIX,ipip.net,DIRECT
- DOMAIN-SUFFIX,iqiyi.com,DIRECT
- DOMAIN-SUFFIX,jd.com,DIRECT
- DOMAIN-SUFFIX,jianshu.com,DIRECT
- DOMAIN-SUFFIX,knewone.com,DIRECT
- DOMAIN-SUFFIX,le.com,DIRECT
- DOMAIN-SUFFIX,lecloud.com,DIRECT
- DOMAIN-SUFFIX,lemicp.com,DIRECT
- DOMAIN-SUFFIX,luoo.net,DIRECT
- DOMAIN-SUFFIX,meituan.com,DIRECT
- DOMAIN-SUFFIX,meituan.net,DIRECT
- DOMAIN-SUFFIX,mi.com,DIRECT
- DOMAIN-SUFFIX,miaopai.com,DIRECT
- DOMAIN-SUFFIX,microsoft.com,DIRECT
- DOMAIN-SUFFIX,microsoftonline.com,DIRECT
- DOMAIN-SUFFIX,miui.com,DIRECT
- DOMAIN-SUFFIX,miwifi.com,DIRECT
- DOMAIN-SUFFIX,mob.com,DIRECT
- DOMAIN-SUFFIX,netease.com,DIRECT
- DOMAIN-SUFFIX,office.com,DIRECT
- DOMAIN-KEYWORD,officecdn,DIRECT
- DOMAIN-SUFFIX,office365.com,DIRECT
- DOMAIN-SUFFIX,oschina.net,DIRECT
- DOMAIN-SUFFIX,ppsimg.com,DIRECT
- DOMAIN-SUFFIX,pstatp.com,DIRECT
- DOMAIN-SUFFIX,qcloud.com,DIRECT
- DOMAIN-SUFFIX,qdaily.com,DIRECT
- DOMAIN-SUFFIX,qdmm.com,DIRECT
- DOMAIN-SUFFIX,qhimg.com,DIRECT
- DOMAIN-SUFFIX,qhres.com,DIRECT
- DOMAIN-SUFFIX,qidian.com,DIRECT
- DOMAIN-SUFFIX,qihucdn.com,DIRECT
- DOMAIN-SUFFIX,qiniu.com,DIRECT
- DOMAIN-SUFFIX,qiniucdn.com,DIRECT
- DOMAIN-SUFFIX,qiyipic.com,DIRECT
- DOMAIN-SUFFIX,qq.com,DIRECT
- DOMAIN-SUFFIX,qqurl.com,DIRECT
- DOMAIN-SUFFIX,rarbg.to,DIRECT
- DOMAIN-SUFFIX,ruguoapp.com,DIRECT
- DOMAIN-SUFFIX,segmentfault.com,DIRECT
- DOMAIN-SUFFIX,sinaapp.com,DIRECT
- DOMAIN-SUFFIX,smzdm.com,DIRECT
- DOMAIN-SUFFIX,sogou.com,DIRECT
- DOMAIN-SUFFIX,sogoucdn.com,DIRECT
- DOMAIN-SUFFIX,sohu.com,DIRECT
- DOMAIN-SUFFIX,soku.com,DIRECT
- DOMAIN-SUFFIX,speedtest.net,DIRECT
- DOMAIN-SUFFIX,sspai.com,DIRECT
- DOMAIN-SUFFIX,suning.com,DIRECT
- DOMAIN-SUFFIX,taobao.com,DIRECT
- DOMAIN-SUFFIX,tenpay.com,DIRECT
- DOMAIN-SUFFIX,tmall.com,DIRECT
- DOMAIN-SUFFIX,tudou.com,DIRECT
- DOMAIN-SUFFIX,umetrip.com,DIRECT
- DOMAIN-SUFFIX,upaiyun.com,DIRECT
- DOMAIN-SUFFIX,upyun.com,DIRECT
- DOMAIN-SUFFIX,veryzhun.com,DIRECT
- DOMAIN-SUFFIX,weather.com,DIRECT
- DOMAIN-SUFFIX,weibo.com,DIRECT
- DOMAIN-SUFFIX,xiami.com,DIRECT
- DOMAIN-SUFFIX,xiami.net,DIRECT
- DOMAIN-SUFFIX,xiaomicp.com,DIRECT
- DOMAIN-SUFFIX,ximalaya.com,DIRECT
- DOMAIN-SUFFIX,xmcdn.com,DIRECT
- DOMAIN-SUFFIX,xunlei.com,DIRECT
- DOMAIN-SUFFIX,yhd.com,DIRECT
- DOMAIN-SUFFIX,yihaodianimg.com,DIRECT
- DOMAIN-SUFFIX,yinxiang.com,DIRECT
- DOMAIN-SUFFIX,ykimg.com,DIRECT
- DOMAIN-SUFFIX,youdao.com,DIRECT
- DOMAIN-SUFFIX,youku.com,DIRECT
- DOMAIN-SUFFIX,zealer.com,DIRECT
- DOMAIN-SUFFIX,zhihu.com,DIRECT
- DOMAIN-SUFFIX,zhimg.com,DIRECT
- DOMAIN-SUFFIX,zimuzu.tv,DIRECT

# (GlobalTV)
# > ABC
- DOMAIN-SUFFIX,edgedatg.com,GlobalTV
- DOMAIN-SUFFIX,go.com,GlobalTV

# > AbemaTV
- DOMAIN,linear-abematv.akamaized.net,GlobalTV
- DOMAIN-SUFFIX,abema.io,GlobalTV
- DOMAIN-SUFFIX,abema.tv,GlobalTV
- DOMAIN-SUFFIX,akamaized.net,GlobalTV
- DOMAIN-SUFFIX,ameba.jp,GlobalTV
- DOMAIN-SUFFIX,hayabusa.io,GlobalTV

# > Amazon Prime Video
- DOMAIN-SUFFIX,aiv-cdn.net,GlobalTV
- DOMAIN-SUFFIX,amazonaws.com,GlobalTV
- DOMAIN-SUFFIX,amazonvideo.com,GlobalTV
- DOMAIN-SUFFIX,llnwd.net,GlobalTV

# > Bahamut
- DOMAIN-SUFFIX,bahamut.com.tw,GlobalTV
- DOMAIN-SUFFIX,gamer.com.tw,GlobalTV
- DOMAIN-SUFFIX,hinet.net,GlobalTV

# > BBC
- DOMAIN-KEYWORD,bbcfmt,GlobalTV
- DOMAIN-KEYWORD,co.uk,GlobalTV
- DOMAIN-KEYWORD,uk-live,GlobalTV
- DOMAIN-SUFFIX,bbc.co,GlobalTV
- DOMAIN-SUFFIX,bbc.co.uk,GlobalTV
- DOMAIN-SUFFIX,bbc.com,GlobalTV
- DOMAIN-SUFFIX,bbci.co,GlobalTV
- DOMAIN-SUFFIX,bbci.co.uk,GlobalTV

# > CHOCO TV
- DOMAIN-SUFFIX,chocotv.com.tw,GlobalTV

# > Epicgames
- DOMAIN-KEYWORD,epicgames,GlobalTV
- DOMAIN-SUFFIX,helpshift.com,GlobalTV

# > Fox+
- DOMAIN-KEYWORD,foxplus,GlobalTV
- DOMAIN-SUFFIX,config.fox.com,GlobalTV
- DOMAIN-SUFFIX,emome.net,GlobalTV
- DOMAIN-SUFFIX,fox.com,GlobalTV
- DOMAIN-SUFFIX,foxdcg.com,GlobalTV
- DOMAIN-SUFFIX,foxnow.com,GlobalTV
- DOMAIN-SUFFIX,foxplus.com,GlobalTV
- DOMAIN-SUFFIX,foxplay.com,GlobalTV
- DOMAIN-SUFFIX,ipinfo.io,GlobalTV
- DOMAIN-SUFFIX,mstage.io,GlobalTV
- DOMAIN-SUFFIX,now.com,GlobalTV
- DOMAIN-SUFFIX,theplatform.com,GlobalTV
- DOMAIN-SUFFIX,urlload.net,GlobalTV

# > HBO && HBO Go
- DOMAIN-SUFFIX,execute-api.ap-southeast-1.amazonaws.com,GlobalTV
- DOMAIN-SUFFIX,hbo.com,GlobalTV
- DOMAIN-SUFFIX,hboasia.com,GlobalTV
- DOMAIN-SUFFIX,hbogo.com,GlobalTV
- DOMAIN-SUFFIX,hbogoasia.hk,GlobalTV

# > Hulu
- DOMAIN-SUFFIX,happyon.jp,GlobalTV
- DOMAIN-SUFFIX,hulu.com,GlobalTV
- DOMAIN-SUFFIX,huluim.com,GlobalTV
- DOMAIN-SUFFIX,hulustream.com,GlobalTV

# > Imkan
- DOMAIN-SUFFIX,imkan.tv,GlobalTV

# > JOOX
- DOMAIN-SUFFIX,joox.com,GlobalTV

# > MytvSUPER
- DOMAIN-KEYWORD,nowtv100,GlobalTV
- DOMAIN-KEYWORD,rthklive,GlobalTV
- DOMAIN-SUFFIX,mytvsuper.com,GlobalTV
- DOMAIN-SUFFIX,tvb.com,GlobalTV

# > Netflix
- DOMAIN-SUFFIX,netflix.com,GlobalTV
- DOMAIN-SUFFIX,netflix.net,GlobalTV
- DOMAIN-SUFFIX,nflxext.com,GlobalTV
- DOMAIN-SUFFIX,nflximg.com,GlobalTV
- DOMAIN-SUFFIX,nflximg.net,GlobalTV
- DOMAIN-SUFFIX,nflxso.net,GlobalTV
- DOMAIN-SUFFIX,nflxvideo.net,GlobalTV

# > Pandora
- DOMAIN-SUFFIX,pandora.com,GlobalTV

# > Sky GO
- DOMAIN-SUFFIX,sky.com,GlobalTV
- DOMAIN-SUFFIX,skygo.co.nz,GlobalTV

# > Spotify
- DOMAIN-KEYWORD,spotify,GlobalTV
- DOMAIN-SUFFIX,scdn.co,GlobalTV
- DOMAIN-SUFFIX,spoti.fi,GlobalTV

# > viuTV
- DOMAIN-SUFFIX,viu.tv,GlobalTV

# > Youtube
- DOMAIN-KEYWORD,youtube,GlobalTV
- DOMAIN-SUFFIX,googlevideo.com,GlobalTV
- DOMAIN-SUFFIX,gvt2.com,GlobalTV
- DOMAIN-SUFFIX,youtu.be,GlobalTV

## 常见广告域名
- DOMAIN-KEYWORD,admarvel,REJECT
- DOMAIN-KEYWORD,admaster,REJECT
- DOMAIN-KEYWORD,adsage,REJECT
- DOMAIN-KEYWORD,adsmogo,REJECT
- DOMAIN-KEYWORD,adsrvmedia,REJECT
- DOMAIN-KEYWORD,adwords,REJECT
- DOMAIN-KEYWORD,adservice,REJECT
- DOMAIN-KEYWORD,domob,REJECT
- DOMAIN-KEYWORD,duomeng,REJECT
- DOMAIN-KEYWORD,dwtrack,REJECT
- DOMAIN-KEYWORD,guanggao,REJECT
- DOMAIN-KEYWORD,lianmeng,REJECT
- DOMAIN-KEYWORD,omgmta,REJECT
- DOMAIN-KEYWORD,openx,REJECT
- DOMAIN-KEYWORD,partnerad,REJECT
- DOMAIN-KEYWORD,pingfore,REJECT
- DOMAIN-KEYWORD,supersonicads,REJECT
- DOMAIN-KEYWORD,tracking,REJECT
- DOMAIN-KEYWORD,uedas,REJECT
- DOMAIN-KEYWORD,umeng,REJECT
- DOMAIN-KEYWORD,usage,REJECT
- DOMAIN-KEYWORD,wlmonitor,REJECT
- DOMAIN-KEYWORD,zjtoolbar,REJECT


## LAN
- DOMAIN-SUFFIX,local,DIRECT
- IP-CIDR,127.0.0.0/8,DIRECT
- IP-CIDR,172.16.0.0/12,DIRECT
- IP-CIDR,192.168.0.0/16,DIRECT
- IP-CIDR,10.0.0.0/8,DIRECT
- IP-CIDR,17.0.0.0/8,DIRECT
- IP-CIDR,100.64.0.0/10,DIRECT
- GEOIP,CN,DIRECT
- FINAL, ,Proxy