var appFilters = angular.module('app.filters', []);

appFilters.filter('capitalizeFilter', function() {
    return function(item) {
        return item.charAt(0) + item.substr(1).toLowerCase();
    };
});