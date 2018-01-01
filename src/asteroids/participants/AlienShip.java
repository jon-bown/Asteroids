package asteroids.participants;

import static asteroids.Constants.SHIP_ACCELERATION;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import asteroids.Constants;
import asteroids.Controller;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.ShipDestroyer;

/**
 * Class that creates enemy ships
 * 
 * @author Jon Bown
 */

public class AlienShip extends Participant implements ShipDestroyer {

	// Game Controller
	private Controller controller;

	// Outline of the Ship
	private Shape outline;

	private int size;

	private boolean changeDirection = false;

	public AlienShip(int size, Controller controller) {

		if ((size < 0) || (size > 3)) {
			throw new IllegalArgumentException("Invalid alien ship size " + size);
		}

		this.size = size;
		this.controller = controller;

		Path2D.Double poly = new Path2D.Double();
		poly.moveTo(20.0D, 0.0D);
		poly.lineTo(9.0D, 9.0D);
		poly.lineTo(-9.0D, 9.0D);
		poly.lineTo(-20.0D, 0.0D);
		poly.lineTo(20.0D, 0.0D);
		poly.lineTo(-20.0D, 0.0D);
		poly.lineTo(-9.0D, -9.0D);
		poly.lineTo(9.0D, -9.0D);
		poly.lineTo(-9.0D, -9.0D);
		poly.lineTo(-5.0D, -17.0D);
		poly.lineTo(5.0D, -17.0D);
		poly.lineTo(9.0D, -9.0D);
		poly.closePath();

		outline = poly;

		double scale = Constants.ALIENSHIP_SCALE[size];
		poly.transform(AffineTransform.getScaleInstance(scale, scale));
		new ParticipantCountdownTimer(this, "shoot", 1500);
		new ParticipantCountdownTimer(this, "move", 1000);
		controller.addLocation(this, controller.getListNumber());
		controller.getSounds().play("alien");
	}

	/**
	 * Removes the AlienShip if it is defeated
	 */
	public void remove() {
		Participant.expire(this);
		controller.alienDestroyed();
		controller.removeLocation(this);
		this.controller.getSounds().play("destroyS");
		this.controller.getSounds().stop("alien");
		if (getSize() == 1) {
			// getSounds().stopLoop("saucerBig");
		} else {
			// getSounds().stopLoop("saucerSmall");
		}
	}

	/**
	 * returns the size of the AlienShip
	 * 
	 * @return
	 */

	public int getSize() {
		return this.size;
	}

	@Override
	protected Shape getOutline() {

		return outline;
	}

	public void accelerate() {
		accelerate(SHIP_ACCELERATION);
	}

	@Override
	public void collidedWith(Participant p) {
		if ((p instanceof AlienDestroyer)) {
			remove();

			this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (this.size + 1)));
			this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (this.size + 1)));
			this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (this.size + 1)));
			this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (this.size + 1)));
			this.controller.addParticipant(new ShipDebris(getX(), getY(), 5 * (this.size + 1)));
			this.controller.addParticipant(new ShipDebris(getX(), getY(), 5 * (this.size + 1)));
		}
	}

	@Override
	public void countdownComplete(Object payload) {
		if (this != null) { // Give a burst of acceleration, then schedule
							// another
			if (payload.equals("shoot")) {
				AlienShip ship = this.controller.getAlien();
				if (ship != null) {
					fireBullet();
					controller.getSounds().play("fire");
					new ParticipantCountdownTimer(this, "shoot", 1500);

				}

			} else if (payload.equals("move")) {
				new ParticipantCountdownTimer(this, "change", 1000);
				changeDirection = true;
			}

		}
	}

	/**
	 * Fires the bullets according to the timer
	 */

	public void fireBullet() {
		if (this != null) {
			controller.shootBullets(this);
		}
	}
	/**
	 * Gets the direction to the ship
	 * @return
	 */

	public double getShootingDirectionToShip() {
		if (this.size >= 1) {
			return Constants.RANDOM.nextDouble() * 2.0D * Math.PI;
		}
		if (controller.getShip() != null) {
			Ship ship = this.controller.getShip();
			double deltaX = ship.getX() - getX();
			double deltaY = ship.getY() - getY();
			double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			double direction = Math.acos(deltaX / distance);
			return deltaY > 0.0 ? direction : -direction;
		}

		else {
			return 0.0;
		}
	}

	/**
	 * Changes direction of the movement
	 */
	public void move() {
		super.move();

		if (this.changeDirection) {
			changeDirection = false;

			if (Math.cos(getDirection()) > 0.0) {
				setDirection(Constants.RANDOM.nextInt(3) - 1);
			} else {
				setDirection(Math.PI + Constants.RANDOM.nextInt(3) - 1.0);
			}

			new ParticipantCountdownTimer(this, "change", 1000);
		}
	}

}
