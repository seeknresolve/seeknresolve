var roleControllers = angular.module('roleControllers', []);

roleControllers.controller('UserRoleListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.userRoles = [ ];

        $http.get('/role/all_user').success(function(data) {
            $scope.userRoles = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve user role list!";
        });
    }
]);

roleControllers.controller('ProjectRoleListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.projectRoles = [ ];

        $http.get('/role/all_project').success(function(data) {
            $scope.projectRoles = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve project role list!";
        });
    }
]);