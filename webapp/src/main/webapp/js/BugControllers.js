var bugControllers = angular.module('bugControllers', []);

bugControllers.controller('BugListController', ['$scope', '$http',
    function($scope, $http) {
        $scope.bugs = [ ];

        $http.get('/bug/all').success(function(data) {
            $scope.bugs = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve bug list!";
        });
}]);

bugControllers.controller('BugDetailsController', ['$scope', '$routeParams',
    function($scope, $routeParams) {
        $scope.bugTag = $routeParams.tag;
}]);
