package asteroids.participants;
import asteroids.Constants;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.util.Random;

public class ShipDebris extends Participant
{
  private Shape outline;

  public ShipDebris(double x, double y, double length)
  {
    double noise = Constants.RANDOM.nextDouble() * 10.0D - 5.0D;

    Path2D.Double line = new Path2D.Double();
    line.moveTo(0.0D, -length / 2.0D);
    line.lineTo(0.0D, length / 2.0D);

    setRotation(6.283185307179586D * Constants.RANDOM.nextDouble());
    setPosition(x + noise, y + noise);
    setVelocity(Constants.RANDOM.nextDouble(), Constants.RANDOM.nextDouble() * 2.0D * 3.141592653589793D);

    this.outline = line;

   new ParticipantCountdownTimer(this, this, 1500 + (int)(Constants.RANDOM.nextDouble() * 500.0D));
  }

  protected Shape getOutline()
  {
    return this.outline;
  }

  public void countdownComplete(Object payload)
  {
    Participant.expire(this);
  }

  public void collidedWith(Participant p)
  {
  }


}

