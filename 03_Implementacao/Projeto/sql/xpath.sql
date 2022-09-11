use animearena;

insert into MEMBERS values(1);
insert into MEMBERS values(2);
SELECT * FROM MEMBERS;

insert into ADMINISTRATOR values(1);

SELECT * FROM ADMINISTRATOR;
SELECT username 
FROM ADMINISTRATOR INNER JOIN USERS 
on ADMINISTRATOR.administratorID=USERS.userID;

SELECT * FROM USERS;

select nome from theme_character
where characterID=1 and themeID=0;

select descricao from theme_character
where characterID=1 and themeID=0;

select nome from theme_character
where characterID=1 and themeID=1;

select * from ABILITY_E2;

select descricao from theme_character
where characterID=1 and themeID=1;

select nome
from THEME_CHARACTER
where THEME_CHARACTER.themeID=1;

select * from BLEACH;

SELECT * 
FROM MISSION, THEME_CHARACTER
WHERE THEME_CHARACTER.themeID = 1 and MISSION.characterID=THEME_CHARACTER.characterID;

SELECT MISSION.nome 
FROM MISSION, THEME_CHARACTER
WHERE THEME_CHARACTER.themeID = 1 and MISSION.characterID=THEME_CHARACTER.characterID;

select quantity
from ABILITY_E5
where abilityID=0;

select nome, descricao
from THEME_ABILITY
where themeID=1 and abilityID=0;

select characterID, nome
from THEME_CHARACTER
where THEME_CHARACTER.themeID=1;

select abilityID
from ABILITY
where characterID=1;
select nome, descricao
from THEME_ABILITY
where themeID=1;

select nome, descricao 
from THEME_ABILITY
where themeID=1;

select abilityID
from ABILITY 
where characterID=1;

select nome, descricao
from THEME_ABILITY
where themeID=1 and abilityID IN (4, 5, 6, 7);

select username 
from USERS INNER JOIN ADMINISTRATOR 
where USERS.userID=ADMINISTRATOR.administratorID;

select * from MISSION where missionID=0;

select * from ABILITY where characterID=1;
select * from THEME_ABILITY where themeID=1 and abilityID=0;

select * from THEME_CHARACTER where themeID=1 and characterID=0;

select * from THEME_CHARACTER where themeID=1;

select * from BLEACH INNER JOIN THEME_CHARACTER where BLEACH.bleachID=THEME_CHARACTER.characterID and THEME_CHARACTER.themeID=1 and BLEACH.bleachID=0;


select * from THEME_ABILITY where themeID=1;
select * from THEME_ABILITY where themeID=1 and abilityID=1;

select * from MISSION where missionID=0;

select * from THEME_CHARACTER;

select pass, userID FROM USERS where username="axe";

select * from ADMINISTRATOR where administratorID=1;

select * from USERS where userID=1;

SELECT * FROM USERS ORDER BY xp DESC LIMIT 10;
SELECT * FROM USERS ORDER BY xp DESC;

select * from ABILITY where characterID=0;

select * from USERS where userID=1;

select membersID from MEMBERS order by membersID DESC LIMIT 1;

select * from USERS;
select * FROM MISSION;
select * from MEMBERS;

select * from CHARACTERS;
select * from BLEACH;
select * from BLEACH;

select * from ABILITY;

select * from CHARACTERS order by characterID DESC LIMIT 1;
select * from ABILITY order by abilityID DESC LIMIT 1;

select * from MISSION order by missionID DESC LIMIT 1;

select * from USERS;
select * from USERS order by streak DESC LIMIT 10;

select username from USERS;

SELECT * FROM USERS ORDER BY xp DESC;

select * from USERS where username="axe";
select * from USERS where userID=1;

select * from USERS order by xp DESC;

select characterID from THEME_CHARACTER where themeID=1 LIMIT 12;
select * from USERS where userID=1;

select * from THEME_CHARACTER where themeID=1;
select * from THEME_CHARACTER where themeID=1 and characterID=1;

SELECT COUNT(characterID) from THEME_CHARACTER where themeID=1;

select * from ABILITY where characterID=6;
select * from THEME_ABILITY where themeID=1;

select * from THEME_ABILITY where themeID=1 and abilityID=1;

select * from USERS where userID=1;
select * from THEME_CHARACTER where themeID=1 and characterID=1;

select * from ABILITY where characterID=1;
select * from THEME_ABILITY where themeID=1 and abilityID=4;
select * from THEME_CHARACTER where themeID=1;
select * from THEME_CHARACTER where themeID=1 and (characterID=0 or characterID=4 or characterID=6 or characterID=1 or characterID=7);

select * from USERS where userID=0 or userID=1;

select * from THEME_CHARACTER where themeID=1;
select * from THEME_ABILITY  where themeID=1 and abilityID=20;

select quantity from ABILITY_E1 where abilityID=17;
select quantity from ABILITY_E2 where abilityID=17;
select quantity from ABILITY_E3 where abilityID=17;
select quantity from ABILITY_E4 where abilityID=17;
select quantity from ABILITY_E5 where abilityID=17;

select * from THEME_CHARACTER;
select * from BLEACH;

select * from THEME_CHARACTER where characterID=9;

select * from SAO;
select * from THEME_CHARACTER where themeID=1;

select * from USERS;

select * from THEME_CHARACTER where themeID=1;
select * from THEME_CHARACTER where themeID=0;

select abilityID from ABILITY where characterID=4;

select username from USERS where userID=1;

select nome from THEME_CHARACTER where ThemeId=1 and characterID=10;
select abilityID from ABILITY where characterID=10;

select * from THEME_CHARACTER where themeID=1 and (characterID=10 or characterID=7 or characterID=4) order by case when characterID = 10 then 1 when characterID=7 then 2 when characterID=4 then 3 else null end ;

select * from THEME_ABILITY where themeID=1 and abilityID=9;

select * from ABILITY where abilityID=42;
select * from THEME_CHARACTER where themeID=1;
select * from THEME_ABILITY where themeID=1;


UPDATE THEME_ABILITY
SET descricao = "Killua Zoldyck deals 50 damage to one enemy."
where themeID=1 and abilityID=14;

select * from ABILITY where abilityID=0;
UPDATE ABILITY
SET cooldown = 1
where abilityID=14;

select * from USERS where userID=2;
UPDATE USERS
SET highestStreak = 0
where userID=2;

select * from USER_CHARACTER;
select * from USERS;
SELECT * FROM THEME_CHARACTER WHERE themeID=1;
-- DELETE FROM THEME_CHARACTER WHERE themeID=1 and characterID=24;
-- 18 inclusive ate 21

delete from USER_CHARACTER where characterID=21;

SELECT * FROM THEME_ABILITY WHERE themeID=1;
-- DELETE FROM THEME_ABILITY WHERE themeID=1 and abilityID=53;
-- delete from USER_CHARACTER where userID=1;
-- delete from USER_CHARACTER where userID=2;
-- delete from USER_CHARACTER where userID=3;

UPDATE THEME_ABILITY
SET descricao = "Kurapika locks the heart of an enemy and removes 2 random nature from him. That character will take 25 damage and be stunned for 1 turn."
where themeID=1 and abilityID=51;

select * from MISSION INNER JOIN HUNTERXHUNTER where characterID=hunterxhunterID;
select * from THEME_CHARACTER where themeID=1 and characterID=22;
select * from USER_MISSION where userID=0 and missionID=0;

insert into USER_MISSION values  (0, 1, false);

UPDATE USER_MISSION
SET completed = true
where userID=0 and missionID=0;

UPDATE THEME_ABILITY
SET descricao = "Daki fills out the battlefield with her ability dealing 15 damage to all enemies."
where themeID=1 and abilityID=117;

select * from USER_MISSION where userID=0 and missionID=0;

select * from USER_MISSION;
select * from MISSION;
select * from USERS;

select * from THEME_CHARACTER where themeID=1 and characterID=22;

select * from CHARACTERS;
select * from USERS where username="Clover";
-- esta query está a functionar
SELECT * 
FROM   CHARACTERS 
WHERE characterID NOT IN (SELECT characterID FROM MISSION);

delete from USER_CHARACTER where userID=5;

select userID from USERS where username="hash";
select * from USER_CHARACTER;
-- UPDATE USERS
-- SET pass = "1164107526"
-- where userID=6;
select * from USER_CHARACTER where userID=5;
select * from USER_MISSION where userID=5;
select * from CHARACTERS;
insert into USER_CHARACTER (userID, characterID) values (5,0);

select * from USERS;

UPDATE USERS
SET nWins = 18
where userID=7;

select * from USER_CHARACTER where userID=5;
select * from USER_MISSION;

select * from THEME_CHARACTER where themeID=1 and characterID=4;

select * from ANNOUNCEMENT;

select * from USERS where userID=9;
select * from ADMINISTRATOR;

SELECT * FROM CHARACTERS WHERE characterID NOT IN (SELECT characterID FROM MISSION);

SELECT * FROM BLEACH WHERE bleachID =4;
select * from ABILITY_E5 where abilityID=120;

select * from ABILITY where characterID=4;
select * from THEME_ABILITY where themeID=1 and abilityID=120;

select currentTheme from USERS;

UPDATE USERS
SET currentTheme = 1
where userID=0 or userID=1 or userID=2 or userID=3 or userID=4 or userID=5 or userID=6 or userID=7 or userID=8 or userID=9 or userID=10 or userID=11;

select * from THEME_CHARACTER where themeID=1;
select * from THEME_ABILITY where themeID=1;

-- DELETE FROM THEME WHERE themeID=2;
select COUNT(*) from THEME_CHARACTER where themeID=1;

select * from THEME;
select * from THEME_CHARACTER where themeID=2;
select * from THEME_ABILITY where themeID=2;

SET GLOBAL max_connections=2048;

select * from USERS where userID=0;