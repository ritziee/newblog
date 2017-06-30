(function() {
    'use strict';
    angular
        .module('blogApp')
        .factory('Se', Se);

    Se.$inject = ['$resource'];

    function Se ($resource) {
        var resourceUrl =  'api/ses/:id';

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
