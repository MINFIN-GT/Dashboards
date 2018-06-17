<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div ng-controller="usersController as usuario" class="maincontainer" id="title" class="all_page">
<script type="text/ng-template" id="email.html">
        <div class="modal-header">
            <h3 class="modal-title" id="modal-title">Email</h3>
        </div>
        <div class="modal-body" id="modal-body">
			<div class="row">
				<div class="col-sm-12" style="text-align: left;">
					<label>Para</label>
				    	<input type="text" ng-model="$ctrl.to" required  class="form-control"/>
				</div>
		    </div>
			<div class="row">
				<div class="col-sm-12" style="text-align: left;">
					<label>Tema</label>
				    	<input type="text" ng-model="$ctrl.subject" required  class="form-control"/>
				</div>
		    </div>
			<br/>
			<div class="row">
				<div class="col-sm-12" style="text-align: left;">
					<label>Mensaje</label>
					<div class="panel panel-default">
  						<div class="panel-body" ng-bind-html="$ctrl.message">
    						
  						</div>
					</div>
				</div>
		    </div>
        </div>
    </script>
<shiro:hasPermission name="1">
	<h4>Usuarios</h4>
	<h5>Administración de Usuarios</h5>
	<br/>
	<br/>
	<div class="row">
		<uib-tabset active="usuario.tabActivo">
		    <uib-tab index="0" heading="Usuarios">
		    	<table st-table="usuario.displayedCollection" st-safe-src="usuario.rowCollection" class="table table-striped">
					<thead>
					<tr>
						<th st-sort="id">Usuario</th>
						<th st-sort="name">Nombre</th>
						<th st-sort="dependence">Dependencia</th>
						<th st-sort="position">Posición</th>
					</tr>
					<tr>
						<th colspan="4"><input st-search="" class="form-control" placeholder="búsqueda global ..." type="text"/></th>
					</tr>
					</thead>
					<tbody>
					<tr st-select-row="row" ng-repeat="row in usuario.displayedCollection" ng-click="usuario.clickUser(row)">
						<td>{{row.username}}</td>
						<td>{{ row.firstname + (row.secondname.length>0 ? ' ' + row.secondname : '' ) + ' ' + row.lastname + ( row.secondlastname.length>0 ? ' ' + row.secondlastname: '') }}</td>
						<td>{{row.dependence}}</td>
						<td>{{row.position}}</td>
					</tr>
					</tbody>
				</table>
				<div style="text-align: right;">{{ usuario.displayedCollection.length }} {{ (usuario.displayedCollection.length==1 ? "usuario" : "usuarios")}}</div>
		    </uib-tab>
		    <uib-tab index="1" heading="Detalle">
		    	<br/>
		    	<form name="usuario.mainform">
			    	<div style="width: 90%; margin: 0 auto;">
			    		<div class="row">
				      		<div class="col-sm-6">
				      			<label class="label_required">Usuario</label>
				      			<input type="text" ng-model="usuario.user.username" required  class="form-control" ng-disabled="!usuario.newUser"/>
				      		</div>
				      	</div>
				      	<br/>
				      	<div class="row">
				      		<div class="col-sm-3">
				      			<label class="label_required">P. Nombre</label>
				      			<input type="text" ng-model="usuario.user.firstname" required  class="form-control"/>
				      		</div>
				      		<div class="col-sm-3">
				      			<label>S. Nombre</label>
				      			<input type="text" ng-model="usuario.user.secondname" class="form-control"/>
				      		</div>
				      		<div class="col-sm-3">
				      			<label class="label_required">P. Apellido</label>
				      			<input type="text" ng-model="usuario.user.lastname" required  class="form-control"/>
				      		</div>
				      		<div class="col-sm-3">
				      			<label>S. Apellido</label>
				      			<input type="text" ng-model="usuario.user.secondlastname" class="form-control"/>
				      		</div>
				      	</div>
				      	<br/>
				      	<div class="row">
				      		<div class="col-sm-12">
				      			<label class="label_required">Email</label>
				      			<input type="text" ng-model="usuario.user.email" required class="form-control"/>
				      		</div>
				      	</div>
				      	<br/>
				      	<div class="row">
				      		<div class="col-sm-6">
				      			<label class="label_required">Dependencia</label>
				      			<input type="text" ng-model="usuario.user.dependence" required class="form-control"/>
				      		</div>
				      		<div class="col-sm-6">
				      			<label  class="label_required">Posición</label>
				      			<input type="text" ng-model="usuario.user.position" required class="form-control"/>
				      		</div>
				      	</div>
				      	<br/>
				      	<div class="row">
				      		<div class="col-sm-6">
				      			<label class="label_required">Contraseña</label>
				      			<input type="password" ng-model="usuario.user.password" ng-required="usuario.newUser"  class="form-control"/>
				      		</div>
				      		<div class="col-sm-6">
				      			<label class="label_required">Confirmación de Contraseña</label>
				      			<input type="password" ng-model="usuario.password_confirm" ng-required="usuario.newUser || (usuario.user.password!=null && usuario.user.password!='')" class="form-control"/>
				      		</div>
				      	</div>
				      	<br/>
				      	<br/>
				      	<h4>Permisos</h4>
				      	<table st-table="usuario.user.permissions" class="table table-striped">
				      		<thead>
				      			<tr>
				      				<td width="1%"></td>
				      				<td></td>
				      			</tr>
				      		</thead>
				      		<tbody>
				      			<tr st-select-row="row" ng-repeat="row in usuario.permissions">
									<td><input type="checkbox" ng-model="row.hasPermission"></td>
									<td>{{ row.name }}</td>
								</tr>
				      		</tbody>
				      	</table>
				      	<br/>
				      	<br/>
				      	<br/>
				      	<div class="row" style="text-align: center;">
				      		<button type="button" class="btn btn-primary" ng-click="usuario.clickSave()" ng-disabled="usuario.disabled_buttons">Guardar</button>
	     	 				<button type="button" class="btn btn-success" ng-click="usuario.clickEmail()" ng-disabled="usuario.disabled_buttons || usuario.newUser">Enviar Correo</button>
	     	 				<button type="button" class="btn btn-success" ng-click="usuario.clickViewEmail()" ng-disabled="usuario.disabled_buttons || usuario.newUser">Ver Correo</button>
	     	 				<button type="button" class="btn btn-primary" ng-click="usuario.clickNew()" ng-disabled="usuario.disabled_buttons">Nuevo</button>
	     	 				<button type="button" class="btn btn-danger" ng-click="usuario.clickDelete()" ng-disabled="usuario.disabled_buttons || usuario.newUser">Borrar</button>
				      	</div>
			      	</div>
		      	</form>
		    </uib-tab>
		  </uib-tabset>
	</div>
	<br/>
	<br/>
	<br/>
</shiro:hasPermission>
<shiro:lacksPermission name="1">
	<div ng-init="usuario.redirectMain()"></div>
</shiro:lacksPermission>
</div>