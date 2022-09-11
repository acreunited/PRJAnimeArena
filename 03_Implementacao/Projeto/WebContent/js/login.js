function verifyLogin() {
	var username = document.getElementById("inputUsername").value;
	var password = document.getElementById("inputPassword").value;
	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
		
		if (this.responseText.trim() == "valid") {
			window.location.href = "index.jsp";
		}
		else if ( this.responseText.trim() == "notMatch" ) {
			document.getElementById("error").style.display = "block";
			document.getElementById("notActive").style.display = "none";
			document.getElementById("banned").style.display = "none";
		}
		else if ( this.responseText.trim() == "notActive" ) {
			document.getElementById("notActive").style.display = "block";
			document.getElementById("error").style.display = "none";
			document.getElementById("banned").style.display = "none";
		}
		else if (this.responseText.trim()=="banned") {
			document.getElementById("notActive").style.display = "none";
			document.getElementById("error").style.display = "none";
			document.getElementById("banned").style.display = "block";
		}
		
	}

	xhttp.open("POST", "Authentication?action=login&username="+username+"&password="+password, true);
	xhttp.send(null);
}

