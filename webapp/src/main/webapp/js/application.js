var seekNResolve = angular.module('seekNResolve', ['ngRoute', 'bugControllers']);

seekNResolve.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/index', {
            templateUrl: 'templates/hello.html',
            controller: 'BugController'
        }).
        when('/project', {
            templateUrl: 'templates/project/project.html'
        }).
        when('/bug', {
            templateUrl: 'templates/bug/bug.html'
        }).
        when('/user', {
            templateUrl: 'templates/user/user.html'
        }).
        when('/about', {
            templateUrl: 'templates/about.html'
        }).
        otherwise({
            redirectTo: '/index'
        });
}]);
