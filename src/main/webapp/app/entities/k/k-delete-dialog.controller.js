(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('KDeleteController',KDeleteController);

    KDeleteController.$inject = ['$uibModalInstance', 'entity', 'K'];

    function KDeleteController($uibModalInstance, entity, K) {
        var vm = this;

        vm.k = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            K.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
