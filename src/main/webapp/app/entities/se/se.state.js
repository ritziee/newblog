(function() {
    'use strict';

    angular
        .module('blogApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('se', {
            parent: 'entity',
            url: '/se',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.se.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/se/ses.html',
                    controller: 'SeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('se');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('se-detail', {
            parent: 'se',
            url: '/se/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.se.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/se/se-detail.html',
                    controller: 'SeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('se');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Se', function($stateParams, Se) {
                    return Se.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'se',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('se-detail.edit', {
            parent: 'se-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/se/se-dialog.html',
                    controller: 'SeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Se', function(Se) {
                            return Se.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('se.new', {
            parent: 'se',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/se/se-dialog.html',
                    controller: 'SeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                invent: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('se', null, { reload: 'se' });
                }, function() {
                    $state.go('se');
                });
            }]
        })
        .state('se.edit', {
            parent: 'se',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/se/se-dialog.html',
                    controller: 'SeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Se', function(Se) {
                            return Se.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('se', null, { reload: 'se' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('se.delete', {
            parent: 'se',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/se/se-delete-dialog.html',
                    controller: 'SeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Se', function(Se) {
                            return Se.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('se', null, { reload: 'se' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
