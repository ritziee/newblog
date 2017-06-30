(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('KDetailController', KDetailController);

    KDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'K'];

    function KDetailController($scope, $rootScope, $stateParams, previousState, entity, K) {
        var vm = this;

        vm.k = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('blogApp:kUpdate', function(event, result) {
            vm.k = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
