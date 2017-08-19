//<-------------客户端的用户字典数据及操作方法(与CustomValidation中的userdict验证规则配合使用)
//用于提供固定选项的下拉列表
//主页面须含有元素<div id="divUserDictSelectors"></div>用于存放创建的控件
app.GlobalDict = {
    GetName:function (dictVar, codeStr) {//按代码查名称
        var v = dictVar[codeStr];
        if (v) return v;
        return null;
    },
    GetCode:function (dictVar, nameStr) {//按名称查代码
        for (prop in dictVar) {
            if (dictVar[prop] == nameStr) {
                return prop;
            }
        }
        return null;
    },
    LoadSelector:function (dictvarname) {//根据字典数据创建下拉列表控件
        $('#divUserDictSelectors').append('<select class="displaynone" dictvar="' + dictvarname + '"  size=5></select>');
        var select = $('[dictvar="' + dictvarname + '"]');
        var dictVar = eval(dictvarname);
        for (var prop in dictVar) {
            select.append('<option value="' + prop + '">' + prop + ' ' + dictVar[prop] + '</option>');
        }
    },
    AddUserDictSelecting:function(currentAjaxUpdateTarget) {//将字典下拉列表控件与标记为进行userdict验证的输入框关联起来，并根据输入内容自动在列表中查找匹配项
        $('[data-val-userdict]:not([readonly])', currentAjaxUpdateTarget).keydown(function (event) {
            if (event.which == 32) {
                var inputid = $(this).attr('id');
                var dictVarName = GetDictVarNameForSelector(inputid);
                var select = $('[dictvar=' + dictVarName + ']');
                $(select).data('searchfor', $(this).val().trim());
                $(select).siblings().hide();
                $(select).show();
                $(select).position({
                    my: "left top",
                    at: "left bottom",
                    of: this,
                    collision: "none none"
                });
                if ($(select).data('searchfor') != '') {
                    $('option', select).each(function () {
                        if ($(this).text().indexOf($(select).data('searchfor')) >= 0) {
                            $(this).prop('selected', true);
                            $('#' + inputid).val($(this).val());
                            return false;
                        }
                    });
                }
                $('option', select).each(function () {
                    if ($(this).text().indexOf($(select).data('searchfor')) >= 0) {
                        if ($(this).parent().prop('tagName') == 'SPAN') {
                            $(this).unwrap();
                        }
                        $(this).show();
                    } else {
                        if ($(this).parent().prop('tagName') != 'SPAN') {
                            $(this).wrap('<span></span>');
                        }
                        $(this).parent().hide();
                    }
                });

                $(select).focus();
                $(select).off();
                $(select).one('click', function () {
                    $('#' + inputid).val($(this).val());
                    //$('#' + inputid).triggerHandler('change');
                    $(select).hide();
                    $('#' + inputid).focus();
                });
                $(select).on('keydown', function (event) {
                    if (event.which == 13) {
                        $('#' + inputid).val($(this).val());
                        $(this).hide();
                        $('#' + inputid).focus();
                    } else if (event.which == 32) {
                        var selectedIndex = $('option:selected', select).index();
                        var found = false;
                        $('option', select).each(function () {
                            if ($(this).text().indexOf($(select).data('searchfor')) >= 0 && $(this).index() > selectedIndex) {
                                $(this).prop('selected', true);
                                $('#' + inputid).val($(this).val());
                                found = true;
                                return false;
                            }
                        });
                        if (found) return;
                        $('option', select).each(function () {
                            if ($(this).text().indexOf($(select).data('searchfor')) >= 0 && $(this).index() < selectedIndex) {
                                $(this).prop('selected', true);
                                $('#' + inputid).val($(this).val());
                                return false;
                            }
                        });
                    }
                });
                return false;
            }
        });
    },
    PartyClass : {
    	    "1": "国有",
    	    "2": "合作",
    	    "3": "合资",
    	    "4": "独资",
    	    "5": "集体",
    	    "6": "私营",
    	    "7": "个体工商户",
    	    "8": "报关",
    	    "9": "其他"
    	}
};











