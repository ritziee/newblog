(function() {
    'use strict';
    angular
        .module('blogApp')
        .factory('Rdr', Rdr);

    Rdr.$inject = ['$resource'];

    function Rdr ($resource) {
        var resourceUrl =  'api/rdrs/:id';

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
