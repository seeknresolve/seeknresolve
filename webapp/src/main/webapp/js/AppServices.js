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
