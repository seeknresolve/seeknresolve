var projectModule = angular.module('projectModule', ['app.services']);

projectModule.controller('ProjectListController', ['$scope', '$http',
    function(scope, http) {
        scope.projects = [];

        http.get('/project/all').success(function(data) {
            scope.projects = data.object;
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Can't retrieve project list!";
        });
    }
]);

projectModule.controller('ProjectUserAssignController', ['$scope', '$modalInstance', '$http',
    function(scope, modalInstance, http) {
        scope.selected = {
            role: null,
            user: null,
            project: null
        };

        scope.roles = [];
        http.get('/role/projects').success(function(data) {
            scope.roles = data.object;
            scope.selected.role = scope.roles[0];
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Can't retrieve project role list!";
        });

        scope.users = [];
        http.get('/user/all').success(function(data) {
            scope.users = data.object;
            scope.selected.user = scope.users[0];
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Can't retrieve user list!";
        });

        scope.ok = function () {
            dto = {
                role: scope.selected.role.roleName,
                userId: scope.selected.user.id
            }
            modalInstance.close(dto);
        };

        scope.cancel = function () {
            modalInstance.dismiss('cancel');
        };
    }
]);

projectModule.controller('ProjectDetailsController', ['$scope', '$http', '$routeParams', '$modal',
    function(scope, http, routeParams, modal) {
        scope.id = null;

        http.get('/project/' + routeParams.id).success(function(data) {
            scope.project = data.object;
        }).error(function(data, status, headers, config) {
            if(data.error) {
                scope.errorMessage = data.error;
            } else {
                scope.errorMessage = "Can't retrieve project details!";
            }
        });

        scope.openAssignPopup = function () {
            var modalInstance = modal.open({
                templateUrl: 'projectUserAssignment.html',
                controller: 'ProjectUserAssignController'
            });

            modalInstance.result.then(function (dto) {
                dto.projectId = scope.project.id;
                var params = JSON.stringify(dto);

                http.post('project/grantRole', params, {
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








            }, function () {
                $log.info('Modal dismissed at: ' + new Date() + scope.selectedUser + ' ' + scope.selectedRole);
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