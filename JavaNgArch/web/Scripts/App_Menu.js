//menu text should be unique
app.constant('App_MenuData', [
               { text: 'Sample', funcCode: 'M_Sample', stateRef: '', subMenus: [
                                                            { text: 'Task', funcCode: 'M_Task', stateName: '/ta/Task/SrcCode'},
                                                            { text: 'TaskItem', funcCode: 'M_TaskItem', stateName: '/ta/TaskItem/SrcCode' }]               
               },
               { text: 'System', funcCode: 'M_System', subMenus: [
                                                            { text: 'Corp', funcCode: 'M_Corp', stateName: '/gn/Corp/MultiViewsSample' },
                                                            { text: 'Dept', funcCode: 'M_Dept', stateName: '/gn/Dept/SrcCode' },
                                                            { text: 'User', funcCode: 'M_User', stateName: '/gn/User/List'}]
               },
               { text: 'Personal', funcCode: 'M_Personal', subMenus: [
                                                                { text: 'ChangePass', funcCode: 'M_Pass', stateName: '/gn/User/PassEdit'}]
               }
]);
