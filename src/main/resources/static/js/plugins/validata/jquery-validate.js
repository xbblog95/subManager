;(function($){
    var pluginName = "jqueryValidate";

    function validaterObj()
    {
        var eleObjs = new Map();

        function getObj(element) {
            return eleObjs.get(element);
        }
        //获取规则
        function getRules(element) {
            var obj =  getObj(element);
            if(obj)
            {
                return obj.rules;
            }
            return {};
        }
        //获取错误信息
        function getErrorMessages(element) {
            var obj = getObj(element);
            if(obj)
            {
                return obj.errorMessages;
            }
            return {};
        }
        //获取插件类型
        function getType(element) {
            var obj =  getObj(element);
            if(obj)
            {
                return obj.type;
            }
            return "default";
        }

        /**
         * 添加、修改规则
         * element : dom对象
         * ruleObj ： {
         *     type : "插件类型",
         *     rules : {
         *         "required" : true
         *     },
         *     errorMessages : {
         *         "required" : "必填项"
         *     }
         * }
         */
        function updateRule(element,ruleObj) {
            if(!ruleObj)
            {
                return;
            }
            if(!ruleObj.rules || ruleObj.rules.length == 0 )
            {
                return;
            }
            //获取插件，判断是否存在
            var valiObj = getObj(element);
            if(valiObj)
            {
                //存在则更新
                valiObj.type = ruleObj.type ? ruleObj.type : valiObj.type;
                var rules = getRules(element);
                var errorMessages = getErrorMessages(element);
                for(var i in ruleObj.rules)
                {
                    rules[i] = ruleObj.rules[i];
                    errorMessages[i] = ruleObj.errorMessages[i] ? ruleObj.errorMessages[i] : "";
                }
            }
            else
            {
                //不存在则新增
                var obj = {
                    type : ruleObj.type,
                    rules : ruleObj.rules,
                    errorMessages: ruleObj.errorMessages
                }
                eleObjs.set(element, obj);
                //调用元素的inInit方法，执行自定义初始化操作，比如值改变时检验等等。
                callElementInit(element);
            }
        }

        /**
         * 删除某个元素检验
         * @param element
         * @param rule
         */
        function deleteRule(element, rule) {
            if(!rule)
            {
                return;
            }
            //获取插件，判断是否存在
            var valiObj = getObj(element);
            if(valiObj)
            {
                var rules = getRules(element);
                var errorMessages = getErrorMessages(element);
                delete rules[rule];
                delete errorMessages[rule];
            }
            //没有插件不管
        }

        /**
         * 通知设置元素初始化自定义方法
         * @param element
         */
        function callElementInit(element) {
            var type = getType(element);
            //如果配置该元素的预设置参数，则使用，否则使用default参数
            if($.validater.type[type])
            {
                $.validater.type[type].onInit(element);
            }
        }
        /**
         * 通知设置元素错误样式
         * @param element
         */
        function callElementError(errorMsg, element) {
            var type = getType(element);
            //如果配置该元素的预设置参数，则使用，否则使用default参数
            if($.validater.type[type])
            {
                $.validater.type[type].onError(errorMsg, element);
            }
            else
            {
                $.validater.type.default.onError(errorMsg, element);
            }
        }

        /**
         * 通知设置元素重置样式
         * @param element
         */
        function callElementReset(element) {
            var type = getType(element);
            //如果配置该元素的预设置参数，则使用，否则使用default参数
            if($.validater.type[type])
            {
                $.validater.type[type].onError(element);
            }
            else
            {
                $.validater.type.default.onError(element);
            }
        }

        /**
         * 通知设置元素成功样式
         * @param element
         */
        function callElementSuccess(element) {
            var type = getType(element);
            //如果配置该元素的预设置参数，则使用，否则使用default参数
            if($.validater.type[type])
            {
                $.validater.type[type].onSuccess(element);
            }
            else
            {
                $.validater.type.default.onSuccess(element);
            }
        }

        /**
         * 获取元素值
         * @param element
         */
        function callElementGetValue(element) {
            var type = getType(element);
            //如果配置该元素的预设置参数，则使用，否则使用default参数
            if($.validater.type[type])
            {
                return $.validater.type[type].onGetValue(element);
            }
            else
            {
                return $.validater.type.default.onGetValue(element);
            }
        }

        /**
         * 检验某个元素
         * @param element 元素
         * @returns {boolean} 成功与否
         */
        function validate(element) {
            var rules = getRules(element);
            //没有规则的话，直接返回true
            if($.isEmptyObject(rules))
            {
                return true;
            }
            //获取元素当前值
            var value = callElementGetValue(element);
            //循环每个检验规则
            var result = true;
            for(var i in rules)
            {
                //判断检验方法是否存在
                if($.validater.validatefunction[i])
                {
                    //存在的话
                    var res = $.validater.validatefunction[i].call(element, value, rules[i]);
                    if(!res)
                    {
                        //获取错误语句
                        var errorMessage = getErrorMessages(element)[i];
                        callElementError(errorMessage, element);
                        result = false;
                        continue;
                    }
                }
                else
                {
                    //不存在执行定制化检验方式
                    var res = $.validater.validatefunction.custom.call(element, value, rules[i]);
                    if(!res)
                    {
                        //获取错误语句
                        var errorMessage = getErrorMessages(element)[i];
                        callElementError(errorMessage, element);
                        result = false;
                        continue;
                    }
                }
            }
            //如果该元素所有检验都通过，通知UI元素成功检验
            if(result)
            {
                callElementSuccess(element);
            }
            return result;
        }

        /**
         * 检验单个jquery对象
         * @param jq
         */
        function validateJquery(jq) {
            //jquery对象是否有东西
            if(!jq || jq.length == 0)
            {
                return true;
            }
            //先检验自己
            var isSuccess = true;
            if(!validate(jq[0]))
            {
               isSuccess = false;
            }
            jq.find(".validater").each(function () {
                var result = validate($(this)[0]);
                if(!result)
                {
                    isSuccess = false;
                }
            })
            return isSuccess;
        }
        //添加/替换规则与错误信息
        this.updateRule = function (ruleObj, jq) {
            if(!jq)
            {
                jq = $(this);
            }
            if(jq instanceof jQuery)
            {
                updateRule(jq.get(0), ruleObj);
            }
            else
            {
                updateRule(jq, ruleObj);
            }

        }
        //删除规则与错误信息
        this.deleteRule = function (jq, rule) {
            if(jq instanceof jQuery)
            {
                deleteRule(jq.get(0), rule);
            }
            else
            {
                deleteRule(jq, rule);
            }
        }
        //检验元素
        this.validate = function(element)
        {
            var result = [];
            if(!element)
            {
                if(!this)
                {
                    return true;
                }
                return validateJquery($(this));
            }
            //如果是多个
            if(element instanceof Array)
            {
                for(var i in element)
                {
                    result.push(validateJquery($(element[i])));
                }
            }
            else
            {
                result.push(validateJquery($(element)));
            }
            if(result.length == 1)
            {
                return result[0];
            }
            return result;
        }
        //重置元素
        this.reset = function (jq) {
            if(!jq)
            {
                if(!this)
                {
                    return;
                }
                callElementReset($(this).get(0));
            }
            else
            {
                callElementReset(jq.get(0));
            }
        }
        //获取元素的类型
        this.getType = function (element) {
            return getType(element);
        }
    }
    var validater = new validaterObj();
    //检验的相关参数
    $.extend({
        validater : {
            type : {},
            validatefunction : {
                //定制化检验
                "custom" : function (value, func) {
                    if($.isFunction(func))
                    {
                        return func.call(this, value);
                    }
                    return false;
                }
            }
        }
    });


    //dom调用检验插件其中方法
    $.fn[pluginName] = function(opt, ...param)
    {
        var result = [];
        this.each(function () {
            if(opt && typeof(opt) === "string")
            {
                if(!$.isFunction(validater[opt]) || opt.charAt(0) === '_')
                {
                    console.log("找不到" + opt + "方法");
                }
                result.push(validater[opt].apply($(this), param));
            }
            else
            {
                //初始化
                var _options = {
                    type : $(this).attr("data-validate-type") ? $(this).attr("data-validate-type") : "default",
                    errorMessages :   JSON.parse($(this).attr("data-validate-message") ? $(this).attr("data-validate-message") : "{}"),
                    rules : JSON.parse($(this).attr("data-validate-rule") ? $(this).attr("data-validate-rule") : "{}")
                };
                validater.updateRule(_options, $(this));
            }
        })
        if(result.length == 1)
        {
            return result[0];
        }
        return result;
    };

    //通过validate中的方法对某个dom执行某项操作
    $[pluginName] = {
        validate : function (element) {
           return  validater.validate(element);
        },
        updateRule : function (element, ruleObj) {
            validater.updateRule(element, ruleObj);
        },
        deleteRule : function (element) {
            validater.deleteRule(element);
        },
        reset : function (element) {
            validater.reset(element);
        },
        getType : function (element) {
            return validater.getType(element);
        }
    };

})(jQuery);


$(function () {
    $(".validater").jqueryValidate();
})