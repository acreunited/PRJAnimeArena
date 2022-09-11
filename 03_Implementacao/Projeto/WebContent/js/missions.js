function hideAll() {
	document.getElementById("bleach").style.display = "none";
	document.getElementById("hunter").style.display = "none";
	document.getElementById("demonslayer").style.display = "none";
}

function showMissions(anime) {
	hideAll();
	document.getElementById(anime).style.display = "block";
    
	var animeTypes = ["missions-bleach", "missions-hunter", "missions-demonslayer"];
	for (let i = 0; i < animeTypes.length; i++) {
		console.log(animeTypes[i])
		if (anime == animeTypes[i].split("-")[1]) {
			document.getElementById(animeTypes[i]).classList.remove("notActiveAnimeType");
			document.getElementById(animeTypes[i]).classList.add("activeAnime");
		}
		else {
			document.getElementById(animeTypes[i]).classList.add("notActiveAnimeType");
			document.getElementById(animeTypes[i]).classList.remove("activeAnime");
		}
	}
}

function missionsCheckBox() {
	var locked = document.getElementById("locked").checked;
	var completed = document.getElementById("completed").checked;
	
	var mLocked = document.getElementsByClassName("mLocked");
	if (locked) {
		for (let i = 0; i < mLocked.length; i++) {
			mLocked[i].parentNode.parentNode.parentNode.classList.add("hideCheckBox");
		}
	}
	else {
		for (let i = 0; i < mLocked.length; i++) {
			mLocked[i].parentNode.parentNode.parentNode.classList.remove("hideCheckBox");
		}
	}
	
	var mCompleted = document.getElementsByClassName("mCompleted");
	if (completed) {
		for (let i = 0; i < mCompleted.length; i++) {
			mCompleted[i].parentNode.parentNode.parentNode.classList.add("hideCompleted");
		}
	}
	else {
		for (let i = 0; i < mCompleted.length; i++) {
			mCompleted[i].parentNode.parentNode.parentNode.classList.remove("hideCompleted");
		}
	}
	
}

function getArrChars() {
	var arr = [];
	var names = document.getElementsByClassName("characterName");
	for (let i = 0; i < names.length; i++) {
		arr.push(names[i].innerHTML.trim());
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
	var names = document.getElementsByClassName("characterName");
	for (let i = 0; i < names.length; i++) {
		if (arr.includes(names[i].innerHTML.trim())) {
			names[i].parentNode.classList.remove("hideSearch");
		}
		else {
			names[i].parentNode.classList.add("hideSearch");
		}
	}
}

