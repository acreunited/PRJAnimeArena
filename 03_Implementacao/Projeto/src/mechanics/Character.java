package mechanics;

import java.math.RoundingMode;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.Connector;

public class Character {
	
	private int id;
	private int hp;
	private boolean isStunned;
	private int stunnedDuration;
	private boolean isInvul;
	private int invulDuration;
	private int permanentDamageIncrease;
	private int temporaryDamageIncrease;
	private int dr;
	private int natureGain;
	private int natureLoss;
	private Ability ability1;
	private Ability ability2;
	private Ability ability3;
	private Ability ability4;
	private boolean isDead;
	
	public Character(int id) {
		this.id = id;
		this.hp = 100;
		this.isStunned = false;
		this.stunnedDuration = 0;
		this.isInvul = false;
		this.invulDuration = 0;
		this.permanentDamageIncrease = 0;
		this.temporaryDamageIncrease = 0;
		this.dr = 0;
		this.natureGain = 0;
		this.natureLoss = 0;
		this.ability1 = new Ability( this.getCharacterAbilities(this.id)[0] );
		this.ability2 = new Ability( this.getCharacterAbilities(this.id)[1] );
		this.ability3 = new Ability( this.getCharacterAbilities(this.id)[2] );
		this.ability4 = new Ability( this.getCharacterAbilities(this.id)[3] );
		this.isDead = false;
	}
	
	public void applyAbility(Ability a, Character c, boolean isAoe) {
		

		//damage
		if (a.getDamage()[1]>0) {
			
			int damage = doDamage(a, c);
			if (damage>0) {
				c.setHp( c.getHp()-damage );
			}
			
			//reduce duration by 1
//			int[] lessTurn = new int[2];
//			lessTurn[0] = a.getDamage()[0];
//			lessTurn[1] = a.getDamage()[1] - 1;
//			a.setDamage(lessTurn);
		}
		
		//permanent damage
		if (a.getPermanentDamageIncrease() > 0) {
			c.setPermanentDamageIncrease( c.getPermanentDamageIncrease() + a.getPermanentDamageIncrease() );
			
			//reduce duration by 1
//			int[] lessTurn = new int[2];
//			lessTurn[0] = a.getPermanentDamageIncrease()[0];
//			lessTurn[1] = a.getPermanentDamageIncrease()[1] - 1;
//			a.setPermanentDamageIncrease( (lessTurn) );
		}
		
		//temporary damage
		if (a.getTemporaryDamageIncrease()[1] > 0) {
			//c.setTemporaryDamageIncrease( c.getTemporaryDamageIncrease() + a.getTemporaryDamageIncrease()[0] );
			//System.out.println(a.getTemporaryDamageIncreaseTarget()[1]);
			if (a.getTemporaryDamageIncreaseTarget()[1].equalsIgnoreCase("all")) {
				this.ability1.setCurrentTemporaryDamage( this.ability1.getCurrentTemporaryDamage() + a.getTemporaryDamageIncrease()[0] );
				this.ability2.setCurrentTemporaryDamage( this.ability2.getCurrentTemporaryDamage() + a.getTemporaryDamageIncrease()[0] );
				this.ability3.setCurrentTemporaryDamage( this.ability3.getCurrentTemporaryDamage() + a.getTemporaryDamageIncrease()[0] );
				this.ability4.setCurrentTemporaryDamage( this.ability4.getCurrentTemporaryDamage() + a.getTemporaryDamageIncrease()[0] );
			}
			else { //só aumenta a habilidade selecionada (String = 'current')
				a.setCurrentTemporaryDamage( a.getCurrentTemporaryDamage() + a.getTemporaryDamageIncrease()[0]);
			}
			
		}
		
		//damagePerUse
		/*if (a.getDamageIncreasePerUse()>0) {
			a.setnTimesUsed( a.getnTimesUsed()+1 ); TODO
		}*/
		
		//gainHP
		if (a.getGainHP()[1]>0) {
			int hp = a.getGainHP()[0];
			if (a.getHealIncreasePerUse()>0) {
				hp = hp + (a.getHealIncreasePerUse() * a.getnTimesUsed()) ;
			}
			if (hp > 0) {
				c.setHp(c.getHp() + hp);
			}
			
			//reduce duration by 1
//			int[] lessTurn = new int[2];
//			lessTurn[0] = a.getGainHP()[0];
//			lessTurn[1] = a.getGainHP()[1] - 1;
//			a.setGainHP(lessTurn);
		}
		
		//gain DR
		if (a.getDR()[1]>0) {
			int dr = a.getDR()[0];
			if (dr>0) {
				if (a.getGainDRTarget().equalsIgnoreCase("self")) {
					this.setDr( this.getDr() + dr );
				}
				else {
					c.setDr( c.getDr() + dr );
				}
				
			}
			
			//reduce duration by 1
//			int[] lessTurn = new int[2];
//			lessTurn[0] = a.getDR()[0];
//			lessTurn[1] = a.getDR()[1] - 1;
//			a.setDR(lessTurn);
		}
		
		//gain nature
		if (a.getGainNature()[1]>0) {
			int natureGain = a.getGainNature()[0];
			if (natureGain>0) {
				c.setNatureGain( c.getNatureGain() + natureGain );
			}
			
			//reduce duration by 1
//			int[] lessTurn = new int[2];
//			lessTurn[0] = a.getGainNature()[0];
//			lessTurn[1] = a.getGainNature()[1] - 1;
//			a.setGainNature(lessTurn);
		}
		
		//lose nature
		if (a.getRemoveNature()[1]>0) {
			int natureLoss = a.getRemoveNature()[0];
			if (natureLoss > 0) {
				c.setNatureLoss( c.getNatureLoss() + natureLoss );
			}
			
			//reduce duration by 1
//			int[] lessTurn = new int[2];
//			lessTurn[0] = a.getRemoveNature()[0];
//			lessTurn[1] = a.getRemoveNature()[1] - 1;
//			a.setRemoveNature(lessTurn);
		}
		
		//become invulnerable
		if (a.getBecomeInvulDuration()>0) {
			c.setInvulDuration( a.getBecomeInvulDuration() );
			
			//reduce duration by 1
		//	a.setBecomeInvulDuration( a.getBecomeInvulDuration()-1 );
		}
		
		if (a.getStunDuration()>0) {
			c.setStunnedDuration( a.getStunDuration() );
			//c.setStunned(true);
			a.setStunDuration(0);
			
		//	a.setStunDuration( a.getStunDuration()-1 );
		}
		

		
		if (!isAoe) {
			a.reduceDuration();
			a.setnTimesUsed( a.getnTimesUsed()+1 );
		}
				
	}

	
	private int doDamage(Ability a, Character c) {
		int damage = a.getDamage()[0];
		
		int permanent = this.getPermanentDamageIncrease();
		int temporary = a.getCurrentTemporaryDamage();
		int damageIncreaseUse = a.getDamageIncreasePerUse();
		if (damageIncreaseUse>0) {
			damageIncreaseUse = damageIncreaseUse*a.getnTimesUsed();
		}
		
		damage += permanent + temporary + damageIncreaseUse;
		//damage += permanent + damageIncreaseUse;
		
		int damagePerEnemyHPLost = this.getAbilityDamageEnemyHP(a, c);
		if (damagePerEnemyHPLost>0) {
			damage += damagePerEnemyHPLost;
		}
		int damagePerSelfHPLost = this.getAbilityDamageSelfHP(a);
		if (damagePerSelfHPLost>0) {
			damage += damagePerSelfHPLost;
		}
		
		//check if opp has DR
		damage -= c.getDr();
		//c.setDr( c.getDr()-damage );
		
		
		
		return (damage>0) ? damage : 0;
	}
	
	private int getAbilityDamageEnemyHP(Ability a, Character c) {

		int hpLost = 100-c.getHp();
		int damagePerHp = a.getDamagePerEnemyHPLost()[1];		
		int damage = a.getDamagePerEnemyHPLost()[0];
		
		int nTimes = -1;
		if (damagePerHp>0) {
			nTimes = (int) Math.ceil(hpLost/damagePerHp);
		}
		
		return damage*nTimes;
	}
	private int getAbilityDamageSelfHP(Ability a) {
		
		int hpLost = 100-this.getHp();
		int damagePerHp = a.getDamagePerHPLost()[1];		
		int damage = a.getDamagePerHPLost()[0];
		
		int nTimes = -1;
		if (damagePerHp>0) {
			nTimes = (int) Math.ceil(hpLost/damagePerHp);
		}
		
		return damage*nTimes;
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHp() {
		return hp;
	}

	//nao pode passar dos 100 TODO
	public void setHp(int hp) {
		if (!this.isDead) {
			int between = (hp<=100) ? hp : 100;
			this.hp =  (between>=0) ? between : 0;
			
			if (this.hp <= 0) {
				this.setDead(true);
			}
		}
		
	}

	public boolean isStunned() {
		return isStunned;
	}

	public void setStunned(boolean isStunned) {
		this.isStunned = isStunned;
	}

	public boolean isInvul() {
		return isInvul;
	}

	public void setInvul(boolean isInvul) {
		this.isInvul = isInvul;
	}

	public int getPermanentDamageIncrease() {
		return permanentDamageIncrease;
	}

	public void setPermanentDamageIncrease(int permanentDamageIncrease) {
		this.permanentDamageIncrease = permanentDamageIncrease;
	}


	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = (dr>=0) ? dr : 0;
	}
	public Ability getAbility1() {
		return ability1;
	}


	public Ability getAbility2() {
		return ability2;
	}


	public Ability getAbility3() {
		return ability3;
	}


	public Ability getAbility4() {
		return ability4;
	}
	private int[] getCharacterAbilities(int id) {
		PreparedStatement stmt = null;
		int[] abilitiesID = new int[4];
		
		try {
			Class.forName(Connector.drv);

			Connection conn = Connector.getConnection();

			String query = String.format("select abilityID from ABILITY where characterID="+id); 
			
			stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			int count = 0;
			
			while (rs.next()) {
				
				abilitiesID[count] = Integer.parseInt( rs.getString("abilityID") );
				count++;
			}
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
		return abilitiesID;
	}

	public int getTemporaryDamageIncrease() {
		return temporaryDamageIncrease;
	}

	public void setTemporaryDamageIncrease(int temporaryDamageIncrease) {
		this.temporaryDamageIncrease = temporaryDamageIncrease;
	}

	public int getStunnedDuration() {
		return stunnedDuration;
	}

	public void setStunnedDuration(int stunnedDuration) {
		this.stunnedDuration = (stunnedDuration>0) ? stunnedDuration : 0;
		this.setStunned( (stunnedDuration>0) ? true : false );
	}

	public int getInvulDuration() {
		return invulDuration;
	}

	public void setInvulDuration(int invulDuration) {
		this.invulDuration = invulDuration;
		this.setInvul( (invulDuration>0) ? true : false );
	}

	public int getNatureGain() {
		return natureGain;
	}

	public void setNatureGain(int natureGain) {
		this.natureGain = natureGain;
	}

	public int getNatureLoss() {
		return natureLoss;
	}

	public void setNatureLoss(int natureLoss) {
		this.natureLoss = natureLoss;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		if (isDead) {
			this.setNatureGain(0);
			this.setNatureLoss(0);
		}
		this.isDead = isDead;
	}

}
