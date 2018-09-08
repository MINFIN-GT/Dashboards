<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
	.table_historicos{
		width:90%;
		overflow-x: auto; 
		margin: 0 auto;
		display: block;
	}
	
	.table_historicos td {
		padding: 3px;
		border-bottom: 1px solid #c3c3c3;
		border-right: 1px solid #c3c3c3;
		white-space: nowrap; 
	}
	
	.table_pronosticos{
		width:90%;
		overflow-x: auto; 
		margin: 0 auto;
		display: block;
	}
	
	.table_pronosticos td {
		color: #FF0000;
		padding: 3px;
		white-space: nowrap; 
		border-bottom: 1px solid #c3c3c3;
		border-right: 1px solid #c3c3c3;
	}
	
	.glyphicon {
	    position: relative;
	    top: 1px;
	    display: inline-block;
	    font-family: 'Glyphicons Halflings';
	    font-style: normal;
	    font-size: 10px;
	    line-height: 1;
	    -webkit-font-smoothing: antialiased;
	    -moz-osx-font-smoothing: grayscale;
	}

</style>    
    
<div ng-controller="egresosController as egreso" class="maincontainer" id="title" class="all_page">
<script type="text/ng-template" id="treeGrid.html">
	<div class="table-responsive">
   <table class="table tree-grid" style="white-space: nowrap;">
   <thead>
     <tr>
       <th><a ng-if="expandingProperty.sortable" ng-click="sortBy(expandingProperty)">{{expandingProperty.displayName || expandingProperty.field || expandingProperty}}</a><span ng-if="!expandingProperty.sortable">{{expandingProperty.displayName || expandingProperty.field || expandingProperty}}</span><i ng-if="expandingProperty.sorted" class="{{expandingProperty.sortingIcon}} pull-right"></i></th>
       <th ng-repeat="col in colDefinitions"><a ng-if="col.sortable" ng-click="sortBy(col)">{{col.displayName || col.field}}</a><span ng-if="!col.sortable">{{col.displayName || col.field}}</span><i ng-if="col.sorted" class="{{col.sortingIcon}} pull-right"></i></th>
     </tr>
   </thead>
   <tbody>
     <tr ng-repeat="row in tree_rows | searchFor:$parent.filterString:expandingProperty:colDefinitions:true track by row.branch.uid"
       ng-class="'level-' + {{ row.level }} + (row.branch.selected ? ' active':'')" class="tree-grid-row">
       <td style="width: 10px;"><a ng-click="user_clicks_branch(row.branch)"><i ng-class="row.tree_icon"
              ng-click="row.branch.expanded = !row.branch.expanded"
              class="indented tree-icon"></i></a><span class="indented tree-label" ng-click="on_user_click(row.branch)">
             {{row.branch[expandingProperty.field] || row.branch[expandingProperty]}}</span>
       </td>
       <td ng-repeat="col in colDefinitions track by $index" style="{{ $index!=1 ? 'text-align: right; width:10px;' : ''}}">
         <div ng-if="col.cellTemplate" compile="col.cellTemplate" cell-template-scope="col.cellTemplateScope"></div>
         <div ng-if="!col.cellTemplate">{{$index>1 ? row.branch[col.field][$index-2] : row.branch[col.field]}}</div>
       </td>
     </tr>
   </tbody>
 </table>
</div>
</script>
<h4>Egresos</h4>
<h5>Pronósticos</h5>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12" style="padding-left: 0px;">
		<div class="btn-group" uib-dropdown>
	      <button id="single-button" type="button" class="btn btn-default no-border" ng-disabled="egreso.showloading"  uib-dropdown-toggle style="width: 150px; text-align: left; font-size: 24px;">
	        {{ egreso.nmes }} <span class="caret"></span>
	      </button>
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
	        <li role="menuitem"><a href ng-click="egreso.mesClick(1, true)">Enero</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(2, true)">Febrero</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(3, true)">Marzo</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(4, true)">Abril</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(5, true)">Mayo</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(6, true)">Junio</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(7, true)">Julio</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(8, true)">Agosto</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(9, true)">Septiembre</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(10, true)">Octubre</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(11, true)">Noviembre</a></li>
	        <li role="menuitem"><a href ng-click="egreso.mesClick(12, true)">Diciembre</a></li>
	      </ul>
	    </div>
	    <div class="btn-group" uib-dropdown>
	      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="egreso.showloading" style="width: 100px; text-align: left; font-size: 24px;">
	        {{ egreso.anio }} <span class="caret"></span>
	      </button>
	      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
	        <li role="menuitem"><a href ng-click="egreso.anoClick(2017)">2017</a></li>
	        <li role="menuitem"><a href ng-click="egreso.anoClick(2016)">2018</a></li>
	      </ul>
	    </div>
	    <span ng-show="egreso.showloading">&nbsp;<i class="fa fa-spinner fa-spin fa-lg"></i></span> 
    </div>
</div>
<br/>
<div class="row" style="margin-bottom: 10px;">
	<div class="col-sm-12">Número de meses a proyectar:
		<input type="number" ng-model="egreso.numero_pronosticos" min="1" max="24" style="text-align: right;" ng-disabled="egreso.showloading"/>
	</div>
</div>
<br/>
		<div class="row" style="margin-bottom: 10px; margin-top:15px;">
			<div class="col-sm-12">
				<div>Entidad</div>
				<div angucomplete-alt id="entidad"
		              placeholder="Busqueda de entidades"
		              pause="100"
		              selected-object="egreso.cambioEntidad"
		              local-data="egreso.entidades"
		              search-fields="nombre_control"
		              title-field="nombre_control"
		              description-field=""
		              minlength="1"
		              input-class="input-angucomplete"
		              match-class="highlight"
		              focus-out="egreso.blurEntidad()"
		              inputname="entidad"
		              disable-input="egreso.showloading"></div>
		    </div>
		    <div class="col-sm-12" style="margin-top: -20px;">
		    	<div>Unidad Ejecutora</div>
				<div angucomplete-alt id="unidad_ejecutora"
		              placeholder="Busqueda de unidades ejecutoras"
		              pause="100"
		              selected-object="egreso.cambioUnidadEjecutora"
		              local-data="egreso.unidades_ejecutoras"
		              search-fields="nombre_control"
		              title-field="nombre_control"
		              description-field=""
		              minlength="1"
		              input-class="input-angucomplete"
		              match-class="highlight"
		              focus-out="egreso.blurUnidadEjecutora()"
		              inputname="auxiliar" 
		              disable-input="egreso.entidad==null || egreso.showloading"></div>
		    </div>
		</div>
<uib-tabset active="1">
	<uib-tab index="1" heading="Gráfica">
		<div style="margin-bottom: 10px; margin-top: 20px; text-align: center;" ng-show="!egreso.sindatos">
			<div style="width: 800px; height: 400px; margin: 0 auto;">
				<canvas class="chart-base" chart-type="egreso.chartType" chart-data="egreso.chartData"
							chart-labels="egreso.chartLabels" chart-series="egreso.chartSeries" chart-options="egreso.chartOptions" chart-colors="egreso.chartColors"
							chart-dataset-override="egreso.chartDataset">
						</canvas>
			</div>
		</div>
		<div style="margin-bottom: 10px; margin-top: 20px; text-align: center;" ng-show="egreso.chartLoaded && !egreso.sindatos">
			<div>Pronósticos</div>
			<div style="text-align: center;">
				<div style="width: 90%; text-align: right;">
				<button type="button" class="btn btn-default" ng-model="egreso.viewQuetzales_p" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false">
		        	Q
		   		</button>
		   		</div>
		   		<br/>
				<table class="table_pronosticos">
					<thead>
						<tr>
							<td align="center" ng-repeat="label in egreso.chartLabels.slice(12) track by $index">{{ label }}</td>
							<td align="center">Total</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td align="right" ng-repeat="dato_pronostico in egreso.chartData[1].slice(12) track by $index">{{ egreso.filtroQuetzalesP(dato_pronostico.toFixed(2)) }}</td>
							<td align="right">{{ egreso.filtroQuetzales(egreso.total_pronosticos.toFixed(2)) }}</td>
						</tr>
					</tbody>
				</table>
			</div>
			<br/>
			<div>Historia</div>
			<div style="text-align: center;">
				<div style="width: 90%; text-align: right;">
				<button type="button" class="btn btn-default" ng-model="egreso.viewQuetzales" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false">
		        	Q
		   		</button>
		   		</div>
		   		<br/>
				<table class="table_historicos">
					<thead>
						<tr>
							<td>Año</td>
							<td ng-repeat="mes in egreso.meses">{{ mes }}</td>
							<td>Total</td>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="ejercicio in egreso.historia track by $index">
							<td align="center">{{ ejercicio[0] }}</td>
							<td align="right" ng-repeat="dato in ejercicio.slice(1) track by $index">{{ egreso.filtroQuetzales(dato.toFixed(2)) }}</td>
							<td align="right">{{ egreso.filtroQuetzales(egreso.total_ejercicio[$index].toFixed(2)) }}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div style="text-align: center;" ng-show="egreso.sindatos && egreso.entidad==0">Sin datos históricos suficientes para generar los prónosticos</div>
		</uib-tab>
		<uib-tab index="2" heading="Detalle">
			<br/>
			<br/>
			<button type="button" class="btn btn-default" ng-model="egreso.viewQuetzales_d" uib-btn-checkbox btn-checkbox-true="true" btn-checkbox-false="false"
			ng-change="egreso.aplicarFormatoDetalle()"
			>
		        	Q
		   		</button>
			<br/>
			<tree-grid style="font-size: 12px;" tree-data="egreso.tree_data" template-url="treeGrid.html" icon-expand="glyphicon glyphicon-plus"
    			icon-collapse="glyphicon glyphicon-minus" icon-leaf="glyphicon glyphicon-menu-right"
    			col-defs="egreso.tree_cols" expand-on="codigo" tree-control="egreso.detalleTree"></tree-grid>
		</uib-tab>
	</uib-tabset>
<br/>
<br/>
<br/>
</div>