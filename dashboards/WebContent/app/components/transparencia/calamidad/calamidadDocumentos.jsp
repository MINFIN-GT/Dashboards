<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <style>
    form .progress {
	    line-height: 15px;
	}
	
	.progress {
	    display: inline-block;
	    width: 100px;
	    border: 3px groove #CCC;
	}

	.progress div {
	    font-size: smaller;
	    background: green;
	    width: 0
	}
	</style>
    <div ng-controller="calamidadDocumentosController as control" class="maincontainer" id="title">
	<h3>Tablero de Seguimiento a Estados de {{ control.tipo }} - {{ control.titulo }}</h3>
	<br/>
	<div class="row">
		<div class="col-sm-12">
			<h4>Documentos</h4>
		</div>
	</div>
	
	<div class="row panel panel-default" style="margin: 10px 0px 20px 0px; height: 500px; position: relative; overflow: auto;">
		<table st-table="control.documentos" st-safe-src="control.original_documentos" class="table table-striped">
			<thead>
				<tr>
					<th st-sort="nombre">Nombre</th>
					<th st-sort="actividad">Actividad</th>
					<th st-sort="fecha_creacion">Fecha de Creación</th>
					<th>Link</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="row in control.documentos">
					<td>{{ row.nombre }}</td>
					<td>{{ row.actividad }}</td>
					<td>{{ row.fecha_creacion | date }}</td>
					<td><a href="/SDownload?place={{ control.subprograma }}&iddoc={{ row.id }}"><span class="glyphicon glyphicon-save"></span></a></td>
				</tr>
			</tbody>
		</table>
		<div class="grid_loading" ng-hide="!control.showloading">
 			<div class="msg">
     			<span><i class="fa fa-spinner fa-spin fa-4x"></i>
	  			<br /><br />
	  			<b>Cargando, por favor espere...</b>
  				</span>
			</div>
		</div>
	</div>
	
	</div>

