#Config File For ${group}
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

proxies:
<#list v2rayNode as vmess>
- { "name": "${vmess.remarks}", "type": "vmess", "server": "${vmess.ip}", "port": "${vmess.port}", "uuid": "${vmess.uuid}", "alterId": "${vmess.alterId}", "cipher": "auto"<#if vmess.camouflageTls?length gt 0 >,"tls": true</#if><#if (vmess.network!"tcp") != "tcp" >,"network": "${vmess.network}"</#if><#if vmess.camouflageHost?length gt 0 >,"ws-headers": { "Host": "${vmess.camouflageHost}" }</#if><#if vmess.camouflagePath?length gt 0 >,"ws-path": "${vmess.camouflagePath}"</#if>}
</#list>
<#list ssNode as ss>
- { "name": "${ss.remarks}", "type": "ss", "server": "${ss.ip}", "port": "${ss.port}", "cipher": "${ss.security}", "password": "${ss.password}"}
</#list>
<#list ssrNode as ssr>
- { "name" : "${ssr.remarks}", "type": "ssr", "server": "${ssr.ip}", "port": "${ssr.port}", "cipher": "${ssr.security}", "password": "${ssr.password}", "protocol": "${ssr.protocol}", "protocol-param":"${ssr.protocolParam}", "obfs":"${ssr.obfs}", "obfs-param":"${ssr.obfsParam}"}
</#list>

proxy-groups:
-
  name: "${group}"
  type: select
  proxies:
    <#list v2rayNode as vmess>
      - '${vmess.remarks}'
    </#list>
    <#list ssNode as ss>
      - '${ss.remarks}'
    </#list>
    <#list ssrNode as ssr>
      - '${ssr.remarks}'
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
- DOMAIN-KEYWORD,amazon,${group}
- DOMAIN-KEYWORD,google,${group}
- DOMAIN-KEYWORD,gmail,${group}
- DOMAIN-KEYWORD,youtube,${group}
- DOMAIN-KEYWORD,facebook,${group}
- DOMAIN-SUFFIX,fb.me,${group}
- DOMAIN-SUFFIX,fbcdn.net,${group}
- DOMAIN-KEYWORD,twitter,${group}
- DOMAIN-KEYWORD,instagram,${group}
- DOMAIN-KEYWORD,dropbox,${group}
- DOMAIN-KEYWORD,netflix,${group}
- DOMAIN-SUFFIX,twimg.com,${group}
- DOMAIN-KEYWORD,blogspot,${group}
- DOMAIN-SUFFIX,youtu.be,${group}
- DOMAIN-KEYWORD,whatsapp,${group}
- DOMAIN-SUFFIX,9to5mac.com,${group}
- DOMAIN-SUFFIX,abpchina.org,${group}
- DOMAIN-SUFFIX,adblockplus.org,${group}
- DOMAIN-SUFFIX,adobe.com,${group}
- DOMAIN-SUFFIX,alfredapp.com,${group}
- DOMAIN-SUFFIX,amplitude.com,${group}
- DOMAIN-SUFFIX,ampproject.org,${group}
- DOMAIN-SUFFIX,android.com,${group}
- DOMAIN-SUFFIX,angularjs.org,${group}
- DOMAIN-SUFFIX,aolcdn.com,${group}
- DOMAIN-SUFFIX,apkpure.com,${group}
- DOMAIN-SUFFIX,appledaily.com,${group}
- DOMAIN-SUFFIX,appshopper.com,${group}
- DOMAIN-SUFFIX,appspot.com,${group}
- DOMAIN-SUFFIX,arcgis.com,${group}
- DOMAIN-SUFFIX,archive.org,${group}
- DOMAIN-SUFFIX,armorgames.com,${group}
- DOMAIN-SUFFIX,aspnetcdn.com,${group}
- DOMAIN-SUFFIX,att.com,${group}
- DOMAIN-SUFFIX,awsstatic.com,${group}
- DOMAIN-SUFFIX,azureedge.net,${group}
- DOMAIN-SUFFIX,azurewebsites.net,${group}
- DOMAIN-SUFFIX,bing.com,${group}
- DOMAIN-SUFFIX,bintray.com,${group}
- DOMAIN-SUFFIX,bit.com,${group}
- DOMAIN-SUFFIX,bit.ly,${group}
- DOMAIN-SUFFIX,bitbucket.org,${group}
- DOMAIN-SUFFIX,bjango.com,${group}
- DOMAIN-SUFFIX,bkrtx.com,${group}
- DOMAIN-SUFFIX,blog.com,${group}
- DOMAIN-SUFFIX,blogcdn.com,${group}
- DOMAIN-SUFFIX,blogger.com,${group}
- DOMAIN-SUFFIX,blogsmithmedia.com,${group}
- DOMAIN-SUFFIX,blogspot.com,${group}
- DOMAIN-SUFFIX,blogspot.hk,${group}
- DOMAIN-SUFFIX,bloomberg.com,${group}
- DOMAIN-SUFFIX,box.com,${group}
- DOMAIN-SUFFIX,box.net,${group}
- DOMAIN-SUFFIX,cachefly.net,${group}
- DOMAIN-SUFFIX,chromium.org,${group}
- DOMAIN-SUFFIX,cl.ly,${group}
- DOMAIN-SUFFIX,cloudflare.com,${group}
- DOMAIN-SUFFIX,cloudfront.net,${group}
- DOMAIN-SUFFIX,cloudmagic.com,${group}
- DOMAIN-SUFFIX,cmail19.com,${group}
- DOMAIN-SUFFIX,cnet.com,${group}
- DOMAIN-SUFFIX,cocoapods.org,${group}
- DOMAIN-SUFFIX,comodoca.com,${group}
- DOMAIN-SUFFIX,crashlytics.com,${group}
- DOMAIN-SUFFIX,culturedcode.com,${group}
- DOMAIN-SUFFIX,d.pr,${group}
- DOMAIN-SUFFIX,danilo.to,${group}
- DOMAIN-SUFFIX,dayone.me,${group}
- DOMAIN-SUFFIX,db.tt,${group}
- DOMAIN-SUFFIX,deskconnect.com,${group}
- DOMAIN-SUFFIX,digicert.com,${group}
- DOMAIN-SUFFIX,disq.us,${group}
- DOMAIN-SUFFIX,disqus.com,${group}
- DOMAIN-SUFFIX,disquscdn.com,${group}
- DOMAIN-SUFFIX,dnsimple.com,${group}
- DOMAIN-SUFFIX,docker.com,${group}
- DOMAIN-SUFFIX,dribbble.com,${group}
- DOMAIN-SUFFIX,droplr.com,${group}
- DOMAIN-SUFFIX,duckduckgo.com,${group}
- DOMAIN-SUFFIX,dueapp.com,${group}
- DOMAIN-SUFFIX,dytt8.net,${group}
- DOMAIN-SUFFIX,edgecastcdn.net,${group}
- DOMAIN-SUFFIX,edgekey.net,${group}
- DOMAIN-SUFFIX,edgesuite.net,${group}
- DOMAIN-SUFFIX,engadget.com,${group}
- DOMAIN-SUFFIX,entrust.net,${group}
- DOMAIN-SUFFIX,eurekavpt.com,${group}
- DOMAIN-SUFFIX,evernote.com,${group}
- DOMAIN-SUFFIX,fabric.io,${group}
- DOMAIN-SUFFIX,fast.com,${group}
- DOMAIN-SUFFIX,fastly.net,${group}
- DOMAIN-SUFFIX,fc2.com,${group}
- DOMAIN-SUFFIX,feedburner.com,${group}
- DOMAIN-SUFFIX,feedly.com,${group}
- DOMAIN-SUFFIX,feedsportal.com,${group}
- DOMAIN-SUFFIX,fiftythree.com,${group}
- DOMAIN-SUFFIX,firebaseio.com,${group}
- DOMAIN-SUFFIX,flexibits.com,${group}
- DOMAIN-SUFFIX,flickr.com,${group}
- DOMAIN-SUFFIX,flipboard.com,${group}
- DOMAIN-SUFFIX,g.co,${group}
- DOMAIN-SUFFIX,gabia.net,${group}
- DOMAIN-SUFFIX,geni.us,${group}
- DOMAIN-SUFFIX,gfx.ms,${group}
- DOMAIN-SUFFIX,ggpht.com,${group}
- DOMAIN-SUFFIX,ghostnoteapp.com,${group}
- DOMAIN-SUFFIX,git.io,${group}
- DOMAIN-KEYWORD,github,${group}
- DOMAIN-SUFFIX,globalsign.com,${group}
- DOMAIN-SUFFIX,gmodules.com,${group}
- DOMAIN-SUFFIX,godaddy.com,${group}
- DOMAIN-SUFFIX,golang.org,${group}
- DOMAIN-SUFFIX,gongm.in,${group}
- DOMAIN-SUFFIX,goo.gl,${group}
- DOMAIN-SUFFIX,goodreaders.com,${group}
- DOMAIN-SUFFIX,goodreads.com,${group}
- DOMAIN-SUFFIX,gravatar.com,${group}
- DOMAIN-SUFFIX,gstatic.com,${group}
- DOMAIN-SUFFIX,gvt0.com,${group}
- DOMAIN-SUFFIX,hockeyapp.net,${group}
- DOMAIN-SUFFIX,hotmail.com,${group}
- DOMAIN-SUFFIX,icons8.com,${group}
- DOMAIN-SUFFIX,ift.tt,${group}
- DOMAIN-SUFFIX,ifttt.com,${group}
- DOMAIN-SUFFIX,iherb.com,${group}
- DOMAIN-SUFFIX,imageshack.us,${group}
- DOMAIN-SUFFIX,img.ly,${group}
- DOMAIN-SUFFIX,imgur.com,${group}
- DOMAIN-SUFFIX,imore.com,${group}
- DOMAIN-SUFFIX,instapaper.com,${group}
- DOMAIN-SUFFIX,ipn.li,${group}
- DOMAIN-SUFFIX,is.gd,${group}
- DOMAIN-SUFFIX,issuu.com,${group}
- DOMAIN-SUFFIX,itgonglun.com,${group}
- DOMAIN-SUFFIX,itun.es,${group}
- DOMAIN-SUFFIX,ixquick.com,${group}
- DOMAIN-SUFFIX,j.mp,${group}
- DOMAIN-SUFFIX,js.revsci.net,${group}
- DOMAIN-SUFFIX,jshint.com,${group}
- DOMAIN-SUFFIX,jtvnw.net,${group}
- DOMAIN-SUFFIX,justgetflux.com,${group}
- DOMAIN-SUFFIX,kat.cr,${group}
- DOMAIN-SUFFIX,klip.me,${group}
- DOMAIN-SUFFIX,libsyn.com,${group}
- DOMAIN-SUFFIX,licdn.com,${group}
- DOMAIN-SUFFIX,linkedin.com,${group}
- DOMAIN-SUFFIX,linode.com,${group}
- DOMAIN-SUFFIX,lithium.com,${group}
- DOMAIN-SUFFIX,littlehj.com,${group}
- DOMAIN-SUFFIX,live.com,${group}
- DOMAIN-SUFFIX,live.net,${group}
- DOMAIN-SUFFIX,livefilestore.com,${group}
- DOMAIN-SUFFIX,llnwd.net,${group}
- DOMAIN-SUFFIX,macid.co,${group}
- DOMAIN-SUFFIX,macromedia.com,${group}
- DOMAIN-SUFFIX,macrumors.com,${group}
- DOMAIN-SUFFIX,mashable.com,${group}
- DOMAIN-SUFFIX,mathjax.org,${group}
- DOMAIN-SUFFIX,medium.com,${group}
- DOMAIN-SUFFIX,mega.co.nz,${group}
- DOMAIN-SUFFIX,mega.nz,${group}
- DOMAIN-SUFFIX,megaupload.com,${group}
- DOMAIN-SUFFIX,microsofttranslator.com,${group}
- DOMAIN-SUFFIX,mindnode.com,${group}
- DOMAIN-SUFFIX,mobile01.com,${group}
- DOMAIN-SUFFIX,modmyi.com,${group}
- DOMAIN-SUFFIX,msedge.net,${group}
- DOMAIN-SUFFIX,myfontastic.com,${group}
- DOMAIN-SUFFIX,name.com,${group}
- DOMAIN-SUFFIX,nextmedia.com,${group}
- DOMAIN-SUFFIX,nsstatic.net,${group}
- DOMAIN-SUFFIX,nssurge.com,${group}
- DOMAIN-SUFFIX,nyt.com,${group}
- DOMAIN-SUFFIX,nytimes.com,${group}
- DOMAIN-SUFFIX,omnigroup.com,${group}
- DOMAIN-SUFFIX,onedrive.com,${group}
- DOMAIN-SUFFIX,onenote.com,${group}
- DOMAIN-SUFFIX,ooyala.com,${group}
- DOMAIN-SUFFIX,openvpn.net,${group}
- DOMAIN-SUFFIX,openwrt.org,${group}
- DOMAIN-SUFFIX,orkut.com,${group}
- DOMAIN-SUFFIX,osxdaily.com,${group}
- DOMAIN-SUFFIX,outlook.com,${group}
- DOMAIN-SUFFIX,ow.ly,${group}
- DOMAIN-SUFFIX,paddleapi.com,${group}
- DOMAIN-SUFFIX,parallels.com,${group}
- DOMAIN-SUFFIX,parse.com,${group}
- DOMAIN-SUFFIX,pdfexpert.com,${group}
- DOMAIN-SUFFIX,periscope.tv,${group}
- DOMAIN-SUFFIX,pinboard.in,${group}
- DOMAIN-SUFFIX,pinterest.com,${group}
- DOMAIN-SUFFIX,pixelmator.com,${group}
- DOMAIN-SUFFIX,pixiv.net,${group}
- DOMAIN-SUFFIX,playpcesor.com,${group}
- DOMAIN-SUFFIX,playstation.com,${group}
- DOMAIN-SUFFIX,playstation.com.hk,${group}
- DOMAIN-SUFFIX,playstation.net,${group}
- DOMAIN-SUFFIX,playstationnetwork.com,${group}
- DOMAIN-SUFFIX,pushwoosh.com,${group}
- DOMAIN-SUFFIX,rime.im,${group}
- DOMAIN-SUFFIX,servebom.com,${group}
- DOMAIN-SUFFIX,sfx.ms,${group}
- DOMAIN-SUFFIX,shadowsocks.org,${group}
- DOMAIN-SUFFIX,sharethis.com,${group}
- DOMAIN-SUFFIX,shazam.com,${group}
- DOMAIN-SUFFIX,skype.com,${group}
- DOMAIN-SUFFIX,smartdnsProxy.com,${group}
- DOMAIN-SUFFIX,smartmailcloud.com,${group}
- DOMAIN-SUFFIX,sndcdn.com,${group}
- DOMAIN-SUFFIX,sony.com,${group}
- DOMAIN-SUFFIX,soundcloud.com,${group}
- DOMAIN-SUFFIX,sourceforge.net,${group}
- DOMAIN-SUFFIX,spotify.com,${group}
- DOMAIN-SUFFIX,squarespace.com,${group}
- DOMAIN-SUFFIX,sstatic.net,${group}
- DOMAIN-SUFFIX,st.luluku.pw,${group}
- DOMAIN-SUFFIX,stackoverflow.com,${group}
- DOMAIN-SUFFIX,startpage.com,${group}
- DOMAIN-SUFFIX,staticflickr.com,${group}
- DOMAIN-SUFFIX,steamcommunity.com,${group}
- DOMAIN-SUFFIX,symauth.com,${group}
- DOMAIN-SUFFIX,symcb.com,${group}
- DOMAIN-SUFFIX,symcd.com,${group}
- DOMAIN-SUFFIX,tapbots.com,${group}
- DOMAIN-SUFFIX,tapbots.net,${group}
- DOMAIN-SUFFIX,tdesktop.com,${group}
- DOMAIN-SUFFIX,techcrunch.com,${group}
- DOMAIN-SUFFIX,techsmith.com,${group}
- DOMAIN-SUFFIX,thepiratebay.org,${group}
- DOMAIN-SUFFIX,theverge.com,${group}
- DOMAIN-SUFFIX,time.com,${group}
- DOMAIN-SUFFIX,timeinc.net,${group}
- DOMAIN-SUFFIX,tiny.cc,${group}
- DOMAIN-SUFFIX,tinypic.com,${group}
- DOMAIN-SUFFIX,tmblr.co,${group}
- DOMAIN-SUFFIX,todoist.com,${group}
- DOMAIN-SUFFIX,trello.com,${group}
- DOMAIN-SUFFIX,trustasiassl.com,${group}
- DOMAIN-SUFFIX,tumblr.co,${group}
- DOMAIN-SUFFIX,tumblr.com,${group}
- DOMAIN-SUFFIX,tweetdeck.com,${group}
- DOMAIN-SUFFIX,tweetmarker.net,${group}
- DOMAIN-SUFFIX,twitch.tv,${group}
- DOMAIN-SUFFIX,txmblr.com,${group}
- DOMAIN-SUFFIX,typekit.net,${group}
- DOMAIN-SUFFIX,ubertags.com,${group}
- DOMAIN-SUFFIX,ublock.org,${group}
- DOMAIN-SUFFIX,ubnt.com,${group}
- DOMAIN-SUFFIX,ulyssesapp.com,${group}
- DOMAIN-SUFFIX,urchin.com,${group}
- DOMAIN-SUFFIX,usertrust.com,${group}
- DOMAIN-SUFFIX,v.gd,${group}
- DOMAIN-SUFFIX,vimeo.com,${group}
- DOMAIN-SUFFIX,vimeocdn.com,${group}
- DOMAIN-SUFFIX,vine.co,${group}
- DOMAIN-SUFFIX,vivaldi.com,${group}
- DOMAIN-SUFFIX,vox-cdn.com,${group}
- DOMAIN-SUFFIX,vsco.co,${group}
- DOMAIN-SUFFIX,vultr.com,${group}
- DOMAIN-SUFFIX,w.org,${group}
- DOMAIN-SUFFIX,w3schools.com,${group}
- DOMAIN-SUFFIX,webtype.com,${group}
- DOMAIN-SUFFIX,wikiwand.com,${group}
- DOMAIN-SUFFIX,wikileaks.org,${group}
- DOMAIN-SUFFIX,wikimedia.org,${group}
- DOMAIN-SUFFIX,wikipedia.com,${group}
- DOMAIN-SUFFIX,wikipedia.org,${group}
- DOMAIN-SUFFIX,windows.com,${group}
- DOMAIN-SUFFIX,windows.net,${group}
- DOMAIN-SUFFIX,wire.com,${group}
- DOMAIN-SUFFIX,wordpress.com,${group}
- DOMAIN-SUFFIX,workflowy.com,${group}
- DOMAIN-SUFFIX,wp.com,${group}
- DOMAIN-SUFFIX,wsj.com,${group}
- DOMAIN-SUFFIX,wsj.net,${group}
- DOMAIN-SUFFIX,xda-developers.com,${group}
- DOMAIN-SUFFIX,xeeno.com,${group}
- DOMAIN-SUFFIX,xiti.com,${group}
- DOMAIN-SUFFIX,yahoo.com,${group}
- DOMAIN-SUFFIX,yimg.com,${group}
- DOMAIN-SUFFIX,ying.com,${group}
- DOMAIN-SUFFIX,yoyo.org,${group}
- DOMAIN-SUFFIX,ytimg.com,${group}

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
- MATCH, ,${group}