package com.sillybubbles.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.Random;

public class SillyBubblesGame implements ApplicationListener {

	// create Actor "Bubble that displays TextureRegion passed
	class Bubble extends Actor {
		private TextureRegion _texture;
		//float bubbleX = 0, bubbleY = 0;
		public boolean started = false;
		int speed = 0;

		// random number seed
		Random random = new Random();
		int randomX = random.nextInt(Gdx.graphics.getWidth());

		public Bubble(TextureRegion texture){
			_texture = texture;
			setBounds(getX(), getY(), _texture.getRegionWidth(), _texture.getRegionHeight());

			this.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
					Gdx.app.log("JSLOG", "Touched" + getName());
					setVisible(false);
					return true;
				}
			});
		}


		// implements draw() completely to handle rotation and scaling
		public void draw(Batch batch, float alpha) {
			batch.draw(_texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
					getScaleX(), getScaleY(), getRotation());
		}


        /*
        // This hit() instead of checking against a bounding box, checks a bounding circle.
        public Actor hit(float x, float y, boolean touchable){
            // If this Actor is hidden or untouchable, it cant be hit
            if(!this.isVisible() || this.getTouchable() == Touchable.disabled)
                return null;

            // Get centerpoint of bounding circle, also known as the center of the rect
            float centerX = getWidth()/2;
            float centerY = getHeight()/2;

            // Square roots are bad m'kay. In "real" code, simply square both sides for much speedy fastness
            // This however is the proper, unoptimized and easiest to grok equation for a hit within a circle
            // You could of course use LibGDX's Circle class instead.

            // Calculate radius of circle
            float radius = (float) Math.sqrt(centerX * centerX +
                    centerY * centerY);

            // And distance of point from the center of the circle
            float distance = (float) Math.sqrt(((centerX - x) * (centerX - x))
                    + ((centerY - y) * (centerY - y)));

            // If the distance is less than the circle radius, it's a hit
            if(distance <= radius) return this;

            // Otherwise, it isnt
            return null;
        }*/

		@Override
		public void act(float delta){


			this.setPosition(this.getX(), this.getY() + this.speed);
			Gdx.app.log("JSLOG", "this.getY() " + this.getY() + " this.getX() " + this.getX());

			if(this.getY() > Gdx.graphics.getHeight() + _texture.getRegionHeight()) {
				this.reset();
			}

			if(this.started){

				this.speed++;
				//started=false;
			}
		}

		public void reset() {

			//Assign the position of the bubble to a random value within the screen boundaries
			int randomX = random.nextInt(Gdx.graphics.getWidth());
			this.setVisible(true);
			//this.setScale(random.nextFloat());
			this.speed =  random.nextInt(20) + 10;
			this.setPosition(randomX, -3000);

			Gdx.app.log("JSLOG", "bubble " + " reset.");
			//Gdx.app.log("JSLOG", "randomX " + randomX);

		}
	}

	private Bubble[] bubbles;
	//private MoveToAction[] moveActions;

	private int bubbleCount = 8;
	private Stage stage;

	@Override
	public void create() {
		// stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
		stage = new Stage();
		final TextureRegion bubbleTexture = new TextureRegion(new Texture("bubble.png"));

		// random number seed
		Random random = new Random();

		// initialize arrays
		bubbles = new Bubble[bubbleCount];
		// moveActions = new MoveToAction[bubbleCount];

		// make 10 bubble objects at random on screen locations
		for(int i = 0; i < bubbleCount; i++){
			bubbles[i] = new Bubble(bubbleTexture);
			//moveActions[i] = new MoveToAction();

			// random.nextInt(Gdx.graphics.getWidth());
			bubbles[i].setScale(random.nextFloat() * 2);
			bubbles[i].speed =  random.nextInt(20) + 10;
			bubbles[i].setPosition(bubbles[i].getX(), -3000);

			// set the name of the bubble to it's index within the loop
			bubbles[i].setName(Integer.toString(i));
			bubbles[i].reset();


			// add to the stage
			stage.addActor(bubbles[i]);
		}

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}