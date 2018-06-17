/**
 * 
 */

var app=angular.module('usersController',['dashboards','smart-table','ngSanitize']);

app.controller('usersController',['$scope','$routeParams','$http','$interval',
	'$location','Utilidades','$window','$uibModal',	   
		function($scope,$routeParams,$http, $interval, $location,$utilidades,$window,$uibModal){
			var me = this;
			me.tabActivo = 0;
			me.displayedCollection=[];
			me.rowCollection=[];
			me.newUser=false;
			me.disabled_buttons=false;
			
			me.user=null;
			
			me.redirectMain = function(){
				redirectMain();
			}
			
			$http.post('/SUser', { action:  'usersList', t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	me.rowCollection = response.data.users;
			    	me.displayedCollection = deepCopy(me.rowCollection);
				}
		 	});
			
			me.clickUser=function(row){
				me.user=row;
				me.newUser=false;
				$http.post('/SUser', { action:  'userPermissions', user: row.username, t: (new Date()).getTime() }).then(function(response){
				    if(response.data.success){
				    	me.permissions = response.data.permissions;
					}
			 	});
			}
			
			me.clickNew=function(){
				me.user={};
				me.newUser=true;
				me.password_confirm=null;
				$http.post('/SUser', { action:  'userPermissions', user: null, t: (new Date()).getTime() }).then(function(response){
				    if(response.data.success){
				    	me.permissions = response.data.permissions;
					}
			 	});
			}
			
			me.clickSave=function(){
				if(me.mainform.$valid){
					if((me.user.password!=null && me.user.password==me.password_confirm) || me.user.password==null){
						me.disabled_buttons=true;
						var permissions_simple='';
						for(var i=0; i<me.permissions.length; i++){
							permissions_simple+= (me.permissions[i].hasPermission) ? ','+me.permissions[i].permissionId : '';
						}
						permissions_simple = (permissions_simple.length>0) ? permissions_simple.substring(1) : '';
						$http.post('/SUser', { action:  'saveUser', user: JSON.stringify(me.user), permissions: permissions_simple, isnew: me.newUser, t: (new Date()).getTime() }).then(function(response){
							if(response.data.success){
								$utilidades.mensaje('success','Usuario '+(me.newUser ? 'creado' : 'actualizado')+' con éxito');
								me.displayedCollection.push(me.user);
								me.rowCollection.push(me.user);
								me.newUser=false;
							}
							else{
								$utilidades.mensaje('warning','Se ha producido un error al actualizar el usuario');
							}
							me.disabled_buttons=false;
						});
					}
					else
						$utilidades.mensaje('warning','Los campos de las contraseñas deben de coincidir');
				}
				else{
					$utilidades.mensaje('warning','Debe ingresar todos los campos obligatorios');
				}
			}
			
			me.clickDelete=function(){
				if(me.user.username!=null){
					me.disabled_buttons=true;
					var deleteUser = $window.confirm('Está seguro que desea borrar al usuario?');
				    if (deleteUser) {
				    	$http.post('/SUser', { action:  'deleteUser', user: me.user.username, t: (new Date()).getTime() }).then(function(response){
							if(response.data.success){
								var index=me.rowCollection.indexOf(me.user);
								if(index>-1)
									me.rowCollection.splice(index,1);
								index = me.displayedCollection.indexOf(me.user);
								if(index>-1)
									me.displayedCollection.splice(index,1);
								me.user={};
								me.newUser=true;
								$utilidades.mensaje('success','El usuario se ha borrado con éxito');
								for(var i=0; i<me.permissions.length; i++)
									me.permissions[i].hasPermission=false;
							}
							else{
								$utilidades.mensaje('warning','Se ha producido un error al borrar al usuario');
							}
							me.disabled_buttons=false;
						});
				    }
				}
				else{
					me.user={};
					me.newUser=false;
				}
			}
			
			me.clickEmail=function(){
				$http.post('/SUser', { action:  'emailUser', user: me.user.username, t: (new Date()).getTime() }).then(function(response){
					if(response.data.success){
						$utilidades.mensaje('success','Se ha enviado el email al usuario');
					}
					else{
						$utilidades.mensaje('warning','Se ha producido un error al enviar el correo al usuario');
					}
				});
			}
			
			me.clickViewEmail=function(){
				$http.post('/SUser', { action:  'viewEmailUser', user: me.user.username, t: (new Date()).getTime() }).then(function(response){
					if(response.data.success){
						me.email_message="";
						me.email_to= me.user.email;
						me.email_hash=response.data.hash;
						var modalInstance = $uibModal.open({
					      animation: true,
					      ariaLabelledBy: 'modal-title',
					      ariaDescribedBy: 'modal-body',
					      templateUrl: 'email.html',
					      controller: 'ModalInstanceCtrl',
					      controllerAs: '$ctrl',
					      size: 'lg',
					      resolve: {
					        to: function(){
					        	return me.email_to;
					        },
					        hash: function(){
					        	return me.email_hash;
					        },
					        firstname: function(){
					        	return me.user.firstname;
					        },
					        lastname: function(){
					        	return me.user.lastname;
					        },
					        username: function(){
					        	return me.user.username;
					        }
					      }
					    });
					}
					else{
						$utilidades.mensaje('warning','Se ha producido un error al obtener la información del correo');
					}
				});
			}
			
		}
	]);

app.controller('ModalInstanceCtrl', ['$scope','$uibModalInstance','$sce', 'to','hash','firstname','lastname','username',
	function ($scope,$uibModalInstance,$sce, to, hash, firstname, lastname, username) {
	  var $ctrl = this;
	  $ctrl.to = to;
	  $ctrl.subject = "Sistema de tableros MINFIN";
	  $ctrl.message = $sce.trustAsHtml("Estimado "+firstname+" "+lastname+": <br/><br/> Es un gusto saludarle y desearle éxitos en sus actividades diarias.<br/>"
		+ "Le damos la bienvenida al uso del sistema de tableros del MINFIN, del cual se describen a continuación los datos para su accesso.<br/><br/>"
		+ "Link: <a href=\"http:\\\\tableros.minfin.gob.gt\\templogin.jsp?user="+username+"&access="+encodeURI(hash)+"\">http://tableros.minfin.gob.gt</a><br/>"
		+ "Usurio: "+username+"<br/><br/>"
		+ "Al ingresar el sistema le pedirá que ingrese una contraseña la cual utilizará para el ingreso al sistema.<br/><br/>"
		+ "Agradeceré cualquier comentario que tenga sobre el sistema para poder incluirlo en mejoras futuras.<br/><br/>"+
		+ "Ing. Rafael Reyes<br/>"
		+ "Ministerio de Finanzas Públicas");
	}]);