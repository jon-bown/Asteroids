package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.color.*;
import asteroids.Constants;
import asteroids.Controller;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;
import asteroids.destroyers.TokenDestroyer;

/**
 * Class that creates the three types of tokens that can be picked up by the ship
 * @author Jon Bown
 */

public class Token extends Participant{
	
	//Integer that represents the type of token to interact with the controller;
	private int type;
	
	//Outline of the token
	private Shape outline;
	
	private Controller controller;
	
	//Takes a string as a parameter telling which token to draw
	public Token(int type, Controller controller){
		if(type < 0 || type > 4){
			throw new IllegalArgumentException();
		}
		this.controller = controller;
		Path2D.Double poly = new Path2D.Double();
		if(type == 0){ //Nuke
			this.type = 0;

			poly.moveTo(15.0D, 15.0D);
			poly.lineTo(15.0D, -15.0D);
			poly.lineTo(-15.0D, -15.0D);
			poly.lineTo(15.0D, 15.0D);
			poly.lineTo(-10.0D, 15.0D);
			poly.lineTo(-10.0D, -10.0D);
			poly.lineTo(10.0D, 10.0D);
			poly.lineTo(10.0D, -15.0D);
			poly.lineTo(-15.0D, -15.0D);
			poly.lineTo(-15.0D, 15.0D);
			poly.closePath();
		
			
		}
		else if(type == 1){   //Shield
			this.type = 1;

			
			poly.moveTo(15.0D, 15.0D);
			poly.lineTo(-15.0D, 15.0D);
			poly.lineTo(-8.0D, 13.0D);
			poly.lineTo(0.0D, 10.0D);
			poly.lineTo(8.0D, 5.0D);
			poly.lineTo(0.0D, 0.0D);
			poly.lineTo(-8.0D, -5.0D);
			poly.lineTo(0.0D, -10.0D);
			poly.lineTo(8.0D, -13.0D);
			poly.lineTo(15.0D, -15.0D);
			poly.lineTo(-15D, -15.0D);
			poly.lineTo(-15.0D, 15.0D);
			poly.lineTo(15.0D, 15D);
			poly.lineTo(15.0D, -15D);
			poly.closePath();

		}
		else if(type == 2){ //Drone
			this.type = 2;
			poly.moveTo(15.0D, 15.0D);
			poly.lineTo(-15.0D, 15.0D);
			poly.lineTo(-15.0D, -15.0D);
			poly.lineTo(-8.0D, -15.0D);
			poly.lineTo(-8.0D, 15.0D);
			poly.lineTo(8.0D, 8.0D);
			poly.lineTo(8.0D, -8.0D);
			poly.lineTo(-8.0D, -15.0D);
			poly.lineTo(15.0D, -15.0D);
			poly.lineTo(15.0D, 15.0D);
			poly.closePath();
			
		}
		else if(type == 3){ //New Life
			this.type = 3;
			poly.moveTo(15.0D, 15.0D);
			poly.lineTo(-15.0D, 15.0D);
			poly.lineTo(-15.0D, -15.0D);
			poly.lineTo(0.0D, -15.0D);
			poly.lineTo(0.0D, 0.0D);
			poly.lineTo(-15.0D, 0.0D);
			poly.lineTo(15.0D, 0.0D);
			poly.lineTo(15.0D, 15.0D);
			poly.lineTo(0.0D, 15.0D);
			poly.lineTo(0.0D, -15.0D);
			poly.lineTo(15.0D, -15.0D);
			poly.lineTo(15.0D, 15.0D);
			poly.closePath();
			
		}
		else if(type == 4){ //Mine
			this.type = 4;
			poly.moveTo(15.0D, 15.0D);
			poly.lineTo(-15.0D, 15.0D);
			poly.lineTo(-15.0D, -15.0D);
			poly.lineTo(15.0D, 15.0D);
			poly.lineTo(15.0D, -15.0D);
			poly.lineTo(-15.0D, 15.0D);
			poly.lineTo(-15.0D, -15.0D);
			poly.lineTo(15.0D, -15.0D);
			poly.closePath();
		}
			this.outline = poly;
			controller.getSounds().play("tokenAppears");
			this.setPosition(0.0D, 750.0D * Constants.RANDOM.nextDouble());
			this.setVelocity(5 - 1, Constants.RANDOM.nextInt(2) * 3.141592653589793D);
			new ParticipantCountdownTimer(this, "delete", 10000);	
	}

	@Override
	protected Shape getOutline() {
		return outline;
	}
	
	/**
	 * returns the type of token for use in the controller class
	 * @return
	 */
	public int getType(){
		return 0;
	}


	@Override
	public void collidedWith(Participant p) {
		if(p instanceof TokenDestroyer){
			controller.getSounds().play("1up");
			if(this.type == 0){
				this.controller.setNuke();
			}
			else if(this.type == 1){
				this.controller.setShield();
			}
			else if(this.type == 2){
				this.controller.setDrone();
			}
			else if(this.type == 3){
				this.controller.increaseLives();
			}
			else if(this.type == 4){
				this.controller.addMines();
			}
			Participant.expire(this);
		}
		
	}
	
	@Override
	public void countdownComplete(Object payload) {
	
		if(payload.equals("delete")){
			
			if(this.type ==0 || this.type == 2){
				Participant.expire(this);
			}
			else if(this.type == 1){
				controller.expireShield();
			}
			else{
				Participant.expire(this);
				controller.getSounds();
			}
		
			
		}

		
	}




}
