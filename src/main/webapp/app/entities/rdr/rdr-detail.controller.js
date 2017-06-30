(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('RdrDetailController', RdrDetailController);

    RdrDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Rdr'];

    function RdrDetailController($scope, $rootScope, $stateParams, previousState, entity, Rdr) {
        var vm = this;

        vm.rdr = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('blogApp:rdrUpdate', function(event, result) {
            vm.rdr = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
