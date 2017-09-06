//menu text should be unique
app.constant('App_MenuData', [
                              	{ Text: 'Introduction', SubMenus: [
                                                      		{ Text: 'Introduction', StateName: '/home/Home/Introduction' },
                                                           { Text: 'Architecture', StateName: '/home/Home/Architecture'}]               
                              	},
                              	{ Text: 'Guide', SubMenus: [
                                                            { Text: 'ProjectSetup', StateName: '/guide/Guide/ProjectSetup'},
                                                            { Text: 'Config', StateName: '/guide/Guide/Config'},
                                                            { Text: 'DirStructure', StateName: '/guide/Guide/DirStructure'},
                                                            { Text: 'Convention', StateName: '/guide/Guide/Convention'},
                                                            { Text: 'HowTo', StateName: '/guide/Guide/HowTo'},
                                                            { Text: 'DO', StateName: '/guide/Guide/DO'},
                                                            { Text: 'BD', StateName: '/guide/Guide/BD'},
                                                            { Text: 'Controller', StateName: '/guide/Guide/Controller'},
                                                            { Text: 'ViewModel', StateName: '/guide/Guide/ViewModel'},
                                                            { Text: 'View', StateName: '/guide/Guide/View'},
                                                            { Text: 'JS', StateName: '/guide/Guide/JS'}]               
                               },
                               { Text: 'APIReference', SubMenus: [
                                                            { Text: 'Domain', StateName: '/api/Domain/Reference1'},
                                                            { Text: 'Mvc', StateName: '/api/Mvc/Reference1'},
                                                            { Text: 'Utils', StateName: '/api/Utils/Reference1'},
                                                            { Text: 'JS', StateName: '/api/JS/Reference1'},
                                                            { Text: 'Desktop', StateName: '/api/Desktop/Reference1'}]               
                               }
]);

