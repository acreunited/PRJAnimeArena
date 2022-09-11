function createAnn() {
	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   var news = this.responseText.split("break");
		   document.getElementById("annoucements").innerHTML = news[0];	
		   document.getElementById("pastNews").innerHTML = news[1];	
		} 
	}
	xhttp.open("GET", "Announcement?action=load", true);  // assincrono
	xhttp.send(null);

}