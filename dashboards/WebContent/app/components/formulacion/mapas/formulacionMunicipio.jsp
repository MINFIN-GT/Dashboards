<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>    
    .activo{
    	border: none; 
    	width: 200px; 
    	height: 40px; 
    	color: #000000; 
    	font-size: 24px; 
    	display: block; 
    	font-weight: 200; 
    	padding-top: 3px; 
    	background-color: #BDBDBD;
    }
    ng-map {width:100%; height:550px;}
    .divTablaMapa{
    	max-height: 600px;
    }
    .divTablaDatos{
    	max-height: 550px;
    }
</style>
<link rel="stylesheet" type="text/css" href="/assets/css/main_mapa_detalle.css" />
<div ng-controller="maparecomendadoMunicipioController as control" class="maincontainer" id="gastodepartamentomap" class="all_page"  id="title">
	<div class="row">
		<div class="div_principal">
		    <h4>Recomendado por Municipio</h4>
			<br/>
			<div class="row">
				<div class="col-sm-6">
					<div class="divTablaMapa">
						<ng-map zoom="{{ control.map_options.zoom }}" styles="{{ control.map_options.styles }}" map-type-id="ROADMAP" disable-default-u-i="true" draggable="false"
							center="{{ control.map_options.center }}" map-initialized="control.mapLoaded(map)" id="Municipios">
							<custom-control id="show_scale" position="BOTTOM_LEFT" index="1" style="z-index: 0; position: absolute; left: 30px; bottom: 0px;">
							      <div class="panel ng-scope" style="margin-top: 5px;">
							      	<table style="padding: 5px;">
							      		<tbody><tr>
							      			<td style="padding-left: 5px;">{{control.centros[control.departamentoid-1].split(',')[3]}} % - {{control.centros[control.departamentoid-1].split(',')[4]}} %</td><td style="padding: 3px 5px 0px 10px;"><i class="fas fa-circle" style="color: #d6f5d6; font-family: FontAwesome"></i></td>
							      		</tr>
							      		<tr>
							      			<td style="padding-left: 5px;">{{control.centros[control.departamentoid-1].split(',')[4]}} % - {{control.centros[control.departamentoid-1].split(',')[5]}} %</td><td style="padding: 3px 5px 0px 10px;"><i class="fas fa-circle" style="color: #adebad; font-family: FontAwesome"></i></td>
							      		</tr>
							      		<tr>
							      			<td style="padding-left: 5px;">{{control.centros[control.departamentoid-1].split(',')[5]}} % - {{control.centros[control.departamentoid-1].split(',')[6]}} %</td><td style="padding: 3px 5px 0px 10px;"><i class="fas fa-circle" style="color: #85e085; font-family: FontAwesome"></i></td>
							      		</tr>
							      		<tr>
							      			<td style="padding-left: 5px;">{{control.centros[control.departamentoid-1].split(',')[6]}} % - {{control.centros[control.departamentoid-1].split(',')[7]}} %</td><td style="padding: 3px 5px 0px 10px;"><i class="fas fa-circle" style="color: #5cd65c; font-family: FontAwesome"></i></td>
							      		</tr>
							      		<tr>
							      			<td style="padding-left: 5px;">Mayor a {{control.centros[control.departamentoid-1].split(',')[7]}} %</td><td style="padding: 3px 5px 0px 10px;"><i class="fas fa-circle" style="color: #47d147; font-family: FontAwesome"></i></td>
							      		</tr>
							      	</tbody></table>
								  </div>
							  </custom-control>
						</ng-map>
					</div>					
				</div>
				<div class="col-sm-6">
					<div class="divTablaDatos" style="overflow-y: auto;">
						<div class="div_titulo">
					    	<div style="font-size: 12px; font-weight: bold;">Presupuesto por Municipio</div>
					    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
				    	</div>
				    	<div class="row">
							<div class="col-sm-12" style="text-align: left;">
								<div class="btn-group">
							        <label class="btn btn-default" ng-model="control.viewMillones" uib-btn-radio="true" uncheckable tooltip-placement="bottom" uib-tooltip="Ver en millones">MQ</label>
							        <label class="btn btn-default" ng-model="control.viewMillones" uib-btn-radio="false" uncheckable tooltip-placement="bottom" uib-tooltip="Ver en quetzales">Q</label>
							    </div>
							</div>
						</div>
						<table st-table="control.municipios" class="table">
							<thead>
							<tr>
								<th style="text-align: center;text-transform: capitalize;">Municipios de {{control.nombreDepartamento}}</th>
								<th style="text-align: center; width: 160px;">Recomendado <br/>{{ control.anio }}</th>
							</tr>
							</thead>
							<tbody>
								<tr>
									<td style="font-weight: bold; text-align: center;">Total</td>						
									<td style="font-weight: bold;">{{ control.filtroMillones(control.total_recomendado, control.viewMillones) }}</td>
								</tr>
								<tr ng-repeat="row in control.municipios track by $index" ng-click="control.clickRow(row,$index)" ng-show="row.show">
									<td style="white-space: nowrap; text-transform: capitalize">
										<span class="{{ 'tab'+row.nivel }}"></span>
											<span style="font-size: 8px; margin-right: 15px;"class="{{ row.nivel<8 ? (row.showChildren==false ? 'glyphicon glyphicon-chevron-down' : 'glyphicon glyphicon-chevron-up') : '' }}"></span>
												{{ row.nivel>3 ? (row.nivel==8 ? (row.codigo+'').padStart(2,'0') : row.codigo) +' - '+row.nombre : row.nombre }}
											<span ng-show="row.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span>
									</td>
									<td>{{ control.filtroMillones(row.recomendado, control.viewMillones) }}</td>
								</tr>
							</tbody>
							<tfoot>
								<tr>
									<td style="font-weight: bold; text-align: center;">Total</td>							
									<td style="font-weight: bold;">{{ control.filtroMillones(control.total_recomendado, control.viewMillones) }}</td>
								</tr>
							</tfoot>
						</table>
					</div>							
				</div>
			</div>
			<div class="grid_loading_chart" ng-hide="!ctrl.showChartLoading">
			  	<div class="msg">
			      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
					  <br /><br />
					  <b>Cargando, por favor espere...</b>
				  </span>
				</div>
			</div>
			<div>			
				<div class="chart-title" ng-click="control.returnByChartTitle()">{{ control.chartTitle }}</div>
				<div id="chart_div" style="margin-top: 10px;"></div>
				<div class="chart-return">Para regresar a un nivel anterior presione el titulo de la gráfica</div>
			</div>
		</div>
	</div>		
</div>