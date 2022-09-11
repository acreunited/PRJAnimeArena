function addText(x) {
	document.getElementById("fullAnnText").value += x;
}

function showPreview() {
	var current = document.getElementById("fullAnnText").value;
	
	var current2 = current.replaceAll("[", "asdfg");
	var current3 = current2.replaceAll("]", "gfdsa");
	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {s
		   document.getElementById("previewHere").innerHTML = this.responseText;
		   document.getElementById("prev").style.display = "block";
	   } 
	}
	xhttp.open("POST", "Announcement?action=preview&current="+current3, true);  // assincrono
	xhttp.send(null);
	
	
	
}