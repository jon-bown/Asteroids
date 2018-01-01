package asteroids;

import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

import asteroids.participants.ABullet;
import asteroids.participants.Asteroid;
import asteroids.participants.Boss;
import asteroids.participants.Bullet;
import asteroids.participants.Drone;
import asteroids.participants.AlienShip;
import asteroids.participants.Nuke;
import asteroids.participants.Ship;
import asteroids.participants.Token;

import static asteroids.Constants.*;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener {
	// The state of all the Participants
	private ParticipantState pstate;

	// The ship (if one is active) or null (otherwise)
	private Ship ship;

	// The Alien ship if one is active or null
	private AlienShip enemyShip;
	// When this timer goes off, it is time to refresh the animation
	private Timer refreshTimer;
	// Nuke used in the game
	private Nuke nuke;

	// Boss that appears on the 6th level
	private Boss boss;

	// The time at which a transition to a new stage of the game should be made.
	// A transition is scheduled a few seconds in the future to give the user
	// time to see what has happened before doing something like going to a new
	// level or resetting the current level.
	private long transitionTime;

	// Number of lives left
	private int lives, score, level, nukes, drones, mines;

	// The game display
	private Display display;

	// The number of Bullets
	private int shipBullets;

	// The Timer variables
	private Timer beatTimer;
	private boolean playBeat1;

	// The closest participant to the ship
	private Participant pClosest, shieldP;
	// The direction to the closest participant to the ship
	private double directionTo, minDistance;

	// ArrayLists that keep track of x & y values of asteroids
	private ArrayList<Double> xList;
	private ArrayList<Double> yList;
	private ArrayList<Participant> pList;
	// The beginning list number
	private int listNumber = 0;
	// Booleans for movement and game play type
	private boolean turnLeft, turnRight, propel, enhanced, shield;
	// Sound Object used to obtain the sounds in sounds package
	private Sound sound;
	private Drone drone;

	/**
	 * Constructs a controller to coordinate the game and screen
	 */
	public Controller() {
		// Initialize the ParticipantState
		pstate = new ParticipantState();
		// Set up the refresh timer.
		refreshTimer = new Timer(FRAME_INTERVAL, this);
		refreshTimer.setActionCommand("move");

		// Set up the beat timer
		this.beatTimer = new Timer(900, this);
		this.playBeat1 = true;
		// Clear the transitionTime
		transitionTime = Long.MAX_VALUE;
		// Record the display object
		display = new Display(this);
		sound = new Sound();
		initiatLists();
		listNumber = 0;
		// Bring up the splash screen and start the refresh timer
		splashScreen();
		display.setVisible(true);
		refreshTimer.start();

	}

	/**
	 * Sets the controller to enhanced mode
	 */
	public void setEnhanced() {
		enhanced = true;
		display.setColor(Color.RED);

	}

	/**
	 * If the player obtains the shield token the controller turns on the shield
	 */
	public void setShield() {
		if (!shield) {
			if(shieldP==null){
			sound.play("shieldOn");
			shield = true;
			shieldP = new Nuke(this, ship.getX(), ship.getY(), 4, false, false);
			shieldP.setPosition(ship.getX(), ship.getY());
			shieldP.setRotation(ship.getRotation());
			shieldP.setVelocity(ship.getSpeed(), ship.getDirection());
			addParticipant(shieldP);
			}
		}
	}

	public void expireShield() {
		shield = false;
	}

	/**
	 * Used to control enhanced mode in the participant classes
	 * 
	 * @return
	 */
	public boolean getEnhanced() {
		return enhanced;
	}

	/**
	 * returns the sounds used in each class
	 */
	public Sound getSounds() {
		return sound;
	}

	/**
	 * Returns the ship, or null if there isn't one
	 */
	public Ship getShip() {
		return ship;
	}

	/**
	 * 
	 * @return
	 */

	public AlienShip getAlien() {
		return enemyShip;
	}

	/**
	 * Configures the game screen to display the splash screen
	 */
	private void splashScreen() {
		// Clear the screen, reset the level, and display the legend
		clear();
		if (!enhanced) {
			display.setLegend("Asteroids");
		} else {
			display.setLegend("Asteroids Enhanced");
		}

		// Place four asteroids near the corners of the screen.
		placeAsteroids(level);
	}

	/**
	 * Retrieves the lives from the controller
	 * 
	 */
	public int getLives() {
		return lives;
	}

	public int getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * Increments the number of drones available to the player
	 */
	public void setDrone() {
		drones++;
		display.setDronesLabel(drones);

	}

	/**
	 * initializes the three array lists that track the positions of asteroids
	 * and ships
	 */
	public void initiatLists() {
		xList = new ArrayList<>();
		yList = new ArrayList<>();
		pList = new ArrayList<>();
	}

	/**
	 * Uses 3 lists to keep track of the ship, aliens and asteroids and their
	 * locations for objects that need to know
	 * 
	 * @param p
	 *            participant
	 * @param n
	 *            list index number
	 */
	public void addLocation(Participant p, int n) {

		if (p != null) {
			if (n < pList.size()) {
				xList.set(n, p.getX());
				yList.set(n, p.getY());
				pList.set(n, p);
			} else {
				xList.add(p.getX());
				yList.add(p.getY());
				pList.add(p);
			}
		}
	}

	/**
	 * updates the ships movement inside index 0 of the array lists
	 */
	public void updateShipLocation() {
		addLocation(ship, 0);
	}

	/**
	 * When a participant is expired it is removed from the array list
	 * 
	 * @param p
	 */

	public void removeLocation(Participant p) {
		for (int i = 0; i < pList.size(); i++) {
			if (pList.get(i).equals(p)) {
				xList.remove(i);
				yList.remove(i);
				pList.remove(i);
			}
		}
	}

	/**
	 * returns the index incremented by one each time it is called to prevent
	 * duplicaiton
	 * 
	 * @return
	 */
	public int getListNumber() {
		listNumber = pList.size();
		listNumber++;
		return listNumber;

	}

	/**
	 * sets the amount of Nukes
	 * 
	 */
	public void setNuke() {
		nukes++;
		display.setNukesLabel(nukes);
	}

	/**
	 * For use inside the token class, if an "L" token is picked up increases
	 * amount of lives
	 */
	public void increaseLives() {
		lives++;
		display.setLivesLabel(lives);
	}

	/**
	 * returns the closest participant, and the direction to that participant
	 */
	public void getClosestParticipant() {

		double deltaX = 0;
		double deltaY = 0;
		int random = asteroids.Constants.RANDOM.nextInt(pList.size());
		if (pstate.countAsteroids() > 0) {

			deltaX = xList.get(random) - xList.get(0);
			deltaY = yList.get(random) - yList.get(0);
			minDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			pClosest = pList.get(random);
			directionTo = Math.atan2(deltaY, deltaX);

		}

	}

	public void checkClosestParticipant() {

		double deltaX = 0;
		double deltaY = 0;
		for (int i = 1; i < pList.size(); i++) {
			deltaX = xList.get(i) - xList.get(0);
			deltaY = yList.get(i) - yList.get(0);
			double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

			if (distance < this.minDistance) {
				pClosest = pList.get(i);
				minDistance = distance;
				if (deltaY < 0) {
					directionTo = -directionTo;
				}
			}
		}
	}

	public void setMinDistance() {
		minDistance = 1000000;
	}

	/**
	 * returns the closet participants, used for the drone
	 * 
	 * @return
	 */
	public Participant getClosestP() {
		return pClosest;
	}

	public double getDirection() {
		return directionTo;
	}

	/**
	 * The game is over. Displays a message to that effect.
	 */
	private void finalScreen() {
		display.setLegend(GAME_OVER);
		display.removeKeyListener(this);
	}

	/**
	 * Place a new ship in the center of the screen. Remove any existing ship
	 * first.
	 */
	private void placeShip() {
		// Place a new ship
		Participant.expire(ship);
		ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
		addParticipant(ship);
		display.setLegend("");
		addLocation(ship, 0);
		propel = false;
		turnRight = false;
		turnLeft = false;
	}

	/**
	 * Place the alien ship
	 */
	private void placeAlien() {
		Participant.expire(enemyShip);
		if (this.level > 1) {
			int alienShipSize = 0;
			if (level == 2) {
				alienShipSize = 1;
			} else {
				alienShipSize = Constants.RANDOM.nextInt(2);
			}
			this.enemyShip = new AlienShip(alienShipSize, this);
			this.enemyShip.setPosition(0.0D, 750.0D * Constants.RANDOM.nextDouble());
			this.enemyShip.setVelocity(5 - alienShipSize, Constants.RANDOM.nextInt(2) * 3.141592653589793D);
			addParticipant(this.enemyShip);
		}
	}

	/**
	 * Places the boss
	 */
	public void placeBoss(int n) {
		Participant.expire(boss);
		sound.play("boss");
		this.boss = new Boss(this, n);
		this.boss.setPosition(0.0D, 750.0D * Constants.RANDOM.nextDouble());
		this.boss.setVelocity(4, Constants.RANDOM.nextInt(2) * Math.PI);
		addParticipant(this.boss);

	}

	/**
	 * Place the heat seeking drone
	 */

	public void placeDrone() {
		sound.play("missile");
		drones--;
		display.setDronesLabel(drones);
		// if (pClosest != null) {
		getClosestParticipant();
		checkClosestParticipant();
		drone = new Drone(ship.getXNose(), ship.getYNose(), pClosest.getDirection(), ship.getRotation(), this, 2);
		addParticipant(drone);
		drone.accelerate(ship.getDirection());
		drone.applyFriction(SHIP_FRICTION);

	}

	/**
	 * Places the smaller drone after the bigger one has exploded
	 */

	public void placeSmallDrone() {
		getClosestParticipant();
		checkClosestParticipant();
		if (pClosest != null) {

			if (ship != null) {
				drone = new Drone(drone.getXNose(), drone.getYNose(), pClosest.getDirection(), ship.getRotation(), this,
						1);
				addParticipant(drone);
			}

			addParticipant(new Drone(drone.getXNose(), drone.getYNose(), pClosest.getDirection(), drone.getRotation(),
					this, 1));
			drone.accelerate(pClosest.getDirection());
		}

	}

	/**
	 * Places four asteroids near the corners of the screen. Gives them random
	 * velocities and rotations. if the level is bigger
	 */
	private void placeAsteroids(int n) {

		addParticipant(new Asteroid(0, 2, EDGE_OFFSET, EDGE_OFFSET, 3, this));
		addParticipant(new Asteroid(1, 2, SIZE - EDGE_OFFSET, EDGE_OFFSET, 3, this));
		addParticipant(new Asteroid(2, 2, EDGE_OFFSET, SIZE - EDGE_OFFSET, 3, this));
		addParticipant(new Asteroid(3, 2, SIZE - EDGE_OFFSET, SIZE - EDGE_OFFSET, 3, this));
		if (n > 1) {
			for (int i = 1; i < n; i++) {
				int variety = Constants.RANDOM.nextInt(3);
				addParticipant(new Asteroid(variety, 2, SIZE - EDGE_OFFSET, EDGE_OFFSET - Constants.RANDOM.nextInt(100),
						3, this));
			}
		}
	}

	/**
	 * Places the Nuke
	 * 
	 * @param p
	 */
	public void placeNuke() {

		try {
			sound.play("nuke");
			nuke = new Nuke(this, ship.getXNose(), ship.getYNose(), 0, true, false);
			nuke.setPosition(ship.getX(), ship.getY());
			addParticipant(nuke);
			nukes--;
			display.setNukesLabel(nukes);

		} catch (Exception g) {
			this.nukeDestroyed();
		}
	}

	/**
	 * Shoots the bullets, takes in a participant and decides which bullets to
	 * fire and where to fire them from
	 * 
	 * @param p
	 */

	public void shootBullets(Participant p) {
		if (p.equals(ship)) {
			addParticipant(new Bullet(ship.getXNose(), ship.getYNose(), ship.getRotation(), this));
			if (enhanced) {
				sound.play("pew");
			} else {
				sound.play("fire");
			}
		} else if (p.equals(enemyShip)) {
			addParticipant(
					new ABullet(enemyShip.getX(), enemyShip.getY(), enemyShip.getShootingDirectionToShip(), this));
			sound.play("fire");
		}

	}

	/**
	 * Clears the screen so that nothing is displayed
	 */
	private void clear() {
		pstate.clear();
		display.setLegend("");
		ship = null;
		shipBullets = 0;
		if (this.enemyShip != null) {
			Participant.expire(this.enemyShip);
			this.enemyShip.remove();
			this.enemyShip = null;
		}
	}

	/**
	 * Sets things up and begins a new game.
	 */
	private void initialScreen() {
		// Clear the screen
		clear();
		initiatLists();
		setMinDistance();
		if (!enhanced) {
			this.beatTimer.stop();
			this.beatTimer.setDelay(900);
			this.playBeat1 = true;
			this.beatTimer.start();
		}

		if (enhanced) {
			getSounds().play("enhancedMusic");

		}
		// Place the ship
		placeShip();
		// Reset statistics
		lives = 100;
		score = 0;
		level = 1;
		shipBullets = 0;
		drones = 0;
		mines = 0;

		nukes = 0;
		if (enhanced) {
			display.setDronesLabel(drones);
			display.setNukesLabel(nukes);
			display.setMinesLabel(mines);
		}
		shield = false;
		placeAsteroids(level);
		// Start listening to events (but don't listen twice)
		display.removeKeyListener(this);
		display.addKeyListener(this);
		getClosestParticipant();
		// Give focus to the game screen
		display.requestFocusInWindow();
	}

	/**
	 * Adds a new Participant
	 */
	public void addParticipant(Participant p) {
		pstate.addParticipant(p);
	}

	/**
	 * The ship has been destroyed
	 */
	public void shipDestroyed() {
		// Null out the ship
		ship = null;
		sound.play("destroyedM");
		this.beatTimer.stop();
		if (enhanced) {
			sound.play("shipDie");
		}
		if (!enhanced) {
			display.setLegend("Ouch!");
		}
		// Decrement lives
		if (lives >= 1) {
			lives--;
			display.setLivesLabel(this.lives);
		}
		// Since the ship was destroyed, schedule a transition
		scheduleTransition(END_DELAY);
	}

	/**
	 * An asteroid of the given size has been destroyed
	 */
	public void asteroidDestroyed(int size) {
		// If all the asteroids are gone, schedule a transition
		addToScore(Constants.ASTEROID_SCORE[size]);

		if (pstate.countAsteroids() == 0) {
			if (!enhanced) {
				this.beatTimer.stop();
			}
			level++;
			if (enhanced) {

				for (int i = 0; i < level - 1; i++) {
					int type = Constants.RANDOM.nextInt(5);

					if (type == 0) {

						placeShieldToken();
					}
					if (type == 1) {

						placeNukeToken();
					}
					if (type == 2) {

						placeDroneToken();
					}
					if (type == 4) {
						placeMineToken();
					}
				}

				if (level >= 2) {
					drones++;
				}
			}
			display.setLevelLabel(level);
			if (enhanced) {
				sound.play("levelWon");
				display.setDronesLabel(drones);
			}

			scheduleTransition(END_DELAY);

		}
	}

	/**
	 * Adds to the score
	 * 
	 * @param s
	 */

	public void addToScore(int s) {
		// adds to the score and sets the label
		this.score += s;
		this.display.setScoreLabel(this.score);
	}

	public void bulletDestroyed() {
		// decrement bullets
		shipBullets--;

	}

	public void alienDestroyed() {
		this.enemyShip = null;

		if (this.ship != null) {
			scheduleTransition((int) Math.round(5000.0D * (Constants.RANDOM.nextDouble() + 1.0D)));
		}

	}

	/**
	 * removes the drone from the game
	 */

	public void droneDestroyed() {

	}

	/**
	 * Schedules a transition m msecs in the future
	 */
	private void scheduleTransition(int m) {
		transitionTime = System.currentTimeMillis() + m;
	}

	/**
	 * This method will be invoked because of button presses and timer events.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// The start button has been pressed. Stop whatever we're doing
		// and bring up the initial screen

		if (e.getSource() instanceof JButton) {
			initialScreen();

			display.setLivesLabel(lives);
			display.setLevelLabel(level);
			display.setScoreLabel(score);

		} else if (e.getSource() == beatTimer && !enhanced) {
			// Sets the beat of the game
			String beat = playBeat1 ? "beat1" : "beat2";
			playBeat1 = (!playBeat1);
			sound.play(beat);
			beatTimer.setDelay(Math.max(300, beatTimer.getDelay() - 8));
		}

		// Time to refresh the screen and deal with keyboard input
		else if (e.getSource() == refreshTimer) {
			// It may be time to make a game transition
			performTransition();
			if (ship != null) {
				addLocation(ship, 0);
			}
			// Move the participants to their new locations
			pstate.moveParticipants();

			// Refresh screen
			display.refresh();

			try {
				if (drone != null && pClosest != null) {
					double deltaX = pClosest.getX() - drone.getX();
					double deltaY = pClosest.getY() - drone.getY();
					double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
					double difference = deltaY / deltaX;
					double direction = Math.acos(deltaX / distance);
					double angle = Math.atan2(deltaY, deltaX);
					drone.move();
					// drone.applyFriction(SHIP_FRICTION);
					drone.accelerate(direction);
					if (difference == 0) {
						drone.accelerate(direction);
						drone.applyFriction(SHIP_FRICTION);
					}
					if (deltaY > 0) {
						// drone.applyFriction(SHIP_FRICTION);
						drone.rotate(angle / 20);
						// drone.accelerate(direction);
						drone.applyFriction(SHIP_FRICTION);
					}
					if (deltaY < 0) {
						// drone.applyFriction(SHIP_FRICTION);
						drone.rotate(-angle / 20);
						// drone.accelerate(-direction);
						drone.applyFriction(SHIP_FRICTION);
					}
					updateShipLocation();
					checkClosestParticipant();
				}
			} catch (Exception g) {

			}

			if (enhanced && shield) {
				if (ship != null && shieldP != null) {
					shieldP.setPosition(ship.getX() - 1, ship.getY() - 1);
					shieldP.setDirection(ship.getDirection());
					shieldP.setRotation(ship.getRotation());
					shieldP.accelerate(ship.getSpeed() - 1);
					shieldP.setRotation(ship.getRotation());
					shieldP.setDirection(ship.getDirection());
				} else {
					Participant.expire(shieldP);
				}

			}

			// Moves the ship based on the timer
			if ((this.turnLeft) && (this.ship != null)) {
				this.ship.turnLeft();
				if (shieldP != null) {
					shieldP.rotate(ship.getRotation());
					sound.play("droneTurn");
				}
			}

			if ((this.turnRight) && (this.ship != null)) {
				this.ship.turnRight();
				if (shieldP != null) {
					shieldP.rotate(ship.getRotation());
					sound.play("droneTurn");
				}

			}

			if ((this.propel) && (this.ship != null)) {
				this.ship.accelerate();
			} else if (this.ship != null) {
				this.ship.coast();
			}

		}

	}

	/**
	 * Returns an iterator over the active participants
	 */
	public Iterator<Participant> getParticipants() {
		return pstate.getParticipants();
	}

	/**
	 * If the transition time has been reached, transition to a new state
	 */
	private void performTransition() {
		// Do something only if the time has been reached
		if (transitionTime <= System.currentTimeMillis()) {
			// Clear the transition time
			transitionTime = Long.MAX_VALUE;
			setMinDistance();
			// Adds a random token at every transition
			if (enhanced) {
				if (level > 1) {
					int type = Constants.RANDOM.nextInt(5);
					addParticipant(new Token(type, this));
				}
			}
			// If there are no lives left, the game is over. Show the final
			// screen.

			if (lives <= 0) {
				if (enhanced) {
					sound.stop("mute");
					sound.play("gameOverBro");
				}
				finalScreen();
			}

			if (pstate.countAsteroids() == 0) {
				if (!enhanced) {
					this.beatTimer.setDelay(900);
					this.beatTimer.restart();
				}
				placeAsteroids(level);
			}

			// If the ship was destroyed, place a new one and continue restart
			// the beatTimer
			if (ship == null && lives != 0) {
				if (!enhanced) {
					this.beatTimer.restart();
				}
				placeShip();

			} // If the alien ship was destroyed place a new one
			if (enemyShip == null) {
				placeAlien();
			} // Place a new boss after level 5
			if (boss == null && level > 1) {
				
				sound.play("boss enter");
				sound.stop("mute");
			}
		}
	}

	/**
	 * Resets the nuke
	 */
	public void nukeDestroyed() {
		Participant.expire(nuke);
		nuke = null;
	}

	/**
	 * If a key of interest is pressed, record that it is down.
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W && ship != null) {
			propel = true;
			sound.play("thrust");
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A && ship != null) {
			turnLeft = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D && ship != null) {
			turnRight = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_SPACE && ship != null) {
			if (shipBullets <= BULLET_LIMIT) {
				try {
					shootBullets(ship);
					shipBullets++;
				} catch (Exception g) {

				}
			}
		}
		// Places the drone by key press of G

		if (e.getKeyCode() == KeyEvent.VK_G && ship != null) {
			if (enhanced && drones > 0) {
				placeDrone();
			}

		}
		// Places the nuke by key press of N
		if (e.getKeyCode() == KeyEvent.VK_N && ship != null) {
			if (nuke == null && enhanced && nukes > 0) {
				placeNuke();
			}

		}
		if (e.getKeyCode() == KeyEvent.VK_M && ship != null) {
			placeBoss(4);
			if (enhanced && mines > 0) {
				placeMine();
				sound.play("mine");

			}
		}

	}

	/**
	 * Ignore these events.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (ship != null) {
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				propel = false;
				sound.stop("thrust");
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				turnLeft = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				turnRight = false;
			}
		}
	}

	/**
	 * Places the shield token
	 */
	public void placeShieldToken() {
		addParticipant(new Token(1, this));

	}

	/**
	 * Places the Nuke Token
	 */

	public void placeNukeToken() {
		addParticipant(new Token(0, this));

	}

	/**
	 * Places drone token
	 */

	public void placeDroneToken() {
		addParticipant(new Token(2, this));

	}

	/**
	 * Places the mine
	 */
	public void placeMine() {
		mines--;
		display.setMinesLabel(mines);
		addParticipant(new Nuke(this, ship.getX(), ship.getY(), 0, false, true));
	}

	/**
	 * Adds to the mines
	 */
	public void addMines() {
		mines++;
		display.setMinesLabel(mines);
	}

	/**
	 * Places the mine token
	 */
	public void placeMineToken() {
		addParticipant(new Token(4, this));
	}

}
