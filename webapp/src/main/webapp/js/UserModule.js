var userModule = angular.module('userModule', []);

userModule.controller('UserListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.users = [ ];

        $http.get('/user/all').success(function(data) {
            $scope.users = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve user list!";
        });
    }
]);

userModule.controller('UserDetailsController', ['$scope', '$http', '$routeParams', '$location', 'notificationsService',
    function(scope, http, routeParams, location, notificationsService) {
        scope.login = null;

        http.get('/user/' + routeParams.login).success(function(data) {
            scope.user = data.object;
        }).error(function(data, status, headers, config) {
            notificationsService.error('Error', 'Can\'t fetch user\'s details!');
            location.path('/user');
        });
    }
]);