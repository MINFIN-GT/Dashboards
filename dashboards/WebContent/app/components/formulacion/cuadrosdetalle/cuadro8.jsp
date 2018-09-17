<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/assets/css/main_cuadros_detalle.css" />
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
			    	<div style="font-size: 12px; font-weight: bold;">Administración Central</div>
			    	<div style="font-size: 12px; font-weight: bold;">Presupuesto Recomendado {{ ctrl.anio }}</div>
			    	<div style="font-size: 12px; font-weight: bold;">Institucional por Finalidad</div>
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
						<table st-table="ctrl.entidades_finalidades" class="table">
							<thead>
							<tr>
								<th style="text-align: center">Institución</th>
								<th style="text-align: center; width: 150px;">Total</th>
								<th style="text-align: center; width: 150px;" 
									ng-repeat="row_f in ctrl.finalidades">{{ row_f }}</th>
							</tr>
							</thead>
							<tbody>
								<tr>
									<td style="font-weight: bold; text-align: center;">Total</td>
									<td style="font-weight: bold; text-align: right;"
										ng-repeat="row in ctrl.total_finalidad track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
								</tr>
								<tr ng-repeat="row in ctrl.entidades_finalidades track by $index" ng-click="ctrl.clickRow(row,$index)" ng-show="row.show">
									<td style="white-space: nowrap; text-transform: capitalize">
										<span class="{{ 'tab'+row.nivel }}"></span>
											<span style="font-size: 8px; margin-right: 15px;"class="{{ row.nivel<6 ? (row.showChildren==false ? 'glyphicon glyphicon-chevron-down' : 'glyphicon glyphicon-chevron-up') : '' }}"></span>
												{{ row.nivel>1 ? (row.nivel==5 ? (row.codigo+'').padStart(2,'0') : row.codigo) +' - '+row.nombre : row.nombre }}
											<span ng-show="row.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span>
									</td>
									<td style="text-align: right;">{{ ctrl.filtroMillones(row.recomendado_total, ctrl.viewMillones) }}</td>
									<td style="text-align: right;"
										ng-repeat="fs in ctrl.finalidades_codigos">{{ ctrl.filtroMillones(row['f'+fs+'_monto'], ctrl.viewMillones) }}</td>
								</tr>
							</tbody>
							<tfoot>
								<tr>
									<td style="font-weight: bold; text-align: center;">Total</td>
									<td style="font-weight: bold; text-align: right;"
										ng-repeat="row in ctrl.total_finalidad track by $index">{{ ctrl.filtroMillones(row, ctrl.viewMillones) }}</td>
								</tr>
							</tfoot>
						</table>
					</div>			     	
				</div>
				<div class="nota"><span style="font-weight: bold;">Nota:</span> Pueden existir diferencias por redondeo.</div>
			</div>
	    
</div>
<br/>
<br/>
<br/>
</div>