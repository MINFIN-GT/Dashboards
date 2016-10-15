angular.module('prestamosEjecucionPresupuestariaModule',['dashboards','smart-table'])
.controller('prestamosController',['$scope','$routeParams','$http','$document','$timeout','uiGmapGoogleMapApi','$log',
	   function($scope,$routeParams,$http, $document, $timeout, uiGmapGoogleMapApi, $log){			
			this.lastupdate = '';
			
			this.map = {
				center: {
					latitude: 15.035009229644448,
					longitude: -90.7093818359375
				},
				zoom: 8	
			}
			
			$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.lastupdate = response.data.lastupdate;
				}
			}.bind(this)
			);
			
			var current_year = moment().year();		
			this.prestamo_ejecucion_financiera=0;
			this.prestamo_vigente=0;
			this.prestamo_asignado=0;
			this.prestamo_modificaciones=0;
			this.prestamo_ejecutado=0;
			this.prestamo_level = 1;
			this.prestamo_nombre="";
			this.prestamo_sigla="";
			this.prestamo_entidad;
			this.prestamo_entidad_nombre="";
			this.prestamo_unidad_ejecutora_nombre="";
			this.prestamo_unidad_ejecutora=null;
			this.prestamo_programa_nombre="";
			this.prestamo_programa=null;
			this.prestamo_subprograma_nombre="";
			this.prestamo_subprograma=null;
			this.prestamo_proyecto_nombre="";
			this.prestamo_proyecto=null;
			this.prestamo_actividad_nombre="";
			this.prestamo_actividad=null;
			this.prestamo_renglon_nombre="";
			this.prestamo_renglon=null;
			
			this.entidad_ejecucion_financiera=0;
			this.entidad_asignado=0;
			this.entidad_modificaciones=0;
			this.entidad_vigente=0;
			this.entidad_ejecutado=0;
			this.entidad_level = 1;
			this.entidad_prestamo_nombre="";
			this.entidad_prestamo_sigla="";
			this.entidad=null;
			this.entidad_nombre="";
			this.entidad_unidad_ejecutora_nombre="";
			this.entidad_unidad_ejecutora=null;
			this.entidad_programa_nombre="";
			this.entidad_programa=null;
			this.entidad_subprograma_nombre="";
			this.entidad_subprograma=null;
			this.entidad_proyecto_nombre="";
			this.entidad_proyecto=null;
			this.entidad_actividad_nombre="";
			this.entidad_actividad=null;
			this.entidad_renglon_nombre="";
			this.entidad_renglon=null;
						
			this.entidad_ejecucion_data=[];
			this.entidad_ejecucion_data_original=[];
			this.entidad_ejecucion_totales=[0.0, 0.0, 0.0, 0.0, 0.0];
			
			this.prestamo_ejecucion_data=[];
			this.prestamo_ejecucion_data_original=[];
			this.prestamo_ejecucion_totales=[0.0, 0.0, 0.0, 0.0, 0.0];
			
						
			this.entidad_clickRow = function(codigo, nombre){
				switch(this.entidad_level){
					case 1: this.entidad = codigo; this.entidad_nombre = nombre;
							this.entidad_prestamo_sigla=null; this.entidad_unidad_ejecutora=null; this.entidad_programa=null; this.entidad_subprograma=null; this.entidad_proyecto = null; this.entidad_actividad=null; this.entidad_renglon=null;
							break;
					case 2:	this.entidad_prestamo_sigla = codigo; this.entidad_prestamo_nombre = nombre;
							this.entidad_unidad_ejecutora=null; this.entidad_programa=null; this.entidad_subprograma=null; this.entidad_proyecto = null; this.entidad_actividad=null; this.entidad_renglon=null;
							break;
					case 3: this.entidad_unidad_ejecutora = codigo; this.entidad_unidad_ejecutora_nombre = nombre; 
							this.entidad_programa=null; this.entidad_subprograma=null; this.entidad_proyecto = null; this.entidad_actividad=null; this.entidad_renglon=null;
							break;
					case 4: this.entidad_programa = codigo; this.entidad_programa_nombre = nombre; 
							this.entidad_subprograma=null; this.entidad_proyecto = null; this.entidad_actividad=null; this.entidad_renglon=null;
							break;		
					case 5: this.entidad_subprograma = codigo; this.entidad_subprograma_nombre = nombre; 
							this.entidad_proyecto = null; this.entidad_actividad=null; this.entidad_renglon=null;
							break;
					case 6: this.entidad_proyecto=codigo; this.entidad_proyecto_nombre = nombre;
							this.entidad_actividad=null;this.entidad_renglon=null;
							break;
					case 7: this.entidad_actividad=codigo; this.entidad_actividad_nombre = nombre; 
							this.entidad_renglon=null;
							break;	
					case 8: this.entidad_renglon=codigo; this.entidad_renglon_nombre=nombre;
							break;
				}
				this.entidad_level = this.entidad_level<8 ? this.entidad_level+1 : this.entidad_level;
				this.entidad_goLevel(this.entidad_level);
			};
			
			this.entidad_goLevel=function(entidad_level){			
				this.entidad_level = entidad_level;
				switch(this.entidad_level){
					case 1: this.entidad=null; this.entidad_prestamo_sigla=null; this.entidad_unidad_ejecutora=null; this.entidad_programa=null; this.entidad_subprograma=null; this.entidad_proyecto=null; this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 2: this.entidad_prestamo_sigla=null; this.entidad_unidad_ejecutora=null; this.entidad_programa=null; this.entidad_subprograma=null; this.entidad_proyecto=null; this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 3: this.entidad_unidad_ejecutora=null; this.entidad_programa=null; this.entidad_subprograma=null; this.entidad_proyecto=null; this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 4: this.entidad_programa=null; this.entidad_subprograma=null; this.entidad_proyecto=null; this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 5: this.entidad_subprograma=null; this.entidad_proyecto=null; this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 6: this.entidad_proyecto=null; this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 7: this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 8: this.entidad_renglon=null;
				}
				var data = { tipo: "entidad", level:this.entidad_level, ejercicio:current_year, entidad: this.entidad, prestamo:this.entidad_prestamo_sigla, unidad_ejecutora: this.entidad_unidad_ejecutora,
							programa:this.entidad_programa, subprograma:this.entidad_subprograma, proyecto:this.entidad_proyecto, actividad:this.entidad_actividad, renglon:this.entidad_renglon};				
    			this.entidad_showloading = true;
				$http.post('/SPrestamos', data).then(function(response){
	    			if(response.data.success){
	    				this.entidad_ejecucion_data_original = response.data.datos;
	    		
	    		this.entidad_ejecucion_data = this.entidad_ejecucion_data_original.length > 0 ? this.entidad_ejecucion_data_original : [];
	    				this.entidad_ejecucion_totales[0] = 0;
    					this.entidad_ejecucion_totales[1] = 0;
    					this.entidad_ejecucion_totales[2] = 0;
    					this.entidad_ejecucion_totales[3] = 0;
    					this.entidad_ejecucion_totales[4] = 0;

    					for (i=0; i<this.entidad_ejecucion_data.length; i++){
	    					this.entidad_ejecucion_totales[0] += this.entidad_ejecucion_data[i].asignado;
	    					this.entidad_ejecucion_totales[2] += this.entidad_ejecucion_data[i].vigente;
	    					this.entidad_ejecucion_totales[3] += this.entidad_ejecucion_data[i].ejecutado;
	    				}
    					this.entidad_ejecucion_totales[1] = this.entidad_ejecucion_totales[2]-this.entidad_ejecucion_totales[0];
	    				this.entidad_ejecucion_totales[4] = this.entidad_ejecucion_totales[2]>0?(this.entidad_ejecucion_totales[3]/this.entidad_ejecucion_totales[2]*100):0.0;

	    				if (this.entidad_level==1){
	    					this.entidad_asignado=this.entidad_ejecucion_totales[0];
	    					this.entidad_modificaciones=this.entidad_ejecucion_totales[1];
	    					this.entidad_vigente=this.entidad_ejecucion_totales[2];
	    					this.entidad_ejecutado=this.entidad_ejecucion_totales[3];
	    					this.entidad_ejecucion_financiera = this.entidad_ejecucion_totales[4];
	    				}
	    			}		
	    			this.entidad_showloading = false;		
	    		}.bind(this), function errorCallback(response){	 		
			 	});		
			};
			
			this.prestamo_clickRow = function(codigo, nombre){
				switch(this.prestamo_level){
					case 1: this.prestamo_sigla = codigo; this.prestamo_nombre = nombre;
							this.prestamo_entidad=null; this.prestamo_unidad_ejecutora=null; this.prestamo_programa=null; this.prestamo_subprograma=null; this.prestamo_proyecto = null; this.prestamo_actividad=null; this.prestamo_renglon=null;
							break;
					case 2:	this.prestamo_entidad = codigo; this.prestamo_entidad_nombre = nombre;
							this.prestamo_unidad_ejecutora=null; this.prestamo_programa=null; this.prestamo_subprograma=null; this.prestamo_proyecto = null; this.prestamo_actividad=null; this.prestamo_renglon=null;
							break;
					case 3: this.prestamo_unidad_ejecutora = codigo; this.prestamo_unidad_ejecutora_nombre = nombre; 
							this.prestamo_programa=null; this.prestamo_subprograma=null; this.prestamo_proyecto = null; this.prestamo_actividad=null; this.prestamo_renglon=null;
							break;
					case 4: this.prestamo_programa = codigo; this.prestamo_programa_nombre = nombre; 
							this.prestamo_subprograma=null; this.prestamo_proyecto = null; this.prestamo_actividad=null; this.prestamo_renglon=null;
							break;		
					case 5: this.prestamo_subprograma = codigo; this.prestamo_subprograma_nombre = nombre; 
							this.prestamo_proyecto = null; this.prestamo_actividad=null; this.prestamo_renglon=null;
							break;
					case 6: this.prestamo_proyecto=codigo; this.prestamo_proyecto_nombre = nombre;
							this.prestamo_actividad=null;this.prestamo_renglon=null;
							break;
					case 7: this.prestamo_actividad=codigo; this.prestamo_actividad_nombre = nombre; 
							this.prestamo_renglon=null;
							break;	
					case 8: this.prestamo_renglon=codigo; this.prestamo_renglon_nombre=nombre;
							break;
				}
				this.prestamo_level = this.prestamo_level<8 ? this.prestamo_level+1 : this.prestamo_level;
				this.prestamo_goLevel(this.prestamo_level);
			};
			
			this.prestamo_goLevel=function(prestamo_level){			
				this.prestamo_level = prestamo_level;
				switch(this.prestamo_level){
					case 1: this.prestamo_sigla=null; this.prestamo_entidad=null; this.prestamo_unidad_ejecutora=null; this.prestamo_programa=null; this.prestamo_subprograma=null; this.prestamo_proyecto=null; this.prestamo_actividad=null; this.prestamo_renglon=null; break;
					case 2: this.prestamo_entidad=null; this.prestamo_unidad_ejecutora=null; this.prestamo_programa=null; this.prestamo_subprograma=null; this.prestamo_proyecto=null; this.prestamo_actividad=null; this.prestamo_renglon=null; break;
					case 3: this.prestamo_unidad_ejecutora=null; this.prestamo_programa=null; this.prestamo_subprograma=null; this.prestamo_proyecto=null; this.prestamo_actividad=null; this.prestamo_renglon=null; break;
					case 4: this.prestamo_programa=null; this.prestamo_subprograma=null; this.prestamo_proyecto=null; this.prestamo_actividad=null; this.prestamo_renglon=null; break;
					case 5: this.prestamo_subprograma=null; this.prestamo_proyecto=null; this.prestamo_actividad=null; this.prestamo_renglon=null; break;
					case 6: this.prestamo_proyecto=null; this.prestamo_actividad=null; this.prestamo_renglon=null; break;
					case 7: this.prestamo_actividad=null; this.prestamo_renglon=null; break;
					case 8: this.prestamo_renglon=null;
				}
				var data = { tipo: "prestamo", level:this.prestamo_level, ejercicio:current_year, prestamo: this.prestamo_sigla, entidad:this.prestamo_entidad, unidad_ejecutora: this.prestamo_unidad_ejecutora,
							programa:this.prestamo_programa, subprograma:this.prestamo_subprograma, proyecto:this.prestamo_proyecto, actividad:this.prestamo_actividad, renglon:this.prestamo_renglon};				
    			this.prestamo_showloading = true;
				$http.post('/SPrestamos', data).then(function(response){
	    			if(response.data.success){
	    				this.prestamo_ejecucion_data_original = response.data.datos;
	    		
	    		this.prestamo_ejecucion_data = this.prestamo_ejecucion_data_original.length > 0 ? this.prestamo_ejecucion_data_original : [];
	    				this.prestamo_ejecucion_totales[0] = 0;
    					this.prestamo_ejecucion_totales[1] = 0;
    					this.prestamo_ejecucion_totales[2] = 0;
    					this.prestamo_ejecucion_totales[3] = 0;
    					this.prestamo_ejecucion_totales[4] = 0;

    					for (i=0; i<this.prestamo_ejecucion_data.length; i++){
	    					this.prestamo_ejecucion_totales[0] += this.prestamo_ejecucion_data[i].asignado;
	    					this.prestamo_ejecucion_totales[2] += this.prestamo_ejecucion_data[i].vigente;
	    					this.prestamo_ejecucion_totales[3] += this.prestamo_ejecucion_data[i].ejecutado;
	    				}
    					this.prestamo_ejecucion_totales[1] = this.prestamo_ejecucion_totales[2]-this.prestamo_ejecucion_totales[0];
	    				this.prestamo_ejecucion_totales[4] = this.prestamo_ejecucion_totales[2]>0?(this.prestamo_ejecucion_totales[3]/this.prestamo_ejecucion_totales[2]*100):0.0;

	    				if (this.prestamo_level==1){
	    					this.prestamo_asignado=this.prestamo_ejecucion_totales[0];
	    					this.prestamo_modificaciones=this.prestamo_ejecucion_totales[1];
	    					this.prestamo_vigente=this.prestamo_ejecucion_totales[2];
	    					this.prestamo_ejecutado=this.prestamo_ejecucion_totales[3];
	    					this.prestamo_ejecucion_financiera = this.prestamo_ejecucion_totales[4];
	    				}
	    			}		
	    			this.prestamo_showloading = false;		
	    		}.bind(this), function errorCallback(response){	 		
			 	});		
			};
		
		this.prestamo_goLevel(1);
		this.entidad_goLevel(1);
		
			this.map = {
					center: {
						latitude: 15.035009229644448,
						longitude: -90.7093818359375
					},
					zoom: 7
				};
			
			this.polygons = [];
						
			for(var muniID in municipios){
				var muni = municipios[muniID];  
				for(var i = 0; i < muni.length; i++){			
					this.polygons.push(  	
			        {
			          id: muni[i].propiedad,
			          path: muni[i].coordenadas,
			          stroke: {
			            color: '#6060FB',
			            weight: 1
			          },
			          editable: false,
			          draggable: false,
			          geodesic: false,
			          visible: true,
			          fill: {
			            color: '#ff0000',
			            opacity: 0.8
			          },
			          events:{
					  	click: function(){
						  	var prop = this.events.data;
						  	
						  	$log.info(prop);      
						},
						data: muni[i].propiedad
		        	  },
			        });
			      
			    }
			}
					
	}
]);
