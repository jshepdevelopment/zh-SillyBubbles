package com.sillybubbles.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SillyBubblesGame implements ApplicationListener {

	private SpriteBatch spriteBatch;
	private Texture bubbleImage;
	private OrthographicCamera camera;
	private Sprite bubbleSprite;
	private Vector3 spritePosition = new Vector3();
	private Vector2 spriteVelocity = new Vector2();



	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		bubbleImage = new Texture("bubble.png");

		// camera is used to transform screens touch coordinates into world coordinates.
		camera = new OrthographicCamera();

		// camera will setup a viewport with pixels as units.
		// y-axis points up and origin is lower left of screen.
		camera.setToOrtho(false);

		bubbleSprite = bubbleSprite;
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		bubbleImage.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		spriteBatch.draw(bubbleImage, spritePosition.x, spritePosition.y );
		// drawing sprite to spritebatch.
		//bubbleSprite.draw(spriteBatch);
		spriteBatch.end();

		// if a finger is down, set the sprite's x/y coordinate.
		if (Gdx.input.isTouched()) {
			// the unproject method takes a Vector3 in window coordinates (origin in
			// upper left corner, y-axis pointing down) and transforms it to world
			// coordinates.
			camera.unproject(spritePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		}

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
