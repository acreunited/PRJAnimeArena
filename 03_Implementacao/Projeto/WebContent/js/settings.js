function correctPassword() {
	if ( $('form')[0].checkValidity() ) {
		var oldPass = document.getElementById("inpOld").value;
		
		const xhttp = new XMLHttpRequest();

		xhttp.onload = function() {
			if( this.responseText.trim() == "false" ) {
				document.getElementById("errorPassIncorrect").style.display = "block";
				document.getElementById("errorPassNotMatch").style.display = "none";
				document.getElementById("patternTitle").style.display = "none";

			}
			else {
				document.getElementById("errorPassIncorrect").style.display = "none";
				document.getElementById("errorPassNotMatch").style.display = "none";
				document.getElementById("patternTitle").style.display = "none";

				//console.log(this.responseText.trim());
				verifyPassword();
			}
		}

		xhttp.open("POST", "Settings?action=verifyOldPass&pass="+oldPass, true);
		xhttp.send(null);
	}
	else {
		document.getElementById("patternTitle").style.display = "block";
	}
			
	
}

function verifyPassword() {
	
	var password = document.getElementById("inpPass").value;
	var confirmPassword = document.getElementById("inpConfirmPass").value;
	if (password!=confirmPassword) {
		document.getElementById("errorPassIncorrect").style.display = "none";
		document.getElementById("errorPassNotMatch").style.display = "block";
	}
	else {
		 document.getElementById("form").submit();
		//verifyRegister();
	}
	
}
function correctEmail() {
	if ( $('form')[0].checkValidity() ) {
		var oldEmail = document.getElementById("inpEmail").value;
		
		const xhttp = new XMLHttpRequest();

		xhttp.onload = function() {
			if( this.responseText.trim() == "false" ) {
				document.getElementById("errorEmailIncorrect").style.display = "block";
				document.getElementById("patternTitleEmail").style.display = "none";
			}
			else {
				document.getElementById("errorEmailIncorrect").style.display = "none";
				document.getElementById("patternTitleEmail").style.display = "none";

				document.getElementById("form").submit();
			}
		}

		xhttp.open("POST", "Settings?action=verifyOldEmail&email="+oldEmail, true);
		xhttp.send(null);
	}
	else {
		document.getElementById("patternTitleEmail").style.display = "block";
	}
}