//固定的字典信息
(function (String, angular) {
    'use strict';

angular.module('app').constant("App_Dict",{

TrueDisplay:'是',
FalseDisplay:'否',

Task_StatusEnum: {
    "自动": 0,
    "完成": 1,
    "关闭": 2,
    "取消": 3
}

});
} (String, angular));                    //end pack