//menu text should be unique
app.constant('App_MenuData', [
                              	{ text: 'Introduction', funcCode: 'M_CZ', stateRef: '', subMenus: [
                                                      		{ text: 'Introduction', funcCode: 'M_EduProduct', stateName: '/home/Home/Introduction' },
                                                           { text: 'Architecture', funcCode: 'M_Member', stateName: '/home/Home/Architecture'}]               
                              	},
                              	{ text: 'Guide', funcCode: 'M_CZ', stateRef: '', subMenus: [
                                                                                               { text: 'ProjectSetup', funcCode: 'M_Member', stateName: '/guide/Guide/ProjectSetup'}]               
                               },
                               { text: 'APIReference', funcCode: 'M_TA', stateRef: '', subMenus: [
                                                            { text: 'Reference1', funcCode: 'M_Task', stateName: '/api/Domain/Reference1'}]               
                               }
]);

