$(window).resize(function(){ 
    $("#dataForm").css({ 
        position: "absolute", 
        left: ($(window).width() - $("#dataForm").outerWidth())/2, 
        top: ($(window).height() - $("#dataForm").outerHeight())/2 
    });        
});
$(function(){ 
	console.log($(window).width());
	console.log($("#dataForm").outerWidth());
    $(window).resize(); 
}); 