<!DOCTYPE html>
<head lang="zh-cn">
    <title>一个不知道是啥的站点</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta charset="UTF-8">
    <base  href="${BASE_PATH}">
    <script type="text/javascript" src="${BASE_PATH}/static/base/jquery.1.9.1.min.js"></script>
    <script type="text/javascript" src="${BASE_PATH}/static/base/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="${BASE_PATH}/static/base/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/base/css/font-awesome.min.css" />
    <script type="text/javascript" src="${BASE_PATH}/static/js/plugins/artDialog/js/dialog-plus.js"></script>
    <link href="${BASE_PATH}/static/js/plugins/artDialog/css/dialog.css" rel="stylesheet"/>
    <script type="text/javascript" src="${BASE_PATH}/static/js/plugins/jquery.blockUI/jquery.blockUI.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.10/dist/js/bootstrap-select.min.js"></script>
    <script type="text/javascript" src="${BASE_PATH}/static/js/plugins/bootstrap-select-1.12.2/defaults-zh_CN.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.10/dist/css/bootstrap-select.min.css">
    <script src="${BASE_PATH}/static/js/plugins/validata/jquery-validate.js" type="text/javascript"></script>
    <script src="${BASE_PATH}/static/js/plugins/validata/validate-config.js" type="text/javascript"></script>
    <script type="text/javascript" src="${BASE_PATH}/static/js/common/common.js"></script>
    <script type="text/javascript" src="${BASE_PATH}/static/js/common/common2.js"></script>

    <script type="text/javascript" src="${BASE_PATH}/static/js/plugins/toastr/toastr.js"></script>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/plugins/toastr/toastr.min.css"/>
    <script type="text/javascript" src="${BASE_PATH}/static/js/login/login.js"></script>
    <script  type="text/javascript">
        var BASE_PATH = '${BASE_PATH}';
    </script>
    <style rel="stylesheet">
        html body{
            height: 100%;
            width: 100%;
            font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
            background-color: #2f4050;
            font-size: 13px;
            color: #676a6c;
            overflow-x: hidden;
        }
        body{
            background:url("static/img/login/background.jpg") top center no-repeat;
            background-size:cover;
        }
        .text-center{
            text-align: center;
        }
        .loginMain{
            margin:0 auto;
            max-width: 500px;
            padding-top: 40px;
        }
        @media (max-width: 768px) {
            .body-div{
                width: 300px;
            }
        }
        @media (min-width: 768px) {
            .body-div{
                width: 400px;
                padding: 50px;
            }
            .reg{
                width: 700px;
            }
            .padden{
                padding-left: 9px;
            }
        }
        .text{
            box-shadow: none;
            border-radius: 1px;
        }
        a{
            color: #ffffff;
        }
        .btn-white {
            color: inherit;
            background: white;
            border: 1px solid #e7eaec;
        }
        .btn-sm{
            padding: 5px 10px;
            font-size: 12px;
            line-height: 1.5;
            margin-top: 10px;
        }
        .file-preview{
            border: 0;
        }
        .img-circle{
            border-radius: 50%;
            align-items: center;
        }
    </style>
</head>
<body>
<div >
    <div class="loginMain text-center body-div" >
        <div>
            <img class="img-circle" src="${BASE_PATH}/static/img/profile_small.png" style="height: 150px; width: 150px"/>
        </div>
        <h1>一个不知道是啥的站点</h1>
        <form class="m-t" role="form" id="loginForm" method="post">
            <div class="form-group has-feedback">
                <input type="text" class="form-control text" id="name" name="name" placeholder="用户名" required="required">
            </div>
            <div class="form-group  has-feedback">
                <input type="password" class="form-control text" id="password" name="password" placeholder="初始密码为123456" required="required">
            </div>
            <button type="button" class="btn btn-success block m-b" id="login" style="width: 100%">登陆</button>
            <table style="width: 100%">
                <tr>
                    <td style="width: 50%"><a class="btn btn-sm btn-white btn-block"  id="forgetBtn" >忘记密码</a></td>
                </tr>
            </table>
            <p style="padding-top: 20px; color: red" id="error"></p>
        </form>
    </div>
</div>
<div id="forgetForm" style="display: none" class="reg">
    <form id="forget">
        <div id="reset">
            <div class="row">
                <div class="form-group col-xs-12 col-sm-6 col-lg-6 row" >
                    <label for="authType" class="col-xs-5 col-lg-4 col-md-4 col-sm-4 col-form-label" style="height: 34px;line-height: 34px">验证方式:</label>
                    <div class="col-xs-7 col-lg-8 col-md-8 col-sm-8">
                        <select class="selectpicker validater" data-width="100%" name="authType" id="authType" data-validate-rule='{
                      "required":"true"
                  }' data-validate-message='{
                      "required":"必选项"
                  }'data-validate-type="bootStrap4Selectpicker">
                            <option value="" selected>---请选择---</option>
                            <option value="email">邮箱验证</option>
                        </select>
                    </div>
                </div>
                <div class="form-group col-xs-12 col-sm-6 col-lg-6 row" >
                    <label for="authName" class="col-xs-5 col-lg-4 col-md-4 col-sm-4 col-form-label" style="height: 34px;line-height: 34px">登陆名:</label>
                    <div class="col-xs-7 col-lg-8 col-md-8 col-sm-8">
                        <input type="text" class="form-control validater" id="authName" name="authName" placeholder="登陆名" data-validate-rule='{
                      "required":"true"
                  }' data-validate-message='{
                      "required":"必填项"
                  }' data-validate-type="bootStrap4Input"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group col-xs-12 col-sm-6 col-lg-6" >
                </div>
                <div class="form-group col-xs-12 col-sm-6 col-lg-6 row" >
                    <div  class="col-xs-5 col-lg-4 col-md-4 col-sm-4 control-label" ></div>
                    <div class="col-xs-7 col-lg-8 col-md-8 col-sm-8"><button class="btn btn-primary" type="button" id="sendCode">发送验证码</button><div id="authMsg" style="color: red"><br/></div></div>
                </div>
            </div>
        </div>
        <hr/>
        <div class="row">
            <div class="form-group col-xs-12 col-sm-6 col-lg-6 row" >
                <label for="authCode" class="col-xs-5 col-lg-4 col-md-4 col-sm-4 control-label" style="height: 34px;line-height: 34px">验证码:</label>
                <div class="col-xs-7 col-lg-8 col-md-8 col-sm-8">
                    <input type="text" class="form-control validater" id="authCode" name="authCode" placeholder="验证码" data-validate-rule='{
                    "required":"true"
                }' data-validate-message='{
                    "required":"必填项"
                }' data-validate-type="bootStrap4Input"/>
                </div>
            </div>
            <div class="form-group col-xs-12 col-sm-6 col-lg-6 row" >
                <div  class="col-xs-5 col-lg-4 col-md-4 col-sm-4 control-label" ></div>
                <div class="col-xs-7 col-lg-8 col-md-8 col-sm-8"><button class="btn btn-primary" type="button" id="authSub">提交</button></div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
