var bugControllers = angular.module('bugControllers', []);

bugControllers.controller('BugListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.bugs = [ ];

        $http.get('/bug/all').success(function(data) {
            $scope.bugs = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve bug list!";
        });
    }
]);

bugControllers.controller('BugDetailsController', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams) {
        $scope.tag = null;

        $http.get('/bug/' + $routeParams.tag).success(function(data) {
            $scope.bug = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve bug details!";
        });
    }
]);

bugControllers.controller('BugCreateController', ['$scope', '$http',
    function($scope, $http) {

    }
]);