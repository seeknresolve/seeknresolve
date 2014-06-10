var appFilters = angular.module('app.filters', []);

appFilters.filter('capitalizeFilter', function() {
    return function(item) {
        return item.charAt(0) + item.substr(1).toLowerCase();
    };
});

appFilters.filter('bugStateFilter', function() {
    return function(item) {
        switch(item) {
            case 'NEW':
                return 'New';

            case 'REJECTED':
                return 'Rejected';

            case 'IN_PROGRESS':
                return 'In progress';

            case 'READY_TO_TEST':
                return 'Ready to test';

            case 'REOPENED':
                return 'Reopened';

            case 'STOPPED':
                return 'Stopped';

            case 'CLOSED':
                return 'Closed';
                
            default:
                return item;
        }
    };
});