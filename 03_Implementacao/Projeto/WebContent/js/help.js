function displayNones() {
	document.getElementById("textInGame").style.display = "none";
	document.getElementById("explanationInGame").style.display = "none";

	document.getElementById("textEnergy").style.display = "none";
	document.getElementById("explanationEnergy").style.display = "none";

}

function setNav(nav) {
	var animeTypes = ["nav-InGame", "nav-Energy"];
	for (let i = 0; i < animeTypes.length; i++) {
	
		if (nav == animeTypes[i].split("-")[1]) {
			document.getElementById(animeTypes[i]).classList.remove("notActiveAnimeType");
			document.getElementById(animeTypes[i]).classList.add("activeAnime");
		}
		else {
			document.getElementById(animeTypes[i]).classList.add("notActiveAnimeType");
			document.getElementById(animeTypes[i]).classList.remove("activeAnime");
		}
	}
}

function showEnergy() {
	displayNones();
	setNav("Energy");
	
	document.getElementById("textEnergy").style.display = "block";
	document.getElementById("explanationEnergy").style.display = "block";
}

function showInGame() {
	displayNones();
	setNav("InGame");
	
	document.getElementById("textInGame").style.display = "block";
	document.getElementById("explanationInGame").style.display = "block";
}

function displayNonesChars() {
	var bleach = document.getElementsByClassName("bleach");
	var ds = document.getElementsByClassName("DS");
	var hunter = document.getElementsByClassName("hunter");
	var onepunchman = document.getElementsByClassName("OnePunchMan");
	var sao = document.getElementsByClassName("SAO");
	
	for (let i = 0; i < bleach.length; i++) {
		bleach[i].style.display="none";
	}
	for (let i = 0; i < ds.length; i++) {
		ds[i].style.display="none";
	}
	for (let i = 0; i < hunter.length; i++) {
		hunter[i].style.display="none";
	}
	for (let i = 0; i < onepunchman.length; i++) {
		onepunchman[i].style.display="none";
	}
	for (let i = 0; i < sao.length; i++) {
		sao[i].style.display="none";
	}
}

function setNavChars(nav) {
	var animeTypes = ["nav-all", "nav-bleach", "nav-DS", "nav-hunter", "nav-OnePunchMan", "nav-SAO"];
	for (let i = 0; i < animeTypes.length; i++) {
	console.log(animeTypes[i]+" vs "+nav);
		if (nav == animeTypes[i].split("-")[1]) {
			document.getElementById(animeTypes[i]).classList.remove("notActiveAnimeType");
			document.getElementById(animeTypes[i]).classList.add("activeAnime");
		}
		else {
			document.getElementById(animeTypes[i]).classList.add("notActiveAnimeType");
			document.getElementById(animeTypes[i]).classList.remove("activeAnime");
		}
	}
	
	showChar(nav);
}
function showChar(nav) {
	displayNonesChars();
	var bleach = document.getElementsByClassName("bleach");
	var ds = document.getElementsByClassName("DS");
	var hunter = document.getElementsByClassName("hunter");
	var onepunchman = document.getElementsByClassName("OnePunchMan");
	var sao = document.getElementsByClassName("SAO");
	
	if (nav=="all") {
		for (let i = 0; i < bleach.length; i++) {
			bleach[i].style.display="block";
		}
		for (let i = 0; i < ds.length; i++) {
			ds[i].style.display="block";
		}
		for (let i = 0; i < hunter.length; i++) {
			hunter[i].style.display="block";
		}
		for (let i = 0; i < onepunchman.length; i++) {
			onepunchman[i].style.display="block";
		}
		for (let i = 0; i < sao.length; i++) {
			sao[i].style.display="block";
		}
	}
	else {
		var x = document.getElementsByClassName(nav);
		for (let i = 0; i < x.length; i++) {
			x[i].style.display="block";
		}
	}
}

function getArrChars() {
	var arr = [];
	var names = document.getElementsByClassName("fName");
	var names2 = document.getElementsByClassName("sName");
	for (let i = 0; i < names.length; i++) {
		var nome = names[i].innerHTML.trim()+"-"+names2[i].innerHTML.trim();
		arr.push(nome);
	}
	return arr;
}

function searchForCharacter(el) {
	var input = el.value;
	
	var arr = getArrChars();
	var re = new RegExp(input+'.+$', 'i');
	arr = arr.filter(function(e, i, a){
	    return e.search(re) != -1;
	});
	hideNotResult(arr);
}

function hideNotResult(arr) {
	var names = document.getElementsByClassName("fName");
	var names2 = document.getElementsByClassName("sName");
	for (let i = 0; i < names.length; i++) {
		var nome = names[i].innerHTML.trim()+"-"+names2[i].innerHTML.trim();
		
		if (arr.includes(nome)) {
			names[i].parentNode.classList.remove("hideSearch");
		}
		else {
			names[i].parentNode.classList.add("hideSearch");
		}
	}
}
