/**
 * 
 */

var deepCopy = function( o ) {
    return JSON.parse(JSON.stringify( o ));
}

var redirectMain = function(){
	location.href="/main.jsp#";
}