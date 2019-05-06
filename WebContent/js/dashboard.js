//On load
$(function() {
    // Default: hide edit mode
    $(".editMode").hide();
    
    // Click on "selectall" box
    $("#selectall").click(function () {
        $('.cb').prop('checked', this.checked);
    });

    // Click on a checkbox
    $(".cb").click(function() {
        if ($(".cb").length == $(".cb:checked").length) {
            $("#selectall").prop("checked", true);
        } else {
            $("#selectall").prop("checked", false);
        }
        if ($(".cb:checked").length != 0) {
            $("#deleteSelected").enable();
        } else {
            $("#deleteSelected").disable();
        }
    });

});


// Function setCheckboxValues
(function ( $ ) {

    $.fn.setCheckboxValues = function(formFieldName, checkboxFieldName) {

        var str = $('.' + checkboxFieldName + ':checked').map(function() {
            return this.value;
        }).get().join();
        
        $("input[name="+formFieldName+"]").attr('value',str);
        
        return this;
    };

}( jQuery ));

// Function toggleEditMode
(function ( $ ) {

    $.fn.toggleEditMode = function() {
        if($(".editMode").is(":visible")) {
            $(".editMode").hide();
            $("#editComputer").text("Edit");
        }
        else {
            $(".editMode").show();
            $("#editComputer").text("View");
        }
        return this;
    };

}( jQuery ));


// Function delete selected: Asks for confirmation to delete selected computers, then submits it to the deleteForm
(function ( $ ) {
    $.fn.deleteSelected = function() {
        if (confirm("Are you sure you want to delete the selected computers?")) { 
            $('#deleteForm input[name=selection]').setCheckboxValues('selection','cb');
            $('#deleteForm').submit();
        }
    };
}( jQuery ));



//Event handling
//Onkeydown
$(document).keydown(function(e) {

    switch (e.keyCode) {
        //DEL key
        case 46:
            if($(".editMode").is(":visible") && $(".cb:checked").length != 0) {
                $.fn.deleteSelected();
            }   
            break;
        //E key (CTRL+E will switch to edit mode)
        case 69:
            if(e.ctrlKey) {
                $.fn.toggleEditMode();
            }
            break;
    }
});

//Pagination
$(function() {
	let url = new URL(window.location.href);
	let searchParams = new URLSearchParams(url.search);
	var search = searchParams.get('search');
	
	var activePage = parseInt($("#Previous > a").attr("href").split("=")[1],10)+1;
	var lastPage = $("#Last > a").attr("href").split("=")[1];
	$(".pagination li a:not(:has(> span)):gt("+(lastPage-1)+")").addClass("hidden");
	$('.pagination li a:not(:has(> span))').each(function(){
		if($(this).text() == activePage){
			$(this).addClass("active");
		}
	});
	
	if(activePage == 1){
		$("#First,#Previous").each(function(){
			$(this).addClass("disabled");
		});
	} else if(activePage == lastPage){
		$("#Last,#Next").each(function(){
			$(this).addClass("disabled");
		});
	}
	
	$("#size li a").each(function(){
		if($(this).text() == $("#size").attr("current-size")){
			$(this).addClass("active");
		}
	});
	
	//Keep the search param
	if(search){
		$(".pagination li a").each(function(){
			$(this).attr("href",$(this).attr("href")+"&search="+search);
		});
	}

});

function addParameterToURL(param){
    _url = location.href;
    _url += (_url.split('?')[1] ? '&':'?') + param;
    return _url;
}
