<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/assets/css/main_cuadros_detalle.css" />
<div ng-controller="proyectodetalleController as ctrl" class="maincontainer" id="title" class="all_page">
<div class="row" style="margin-bottom: 10px;">
</div>
<div class="row">
	<div class="div_principal">
	    <div class="menu-title">Proyecto de Presupuesto {{ ctrl.anio }}</div>
	    <div class="row">
	    	<div class="col-sm-4 menu-subtitle-money">Q {{ ctrl.filtroMillones(ctrl.ejecutado_dos_antes, ctrl.viewMillones) }}</div>
	    	<div class="col-sm-4 menu-subtitle-money">Q {{ ctrl.filtroMillones(ctrl.aprobado_anterior_mas_amp, ctrl.viewMillones) }}</div>
	    	<div class="col-sm-4 menu-subtitle-money">Q {{ ctrl.filtroMillones(ctrl.recomendado, ctrl.viewMillones) }}</div>
	    </div>	
	    <div class="row">
	    	<div class="col-sm-4 menu-subtitle">Ejecutado {{ (ctrl.anio -2) }}</div>
	    	<div class="col-sm-4 menu-subtitle">Aprobado* {{ (ctrl.anio -1) }}</div>
	    	<div class="col-sm-4 menu-subtitle">Recomendado {{ ctrl.anio }}</div>
	    </div>
	    <div class="row menu-detalle">
	    	<div class="menu-detalle-item" ng-click="ctrl.goTo('/formulacion/cuadrosdetalle/cuadro5')"><a href="#!/formulacion/cuadrosdetalle/cuadro5">Presupuesto Institucional</a></div>
	    	<div class="menu-detalle-item" ng-click="ctrl.goTo('/formulacion/cuadrosdetalle/cuadro6')"><a href="#!/formulacion/cuadrosdetalle/cuadro6">Por Tipo de Gasto</a></div>
	    	<div class="menu-detalle-item" ng-click="ctrl.goTo('/formulacion/cuadrosdetalle/cuadro8')"><a href="#!/formulacion/cuadrosdetalle/cuadro8">Por Finalidad</a></div>
	    	<div class="menu-detalle-item" ng-click="ctrl.goTo('/formulacion/maparecomendado')"><a href="#!/formulacion/maparecomendado">Mapa de Calor</a></div>	
	    </div>
	</div>
</div>
<br/>
<br/>
<br/>
</div>