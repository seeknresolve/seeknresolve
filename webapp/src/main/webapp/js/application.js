var seekNResolve = angular.module('seekNResolve', ['ngRoute']);

seekNResolve.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/index', {
            templateUrl: 'templates/hello.html'
        }).
        when('/about', {
            templateUrl: 'templates/about.html'
        }).
        otherwise({
            redirectTo: '/index'
        });
}]);
