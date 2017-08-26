//menu text should be unique
app.constant('App_MenuData', [
                              	{ Text: 'Introduction', SubMenus: [
                                                      		{ Text: 'Introduction', StateName: '/home/Home/Introduction' },
                                                           { Text: 'Architecture', StateName: '/home/Home/Architecture'}]               
                              	},
                              	{ Text: 'Guide', SubMenus: [
                                                            { Text: 'ProjectSetup', StateName: '/guide/Guide/ProjectSetup'},
                                                            { Text: 'Config', StateName: '/guide/Guide/Config'},
                                                            { Text: 'DirStructure', StateName: '/guide/Guide/DirStructure'}]               
                               },
                               { Text: 'APIReference', SubMenus: [
                                                            { Text: 'Configuration', StateName: '/api/Domain/Configuration'}]               
                               }
]);

