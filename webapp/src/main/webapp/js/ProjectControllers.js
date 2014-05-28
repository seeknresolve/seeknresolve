var projectControllers = angular.module('projectControllers', []);

projectControllers.service('alertsService', function() {
    var alerts = [ ];
    return {
        addAlert: function(type, message) {
            alerts.push({type:type, msg: message});
        },

        getAlerts: function() {
            return alerts;
        }
    }
});

var ProjectAlertController = ['$scope', 'alertsService', function(scope, alertsService) {
    scope.alerts = alertsService.getAlerts();

    scope.closeAlert = function(index) {
        scope.alerts.splice(index, 1);
    };
}];

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

projectControllers.controller('ProjectCreateController', ['$scope', '$http', '$location', 'alertsService',
    function(scope, http, location, alertsService) {
        scope.createProject = function() {
            var project = scope.project;
            var params = JSON.stringify(project);

            http.post('/project/create', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                alertsService.addAlert('success', 'Project created successfully');
                location.path('/project');
            }).error(function (data, status, headers, config) {
                alertsService.addAlert('error', 'Creating project failed! ' + data.error);
                location.path('/project');
            });
        }
    }
]);
