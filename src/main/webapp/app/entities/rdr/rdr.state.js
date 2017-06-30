(function() {
    'use strict';

    angular
        .module('blogApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rdr', {
            parent: 'entity',
            url: '/rdr',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.rdr.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rdr/rdrs.html',
                    controller: 'RdrController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rdr');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('rdr-detail', {
            parent: 'rdr',
            url: '/rdr/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.rdr.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rdr/rdr-detail.html',
                    controller: 'RdrDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rdr');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Rdr', function($stateParams, Rdr) {
                    return Rdr.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rdr',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rdr-detail.edit', {
            parent: 'rdr-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rdr/rdr-dialog.html',
                    controller: 'RdrDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rdr', function(Rdr) {
                            return Rdr.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rdr.new', {
            parent: 'rdr',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rdr/rdr-dialog.html',
                    controller: 'RdrDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rdr', null, { reload: 'rdr' });
                }, function() {
                    $state.go('rdr');
                });
            }]
        })
        .state('rdr.edit', {
            parent: 'rdr',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rdr/rdr-dialog.html',
                    controller: 'RdrDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rdr', function(Rdr) {
                            return Rdr.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rdr', null, { reload: 'rdr' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rdr.delete', {
            parent: 'rdr',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rdr/rdr-delete-dialog.html',
                    controller: 'RdrDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Rdr', function(Rdr) {
                            return Rdr.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rdr', null, { reload: 'rdr' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
