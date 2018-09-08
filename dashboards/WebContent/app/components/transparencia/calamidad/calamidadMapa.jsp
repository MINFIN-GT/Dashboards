<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <div ng-controller="calamidadMapaController as control" class="maincontainer" id="title">
	<h3>Tablero de Seguimiento a Estados de {{ control.tipo }} - {{ control.titulo}}</h3>
	<br/>
	<script type="text/ng-template" id="geograficosDetalles.html">
        <div class="modal-header">
            <h4 class="modal-title" id="modal-title">Municipios</h4>
        </div>
        <div class="modal-body" id="modal-body">
			<h5>Presupuesto en Programa 94</h5>
            <table class="table table-striped">
					<thead>
						<tr style="font-weight: bold;">
							<td>Municipio</td>
							<td style="text-align: right;">Vigente</td>
							<td style="text-align: right;">Comprometido</td>
							<td style="text-align: right;">Ejecutado</td>
						</tr>
					</thead>
					<tfoot>
						<tr style="font-weight: bold; text-align: right; white-space: nowrap; cursor: pointer;" >
							<td>Totales</td>
							<td style="text-align: right;">Q {{ $ctrl.total_94_vigente|number:2 }}</td>
							<td style="text-align: right;">Q {{ $ctrl.total_94_compromiso|number:2 }}</td>
							<td style="text-align: right;">Q {{ $ctrl.total_94_ejecutado|number:2 }}</td>
						</tr>
					</tfoot>
					<tbody style="font-size: 11px;">
						<tr ng-repeat="row in $ctrl.geograficos_94">
							<td>{{ row.nombre }}</td>
							<td style="text-align: right;">Q {{ row.vigente|number:2 }}</td>
							<td style="text-align: right;">Q {{ row.compromiso|number:2 }}</td>
							<td style="text-align: right;">Q {{ row.ejecutado|number:2 }}</td>
						</tr>
					</tbody>
				</table>
			<br/>
			<h5>Presupuesto ejecutado en otros programas</h5>
            <table class="table table-striped">
					<thead>
						<tr style="font-weight: bold;">
							<td>Municipio</td>
							<td style="text-align: right;">Comprometido</td>
							<td style="text-align: right;">Ejecutado</td>
						</tr>
					</thead>
					<tfoot>
						<tr style="font-weight: bold; text-align: right; white-space: nowrap; cursor: pointer;" >
							<td>Totales</td>
							<td style="text-align: right;">Q {{ $ctrl.total_otros_compromiso|number:2 }}</td>
							<td style="text-align: right;">Q {{ $ctrl.total_otros_ejecutado|number:2 }}</td>
						</tr>
					</tfoot>
					<tbody style="font-size: 11px;">
						<tr ng-repeat="row in $ctrl.geograficos_otros">
							<td>{{ row.nombre }}</td>
							<td style="text-align: right;">Q {{ row.compromiso|number:2 }}</td>
							<td style="text-align: right;">Q {{ row.ejecutado|number:2 }}</td>
						</tr>
					</tbody>
				</table>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Cerrar</button>
        </div>
    </script>
    <script type="text/ng-template" id="geograficoDetalles.html">
        <div class="modal-header">
            <h4 class="modal-title">{{ $ctrl.datos.municipio }}, {{ $ctrl.datos.departamento }}</h3>
        </div>
        <div class="modal-body">
            <table align="center" width="90%">
				<tr style="font-weight: bold;">
					<td width="25%"></td>
					<td width="25%" style="text-align: right;">Vigente</td>
					<td width="25%" style="text-align: right;">Comprometido</td>
					<td width="25%" style="text-align: right;">Ejecutado</td>
				</tr>
				<tr>
					<td>Programa 94</td>
					<td style="text-align: right;">Q {{ $ctrl.datos.vigente_94|number:2 }}</td>
					<td style="text-align: right;">Q {{ $ctrl.datos.comprometido_94|number:2 }}</td>
					<td style="text-align: right;">Q {{ $ctrl.datos.ejecutado_94|number:2 }}</td>
				</tr>
			</table>
			<br/>
			<table align="center" width="90%">
				<tr style="font-weight: bold;">
					<td width="25%"></td>
					<td width="25%"></td>
					<td width="25%" style="text-align: right;">Comprometido</td>
					<td width="25%" style="text-align: right;">Ejecutado</td>
				</tr>
				<tr>
					<td>Otros Programas</td>
					<td></td>
					<td style="text-align: right;">Q {{ $ctrl.datos.comprometido_otros|number:2 }}</td>
					<td style="text-align: right;">Q {{ $ctrl.datos.ejecutado_otros|number:2 }}</td>
				</tr>
			</table>
        </div>
		<div class="modal-footer">
            <button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">Cerrar</button>
        </div>
    </script>
	<div class="row">
		<div class="col-sm-12">
			<h4>Mapa de Actividades</h4>
		</div>
	</div>
	<div class="row panel panel-default" style="margin: 10px 0px 20px 0px; height: 500px; position: relative;">
		<ng-map zoom="{{ control.map_options.zoom }}" styles="{{ control.map_options.styles }}" map-type-id="ROADMAP" style="height: 100%;">
			<marker ng-if="actividad.latitude!=null && control.show_marks" ng-repeat="actividad in control.actividades" id="{{ actividad.id }}" position="{{actividad.latitude}},{{actividad.longitude}}"
				on-click="control.map_options.map.showInfoWindow(event, 'i{{ actividad.id }}')"></marker>
			<info-window ng-if="actividad.latitude!=null && control.show_marks" ng-repeat="actividad in control.actividades" id="i{{ actividad.id }}">
				<div ng-non-bindable>
		          <div id="bodyContent">
		            {{ actividad.nombre }}
		          </div>
		        </div>
			</info-window>
			<fusion-tables-layer ng-if="control.geograficos_loaded" 
				query="{
			        select: 'geometry',
			        from: '1N_9GQ5_zJYwZGmzW2UMDfKAjGc6F4QVct1E7qgny',
			        where: 'Codigo IN ( {{ control.geograficos_list_all }} ) ' }"
			    styles="[{
				        where: 'Codigo IN ( {{ control.geograficos_otros_list }} ) ',
				      	polygonOptions: {
				          fillColor: '#bf9000',
				          fillOpacity: 0.75
				        }
			        },{
			    	where: 'Codigo IN ( {{ control.geograficos_94_list }} ) ',
			        polygonOptions: {
			          fillColor: '#38761d',
			          fillOpacity: 0.75
			        }
			      }
			      ]"
			      options="{suppressInfoWindows : true}">
		      </fusion-tables-layer>
		      <custom-control id="show_hide_marks" position="TOP_CENTER" index="1">
			      <div class="panel" style="margin-top: 10px;">
			      <div class="btn-group" role="group" aria-label="...">
					  <button type="button" class="btn btn-default btn-xs" ng-model="control.show_marks" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false"
				      ng-change="control.showHideMarks()">Mostrar Actividades</button>
					  <button type="button" class="btn btn-default btn-xs" ng-click="control.openGeograficosDetalle()">Ver detalle</button>
					</div>
				  </div>
			  </custom-control>
			  <shape id="circle" name="circle" centered="true"
			      stroke-color='#FF0000' stroke-opacity="0.8" stroke-weight="1" fill-color='#FF0000' fill-opacity="1"
			      center="[14.4747103,-90.9156536]" radius="3000" ></shape>
		</ng-map>
	</div>
	<div style="text-align: center;">
		<table style="font-size: 10px;">
			<tr>
				<td style="padding: 10px;"><span class="glyphicon glyphicon-stop" style="color: #FF0000;"></span> Ubicación Volcán de Fuego</td>
				<td style="padding: 10px;"><span class="glyphicon glyphicon-stop" style="color: #38761D;"></span> Ejecución en programa 94 y Subprograma {{ control.subprograma }}</td>
				<td style="padding: 10px;"><span class="glyphicon glyphicon-stop" style="color: #BF9000;"></span> Ejecución en otros programas</td>
			</tr>
		</table>
	</div>
	<div class="row">
		<div class="col-sm-12">
			<div style="text-align: center;">Última actualización: {{ control.lastupdate }}</div>
		</div>		
	</div>
</div>

