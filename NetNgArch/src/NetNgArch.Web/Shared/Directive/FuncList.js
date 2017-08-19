﻿(function (String, angular) {
    'use strict';

    app.directive("pbFunctree",['pb', function factory(pb) {
        var directiveDefinitionObject = {
            priority: 0,
            templateUrl: "../Common/FuncList",
            // transclude: false,
            restrict: 'EA',
            scope: {
                inputModel: "=inputModel",
                treeModel: "=treeModel",
                disabled: "=disabled"
            },
            controllerAs: 'd',
            controller: ['$scope', '$element', '$attrs', '$transclude',function ($scope, $element, $attrs, $transclude) {
                var d = this;
                d.chk_change = function (level) {
                    var foundIdx = 0;
                    for (var i = 0; i < $scope.d.levelList.length; i++) {
                        if ($scope.d.levelList[i] == level) {
                            foundIdx = i;
                            break;
                        }
                    }
                    for (var j = foundIdx + 1; j < $scope.d.levelList.length; j++) {
                        if ($scope.d.levelList[j].length <= level.length) {
                            break;
                        }
                        $scope.d.selectedList[j] = $scope.d.selectedList[foundIdx];
                    }
                };
            }],
            link: function (scope, iElement, iAttrs, controller) {
                var SetUnCheck = function (funcIds, canChange) {
                    var funcIdArray = funcIds.split(',');
                    angular.forEach(funcIdArray, function (funcId) {
                        angular.forEach(scope.d.funcList, function (selVal, index) {
                            if (selVal == funcId) {
                                scope.d.selectedList[index] = false;
                                scope.d.disableList[index] = !canChange;
                            }
                        });
                    });
                };
                scope.d.funcList = scope.d._initData.funcList;
                scope.d.levelList = scope.d._initData.levelList;
                scope.d.selectedList = [];
                for (var i = 0; i < scope.d.funcList.length; i++) {
                    scope.d.selectedList[i] = true;
                }
                scope.d.disableList = [];
                scope.treeModel = { funcList: scope.d.funcList, selectedList: scope.d.selectedList, disableList: scope.d.disableList };
                var canChangeDept = angular.isUndefined(scope.inputModel.UserFuncIds);
                var canChangeCorp = angular.isUndefined(scope.inputModel.DeptFuncIds);
                SetUnCheck(scope.inputModel.CorpFuncIds, canChangeCorp);
                if (scope.inputModel.DeptFuncIds) SetUnCheck(scope.inputModel.DeptFuncIds, canChangeDept);
                if (scope.inputModel.UserFuncIds) SetUnCheck(scope.inputModel.UserFuncIds, true);

            }
        };
        return directiveDefinitionObject;
    }]);



} (String, angular));                                                                  //end pack