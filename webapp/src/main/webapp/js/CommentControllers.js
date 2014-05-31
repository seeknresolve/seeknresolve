var commentControllers = angular.module('commentControllers', []);

commentControllers.controller('CommentCreateController', ['$scope', '$http', '$location', 'notificationsService',
    function(scope, http, location, notificationsService) {
        scope.createComment = function() {
            var comment = scope.comment;
            var params = JSON.stringify(comment);

            http.post('/comment/create', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'Comment created successfully');
                //TODO: Przekierowac do konkretnego buga
                location.path('/bug');
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Creating comment failed! ' + data.error);
                location.path('/bug');
            });
        }
    }
]);
