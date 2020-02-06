<#assign base=request.contextPath />
<!DOCTYPE html>
<html lang="zh-cn" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>一个不知道是啥的网站</title>
    <meta charset="utf-8">
    <base href="${base}"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="${base}/static/base/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${base}/static/base/css/font-awesome.min.css" />
    <!-- Toastr style -->
    <link rel="stylesheet" href="${base}/static/css/plugins/toastr/toastr.min.css"/>
    <link href="${base}/static/css/animate.css" rel="stylesheet">
    <link href="${base}/static/css/style.css" rel="stylesheet">
    <script  type="text/javascript">
        var BASE_PATH = '${base}';
    </script>
</head>
<body style="  background-size: cover;">
<div id="wrapper">
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav metismenu" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element">
                        <img alt="image" class="rounded-circle" src="${base}/static/img/profile_smallest.png"/>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="block m-t-xs font-bold" >${user.name}</span>
                            <span class="text-muted text-xs block">一般用户 <b class="caret"></b></span>
                        </a>
                        <ul class="dropdown-menu animated fadeInRight m-t-xs">
                            <li><a class="dropdown-item" href="${base}/user/toIndex">关于我</a></li>
                            <li><a class="dropdown-item modPass" href="javascript:void(0)" >修改密码</a></li>
                            <li class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="${base}/user/loginOut">注销</a></li>
                        </ul>
                    </div>
                </li>
                <li class="active">
                    <a href="javascript:void(0)"><i class="fa fa-th-large"></i> <span class="nav-label">主页</span> <span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="active"><a href="${base}/user/toIndex">个人中心</a></li>
                    </ul>
                </li>
            </ul>

        </div>
    </nav>

    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">
                </div>
                <ul class="nav navbar-top-links navbar-right">
                    <li style="padding: 20px">
                        <span class="m-r-sm text-muted welcome-message">一个不知道是啥的站点</span>
                    </li>

                    <li>
                        <a href="javascript:void(0)" class="modPass">
                            <i class="fa fa-edit"></i> 修改密码
                        </a>
                    </li>
                    <li>
                        <a href="${base}/user/loginOut">
                            <i class="fa fa-sign-out"></i> 注销
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
        <div class="text-center col-md-12 col-sm-12 col-lg-12" style="display: none;min-width: 300px" id="modPass">
            <form>
                <div class="mb-3">
                    <label for="oldPassword">原密码</label>
                    <input type="password" class="form-control validater" id="oldPassword" name="oldPassword"  placeholder="" value="" data-validate-rule='{
                                                  "required":"true"
                                              }' data-validate-message='{
                                                  "required":"必填项"
                                              }' data-validate-type="bootStrap4Input"  required>
                </div>


                <div class="mb-3">
                    <label for="newPassword">新密码</label>
                    <input type="password" class="form-control validater" id="newPassword" name="newPassword" data-validate-rule='{
                                                  "required":"true",
                                                  "notEqualTo" : "#oldPassword"
                                              }' data-validate-message='{
                                                  "required":"必填项",
                                                  "notEqualTo" : "新旧密码不能一致"
                                              }' data-validate-type="bootStrap4Input" >
                </div>

                <div class="mb-3">
                    <label for="confirmnewPassword">确认新密码</label>
                    <input type="password" class="form-control validater" id="confirmnewPassword" name="confirmnewPassword" data-validate-rule='{
                                                  "required":"true",
                                                  "equalTo" : "#newPassword"
                                              }' data-validate-message='{
                                                  "required":"必填项",
                                                  "equalTo" : "新密码与确认密码不一致"
                                              }' data-validate-type="bootStrap4Input">
                </div>
                <hr class="mb-4">
                <button class="btn btn-primary btn-lg btn-block" type="button" id="modPassSubmit">提交</button>
            </form>
        </div>
        <div class="row  border-bottom white-bg dashboard-header">

            <div class="col-md-12">
                您的v2rayN/v2rayNG(PC/Android客户端)专用订阅地址为：<span id="v2rayNgSub" >${BASE_PATH}link/${user.token}/v2rayNg/getLink</span>
                <br/>
                <a href="javascript:void(0)" class="copy" data-clipboard-action="copy" data-clipboard-target="#v2rayNgSub">点击此处复制订阅地址</a>
                <a href="${base}/static/html/userguide/v2rayNg.html" target="_blank">点击此处查看v2rayNG使用教程</a>
                <br/>
                您的shadowsocksR(PC/Android客户端)专用订阅地址为：<span id="shadowsocksRSub" >${BASE_PATH}link/${user.token}/shadowsocksR/getLink</span>
                <br/>
                <a href="javascript:void(0)" class="copy" data-clipboard-action="copy" data-clipboard-target="#shadowsocksRSub">点击此处复制订阅地址</a>
                <a href="${base}/static/html/userguide/shadowsocksR.html" target="_blank" >点击此处查看shadowsocksR使用教程</a>
                <br/>
                您的shadowrocket(IOS客户端)专用订阅地址为：<span id="shadowrocketSub" >${BASE_PATH}link/${user.token}/shadowrocket/getLink</span>
                <br/>
                <a href="javascript:void(0)" class="copy" data-clipboard-action="copy" data-clipboard-target="#shadowrocketSub">点击此处复制订阅地址</a>
                <a href="${base}/static/html/userguide/shadowrocket.html" target="_blank">点击此处查看shadowrocket使用教程</a>
                <br/>
                您的quantumult(IOS客户端)专用订阅地址为：<span id="quantumultSub" >${BASE_PATH}link/${user.token}/quantumult/getLink</span>
                <br/>
                <a href="javascript:void(0)" class="copy" data-clipboard-action="copy" data-clipboard-target="#quantumultSub">点击此处复制订阅地址</a>
                <a href="${base}/static/html/userguide/quantumult.html" target="_blank">点击此处查看quantumult使用教程</a>
                <br/>
                您的quantumultX(IOS客户端)专用订阅地址为：<span id="quantumultxSub" >${BASE_PATH}link/${user.token}/quantumultx/getLink</span>
                <br/>
                <a href="javascript:void(0)" class="copy" data-clipboard-action="copy" data-clipboard-target="#quantumultxSub">点击此处复制订阅地址</a>
                <br/>
                您的clash(PC客户端)专用托管地址为：<span id="clashSub" >${BASE_PATH}link/${user.token}/clash/getLink</span>
                <br/>
                <a href="javascript:void(0)" class="copy" data-clipboard-action="copy" data-clipboard-target="#clashSub">点击此处复制订阅地址</a>
                <a href="${base}/static/html/userguide/clash.html" target="_blank">点击此处查看clash使用教程</a>
                <br/>
                您的Potatso Lite(IOS客户端)专用订阅地址为：<span id="potatsoSub" >${BASE_PATH}link/${user.token}/potatso/getLink</span>
                <br/>
                <a href="javascript:void(0)" class="copy" data-clipboard-action="copy" data-clipboard-target="#potatsoSub">点击此处复制订阅地址</a>
                <a href="${base}/static/html/userguide/potatso.html" target="_blank">点击此处查看Potatso Lite使用教程</a>
                <br/>
                您的Pharos Pro(IOS客户端)专用订阅地址为：<span id="pharosPro" >${BASE_PATH}link/${user.token}/pharosPro/getLink</span>
                <br/>
                <a href="javascript:void(0)" class="copy" data-clipboard-action="copy" data-clipboard-target="#pharosPro">点击此处复制订阅地址</a>
                <a href="${base}/static/html/userguide/pharosPro.html" target="_blank">点击此处查看Pharos Pro使用教程</a>
                <p style="color: red;font-size: 150%">各个客户端订阅格式不兼容，请选择对应正确的客户端订阅地址</p>
                <p>其他教程</p>
                <a href="${base}/static/html/userguide/switchyOmega.html" target="_blank">SwitchyOmega使用教程</a>
                <a href="${base}/static/html/userguide/Netch.html" target="_blank">游戏加速工具Netch使用教程</a>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="wrapper wrapper-content">
                    <div class="row">
                        <button type="button" class="btn btn-primary" id="resetToken">重置订阅地址</button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<!-- Mainly scripts -->
<script type="text/javascript" src="${base}/static/base/jquery.1.9.1.min.js"></script>
<script type="text/javascript" src="${base}/static/base/bootstrap/js/bootstrap.bundle.min.js"></script>>
<script src="${base}/static/js/plugins/metisMenu/metisMenu.js"></script>
<!-- Toastr -->
<script type="text/javascript" src="${base}/static/js/plugins/toastr/toastr.js"></script>
<script type="text/javascript" src="${base}/static/js/plugins/artDialog/js/dialog-plus.js"></script>
<link href="${base}/static/js/plugins/artDialog/css/dialog.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.0/clipboard.min.js"></script>
<script src="${base}/static/js/plugins/validata/jquery-validate.js" type="text/javascript"></script>
<script src="${base}/static/js/plugins/validata/validate-config.js" type="text/javascript"></script>
<script type="text/javascript" src="${base}/static/js/common/common.js"></script>
<script type="text/javascript" src="${base}/static/js/common/common2.js"></script>
<script type="text/javascript" src="${base}/static/js/index/index.js"></script>
</body>
</html>
