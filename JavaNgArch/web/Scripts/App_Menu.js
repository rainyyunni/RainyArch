//menu Text should be unique
app.constant('App_MenuData', [
               { Text: 'Sample', FuncCode: 'M_Sample', stateRef: '', SubMenus: [
                                                            { Text: 'Task', FuncCode: 'M_Task', StateName: '/ta/Task/SrcCode'},
                                                            { Text: 'TaskItem', FuncCode: 'M_TaskItem', StateName: '/ta/TaskItem/SrcCode' }]               
               },
               { Text: 'System', FuncCode: 'M_System', SubMenus: [
                                                            { Text: 'Corp', FuncCode: 'M_Corp', StateName: '/gn/Corp/MultiViewsSample' },
                                                            { Text: 'Dept', FuncCode: 'M_Dept', StateName: '/gn/Dept/SrcCode' },
                                                            { Text: 'User', FuncCode: 'M_User', StateName: '/gn/User/List'}]
               },
               { Text: 'Personal', FuncCode: 'M_Personal', SubMenus: [
                                                                { Text: 'ChangePass', FuncCode: 'M_Pass', StateName: '/gn/User/PassEdit'}]
               }
]);
