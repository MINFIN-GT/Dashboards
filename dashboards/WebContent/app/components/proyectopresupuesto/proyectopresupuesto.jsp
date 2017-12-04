<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
<!--
.header_page{
	background-color: white;
	height: 25mm;
	margin: 20px 0 0 0;
}
.footer_page{
	background-color: white;
	height: 25mm;
	margin: 0 0 20px 0;
}

.body_page{
	background-color: white;
	height: 280mm;
}



.cke_chrome{
	border-top: none;
	border-bottom: none;
}

.ng-scope{
    height: 100% !important;
}

.main{
	height: calc(100% - 95px);
	width: calc(100% - 120px);
	position: absolute;
}

.documento{
	background-color: #c3c3c3; 
	height: calc(100% - 200px);
	overflow-y: auto;
	position: relative;
}

.pagina{
	width: 216mm; 
	margin: 0 auto; 
}

.vruler{
	float: left; 
	width: 20px; 
	height: 330mm;
	background-color: white;
}


.hruler{
	height: 40px;
	width: 216mm; 
	margin-right: auto;
	margin-left: auto;
	margin-top: 10px;
	margin-bottom: 20px;
}

.custom-slider.rzslider .rz-bar {
  background: #b0c4de;
  height: 2px;
}
.custom-slider.rzslider .rz-selection {
  background: #4682b4;
}

.custom-slider.rzslider .rz-pointer {
  width: 8px;
  height: 16px;
  top: auto; /* to remove the default positioning */
  bottom: 0;
  background-color: #333;
  border-top-left-radius: 3px;
  border-top-right-radius: 3px;
}

.custom-slider.rzslider .rz-pointer:after {
  display: none;
}

.custom-slider.rzslider .rz-bubble {
  bottom: 14px;
}

.custom-slider.rzslider .rz-limit {
  font-weight: bold;
  color: #c3c3c3;
}

.custom-slider.rzslider .rz-tick {
  width: 1px;
  height: 10px !important;
  margin-left: 4px;
  border-radius: 0;
  background: #4682b4;
  top: -1px;
}

.custom-slider.rzslider .rz-tick.rz-selected {
  background: #c3c3c3;
}

.custom-slider-v.rz-vertical .rz-bar {
  background: #b0c4de;
  width: 2px;
}
.custom-slider-v.rzslider .rz-selection {
  background: #4682b4;
}

.custom-slider-v.rzslider .rz-pointer {
  width: 16px;
  height: 8px;
  top: auto; /* to remove the default positioning */
  bottom: 0;
  background-color: #333;
  border-top-left-radius: 3px;
  border-bottom-left-radius: 3px;
  margin-left: 5px;
}

.custom-slider-v.rzslider .rz-pointer:after {
  display: none;
}

.custom-slider-v.rzslider .rz-bubble {
  bottom: 14px;
}

.custom-slider-v.rzslider .rz-limit {
  font-weight: bold;
  color: black;
}

.custom-slider-v.rzslider .rz-tick {
  width: 10px;
  height: 1px !important;
  border-radius: 0;
  background: #4682b4;
  top: -1px;
}

.custom-slider-v.rzslider .rz-tick.rz-selected {
  background: #c3c3c3;
}

.custom-slider-v.rz-vertical .rz-tick{
	margin-top: 0;
}

-->
</style>
<div ng-controller="proyectoPresupuestoController as proyecto" class="maincontainer" id="title" class="all_page">
	<div>
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
	<br/>
	<div id="main" class="main">
		<div id="barra_herramientas" style="width: 100%;"></div>
		<div class="hruler">
			<rzslider class="custom-slider"
	          rz-slider-model="proyecto.slider.minValue"
	          rz-slider-high="proyecto.slider.maxValue"
	          rz-slider-options="proyecto.slider.options"></rzslider>
	    </div>
		<div id="documento" class="documento">
			<div>
				<div class="vruler">
					<rzslider class="custom-slider-v"
			          rz-slider-model="proyecto.slider_v.minValue"
			          rz-slider-high="proyecto.slider_v.maxValue"
			          rz-slider-options="proyecto.slider_v.options"></rzslider>
				</div>
				<div class="pagina">
					<div class="header_page">
						<textarea ckeditor="editorOptions" ng-model="proyecto.headers[1]" id="h1"></textarea>
					</div>
					<div class="body_page">
						<textarea ckeditor="editorOptions" ng-model="proyecto.contents[1]" id="b1"></textarea>
			         </div>
			         <div class="footer_page">
			         	<textarea ckeditor="editorOptions" ng-model="proyecto.footers[1]" id="f1"></textarea>
			         </div>
	         	</div>
         	</div>
        </div>
	</div>
</div>