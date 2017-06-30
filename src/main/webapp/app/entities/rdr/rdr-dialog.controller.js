(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('RdrDialogController', RdrDialogController);

    RdrDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Rdr'];

    function RdrDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Rdr) {
        var vm = this;

        vm.rdr = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.rdr.id !== null) {
                Rdr.update(vm.rdr, onSaveSuccess, onSaveError);
            } else {
                Rdr.save(vm.rdr, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blogApp:rdrUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
