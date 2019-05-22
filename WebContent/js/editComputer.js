//On load
$(function() {
	$(document).ready(function(){
		
		$("#introduced").datepicker({
			dateFormat: 'dd-mm-yy',
			onSelect: function(selected) {
				var date = $(this).datepicker('getDate','+1d');
				date.setDate(date.getDate()+1); 
				$("#discontinued").datepicker("option","minDate", date);
			}
		});
		$("#discontinued").datepicker({ 
			dateFormat: 'dd-mm-yy',
			onSelect: function(selected) {
				var date2 = $(this).datepicker('getDate','-1d');
				date2.setDate(date2.getDate()-1); 
				$("#introduced").datepicker("option","maxDate", date2)
			}
		});  
	});

	$("#editComputer").on("submit",function(e){
		$(".alert").remove();
		$(".has-error").removeClass("has-error");

		var name = $("#computerName").val();
		if(name.trim().length === 0){
			e.preventDefault();
			$('#computerName').after('<div class="alert alert-danger" role="alert">'+
					'Please enter a valid name'+
			'</div>');
			$('#computerName').parent().addClass("has-error");

		}
		var regex = new RegExp("\\d{2}-\\d{2}-\\d{4}$");
		var introduced = $("#introduced").val();
		if(introduced && !regex.test(introduced)){
			e.preventDefault();
			$('#introduced').after('<div class="alert alert-danger" role="alert">'+
					'Please enter a valid date'+
			'</div>');
			$('#introduced').parent().addClass("has-error");
		}
		var discontinued = $("#discontinued").val();
		if(discontinued && !regex.test(discontinued)){
			e.preventDefault();
			$('#discontinued').after('<div class="alert alert-danger" role="alert">'+
					'Please enter a valid date'+
			'</div>');
			$('#discontinued').parent().addClass("has-error");
		}

		if((!introduced && !!discontinued) || (!!discontinued && $("#introduced").datepicker('getDate') >= $("#discontinued").datepicker('getDate'))){
			e.preventDefault();
			$('#discontinued').after('<div class="alert alert-danger" role="alert">'+
					'Please make sure that discontinued is after introduced'+
			'</div>');
			$('#discontinued').parent().addClass("has-error");
		}
	});

});

//Lang selector
$(function(){
	
	var cookieValue = document.cookie.replace(/(?:(?:^|.*;\s*)language\s*\=\s*([^;]*).*$)|^.*$/, "$1");
	$(".lang-selector").children().each(function(){
		if($(this).text().toUpperCase()===cookieValue.toUpperCase()){
			$(this).addClass("bold");
		}
	});

	let url = new URL(window.location.href);
	let searchParams = new URLSearchParams(url.search);
	var id = searchParams.get('id');
	
	//Keep the id param
	if(id){
		$(".lang-selector a").each(function(){
			$(this).attr("href",$(this).attr("href")+"&id="+id);
		});
	}
});
