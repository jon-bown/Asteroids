package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;

import asteroids.Constants;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;

public class ADebris extends Participant {
	


	// Outline of the Debris
	private Shape outline;
	
	public ADebris(double x, double y){
		
	    double noise = Constants.RANDOM.nextDouble() * 10.0D - 5.0D;

		 Path2D.Double poly = new Path2D.Double();
         poly.moveTo(0,.7);
         poly.lineTo(.7, 0);
         poly.lineTo(0, -.7);
         poly.lineTo(-.7, 0);
         poly.closePath();
         this.outline = poly;
	    setRotation(6.283185307179586D * Constants.RANDOM.nextDouble());
	    setPosition(x + noise, y + noise);
	    setVelocity(Constants.RANDOM.nextDouble(), Constants.RANDOM.nextDouble() * 2.0D * 3.141592653589793D);
	    new ParticipantCountdownTimer(this, this, 1500 + (int)(Constants.RANDOM.nextDouble() * 500.0D));
	}
	
	

	@Override
	protected Shape getOutline() {
		return outline;
	}
	
	//Sets the timer for the expiration of the individual debris
	
	public void countdownComplete(Object payload)
	  {
	    Participant.expire(this);
	  }

	@Override
	public void collidedWith(Participant p) {
		// TODO Auto-generated method stub
		
	}




}
