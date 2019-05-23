//On load
$(function() {
	
	var cookieValue = document.cookie.replace(/(?:(?:^|.*;\s*)language\s*\=\s*([^;]*).*$)|^.*$/, "$1");
	var haveBold = false;
	$(".lang-selector").children().each(function(){
		if($(this).text().toUpperCase()===cookieValue.toUpperCase()){
			$(this).addClass("bold");
			haveBold = true;
		}
	});
	if(haveBold === false){
		document.cookie = "language=en";
	}
	
	let url = new URL(window.location.href);
	let searchParams = new URLSearchParams(url.search);
	var search = searchParams.get('search');
	//Keep the search param
	if(search){
		$(".lang-selector a").each(function(){
			$(this).attr("href",$(this).attr("href")+"&search="+search);
		});
	}

});
