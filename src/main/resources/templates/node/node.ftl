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
    <link href="${base}/static/css/plugins/bootstrap-table/bootstrap-table.css" rel="stylesheet">
    <script  type="text/javascript">
        var BASE_PATH = '${base}';
    </script>
    <style rel="stylesheet">
        .col-backgroud-min {
            background-color: #ffffff;
        }

        .col-backgroud-normal {
            background-color: #33ffff;
        }

        .col-backgroud-max {
            background-color: #ff0099;
        }
    </style>
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
                            <span class="text-muted text-xs block">组别：${user.group}<span><a href="javascript:void(0)" id="changeGroupSwitch">切换</a> </span></span>
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
                        <li><a href="${base}/node/toNodeList">节点列表</a></li>
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
        <div class="text-center col-md-12 col-sm-12 col-lg-12" style="display: none;min-width: 300px" id="changeGroupForm">
            <form>
                <div class="mb-3">
                    <label for="oldPassword">组</label>
                    <select  class="form-control validater" id="groupId" name="groupId" data-validate-rule='{
                                                  "required":"true"
                                              }' data-validate-message='{
                                                  "required":"必填项"
                                              }' data-validate-type="select"  required>
                        <option value="1">组别1</option>
                        <option value="2">组别2</option>
                        <option value="3">组别3</option>
                    </select>
                </div>
                <hr class="mb-4">
                <button class="btn btn-primary btn-lg btn-block" type="button" id="changeGroupSubmit">提交</button>
            </form>
        </div>
        <div class="row  border-bottom white-bg dashboard-header">
            <div id="table" style="width: 100%"></div>
        </div>

    </div>
</div>

<!-- Mainly scripts -->
<script type="text/javascript" src="${base}/static/base/jquery.1.9.1.min.js"></script>
<script type="text/javascript" src="${base}/static/base/bootstrap/js/bootstrap.bundle.min.js"></script>
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
<script type="text/javascript" src="${base}/static/js/plugins/bootstrap-table/bootstrap-table.js"></script>
<script type="text/javascript" src="${base}/static/js/plugins/bootstrap-table/bootstrap-table-zh-CN.min.js"></script>

<script type="text/javascript">

    $("#table").bootstrapTable({
        url : BASE_PATH + "/node/queryNodeStatus",
        columns : [{
            field : "name",
            title : "名称",
            width : 200
        },{
            field : "location",
            title : "地区",
            width : 70
        },{
            field : "ping",
            title : "ping延迟",
            width : 120,
            sortable : true,
            formatter : function (value, row, index)
            {
                if(value)
                {
                    return value + "ms";
                }
                else
                {
                    return "";
                }
            },
            cellStyle : function (value, row, index) {
                if (!value) {
                    return {};
                }
                return {
                    css  : {
                        "background-color" : "#66ff66"
                    }
                }
            }
        },{
            field : "tcpPing",
            title : "TCP Ping延迟",
            width : 120,
            sortable : true,
            formatter : function (value, row, index)
            {
                if(value)
                {
                    return value + "ms";
                }
                else
                {
                    return "";
                }
            },
            cellStyle : function (value, row, index) {
                if (!value) {
                    return {};
                }
                return {
                    css  : {
                        "background-color" : "#66ff66"
                    }
                }
            }
        },{
            field : "loss",
            title : "丢包率",
            width : 80,
            sortable : true,
            formatter : function (value, row, index)
            {
                if($.isNumeric(value))
                {
                    return value + "%";
                }
                else
                {
                    return value;
                }
            }
        },{
            field : "nat",
            title : "nat",
            width : 100,
        },{
            field : "speed",
            title : "平均速度",
            width : 150,
            sortable : true,
            cellStyle : function (value, row, index) {
                if (!value) {
                    return {};
                }
                if(!row.tcpPing)
                {
                    return {};
                }
                let speed = value / 1024;
                if (speed > 1) {
                    return {
                        classes: 'col-backgroud-max'
                    }
                }
                if (speed > 0.5) {
                    return {
                        classes: 'col-backgroud-normal'
                    }
                } else {
                    return {
                        classes: 'col-backgroud-min'
                    }
                }
            },
            formatter : function (value, row, index)
            {
                if(!row.tcpPing)
                {
                    return "";
                }
                if(value)
                {
                    return value / 1024 + "MB/s";
                }
                else
                {
                    return "";
                }
            }
        },{
            field : "maxSpeed",
            title : "最大速度",
            width : 150,
            sortable : true,
            cellStyle : function (value, row, index)
            {
                if(!value)
                {
                    return {};
                }
                if(!row.tcpPing)
                {
                    return {};
                }
                let speed = value / 1024;
                if(speed  > 1)
                {
                    return {
                        classes : 'col-backgroud-max'
                    }
                }
                if(speed  > 0.5)
                {
                    return {
                        classes : 'col-backgroud-normal'
                    }
                }
                else
                {
                    return {
                        classes : 'col-backgroud-min'
                    }
                }
            },
            formatter : function (value, row, index)
            {
                if(!row.tcpPing)
                {
                    return "";
                }
                if(value)
                {
                    return value / 1024 + "MB/s";
                }
                else
                {
                    return "";
                }
            }
        },{
            field : "updateTime",
            title : "最后一次更新时间",
            sortable : true
        }]
    });
</script>
</body>
</html>
