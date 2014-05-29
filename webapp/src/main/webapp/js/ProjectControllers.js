var projectControllers = angular.module('projectControllers', ['app.services']);

projectControllers.controller('ProjectListController', ['$scope', '$http',
    function(scope, http) {
        scope.projects = [];

        http.get('/project/all').success(function(data) {
            scope.projects = data.object;
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Can't retrieve project list!";
        });
    }
]);

projectControllers.controller('ProjectDetailsController', ['$scope', '$http', '$routeParams',
    function(scope, http, routeParams) {
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
    }
]);

projectControllers.controller('ProjectCreateController', ['$scope', '$http', '$location', 'notificationsService',
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
