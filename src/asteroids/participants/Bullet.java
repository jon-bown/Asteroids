package asteroids.participants;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import com.sun.javafx.geom.Ellipse2D;
import com.sun.prism.paint.Color;
import java.awt.*;

import asteroids.Controller;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BossDestroyer;
import asteroids.destroyers.ShipDestroyer;
import javafx.scene.shape.Circle;
import asteroids.Constants;
import asteroids.participants.*;
import asteroids.participants.Ship;

/**
 * Class that implements bullets that are asteroid destroyers. They create very small polygons that are recognized by the collide methods.
 * @author Jon Bown
 */
@SuppressWarnings("unused")
public class Bullet extends Participant implements AsteroidDestroyer, AlienDestroyer, BossDestroyer {
	
	//Shape of the bullet
	private Shape outline;
	
	//Controller for the game
	private Controller controller;
public Bullet (double x, double y, double rotation, Controller controller){
    	
    	this.controller = controller;
    	 if (x < 0)
    	 {
             throw new IllegalArgumentException("Invalid bullet position: ");
         }
         else if (y < 0)
         {       throw new IllegalArgumentException();
             
         }
    	setPosition(x,y);
    	setDirection(rotation);
    	setVelocity(Constants.BULLET_SPEED,rotation);

    	 Path2D.Double poly = new Path2D.Double();
         poly.moveTo(0,1);
         poly.lineTo(1, 0);
         poly.lineTo(0, -1);
         poly.lineTo(-1, 0);
         poly.closePath();
    
    	outline = poly;
    
    	new ParticipantCountdownTimer(this, "delete", Constants.BULLET_DURATION);
    
         }
	@Override
	protected Shape getOutline() {
		
		return outline;
	}


//Tells the bullet to go away and that it collided with an asteroid. 
	@Override
	public void collidedWith(Participant p) {
		if (p instanceof ShipDestroyer)
	        {
	            // Expire the bullet from the game
	            Participant.expire(this);

	            // Tell the controller the ship was destroyed
	            controller.bulletDestroyed();
	        }
	}
	//Moves the bullet
	@Override
	public void move(){
		super.move();
	}
	
	//Timer that deletes the bullet after the given time expires
	 @Override
	    public void countdownComplete (Object payload)
	    {
	        // Give a burst of acceleration, then schedule another
	        // burst for 200 msecs from now.
	        if (payload.equals("delete"))
	        {
	           Participant.expire(this);
	         
	           controller.bulletDestroyed();
	        }
	    }

	

}
