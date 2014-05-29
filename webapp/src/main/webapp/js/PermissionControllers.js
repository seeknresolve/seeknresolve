var permissionControllers = angular.module('permissionControllers', []);

permissionControllers.controller('PermissionListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.permissions = [ ];

        $http.get('/permission/all').success(function(data) {
            $scope.permissions = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve permissions list!";
        });
    }
]);

permissionControllers.controller('PermissionCreateController', ['$scope', '$http',
    function($scope, $http) {
        $http({
            url:'/permission/create',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data: {permissionName: $scope.permissionName}
        });
    }
]);