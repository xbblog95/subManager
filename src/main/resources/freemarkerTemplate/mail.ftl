
    <style type="text/css">
p{
    text-indent: 2em;
}
    </style>
        <div style="position:relative">
            <img src="https://s2.ax1x.com/2019/03/07/kxuBH1.jpg" alt="kxuBH1.jpg" border="0" style="height: 650px;width: 100%"/>
            <div style="top: 50px;position:absolute;width: 100%">
                <div style="margin-left: auto;margin-right: auto; width: 60%">
                    <h1>尊敬的先生:</h1>
                    <p>您好，检测到部分服务器在${date?string("yyyy年MM月dd日HH时mm分")}出现不能访问的错误，请尽快修复！</p>
                    <p>异常的服务器列表：</p>
                    <p style="text-align: center;width: 100%;">
                        <table style="width: 80%;text-align: center;margin-left: auto;margin-right: auto;" border="1">
                        <tr>
                            <td>ip</td>
                            <td>端口</td>
                        </tr>
                        <#list list as p>
                            <tr>
                                <td>${p.ip}</td>
                                <td>${p.port?c}</td>
                            </tr>
                        </#list>
                    </table>
                    </p>
                </div>
            </div>
        </div>
