<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div ng-controller="proyectoPresupuestoController as proyecto" class="maincontainer" id="title" class="all_page">
	<div class="row">
		<div style="margin-top: 6px; float: left; font-size: 24px;">Proyecto de Presupuesto</div>
		<div style="margin-right: 5px;">
			<div class="btn-group" uib-dropdown>
		      <button id="single-button" type="button" class="btn btn-default no-border" uib-dropdown-toggle ng-disabled="ejecucion.showloading" style="width: 100px; text-align: left; font-size: 24px;">
		        {{ proyecto.ano }} <span class="caret"></span>
		      </button>
		      <ul uib-dropdown-menu role="menu" aria-labelledby="single-button">
		      	<li role="menuitem"><a href ng-click="proyecto.anoClick(2018)">2018</a></li>
		        <li role="menuitem"><a href ng-click="proyecto.anoClick(2017)">2017</a></li>
		        <li role="menuitem"><a href ng-click="proyecto.anoClick(2016)">2016</a></li>
		      </ul>
		    </div>
	    </div>
	</div>
</div>