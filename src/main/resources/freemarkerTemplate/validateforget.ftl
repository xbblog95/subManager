
    <style type="text/css">
p{
    text-indent: 2em;
}
    </style>
        <div style="position:relative">
            <img src="${host}/img/mail/bk.jpg" style="height: 650px;width: 100%"/>
            <div style="top: 50px;position:absolute;width: 100%">
                <div style="margin-left: auto;margin-right: auto; width: 60%">
                    <h1>尊敬的先生/女士:</h1>
                    <p>您好，您在${date?string("yyyy年MM月dd日HH时mm分")}执行了重置密码的操作，如您未进行该操作，请忽略本邮件并及时修改您的密码。</p>
                    <p>您此次操作的验证码为：${code}</p>
                    <div style="text-align: right">
                    ${date?string("yyyy年MM月dd日HH时mm分")}<br/>
                    </div>
                    <hr/>
                    <div>
                        本邮件由系统自动发出<br/>
                        如有疑问，请联系管理员，切勿回复。
                    </div>
                </div>
            </div>
        </div>
