/**
 * Created by CherryDream on 2016/11/14.
 */
(function ($) {
    $.fn.maskDiv = function (options) {
        $(this).block(
            {
                message : options.msg,
                baseZ : 100000,
                css : {
                    backgroundColor : '',
                    color : '#fff',
                    border : 'none'
                },
                overlayCSS : {
                    cursor: 'default'
                }
            }
        )
    };
    $.fn.unmaskDiv = function () {
        $(this).unblock();
    };
})(jQuery)