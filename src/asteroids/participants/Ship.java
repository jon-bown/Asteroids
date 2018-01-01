package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.*;

import asteroids.Controller;
import asteroids.Participant;
import asteroids.destroyers.*;
import static asteroids.Constants.*;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer, TokenDestroyer
{
   //The outline of the ship
    private Shape outline;
    
   //Outline of the flame
    private Shape flameOutline;
    //Boolean that determines which outline to draw
    private boolean accelerating;
    //Boolean that determines to show the flame
    private boolean showFlame;
    //Boolean that determines which outline to draw based on the shield

    // Game controller
    private Controller controller;

  
    // Constructs a ship at the specified coordinates
    // that is pointed in the given direction.
    public Ship (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(20, 0);
        poly.lineTo(-20, 12);
        poly.lineTo(-13, 10);
        poly.lineTo(-13, -10);
        poly.lineTo(-20, -12);
        poly.closePath();
        outline = poly;
        
        
        poly = new Path2D.Double();
        poly.moveTo(20.0D, 0.0D);
        poly.lineTo(-20.0D, 12.0D);
        poly.lineTo(-13.0D, 10.0D);
        poly.lineTo(-13.0D, -5.0D);

        poly.lineTo(-25.0D, 0.0D);
        poly.lineTo(-13.0D, 5.0D);
        poly.lineTo(-13.0D, -10.0D);

        poly.lineTo(-20.0D, -12.0D);
        poly.closePath();
        flameOutline = poly;
       //Schedule an acceleration in two seconds
       //new ParticipantCountdownTimer(this, "move", 2000);
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose
     * is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose
     * is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    @Override
    protected Shape getOutline ()
    {
    	
        if (this.accelerating)
        {
          this.showFlame = (!this.showFlame);
          if (this.showFlame)
          {
            return this.flameOutline;
          }

          return outline;
        }

        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        applyFriction(SHIP_FRICTION);
        super.move();
    }

    /**
     * Turns right by Pi/16 radians
     */
    public void turnRight ()
    {
        rotate(Math.PI / 16);
    }

    /**
     * Turns left by Pi/16 radians
     */
    public void turnLeft ()
    {
        rotate(-Math.PI / 16);
    }
    
    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(SHIP_ACCELERATION);
        accelerating = true;
    }
    
    public void coast()
    {
      if (accelerating)
      {
        //getSounds().stopLoop("thrust");
      }
      accelerating = false;
    }

    /**
     * When a Ship collides with a ShipKiller, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);
            controller.removeLocation(this);
            
            
            //Add debris
            
          this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (1)));
  	      this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (1)));
  	      this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (1)));
  	      this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (1)));
  	      this.controller.addParticipant(new ShipDebris(getX(), getY(), 5 * (1)));

            // Tell the controller the ship was destroyed
            controller.shipDestroyed();
        }
    }
    
    /**
     * This method is invoked when a ParticipantCountdownTimer completes
     * its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Give a burst of acceleration, then schedule another
        // burst for 200 msecs from now.
        if (payload.equals("move"))
        {
            accelerate();
           
        }
    }


}
