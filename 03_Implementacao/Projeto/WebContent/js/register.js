function verifyPassword() {
	
	var password = document.getElementById("inpPass").value;
	var confirmPassword = document.getElementById("inpConfirmPass").value;
	if (password!=confirmPassword) {
		document.getElementById("errorUser").style.display = "none";
		document.getElementById("passNotMatch").style.display = "block";
	}
	else {
		 document.getElementById("form").submit();
		//verifyRegister();
	}
	
}

function verifyUsername() {
	
	var username = document.getElementById("inpUsername").value;
	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
		if( this.responseText.trim() == "true" ) {
			document.getElementById("errorUser").style.display = "block";
			document.getElementById("passNotMatch").style.display = "none";
		}
		else {
			verifyPassword();
		}
	}

	xhttp.open("POST", "Authentication?action=verifyUsername&username="+username, true);
	xhttp.send(null);
}
/*
function verifyRegister() {
	console.log("verifyRegister");
	var username = document.getElementById("inpUsername").value;
	var password = document.getElementById("inpPass").value;
	var confirmPassword = document.getElementById("inpConfirmPass").value;
	var email = document.getElementById("inpEmail").value;
	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	
		if (this.responseText.trim() == "success") {
			var location = "http://localhost:8080/AnimeArena/VerifyEmail?action=emailSent&username="+username;
			//window.location.href = "email.jsp";
			//sendEmail();
			window.location.href = location;
			
		}
		else {
			window.alert(this.responseText);
		}

	}

	xhttp.open("POST", "Authentication?action=register&username="+username+"&password="+password+
			"&confirm="+confirmPassword+"&email="+email, true);
	xhttp.send(null);
}
*/
function sendEmail() {
	console.log("sendEmail");
	const xhttp = new XMLHttpRequest();

	/*xhttp.onload = function() {
	
		if (this.responseText.trim() == "success") {
			//window.location.href = "email.jsp";
			sendEmail();
		}

	}*/

	xhttp.open("POST", "VerifyEmail?action=emailSent&username="+username+"&email="+email, true);
	xhttp.send(null);
}

