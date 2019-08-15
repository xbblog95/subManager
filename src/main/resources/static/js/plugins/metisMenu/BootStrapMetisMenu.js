/**
 * Created by CherryDream on 2016/11/1.
 */
(function ($) {
    $.fn.BootStrapMetisMenu = function (options, param) {
        if(typeof options === 'string')
        {
            return ;//无方法
        }
        else
        {
            var defaults = {
                data :{

                },
                onclick : function (node) {

                },
                isOpen : function (node) {
                    return false;
                },
                isDisplayChildCount : false,
                displayChildCountClass : 'success',
                textStyler : function (node) {

                }
            };
            options = $.extend({},defaults,options);
            this.each(function () {
                return init(options, this);
            })
        }
    };


    function init(options, target) {
        var data = options.data;
        $(target).addClass("metismenu");
        $(target).addClass("nav");
        //写li
        for(var i = 0; i < data.length; i++)
        {
            $(target).append( renderLi(data[i], options, 1));
        }
        $(target).metisMenu();
    }

    function renderLi(node, options, lev) {
        var textStyler = options.textStyler(node);
        var isOpen = options.isOpen(node) || node.isOpen;
        var $li = $("<li>");
        if(isOpen)
        {
            $li.addClass("active");
        }
        var $a = $("<a>");
        $a.attr("href", "javascript:void(0)");
        var $i = $("<i>");
        if(lev == 2)
        {
            if(node.icon == undefined || node.icon == null || node.icon == "")
            {
                $i.addClass("fa fa-file-text");
            }
            else
            {
                $i.addClass(node.icon);
            }
        }
        else if(lev == 1)
        {
            if(node.icon == undefined || node.icon == null || node.icon == "")
            {
                $i.addClass("fa fa-folder");
            }
            else
            {
                $i.addClass(node.icon);
            }
        }
        $a.append($i);
        if(lev == 1)
        {
            var $spanLabel = $("<span>");
            $spanLabel.addClass("nav-label");
            if(textStyler != undefined && textStyler != '')
            {
                $spanLabel.html(textStyler);
            }
            else
            {
                $spanLabel.text(node.text);
            }
            $a.append($spanLabel);
        }
        else
        {
            $a.append(node.text);
        }
        if(lev < 2)
        {
            if(node.nodes != undefined && node.nodes != null && typeof node.nodes === 'object')
            {
                if(options.isDisplayChildCount)
                {
                    var $spanCount = $("<span>");
                    $spanCount.addClass("badge " + options.displayChildCountClass);
                    $spanCount.css("float", "right");
                    $spanCount.text(node.nodes.length);
                    $a.append($spanCount);
                }
                var $spanArrow = $("<span>");
                $spanArrow.addClass("glyphicon arrow");
                $a.append($spanArrow);
            }
        }
        $a.on("click", function (e) {
            options.onclick(node);
            e.stopPropagation();
        })
        $li.append($a);
        if(lev < 2)
        {
            if(node.nodes != undefined && node.nodes != null && typeof node.nodes === 'object')
            {
                var $ul = $("<ul>");
                $ul.addClass("nav nav-second-level collapse");
                if(isOpen)
                {
                    $ul.addClass("in");
                }
                for(var i = 0; i < node.nodes.length; i++)
                {
                    $ul.append(renderLi(node.nodes[i], options, lev + 1));
                }
                $li.append($ul);
            }
        }
        return $li;
    }
})(jQuery)
