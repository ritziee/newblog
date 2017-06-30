(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('RdrDeleteController',RdrDeleteController);

    RdrDeleteController.$inject = ['$uibModalInstance', 'entity', 'Rdr'];

    function RdrDeleteController($uibModalInstance, entity, Rdr) {
        var vm = this;

        vm.rdr = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Rdr.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
