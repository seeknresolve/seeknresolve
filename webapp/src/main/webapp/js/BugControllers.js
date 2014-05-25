var bugControllers = angular.module('bugControllers', []);

bugControllers.controller('BugListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.bugs = [ ];

        $http.get('/bug/all').success(function(data) {
            $scope.bugs = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = 'Can\'t fetch bugs list!';
        });
    }
]);

bugControllers.controller('BugDetailsController', ['$scope', '$http', '$routeParams', '$location',
    function($scope, $http, $routeParams, $location) {
        $scope.tag = null;

        $http.get('/bug/' + $routeParams.tag).success(function(data) {
            $scope.bug = data.object;
        }).error(function(data, status, headers, config) {
            if(data.error) {
                $scope.errorMessage = data.error;
            } else {
                $scope.errorMessage = 'Can\'t fetch bug\'s details!';
            }
        });

        $scope.deleteBug = function(tag) {
            $http.delete('/bug/' + tag).success(function (data, status, headers, config) {
                $location.path('/bug');
                $scope.message = 'Bug ' + tag + ' was deleted';
            }).error(function (data, status, headers, config) {
                $scope.errorMessage = 'Can\'t delete bug!';
            });
        };
    }
]);

bugControllers.controller('BugCreateController', ['$scope', '$http',
    function($scope, $http) {

    }
]);