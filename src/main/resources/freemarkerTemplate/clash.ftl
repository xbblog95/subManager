#Config File For clash
# port of HTTP
port: 7890

# port of SOCKS5
socks-port: 7891

# redir port for Linux and macOS
redir-port: 7892

allow-lan: true

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
  enable: true # set true to enable dns (default is false)
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

proxies:
<#list nodes as node>
  <#if node.type == "v2ray">
-
  name: "${node.remarks}"
  type: "vmess"
  server: "${node.ip}"
  port: "${node.port}"
  uuid: "${node.uuid}"
  alterId: "${node.alterId}"
  cipher: "auto"
    <#if node.udp == "1">
  udp: true
    </#if>
    <#if node.camouflageTls?length gt 0 >
  tls: true
  skip-cert-verify: true
    </#if>
    <#if (node.network!"tcp") != "tcp" >
  network: "${node.network}"
    </#if>
    <#if node.camouflageHost?length gt 0 >
  ws-headers:
    Host: "${node.camouflageHost}"
    </#if>
    <#if node.camouflagePath?length gt 0 >
  ws-path: "${node.camouflagePath}"
    </#if>
    <#if node.network == "ws">
  ws-opts:
      <#if node.camouflagePath?length gt 0 >
    path: "${node.camouflagePath}"
      </#if>
      <#if node.camouflageHost?length gt 0 >
    headers:
        Host: "${node.camouflageHost}"
      </#if>
    </#if>
  <#elseif node.type == "trojan">
-
  name: "${node.remarks}"
  type: "trojan"
  server: "${node.ip}"
  port: "${node.port}"
  skip-cert-verify: true
  password: "${node.password}"
    <#if node.udp == "1">
  udp: true
    </#if>
    <#if node.sni?length gt 0>
  sni: ${node.sni}
    </#if>
    <#if node.alpn?length gt 0>
  alpn:
    - ${node.alpn}
    </#if>
  <#elseif node.type == "ss">
-
  name: "${node.remarks}"
  type: "ss"
  server: "${node.ip}"
  port: "${node.port}"
    <#if node.udp == "1">
  udp: true
    </#if>
  cipher: "${node.security}"
  password: "${node.password}"
  <#else>
-
  name : "${node.remarks}"
    <#if node.protocol == "origin" &&  node.protocolParam?length == 0>
  type: "ss"
    <#else >
  type: "ssr"
    </#if>
  server: "${node.ip}"
  port: "${node.port}"
  cipher: "${node.security}"
  password: "${node.password}"
    <#if node.udp == "1">
  udp: true
    </#if>
    <#if node.protocol == "origin" &&  node.protocolParam?length == 0>
  plugin: "obfs"
  plugin-opts:
      <#if node.obfs == "http_simple">
    mode: "http"
    host: "${node.obfsParam}"
      </#if>
      <#if node.obfs == "tls1.2_ticket_auth">
    mode: "tls"
    host: "${node.obfsParam}"
      </#if>
    <#else >
  protocol: "${node.protocol}"
  protocol-param: "${node.protocolParam}"
  obfs: "${node.obfs}"
  obfs-param: "${node.obfsParam}"
    </#if>
  </#if>
</#list>


proxy-groups:
-
  name: "国外流量"
  type: select
  proxies:
  <#list group as nodeGroup>
    - ${nodeGroup.name}
  </#list>
<#list group as nodeGroup>
-
  name: "${nodeGroup.name}"
  type: select
  proxies:
  <#list nodeGroup.nodes as node>
    - '${node.remarks}'
  </#list>
    - DIRECT
</#list>

rules:
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
- DOMAIN-SUFFIX,v2ex.com,DIRECT
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

#国外
- DOMAIN-KEYWORD,amazon,国外流量
- DOMAIN-KEYWORD,google,国外流量
- DOMAIN-KEYWORD,gmail,国外流量
- DOMAIN-KEYWORD,youtube,国外流量
- DOMAIN-KEYWORD,facebook,国外流量
- DOMAIN-SUFFIX,fb.me,国外流量
- DOMAIN-SUFFIX,fbcdn.net,国外流量
- DOMAIN-KEYWORD,twitter,国外流量
- DOMAIN-KEYWORD,instagram,国外流量
- DOMAIN-KEYWORD,dropbox,国外流量
- DOMAIN-KEYWORD,netflix,国外流量
- DOMAIN-SUFFIX,twimg.com,国外流量
- DOMAIN-KEYWORD,blogspot,国外流量
- DOMAIN-SUFFIX,youtu.be,国外流量
- DOMAIN-KEYWORD,whatsapp,国外流量
- DOMAIN-SUFFIX,9to5mac.com,国外流量
- DOMAIN-SUFFIX,abpchina.org,国外流量
- DOMAIN-SUFFIX,adblockplus.org,国外流量
- DOMAIN-SUFFIX,adobe.com,国外流量
- DOMAIN-SUFFIX,alfredapp.com,国外流量
- DOMAIN-SUFFIX,amplitude.com,国外流量
- DOMAIN-SUFFIX,ampproject.org,国外流量
- DOMAIN-SUFFIX,android.com,国外流量
- DOMAIN-SUFFIX,angularjs.org,国外流量
- DOMAIN-SUFFIX,aolcdn.com,国外流量
- DOMAIN-SUFFIX,apkpure.com,国外流量
- DOMAIN-SUFFIX,appledaily.com,国外流量
- DOMAIN-SUFFIX,appshopper.com,国外流量
- DOMAIN-SUFFIX,appspot.com,国外流量
- DOMAIN-SUFFIX,arcgis.com,国外流量
- DOMAIN-SUFFIX,archive.org,国外流量
- DOMAIN-SUFFIX,armorgames.com,国外流量
- DOMAIN-SUFFIX,aspnetcdn.com,国外流量
- DOMAIN-SUFFIX,att.com,国外流量
- DOMAIN-SUFFIX,awsstatic.com,国外流量
- DOMAIN-SUFFIX,azureedge.net,国外流量
- DOMAIN-SUFFIX,azurewebsites.net,国外流量
- DOMAIN-SUFFIX,bing.com,国外流量
- DOMAIN-SUFFIX,bintray.com,国外流量
- DOMAIN-SUFFIX,bit.com,国外流量
- DOMAIN-SUFFIX,bit.ly,国外流量
- DOMAIN-SUFFIX,bitbucket.org,国外流量
- DOMAIN-SUFFIX,bjango.com,国外流量
- DOMAIN-SUFFIX,bkrtx.com,国外流量
- DOMAIN-SUFFIX,blog.com,国外流量
- DOMAIN-SUFFIX,blogcdn.com,国外流量
- DOMAIN-SUFFIX,blogger.com,国外流量
- DOMAIN-SUFFIX,blogsmithmedia.com,国外流量
- DOMAIN-SUFFIX,blogspot.com,国外流量
- DOMAIN-SUFFIX,blogspot.hk,国外流量
- DOMAIN-SUFFIX,bloomberg.com,国外流量
- DOMAIN-SUFFIX,box.com,国外流量
- DOMAIN-SUFFIX,box.net,国外流量
- DOMAIN-SUFFIX,cachefly.net,国外流量
- DOMAIN-SUFFIX,chromium.org,国外流量
- DOMAIN-SUFFIX,cl.ly,国外流量
- DOMAIN-SUFFIX,cloudflare.com,国外流量
- DOMAIN-SUFFIX,cloudfront.net,国外流量
- DOMAIN-SUFFIX,cloudmagic.com,国外流量
- DOMAIN-SUFFIX,cmail19.com,国外流量
- DOMAIN-SUFFIX,cnet.com,国外流量
- DOMAIN-SUFFIX,cocoapods.org,国外流量
- DOMAIN-SUFFIX,comodoca.com,国外流量
- DOMAIN-SUFFIX,crashlytics.com,国外流量
- DOMAIN-SUFFIX,culturedcode.com,国外流量
- DOMAIN-SUFFIX,d.pr,国外流量
- DOMAIN-SUFFIX,danilo.to,国外流量
- DOMAIN-SUFFIX,dayone.me,国外流量
- DOMAIN-SUFFIX,db.tt,国外流量
- DOMAIN-SUFFIX,deskconnect.com,国外流量
- DOMAIN-SUFFIX,digicert.com,国外流量
- DOMAIN-SUFFIX,disq.us,国外流量
- DOMAIN-SUFFIX,disqus.com,国外流量
- DOMAIN-SUFFIX,disquscdn.com,国外流量
- DOMAIN-SUFFIX,dnsimple.com,国外流量
- DOMAIN-SUFFIX,docker.com,国外流量
- DOMAIN-SUFFIX,dribbble.com,国外流量
- DOMAIN-SUFFIX,droplr.com,国外流量
- DOMAIN-SUFFIX,duckduckgo.com,国外流量
- DOMAIN-SUFFIX,dueapp.com,国外流量
- DOMAIN-SUFFIX,dytt8.net,国外流量
- DOMAIN-SUFFIX,edgecastcdn.net,国外流量
- DOMAIN-SUFFIX,edgekey.net,国外流量
- DOMAIN-SUFFIX,edgesuite.net,国外流量
- DOMAIN-SUFFIX,engadget.com,国外流量
- DOMAIN-SUFFIX,entrust.net,国外流量
- DOMAIN-SUFFIX,eurekavpt.com,国外流量
- DOMAIN-SUFFIX,evernote.com,国外流量
- DOMAIN-SUFFIX,fabric.io,国外流量
- DOMAIN-SUFFIX,fast.com,国外流量
- DOMAIN-SUFFIX,fastly.net,国外流量
- DOMAIN-SUFFIX,fc2.com,国外流量
- DOMAIN-SUFFIX,feedburner.com,国外流量
- DOMAIN-SUFFIX,feedly.com,国外流量
- DOMAIN-SUFFIX,feedsportal.com,国外流量
- DOMAIN-SUFFIX,fiftythree.com,国外流量
- DOMAIN-SUFFIX,firebaseio.com,国外流量
- DOMAIN-SUFFIX,flexibits.com,国外流量
- DOMAIN-SUFFIX,flickr.com,国外流量
- DOMAIN-SUFFIX,flipboard.com,国外流量
- DOMAIN-SUFFIX,g.co,国外流量
- DOMAIN-SUFFIX,gabia.net,国外流量
- DOMAIN-SUFFIX,geni.us,国外流量
- DOMAIN-SUFFIX,gfx.ms,国外流量
- DOMAIN-SUFFIX,ggpht.com,国外流量
- DOMAIN-SUFFIX,ghostnoteapp.com,国外流量
- DOMAIN-SUFFIX,git.io,国外流量
- DOMAIN-KEYWORD,github,国外流量
- DOMAIN-SUFFIX,globalsign.com,国外流量
- DOMAIN-SUFFIX,gmodules.com,国外流量
- DOMAIN-SUFFIX,godaddy.com,国外流量
- DOMAIN-SUFFIX,golang.org,国外流量
- DOMAIN-SUFFIX,gongm.in,国外流量
- DOMAIN-SUFFIX,goo.gl,国外流量
- DOMAIN-SUFFIX,goodreaders.com,国外流量
- DOMAIN-SUFFIX,goodreads.com,国外流量
- DOMAIN-SUFFIX,gravatar.com,国外流量
- DOMAIN-SUFFIX,gstatic.com,国外流量
- DOMAIN-SUFFIX,gvt0.com,国外流量
- DOMAIN-SUFFIX,hockeyapp.net,国外流量
- DOMAIN-SUFFIX,hotmail.com,国外流量
- DOMAIN-SUFFIX,icons8.com,国外流量
- DOMAIN-SUFFIX,ift.tt,国外流量
- DOMAIN-SUFFIX,ifttt.com,国外流量
- DOMAIN-SUFFIX,iherb.com,国外流量
- DOMAIN-SUFFIX,imageshack.us,国外流量
- DOMAIN-SUFFIX,img.ly,国外流量
- DOMAIN-SUFFIX,imgur.com,国外流量
- DOMAIN-SUFFIX,imore.com,国外流量
- DOMAIN-SUFFIX,instapaper.com,国外流量
- DOMAIN-SUFFIX,ipn.li,国外流量
- DOMAIN-SUFFIX,is.gd,国外流量
- DOMAIN-SUFFIX,issuu.com,国外流量
- DOMAIN-SUFFIX,itgonglun.com,国外流量
- DOMAIN-SUFFIX,itun.es,国外流量
- DOMAIN-SUFFIX,ixquick.com,国外流量
- DOMAIN-SUFFIX,j.mp,国外流量
- DOMAIN-SUFFIX,js.revsci.net,国外流量
- DOMAIN-SUFFIX,jshint.com,国外流量
- DOMAIN-SUFFIX,jtvnw.net,国外流量
- DOMAIN-SUFFIX,justgetflux.com,国外流量
- DOMAIN-SUFFIX,kat.cr,国外流量
- DOMAIN-SUFFIX,klip.me,国外流量
- DOMAIN-SUFFIX,libsyn.com,国外流量
- DOMAIN-SUFFIX,licdn.com,国外流量
- DOMAIN-SUFFIX,linkedin.com,国外流量
- DOMAIN-SUFFIX,linode.com,国外流量
- DOMAIN-SUFFIX,lithium.com,国外流量
- DOMAIN-SUFFIX,littlehj.com,国外流量
- DOMAIN-SUFFIX,live.com,国外流量
- DOMAIN-SUFFIX,live.net,国外流量
- DOMAIN-SUFFIX,livefilestore.com,国外流量
- DOMAIN-SUFFIX,llnwd.net,国外流量
- DOMAIN-SUFFIX,macid.co,国外流量
- DOMAIN-SUFFIX,macromedia.com,国外流量
- DOMAIN-SUFFIX,macrumors.com,国外流量
- DOMAIN-SUFFIX,mashable.com,国外流量
- DOMAIN-SUFFIX,mathjax.org,国外流量
- DOMAIN-SUFFIX,medium.com,国外流量
- DOMAIN-SUFFIX,mega.co.nz,国外流量
- DOMAIN-SUFFIX,mega.nz,国外流量
- DOMAIN-SUFFIX,megaupload.com,国外流量
- DOMAIN-SUFFIX,microsofttranslator.com,国外流量
- DOMAIN-SUFFIX,mindnode.com,国外流量
- DOMAIN-SUFFIX,mobile01.com,国外流量
- DOMAIN-SUFFIX,modmyi.com,国外流量
- DOMAIN-SUFFIX,msedge.net,国外流量
- DOMAIN-SUFFIX,myfontastic.com,国外流量
- DOMAIN-SUFFIX,name.com,国外流量
- DOMAIN-SUFFIX,nextmedia.com,国外流量
- DOMAIN-SUFFIX,nsstatic.net,国外流量
- DOMAIN-SUFFIX,nssurge.com,国外流量
- DOMAIN-SUFFIX,nyt.com,国外流量
- DOMAIN-SUFFIX,nytimes.com,国外流量
- DOMAIN-SUFFIX,omnigroup.com,国外流量
- DOMAIN-SUFFIX,onedrive.com,国外流量
- DOMAIN-SUFFIX,onenote.com,国外流量
- DOMAIN-SUFFIX,ooyala.com,国外流量
- DOMAIN-SUFFIX,openvpn.net,国外流量
- DOMAIN-SUFFIX,openwrt.org,国外流量
- DOMAIN-SUFFIX,orkut.com,国外流量
- DOMAIN-SUFFIX,osxdaily.com,国外流量
- DOMAIN-SUFFIX,outlook.com,国外流量
- DOMAIN-SUFFIX,ow.ly,国外流量
- DOMAIN-SUFFIX,paddleapi.com,国外流量
- DOMAIN-SUFFIX,parallels.com,国外流量
- DOMAIN-SUFFIX,parse.com,国外流量
- DOMAIN-SUFFIX,pdfexpert.com,国外流量
- DOMAIN-SUFFIX,periscope.tv,国外流量
- DOMAIN-SUFFIX,pinboard.in,国外流量
- DOMAIN-SUFFIX,pinterest.com,国外流量
- DOMAIN-SUFFIX,pixelmator.com,国外流量
- DOMAIN-SUFFIX,pixiv.net,国外流量
- DOMAIN-SUFFIX,playpcesor.com,国外流量
- DOMAIN-SUFFIX,playstation.com,国外流量
- DOMAIN-SUFFIX,playstation.com.hk,国外流量
- DOMAIN-SUFFIX,playstation.net,国外流量
- DOMAIN-SUFFIX,playstationnetwork.com,国外流量
- DOMAIN-SUFFIX,pushwoosh.com,国外流量
- DOMAIN-SUFFIX,rime.im,国外流量
- DOMAIN-SUFFIX,servebom.com,国外流量
- DOMAIN-SUFFIX,sfx.ms,国外流量
- DOMAIN-SUFFIX,shadowsocks.org,国外流量
- DOMAIN-SUFFIX,sharethis.com,国外流量
- DOMAIN-SUFFIX,shazam.com,国外流量
- DOMAIN-SUFFIX,skype.com,国外流量
- DOMAIN-SUFFIX,smartdnsProxy.com,国外流量
- DOMAIN-SUFFIX,smartmailcloud.com,国外流量
- DOMAIN-SUFFIX,sndcdn.com,国外流量
- DOMAIN-SUFFIX,sony.com,国外流量
- DOMAIN-SUFFIX,soundcloud.com,国外流量
- DOMAIN-SUFFIX,sourceforge.net,国外流量
- DOMAIN-SUFFIX,spotify.com,国外流量
- DOMAIN-SUFFIX,squarespace.com,国外流量
- DOMAIN-SUFFIX,sstatic.net,国外流量
- DOMAIN-SUFFIX,st.luluku.pw,国外流量
- DOMAIN-SUFFIX,stackoverflow.com,国外流量
- DOMAIN-SUFFIX,startpage.com,国外流量
- DOMAIN-SUFFIX,staticflickr.com,国外流量
- DOMAIN-SUFFIX,steamcommunity.com,国外流量
- DOMAIN-SUFFIX,symauth.com,国外流量
- DOMAIN-SUFFIX,symcb.com,国外流量
- DOMAIN-SUFFIX,symcd.com,国外流量
- DOMAIN-SUFFIX,tapbots.com,国外流量
- DOMAIN-SUFFIX,tapbots.net,国外流量
- DOMAIN-SUFFIX,tdesktop.com,国外流量
- DOMAIN-SUFFIX,techcrunch.com,国外流量
- DOMAIN-SUFFIX,techsmith.com,国外流量
- DOMAIN-SUFFIX,thepiratebay.org,国外流量
- DOMAIN-SUFFIX,theverge.com,国外流量
- DOMAIN-SUFFIX,time.com,国外流量
- DOMAIN-SUFFIX,timeinc.net,国外流量
- DOMAIN-SUFFIX,tiny.cc,国外流量
- DOMAIN-SUFFIX,tinypic.com,国外流量
- DOMAIN-SUFFIX,tmblr.co,国外流量
- DOMAIN-SUFFIX,todoist.com,国外流量
- DOMAIN-SUFFIX,trello.com,国外流量
- DOMAIN-SUFFIX,trustasiassl.com,国外流量
- DOMAIN-SUFFIX,tumblr.co,国外流量
- DOMAIN-SUFFIX,tumblr.com,国外流量
- DOMAIN-SUFFIX,tweetdeck.com,国外流量
- DOMAIN-SUFFIX,tweetmarker.net,国外流量
- DOMAIN-SUFFIX,twitch.tv,国外流量
- DOMAIN-SUFFIX,txmblr.com,国外流量
- DOMAIN-SUFFIX,typekit.net,国外流量
- DOMAIN-SUFFIX,ubertags.com,国外流量
- DOMAIN-SUFFIX,ublock.org,国外流量
- DOMAIN-SUFFIX,ubnt.com,国外流量
- DOMAIN-SUFFIX,ulyssesapp.com,国外流量
- DOMAIN-SUFFIX,urchin.com,国外流量
- DOMAIN-SUFFIX,usertrust.com,国外流量
- DOMAIN-SUFFIX,v.gd,国外流量
- DOMAIN-SUFFIX,vimeo.com,国外流量
- DOMAIN-SUFFIX,vimeocdn.com,国外流量
- DOMAIN-SUFFIX,vine.co,国外流量
- DOMAIN-SUFFIX,vivaldi.com,国外流量
- DOMAIN-SUFFIX,vox-cdn.com,国外流量
- DOMAIN-SUFFIX,vsco.co,国外流量
- DOMAIN-SUFFIX,vultr.com,国外流量
- DOMAIN-SUFFIX,w.org,国外流量
- DOMAIN-SUFFIX,w3schools.com,国外流量
- DOMAIN-SUFFIX,webtype.com,国外流量
- DOMAIN-SUFFIX,wikiwand.com,国外流量
- DOMAIN-SUFFIX,wikileaks.org,国外流量
- DOMAIN-SUFFIX,wikimedia.org,国外流量
- DOMAIN-SUFFIX,wikipedia.com,国外流量
- DOMAIN-SUFFIX,wikipedia.org,国外流量
- DOMAIN-SUFFIX,windows.com,国外流量
- DOMAIN-SUFFIX,windows.net,国外流量
- DOMAIN-SUFFIX,wire.com,国外流量
- DOMAIN-SUFFIX,wordpress.com,国外流量
- DOMAIN-SUFFIX,workflowy.com,国外流量
- DOMAIN-SUFFIX,wp.com,国外流量
- DOMAIN-SUFFIX,wsj.com,国外流量
- DOMAIN-SUFFIX,wsj.net,国外流量
- DOMAIN-SUFFIX,xda-developers.com,国外流量
- DOMAIN-SUFFIX,xeeno.com,国外流量
- DOMAIN-SUFFIX,xiti.com,国外流量
- DOMAIN-SUFFIX,yahoo.com,国外流量
- DOMAIN-SUFFIX,yimg.com,国外流量
- DOMAIN-SUFFIX,ying.com,国外流量
- DOMAIN-SUFFIX,yoyo.org,国外流量
- DOMAIN-SUFFIX,ytimg.com,国外流量

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
- MATCH, ,国外流量