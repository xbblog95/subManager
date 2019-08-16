$(function () {
    $("#submit").on("click", function () {
        if(!$("#modPass").jqueryValidate("validate"))
        {
            return;
        }
        $.ajax({
            url : BASE_PATH + "user/modifyPassword",
            method : 'post',
            async : true,
            data : {
                oldPassword : $("#oldPassword").val(),
                newPassword : $("#newPassword").val()
            },
            success : function (result) {
                if(result.success)
                {
                    window.location.href=BASE_PATH;
                }
                else
                {
                    alert(result.msg);
                }
            }
        })
    })
})