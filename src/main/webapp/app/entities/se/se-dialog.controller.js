(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('SeDialogController', SeDialogController);

    SeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Se'];

    function SeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Se) {
        var vm = this;

        vm.se = entity;
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
            if (vm.se.id !== null) {
                Se.update(vm.se, onSaveSuccess, onSaveError);
            } else {
                Se.save(vm.se, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blogApp:seUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
