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

//Order By
$(function() {
	$(".fa-sort, .fa-sort-asc, fa-sort-desc").each(function(index){
		var orderBy = index*2+1;
		$(this).parent().attr("href","?orderBy="+orderBy);
	});
	var orderBy = parseInt($("#orderBy").text(),10);
	switch(orderBy){
	case 1:
		var icon = $(".fa-sort").eq(0);
		icon.parent().attr("href","?orderBy="+2);
		icon.addClass("fa-sort-asc");
		icon.removeClass("fa-sort");
		break;
	case 2:
		var icon = $(".fa-sort").eq(0);
		icon.parent().attr("href","?orderBy="+0);
		icon.addClass("fa-sort-desc");
		icon.removeClass("fa-sort");
		break;
	case 3:
		var icon = $(".fa-sort").eq(1);
		icon.parent().attr("href","?orderBy="+4);
		icon.addClass("fa-sort-asc");
		icon.removeClass("fa-sort");
		break;
	case 4:
		var icon = $(".fa-sort").eq(1);
		icon.parent().attr("href","?orderBy="+0);
		icon.addClass("fa-sort-desc");
		icon.removeClass("fa-sort");
		break;
	case 5:
		var icon = $(".fa-sort").eq(2);
		icon.parent().attr("href","?orderBy="+6);
		icon.addClass("fa-sort-asc");
		icon.removeClass("fa-sort");
		break;
	case 6:
		var icon = $(".fa-sort").eq(2);
		icon.parent().attr("href","?orderBy="+0);
		icon.addClass("fa-sort-desc");
		icon.removeClass("fa-sort");
		break;
	case 7:
		var icon = $(".fa-sort").eq(3);
		icon.parent().attr("href","?orderBy="+8);
		icon.addClass("fa-sort-asc");
		icon.removeClass("fa-sort");
		break;
	case 8:
		var icon = $(".fa-sort").eq(3);
		icon.parent().attr("href","?orderBy="+0);
		icon.addClass("fa-sort-desc");
		icon.removeClass("fa-sort");
		break;
	default:
		
		
	}
	$(".container").on("click",".fa-sort",function(){
		$(this).removeClass("fa-sort");
		$(this).addClass("fa-sort-desc");
	});
	
	$(".container").on("click",".fa-sort-desc",function(){
		$(this).removeClass("fa-sort-desc");
		$(this).addClass("fa-sort-asc");
	});
	
	$(".container").on("click",".fa-sort-asc",function(){
		$(this).removeClass("fa-sort-asc");
		$(this).addClass("fa-sort");
	});
});

//Pagination
$(function() {
	let url = new URL(window.location.href);
	let searchParams = new URLSearchParams(url.search);
	var size = searchParams.get('size');
	var search = searchParams.get('search');
	var orderBy = searchParams.get('orderBy');
	
	var activePage = parseInt($("#Previous > a").attr("href").split("=")[1],10)+1;
	var lastPage = $("#Last > a").attr("href").split("=")[1].split("&")[0];
	$("#pagination li a:not(:has(> span)):gt("+(lastPage-1)+")").addClass("hidden");
	$('#pagination li a:not(:has(> span))').each(function(){
		if($(this).text() == activePage){
			$(this).addClass("active");
		}
	});
	
	if(activePage == 1){
		$("#First,#Previous").each(function(){
			$(this).addClass("disabled");
		});
	} 
	if(activePage == lastPage){
		$("#Last,#Next").each(function(){
			$(this).addClass("disabled");
		});
	}
	
	$("#size li a").each(function(){
		if($(this).text() == $("#size").attr("current-size")){
			$(this).addClass("active");
		}
	});
	
	//Keep the size param
	if(size){
		$("#pagination li a").each(function(){
			$(this).attr("href",$(this).attr("href")+"&size="+size);
		});
		
		$(".fa-sort, .fa-sort-asc, fa-sort-desc").each(function(index){
			$(this).parent().attr("href",$(this).parent().attr("href")+"&size="+size);
		});
	}
	//Keep the search param
	if(search){
		$(".pagination li a").each(function(){
			$(this).attr("href",$(this).attr("href")+"&search="+search);
		});
		$(".fa-sort, .fa-sort-asc, fa-sort-desc").each(function(index){
			$(this).parent().attr("href",$(this).parent().attr("href")+"&search="+search);
		});
	}
	
	//Keep the orderBy param
	if(orderBy){
		$(".pagination li a").each(function(){
			$(this).attr("href",$(this).attr("href")+"&orderBy="+orderBy);
		});
	}
});

//Lang selector
$(function(){
	
	var cookieValue = document.cookie.replace(/(?:(?:^|.*;\s*)language\s*\=\s*([^;]*).*$)|^.*$/, "$1");
	$(".lang-selector").children().each(function(){
		if($(this).text().toUpperCase()===cookieValue.toUpperCase()){
			$(this).addClass("bold");
		}
	});
});

