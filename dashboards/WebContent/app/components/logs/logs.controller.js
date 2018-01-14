/**
 * 
 */

angular.module('logsController',['dashboards','ui.bootstrap.contextMenu','treeControl','angularResizable']).controller('logsController',['$scope','$routeParams','$http','$interval',
	'$location','$timeout',
	   function($scope,$routeParams,$http, $interval, $location, $timeout){
			
			this.nodo_seleccionado=null;
			this.nodos_expandidos=[];
			this.treedata=[];
			this.content_file=null;
			
			this.tree_options={
				allowDeselect: false
			};
	
			this.showSelected=function(nodo){
				if(this.nodo_seleccionado!=nodo){
					this.content_file="";
					this.nodo_seleccionado = nodo;
					$http.post('SViewLog', {action: 'getContentFile', name: nodo.name }).then(function(response){
						if(response.data.success){
							this.content_file = response.data.content_file.replace('\\n','\\');
						}
					}.bind(this));
				}
			}
	
	
			$http.post('/SViewLog', { action: 'getListFiles' }).then(function(response){
				    if(response.data.success){
				    	this.treedata=response.data.files;
					}
				    
			 	}.bind(this), function errorCallback(response){
			 		
			 	}
			);
			
			
		}
	]);