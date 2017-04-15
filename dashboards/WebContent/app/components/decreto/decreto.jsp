<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div ng-controller="decretoController as decreto" class="maincontainer" id="title" class="all_page">
<h4>Decreto {{ decreto.titulo}}</h4>
<div class="row">
	<div class="col-sm-12">
		
	 </div>
	 <div class="col-sm-12">
	    
	</div>
</div>
<br/>
<div class="row">
	<table st-table="displayedCollection" st-safe-src="rowCollection" class="table table-striped">
			<thead>
			<tr>
				<th st-sort="numero">No.</th>
				<th st-sort="titulo">Artículo</th>
				<th st-sort="contenido">Obligación</th>
				<th st-sort="tiempo">Tiempo</th>
				<th st-sort="responsable">Responsable</th>
				<th st-sort="entrega_a">Entrega a</th>
				<th st-sort="modo_verificacion">Modo Verificación</th>
				<th st-sort="aplica_a_minfin">Aplica MINFIN</th>
				<th st-sort="obligacion_estado_id">Estado</th>
			</tr>
			<tr>
				<th colspan="5"><input st-search="" class="form-control" placeholder="busqueda general ..." type="text"/></th>
			</tr>
			</thead>
			<tbody>
			<tr ng-repeat="row in displayedCollection">
				<td>{{row.numero}}</td>
				<td>{{row.titulo}}</td>
				<td>{{row.contenido}}</td>
				<td>{{row.tiempo}}</td>
				<td>{{row.responsable}}</td>
				<td>{{row.entrega_a}}</td>
				<td>{{row.modo_verificacion}}</td>
				<td>{{row.aplica_a_minfin}}</td>
				<td>
					<div>
						<span class="semaphore-dot" ng-style="{{ 'color: '+row.color }}">&bull;</span>
					</div>
				</td>
			</tr>
			</tbody>
		</table>
</div>
<br/>
<br/>
<br/>
</div>