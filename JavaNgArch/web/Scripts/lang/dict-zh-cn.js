//固定的字典信息
(function (String, angular) {
    'use strict';

angular.module('projectbase').constant("App_Dict",{

TrueDisplay:'是',
FalseDisplay:'否',
MemberNote_NoteClassEnum:{
"暂存":0,
"重要1":1
},
MemberNote_ResultEnum:{
"未处理":0,
"审核中":1,
"已记录":2
},
Task_StatusEnum: {
    "自动": 0,
    "完成": 1,
    "关闭": 2,
    "取消": 3
}

});
} (String, angular));                    //end pack