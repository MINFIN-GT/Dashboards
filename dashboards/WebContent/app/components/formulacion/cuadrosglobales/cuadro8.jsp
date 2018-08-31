<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/assets/css/main_cuadros.css" />
<div ng-controller="cuadro8Controller as ctrl" class="maincontainer" id="title" class="all_page">
<h4>Cuadros Globales</h4>
<div class="row" style="margin-bottom: 10px;">
</div>
<div class="row">
	
	    <div class="div_principal">
	    	<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				</div>
	    		<div class="div_titulo">
			    	<div style="font-size: 16px; font-weight: bold;">Cuadro 8</div>
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto Recomendado {{ ctrl.anio }}</div>
			    	<div style="font-size: 12px; font-weight: bold;">Institucional por Finalidad</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
		     	<table st-table="ctrl.entidades_finalidades" class="table">
					<thead>
					<tr>
						<th style="text-align: center">Institución</th>
						<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;">Total</th>
						<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;" 
						ng-repeat="row_f in ctrl.finalidades">{{ row_f }}</th>
					</tr>
					</thead>
					<tbody>
					<tr>
						<td style="font-weight: bold; text-align: center;">Total</td>
						<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
						ng-repeat="row in ctrl.total_finalidad track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
					</tr>
					<tr ng-repeat="row in ctrl.entidades_finalidades">
						<td style="white-space: nowrap;">{{ row.entidad_nombre }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.recomendado_total, ctrl.viewMillones) }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
						ng-repeat="fs in ctrl.finalidades_codigos">{{ ctrl.filtroMillones(row['f'+fs+'_monto'], ctrl.viewMillones) }}</td>
					</tr>
					</tbody>
				</table>
				<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
			</div>
	    
</div>
<br/>
<br/>
<br/>
</div>