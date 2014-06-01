var appServices = angular.module('app.services', ['Postman', 'ngAnimate']);

appServices.service('notificationsService', ['postman', function(postman) {
    return {
        error: function(title, warning) {
            postman.error(title, warning)
        },
        info: function(title, warning) {
            postman.info(title, warning)
        },
        warning: function(title, warning) {
            postman.warn(title, warning)
        },
        success: function(title, warning) {
            postman.success(title, warning)
        }
    }
}]);

//TODO: ten callback działa bardzo kulawo, warto to dopracować @rnw
appServices.service('userService', ['$http', function(http) {
    return {
        getLoggedUser: function (callback) {
            http({method: 'GET', url: '/user/logged'}).then(function (result) {
                if(callback) {
                    callback(result.data.object);
                }
            });
        }
    };
}]);

appServices.service('permissionService', ['$http', '$log', function($http, $log) {
    return {
        hasPermission: function (permissionNameArg, callback) {
            var permission = {permissionName: permissionNameArg};
            var params = JSON.stringify(permission);

            $http.post('/permission/hasPermission', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).then(function(result){
                if(callback) {
                    callback(result.data.object);
                }
            });
        }
    };
}]);