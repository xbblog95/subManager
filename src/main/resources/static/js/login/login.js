/**
 * Created by CherryDream on 2016/11/12.
 */
var flag = false;
var timer;
var dar;
$(function () {
    $("#login").on("click", function () {
        if($("#name").val() == '')
        {
            var pNode = $("#name").parent();
            pNode.addClass("has-error");
        }
        else if($("#password").val() == "")
        {
            var pNode = $("#password").parent();
            pNode.addClass("has-error");
        }
        else
        {
            $.blockUI({
                message : ' <i class="fa fa-refresh fa-spin fa-3x fa-fw margin-bottom"></i>',
                baseZ : 1000000000,
                css : {
                    backgroundColor : '',
                    color : '#fff',
                    border : 'none'
                },
                overlayCSS : {
                    cursor: 'default'
                }
            });
            $.ajax({
                url : BASE_PATH + "/user/login",
                method : "post",
                data: $("#loginForm").serialize(),
                cache: false,
                success : function (result) {
                    if(result.success)
                    {
                        if(result.resetPass)
                        {
                            window.location.href = BASE_PATH + "/user/resetPassForm";

                        }
                        else
                        {
                            window.location.href = BASE_PATH + "/user/toIndex";
                        }
                    }
                    else
                    {
                        $("#error").text(result.msg);
                    }
                    $.unblockUI()
                }
            })
        }
    })
    $("#sendCode").on("click", function () {
        if($("#reset").jqueryValidate("validate"))
        {
            $.blockUI({
                message : ' <i class="fa fa-refresh fa-spin fa-3x fa-fw margin-bottom"></i>',
                baseZ : 1000000000,
                css : {
                    backgroundColor : '',
                    color : '#fff',
                    border : 'none'
                },
                overlayCSS : {
                    cursor: 'default'
                }
            });
            $.ajax({
                url : BASE_PATH + "/user/forgetValidUser",
                method : 'post',
                async : true,
                data : {
                    name : $("#authName").val(),
                    type : $("#authType").selectpicker("val")
                },
                success : function (result) {
                    $.unblockUI();
                    if(result.success)
                    {
                        $("#authName").attr("disabled", "disabled");
                        $("#authType").attr("disabled", "disabled");
                        $("#sendCode").attr("disabled", "disabled");
                        var second = 60;
                        $("#sendCode").text("重新发送(" + second + ")");
                        second--;
                        timer = setInterval(function () {
                            $("#sendCode").text("重新发送(" + second + ")");
                            second--;
                            if(second <= 0)
                            {
                                $("#sendCode").text("重新发送");
                                $("#sendCode").removeAttr("disabled");
                                $("#authName").removeAttr("disabled");
                                $("#authType").removeAttr("disabled");
                                $("#authType").selectpicker("refresh");
                                clearInterval(timer);
                            }
                        }, 1000);
                        $("#authType").selectpicker("refresh");
                        $("#authSub").removeAttr("disabled");
                        $("#authCode").removeAttr("disabled");
                        $("#authMsg").text("验证码已发送至登陆名绑定的" + result.email + "邮箱，请注意查收。");
                    }
                    else
                    {
                        $("#authMsg").text(result.msg);
                    }
                }
            })
        }
    });
    $("#authSub").on("click", function () {
        if(!$("#authCode").jqueryValidate("validate"))
        {
            return;
        }
        $.blockUI({
            message : ' <i class="fa fa-refresh fa-spin fa-3x fa-fw margin-bottom"></i>',
            baseZ : 1000000000,
            css : {
                backgroundColor : '',
                color : '#fff',
                border : 'none'
            },
            overlayCSS : {
                cursor: 'default'
            }
        });
        $.ajax({
            url : BASE_PATH + "/user/resetPass",
            data : {
                name : $("#authName").val(),
                code : $("#authCode").val()
            },
            success : function (result) {
                $.unblockUI();
                if(result.success)
                {
                    $.alertshow("提示", "密码重置成功!请用初始密码登陆系统");
                    dar.close();
                }
                else
                {
                    $.alert("警告", result.msg + "！请重新执行重置密码操作");
                }
            }
        })
    });
    $("#forgetBtn").on("click", function () {
        forget();
    })
})

var d;


function forget() {
    if(timer)
    {
        clearInterval(timer);
    }
    $("#authName").removeAttr("disabled");
    $("#authType").removeAttr("disabled");
    $("#authType").selectpicker("refresh");
    $("#sendCode").removeAttr("disabled");
    $("#sendCode").text("发送验证码");
    $("#authMsg").html("<br/>");
    $("#authSub").attr("disabled", "none");
    $("#authCode").attr("disabled", "none");
    $("#authCode").val("");
    $("#authType").selectpicker("val", "");
    $("#authName").val("");
    dar = dialog({
        title: '重置密码',
        zIndex:10,
        content: $("#forgetForm")[0],
        drag : true,
        cancelValue: '取消',
        cancel: function () {
            this.close();
        }
    });
    dar.showModal();
}


