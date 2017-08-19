(function(){//begin pack
	
	
/*---------------API--begin------------------------------*/
var pb=ProjectBase_WebClientAppRoot.pbDefaultService;
pb.RegisterCustomValidation=RegisterCustomValidation;
/*----------------API--end-----------------------------------*/
	
	
	
/*----------------unobtrusive validation attributes----------------------------------------*/
jQuery.validator.setDefaults({
   onkeyup: false
});

function RegisterCustomValidation() {
    jQuery.validator.addMethod("userdict", function (value, element, param) {//根据输入的代码替换为名称同时验证替换是否成功
        var dictItemName = '';
        var inputid = $(element).attr('id');
        var hiddenId = inputid.replace('_Name', '');
        if (value == '') {
            $('#' + hiddenId).val(value);

            return true;
        }
        var dictVar = GetDictVar(inputid);
        dictItemName = GlobalDict.GetName(dictVar, value);
        if (dictItemName == null) {//已经是名称
            var hiddenValue = $('#' + hiddenId).val();
            if (hiddenValue != '') {//曾经翻译成代码过
                var hiddenIdToName = GlobalDict.GetName(dictVar, hiddenValue);
                if (hiddenIdToName == value) return true;
            }
            if (typeof (g_Party) != 'undefined' && dictVar == g_Party) {
                for (prop in dictVar) {
                    if (dictVar[prop].startWith(value)) {
                        $('#' + hiddenId).val(prop);
                        $(element).val(dictVar[prop]);
                        return true;
                    }
                }
            } else {
                for (prop in dictVar) {
                    if (dictVar[prop] == value) {
                        $('#' + hiddenId).val(prop);
                        return true;
                    }
                }
            }
            return false;
        } else {
            $('#' + hiddenId).val(value);
            $('#' + inputid).val(dictItemName);
            return true;
        }
    });
    jQuery.validator.addMethod("maxbytelength", function (value, element, param) {
        return value == '' || value.getBytesLength() <= param;
    });
    jQuery.validator.addMethod("autotrim", function (value, element, param) {
        $(element).val($(element).val().trim());
        return true;
    });
    
    jQuery.validator.unobtrusive.adapters
        .addBool('userdict')
        .addBool('autotrim')
        .addSingleVal('maxbytelength', 'val');
}


function GetDictVar(inputid) {
    var dictVar = null;
    if (inputid.indexOf('PartyClassCode') >= 0 )
        dictVar = app.GlobalDict.PartyClass;
    else {
        return null;
    }
    return dictVar;
}
function GetDictVarNameForSelector(inputid) {
    var dictVar = null;
    if (inputid.indexOf('PartyClassCode') >= 0 )
        dictVar = 'app.GlobalDict.PartyClass';
    else {
        return null;
    }
    return dictVar;
}

})();//end pack
