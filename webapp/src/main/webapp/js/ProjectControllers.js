var projectControllers = angular.module('projectControllers', []);

projectControllers.controller('ProjectListController',
        ['$scope', '$http',
                function($scope, $http) {
                    $scope.projects = [];

                    $http.get('/project/all').success(function(data) {
                        $scope.projects = data.object;
                    }).error(function(data, status, headers, config) {
                        $scope.errorMessage = "Can't retrieve project list!";
                    });
                }
        ]
);

projectControllers.controller('ProjectDetailsController', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams) {
        $scope.id = null;

        $http.get('/project/' + $routeParams.id).success(function(data) {
            $scope.project = data.object;
        }).error(function(data, status, headers, config) {
            $scope.errorMessage = "Can't retrieve project details!";
        });
    }
]);