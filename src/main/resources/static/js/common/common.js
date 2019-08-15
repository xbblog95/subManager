/**
 * Created by CherryDream on 2016/11/13.
 */
(function ($) {
    $.extend({
        alert: function (title, msg, css, okCallback, cancelfunction) {
            var icon ;
            var color;
            if(css == undefined)
            {
                css = 'danger';
            }
            if(css == 'success')
            {
                icon = 'fa-check-circle';
                color = '#1ab394';
            }
            if(css == 'warning')
            {
                icon = 'fa-info-circle';
                color = '#f0ad4e';
            }
            if(css == 'danger')
            {
                icon = 'fa-times-circle-o';
                color = '#c9302c';
            }
            var d = dialog({
                title : title,
                content : '<table style="height: 4em;min-width: 250px">' +
                    '<tr><td style="width: 100px">' +
                    '<i class="fa ' + icon + ' fa-4x" style="color: ' + color + '; width: 100%" aria-hidden="true"></i></td><td style="text-align: left;"><div style="display: inline;height: 4em;line-height: 4em;">' + msg + '</div></td>'+
                    '</table>',
                okValue: '好的',
                ok : function () {
                    if(okCallback != undefined)
                    {
                        okCallback();
                    }
                    return true;
                },
                cancel: false,
            });
            d.showModal();
        },
        confirm : function (title, msg, okCallback, cancelCallback) {
            var d = dialog({
                title : title,
                content : '<table style="height: 4em;">' +
                '<tr><td style="width: 100px">' +
                '<i class="fa fa-question-circle-o fa-4x" style="color: #3071a9" aria-hidden="true"></i></td><td style="text-align: center;"><div style="display: inline;height: 4em;line-height: 4em;">' + msg + '</div></td>'+
                '</table>',
                okValue: '确认',
                ok : function () {
                    if(okCallback != undefined)
                    {
                        okCallback();
                    }
                    return true;
                },
                cancelValue : '取消',
                cancel : function () {
                    if(cancelCallback != undefined)
                    {
                        cancelCallback();
                    }
                    this.close();
                    return true;
                },
                width:'auto'
            });
            d.showModal();
        },
        alertshow : function (msg, title) {
            toastr["success"](title, msg);
        },
        maskUI : function () {
            window.parent.popit();
        },
        unmaskUI : function () {
            window.parent.unPopit();
        } 
    })
})(jQuery)