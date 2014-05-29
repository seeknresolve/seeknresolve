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

permissionControllers.controller('PermissionCreateController', ['$scope', '$http', '$location', 'notificationsService',
    function(scope, http, location, notificationsService) {
        scope.createPermission = function() {
            var permission = scope.permission;
            var params = JSON.stringify(permission);

            http.post('/permission/create', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'Permission ' + data.object.permissionName + ' created successfully');
                location.path('/permission/all');
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Creating permission failed! ' + data.error);
                location.path('/permission/all');
            });
        }
    }
]);