var seekNResolve = angular.module('seekNResolve', [
    'ngRoute',
    'bugControllers',
    'projectControllers',
    'userControllers'
]);

seekNResolve.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/index', {
            templateUrl: 'templates/hello.html'
        }).
        when('/project', {
            templateUrl: 'templates/project/list.html',
            controller: 'ProjectListController'
        }).
        when('/bug', {
            templateUrl: 'templates/bug/list.html',
            controller: 'BugListController'
        }).
        when('/bug/:tag', {
            templateUrl: 'templates/bug/details.html',
            controller: 'BugDetailsController'
        }).
        when('/bugCreate', {
            templateUrl: 'templates/bug/create.html',
            controller: 'BugCreateController'
        }).
        when('/user', {
            templateUrl: 'templates/user/list.html',
            controller: 'UserListController'
        }).
        when('/about', {
            templateUrl: 'templates/about.html'
        }).
        otherwise({
            redirectTo: '/index'
        });
}]);
