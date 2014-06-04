var seekNResolve = angular.module('seekNResolve', [
    'ngRoute',
    'bugModule',
    'projectModule',
    'userModule',
    'roleModule',
    'permissionModule',
    'ui.bootstrap',
    'app.services',
    'xeditable'
]);

seekNResolve.run(function(editableOptions) {
    editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
});

seekNResolve.directive('loggedUser', ['userService', function(userService) {
    return {
        restrict: 'E',
        template: "{{user.firstName + ' ' + user.lastName}}",
        link: function(scope, element, attrs) {
            scope.user = null;
            userService.getLoggedUser(function(user) {
                scope.user = user;
            });
        }
    };
}]);

seekNResolve.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/index', {
            templateUrl: 'templates/hello.html'
        }).

        when('/project', {
            templateUrl: 'templates/project/list.html',
            controller: 'ProjectListController'
        }).
        when('/project/:id', {
            templateUrl: 'templates/project/details.html',
            controller: 'ProjectDetailsController'
        }).
        when('/projectCreate', {
            templateUrl: 'templates/project/create.html',
            controller: 'ProjectCreateController'
        }).

        when('/bug', {
            templateUrl: 'templates/bug/list.html',
            controller: 'BugListController'
        }).
        when('/bug/details/:tag', {
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

        when('/user', {
            templateUrl: 'templates/user/list.html',
            controller: 'UserListController'
        }).

        when('/user/create', {
            templateUrl: 'templates/user/create.html',
            controller: 'UserCreateController'
        }).

        when('/user/details/:login', {
            templateUrl: 'templates/user/details.html',
            controller: 'UserDetailsController'
        }).

        when('/role/users', {
            templateUrl: 'templates/role/user_list.html',
            controller: 'UserRoleListController'
        }).

        when('/role/projects', {
            templateUrl: 'templates/role/project_list.html',
            controller: 'ProjectRoleListController'
        }).
        when('/role/:roleName', {
            templateUrl: 'templates/role/details.html',
            controller: 'RoleDetailsController'
        }).

        when('/permission/all', {
            templateUrl: 'templates/permission/list.html',
            controller: 'PermissionListController'
        }).

        when('/permissionCreate', {
            templateUrl: 'templates/permission/create.html',
            controller: 'PermissionCreateController'
        }).

        when('/permission/:permissionName', {
            templateUrl: 'templates/permission/details.html',
            controller: 'PermissionDetailsController'
        }).

        when('/about', {
            templateUrl: 'templates/about.html'
        }).

        otherwise({
            redirectTo: '/index'
        });
}]);
