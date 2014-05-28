var projectControllers = angular.module('projectControllers', []);

projectControllers.controller('ProjectListController', ['$scope', '$http', '$rootScope',
    function(scope, http, rootScope) {
        scope.projects = [];
        scope.message = rootScope.message;

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

projectControllers.controller('ProjectCreateController', ['$scope', '$http', '$location', '$rootScope',
    function(scope, http, location, rootScope) {
        scope.createProject = function() {
            var project = scope.project;
            var params = JSON.stringify(project);

            http.post('/project/create', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                rootScope.message = 'Project created sucessfully';
                location.path('/project');
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Creating project failed! " + data.error;
            });
        }
    }
]);
