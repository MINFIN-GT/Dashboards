<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div ng-controller="logsController as log" class="maincontainer" id="title" class="all_page">
<h4>Logs</h4>
<div class="row cols_treeview">
	<div class="cols_treeview div_tree" resizable r-directions="['right']" r-flex="false" style="height: 100% !important;">
			<div style="border: thin solid #c3c3c3; border-radius: 4px; overflow: auto; height: 100%;">
				<div treecontrol="" class="tree-classic" tree-model="log.treedata" options="log.tree_options"  selected-node="log.nodo_seleccionado"
						expanded-nodes="log.nodos_expandidos" on-selection="log.showSelected(node)" style="width: 1000px; margin: 10px 0px 0px -5px;">
     				  {{node.name}}
				</div>
				</div>
		</div>
		<div class="cols_treeview" style="margin: 10px 15px 0px 15px; height: 100% !important;">
		<div style="border: thin solid #c3c3c3; border-radius: 4px; height: 100%; overflow-y: scroll;">
			<pre>{{ log.content_file }}</pre>
		</div>
	</div>
</div>
<br/>
<br/>
<br/>
</div>