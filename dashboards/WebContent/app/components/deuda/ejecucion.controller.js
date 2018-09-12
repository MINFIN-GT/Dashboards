/**
 * 
 */

var app=angular.module('ejecuciondeudaController',['dashboards','smart-table','ngSanitize']);

app.controller('ejecuciondeudaController',['$scope','$routeParams','$http','$interval',
	'$location','Utilidades','$window','$filter',	   
		function($scope,$routeParams,$http, $interval, $location,$utilidades,$window,$filter){
			var me = this;
			
			me.displayedCollection=[];
			me.rowCollection=[];
			
			me.viewQuetzales=true;
			me.viewVigentes=true;
			me.showloading=false;
			
			me.total_vigentes=[0,0,0,0,0,0,0,0,0,0,0,0];
			me.total_ejecuciones=[0,0,0,0,0,0,0,0,0,0,0,0];
			me.total_asignado=0;
			
			me.anos_historia = [];
			var ano_actual = moment().year();
			for(var i=ano_actual; i>2010; i--){
				me.anos_historia.push(i);
			}
			
			me.ano = ano_actual;
			
			me.filtroQuetzales=function(value, transform){
				if(transform){
					return $filter('currency')(value, 'Q ', 2);
				}
				else 
					return value;
			}
			
			me.anoClick=function(ejercicio){
				me.showloading=true;
				me.ano = ejercicio;
				$http.post('/SDeuda', { action:  'getEjecucionTotal', ejercicio: ejercicio, t: (new Date()).getTime() }).then(function(response){
				    if(response.data.success){
				    	me.displayedCollection = convertTreeToList(response.data.arbol);
				    	for(var i=0; i<me.displayedCollection.length; i++)
				    		me.displayedCollection[i].visible = true;
				    	me.rowCollection = me.displayedCollection;
				    	for(var i=0; i<response.data.arbol.length; i++){
				    		me.total_asignado += response.data.arbol[i].asignado;
				    		for(var j=0; j<12; j++){
				    			me.total_vigentes[j]+=response.data.arbol[i].vigente_meses[j];
				    			me.total_ejecuciones[j]+=response.data.arbol[i].ejecucion_meses[j];
				    		}
				    	}
					}
				    me.showloading=false;
			 	});
			}
			
			me.clickRow=function(row){
				if(row.children!=null){
					row.showChildren = (row.showChildren==null) ? false : !row.showChildren;
					me.showHideRow(row.children,row.showChildren);
				}
			}
			
			me.showHideRow=function(children,show){
				for(var i=0; i<children.length; i++){
					if(children[i].children!=null){
						children[i].showChildren=(!show) ? false : children[i].showChildren;
						me.showHideRow(children[i].children,children[i].showChildren);
					}
					children[i].visible = show;
				}
			}
			
			
			me.anoClick(me.ano);
			
		}
	]);