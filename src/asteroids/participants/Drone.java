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
import asteroids.destroyers.ShipDestroyer;

public class Drone extends Participant implements AsteroidDestroyer, AlienDestroyer, BossDestroyer{
	// Game Controller
	private Controller controller;
	private double x;
	private double y;

	// Outline of the Ship
	private Shape outline;
	
	public Drone(double x, double y, double direction, double rotation, Controller controller, int size){
		this.controller = controller;
		if(size == 2){
			this.x = x;
			this.y = y;
		}
		setPosition(x,y);
		setDirection(rotation);
		setRotation(direction);
		setVelocity(Constants.BULLET_SPEED,rotation);
		Path2D.Double poly = new Path2D.Double();
	 		poly.moveTo(0.0,10.0);
			poly.lineTo(-2.0D, 8.0D);
			poly.lineTo(-1.0D, 1.0D);
			poly.lineTo(-3.0D, -1.0D);
			poly.lineTo(0.0D, 0.0D);
			poly.lineTo(3.0D, -1.0D);
			poly.lineTo(1.0D, 1.0D);
			poly.lineTo(2.0D, 8.0D);
			poly.lineTo(-2.0D,  8.0D);
			poly.lineTo(0.0D, 10.0D);
			poly.lineTo(2.0D, 8.0D);
			poly.closePath();	
			this.outline = poly;
			
			double scale = 1*size;
			poly.transform(AffineTransform.getScaleInstance(scale, scale));
			if(size == 2){
				new ParticipantCountdownTimer(this, "shoot", 3000);
			}
			else{
				new ParticipantCountdownTimer(this, "delete", 900);
			}
			
	}

	@Override
	protected Shape getOutline() {
		
		return outline;
	}
	
	//Moves the missile
	@Override
	public void move(){
		super.move();
	}
	

	
	public double getXNose(){
		return this.x;
	}
	public double getYNose(){
		return this.y;
	}
	
	
	  
	@Override
	public void collidedWith(Participant p) {
		if (p instanceof ShipDestroyer)
        {
			  this.controller.addParticipant(new ADebris(getX(), getY()));
	            this.controller.addParticipant(new ADebris(getX(), getY()));
	            this.controller.addParticipant(new ADebris(getX(), getY()));
            // Expire the bullet from the game
            Participant.expire(this);

            // Tell the controller the ship was destroyed
            controller.droneDestroyed();
        }
		
	}
	
	@Override
	public void countdownComplete(Object payload) {
		
	if(payload.equals("shoot")){
		  this.controller.addParticipant(new ADebris(getX(), getY()));
          this.controller.addParticipant(new ADebris(getX(), getY()));
          this.controller.addParticipant(new ADebris(getX(), getY()));
          controller.getSounds().play("droneFire");
		Participant.expire(this);
		
		controller.placeSmallDrone();
	}
	else{
		this.controller.addParticipant(new ADebris(getX(), getY()));
        this.controller.addParticipant(new ADebris(getX(), getY()));
		Participant.expire(this);
	}
	}

}
