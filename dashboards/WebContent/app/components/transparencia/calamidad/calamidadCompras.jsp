<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <div ng-controller="ComprasCtrl as control" class="maincontainer" id="title">
	<h3>Tablero de Seguimiento a Estados de {{ control.tipo }} - {{ control.titulo }}</h3>
	<br/>
	<div class="row">
		<div class="col-sm-12">
			<h4>Compras</h4>
		</div>
	</div>
	
	<div class="row" style="margin: 10px 0px 20px 0px;">
		<uib-tabset active="active">
			<uib-tab index="0" heading="Por Entidad" select="control.view_total=false">
				<table class="table table-striped">
					<thead>
						<tr style="font-weight: bold;">
							<td>Entidad</td>
							<td style="text-align: right;">Eventos Publicados</td>
							<td style="text-align: right;">Adjudicados</td>
							<td style="text-align: right;">Monto Adjudicado</td>
						</tr>
					</thead>
					<tfoot>
						<tr style="font-weight: bold; text-align: right; white-space: nowrap; cursor: pointer;" >
							<td ng-click="control.getComprasList('')">Totales</td>
							<td>{{ control.total_eventos }}</td>
							<td>{{ control.total_adjudicados }}</td>
							<td>Q {{ control.total_monto_adjudicado|number:2 }}</td>
						</tr>
					</tfoot>
					<tbody style="font-size: 11px;">
						<tr ng-repeat="row in control.compras_por_entidad" ng-click="control.getComprasList(row.entidad)" style="cursor: pointer;">
							<td>{{ row.entidad }}</td>
							<td style="text-align: right;">{{ row.num_eventos }}</td>
							<td style="text-align: right;">{{ row.num_adjudicados }}</td>
							<td style="text-align: right;">Q {{ row.total_adjudicado|number:2 }}</td>
						</tr>
					</tbody>
				</table>
				<div style="height: 300px; width: 80%; margin: 40px auto;">
				<canvas class="chart-base" chart-type="control.chartType" chart-data="control.chartData" chart-title="control.chartTitle"
					chart-labels="control.chartLabels" chart-legend="true" chart-series="control.chartSeries" chart-options="control.chartOptions" chart-colors="control.chart_colours">
				</canvas>
				</div>
			</uib-tab>
			<uib-tab index="1" heading="Detalle" select="control.view_total=true">
				<div>
					<table st-table="control.compras" st-safe-src="control.original_compras" class="table table-striped" st-set-filter="myStrictFilter" style="float:left;">
						<thead>
							<tr style="cursor: pointer;">
								<th st-sort="entidad">Entidad</th>
								<th st-sort="unidad">Unidad</th>
								<th st-sort="id">NOG</th>
								<th st-sort="fecha">Publicación</th>
								<th st-sort="descripcion">Descripción</th>
								<th st-sort="modalidad">Modalidad</th>
								<th st-sort="estatus">Estado</th>
								<th st-sort="monto">Monto</th>
							</tr>
							<tr style="font-size: 10px;">
								<th>
									<select mh-search="entidad" ng-model="control.selected_entidad" style="max-width: 150px; color: black;">
					                    <option value="">Todas</option>
					                    <option ng-repeat="row in control.original_compras | unique:'entidad'" value="{{row.entidad}}">{{row.entidad}}</option>
					                </select>
					            </th>
					            <th>
					                <select st-search="unidad" style="max-width: 150px; color: black;">
					                    <option value="">Todas</option>
					                    <option ng-repeat="row in control.original_compras | unique:'unidad'" value="{{row.unidad}}">{{row.unidad}}</option>
					                </select>
					            </th>
								<th><input st-search="id" placeholder="NOG" class="input-sm form-control" type="search" style="font-size:10px; height:18px;" /></th>
								<th></th>
								<th></th>
								<th>
					                <select st-search="modalidad" style="max-width: 150px; color: black;">
					                    <option value="">Todas</option>
					                    <option ng-repeat="row in control.original_compras | unique:'modalidad'" value="{{row.modalidad}}">{{row.modalidad}}</option>
					                </select>
					            </th>
								<th>
					                <select st-search="estado" style="max-width: 150px; color: black;">
					                    <option value="">Todos</option>
					                    <option ng-repeat="row in control.original_compras | unique:'estado'" value="{{row.estado}}">{{row.estado}}</option>
					                </select>
					            </th>
								<th></th>
							</tr>
						</thead>
						<tbody style="font-size: 10px;">
							<tr ng-repeat="row in control.compras">
								<td>{{ row.entidad }}</td>
								<td>{{ row.unidad }}</td>
								<td><a target="_blank" href="{{row.tipo=='NOG' ? 'http://www.guatecompras.gt/concursos/consultaDetalleCon.aspx?nog='+row.id : 'http://www.guatecompras.gt/PubSinConcurso/ConsultaAnexosPubSinConcurso.aspx?op=4&n='+row.id }}">{{ row.id }}</a></td>
								<td style="text-align: right;">{{ row.fecha | date:'d/M/yyyy' }}</td>
								<td>{{ row.descripcion }}</td>
								<td>{{ row.modalidad }}</td>
								<td>{{ row.estado }}</td>
								<td style="min-width: 100px; text-align: right; white-space: nowrap;">Q {{ row.monto|number:2 }}</td>
							</tr>
						</tbody>
					</table>
					<div class="grid_loading" ng-hide="!control.showloading" style="position: relative; width: 100%; height: 100%;">
			 			<div class="msg">
			     			<span><i class="fa fa-spinner fa-spin fa-4x"></i>
				  			<br /><br />
				  			<b>Cargando, por favor espere...</b>
			  				</span>
						</div>
					</div>
				</div>
			</uib-tab>
		</uib-tabset>
	</div>
	<div style="text-align: right;" ng-show="control.view_total">Total de {{ control.compras.length }} eventos en Guatecompras</div><br/><br/>
	</div>

