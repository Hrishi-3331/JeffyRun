package com.hrishi_3331.hrishistudio.bingorun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class BingoRun extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture[] jumpMan;
	Texture cacti;
	Texture hitMan;
	Texture start;
	Texture play;
	private int state;
	private int pause;
	private float gravity;
	private float velocity;
	private float manY;
	private float manYpermanent;
	private ArrayList<Integer> cactiX;
	private ArrayList<Integer> coinX;
	private ArrayList<Integer> coinY;
	private ArrayList<Rectangle> cactiRectangle;
	private Rectangle manRectangle;
	private int cactiCount;
	private int Gamesatate;
	private int score;
	private BitmapFont font;
	private BitmapFont instructionFont;
	private int scorePause;
	Texture coin;
	Random random;
	private int coinCount;
	private ArrayList<Rectangle> coinRectangle;
	Sound sound;
	int soundPause;
	Sound alarm;
	boolean alarmplay;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.jpg");
		man = new Texture[6];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		man[4] = new Texture("frame-5.png");
		man[5] = new Texture("frame-6.png");
		state = 0;
		pause = 0;
		jumpMan = new Texture[2];
		jumpMan[0] = new Texture("jump_up.png");
		jumpMan[1] = new Texture("jump_fall.png");
		gravity = 0.6f;
		velocity = 0;
		manYpermanent = Gdx.graphics.getHeight()/3 - man[state].getHeight()/4;
		manY = manYpermanent;
		cacti = new Texture("cacti.png");
		hitMan = new Texture("hit_frame.png");
		Gamesatate = 0;
		cactiX = new ArrayList<Integer>();
		cactiCount = 0;
		cactiRectangle = new ArrayList<Rectangle>();
		manRectangle = new Rectangle();
		font = new BitmapFont();
		font.setColor(Color.MAROON);
		font.getData().setScale(4);
		score = 0;
		instructionFont = new BitmapFont();
		instructionFont.getData().setScale(8);
		instructionFont.setColor(Color.CHARTREUSE);
		scorePause = 0;
		start = new Texture("start.jpg");
		play = new Texture("play.png");
		coin = new Texture("coin.png");
		coinX = new ArrayList<Integer>();
		coinY = new ArrayList<Integer>();
		coinRectangle = new ArrayList<Rectangle>();
		coinCount = 0;
		random = new Random();
		sound = Gdx.audio.newSound(Gdx.files.internal("data/coin2.wav"));
		soundPause = 0;
		alarm = Gdx.audio.newSound(Gdx.files.internal("data/alarm1.mp3"));
		alarmplay = false;

	}

	private void  makeCacti(){
		float pos = Gdx.graphics.getWidth();
		cactiX.add((int)pos);
	}

	private void makeCoin(){
		float posX = Gdx.graphics.getWidth();
		float posY = cacti.getHeight()/2 + Gdx.graphics.getHeight()/2 * random.nextFloat();
		coinX.add((int) posX);
		coinY.add((int) posY);
	}

	@Override
	public void render () {
			batch.begin();
			batch.draw(start, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			switch (Gamesatate) {
				case 0: {
					sound.pause();
					batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
					batch.draw(play, Gdx.graphics.getWidth()/2 - play.getWidth(), Gdx.graphics.getHeight()/2 - 200, play.getWidth() * 2, play.getHeight() * 2);
					if (Gdx.input.justTouched()) {
						Gamesatate = 1;
					}
				}
				break;

				case 1: {
					if (soundPause !=0 && soundPause < 15){
						soundPause++;
					}
					else if (soundPause == 0){
						soundPause = 0;
					}
					else {
						soundPause = 0;
						sound.stop();
					}

					background.dispose();
					if (scorePause < 10) {
						scorePause++;
					}
					else {
						scorePause = 0;
						score++;
					}

					if (cactiCount < 400) {
						cactiCount++;
					}
					else {
						cactiCount = 0;
						makeCacti();
					}


					if (coinCount < 150) {
						coinCount++;

					}
					else {
						coinCount = 0;
						makeCoin();
					}

					coinRectangle.clear();
						for (int i = 0; i < coinX.size(); i++) {
							batch.draw(coin, coinX.get(i), coinY.get(i), coin.getWidth() , coin.getHeight());
							coinRectangle.add(new Rectangle(coinX.get(i) + 10, coinY.get(i) + 10, coin.getWidth() - 20 , coin.getHeight() - 20));
							coinX.set(i, coinX.get(i) - 6);
						}


					cactiRectangle.clear();
					for (int i = 0; i < cactiX.size(); i++) {
						batch.draw(cacti, cactiX.get(i), manYpermanent, cacti.getWidth() / 2, cacti.getHeight() / 2);
						cactiRectangle.add(new Rectangle(cactiX.get(i) + 50, manYpermanent, cacti.getWidth() / 2 - 180, cacti.getHeight() / 2 - 50));
						cactiX.set(i, cactiX.get(i) - 4);
					}

					if (Gdx.input.justTouched()) {
						velocity = -15;
					}
					velocity = velocity + gravity;
					manY = manY - velocity;
					if (manY <= manYpermanent) {
						manY = manYpermanent;
					}
					if (manY == manYpermanent) {
						run();
						manRectangle = new Rectangle(Gdx.graphics.getWidth() / 3 - man[state].getWidth() + 10, manY + 30, man[state].getWidth() / 2 - 25, man[state].getHeight() / 2 - 25);
					}
					else {
						if (velocity < 0) {
							batch.draw(jumpMan[0], Gdx.graphics.getWidth() / 3 - jumpMan[0].getWidth(), manY, jumpMan[0].getWidth() / 2, jumpMan[0].getHeight() / 2);
							manRectangle = new Rectangle(Gdx.graphics.getWidth() / 3 - jumpMan[0].getWidth(), manY, jumpMan[0].getWidth() / 2, jumpMan[0].getHeight() / 2);
						} else {
							batch.draw(jumpMan[1], Gdx.graphics.getWidth() / 3 - jumpMan[1].getWidth(), manY, jumpMan[1].getWidth() / 2, jumpMan[1].getHeight() / 2);
							manRectangle = new Rectangle(Gdx.graphics.getWidth() / 3 - jumpMan[1].getWidth(), manY, jumpMan[1].getWidth() / 2 - 50, jumpMan[1].getHeight() / 2 - 50);
						}
					}
				}
				break;

				case 2: {
					if (!alarmplay){
						alarm.play();
						alarm.loop();
						alarmplay = true;
					}
					sound.pause();
					instructionFont.draw(batch, "GAME OVER", Gdx.graphics.getWidth() / 2 - 300, (3 * Gdx.graphics.getHeight()) / 4);
					BitmapFont pagain = new BitmapFont();
					pagain.setColor(Color.FIREBRICK);
					pagain.getData().setScale(4);
					pagain.draw(batch, "Click to play again", Gdx.graphics.getWidth() / 2 - 200, (3 * Gdx.graphics.getHeight()) / 4 - 120);
					batch.draw(hitMan, Gdx.graphics.getWidth() / 3 - hitMan.getWidth(), manY, hitMan.getWidth() / 2, hitMan.getHeight() / 2);
					for (int i = 0; i < cactiX.size(); i++) {
						batch.draw(cacti, cactiX.get(i), manYpermanent, cacti.getWidth() / 2, cacti.getHeight() / 2);
					}
					if (Gdx.input.justTouched()) {
						Gamesatate = 1;
						state = 0;
						pause = 0;
						velocity = 0;
						manY = manYpermanent;
						cactiCount = 0;
						score = 0;
						cactiX.clear();
						cactiRectangle.clear();
						coinX.clear();
						coinY.clear();
						coinRectangle.clear();
						coinCount = 0;
						alarm.stop();
						alarmplay = false;
					}
				}
				break;
			}


			for (int i = 0; i < cactiRectangle.size(); i++) {
				if (Intersector.overlaps(manRectangle, cactiRectangle.get(i))) {
					Gamesatate = 2;
					break;
				}
			}

		for (int i = 0; i < coinRectangle.size(); i++) {
			if (Intersector.overlaps(manRectangle, coinRectangle.get(i))) {
				score += 100;
				coinY.set(i, 100000000);
				sound.play();
				sound.loop();
				soundPause = 1;
				break;
			}
		}

			if (manY == manYpermanent && Gamesatate != 2) {
				batch.draw(man[state], Gdx.graphics.getWidth() / 3 - man[state].getWidth(), manY, man[state].getWidth() / 2, man[state].getHeight() / 2);
			}
			font.draw(batch, "Score : " + String.valueOf(score), Gdx.graphics.getWidth() - 400, Gdx.graphics.getHeight() - 50);
			batch.end();
	}


	@Override
	public void dispose () {
		batch.dispose();

	}

	private void run(){
		if (pause <10){
			pause++;
		}
		else {
			pause = 0;
			if (state < 5){
				state++;
			}
			else {
				state = 0;
			}
		}
		batch.draw(man[state], Gdx.graphics.getWidth()/3 - man[state].getWidth(), manY, man[state].getWidth()/2, man[state].getHeight()/2);
	}

}