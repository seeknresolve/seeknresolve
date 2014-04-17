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
bugControllers.controller('ProjectCreateController', ['$scope', '$http',
    function($scope, $http) {
        $http({
            url:'/project/create',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data: {name: $scope.name, description: $scope.description}
        });
    }
]);
