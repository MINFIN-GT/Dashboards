<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
	.table {
		border-style: double; 
		width: 100%; 
		margin: 20px auto;
		border-collapse: collapse;
		border-spacing: 0;
	}
	
	.table>tbody>tr>td{
		padding: 8px;
	    line-height: 1;
	    vertical-align: top;
	    border-top: none;
	}
	
	.table>thead>tr>th{
		border-bottom: double gray;
		vertical-align: middle;
	}
	
	.nota {
	
	}
	
	.div_principal{
		width: 90%;
		font-family: "Times New Roman";
		font-size: 10px;
		margin: 0 auto;
		position: relative;
	}
	
	.div_titulo{
		text-align: center;
		margin-top: 20px;
	}
	
	.tab1{
		margin-left: 10px;
	}
	
	.tab2{
		margin-left: 20px;
	}
	
	.tab3{
		margin-left: 30px;
	}
	
	.tab4{
		margin-left: 40px;
	}
	
	.tab5{
		margin-left: 50px;
	}
	
	.tab6{
		margin-left: 60px;
	}
	
	.negrillas{
		font-weight:bold;
	}
	
	.grid_loading_cuadros{
		position: absolute;
	    top:0;
	    left: 0;
	    right: 0;
	    bottom: 0;
	}
	
	.grid_loading_cuadros .msg {
	  opacity: 1;
	  background-color: rgba(238,238,238,0.85);
	  text-align: center;
	  width: 100%;
	  height: 100%;
	  display: table;
	}
	
	.grid_loading_cuadros .msg span {
	  display: table-cell;
	  vertical-align: middle;
	}
	
	.table>tbody>tr:hover{
		background-color: #ececec;
	}
</style>
<div ng-controller="cuadrosglobalesController as ctrl" class="maincontainer" id="title" class="all_page">
<h4>Cuadros Globales</h4>
<div class="row" style="margin-bottom: 10px;">
</div>
<div class="row">
	<uib-tabset active="active">
		<uib-tab index="2" heading="Clasificación Econ. de los Recursos">
	    	<div class="div_principal">
	    		<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading[2]">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				</div>
	    		<div class="div_titulo">
			    	<div style="font-size: 16px; font-weight: bold;">Cuadro 3</div>
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Clasificación Económica de los Recursos</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
		    	<table st-table="ctrl.recursos" class="table">
					<thead>
					<tr>
						<th style="text-align: center">Descripción</th>
						<th style="text-align: center; width: 160px;border-right: 1px solid gray; border-left: 1px solid gray;">Ejecutado <br/>{{ ctrl.anio-2 }}</th>
						<th style="text-align: center; width: 160px;border-right: 1px solid gray; border-left: 1px solid gray;">Aprobado <br/>{{ ctrl.anio-1 }} (*)</th>
						<th style="text-align: center; width: 160px;">Recomendado <br/>{{ ctrl.anio }}</th>
					</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in ctrl.recursos">
						<td><span class="{{ 'tab'+row.nivel }}"><span class="{{ row.negrillas==1 ? 'negrillas' : '' }}">{{ row.texto }}</span></span></td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.ejecutado_dos_antes, ctrl.viewMillones) }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.aprobado_anterior_mas_amp, ctrl.viewMillones) }}</td>
						<td style="text-align: right;">{{ ctrl.filtroMillones(row.recomendado, ctrl.viewMillones) }}</td>
					</tr>
					</tbody>
				</table>
				<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
			</div>
	    </uib-tab>
	    <uib-tab index="3" heading="Clasificación Econ. del Gasto">
	    	<div class="div_principal">
	    		<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading[3]">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				</div>
	    		<div class="div_titulo">
			    	<div style="font-size: 16px; font-weight: bold;">Cuadro 4</div>
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Clasificación Económica del Gasto</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
		    	<table st-table="ctrl.gastos" class="table">
					<thead>
					<tr>
						<th style="text-align: center">Descripción</th>
						<th style="text-align: center; width: 160px;border-right: 1px solid gray; border-left: 1px solid gray;">Ejecutado <br/>{{ ctrl.anio-2 }}</th>
						<th style="text-align: center; width: 160px;border-right: 1px solid gray; border-left: 1px solid gray;">Aprobado <br/>{{ ctrl.anio-1 }} (*)</th>
						<th style="text-align: center; width: 160px;">Recomendado <br/>{{ ctrl.anio }}</th>
					</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in ctrl.gastos">
						<td><span class="{{ 'tab'+row.nivel }}"><span class="{{ row.negrillas==1 ? 'negrillas' : '' }}">{{ row.texto }}</span></span></td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.ejecutado_dos_antes, ctrl.viewMillones) }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.aprobado_anterior_mas_amp, ctrl.viewMillones) }}</td>
						<td style="text-align: right;">{{ ctrl.filtroMillones(row.recomendado, ctrl.viewMillones) }}</td>
					</tr>
					</tbody>
				</table>
				<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
			</div>
	    </uib-tab>
	    <uib-tab index="4" heading="Institucional">
	    	<div class="div_principal">
	    		<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading[4]">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				</div>
	    		<div class="div_titulo">
			    	<div style="font-size: 16px; font-weight: bold;">Cuadro 5</div>
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto Institucional</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
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
			</div>
	    </uib-tab>
	    <uib-tab index="5" heading="Inst. y Subgrupo de Tipo de Gasto">
	    	<div class="div_principal">
	    		<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading[5]">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				</div>
	    		<div class="div_titulo">
			    	<div style="font-size: 16px; font-weight: bold;">Cuadro 6</div>
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto por Institución y Subgrupo de Tipo de Gasto</div>
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto Recomendado {{ ctrl.anio }}</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
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
			</div>
	    </uib-tab>
	    <uib-tab index="6" heading="Inst. por Tipo Presupuesto y Grupo Gasto" >
	    	<div class="div_principal">
	    		<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading[6]">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				</div>
	    		<div class="div_titulo">
			    	<div style="font-size: 16px; font-weight: bold;">Cuadro 7</div>
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto Recomendado {{ ctrl.anio }}</div>
			    	<div style="font-size: 12px; font-weight: bold;">Institucional por Tipo de Presupuesto y Grupo de Gasto</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
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
			</div>
	    </uib-tab>
	    <uib-tab index="7" heading="Inst. por Finalidad">
	    <div class="div_principal">
	    	<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading[7]">
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
	    </uib-tab>
	    <uib-tab index="8" heading="Finalidad y Económico de G.">
	    	<div class="div_principal">
	    		<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading[8]">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				</div>
	    		<div class="div_titulo">
			    	<div style="font-size: 16px; font-weight: bold;">Cuadro 9</div>
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto Recomendado {{ ctrl.anio }}</div>
			    	<div style="font-size: 12px; font-weight: bold;">Finalidad y Económico de Gasto</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
		    	<table st-table="ctrl.finalidades_economico" class="table">
					<thead>
					<tr>
						<th style="text-align: center">Institución</th>
						<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;">Total</th>
						<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;" 
						ng-repeat="row_e in ctrl.economicos">{{ row_e }}</th>
					</tr>
					</thead>
					<tbody>
					<tr>
						<td style="font-weight: bold; text-align: center;">Total</td>
						<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
						ng-repeat="row in ctrl.total_finalidad_economico track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
					</tr>
					<tr ng-repeat="row in ctrl.finalidades_economico">
						<td style="white-space: nowrap;">{{ row.finalidad_nombre }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.recomendado_total, ctrl.viewMillones) }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
						ng-repeat="n in [].constructor(9) track by $index">{{ ctrl.filtroMillones(row['e'+($index+1)+'_monto'], ctrl.viewMillones) }}</td>
					</tr>
					</tbody>
				</table>
				<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
			</div>
	    </uib-tab>
	    <uib-tab index="9" heading="Inst. Tipo de G. y Región">
	    	<div class="div_principal">
	    		<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading[9]">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				</div>
	    		<div class="div_titulo">
			    	<div style="font-size: 16px; font-weight: bold;">Cuadro 10</div>
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto Recomendado {{ ctrl.anio }}</div>
			    	<div style="font-size: 12px; font-weight: bold;">Por Institución, Tipo de Gasto y Región</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
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
			</div>
	    </uib-tab>
	    <uib-tab index="10" heading="Gasto según Región y Finalidad">
	    	<div class="div_principal">
	    		<div class="grid_loading_cuadros" ng-hide="!ctrl.showloading[10]">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				</div>
	    		<div class="div_titulo">
			    	<div style="font-size: 16px; font-weight: bold;">Cuadro 11</div>
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto Recomendado {{ ctrl.anio }}</div>
			    	<div style="font-size: 12px; font-weight: bold;">Gasto según Región y Finalidad</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
		    	<table st-table="ctrl.finalidades_region" class="table">
					<thead>
					<tr>
						<th style="text-align: center">Institución</th>
						<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;">Total</th>
						<th style="text-align: center; width: 150px;border-right: 1px solid gray; border-left: 1px solid gray;" 
						ng-repeat="row_f in ctrl.regiones">{{ row_f }}</th>
					</tr>
					</thead>
					<tbody>
					<tr>
						<td style="font-weight: bold; text-align: center;">Total</td>
						<td style="font-weight: bold; text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
						ng-repeat="row in ctrl.total_finalidad_region track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
					</tr>
					<tr ng-repeat="row in ctrl.finalidades_region">
						<td style="white-space: nowrap;">{{ row.finalidad_nombre }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;">{{ ctrl.filtroMillones(row.recomendado_total, ctrl.viewMillones) }}</td>
						<td style="text-align: right; border-right: 1px solid gray; border-left: 1px solid gray;"
						ng-repeat="n in [].constructor(11) track by $index">{{ ctrl.filtroMillones(row['r'+($index+1)+'_monto'], ctrl.viewMillones) }}</td>
					</tr>
					</tbody>
				</table>
				<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
			</div>
	    </uib-tab>
	    
  	</uib-tabset>
</div>
<br/>
<br/>
<br/>
</div>