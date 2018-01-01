package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import asteroids.Constants;
import asteroids.Controller;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BossDestroyer;
import asteroids.destroyers.BulletDestroyer;
/**
 * Creates a Nuke or a Shield or a Mine that is given to the player through obtaining a token
 * @author Jon Bown
 */
public class Nuke extends Participant implements AsteroidDestroyer, AlienDestroyer, BulletDestroyer, BossDestroyer {
	
	// Game Controller
		private Controller controller;

		// Outline of the Ship
		private Shape outline;
		//Size of the nuke wave that is contained in the constants class
		private int size;
		//x and y coordinates called by the controller
		private double x,y;
		
		private Participant p,p2;

		public Nuke(Controller controller, double x, double y, int size, boolean repeat, boolean mine){
			
			if(size < 0){
				throw new IllegalArgumentException();
			}
			this.controller = controller;
			this.size = size;
			this.size++;
			this.x = x;
			this.y = y;
			setPosition(x,y);
		    Path2D.Double poly = new Path2D.Double();
	        poly.moveTo(20D, 0D);
	        poly.lineTo(20D, 12D);
	        poly.lineTo(10D, 20D);
	        poly.lineTo(-10D, 20D);
	        poly.lineTo(-20D, 12D);
	        poly.lineTo(-20D, -12D);
	        poly.lineTo(-10D, -20D);
	        poly.lineTo(10D, -20D);
	        poly.lineTo(20D, -12D);
	        poly.closePath();
	        outline = poly;			
			double scale = Constants.NUKE_SCALE[size];
			
			if(repeat){
				new ParticipantCountdownTimer(this, "delete", 90);	
				poly.transform(AffineTransform.getShearInstance(scale, scale));
			}
			else if(!mine){
				new ParticipantCountdownTimer(this, "Shield", 10000);
				poly.transform(AffineTransform.getScaleInstance(scale, scale));
			}
			else{
				new ParticipantCountdownTimer(this, "Mine", 3000);
				poly.transform(AffineTransform.getScaleInstance(scale, scale));
			}
			
			
		}

		public void setSize(int size){
			this.size = size;
		}
		public void setX(double x){
			this.x = x;
		}
		public void setY(double y){
			this.y = y;
		}
	@Override
	protected Shape getOutline() {
		// TODO Auto-generated method stub
		return outline;
	}

	@Override
	public void collidedWith(Participant p) {

		
	}
	
	@Override
	public void countdownComplete(Object payload) {
	
			if(payload.equals("delete")){
				
				Participant.expire(this);
				
				try{	
					p = new Nuke(this.controller, this.x, this.y, this.size+4, true, false);
					p.setPosition(this.x, this.y);
					controller.addParticipant(p);
					p2 = new Nuke(this.controller, this.x, this.y, this.size, true, false);
					p2.setPosition(this.x, this.y);
					controller.addParticipant(p2);
					}
					catch(ArrayIndexOutOfBoundsException g){
						controller.nukeDestroyed();
					}
				
			}
			if(payload.equals("Shield")){
				Participant.expire(this);
				this.controller.expireShield();
				this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (this.size + 1)));
				this.controller.addParticipant(new ShipDebris(getX(), getY(), 12 * (this.size + 1)));
				this.controller.addParticipant(new ShipDebris(getX(), getY(), 18 * (this.size + 1)));
				this.controller.addParticipant(new ShipDebris(getX(), getY(), 5 * (this.size + 1)));
				controller.nukeDestroyed();
			}
			if(payload.equals("Mine")){
				Participant.expire(this);
				try{	
					p = new Nuke(this.controller, this.x, this.y, 15, true, false);
					p.setPosition(this.x, this.y);
					controller.addParticipant(p);

					}
					catch(ArrayIndexOutOfBoundsException g){
						controller.nukeDestroyed();
					}
				
			}
		
	}

}
