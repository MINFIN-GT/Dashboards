<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <nav ng-class="{'showbar' : !hidebar, 'hidebar': hidebar}" class="navbar navbar-inverse navbar-fixed-top">
	    <div class="container">
	    
	        <input type="checkbox" id="navbar-toggle-cbox" />	       
	        <div class="navbar-header">
	            <label for="navbar-toggle-cbox" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
	                <span class="icon-bar"></span>
	                <span class="icon-bar"></span>
	                <span class="icon-bar"></span>
	            </label>
	            <a class="navbar-brand" href="/main.jsp"><span class="glyphicon glyphicon-home" aria-hidden="true"></span></a>
		    </div>
		    
		    <div class="collapse navbar-collapse" id="navBar">
	            <ul class="nav navbar-nav">
	                <li uib-dropdown>
	                    <a href="#" uib-dropdown-toggle><span class="glyphicon glyphicon-stats" aria-hidden="true"></span>Ejecución <b class="caret"></b></a>
	                     <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                     	<li role="menuitem"><a href="#!/dashboards/ejecucionpresupuestaria">Ejecución Presupuestaria</a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/ejecucionfisica">Ejecución Física</a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/copep">Cuotas COPEP</a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/copeprenglon">Cuotas COPEP por Renglón</a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/proyeccion_ingresos">Pronósticos de Ingresos</a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/proyeccion_egresos">Pronósticos de Egresos</a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/flujocaja">Flujo Presupuestario</a></li>
	                    </ul>
	                </li>
	                <li uib-dropdown>
	                    <a href="#" uib-dropdown-toggle><span class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></span>Guatecompras <b class="caret"></b></a>
	                     <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                     	<li role="menuitem"><a href="#!/dashboards/eventosgc">Eventos Guatecompras</a></li>
	                        <!--  <li role="menuitem"><a href="#">Another action</a></li>
	                        <li role="menuitem"><a href="#">Something else here</a></li>
	                        <li class="divider"></li>
	                        <li role="menuitem"><a href="#">Separated link</a></li>  -->
	                    </ul>
	                </li>
	                <li uib-dropdown>
	                    <a href="#" uib-dropdown-toggle><span class="glyphicon glyphicon-search" aria-hidden="true"></span>Transparencia <b class="caret"></b></a>
	                     <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                     	<li role="menuitem"><a href="#!/transparencia/estados_de_calamidad">Estados de Calamidad</a></li>
	                        <!--  <li role="menuitem"><a href="#">Another action</a></li>
	                        <li role="menuitem"><a href="#">Something else here</a></li>
	                        <li class="divider"></li>
	                        <li role="menuitem"><a href="#">Separated link</a></li>  -->
	                    </ul>
	                </li>
	                <li uib-dropdown>
	                    <a href="#" uib-dropdown-toggle><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Mapas <b class="caret"></b></a>
	                     <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                        <li role="menuitem"><a href="#!/maps/gastoGeneral">Mapa de Ejecución</a></li>
	                        <li role="menuitem"><a href="#!/maps/gastoCodedesMunis">Mapa CODEDES y Municipalidades</a></li>
	                        <!--  <li role="menuitem"><a href="#">Another action</a></li>
	                        <li role="menuitem"><a href="#">Something else here</a></li>
	                        <li class="divider"></li>
	                        <li role="menuitem"><a href="#">Separated link</a></li>  -->
	                    </ul>
	                </li>
	                <li uib-dropdown>
	                    <a href="#" uib-dropdown-toggle><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Utils <b class="caret"></b></a>
	                     <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                     	<shiro:hasPermission name="1">
	                        	<li role="menuitem"><a href="#!/admin/users">Usuarios</a></li>
	                        </shiro:hasPermission>
	                        <li role="menuitem"><a href="#!/logs/logs">Logs</a></li>
	                     </ul>
	                </li>
	            </ul>
	            <ul class="nav navbar-nav navbar-right">
		            <li><a href="/SLogout"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Salir</a></li>
		            <li><a><span class="glyphicon glyphicon-chevron-up" aria-hidden="true" ng-click="hideBarFromMenu()"></span> </a></li>
		        </ul>
		    </div>
		    <div ng-hide ="!hidebar" class='triangle-down alineado' ng-click="showBarFromMenu()">
					<p>
					<i class='fa fa-chevron-down fa-12x isDown' id='toggle'></i>
					</p>
			</div>
	    </div>
	</nav>