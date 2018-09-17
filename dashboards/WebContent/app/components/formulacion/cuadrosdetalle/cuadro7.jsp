<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/assets/css/main_cuadros_detalle.css" />
<div ng-controller="cuadro7detalleController as ctrl" class="maincontainer" id="title" class="all_page">
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
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto Recomendado {{ ctrl.anio }}</div>
			    	<div style="font-size: 12px; font-weight: bold;">Institucional por Tipo de Presupuesto y Grupo de Gasto</div>
			    	<div style="font-size: 12px; font-weight: bold;">(Montos en Millones de Quetzales)</div>
		    	</div>
		    	<div>
		    		<div class="col-sm-12" style="text-align: right;">
						<div class="btn-group">
					        <label class="btn btn-default" ng-model="ctrl.viewMillones" uib-btn-radio="true" uncheckable tooltip-placement="bottom" uib-tooltip="Ver en millones">MQ</label>
					        <label class="btn btn-default" ng-model="ctrl.viewMillones" uib-btn-radio="false" uncheckable tooltip-placement="bottom" uib-tooltip="Ver en quetzales">Q</label>
					    </div>
					</div>
					<div class="row" style="width: 100%;overflow-x: auto;">
						<table st-table="ctrl.entidades_tp_gg" class="table">
							<thead>
								<tr>
									<th style="text-align: center">Descripción</th>
									<th style="text-align: center; width: 150px;">Total</th>
									<th style="text-align: center; width: 150px;" 
									ng-repeat="row_tp in ctrl.grupos_gasto">{{ row_tp }}</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="font-weight: bold; text-align: center;">Total</td>
									<td style="font-weight: bold; text-align: right; "
									ng-repeat="row in ctrl.total_tp_gg track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
								</tr>
								<tr>
									<td style="font-weight: bold;">Funcionamiento</td>
									<td style="font-weight: bold; text-align: right;"
									ng-repeat="row in ctrl.total_tp_gg_funcionamiento track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
								</tr>
								<tr ng-repeat="row in ctrl.entidades_tp_grupos_gasto_funcionamiento">
									<td style="white-space: nowrap;">{{ row.entidad_nombre }}</td>
									<td style="text-align: right; ">{{ ctrl.filtroMillones(row.recomendado_total, ctrl.viewMillones) }}</td>
									<td style="text-align: right; "
									ng-repeat="n in [].constructor(10) track by $index">{{ ctrl.filtroMillones(row['g'+$index+'_monto'], ctrl.viewMillones) }}</td>
								</tr>
								<tr>
									<td style="font-weight: bold;">Inversión</td>
									<td style="font-weight: bold; text-align: right;"
									ng-repeat="row in ctrl.total_tp_gg_inversion track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
								</tr>
								<tr ng-repeat="row in ctrl.entidades_tp_grupos_gasto_inversion">
									<td style="white-space: nowrap;">{{ row.entidad_nombre }}</td>
									<td style="text-align: right; ">{{ ctrl.filtroMillones(row.recomendado_total, ctrl.viewMillones) }}</td>
									<td style="text-align: right; "
									ng-repeat="n in [].constructor(10) track by $index">{{ ctrl.filtroMillones(row['g'+$index+'_monto'], ctrl.viewMillones) }}</td>
								</tr>
								<tr>
									<td style="font-weight: bold;">Deuda Pública</td>
									<td style="font-weight: bold; text-align: right;"
									ng-repeat="row in ctrl.total_tp_gg_deuda track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
								</tr>
							</tbody>
							<tfoot>
								<tr>
									<td style="font-weight: bold; text-align: center;">Total</td>
									<td style="font-weight: bold; text-align: right; "
									ng-repeat="row in ctrl.total_tp_gg track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
								</tr>
							</tfoot>
						</table>
					</div>
		    	</div>
		    	<br/>
				<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
			</div>
</div>
<br/>
<br/>
<br/>
</div>