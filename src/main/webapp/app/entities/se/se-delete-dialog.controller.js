(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('SeDeleteController',SeDeleteController);

    SeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Se'];

    function SeDeleteController($uibModalInstance, entity, Se) {
        var vm = this;

        vm.se = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Se.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
