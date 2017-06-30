(function() {
    'use strict';
    angular
        .module('blogApp')
        .factory('Store', Store);

    Store.$inject = ['$resource'];

    function Store ($resource) {
        var resourceUrl =  'api/stores/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
