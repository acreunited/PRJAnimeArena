function hideSearching() {
	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   document.getElementById("searchingOpp").style.display="none";		   
		} 
	}
	xhttp.open("GET", "FindOpponent?metodo=cancel", true);  // assincrono
	xhttp.send(null);

}

function displayInfo(id) {
	
	for (let i = 0; i < 100; i++) {
		if (document.getElementById("displayCharacter"+i)!=null) {					
			document.getElementById("displayCharacter"+i).style.display="none";
		}
	} 
	if (document.getElementById("displayCharacter"+id)!=null) {		
		document.getElementById("displayCharacter"+id).style.display="block";
		document.getElementById("charDescription"+id).style.display="block";
	}		
}

function searchOpp(battleType, element) {
	
	if (element.style.opacity == 1) {
		var char1 = getChar(0);
		var char2 = getChar(1);
		var char3 = getChar(2);

		const xhttp = new XMLHttpRequest();

		xhttp.onload = function() {
			if (xhttp.status === 200 && xhttp.readyState === 4) {
			  	document.getElementById("searchingOpp").style.display="block";	
			  	matchmaking(char1, char2, char3, battleType);
			}
		}
		xhttp.open("GET", "FindOpponent?metodo=enterQueue&battleType="+battleType+"&char1="+char1+"&char2="+char2+"&char3="+char3, true);  // assincrono
		xhttp.send(null);
	}
}

function startPB($this) {
	var val = $this.previousElementSibling.value;
	if(val == ''){
		console.log('no input');
	}
	else{
		console.log(val);
		document.getElementById("pbBattle").style.display = "none";
		var char1 = getChar(0);
		var char2 = getChar(1);
		var char3 = getChar(2);

		const xhttp = new XMLHttpRequest();

		xhttp.onload = function() {
			if (xhttp.status === 200 && xhttp.readyState === 4) {
			  	document.getElementById("searchingOpp").style.display="block";	
			  	matchmakingPrivate(char1, char2, char3, "private", val);
			}
		}
		xhttp.open("GET", "FindOpponent?metodo=enterQueue&battleType=private&usernameOpp="+val+"&char1="+char1+"&char2="+char2+"&char3="+char3, true);  // assincrono
		xhttp.send(null);
	}
}

function hidePB() {
	
	document.getElementById("pbBattle").style.display = "none";
}

function privateBattle(element) {
	
	
	if (element.style.opacity == 1) {
		document.getElementById("pbBattle").style.display = "block";
	}
}

	
function matchmakingPrivate(char1, char2, char3, battleType, usernameOpp) {
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   document.getElementById("searchingOpp").style.display="none";
		   if (this.responseText.trim()=="found") {
			   document.getElementById("foundOpp").style.display="block";
			   enterBattle(battleType);
		   }
		   
		} 
	}
	xhttp.open("GET", "FindOpponent?metodo=searchingOpp&battleType="+battleType+"&usernameOpp="+usernameOpp+"&char1="+char1+"&char2="+char2+"&char3="+char3, true);  // assincrono
	xhttp.send(null);

}
function matchmaking(char1, char2, char3, battleType) {
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   document.getElementById("searchingOpp").style.display="none";
	
		   if (this.responseText.trim()=="found") {
			   document.getElementById("foundOpp").style.display="block";
			   enterBattle(battleType);
		   }
		  
		} 
	}
	xhttp.open("GET", "FindOpponent?metodo=searchingOpp&battleType="+battleType+"&char1="+char1+"&char2="+char2+"&char3="+char3, true);  // assincrono
	xhttp.send(null);

}

function enterBattle(type) {
	$(document).ready(function(){
		$.ajax({
			type: "POST",
			url: "InGame",
			data:{
				metodo:'create',
				battleType : type
			},
			success: function(){
				window.location.href = "battle.jsp";
			   }
				
		});
	});
	
}

function findOpp(type) {
	document.getElementById("searchingOpp").style.display="block";
	
	var items = document.getElementsByClassName("items");
	let first = items[0].firstChild.id;
	let second = items[1].firstChild.id;
	let third = items[2].firstChild.id;
	
	document.getElementById("inputChar1").value = first.split("charpic")[1];
	document.getElementById("inputChar2").value = second.split("charpic")[1];
	document.getElementById("inputChar3").value = third.split("charpic")[1];
	
	document.getElementById("battleType").value = type;
	
	var form = document.getElementById("form");
	 
    form.submit();
		
}

function getChar(pos) {
	var items = document.getElementsByClassName("items");
	let first = items[pos].firstChild.id;

	return first.split("charpic")[1];
}
	

function abilityFooterInfo(abilityID) {
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {

		   document.getElementById("natures"+abilityID).innerHTML = this.responseText;
		   displayAbilityDescription(abilityID);
		} 
	}
	xhttp.open("POST", "AbilityActions?action=getAbilityNatureByID&abilityID="+abilityID, true);  // assincrono
	xhttp.send(null);
	
}



function displayAbilityDescription(id) {
	
	for (let i = 0; i < 999; i++) {
		if (document.getElementById("ability"+i)!=null) {					
			document.getElementById("ability"+i).style.display="none";
		}
		if (document.getElementById("charDescription"+i)!=null) {					
			document.getElementById("charDescription"+i).style.display="none";
		}
	} 
	if (document.getElementById("ability"+id)!=null) {	
		document.getElementById("ability"+id).style.display="block";
	}		
}
function displayCharacterDescription(id) {
	
	for (let i = 0; i < 100; i++) {
		if (document.getElementById("ability"+i)!=null) {					
			document.getElementById("ability"+i).style.display="none";
		}
		if (document.getElementById("charDescription"+i)!=null) {					
			document.getElementById("charDescription"+i).style.display="none";
		}
	}
	if (document.getElementById("charDescription"+id)!=null) {	
		document.getElementById("charDescription"+id).style.display="block";
	}
	
}


function dbChar(charID) {
	
	var pos1 = document.getElementById("items1").getElementsByTagName('img')[0];
	var pos2 = document.getElementById("items2").getElementsByTagName('img')[0];
	var pos3 = document.getElementById("items3").getElementsByTagName('img')[0];
	
	id1 = "nada";id2 = "nada";id3 = "nada";
	
	if (pos1!="undefined" && pos1!=null) {
		id1 = pos1.id;
	}
	if (pos2!="undefined" && pos2!=null) {
		id2 = pos2.id;
	}
	if (pos3!="undefined" && pos3!=null) {
		id3 = pos3.id;
	}
	
	if (id1=="nada") {
		document.getElementById("items1").appendChild(putInItems(charID, 1));
		document.getElementById("savePos"+charID).innerHTML = "";
	}
	else if (id2=="nada") {
		document.getElementById("items2").appendChild(putInItems(charID, 2));
		document.getElementById("savePos"+charID).innerHTML = "";
	}
	else if (id3=="nada") {
		document.getElementById("items3").appendChild(putInItems(charID, 3));
		document.getElementById("savePos"+charID).innerHTML = "";		
	}
	checkIfReady();
}

function putInItems(charID, item) {
	
	var elIMG = document.createElement("img");
	elIMG.setAttribute("id","charpic"+charID);
	elIMG.src = "ViewCharacter?id="+charID;
	elIMG.classList.add('personagem');
	elIMG.onclick = function(event) {
		displayInfo(charID);
	}
	elIMG.ondblclick = function(event) {
		removeFromTeam(charID, item);
	}
	return elIMG;
   /* return
	    "<img id='charpic"+charID+"' src='ViewCharacter?id='"+charID+
	    "' class='personagem' ondblclick='dbChar(<%=characterID %>)' onclick='displayInfo("+charID+")>";
*/
}

function removeFromTeam(charID, item) {
	var elIMG = document.createElement("img");
	elIMG.setAttribute("id","charpic"+charID);
	elIMG.src = "ViewCharacter?id="+charID;
	elIMG.classList.add('personagem');
	elIMG.onclick = function(event) {
		displayInfo(charID);
	}
	elIMG.ondblclick = function(event) {
		dbChar(charID);
	}
	
	document.getElementById("savePos"+charID).appendChild(elIMG);
	document.getElementById("items"+item).innerHTML = "";
	
	checkIfReady();
}

function checkIfReady() {
	var pos1 = document.getElementById("items1").getElementsByTagName('img')[0];
	var pos2 = document.getElementById("items2").getElementsByTagName('img')[0];
	var pos3 = document.getElementById("items3").getElementsByTagName('img')[0];
	
	id1 = "nada";id2 = "nada";id3 = "nada";
	
	if (pos1!="undefined" && pos1!=null) {
		id1 = pos1.id;
	}
	if (pos2!="undefined" && pos2!=null) {
		id2 = pos2.id;
	}
	if (pos3!="undefined" && pos3!=null) {
		id3 = pos3.id;
	}
	
	if (id1!="nada" && id2!="nada" && id3!="nada") {
		document.getElementById("btnLadder").style.opacity = 1;
		document.getElementById("btnQuick").style.opacity = 1;
		document.getElementById("btnPrivate").style.opacity = 1;
	}
	else {
		document.getElementById("btnLadder").style.opacity = 0.7;
		document.getElementById("btnQuick").style.opacity = 0.7;
		document.getElementById("btnPrivate").style.opacity = 0.7;
	}
}
	
function allowDrop(ev) {
  ev.preventDefault();
}

function drag(ev) {
  ev.dataTransfer.setData("img", ev.target.id);
}

function drop(ev) {
	
  ev.preventDefault();
  var data = ev.dataTransfer.getData("img");
  
  if (ev.target.id=="" || ev.target.id=="items1" || ev.target.id=="items2" || ev.target.id=="items3") {
	  document.getElementById(data).ondblclick = function(event) {
			removeFromTeam(data.split("charpic")[1], ev.target.id[5]);
	  }
	  ev.target.appendChild(document.getElementById(data));
	  //log();
  }
  checkIfReady();
}