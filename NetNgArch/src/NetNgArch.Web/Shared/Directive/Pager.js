(function (String, angular) {
    'use strict';

    app.directive("pagerInput", ['pb', '$timeout', function factory(pb, $timeout) {
        var directiveDefinitionObject = {
            priority: 0,
            templateUrl: "../Shared/Directive/Pager.htm",
            //transclude: false,
            restrict: 'A',
            scope: {
                m: "=pagerModel"
            },
            controllerAs: 'd',
            controller: ['$scope', '$element', '$attrs', '$transclude', function ($scope, $element, $attrs, $transclude) {
                var d = this;
                var m = $scope.m;
                if (m == null) {
                    m = { PageNum: 1, PageSize: 0, PageCount: 0 };
                }
                d.inputName_PageNum = $attrs["pagerInput"] + ".PageNum";
                d.inputName_PageSize = $attrs["pagerInput"] + ".PageSize";
                d.submitData = function () {
                    return { "ajax-data":
                        d.inputName_PageNum + "=" + $scope.m.PageNum + "&" + d.inputName_PageSize + "=" + $scope.m.PageSize
                    };
                };
                d.pagerShort = $attrs["pagerShort"];
                d.pagesizeItems = [5, 15, 100];
                if ($attrs["pagerSizemax"] != null) d.pagesizeItems[3] = $attrs["pagerSizemax"];
                if (m.PageNum == null || m.PageNum < 1) m.PageNum = 1;
                //adjust the pageIndex to the actual number of the last page
                if (m.PageNum > m.PageCount) {
                    m.PageNum = m.PageCount;
                }
                if (m.PageCount >= 0 && m.PageNum <= 0) {
                    m.PageNum = 1;
                }
                d.goPageNum = m.PageNum;
                d.firstpage_click = function () {
                    if ($scope.m.PageNum != 1) {
                        d.goPageNum = $scope.m.PageNum = 1;
                        pb.AjaxSubmit($element, null, d.submitData());
                    }
                    return false;
                };
                d.prevpage_click = function () {
                    if ($scope.m.PageNum > 1) {
                        d.goPageNum = $scope.m.PageNum -= 1;
                        pb.AjaxSubmit($element, null, d.submitData());
                    }
                    return false;
                };
                d.nextpage_click = function () {
                    if ($scope.m.PageNum < $scope.m.PageCount) {
                        d.goPageNum = $scope.m.PageNum += 1;
                        pb.AjaxSubmit($element, null, d.submitData());
                    }
                    return false;
                };
                d.lastpage_click = function () {
                    if ($scope.m.PageNum != $scope.m.PageCount) {
                        d.goPageNum = $scope.m.PageNum = $scope.m.PageCount;
                        pb.AjaxSubmit($element, null, d.submitData());
                    }
                    return false;
                };
                d.go_click = function () {
                    if (d.goPageNum == null) return;
                    $scope.m.PageNum = d.goPageNum;
                    pb.AjaxSubmit($element, null, d.submitData(), function (response) {//直接手动绑定，对比下面依赖自动绑定的做法，两种方法二选一
                        d.goPageNum = response.data.ViewModel.Input.Pager.PageNum;
                    });
                };
                d.pagesize_change = function () {
                    pb.AjaxSubmit($element, null, d.submitData(), function () {
                        $timeout(function () {//此处依赖angular自动绑定response中数据到$scope（自动绑定将在当前代码控制序列结束后才进行）,因此需延迟使用
                            d.goPageNum = $scope.m.PageNum;
                        });
                    });
                };
            } ]
        };
        return directiveDefinitionObject;
    } ]);



} (String, angular));                                            //end pack