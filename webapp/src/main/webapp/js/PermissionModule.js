var permissionModule = angular.module('permissionModule', []);

permissionModule.controller('PermissionListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.permissions = [ ];

        $http.get('/permission/all').success(function(data) {
            $scope.permissions = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve permissions list!";
        });
    }
]);

permissionModule.controller('PermissionCreateController', ['$scope', '$http', '$location', 'notificationsService',
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

permissionModule.controller('PermissionDetailsController', ['$scope', '$http', '$routeParams', '$location', 'notificationsService',
    function(scope, http, routeParams, location, notificationsService) {
        scope.permissionName = null;

        http.get('/permission/' + routeParams.permissionName).success(function(data) {
            scope.permission = data.object;
        }).error(function(data, status, headers, config) {
            notificationsService.error('Error', 'Obtaining permission details failed!');
            location.path('/permission/all');
        });

        scope.deletePermission = function(permissionName) {
            http.delete('/permission/' + permissionName).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'Permission ' + permissionName + ' deleted successfully');
                location.path('/permission/all');
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Deleting permission failed!');
                location.path('/permission/all');
            });
        };
    }
]);
