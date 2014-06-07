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

userModule.controller('UserDetailsController', ['$scope', '$http', '$route', '$routeParams', '$location', 'notificationsService', 'permissionService',
    function(scope, http, route, routeParams, location, notificationsService, permissionService) {
        scope.login = null;
        scope.userRoles = [ ];
        scope.shouldShowEditButton = false;

        permissionService.hasPermission('user:update', function(has){scope.shouldShowEditButton = (has == "true")});

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
            http.put('/user', params, {
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
    }
]);