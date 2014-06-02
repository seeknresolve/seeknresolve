var bugModule = angular.module('bugModule', ['app.services', 'app.filters', 'xeditable']);

bugModule.directive('userDisplay', function() {
    return {
        restrict: 'E',
        scope: true,
        //TODO: this link should be optional
        template: '<a href="#/user/{{userLogin}}">{{userName}}</a>',
        link: function (scope, element, attrs) {
            scope.userLogin = attrs.userLogin;
            if(attrs.userLogin == null || attrs.userLogin == '') {
                scope.userName = 'None';
            } else {
                scope.userName = attrs.userName;
                scope.linkOpen = '<a href="#/user/{{attrs.userLogin}}">';
                scope.linkClose = '</a>';
            }
        }
    };
});

bugModule.directive('bugPriority', function() {
    return {
        restrict: 'E',
        scope: true,
        template: '<span class="label {{class}}">{{priority | capitalizeFilter}}</span>',
        link: function (scope, element, attrs) {
            scope.priority = attrs.priority;
            switch (scope.priority) {
                case "LOW":
                    scope.class = 'label-default';
                    break;

                case "NORMAL":
                    scope.class = 'label-info';
                    break;

                case "HIGH":
                    scope.class = 'label-warning';
                    break;

                case "CRITICAL":
                    scope.class = 'label-danger';
                    break;

                default:
                    scope.class = 'label-primary';
                    break;
            }
        }
    }
});

bugModule.controller('BugListController', ['$scope', '$http', 'notificationsService',
    function(scope, http, notificationsService) {
        scope.bugs = [ ];

        http.get('/bug/all').success(function(data) {
            scope.bugs = data.object;
        }).error(function(data, status, headers, config) {
            notificationsService.error('Error', 'Can\'t fetch bugs list!');
        });
    }
]);

bugModule.controller('BugDetailsController', ['$scope', '$http', '$route', '$routeParams', '$location', 'notificationsService',
    function(scope, http, route, routeParams, location, notificationsService) {
        scope.tag = null;

        http.get('/bug/' + routeParams.tag).success(function(data) {
            scope.bug = data.object;
        }).error(function(data, status, headers, config) {
            if(data.error) {
                notificationsService.error('Error', 'Can\'t fetch bug\'s details! ' + data.error);
            } else {
                notificationsService.error('Error', 'Can\'t fetch bug\'s details!');
            }
            location.path('/bug');
        });

        scope.deleteBug = function(tag) {
            http.delete('/bug/' + tag).success(function (data, status, headers, config) {
                notificationsService.warning('Delete', 'Bug ' + tag + ' was deleted');
                location.path('/bug');
            }).error(function (data, status, headers, config) {
                scope.errorMessage = 'Can\'t delete bug!';
            });
        };

        scope.createComment = function() {
            var comment = scope.comment;
            comment.bugTag = scope.bug.tag;
            var params = JSON.stringify(comment);

            http.post('/comment/create', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'Comment created successfully');
                route.reload();
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Creating comment failed! ' + data.error);
                location.path('/bug');
            });
        };

        scope.updateBug = function() {
            alert("aaa");
        };
    }
]);

bugModule.controller('BugCreateController', ['$scope', '$http', '$location', 'notificationsService', 'userService', 'projectService',
    function(scope, http, location, notificationsService, userService, projectService) {
        scope.loggedUser = null;
        userService.getLoggedUser(function (user) {
            scope.loggedUser = user;
        });

        scope.priorities = [
            'LOW',
            'NORMAL',
            'HIGH',
            'CRITICAL'
        ];
        scope.priority = scope.priorities[1];

        scope.projectUsers = [];
        scope.projects = [];
        scope.project = null;
        projectService.getAllPermittedProjects(function (projects) {
            scope.projects = projects;
            scope.project = projects[0];
        });
        scope.$watch('project', function() {
            if (scope.project) {
                projectService.getUsersInProject(scope.project.id, function (users) {
                    scope.projectUsers = users;
                });
            }
        });

        scope.createBug = function() {
            var bug = scope.bug;
            bug.priority = scope.priority;
            bug.projectId = scope.project.id;
            bug.reporterId = scope.loggedUser.id;
            if(scope.assignee) {
                bug.assigneeId = scope.assignee.id;
            }
            var params = JSON.stringify(bug);

            http.post('/bug', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'Bug ' + data.object.tag + ' reported successfully');
                location.path('/bug');
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Creating bug failed! ' + data.error);
                location.path('/bug');
            });
        }
    }
]);