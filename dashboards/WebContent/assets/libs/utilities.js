/**
 * 
 */

var deepCopy = function( o ) {
    return JSON.parse(JSON.stringify( o ));
}

var redirectMain = function(){
	location.href="/main.jsp#";
}

function convertTreeToList(root) {
    var stack = [], array = [], hashMap = {};
    for(var i=root.length-1; i>=0; i--){
    	stack.push(root[i]);
    }
    
    while(stack.length !== 0) {
        var node = stack.pop();
        if(node.children === null) {
        	array.push(node);
        } else {
        	array.push(node);
            for(var i = node.children.length - 1; i >= 0; i--) {
                stack.push(node.children[i]);
            }
        }
    }
    return array;
}