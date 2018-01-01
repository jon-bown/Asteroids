package asteroids;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 * Class that creates the sounds used in enhanced
 * @author Jon Bown
 */
//Sounds to implement: alien destroyed, alien bullets, boss bullets, boss appears, boss music
public class Sound {
	
	
	private Clip fireClip,SaucerClip;
	private Clip destroyedSmall, destroyedMed, destroyedBig;
	private Clip missile, beat, thrust, enhancedMusic, pew, deadShip, levelWon, explosion, gameOver, tokenAppears, lifeOver, oneUP, nuke, beat1, shieldOn, droneFire;
	private Clip droneTurn1, bossMusic, bossLaugh, mine;
	private boolean shipThrust;
	
	
	
	public Sound(){
	     fireClip = createClip("/sounds/fire.wav");
	        SaucerClip = createClip("/sounds/saucerBig.wav");
	        destroyedSmall = createClip("/sounds/bangSmall.wav");
	        destroyedMed = createClip("/sounds/bangMedium.wav");
	        destroyedBig = createClip("/sounds/bangLarge.wav");
	        thrust = createClip("/sounds/thrust.wav");
	        enhancedMusic = createClip("/sounds/PartyBolt.wav");
	        pew = createClip("/sounds/Pew.wav");
	        deadShip = createClip("/sounds/NO.wav");
	        levelWon = createClip("/sounds/LevelWon.wav");
	        explosion = createClip("/sounds/Shot.wav");
	        gameOver = createClip("/sounds/gameOverBro.wav");
	        tokenAppears = createClip("/sounds/powerUp.wav");
	        lifeOver = createClip("/sounds/lostLife.wav");
	        oneUP = createClip("/sounds/1-up.wav");
	        nuke = createClip("/sounds/Nuke.wav");
	        missile = createClip("/sounds/Missile.wav");
	        beat1 = createClip("/sounds/beat2.wav");
	        shieldOn = createClip("/sounds/Lightsaber.wav");
	        droneFire = createClip("/sounds/doubleBlast.wav");
	       droneTurn1 = createClip("/sounds/Hum 5.wav");
	       bossMusic = createClip("/sounds/BossMusic.wav");
	       bossLaugh = createClip("/sounds/BossLaugh.wav");
	       mine = createClip("/sounds/mine.wav");
	    		   
	}

	public void play(String soundFile){
		if(soundFile.equals("fire")){
			 if(fireClip!=null){
				 fireClip.setFramePosition(0);
	            fireClip.start();
			 }
		}
		if(soundFile.equals("pew")){
			 if(pew!=null){
				 pew.setFramePosition(0);
	            pew.start();
			 }
		}
		if(soundFile.equals("gameOverBro")){
		    if(gameOver!=null){
                gameOver.setFramePosition(0);
               gameOver.start();
		    }
		}
		
		if(soundFile.equals("alien")){
			if(SaucerClip!=null){
			SaucerClip.setFramePosition(0);
	            SaucerClip.start();
	            SaucerClip.loop(100);
			}
		}
		if(soundFile.equals("destroyedS")){
			if(destroyedSmall!=null){
			 destroyedSmall.setFramePosition(0);
	            destroyedSmall.start();
			}
		}
		if(soundFile.equals("destroyedM")){
			if(destroyedMed!=null){
			 destroyedMed.setFramePosition(0);
	            destroyedMed.start();
			}
		}
		if(soundFile.equals("destroyedB")){
			if(destroyedBig!=null){
			 destroyedBig.setFramePosition(0);
			 destroyedBig.start();
	      
			}
		}
		if(soundFile.equals("thrust")){
			shipThrust = true;
			if(this.thrust !=null){
				thrust.setFramePosition(0);
				thrust.loop(100);
			}
		}
		if(soundFile.equals("enhancedMusic")){
			//if(enhancedMusic!=null){
				enhancedMusic.setFramePosition(0);
				enhancedMusic.start();
				enhancedMusic.loop(5);
			//}
		}
		if(soundFile.equals("shipDie")){
			deadShip.setFramePosition(10);
			deadShip.start();
		}
		if(soundFile.equals("levelWon")){
			levelWon.setFramePosition(10);
			levelWon.start();
		}
		if(soundFile.equals("shot")){
			explosion.setFramePosition(10);
			explosion.start();
		}
		if(soundFile.equals("tokenAppears")){
			tokenAppears.setFramePosition(0);
			 tokenAppears.start();
		}
		if(soundFile.equals("die")){
			lifeOver.setFramePosition(0);
			 lifeOver.start();
		}
		if(soundFile.equals("1up")){
			oneUP.setFramePosition(0);
			oneUP.start();
		}
		if(soundFile.equals("nuke")){
			nuke.setFramePosition(0);
			nuke.start();
		}
		if(soundFile.equals("missile")){
			missile.setFramePosition(0);
			missile.start();
		}
		if(soundFile.equals("beat1")){
			beat1.setFramePosition(0);
			beat1.start();
		}
		if(soundFile.equals("beat2")){
			
		}
		if(soundFile.equals("shieldOn")){
			shieldOn.setFramePosition(0);
			shieldOn.start();
		}
		if(soundFile.equals("droneFire")){
			droneFire.setFramePosition(0);
			droneFire.start();
		}
		if(soundFile.equals("droneTurn")){
			droneTurn1.setFramePosition(0);
			droneTurn1.start();
			
			
		}
		if(soundFile.equals("boss enter")){
			bossMusic.setFramePosition(300000);
			bossMusic.start();
			bossMusic.loop(5);
			
		}
		if(soundFile.equals("boss")){
			bossLaugh.setFramePosition(0);
			bossLaugh.start();
		}
		if(soundFile.equals("mine")){
			mine.setFramePosition(85000);
			mine.start();
		}
			
			
	}
	public void stop(String soundFile){
		if(soundFile.equals("thrust")){
			thrust.stop();
		}
		if(soundFile.equals("alien")){
			SaucerClip.stop();
		}
		if(soundFile.equals("mute")){
			enhancedMusic.stop();
		}
		if(soundFile.equals("boss")){
			bossMusic.stop();
		}
	}
	
	  public Clip createClip (String soundFile)
	    {
	        // Opening the sound file this way will work no matter how the
	        // project is exported. The only restriction is that the
	        // sound files must be stored in a package.
	        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
	        {
	            // Create and return a Clip that will play a sound file. There are
	            // various reasons that the creation attempt could fail. If it
	            // fails, return null.
	            Clip clip = AudioSystem.getClip();
	            clip.open(AudioSystem.getAudioInputStream(sound));
	            return clip;
	        }
	        catch (LineUnavailableException e)
	        {
	            return null;
	        }
	        catch (IOException e)
	        {
	            return null;
	        }
	        catch (UnsupportedAudioFileException e)
	        {
	            return null;
	        }
	    }

}
