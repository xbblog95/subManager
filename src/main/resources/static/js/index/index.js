var dar;
$(function () {
    var clipboard = new ClipboardJS('.copy');
    clipboard.on('success', function(e) {
        alert("复制成功！")
    });


    //重置订阅地址
    $("#resetToken").on("click", function () {
        $.ajax({
            url : BASE_PATH + "user/resetToken.do",
            method : 'post',
            async : true,
            data : {
            },
            success : function (result) {
                if(result.success)
                {
                    alert("重置成功!");
                    window.location.reload();
                }
                else
                {
                    alert(result.msg);
                }
            }
        })
    })

    //修改密码
    $(".modPass").on("click", function () {
        dar = dialog({
            title: '修改密码',
            zIndex:10,
            content: $("#modPass")[0],
            drag : true,
            cancelValue: '取消',
            cancel: function () {
                this.close();
            }
        });
        dar.showModal();
    })

    //修改密码提交
    $("#modPassSubmit").on("click", function () {
        if(!$("#modPass").jqueryValidate("validate"))
        {
            return;
        }
        $.ajax({
            url : BASE_PATH + "user/modifyPassword.do",
            method : 'post',
            async : true,
            data : {
                oldPassword : $("#oldPassword").val(),
                newPassword : $("#newPassword").val()
            },
            success : function (result) {
                if(result.success)
                {
                    window.location.href=BASE_PATH + "user/loginOut.do";
                }
                else
                {
                    alert(result.msg);
                }
            }
        })
    })
})

