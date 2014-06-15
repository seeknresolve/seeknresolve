var projectModule = angular.module('projectModule', ['app.services', 'highcharts-ng']);

projectModule.controller('ProjectListController', ['$scope', '$http', '$location',
    function(scope, http, location) {
        scope.projects = [];

        http.get('/project/all').success(function(data) {
            scope.projects = data.object;
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Can't retrieve project list!";
        });

        scope.searchProjects = function(query) {
            return http.get('/project/search?query=' + query).then(function(result) {
                return result.data.object;
            });
        };

        scope.selectProject = function(item, model, label) {
            location.path('/project/' + model.id);
        };
    }
]);

projectModule.controller('ProjectUserAssignController', ['$scope', '$modalInstance', '$http', 'projectId',
    function(scope, modalInstance, http, projectId) {
        scope.selected = {
            user: null,
            role: null
        };

        scope.users = [];
        http.get('/user/notAssignedToProject/' + projectId).success(function(data) {
            scope.users = data.object;
            scope.selected.user = scope.users[0];
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Can't retrieve user list!";
        });

        scope.roles = [];
        http.get('/role/projects').success(function(data) {
            scope.roles = data.object;
            scope.selected.role = scope.roles[0];
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Can't retrieve project role list!";
        });

        scope.ok = function () {
            userProjectRoleDto = {
                userId: scope.selected.user.id,
                role: scope.selected.role.roleName,
                projectId: projectId
            };
            modalInstance.close(userProjectRoleDto);
        };

        scope.cancel = function () {
            modalInstance.dismiss('cancel');
        };
    }
]);

function getProjectDetails(scope, http, projectId) {
    http.get('/project/' + projectId).
        success(function(data) {
            scope.project = data.object;
            scope.bugStat.closed = getClosedBugsByDate(scope.project.bugs);
            scope.bugStat.opened = getOpenedBugsByDate(scope.project.bugs);
            scope.chartConfig.series[0].data = scope.bugStat.opened;
            scope.chartConfig.series[1].data = scope.bugStat.closed;
            scope.chartConfig.series[1].data.push(
                [_.last(scope.chartConfig.series[0].data)[0], _.last(scope.chartConfig.series[1].data)[1]]
            );
        }).error(function(data, status, headers, config) {
            if(data.error) {
                scope.errorMessage = data.error;
            } else {
                scope.errorMessage = "Can't retrieve project details!";
            }
        });
}

function getClosedBugsByDate(bugs) {
    result = _.filter(bugs, function(bug) { return _.indexOf(["STOPPED", "CLOSED"], bug.state) != -1 ;});
    return aggregateBugsByDate(result, "dateModified");
}

function getOpenedBugsByDate(bugs) {
    result = _.filter(bugs, function(bug) { return _.indexOf(["IN_PROGRESS", "READY_TO_TEST", "REOPENED" ], bug.state) != -1 ;});
    return aggregateBugsByDate(result, "dateCreated");
}

function aggregateBugsByDate(bugs, dateField) {
    result = _.each(bugs, function(bug) {
        var date = new Date(bug[dateField]);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
        bug[dateField] = date;
    });
    result = _.countBy(result, dateField);
    result = _.pairs(result);
    result = _.sortBy(result, 0);
    for(i = 1; i < result.length; i++) {
        result[i][1] += result[i-1][1];
    }
    return result;
}

function getChartConfig(scope) {
    config = {
        options: {
            chart: {
                type: 'areaspline',
                    zoomType: 'x'
            }
        },
        title: {
            text: 'bugs history'
        },
        loading: false,
        xAxis: {
            type: 'datetime',
            dateTimeLabelFormats: {
                day: '%e of %b',
                month: '%e, %b',
                year: '%b'
            },
            title: {
                text: 'Date'
            }
        },
        yAxis: {
            title: {
                text: 'Number of bugs'
            },
            min: 0
        },
        series: [{
            name: 'Opened bugs',
            data: scope.stats.openedBugs,
            color: '#FF6A48',
            pointInterval: 24 * 3600 * 1000
        }, {
            name: 'Closed bugs',
            data: scope.stats.closedBugs,
            color: '#A7FF4C',
            pointInterval: 24 * 3600 * 1000
        }
]
    };
    return config;
}

projectModule.controller('ProjectDetailsController', ['$scope', '$http', '$routeParams', 'notificationsService', '$modal',
    function(scope, http, routeParams, notificationsService, modal) {
        scope.id = null;
        scope.projectRevisionDiffs = [];
        scope.project = null;
        scope.bugStat = {
            closed: null,
            opened: null
        };
        getProjectDetails(scope, http, routeParams.id);

        http.get('/projectRevision/all/' + routeParams.id).success(function(data) {
            scope.projectRevisionDiffs = data.object;
        }).error(function(data, status, headers, config) {
            scope.errorMessage = "Error getting project history";
        });

        scope.stats = {
            closedBugs: null,
            openedBugs: null
        };

        scope.chartConfig = getChartConfig(scope);

        scope.removeAssignment = function(userId) {
            http.delete('/project/' + scope.project.id + '/revokeRole/user/' + userId).
                success(function (data, status, headers, config) {
                    notificationsService.success("User role have been revoked.");
                    getProjectDetails(scope, http, routeParams.id);
                }).error(function(data, status, headers, config) {
                    scope.errorMessage = "Can't revoke user role.";
                });
        };

        scope.openAssignPopup = function () {
            var modalInstance = modal.open({
                templateUrl: 'projectUserAssignment.html',
                controller: 'ProjectUserAssignController',
                resolve: {
                    projectId: function () {
                        return routeParams.id;
                    }
                }
            });

            modalInstance.result.then(function (userProjectRole) {
                http.post('/project/' + userProjectRole.projectId + '/grantRole/' + userProjectRole.role + '/user/' + userProjectRole.userId).
                    success(function (data, status, headers, config) {
                        notificationsService.success('User has been assigned successfully.');
                        getProjectDetails(scope, http, routeParams.id);
                    }).error(function (data, status, headers, config) {
                    notificationsService.error('Error', 'Assigning user failed! ' + data.error);
                    });

            });
        };
    }
]);

projectModule.controller('ProjectCreateController', ['$scope', '$http', '$location', 'notificationsService',
    function(scope, http, location, notificationsService) {
        scope.createProject = function() {
            var project = scope.project;
            var params = JSON.stringify(project);

            http.post('/project/create', params, {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                notificationsService.success('Success', 'Project ' + data.object.name + ' created successfully');
                location.path('/project');
            }).error(function (data, status, headers, config) {
                notificationsService.error('Error', 'Creating project failed! ' + data.error);
                location.path('/project');
            });
        }
    }
]);