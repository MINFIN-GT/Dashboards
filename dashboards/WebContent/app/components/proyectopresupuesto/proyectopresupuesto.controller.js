/**
 * 
 */

angular.module('proyectoPresupuestoController',['dashboards','ui.bootstrap.contextMenu','rzModule']).controller('proyectoPresupuestoController',['$scope','$routeParams','$http','$interval',
	'$location','$timeout',
	   function($scope,$routeParams,$http, $interval, $location,  $timeout){
			
			this.ano = '';
			this.toolbar = document.getElementById('barra_herramientas');
			this.header=[];
			this.contents=[];
			this.footer=[];
			this.editor_activo=null;
			
			this.first_click = false;
			
			this.slider = {
					  minValue: 2.5,
					  maxValue: 19.1,
					  id: 'hruler',
					  options: {
					    floor: 0,
					    ceil: 21.6,
					    step: 0.1,
					    precision: 1,
					    showTicks: true,
					    hideLimitLabels: true,
					    onEnd: function(id,value_min,value_max) {
					    	this.editor_activo.document.$.body.style.marginLeft = (value_min*10)+'mm';
					    	this.editor_activo.document.$.body.style.marginRight = (216-(value_max*10))+'mm';
					    	this.editor_activo.focus();
				        }.bind(this),
				        translate: function(value,id,label) {
				        	if(label=='high')
				        		return (21.6-value).toFixed(1);
				        	else
				        		return value;
				        }
					  }
					};
			
			this.slider_v = {
					  minValue: 2.5,
					  maxValue: 30.5,
					  id: 'vruler',
					  options: {
						vertical: true,  	
						floor: 0,
					    ceil: 33.0,
					    step: 0.1,
					    precision: 1,
					    showTicks: true,
					    hideLimitLabels: true,
					    onEnd: function(id,value_min,value_max) {
					    	this.editor_activo.document.$.body.style.marginTop = (330-(value_min*10))+'mm';
					    	this.editor_activo.document.$.body.style.marginBottom = (330-(value_max*10))+'mm';
					    	this.editor_activo.focus();
				        }.bind(this),
				        translate: function(value,id,label) {
				        	switch(label){
				        		case 'floor': return 33.0; break;
				        		case 'ceil': return 0; break;
				        		case 'high': return (33.0-value).toFixed(1); break;
				        		default: return value;
				        	}
				        }
					  }
					};
			
			this.total_instances=3;
			this.instances=1;
			
			$scope.editorOptions = {
					sharedSpaces: {top: this.toolbar },
					height: '100%',
					toolbar: [
						{ name: 'document', items: [ 'Preview', 'Print'] },
						{ name: 'clipboard', items: [ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
						{ name: 'editing', items: [ 'Find', 'Replace', '-', 'SelectAll' ] },
						{ name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'CopyFormatting', 'RemoveFormat' ] },
						{ name: 'paragraph', items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote',  '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', 'lineheight' ] },
						{ name: 'links', items: [ 'Link', 'Unlink' ] },
						{ name: 'insert', items: [ 'Image', 'Table', 'HorizontalRule', 'SpecialChar'] },
						'/',
						{ name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize' ] },
						{ name: 'colors', items: [ 'TextColor', 'BGColor' ] }
					],
					language: 'es-mx',
					extraPlugins: 'sharedspace,lineheight,panelbutton',
					removePlugins: 'maximize,resize,elementspath',
					line_height: "1.00;1.15;1.50;2.00;2.50;3.00",
					tabSpaces: 5
					
			};
			
			
			this.anoClick = function(ano){
				this.ano=ano;
				alert('Aqui muestra el proyecto del año '+ano);
			}
			
			$scope.$on('ckeditor.ready',function(evt){
				if(this.instances==this.total_instances){
					for(var i=1; i<=this.total_instances/3; i++){
						CKEDITOR.instances['h'+i].document.$.body.style.marginLeft='25mm';
						CKEDITOR.instances['h'+i].document.$.body.style.marginRight='25mm';
						CKEDITOR.instances['b'+i].document.$.body.style.marginLeft='25mm';
						CKEDITOR.instances['b'+i].document.$.body.style.marginRight='25mm';
						CKEDITOR.instances['f'+i].document.$.body.style.marginLeft='25mm';
						CKEDITOR.instances['f'+i].document.$.body.style.marginRight='25mm';
						
						CKEDITOR.instances['h'+i].on('focus',function(e){
							if(this.first_click){
								this.first_click = false;
								this.editor_activo = e.editor;
							}
							else{
								e.cancel();
								CKEDITOR.instances['b'+e.editor.name.substring(1)].focus();
								this.first_click = true;
							}
							$scope.disable_firstclick();
						}.bind(this));
						
						CKEDITOR.instances['b'+i].on('focus',function(e){
							this.editor_activo = e.editor;
						}.bind(this));
						
						CKEDITOR.instances['f'+i].on('focus',function(e){
							if(this.first_click){
								this.first_click = false;
								this.editor_activo = e.editor;
							}
							else{
								CKEDITOR.instances['b'+e.editor.name.substring(1)].focus();
								this.first_click = true;
								e.cancel();
							}
							$scope.disable_firstclick();
						}.bind(this));
						
					}
					CKEDITOR.instances['b1'].focus();
					this.editor_activo=CKEDITOR.instances['b1'];
				}
				else
					this.instances++;
			}.bind(this));
			
			$scope.disable_firstclick = function(){
				$timeout(function(){
					this.first_click = false;
				}.bind(this),200)
			}.bind(this);
			
			
		}
	]);