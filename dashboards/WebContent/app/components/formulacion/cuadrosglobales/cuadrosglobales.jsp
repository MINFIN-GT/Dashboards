<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
	.table {
		border-style: double; 
		width: 90%; 
		margin: 20px auto;
		border-collapse: collapse;
		border-spacing: 0;
		font-family: "Times New Roman";
		font-size: 10px;
	}
	
	.table>tbody>tr>td{
		padding: 8px;
	    line-height: 1.42857143;
	    vertical-align: top;
	    border-top: none;
	}
	
	.table>thead>tr>th{
		border-bottom: double gray;
		vertical-align: middle;
	}
	
	.nota {
		font-family: "Times New Roman";
		font-size: 10px;
		width: 90%; 
		margin: 0 auto;
	}
	
</style>
<div ng-controller="cuadrosglobalesController as ctrl" class="maincontainer" id="title" class="all_page">
<h4>Cuadros Globales</h4>
<div class="row" style="margin-bottom: 10px;">
</div>
<div class="row">
	<uib-tabset active="active">
	    <uib-tab index="0" heading="Institucional">
	    	<table st-table="ctrl.entidades" class="table">
				<thead>
				<tr>
					<th style="text-align: center">Institución</th>
					<th style="text-align: center; width: 160px;border-right: 1px solid gray; border-left: 1px solid gray;">Ejecutado <br/>{{ ctrl.anio-2 }}</th>
					<th style="text-align: center; width: 160px;border-right: 1px solid gray; border-left: 1px solid gray;">Aprobado <br/>{{ ctrl.anio-1 }} (*)</th>
					<th style="text-align: center; width: 160px;">Recomendado <br/>{{ ctrl.anio }}</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td style="font-weight: bold; text-align: center;">Total</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(ctrl.total_ejecutado_dos_antes, ctrl.viewMillones) }}</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(ctrl.total_aprobado_anterior_mas_ampliaciones, ctrl.viewMillones) }}</td>
					<td style="font-weight: bold; text-align: right;">{{ ctrl.filtroMillones(ctrl.total_recomendado, ctrl.viewMillones) }}</td>
				</tr>
				<tr ng-repeat="row in ctrl.entidades">
					<td>{{ row.entidad_nombre }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.ejecutado_dos_antes, ctrl.viewMillones) }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.aprobado_anterior_mas_ampliaciones, ctrl.viewMillones) }}</td>
					<td style="text-align: right;">{{ ctrl.filtroMillones(row.recomendado, ctrl.viewMillones) }}</td>
				</tr>
				</tbody>
			</table>
			<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
	    </uib-tab>
	    <uib-tab index="1" heading="Inst. por Tipo de Presupuesto">
	     	<table st-table="ctrl.entidades_tipo_gasto" class="table">
				<thead>
				<tr>
					<th style="text-align: center">Institución</th>
					<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;">Total</th>
					<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;" 
					ng-repeat="row_tp in ctrl.tipos_gasto">{{ row_tp }}</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td style="font-weight: bold; text-align: center;">Total</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="row in ctrl.total_tp">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
				</tr>
				<tr ng-repeat="row in ctrl.entidades_tipo_gasto">
					<td style="white-space: nowrap;">{{ row.entidad_nombre }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.recomendado, ctrl.viewMillones) }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="tps in ctrl.tipos_gasto_codigos">{{ ctrl.filtroMillones(row['tp'+tps+'_monto'], ctrl.viewMillones) }}</td>
				</tr>
				</tbody>
			</table>
			<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
	    </uib-tab>
	    <uib-tab index="2" heading="Inst. Tipo Presupuesto y Grupo Gasto" >
	       <table st-table="ctrl.entidades_tp_gg" class="table">
				<thead>
				<tr>
					<th style="text-align: center">Descripción</th>
					<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;">Total</th>
					<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;" 
					ng-repeat="row_tp in ctrl.grupos_gasto">{{ row_tp }}</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td style="font-weight: bold; text-align: center;">Total</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="row in ctrl.total_tp_gg track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
				</tr>
				<tr>
					<td style="font-weight: bold;">Funcionamiento</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="row in ctrl.total_tp_gg_funcionamiento track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
				</tr>
				<tr ng-repeat="row in ctrl.entidades_tp_grupos_gasto_funcionamiento">
					<td style="white-space: nowrap;">{{ row.entidad_nombre }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.recomendado_total, ctrl.viewMillones) }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="n in [].constructor(10) track by $index">{{ ctrl.filtroMillones(row['g'+$index+'_monto'], ctrl.viewMillones) }}</td>
				</tr>
				<tr>
					<td style="font-weight: bold;">Inversión</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="row in ctrl.total_tp_gg_inversion track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
				</tr>
				<tr ng-repeat="row in ctrl.entidades_tp_grupos_gasto_inversion">
					<td style="white-space: nowrap;">{{ row.entidad_nombre }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.recomendado_total, ctrl.viewMillones) }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="n in [].constructor(10) track by $index">{{ ctrl.filtroMillones(row['g'+$index+'_monto'], ctrl.viewMillones) }}</td>
				</tr>
				<tr>
					<td style="font-weight: bold;">Deuda Pública</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="row in ctrl.total_tp_gg_deuda track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
				</tr>
				</tbody>
			</table>
			<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
	    </uib-tab>
	    <uib-tab index="3" heading="Inst. por Finalidad">
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
	    </uib-tab>
	    <uib-tab index="4" heading="Inst. por Región">
	    	<table st-table="ctrl.entidades_tp_r" class="table">
				<thead>
				<tr>
					<th style="text-align: center">Descripción</th>
					<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;">Total</th>
					<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;" 
					ng-repeat="row_r in ctrl.regiones">{{ row_r }}</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td style="font-weight: bold; text-align: center;">Total</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="row in ctrl.total_tp_r track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
				</tr>
				<tr>
					<td style="font-weight: bold;">Funcionamiento</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="row in ctrl.total_tp_r_funcionamiento track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
				</tr>
				<tr ng-repeat="row in ctrl.entidades_tp_r_funcionamiento">
					<td style="white-space: nowrap;">{{ row.entidad_nombre }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.recomendado_total, ctrl.viewMillones) }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="n in [].constructor(11) track by $index">{{ ctrl.filtroMillones(row['r'+($index+1)+'_monto'], ctrl.viewMillones) }}</td>
				</tr>
				<tr>
					<td style="font-weight: bold;">Inversión</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="row in ctrl.total_tp_r_inversion track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
				</tr>
				<tr ng-repeat="row in ctrl.entidades_tp_r_inversion">
					<td style="white-space: nowrap;">{{ row.entidad_nombre }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.recomendado_total, ctrl.viewMillones) }}</td>
					<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="n in [].constructor(11) track by $index">{{ ctrl.filtroMillones(row['r'+($index+1)+'_monto'], ctrl.viewMillones) }}</td>
				</tr>
				<tr>
					<td style="font-weight: bold;">Deuda Pública</td>
					<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
					ng-repeat="row in ctrl.total_tp_r_deuda track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
				</tr>
				</tbody>
			</table>
			<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
	    </uib-tab>
  	</uib-tabset>
</div>
<br/>
<br/>
<br/>
</div>