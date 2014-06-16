var userModule = angular.module('userModule', []);

userModule.controller('UserListController', ['$scope', '$http', 'permissionService',
    function($scope, $http, permissionService) {
        $scope.users = [ ];
        $scope.shouldShowCreateButton = false;

        $http.get('/user/all').success(function(data) {
            $scope.users = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve user list!";
        });

        permissionService.hasPermission('user:create', function(has){$scope.shouldShowCreateButton = (has == "true")});
    }
]);

userModule.controller('UserCreateController', ['$scope', '$http', '$location', 'notificationsService',
    function(scope, http, location, notificationsService) {
        scope.userRoles = [ ];
        scope.creatingUser = null;

        http.get('/role/users').success(function(data) {
            scope.userRoles = data.object;
        }).error(function(data, status, headers, config) {
            notificationsService.error('Error', 'Getting user roles failed');
            location.path('/user');
        });

        scope.createUser = function() {
            var user = scope.creatingUser;
            var params = JSON.stringify(user);

            http.post('/user/create', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'User ' + data.object.login + ' created successfully');
                location.path('/user');
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Creating user failed!');
                location.path('/user');
            });
        }
    }
]);

function getUserHistory(scope, http, routeParams, notificationsService) {
    if(scope.shouldShowUserHistory) {
        http.get('/userRevision/all/' + routeParams.login).success(function (data) {
            scope.userRevisionDiffs = data.object;
        }).error(function (data, status, headers, config) {
            notificationsService.error("Error getting project history");
        });
    }
}

userModule.controller('UserDetailsController', ['$scope', '$http', '$route', '$routeParams', '$location', 'notificationsService', 'permissionService',
    function(scope, http, route, routeParams, location, notificationsService, permissionService) {
        scope.login = null;
        scope.userRoles = [ ];
        scope.shouldShowEditButton = false;
        scope.shouldShowChangePasswordButton = false;
        scope.isChangingPassword = false;
        scope.changingPassword = {};
        scope.shouldShowUserHistory = false;
        scope.userRevisionDiffs = [];

        permissionService.hasPermission('user:update', function(has){scope.shouldShowEditButton = (has == "true")});
        permissionService.hasPermission('user:change_password', function(has){scope.shouldShowChangePasswordButton = (has == "true")});
        permissionService.hasPermission('user:view_history', function(has){
            scope.shouldShowUserHistory = (has == "true");
            if(scope.shouldShowUserHistory) {
                getUserHistory(scope, http, routeParams);
            }
        });

        http.get('/user/details/' + routeParams.login).success(function(data) {
            scope.user = data.object;
        }).error(function(data, status, headers, config) {
            notificationsService.error('Error', 'Can\'t fetch user\'s details!');
            location.path('/user');
        });

        http.get('/role/users').success(function(data) {
            scope.userRoles = data.object;
        }).error(function(data, status, headers, config) {
            notificationsService.error('Error', 'Getting user roles failed');
            location.path('/user');
        });

        scope.updateUser = function() {
            var params = JSON.stringify(scope.user);
            http.post('/user/update', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.info('Info', 'User ' + scope.user.login + ' was updated');
                route.reload();
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Can\'t update user!');
                route.reload();
            });
        };

        scope.showChangePassword = function() {
            scope.isChangingPassword = true;
        }

        scope.cancelChangePassword = function() {
            scope.isChangingPassword = false;
            scope.changingPassword = {};
        }

        scope.changePassword = function() {
            if(scope.changingPassword.password != scope.changingPassword.confirmPassword) {
                notificationsService.error('Error', 'Passwords aren\'t equals');
            } else {
                scope.changingPassword.login = scope.user.login;
                var params = JSON.stringify(scope.changingPassword);
                http.post('/user/changePassword', params, {
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data, status, headers, config) {
                    notificationsService.info('Info', 'User ' + scope.user.login + ' password was changed');
                    route.reload();
                }).error(function (data, status, headers, config) {
                    notificationsService.error('Error', 'Can\'t change user password!');
                    route.reload();
                });
            }
        }
    }
]);