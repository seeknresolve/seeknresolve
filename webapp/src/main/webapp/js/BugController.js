var bugController = angular.module('bugControllers', []);

bugController.controller('BugController', function($scope, $http) {
    $scope.bugs = [ ];
});
