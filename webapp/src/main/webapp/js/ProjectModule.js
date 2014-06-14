var projectModule = angular.module('projectModule', ['app.services', 'highcharts-ng']);

projectModule.controller('ProjectListController', ['$scope', '$http', '$location',
    function(scope, http, location) {
        scope.projects = [];

        http.get('/project/all').success(function(data) {
            scope.projects = data.object;
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Can't retrieve project list!";
        });

        scope.searchProjects = function(query) {
            return http.get('/project/search?query=' + query).then(function(result) {
                return result.data.object;
            });
        };

        scope.selectProject = function(item, model, label) {
            location.path('/project/' + model.id);
        };
    }
]);

projectModule.controller('ProjectUserAssignController', ['$scope', '$modalInstance', '$http', 'projectId',
    function(scope, modalInstance, http, projectId) {
        scope.selected = {
            user: null,
            role: null
        };

        scope.users = [];
        http.get('/user/notAssignedToProject/' + projectId).success(function(data) {
            scope.users = data.object;
            scope.selected.user = scope.users[0];
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Can't retrieve user list!";
        });

        scope.roles = [];
        http.get('/role/projects').success(function(data) {
            scope.roles = data.object;
            scope.selected.role = scope.roles[0];
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Can't retrieve project role list!";
        });

        scope.ok = function () {
            userProjectRoleDto = {
                userId: scope.selected.user.id,
                role: scope.selected.role.roleName,
                projectId: projectId
            };
            modalInstance.close(userProjectRoleDto);
        };

        scope.cancel = function () {
            modalInstance.dismiss('cancel');
        };
    }
]);

function getProjectDetails(scope, http, projectId) {
    http.get('/project/' + projectId).success(function(data) {
        scope.project = data.object;
    }).error(function(data, status, headers, config) {
        if(data.error) {
            scope.errorMessage = data.error;
        } else {
            scope.errorMessage = "Can't retrieve project details!";
        }
    });
}

projectModule.controller('ProjectDetailsController', ['$scope', '$http', '$routeParams', 'notificationsService', '$modal',
    function(scope, http, routeParams, notificationsService, modal) {
        scope.id = null;
        scope.projectRevisionDiffs = [];
        scope.project = null;

        getProjectDetails(scope, http, routeParams.id);

        http.get('/projectRevision/all/' + routeParams.id).success(function(data) {
            scope.projectRevisionDiffs = data.object;
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Error getting project history";
        });

        scope.stats = {
            date: [1,2,3,4,5,6,7,8,9,10],
            closedBugs: [0, 0, 1, 4, 10, 3, 12, 2, 0, 0],
            openedBugs: [10, 15, 12, 8, 7, 1, 1, 19, 15, 10]
        };


        scope.chartConfig = {
            options: {
                chart: {
                    type: 'areaspline',
                    zoomType: 'x'
                }
            },
            title: {
                text: 'bugs history'
            },
            loading: false,
            xAxis: {
                title: {
                    text: 'Date'
                },
                categories: scope.stats.date,
                tickmarkPlacement: 'on',
                endOnTick: 'true',
                startOnTick: 'true'
            },
            yAxis: {
                title: {
                    text: 'Number of bugs'
                }
            },
            series: [{
                name: 'Opened bugs',
                data: scope.stats.openedBugs,
                color: '#FF6A48'
            }, {
                name: 'Closed bugs',
                data: scope.stats.closedBugs,
                color: '#A7FF4C'
            }]
        };

        scope.removeAssignment = function(userId) {
            http.delete('/project/' + scope.project.id + '/revokeRole/user/' + userId).
                success(function (data, status, headers, config) {
                    notificationsService.success("User role have been revoked.");
                    getProjectDetails(scope, http, routeParams.id);
                }).error(function(data, status, headers, config) {
                    scope.errorMessage = "Can't revoke user role.";
                });
        };

        scope.openAssignPopup = function () {
            var modalInstance = modal.open({
                templateUrl: 'projectUserAssignment.html',
                controller: 'ProjectUserAssignController',
                resolve: {
                    projectId: function () {
                        return routeParams.id;
                    }
                }
            });

            modalInstance.result.then(function (userProjectRole) {
                http.post('/project/' + userProjectRole.projectId + '/grantRole/' + userProjectRole.role + '/user/' + userProjectRole.userId).
                    success(function (data, status, headers, config) {
                        notificationsService.success('User has been assigned successfully.');
                        getProjectDetails(scope, http, routeParams.id);
                    }).error(function (data, status, headers, config) {
                    notificationsService.error('Error', 'Assigning user failed! ' + data.error);
                    });

            });
        };
    }
]);

projectModule.controller('ProjectCreateController', ['$scope', '$http', '$location', 'notificationsService',
    function(scope, http, location, notificationsService) {
        scope.createProject = function() {
            var project = scope.project;
            var params = JSON.stringify(project);

            http.post('/project/create', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'Project ' + data.object.name + ' created successfully');
                location.path('/project');
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Creating project failed! ' + data.error);
                location.path('/project');
            });
        }
    }
]);