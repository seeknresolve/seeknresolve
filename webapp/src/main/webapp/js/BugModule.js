var bugModule = angular.module('bugModule', ['app.services']);

bugModule.directive('bugPriority', function() {
    return {
        restrict: 'E',
        scope: true,
        template: '<span class="label {{class}}">{{priority}}</span>',
        link: function (scope, element, attrs) {
            scope.priority = attrs.priority;
            switch (scope.priority) {
                case "LOW":
                    scope.class = 'label-default';
                    break;

                case "NORMAL":
                    scope.class = 'label-info';
                    break;

                case "HIGH":
                    scope.class = 'label-warning';
                    break;

                case "CRITICAL":
                    scope.class = 'label-critical';
                    break;

                default:
                    scope.class = 'label-primary';
                    break;
            }
        }
    }
});

bugModule.filter('priorityFilter', function(item) {

});

bugModule.controller('BugListController', ['$scope', '$http', 'notificationsService',
    function(scope, http, notificationsService) {
        scope.bugs = [ ];

        http.get('/bug/all').success(function(data) {
            scope.bugs = data.object;
        }).error(function(data, status, headers, config) {
            notificationsService.error('Error', 'Can\'t fetch bugs list!');
        });
    }
]);

bugModule.controller('BugDetailsController', ['$scope', '$http', '$routeParams', '$location', 'notificationsService',
    function(scope, http, routeParams, location, notificationsService) {
        scope.tag = null;

        http.get('/bug/' + routeParams.tag).success(function(data) {
            scope.bug = data.object;
        }).error(function(data, status, headers, config) {
            if(data.error) {
                notificationsService.error('Error', 'Can\'t fetch bug\'s details! ' + data.error);
            } else {
                notificationsService.error('Error', 'Can\'t fetch bug\'s details!');
            }
            location.path('/bug');
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

bugModule.controller('BugCreateController', ['$scope', '$http', '$location', 'notificationsService', 'userService',
    function(scope, http, location, notificationsService, userService) {
        scope.loggedUser = null;
        userService.getLoggedUser(function(user) {
            scope.loggedUser = user;
        });

        scope.createBug = function() {
            var bug = scope.bug;
            var params = JSON.stringify(bug);

            http.post('/bug/create', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'Bug ' + data.object.tag + ' reported successfully');
                location.path('/bug');
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Creating bug failed! ' + data.error);
                location.path('/bug');
            });
        }
    }
]);