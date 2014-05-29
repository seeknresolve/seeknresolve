var roleControllers = angular.module('roleControllers', []);

roleControllers.controller('UserRoleListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.userRoles = [ ];

        $http.get('/role/users').success(function(data) {
            $scope.userRoles = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve user role list!";
        });
    }
]);

roleControllers.controller('ProjectRoleListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.projectRoles = [ ];

        $http.get('/role/projects').success(function(data) {
            $scope.projectRoles = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve project role list!";
        });
    }
]);

bugControllers.controller('RoleDetailsController', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams) {
        $scope.roleName = null;

        $http.get('/role/' + $routeParams.roleName).success(function(data) {
            $scope.role = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = 'Can\'t fetch role\'s details!';
        });
    }
]);