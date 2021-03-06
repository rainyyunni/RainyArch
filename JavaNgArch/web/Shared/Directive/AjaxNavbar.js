﻿(function (String, angular) {
    'use strict';

    app.directive("ajaxNavbar", ['pb','APP_ContextPrefix', function factory(pb,APP_ContextPrefix) {
        var directiveDefinitionObject = {
            priority: 0,
            templateUrl: APP_ContextPrefix+"/Shared/Directive/AjaxNavbar.htm",
            // transclude: false,
            restrict: 'EA',
            scope: true,
            controllerAs: 'd',
            controller: ['$scope', '$element', '$attrs', '$transclude',function ($scope, $element, $attrs, $transclude) {
                var d = this;
                d.AjaxNavStack=pb.AjaxNavData.Stack;
                d.AjaxNavRefresh = function () {
                	pb.AjaxNavRefresh();
                };
                d.AjaxNavBack = function () {
                    pb.AjaxNavBack();
                };
                d.AjaxNavTo = function (state,stateParams) {
                	pb.AjaxNavTo(state,stateParams);
                };
            }]
        };
        return directiveDefinitionObject;
    }]);



} (String, angular));                                                          //end pack