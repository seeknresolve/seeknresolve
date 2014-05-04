var userControllers = angular.module('userControllers', []);

userControllers.controller('UserListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.users = [ ];

        $http.get('/user/all').success(function(data) {
            $scope.users = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve user list!";
        });
    }
]);