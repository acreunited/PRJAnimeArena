function hideAll() {
	document.getElementById("ladder").style.display = "none";
	document.getElementById("streak").style.display = "none";
	document.getElementById("nWins").style.display = "none";
}

function showRank(anime) {
	hideAll();
	document.getElementById(anime).style.display = "block";
    
	var animeTypes = ["ranking-ladder", "ranking-streak", "ranking-nWins"];
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

/*Search*/
function getArrChars() {
	var arr = [];
	var names = document.getElementsByClassName("playerName");
	for (let i = 0; i < names.length; i++) {
		arr.push(names[i].innerHTML.trim());
	}
	return arr;
}

function searchForPlayer(el) {
	var input = el.value;
	
	var arr = getArrChars();
	var re = new RegExp(input+'.+$', 'i');
	arr = arr.filter(function(e, i, a){
	    return e.search(re) != -1;
	});
	hideNotResult(arr);
}

function hideNotResult(arr) {
	var names = document.getElementsByClassName("playerName");
	for (let i = 0; i < names.length; i++) {
		if (arr.includes(names[i].innerHTML.trim())) {
			names[i].parentNode.parentNode.classList.remove("hideSearch");
		}
		else {
			names[i].parentNode.parentNode.classList.add("hideSearch");
		}
	}
}