var bugControllers = angular.module('bugControllers', ['app.services']);

bugControllers.controller('BugListController', ['$scope', '$http', 'notificationsService',
    function(scope, http, notificationsService) {
        scope.bugs = [ ];

        http.get('/bug/all').success(function(data) {
            scope.bugs = data.object;
        }).error(function(data, status, headers, config) {
            notificationsService.error('Error', 'Can\'t fetch bugs list!');
        });
    }
]);

bugControllers.controller('BugDetailsController', ['$scope', '$http', '$routeParams', '$location', 'notificationsService',
    function(scope, http, routeParams, location, notificationsService) {
        scope.tag = null;

        http.get('/bug/' + routeParams.tag).success(function(data) {
            scope.bug = data.object;
        }).error(function(data, status, headers, config) {
            if(data.error) {
                scope.errorMessage = data.error;
            } else {
                notificationsService.error('Error', 'Can\'t fetch bug\'s details!');
            }
        });

        scope.deleteBug = function(tag) {
            http.delete('/bug/' + tag).success(function (data, status, headers, config) {
                notificationsService.warning('Delete', 'Bug ' + tag + ' was deleted');
                location.path('/bug');
            }).error(function (data, status, headers, config) {
                scope.errorMessage = 'Can\'t delete bug!';
            });
        };
    }
]);

bugControllers.controller('BugCreateController', ['$scope', '$http',
    function(scope, http) {

    }
]);