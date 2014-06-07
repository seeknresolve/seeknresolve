var roleModule = angular.module('roleModule', []);

roleModule.controller('UserRoleListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.userRoles = [ ];

        $http.get('/role/users').success(function(data) {
            $scope.userRoles = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve user role list!";
        });
    }
]);

roleModule.controller('ProjectRoleListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.projectRoles = [ ];

        $http.get('/role/projects').success(function(data) {
            $scope.projectRoles = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve project role list!";
        });
    }
]);

roleModule.controller('RoleDetailsController', ['$scope', '$http', '$route', '$routeParams', 'notificationsService', 'permissionService',
    function($scope, $http, $route, $routeParams, notificationsService, permissionService) {
        $scope.roleName = null;

        $scope.isAddingPermission = false;
        $scope.isDeletingPermission = false;

        $scope.permissionsToAdd = [];
        $scope.permissionsToDelete = [];

        $scope.permissionToAdd = {};
        $scope.permissionToDelete = {};

        $scope.shouldShowAddPermissionButton = false;
        $scope.shouldShowDeletePermissionButton = false;

        permissionService.hasPermission('role:add_permission', function(has){$scope.shouldShowAddPermissionButton = (has == "true")});
        permissionService.hasPermission('role:delete_permission', function(has){$scope.shouldShowDeletePermissionButton = (has == "true")});

        $http.get('/role/details/' + $routeParams.roleName).success(function(data) {
            $scope.role = data.object;
        }).error(function(data, status, headers, config) {
            notificationsService.error('Can\'t fetch role\'s details!');
        });

        $scope.showAddPermission = function() {
            $http.get('/permission/notInRole/' + $routeParams.roleName).success(function(data) {
                $scope.permissionsToAdd = data.object;
                $scope.isAddingPermission = true;
            }).error(function(data, status, headers, config) {
                notificationsService.error('Can\'t add permission');
            });
        }

        $scope.showDeletePermission = function() {
            $scope.permissionsToDelete = $scope.role.permissions;
            $scope.isDeletingPermission = true;
        }

        $scope.addPermission = function() {
            var change = {};
            change.roleName = $scope.role.roleName,
            change.permissionName = $scope.permissionToAdd.permissionName
            var params = JSON.stringify(change);

            $http.post('/role/addPermission', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'Permission ' + data.object.permissionName + 'was granted to role' + data.object.roleName);
                $route.reload();
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Granting permission failed!');
                $route.reload();
            });
        }

        $scope.deletePermission = function() {
            var change = {};
            change.roleName = $scope.role.roleName,
            change.permissionName = $scope.permissionToDelete.permissionName
            var params = JSON.stringify(change);

            $http.post('/role/deletePermission', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'Permission ' + data.object.permissionName + 'was deleted from role ' + data.object.roleName);
                $route.reload();
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Deleting permission failed!');
                $route.reload();
            });
        }

        $scope.cancelAddPermission = function() {
            $scope.isAddingPermission = false;
            $scope.permissionsToAdd = [];
            $scope.permissionToAdd = {};
        }

        $scope.cancelDeletePermission = function() {
            $scope.isDeletingPermission = false;
            $scope.permissionsToDelete = [];
            $scope.permissionToDelete = {};
        }
    }
]);