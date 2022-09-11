let abilityClicked = null;
let charPosUsedSkill = null;
let abilityUsedPos = null;

let allAbilitiesUsed = [];
let allCharsUsedSkill = [];
let allTargets = [];
let allAllyEnemy = [];
let allAbilitiesID = [];

let canWriteTurn = true;

function cancelTurn() {
	
	 var tai = document.getElementById("toRemoveTaijutsu").innerHTML.trim();
	 var heart = document.getElementById("toRemoveHeart").innerHTML.trim();
	 var energy = document.getElementById("toRemoveEnergy").innerHTML.trim();
	 var spirit = document.getElementById("toRemoveSpirit").innerHTML.trim();
	 var random = document.getElementById("nRandom").innerHTML;
	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
			document.getElementById("toRemoveTaijutsu").innerHTML = "0";
			document.getElementById("toRemoveHeart").innerHTML = "0";
			document.getElementById("toRemoveEnergy").innerHTML = "0";
			document.getElementById("toRemoveSpirit").innerHTML = "0";
			document.getElementById("chooseRandom").style.display = "none";
	   } 
	}

	xhttp.open("POST", "AbilityActions?action=cancelRandom&tai="+tai+"&heart="+heart+"&energy="+energy+"&spirit="+spirit+"&random="+random, true);
	xhttp.send(null);
}

function minus(nature) {
	
	var toRemove = document.getElementById("toRemove"+nature).innerHTML.trim();

	
	if (toRemove != "0") {
		
		const xhttp = new XMLHttpRequest();

		xhttp.onload = function() {
		   if (xhttp.status === 200 && xhttp.readyState === 4) {
			   
			   var response = this.responseText.split("-");
			  
			   document.getElementById("current"+nature).innerHTML = response[0];
			   document.getElementById("toRemove"+nature).innerHTML = response[1];
			   document.getElementById("nRandom").innerHTML = response[2];
		   } 
		}

		xhttp.open("POST", "AbilityActions?action=minus&nature="+nature+"&toRemove="+toRemove, true);
		xhttp.send(null);
	}
}
function plus(nature) {
	
	if (document.getElementById("current"+nature).innerHTML.trim() != "0"
		&& document.getElementById("nRandom").innerHTML.trim() != "0"	
	) {
		
		var toRemove = document.getElementById("toRemove"+nature).innerHTML.trim();
		
		const xhttp = new XMLHttpRequest();

		xhttp.onload = function() {
		   if (xhttp.status === 200 && xhttp.readyState === 4) {
			   
			   var response = this.responseText.split("-");
			  
			   document.getElementById("current"+nature).innerHTML = response[0];
			   document.getElementById("toRemove"+nature).innerHTML = response[1];
			   document.getElementById("nRandom").innerHTML = response[2];
		   } 
		}

		xhttp.open("POST", "AbilityActions?action=plus&nature="+nature+"&toRemove="+toRemove, true);
		xhttp.send(null);
	}
	
}

function updateCurrentRandom() {
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   
		   var response = this.responseText.split("-");
		   
		   document.getElementById("currentTaijutsu").innerHTML = response[0];
		   document.getElementById("currentHeart").innerHTML = response[1];
		   document.getElementById("currentEnergy").innerHTML = response[2];
		   document.getElementById("currentSpirit").innerHTML = response[3];

		   updateRandomAbilityImages();
		   
		} 
	}

	xhttp.open("POST", "AbilityActions?action=currentNature", true);
	xhttp.send(null);
}

function updateRandomAbilityImages() {

	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   document.getElementById("showAbilitiesEndTurn").innerHTML = this.responseText;
		  // document.getElementById("showAbilitiesEndTurn").insertAdjacentHTML('beforeend', this.responseText);
		   
		   randomBeforeTurn();
		   
		} 
	}

	xhttp.open("POST", "AbilityActions?action=endTurnAbilities&allAbilitiesID="+allAbilitiesID, true);
	xhttp.send(null);
	
}

function randomBeforeTurn() {
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   
		   document.getElementById("nRandom").innerHTML = this.responseText;
		   document.getElementById("chooseRandom").style.display = "block";
		  
		} 
	}

	xhttp.open("POST", "AbilityActions?action=beforeTurn", true);
	xhttp.send(null);

	//storeAbilities();
}

function exchangeNature() {
	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   
		   var response = this.responseText.split("-");
		   if (response[4].trim()=="true") {
			   document.getElementById("currentTaijutsu_exchange").innerHTML = response[0];
			   document.getElementById("currentHeart_exchange").innerHTML = response[1];
			   document.getElementById("currentEnergy_exchange").innerHTML = response[2];
			   document.getElementById("currentSpirit_exchange").innerHTML = response[3];
			   document.getElementById("nRandom_exchange").innerHTML = "1";
			   document.getElementById("aboveBelow").innerHTML = "RANDOM NATURE ABOVE";

			   document.getElementById("exchangeRandom").style.display = "block";
		   }
		   
		  
		   
		} 
	}

	xhttp.open("POST", "AbilityActions?action=currentNature", true);
	xhttp.send(null);
	
}

function exchangeFor(nature) {
	
	document.getElementById("exchangeTaijutsu").style.opacity = 0.5;
	document.getElementById("exchangeHeart").style.opacity = 0.5;
	document.getElementById("exchangeEnergy").style.opacity = 0.5;
	document.getElementById("exchangeSpirit").style.opacity = 0.5;
	
	document.getElementById("exchange"+nature).style.opacity = 1;
	
	if (document.getElementById("aboveBelow").innerHTML == "RANDOM NATURE ABOVE") {
		document.getElementById("nRandom_exchange").innerHTML = "5";
		document.getElementById("aboveBelow").innerHTML = "RANDOM NATURE BELOW";
	}
	
}

function minusExchange(nature) {
	if (document.getElementById("aboveBelow").innerHTML == "RANDOM NATURE BELOW") {
		if (
			document.getElementById("toRemove"+nature+"_exchange").innerHTML.trim() != "0"
			) {
			
			var nLeft = document.getElementById("nRandom_exchange").innerHTML.trim();
			//TODO request here
			var toRemove = document.getElementById("toRemove"+nature+"_exchange").innerHTML.trim();
			
			const xhttp = new XMLHttpRequest();

			xhttp.onload = function() {
			   if (xhttp.status === 200 && xhttp.readyState === 4) {
				   
				   var response = this.responseText.split("-");
				  
				   document.getElementById("current"+nature+"_exchange").innerHTML = response[0];
				   document.getElementById("toRemove"+nature+"_exchange").innerHTML = response[1];
				   document.getElementById("nRandom_exchange").innerHTML = response[2];
			   } 
			}

			xhttp.open("POST", "AbilityActions?action=minusExchange&nature="+nature+"&toRemove="+toRemove+"&nLeft="+nLeft, true);
			xhttp.send(null);
		}
	}
}
function plusExchange(nature) {
	if (document.getElementById("aboveBelow").innerHTML == "RANDOM NATURE BELOW") {
		if (
			document.getElementById("nRandom_exchange").innerHTML != "0" &&
			document.getElementById("current"+nature+"_exchange").innerHTML.trim() != "0"
			) {
			
			var nLeft = document.getElementById("nRandom_exchange").innerHTML .trim();
			//TODO request here
			var toRemove = document.getElementById("toRemove"+nature+"_exchange").innerHTML.trim();
			
			const xhttp = new XMLHttpRequest();

			xhttp.onload = function() {
			   if (xhttp.status === 200 && xhttp.readyState === 4) {
				   
				   var response = this.responseText.split("-");
				  
				   document.getElementById("current"+nature+"_exchange").innerHTML = response[0];
				   document.getElementById("toRemove"+nature+"_exchange").innerHTML = response[1];
				   document.getElementById("nRandom_exchange").innerHTML = response[2];
			   } 
			}

			xhttp.open("POST", "AbilityActions?action=plusExchange&nature="+nature+"&toRemove="+toRemove+"&nLeft="+nLeft, true);
			xhttp.send(null);
		}
	}
}

function confirmExchange() {

	if (document.getElementById("nRandom_exchange").innerHTML == "0") {
		
		////var tai = document.getElementById("currentTaijutsu_exchange").innerHTML.trim();
	    //var heart = document.getElementById("currentHeart_exchange").innerHTML.trim();
	   // var energy = document.getElementById("currentEnergy_exchange").innerHTML.trim();
	   // var spirit = document.getElementById("currentSpirit_exchange").innerHTML.trim();
		
	    var selected = null;
	    if (document.getElementById("exchangeTaijutsu").style.opacity == 1) {
	    	selected = "taijutsu";
	    }
	    else if (document.getElementById("exchangeHeart").style.opacity == 1) {
	    	selected = "heart";
	    }
	    else if (document.getElementById("exchangeEnergy").style.opacity == 1) {
	    	selected = "energy";
	    }
	    else if (document.getElementById("exchangeSpirit").style.opacity == 1) {
	    	selected = "spirit";
	    }
	   
		const xhttp = new XMLHttpRequest();

		xhttp.onload = function() {
		   if (xhttp.status === 200 && xhttp.readyState === 4) {
			   
			   //var response = this.responseText.split("-");
			  document.getElementById("natures").innerHTML = this.responseText;
			  document.getElementById("exchangeRandom").style.display = "none";
			  canUseAbilityNature();
			  
		   } 
		}

		xhttp.open("POST", "AbilityActions?action=confirmExchange&selected="+selected, true);
		xhttp.send(null);
	}
}

function cancelExchange() {
	 var tai = document.getElementById("toRemoveTaijutsu_exchange").innerHTML.trim();
	 var heart = document.getElementById("toRemoveHeart_exchange").innerHTML.trim();
	 var energy = document.getElementById("toRemoveEnergy_exchange").innerHTML.trim();
	 var spirit = document.getElementById("toRemoveSpirit_exchange").innerHTML.trim();
	 //var random = document.getElementById("nRandom_exchange").innerHTML;
	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		    document.getElementById("exchangeTaijutsu").style.opacity = 0.5;
			document.getElementById("exchangeHeart").style.opacity = 0.5;
			document.getElementById("exchangeEnergy").style.opacity = 0.5;
			document.getElementById("exchangeSpirit").style.opacity = 0.5;
			document.getElementById("toRemoveTaijutsu_exchange").innerHTML = "0";
			document.getElementById("toRemoveHeart_exchange").innerHTML = "0";
			document.getElementById("toRemoveEnergy_exchange").innerHTML = "0";
			document.getElementById("toRemoveSpirit_exchange").innerHTML = "0";
			document.getElementById("exchangeRandom").style.display = "none";
	   } 
	}

	xhttp.open("POST", "AbilityActions?action=cancelExchange&tai="+tai+"&heart="+heart+"&energy="+energy+"&spirit="+spirit, true);
	xhttp.send(null);

}
function canUseAbilityNature() {
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   
		   var response = this.responseText.split("-");
		   
//		   var skill0 = document.getElementsByClassName("skillimg0");
//		   var skill1 = document.getElementsByClassName("skillimg1");
//		   var skill2 = document.getElementsByClassName("skillimg2");
//		   var skill3 = document.getElementsByClassName("skillimg3");
		   
		   for(let i = 0; i<response.length; i++) {
			   if (i<4) { //char1
				   var x = document.getElementsByClassName("skillimg"+i);
				   if (response[i].trim()=="false") {
					   x[1].classList.add('disabledNature');
				   }
				   else {
					   x[1].classList.remove('disabledNature');
				   }
			   }
			   else if (i<8) {//char2
				   var x = document.getElementsByClassName("skillimg"+(i-4));
				   if (response[i].trim()=="false") {
					   x[3].classList.add('disabledNature');
				   }
				   else {
					   x[3].classList.remove('disabledNature');
				   }
			   }
			   else if (i<12){//char3
				   var x = document.getElementsByClassName("skillimg"+(i-8));
				   if (response[i].trim()=="false") {
					   x[5].classList.add('disabledNature');
				   }
				   else {
					   x[5].classList.remove('disabledNature');
				   }
			   }
		   }

		} 
	}

	xhttp.open("POST", "AbilityActions?action=abilityHasNature", true);
	xhttp.send(null);
}

function defineTurns(turn) {
	
	var opp = document.getElementsByClassName ("opp_turn");
	var opp_text = document.getElementsByClassName ("opp_text");
	var my = document.getElementsByClassName ("my_turn");
	
	if (turn==true) {
		
		canUseAbilityNature();	
		
		
		for (var i = 0; i < opp.length; i++) {
			opp[i].style.display="none";
		}
		for (var i = 0; i < opp_text.length; i++) {
			opp_text[i].style.display="none";
		}
		for (var i = 0; i < my.length; i++) {
			my[i].style.display="block";
		}
	}
	else if (turn==false) {

		for (var i = 0; i < opp.length; i++) {
			opp[i].style.display="block";
		}
		for (var i = 0; i < opp_text.length; i++) {
			opp_text[i].style.display="block";
		}
		for (var i = 0; i < my.length; i++) {
			my[i].style.display="none";
		}
		
		lockSemaphore();
	}
	
}

function lockSemaphore() {
	
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {

		   if (this.responseText.split("break")[0].trim()=="winner") {
			   
			   var response = this.responseText.split("break");
			   
			   if (canWriteTurn) {
				   canWriteTurn = false;
				   var against = document.getElementById("textWinnerQuick").innerHTML;
				   document.getElementById("textWinnerQuick").innerHTML = response[1] + against + response[2];
			   }
			  
			   
			   winner();
		   }
		   else if (this.responseText.split("break")[0].trim()=="loser") {
			   

			   loser();
		   }
		   else {
			   var x = this.responseText.split("break");
			
			   document.getElementById("hpAlly0").innerHTML = x[0];
			   document.getElementById("hpAlly1").innerHTML = x[1];
			   document.getElementById("hpAlly2").innerHTML = x[2];
		       document.getElementById("hpEnemy1").innerHTML = x[3];
			   document.getElementById("hpEnemy2").innerHTML = x[4];
			   document.getElementById("hpEnemy3").innerHTML = x[5];
			   
			   document.getElementById("effectsAlly0").innerHTML = x[6];
			   document.getElementById("effectsAlly1").innerHTML = x[7];
			   document.getElementById("effectsAlly2").innerHTML = x[8];
			   document.getElementById("effectsEnemy1").innerHTML = x[9];
			   document.getElementById("effectsEnemy2").innerHTML = x[10];
			   document.getElementById("effectsEnemy3").innerHTML = x[11];
			   
			   document.getElementById("natures").innerHTML = x[12];
			   
			   var charIsStunned = x[13].split("-");
			   
			   if (charIsStunned[0].trim()=="true") {
				   $('#allSkillsChar0 img').addClass('disabled');
			   }
			   if (charIsStunned[1].trim()=="true") {
				   $('#allSkillsChar1 img').addClass('disabled');
			   }
			   if (charIsStunned[2].trim()=="true") {
				   $('#allSkillsChar2 img').addClass('disabled');
			   }
			   
			   var cooldown1 = x[14].split("-");
			   for (let i = 0; i<cooldown1.length; i++) {
				   document.getElementById("cooldown0-"+i).innerHTML = cooldown1[i].trim();
				   if (cooldown1[i].trim()!="0") {
					   document.getElementById("cooldown0-"+i).style.display = "block";
					   document.getElementById("cooldown0-"+i).parentElement.classList.add('disabledCooldown');
				   }
				   else {
					   document.getElementById("cooldown0-"+i).style.display = "none";
					   document.getElementById("cooldown0-"+i).parentElement.classList.remove('disabledCooldown');
				   }
				  
			   }
			   var cooldown2 = x[15].split("-");
			   for (let i = 0; i<cooldown2.length; i++) {
				   document.getElementById("cooldown1-"+i).innerHTML = cooldown2[i].trim();
				   if (cooldown2[i].trim()!="0") {
					   document.getElementById("cooldown1-"+i).style.display = "block";
					   document.getElementById("cooldown1-"+i).parentElement.classList.add('disabledCooldown');
				   }
				   else {
					   document.getElementById("cooldown1-"+i).style.display = "none";
					   document.getElementById("cooldown1-"+i).parentElement.classList.remove('disabledCooldown');
				   }
			   }
			   var cooldown3 = x[16].split("-");
			   for (let i = 0; i<cooldown3.length; i++) {
				   document.getElementById("cooldown2-"+i).innerHTML = cooldown3[i].trim();
				   if (cooldown3[i].trim()!="0") {
					   document.getElementById("cooldown2-"+i).style.display = "block";
					   document.getElementById("cooldown2-"+i).parentElement.classList.add('disabledCooldown');
				   }
				   else {
					   document.getElementById("cooldown2-"+i).style.display = "none";
					   document.getElementById("cooldown2-"+i).parentElement.classList.remove('disabledCooldown');
				   }
			   }

			   var deadAlive = x[17].split("-");
			   for (let i = 0; i < 3; i++) {
				   if (deadAlive[i].trim() == "dead") {
					   document.getElementById("soundDead").play();

					   document.getElementById("dead_0"+i).src = "./battle/dead.png";
					   $("#allSkillsChar"+i+" img").addClass('disabledDead');
					   document.getElementById("effectsAlly"+i).innerHTML = "";
					  // document.getElementById("allSkillsChar"+i).classList.remove("disabledCooldown");
						for (let cd = 0; cd<4; cd++) {
							document.getElementById("cooldown"+i+"-"+cd).innerHTML = "";
						}
				   }
				   if (deadAlive[i+3].trim() == "dead") {
					   document.getElementById("soundDead").play();
					   document.getElementById( "dead_1"+(i+1) ).src = "./battle/dead.png";
					   document.getElementById("effectsEnemy"+(i+1) ).innerHTML = "";
				   }
			   }
			   defineTurns(true);
			   //reset Timer
			   document.getElementById("timeBar").style.width = 191;
		   }
		   
		   allAbilitiesUsed = [];
		   allCharsUsedSkill = [];
		   allTargets = [];
		   allAllyEnemy = [];
		   allAbilitiesID = [];
		} 
	}
	xhttp.open("POST", "InGame?metodo=lock", true);  // assincrono
	xhttp.send(null);

}

function storeAbilities() {
	if (document.getElementById("nRandom").innerHTML.trim()=="0") {
		const xhttp = new XMLHttpRequest();

		xhttp.onload = function() {
		   if (xhttp.status === 200 && xhttp.readyState === 4) {
			   endTurn();
			} 
		}

		xhttp.open("POST", "AbilityActions?action=saveAbilities&allAbilitiesUsed="+allAbilitiesUsed+"&allCharsUsedSkill="+allCharsUsedSkill+
				"&allTargets="+allTargets+"&allAllyEnemy="+allAllyEnemy+"&allAbilitiesID="+allAbilitiesID, true);
		xhttp.send(null);
	}
	
}

function resetToRemove() {
	document.getElementById("toRemoveTaijutsu").innerHTML = "0";
	document.getElementById("toRemoveHeart").innerHTML = "0";
	document.getElementById("toRemoveEnergy").innerHTML = "0";
	document.getElementById("toRemoveSpirit").innerHTML = "0";
}

function endTurn() {

	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		  
		   document.getElementById("soundEndTurn").play();
		   
		   resetToRemove();
		   document.getElementById("chooseRandom").style.display = "none";

		   removeAllTargetClick();
		   cancelAbility(0, false);
		   cancelAbility(1, false);
		   cancelAbility(2, false);
		   allAbilitiesUsed = [];
		   allCharsUsedSkill = [];
		   allTargets = [];
		   allAllyEnemy = [];
		   allAbilitiesID = [];
		   defineTurns(false);
		   //reset Timer
		   document.getElementById("timeBar").style.width = 192;
		   
		   var x = this.responseText.split("break");
		  
		   document.getElementById("hpAlly0").innerHTML = x[0];
		   document.getElementById("hpAlly1").innerHTML = x[1];
		   document.getElementById("hpAlly2").innerHTML = x[2];
	       document.getElementById("hpEnemy1").innerHTML = x[3];
		   document.getElementById("hpEnemy2").innerHTML = x[4];
		   document.getElementById("hpEnemy3").innerHTML = x[5];
		   
		   document.getElementById("effectsAlly0").innerHTML = x[6];
		   document.getElementById("effectsAlly1").innerHTML = x[7];
		   document.getElementById("effectsAlly2").innerHTML = x[8];
		   document.getElementById("effectsEnemy1").innerHTML = x[9];
		   document.getElementById("effectsEnemy2").innerHTML = x[10];
		   document.getElementById("effectsEnemy3").innerHTML = x[11];
		   
		   var deadAlive = x[12].split("-");
		   for (let i = 0; i < 3; i++) {
			   if (deadAlive[i].trim() == "dead") {
				   document.getElementById("soundDead").play();

				   document.getElementById("dead_0"+i).src = "./battle/dead.png";
				   document.getElementById("effectsAlly"+i).innerHTML = "";

			   }
			   if (deadAlive[i+3].trim() == "dead") {
				   document.getElementById("soundDead").play();

				   document.getElementById( "dead_1"+(i+1) ).src = "./battle/dead.png";
				   document.getElementById("effectsEnemy"+(i+1) ).innerHTML = "";
			   }
		   }
  
		} 
	}
	//xhttp.open("POST", "InGame?metodo=unlock&allAbilitiesUsed="+allAbilitiesUsed+"&allCharsUsedSkill="+allCharsUsedSkill+
	//		"&allTargets="+allTargets+"&allAllyEnemy="+allAllyEnemy+"&allAbilitiesID="+allAbilitiesID, true);  // assincrono
	xhttp.open("POST", "InGame?metodo=unlock", true);
	xhttp.send(null);
}

function loser() {
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   
		   document.getElementById("soundLose").play();
		   
		   var response = this.responseText.split("break");
		   
		   if (canWriteTurn) {
			   canWriteTurn = false;
			   var against = document.getElementById("textLoserQuick").innerHTML;
			   document.getElementById("textLoserQuick").innerHTML = response[1] + against + response[2];
					   
		   }
		  
		    document.getElementById("oppTurnDisable").style.display="none";
			document.getElementById("passTurn").style.display="none";
			document.getElementById("winnerTurn").style.display="none";
			document.getElementById("winnerQuick").style.display="none";
			document.getElementById("loserTurn").style.display="block";
			document.getElementById("loserQuick").style.display="block";
		} 
	}
	xhttp.open("POST", "InGame?metodo=loser", true);  // assincrono
	xhttp.send(null);
}


function winner() {
	
	document.getElementById("soundWin").play();
	
	document.getElementById("oppTurnDisable").style.display="none";
	document.getElementById("passTurn").style.display="none";
	document.getElementById("loserTurn").style.display="none";
	document.getElementById("loserQuick").style.display="none";
	document.getElementById("winnerTurn").style.display="block";
	document.getElementById("winnerQuick").style.display="block";
}

function redirectSelection() {
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   window.location.href = "selection.jsp";
		} 
	}
	xhttp.open("POST", "InGame?metodo=remove", true);  // assincrono
	xhttp.send(null);
	
}


function displayNones() {
	document.getElementById("player0").style.display="none";
	document.getElementById("player1").style.display="none";
	
	for (let i = 0; i < 999; i++) {
		if (document.getElementById("character"+i)!=null) {
			document.getElementById("character"+i).style.display="none";
		}
		if (document.getElementById("ability"+i)!=null) {
			document.getElementById("ability"+i).style.display="none";
		}
	}
}

function hasEffect(allyEnemy, charPos) {
	
	if (allyEnemy=="enemy") {
		if (charPos==1) {
			if($('.mc_char_10').find('.chooseEnemy').length !== 0) {
				return true;
			}
			else {
				return false;
			}
		}
		if (charPos==2) {
			if($('.mc_char_11').find('.chooseEnemy').length !== 0) {
				return true;
			}
			else {
				return false;
			}
		}
		if (charPos==3) {
			if($('.mc_char_12').find('.chooseEnemy').length !== 0) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	else if (allyEnemy=="ally") {
		if (charPos==0) {
			if($('.mc_char_00').find('.choose').length !== 0) {
				return true;
			}
			else {
				return false;
			}
		}
		if (charPos==1) {
			if($('.mc_char_01').find('.choose').length !== 0) {
				return true;
			}
			else {
				return false;
			}
		}
		if (charPos==2) {
			if($('.mc_char_02').find('.choose').length !== 0) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	console.log("NAO VERIFICOU");
	return false;
}

function pushAbilityInfo(abilityUsedPos, charPosUsedSkill, allyEnemy, charPos, abilityClicked ) {
	allAbilitiesUsed.push(abilityUsedPos);
	allCharsUsedSkill.push(charPosUsedSkill);
	allAllyEnemy.push(allyEnemy);
	allTargets.push(charPos);
	var username = document.getElementById("currentPlayer").innerHTML.trim();
	allAbilitiesID.push(abilityClicked+"-"+username);
}

function characterFooterInfo(id, allyEnemy, charPos) {

	document.getElementById("soundClickEnemy").play();

	
	if (hasEffect(allyEnemy, charPos)) {
		const xhttp = new XMLHttpRequest();

		xhttp.onload = function() {
		   if (xhttp.status === 200 && xhttp.readyState === 4) {
			   
			   if (this.responseText.trim()!="nada") {
			
				   var response = this.responseText.split("break");
				   if (response[2].trim()=="enemy") {
						document.getElementById("effectsEnemy"+charPos).insertAdjacentHTML('beforeend',response[0]);
						pushAbilityInfo(abilityUsedPos, charPosUsedSkill, allyEnemy, charPos, abilityClicked )
				   }
				   else if (response[2].trim()=="allEnemies") {			
					   for (let i = 1; i < 4; i++) {
						   if (hasEffect(allyEnemy, i)) {
								document.getElementById("effectsEnemy"+i).insertAdjacentHTML('beforeend',response[0]);
								pushAbilityInfo(abilityUsedPos, charPosUsedSkill, allyEnemy, i, abilityClicked );
						   }
					   }	
					
					   //document.getElementById("effectsEnemy1").insertAdjacentHTML('beforeend',response[0]);
					  // document.getElementById("effectsEnemy2").insertAdjacentHTML('beforeend',response[0]);
					   //document.getElementById("effectsEnemy3").insertAdjacentHTML('beforeend',response[0]);

				   }
				   else if (response[2].trim()=="ally" || response[2].trim()=="allyORself" || response[2].trim()=="self") {
					   document.getElementById("effectsAlly"+charPos).insertAdjacentHTML('beforeend', response[0]);
					   pushAbilityInfo(abilityUsedPos, charPosUsedSkill, allyEnemy, charPos, abilityClicked )
				   }
				   else if (response[2].trim()=="allTeam") {
					   for (let i = 0; i < 3; i++) {
						   if (hasEffect(allyEnemy, i)) {
								document.getElementById("effectsAlly"+i).insertAdjacentHTML('beforeend',response[0]);
								pushAbilityInfo(abilityUsedPos, charPosUsedSkill, allyEnemy, i, abilityClicked );
						   }
					   }	
					   //document.getElementById("effectsAlly0").insertAdjacentHTML('beforeend', response[0]);
					   //document.getElementById("effectsAlly1").insertAdjacentHTML('beforeend', response[0]);
					   //document.getElementById("effectsAlly2").insertAdjacentHTML('beforeend', response[0]);
						//pushAbilityInfo(abilityUsedPos, charPosUsedSkill, allyEnemy, charPos, abilityClicked );

				   }
				   
				   document.getElementById("selected"+charPosUsedSkill).innerHTML = "<img src='ViewAbility?id="+abilityClicked+"' id='abilitySelected"+abilityClicked+"'>";
				   
				   if (charPosUsedSkill==0) {
					   $('#allSkillsChar0 img').addClass('disabled');
				   }
				   else if (charPosUsedSkill==1) {
					   $('#allSkillsChar1 img').addClass('disabled');
				   }
				   else if (charPosUsedSkill==2) {
					   $('#allSkillsChar2 img').addClass('disabled');
				   }
				   
				   document.getElementById("natures").innerHTML = response[1];
				   canUseAbilityNature();
				   
				   removeAllTargetClick();
				   abilityClicked = null;
				   charPosUsedSkill = null;
				   abilityUsedPos = null;
		
			   }
			} 
		}
		xhttp.open("POST", "AbilityActions?action=applyAbility&abilityUsedID="+abilityClicked+"&allyEnemy="+allyEnemy, true);  // assincrono
		xhttp.send(null);
	}
	else {
		displayNones();
		document.getElementById("character"+id).style.display="block";
		
		removeAllTargetClick();
		abilityClicked = null;
		charPosUsedSkill = null;
		abilityUsedPos = null;
	}
	
	
	
}


function cancelArrayFirst(pos, parent) {
	
	 var ability = parent.getElementsByTagName('img')[0].id;
	
	 if (ability!="selectedNone") {
		 var username = document.getElementById("currentPlayer").innerHTML.trim();
		 
		 for (let j = 0; j<3;j++) { //por causa dos aoe
			 for (let i = 0; i < allAbilitiesUsed.length; i++) {
				 
				 if ("abilitySelected"+allAbilitiesID[i]==ability+"-"+username) {
					
					 allAbilitiesUsed.splice(i, 1);
					 allCharsUsedSkill.splice(i, 1);
					 allTargets.splice(i, 1);
					 allAllyEnemy.splice(i, 1);
					 allAbilitiesID.splice(i, 1);
				 }
			 }
		 }

		 cancelAbility(pos, true);
	 }
	
}

function cancelAbility(pos, regainNature) {
	   
	var id = document.getElementById("selected"+pos).getElementsByTagName('img')[0].id;
	document.getElementById("selected"+pos).innerHTML = "<a><img src='battle/skillact.png'  id='selectedNone'></a>";

   if (pos==0) {
	  $('#allSkillsChar0 img').removeClass('disabled');
   }
   else if (pos==1) {
	   $('#allSkillsChar1 img').removeClass('disabled');
   }
   else if (pos==2) {
	   $('#allSkillsChar2 img').removeClass('disabled');
   }

   
   var username = document.getElementById("currentPlayer").innerHTML.trim();
   
   document.querySelectorAll('.effects_border0').forEach(function(e){
	  var actives = e.getElementsByTagName('img')[0].id;

	  if (actives.split(username)[1] == id.split("abilitySelected")[1]) {
	
		  e.remove();
	  }
   });

   document.querySelectorAll('.effects_border1').forEach(function(e){
	  var actives = e.getElementsByTagName('img')[0].id;
	  if (actives.split(username)[1] == id.split("abilitySelected")[1]) {
		  e.remove();
	  }
   });

   if (regainNature==true && id.split("Selected")[1]!="" && id.split("Selected")[1]!="undefined" &&
		   id.split("Selected")[1]!=null && id.split("Selected")[1]!="null") {
	   
	    const xhttp = new XMLHttpRequest();

		xhttp.onload = function() {
		   if (xhttp.status === 200 && xhttp.readyState === 4) {
			   
			   document.getElementById("natures").innerHTML = this.responseText;
			   canUseAbilityNature();
			} 
		}
		xhttp.open("POST", "AbilityActions?action=cancelAbility&id="+id.split("Selected")[1], true);  // assincrono
		xhttp.send(null);
		
   }
   
	 
}


function abilityClick(abilityID, selfChar, abilityPos) {
	//   $("#allSkillsChar"+i+" img").addClass('disabledDead');
	//!document.getElementById("allSkillsChar"+selfChar+" img").classList.contains('disabledDead') &&

	document.getElementById("soundClickSkill").play();

	
	if (
		!document.getElementById("imageClickMaybe"+abilityID).classList.contains('disabledDead') &&
		!document.getElementById("imageClickMaybe"+abilityID).classList.contains('disabled') &&
		!document.getElementById("cooldown"+selfChar+"-"+abilityPos).parentElement.classList.contains('disabledCooldown') &&
		!document.getElementById("cooldown"+selfChar+"-"+abilityPos).parentElement.classList.contains('disabledNature')
	) {
		
		var imgIDselected = $.map($("#selected"+selfChar+" > img"), div => div.id);
		
		if(imgIDselected[0] == "selectedNone" || imgIDselected[0] == null) {
			
			const xhttp = new XMLHttpRequest();

			xhttp.onload = function() {
				
			   if (xhttp.status === 200 && xhttp.readyState === 4) {
				   
				   removeAllTargetClick();
				   
				   var divide = this.responseText.split("break");
				   
				   //var answer = this.responseText.split("-");
				   var answer = divide[0].split("-");
				   var deadAlive = divide[1].split("-");
				   
				   var element = document.createElement("div");
				   element.classList.add("choose");
				   
				   if (answer[0].trim()=="self") {
						document.getElementById("ally"+selfChar).appendChild(element);
				   }
				   else if (answer[0].trim()=="allTeam" || answer[0].trim()=="allyORself") {
					   
					   for (let i = 0; i < 3; i++) {
						   if (deadAlive[i].trim()!="true") {
							   document.getElementById("ally"+i).appendChild(element.cloneNode(true));
						   }
					   }
					   
					   /*if (deadAlive[0].trim()!="true") {
							document.getElementById("ally0").appendChild(element.cloneNode(true));
					   }
					   if (deadAlive[1].trim()!="true") {
							document.getElementById("ally1").appendChild(element.cloneNode(true));
					   }
					   if (deadAlive[2].trim()!="true") {
							document.getElementById("ally2").appendChild(element.cloneNode(true));
					   }*/
				   }
				   else if (answer[0].trim()=="ally" || answer[0].trim()=="allAllies") {
					   for (let i = 0; i < 3; i++) {
						   if (i!=selfChar && deadAlive[i].trim()!="true") {
							   document.getElementById("ally"+i).appendChild(element.cloneNode(true));
						   }
					   }
					   
				   }
				   else if (answer[0].trim()=="enemy" || answer[0].trim()=="allEnemies") {
					   var elementEnemy = document.createElement("div");
					   elementEnemy.classList.add("chooseEnemy");
					   
					   for (let i = 1; i < 4; i++) {
						   if (answer[i].trim()=="false" && deadAlive[i+2].trim()!="true") {
							   document.getElementById("enemy"+(i-1)).appendChild(elementEnemy.cloneNode(true));
						   }
					   }
					  /* if (answer[1].trim()=="false" && deadAlive[3]!="true") {
						   document.getElementById("enemy0").appendChild(elementEnemy.cloneNode(true));
					   }
					   if (answer[2].trim()=="false" && deadAlive[4]!="true") {
						   document.getElementById("enemy1").appendChild(elementEnemy.cloneNode(true));
					   }
					   if (answer[3].trim()=="false" && deadAlive[5]!="true") {
						   document.getElementById("enemy2").appendChild(elementEnemy.cloneNode(true));
					   }*/
				   }
				   else if (answer[0].trim()=="all") {
					   for (let i = 0; i < 3; i++) {
						   if (deadAlive[i].trim()!="true") {
							   document.getElementById("ally"+i).appendChild(element.cloneNode(true));
						   }
					   }
					   //document.getElementById("ally0").appendChild(element.cloneNode(true));
					   //document.getElementById("ally1").appendChild(element.cloneNode(true));
					   //document.getElementById("ally2").appendChild(element.cloneNode(true));
					   var elementEnemy = document.createElement("div");
					   elementEnemy.classList.add("chooseEnemy");
					   
					   for (let i = 1; i < 4; i++) {
						   if (answer[i].trim()=="false" && deadAlive[2].trim()!="true") {
							   document.getElementById("enemy"+(i-1)).appendChild(elementEnemy.cloneNode(true));
						   }
					   }
					   /*if () {
						   document.getElementById("enemy0").appendChild(elementEnemy.cloneNode(true));
					   }
					   if (answer[2].trim()=="false") {
						   document.getElementById("enemy1").appendChild(elementEnemy.cloneNode(true));
					   }
					   if (answer[3].trim()=="false") {
						   document.getElementById("enemy2").appendChild(elementEnemy.cloneNode(true));
					   }*/
				   }
				   
				   
				   
				   abilityClicked = abilityID;
				   charPosUsedSkill = selfChar;
				   abilityUsedPos = abilityPos;
				} 
			}
			xhttp.open("POST", "AbilityActions?action=seeTarget&selfChar="+selfChar+"&abilityPos="+abilityPos, true);  // assincrono
			xhttp.send(null);
			
		}
	}
	else {
		removeAllTargetClick();
	}
	

	abilityFooterInfo(abilityID, selfChar, abilityPos);
	
	
}

function abilityFooterInfo(abilityID, selfChar, abilityPos) {
	const xhttp = new XMLHttpRequest();

	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {

		   document.getElementById("natures"+abilityID).innerHTML = this.responseText;
		   displayNones();
		   document.getElementById("ability"+abilityID).style.display="block";
		} 
	}
	xhttp.open("POST", "AbilityActions?action=seeNatureCost&selfChar="+selfChar+"&abilityPos="+abilityPos, true);  // assincrono
	xhttp.send(null);
	
}

function removeAllTargetClick() {

	document.querySelectorAll('.choose').forEach(e => e.remove());
	document.querySelectorAll('.chooseEnemy').forEach(e => e.remove());

}

function playerFooterInfo(my_opp) {
	abilityClicked = null;
	removeAllTargetClick();
	displayNones();

	if (my_opp=="my") {
		document.getElementById("player0").style.display="block";
	} 
	else if (my_opp=="opp") {
		document.getElementById("player1").style.display="block";
	}
	
	
}

function seeActiveSkillEnemy(activeSkill) {
	
	var id = "tooltiptext"+activeSkill;
	var skill = document.getElementsByClassName ("tooltiptext1");
	for (var i = 0; i < skill.length; i++) {
		if (id==skill[i].id) {
			skill[i].style.visibility = "visible" ;
		}
	}
}

function hideActiveSkillEnemy() {
	var skill = document.getElementsByClassName ("tooltiptext1");
	for (var i = 0; i < skill.length; i++) {
		skill[i].style.visibility = "hidden" ;
	}
}

function seeActiveSkill(activeSkill) {
	
	var id = "tooltiptext"+activeSkill;
	var skill = document.getElementsByClassName ("tooltiptext");
	for (var i = 0; i < skill.length; i++) {
		if (id==skill[i].id) {
			skill[i].style.visibility = "visible" ;
		}
	}
}

function hideActiveSkill() {
	var skill = document.getElementsByClassName ("tooltiptext");
	for (var i = 0; i < skill.length; i++) {
		skill[i].style.visibility = "hidden" ;
	}
}

function timeOver() {
	
	if (document.getElementById("passTurn").style.display = "block") {
		endTurn();
	}
	
}

function updateTime() {

	var currentTime = document.getElementById("timeBar").style.width;
	
	if (currentTime <= 0 || currentTime.length<=0) {
		timeOver();
	}
	else {
		 const xhttp = new XMLHttpRequest();

			xhttp.onload = function() {
			   if (xhttp.status === 200 && xhttp.readyState === 4) {
				   document.getElementById("updateTime").innerHTML = this.responseText;
				} 
			}
			xhttp.open("POST", "Timer?action=reduceTimer", true);  // assincrono
			xhttp.send(null);
	}

}

function hats() {
	const xhttp = new XMLHttpRequest();
	
	xhttp.onload = function() {
	   if (xhttp.status === 200 && xhttp.readyState === 4) {
		   
		   var response = this.responseText.split("player");

		   const thisPlayerRanks = document.getElementsByClassName("rankAlly");
		   for (let i = 0; i < thisPlayerRanks.length; i++) {
			   thisPlayerRanks[i].innerHTML = response[0];
		   }
		   
		   document.getElementById("rankName1").innerHTML = response[1].toUpperCase();
		   
		   const oppPlayerRanks = document.getElementsByClassName("rankEnemy");
		   for (let i = 0; i < oppPlayerRanks.length; i++) {
			   oppPlayerRanks[i].innerHTML = response[2];
		
			   if (response[3].toUpperCase() == "admiral".toUpperCase()) {
				   oppPlayerRanks[i].classList.add('revert2');
			   }
			  

		   }
		   
		   document.getElementById("rankName2").innerHTML = response[3].toUpperCase();
		   
		} 
	}
	xhttp.open("POST", "ViewRanking?action=hat", true);  // assincrono
	xhttp.send(null);
}

function changeVolume(value) {
	document.getElementById("soundClickEnemy").volume = value*0.1;
	document.getElementById("soundClickSkill").volume = value*0.1;
	document.getElementById("soundDead").volume = value*0.1;
	document.getElementById("soundEndTurn").volume = value*0.1;
	document.getElementById("soundLose").volume = value*0.1;
	document.getElementById("soundWin").volume = value*0.1;
}


