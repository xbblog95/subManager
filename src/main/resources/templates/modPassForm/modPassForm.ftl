<#assign base=request.contextPath />
<!DOCTYPE html>
<html  >
<head lang="zh-cn" xmlns:th="http://www.thymeleaf.org">
    <title>修改密码</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <script type="text/javascript" src="${base}/static/base/jquery.1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/static/base/bootstrap/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="${base}/static/base/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${base}/static/base/css/font-awesome.min.css" />
    <script type="text/javascript" src="${base}/static/js/plugins/artDialog/js/dialog-plus.js"></script>
    <script type="text/javascript" src="${base}/static/js/plugins/jquery.blockUI/jquery.blockUI.js"></script>
    <script type="text/javascript" src="${base}/static/js/common/common.js"></script>
    <script type="text/javascript" src="${base}/static/js/common/common2.js"></script>
    <link href="${base}/static/js/plugins/artDialog/css/dialog.css" rel="stylesheet"/>

    <script src="${base}/static/js/plugins/validata/jquery-validate.js" type="text/javascript"></script>
    <script src="${base}/static/js/plugins/validata/validate-config.js" type="text/javascript"></script>
    <script type="text/javascript" src="${base}/static/js/plugins/toastr/toastr.js"></script>
    <link rel="stylesheet" href="${base}/static/css/plugins/toastr/toastr.min.css"/>
    <script  type="text/javascript">
        var BASE_PATH = '${base}';
    </script>
</head>
<body class="bg-light">
<div class="container">
    <div class="text-center">
        <form id="modPass">
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
                                                  "required":"true"
                                              }' data-validate-message='{
                                                  "required":"必填项"
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
            <button class="btn btn-primary btn-lg btn-block" type="button" id="submit">提交</button>
        </form>
    </div>
</div>
<script type="text/javascript" src="${base}/static/js/modPass/modPass.js"></script>
</body>
</html>
