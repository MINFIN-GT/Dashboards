<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/assets/css/main_cuadros.css" />
<div ng-controller="cuadro1Controller as ctrl" class="maincontainer" id="title" class="all_page">
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
			    	<div style="font-size: 16px; font-weight: bold;">Cuadro 1</div>
			    	<div style="font-size: 12px; font-weight: bold;">Principales Indicadores del Sector Fiscal</div>
		    	</div>
		    	<table class="table">
					<thead>
					<tr>
						<th style="text-align: center"></th>
						<th style="text-align: center; width: 160px;border-right: 1px solid gray; border-left: 1px solid gray;">Ejecutado <br/>{{ ctrl.anio-2 }}</th>
						<th style="text-align: center; width: 160px;border-right: 1px solid gray; border-left: 1px solid gray;">Aprobado <br/>{{ ctrl.anio-1 }} (*)</th>
						<th style="text-align: center; width: 160px;">Recomendado <br/>{{ ctrl.anio }}</th>
					</tr>
					</thead>
				</table>
				<div style="text-align: center; font-weight: bold; font-size:12px;">Indicadores Macroecnómicos en Porcentajes del PIB</div>
				<table st-table="ctrl.lineas" class="table">
					<tbody>
					<tr ng-repeat="row in ctrl.lineas | limitTo: 23: 0">
						<td><span class="{{ 'tab'+row.nivel }}"><span class="{{ row.negrillas==1 ? 'negrillas' : '' }}">{{ row.texto }}</span></span></td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray; width: 160px;">{{ ctrl.filtroPorcentaje(row.ejecutado_dos_antes) }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray; width: 160px;">{{ ctrl.filtroPorcentaje(row.aprobado_anterior_mas_amp) }}</td>
						<td style="text-align: right; width: 160px;">{{ ctrl.filtroPorcentaje(row.recomendado) }}</td>
					</tr>
					</tbody>
				</table>
				<div style="text-align: center; font-weight: bold;font-size:12px;">Indicadores en Porcentajes de los Ingresos Totales</div>
				<table st-table="ctrl.lineas" class="table">
					<tbody>
					<tr ng-repeat="row in ctrl.lineas | limitTo: 4: 23">
						<td><span class="{{ 'tab'+row.nivel }}"><span class="{{ row.negrillas==1 ? 'negrillas' : '' }}">{{ row.texto }}</span></span></td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray; width: 160px;">{{ ctrl.filtroPorcentaje(row.ejecutado_dos_antes) }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray; width: 160px;">{{ ctrl.filtroPorcentaje(row.aprobado_anterior_mas_amp) }}</td>
						<td style="text-align: right; width: 160px;">{{ ctrl.filtroPorcentaje(row.recomendado) }}</td>
					</tr>
					</tbody>
				</table>
				<div style="text-align: center; font-weight: bold;font-size:12px;">Indicadores en Porcentajes del Presupuesto Total</div>
				<table st-table="ctrl.lineas" class="table">
					<tbody>
					<tr ng-repeat="row in ctrl.lineas | limitTo: 3: 27">
						<td><span class="{{ 'tab'+row.nivel }}"><span class="{{ row.negrillas==1 ? 'negrillas' : '' }}">{{ row.texto }}</span></span></td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray; width: 160px;">{{ ctrl.filtroPorcentaje(row.ejecutado_dos_antes) }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray; width: 160px;">{{ ctrl.filtroPorcentaje(row.aprobado_anterior_mas_amp) }}</td>
						<td style="text-align: right; width: 160px;">{{ ctrl.filtroPorcentaje(row.recomendado) }}</td>
					</tr>
					</tbody>
				</table>
				<div>&nbsp;</div>
				<table st-table="ctrl.lineas" class="table">
					<tbody>
						<tr>
							<td><span class="{{ 'tab'+row.nivel }}"><span class="negrillas">Nota:<br/>PIB (millones de Quetzales)</span></span></td>
							<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray; width: 160px;">{{ ctrl.filtroCurrency(ctrl.pibs[0]) }}</td>
							<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray; width: 160px;">{{ ctrl.filtroCurrency(ctrl.pibs[1]) }}</td>
							<td style="text-align: right; width: 160px;">{{ ctrl.filtroCurrency(ctrl.pibs[2]) }}</td>
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