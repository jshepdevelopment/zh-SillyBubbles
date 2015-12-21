package com.sillybubbles.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

public class SillyBubblesGame extends Game {


    final int itemWidth = 32;
    final int itemHeight = 32;

	// create the items and count
	PrizeItem diamondItem = new PrizeItem("Diamond", 0);
	PrizeItem firstAidItem = new PrizeItem("First Aid", 0);
	PrizeItem starItem = new PrizeItem("Star", 0);
    PrizeItem bookItem = new PrizeItem("Book", 0);
    PrizeItem crystalItem = new PrizeItem("Crystal", 0);
    PrizeItem ringItem = new PrizeItem("Ring", 0);
    PrizeItem jewel1Item = new PrizeItem("Jewel1", 0);
    PrizeItem jewel2Item = new PrizeItem("Jewel2", 0);
    PrizeItem jewel3Item = new PrizeItem("Jewel3", 0);

    Label diamondLabel;
    Label firstAidLabel;
    Label starLabel;
    Label bookLabel;
    Label crystalLabel;
    Label ringLabel;
    Label jewel1Label;
    Label jewel2Label;
    Label jewel3Label;

	TextureRegion background;
	ParallaxBackground rbg;

    BitmapFont textFont;

    boolean playing = true; // flag to switch between playing and checking items

    class BubbleButton extends Actor {
        private TextureRegion _texture;

        public BubbleButton(TextureRegion texture){
            // set bounds
            _texture = texture;
            setBounds(getX(), getY(), _texture.getRegionWidth(), _texture.getRegionHeight());

            // get input
            this.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
                    return true;
                }
            });
        }

        // implements draw() completely to handle rotation and scaling
        public void draw(Batch batch, float alpha) {
            batch.draw(_texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        }

        public Actor hit(float x, float y, boolean touchable) {

            // get centerpoint of bounding circle, also known as the center of the rect
            float centerX = getWidth() / 2;
            float centerY = getHeight() / 2;

            // Calculate radius of circle
            float radius = (float) Math.sqrt(centerX * centerX +
                    centerY * centerY);

            // And distance of point from the center of the circle
            float distance = (float) Math.sqrt(((centerX - x) * (centerX - x))
                    + ((centerY - y) * (centerY - y)));

            // If the distance is less than the circle radius, it's a hit
            if (distance <= radius) {
                if (Gdx.input.justTouched()) {
                        Gdx.app.log("JSLOG", "Bubble button pressed.");

                        diamondLabel.setText(" " + diamondItem.getItemCount());
                        firstAidLabel.setText(" " + firstAidItem.getItemCount());
                        starLabel.setText(" " + starItem.getItemCount());
                        bookLabel.setText(" " + bookItem.getItemCount());
                        crystalLabel.setText(" " + crystalItem.getItemCount());
                        ringLabel.setText(" " + ringItem.getItemCount());
                        jewel1Label.setText(" " + jewel1Item.getItemCount());
                        jewel2Label.setText(" " + jewel2Item.getItemCount());
                        jewel3Label.setText(" " + jewel3Item.getItemCount());

                        Gdx.input.setInputProcessor(menuStage);
                        playing = false;
                }
            }
            // button not pressed, return null
            return null;
        }

        @Override
        public void act(float delta) {
            this.setPosition(0, Gdx.graphics.getHeight() - _texture.getRegionHeight());
        }
    }

    class BubbleBackButton extends Actor {
        private TextureRegion _texture;

        public BubbleBackButton(TextureRegion texture){
            // set bounds
            _texture = texture;
            setBounds(getX(), getY(), _texture.getRegionWidth(), _texture.getRegionHeight());

            // get input
            this.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
                    return true;
                }
            });
        }

        // implements draw() completely to handle rotation and scaling
        public void draw(Batch batch, float alpha) {
            batch.draw(_texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        }

        public Actor hit(float x, float y, boolean touchable) {

            // get centerpoint of bounding circle, also known as the center of the rect
            float centerX = getWidth() / 2;
            float centerY = getHeight() / 2;

            // Calculate radius of circle
            float radius = (float) Math.sqrt(centerX * centerX +
                    centerY * centerY);

            // And distance of point from the center of the circle
            float distance = (float) Math.sqrt(((centerX - x) * (centerX - x))
                    + ((centerY - y) * (centerY - y)));

            // If the distance is less than the circle radius, it's a hit
            if (distance <= radius) {
                if (Gdx.input.justTouched()) {
                    Gdx.app.log("JSLOG", "Bubble back button pressed.");
                    Gdx.input.setInputProcessor(stage);
                    playing = true;
                }
            }

            // button not pressed, return null
            return null;
        }

        @Override
        public void act(float delta) {
            this.setPosition(0, Gdx.graphics.getHeight() - _texture.getRegionHeight());
        }
    }

	// create Bubble as an Actor and show the texture region
	class Bubble extends Actor {

		private TextureRegion texture;
		private TextureRegion prizeTexture;
        boolean poof = false; // used to 'pop' the bubble
		int prizeID = 0;
		int speed = 0;

		public Bubble(){

            Random random = new Random();
            int randomBubble = random.nextInt(8) + 1;

            // set a random texture
            if (randomBubble == 1) this.texture = new TextureRegion(new Texture("bubblewhite.png"));
            if (randomBubble == 2) this.texture = new TextureRegion(new Texture("bubbleblack.png"));
            if (randomBubble == 3) this.texture = new TextureRegion(new Texture("bubbleblue.png"));
            if (randomBubble == 4) this.texture = new TextureRegion(new Texture("bubblegreen.png"));
            if (randomBubble == 5) this.texture = new TextureRegion(new Texture("bubbleorange.png"));
            if (randomBubble == 6) this.texture = new TextureRegion(new Texture("bubblepurple.png"));
            if (randomBubble == 7) this.texture = new TextureRegion(new Texture("bubblered.png"));
            if (randomBubble == 8) this.texture = new TextureRegion(new Texture("bubbleyellow.png"));

            // set bounds
            this.setBounds(getX(), getY(), texture.getRegionWidth(), texture.getRegionHeight());
            //this.scaleModifier = random.nextInt() * 2;
            this.setScale(random.nextFloat() * 2);

			// get input
			this.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
					//Gdx.app.log("JSLOG", "Touched" + getName());
					//setVisible(false);
					return true;
				}
			});
		}

		// implements draw() completely to handle rotation and scaling
		public void draw(Batch batch, float alpha) {

            batch.draw(prizeTexture,
					getX() + getScaleX() * 80,
					getY() + getScaleY() * 80, getOriginX(), getOriginY(),
					getWidth() / 2, getHeight() / 2, getScaleX(), getScaleY(), getRotation());

			batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
					getScaleX(), getScaleY(), getRotation());
		}

        // This hit() instead of checking against a bounding box, checks a bounding circle.
        public Actor hit(float x, float y, boolean touchable){
            // If this Actor is hidden or untouchable, it cant be hit
            if(!this.isVisible() || this.getTouchable() == Touchable.disabled)
                return null;

            // get centerpoint of bounding circle, also known as the center of the rect
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
            if(distance <= radius) {
				// check for justTouched will prevent holding hits
				if(Gdx.input.justTouched()) {
					//Gdx.app.log("JSLOG", "bubble " + this + " hit!");
					if(this.prizeID==1) {
						diamondItem.itemCount++;
						Gdx.app.log("JSLOG", diamondItem.getItemCount() + " Diamonds collected.");
					}
					if(this.prizeID==2) {
						firstAidItem.itemCount++;
						Gdx.app.log("JSLOG", firstAidItem.getItemCount() + " First-aid collected.");
					}
					if(this.prizeID==3) {
						starItem.itemCount++;
						Gdx.app.log("JSLOG", starItem.getItemCount() + " Stars collected.");
					}
                    if(this.prizeID==4) {
                        bookItem.itemCount++;
                        Gdx.app.log("JSLOG", bookItem.getItemCount() + " Books collected.");
                    }
                    if(this.prizeID==5) {
                        crystalItem.itemCount++;
                        Gdx.app.log("JSLOG", crystalItem.getItemCount() + " Crystals collected.");
                    }
                    if(this.prizeID==6) {
                        ringItem.itemCount++;
                        Gdx.app.log("JSLOG", ringItem.getItemCount() + " Rings collected.");
                    }
                    if(this.prizeID==7) {
                        jewel1Item.itemCount++;
                        Gdx.app.log("JSLOG", jewel1Item.getItemCount() + " Jewel1s collected.");
                    }
                    if(this.prizeID==8) {
                        jewel2Item.itemCount++;
                        Gdx.app.log("JSLOG", jewel2Item.getItemCount() + " Jewel2s collected.");
                    }
                    if(this.prizeID==9) {
                        jewel3Item.itemCount++;
                        Gdx.app.log("JSLOG", jewel3Item.getItemCount() + " Jewel3s collected.");
                    }
                    // pop the bubble
                    Gdx.input.vibrate(25);
                    this.reset();
				}
				return this;
			}
            // Otherwise, it isnt
            return null;
        }

		@Override
		public void act(float delta){

            this.setPosition(this.getX(), this.getY() + this.speed);

            // if the bubble wanders to far up the y axis, reset it
            if(this.getY() > Gdx.graphics.getHeight() + texture.getRegionHeight()) {
				this.reset();
			}
		}

		public void reset() {

            this.poof = false;

			Random random = new Random();
			int randomPrize = random.nextInt(20) + 1;

			// set prize
			if (randomPrize == 1) {
				this.prizeTexture = new TextureRegion(new Texture("diamond.png"));
				this.prizeID = 1;
			}
			if (randomPrize == 2) {
				this.prizeTexture = new TextureRegion(new Texture("firstaid.png"));
				this.prizeID = 2;
			}
			if (randomPrize == 3) {
				this.prizeTexture = new TextureRegion(new Texture("star.png"));
				prizeID = 3;
			}
            if (randomPrize == 4) {
                this.prizeTexture = new TextureRegion(new Texture("book.png"));
                this.prizeID = 4;
            }
            if (randomPrize == 5) {
                this.prizeTexture = new TextureRegion(new Texture("crystal.png"));
                this.prizeID = 5;
            }
            if (randomPrize == 6) {
                this.prizeTexture = new TextureRegion(new Texture("ring.png"));
                prizeID = 6;
            }

            if (randomPrize == 7) {
                this.prizeTexture = new TextureRegion(new Texture("jewel1.png"));
                prizeID = 7;
            }
            if (randomPrize == 8) {
                this.prizeTexture = new TextureRegion(new Texture("jewel2.png"));
                prizeID = 8;
            }
            if (randomPrize == 9) {
                this.prizeTexture = new TextureRegion(new Texture("jewel3.png"));
                prizeID = 9;
            }
            if (randomPrize > 9) {
                this.prizeTexture = new TextureRegion(new Texture("empty.png"));
                prizeID = 0;// no prize ID is no prize
            }

            //Assign the position of the bubble to a random value within the screen boundaries
			int randomX = random.nextInt(Gdx.graphics.getWidth());
			//this.setVisible(true);
			//this.setScale(random.nextFloat());
			this.speed =  random.nextInt(20) + 10;
			this.setPosition(randomX, -3000);

            int randomBubble = random.nextInt(8) + 1;

            Gdx.app.log("JSLOG", "Random bubble color " + randomBubble + " set.");

            // set a random texture
            if (randomBubble == 1) this.texture = new TextureRegion(new Texture("bubblewhite.png"));
            if (randomBubble == 2) this.texture = new TextureRegion(new Texture("bubbleblack.png"));
            if (randomBubble == 3) this.texture = new TextureRegion(new Texture("bubbleblue.png"));
            if (randomBubble == 4) this.texture = new TextureRegion(new Texture("bubblegreen.png"));
            if (randomBubble == 5) this.texture = new TextureRegion(new Texture("bubbleorange.png"));
            if (randomBubble == 6) this.texture = new TextureRegion(new Texture("bubblepurple.png"));
            if (randomBubble == 7) this.texture = new TextureRegion(new Texture("bubblered.png"));
            if (randomBubble == 8) this.texture = new TextureRegion(new Texture("bubbleyellow.png"));

		}
	}

	private Stage stage;
    private Stage menuStage;

	@Override
	public void create() {

        // setting up font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("cartoon.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        textFont = generator.generateFont(parameter);

        Preferences prefs = Gdx.app.getPreferences("BubblePrefs"); // load the prefs file

        diamondItem.itemCount = prefs.getInteger(diamondItem.itemName);
        firstAidItem.itemCount = prefs.getInteger(firstAidItem.itemName);
        starItem.itemCount = prefs.getInteger(starItem.itemName);
        bookItem.itemCount = prefs.getInteger(bookItem.itemName);
        crystalItem.itemCount = prefs.getInteger(crystalItem.itemName);
        ringItem.itemCount = prefs.getInteger(ringItem.itemName);
        jewel1Item.itemCount = prefs.getInteger(jewel1Item.itemName);
        jewel2Item.itemCount = prefs.getInteger(jewel2Item.itemName);
        jewel3Item.itemCount = prefs.getInteger(jewel3Item.itemName);

        // Item images used in menu screen
        Image diamondImage = new Image(new TextureRegion((new Texture("diamond.png"))));
        Image firstAidImage = new Image(new TextureRegion((new Texture("firstaid.png"))));
        Image starImage = new Image(new TextureRegion((new Texture("star.png"))));
        Image bookImage = new Image(new TextureRegion((new Texture("book.png"))));
        Image crystalImage = new Image(new TextureRegion((new Texture("crystal.png"))));
        Image ringImage = new Image(new TextureRegion((new Texture("ring.png"))));
        Image jewel1Image = new Image(new TextureRegion((new Texture("jewel1.png"))));
        Image jewel2Image = new Image(new TextureRegion((new Texture("jewel2.png"))));
        Image jewel3Image = new Image(new TextureRegion((new Texture("jewel3.png"))));

        diamondImage.setScale(2.5f, 2.5f);
        firstAidImage.setScale(2.5f, 2.5f);
        starImage.setScale(2.5f, 2.5f);
        bookImage.setScale(2.5f, 2.5f);
        crystalImage.setScale(2.5f, 2.5f);
        ringImage.setScale(2.5f, 2.5f);
        jewel1Image.setScale(2.5f, 2.5f);
        jewel2Image.setScale(2.5f, 2.5f);
        jewel3Image.setScale(2.5f, 2.5f);

        Label.LabelStyle labelStyle = new Label.LabelStyle(textFont, Color.WHITE);

        diamondLabel = new Label(" " + diamondItem.itemCount, labelStyle);
        firstAidLabel = new Label("  " + firstAidItem.itemCount, labelStyle);
        starLabel = new Label(" " + starItem.itemCount, labelStyle);
        bookLabel = new Label(" " + bookItem.itemCount, labelStyle);
        crystalLabel = new Label("  " + crystalItem.itemCount, labelStyle);
        ringLabel = new Label(" " + ringItem.itemCount, labelStyle);
        jewel1Label = new Label(" " + jewel1Item.itemCount, labelStyle);
        jewel2Label = new Label(" " + jewel2Item.itemCount, labelStyle);
        jewel3Label = new Label(" " + jewel3Item.itemCount, labelStyle);

        diamondImage.setPosition(0, Gdx.graphics.getHeight() - 128*3);
        diamondLabel.setPosition(itemWidth * 5, Gdx.graphics.getHeight() - 128*3);
        firstAidImage.setPosition(0, Gdx.graphics.getHeight() - 160*3);
        firstAidLabel.setPosition(itemWidth * 5, Gdx.graphics.getHeight() - 160*3);
        starImage.setPosition(0, Gdx.graphics.getHeight() - 192*3);
        starLabel.setPosition(itemWidth * 5, Gdx.graphics.getHeight() - 192*3);
        bookImage.setPosition(0, Gdx.graphics.getHeight() - 224*3);
        bookLabel.setPosition(itemWidth * 5, Gdx.graphics.getHeight() - 224*3);
        crystalImage.setPosition(0, Gdx.graphics.getHeight() - 256*3);
        crystalLabel.setPosition(itemWidth * 5, Gdx.graphics.getHeight() - 256*3);
        ringImage.setPosition(0, Gdx.graphics.getHeight() - 288*3);
        ringLabel.setPosition(itemWidth * 5, Gdx.graphics.getHeight() - 288*3);

        jewel1Image.setPosition(0, Gdx.graphics.getHeight() - 320*3);
        jewel1Label.setPosition(itemWidth * 5, Gdx.graphics.getHeight() - 320*3);
        jewel2Image.setPosition(0, Gdx.graphics.getHeight() - 352*3);
        jewel2Label.setPosition(itemWidth * 5, Gdx.graphics.getHeight() - 352*3);
        jewel3Image.setPosition(0, Gdx.graphics.getHeight() - 384*3);
        jewel3Label.setPosition(itemWidth * 5, Gdx.graphics.getHeight() - 384*3);

        background = new TextureRegion(new Texture("hills.png"));

		Bubble[] bubbles;
		int bubbleCount = 15;

		// stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
		stage = new Stage(new ScreenViewport());

        // this is a simple game, so we just gonna have a menu type list of viewable items
        // on a seperate stage in the same class
        menuStage = new Stage(new ScreenViewport());

		// button textutes
        final TextureRegion bubbleButtonTexture = new TextureRegion(new Texture("bubblebutton.png"));
        final TextureRegion bubbleBackButtonTexture = new TextureRegion(new Texture("bubblebackbutton.png"));

        rbg = new ParallaxBackground(new ParallaxLayer[]{
				//	new ParallaxLayer(background, new Vector2(),new Vector2(0, 0)),
				new ParallaxLayer(background,new Vector2(1.0f,1.0f),new Vector2(0, 500)),
				//	new ParallaxLayer(background,new Vector2(0.1f,0),new Vector2(0, stage.getViewport().getScreenHeight()-200),new Vector2(0, 0)),
		}, stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight(), new Vector2(150,0));

		// random number seed
		Random random = new Random();

        // add a bubbleButton
        BubbleButton bubbleButton = new BubbleButton(bubbleButtonTexture);
        BubbleBackButton bubbleBackButton = new BubbleBackButton(bubbleBackButtonTexture);

		// initialize arrays for bubbles
		bubbles = new Bubble[bubbleCount];

		// make 10 bubble objects at random on screen locations
		for(int i = 0; i < bubbleCount; i++){
			bubbles[i] = new Bubble();
			//moveActions[i] = new MoveToAction();
            			// random.nextInt(Gdx.graphics.getWidth());
			bubbles[i].speed =  random.nextInt(20) + 10;
			bubbles[i].setPosition(bubbles[i].getX(), -3000);

			// set the name of the bubble to it's index within the loop
			bubbles[i].setName(Integer.toString(i));
            bubbles[i].reset();

			// add to the stage
			stage.addActor(bubbles[i]);

		}

        // we will button on top, because it's like a HUD
        stage.addActor(bubbleButton);

        // menuStage shows a list of collected items
        // images
        menuStage.addActor(diamondImage);
        menuStage.addActor(firstAidImage);
        menuStage.addActor(starImage);
        menuStage.addActor(bookImage);
        menuStage.addActor(crystalImage);
        menuStage.addActor(ringImage);
        menuStage.addActor(jewel1Image);
        menuStage.addActor(jewel2Image);
        menuStage.addActor(jewel3Image);

        // count
        menuStage.addActor(diamondLabel);
        menuStage.addActor(firstAidLabel);
        menuStage.addActor(starLabel);
        menuStage.addActor(bookLabel);
        menuStage.addActor(crystalLabel);
        menuStage.addActor(ringLabel);
        menuStage.addActor(jewel1Label);
        menuStage.addActor(jewel2Label);
        menuStage.addActor(jewel3Label);

        menuStage.addActor(bubbleBackButton);

        Gdx.input.setInputProcessor(stage);

	}

	@Override
	public void dispose() {
		stage.dispose();
        menuStage.dispose();
	}

	@Override
	public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(playing) {
            rbg.render(Gdx.graphics.getDeltaTime());
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }

        else {
            rbg.render(Gdx.graphics.getDeltaTime());
            menuStage.act(Gdx.graphics.getDeltaTime());
            menuStage.draw();
        }
	}

	@Override
    public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
        menuStage.getViewport().update(width, height, true);
		rbg = new ParallaxBackground(new ParallaxLayer[]{
				//	new ParallaxLayer(background, new Vector2(),new Vector2(0, 0)),
				new ParallaxLayer(background,new Vector2(1.0f,1.0f),new Vector2(0, 500)),
				//	new ParallaxLayer(background,new Vector2(0.1f,0),new Vector2(0, stage.getViewport().getScreenHeight()-200),new Vector2(0, 0)),
		}, stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight()/2, new Vector2(150,0));
	}

	@Override
	public void pause() {

        // save the amount of items collected if game is paused
        Preferences prefs = Gdx.app.getPreferences("BubblePrefs");// We store the value 10 with the key of "highScore"
        prefs.putInteger(diamondItem.itemName, diamondItem.itemCount);
        prefs.putInteger(firstAidItem.itemName, firstAidItem.itemCount);
        prefs.putInteger(starItem.itemName, starItem.itemCount);
        prefs.putInteger(bookItem.itemName, bookItem.itemCount);
        prefs.putInteger(crystalItem.itemName, crystalItem.itemCount);
        prefs.putInteger(ringItem.itemName, ringItem.itemCount);
        prefs.flush(); // saves the preferences file

        Gdx.app.log("JSLOG", "Game Paused.");
		//Gdx.app.log("JSLOG", "You have " + diamondItem.getItemCount() + " diamonds.");

	}

	@Override
	public void resume() {
		Gdx.app.log("JSLOG", "Game On.");
	}
}