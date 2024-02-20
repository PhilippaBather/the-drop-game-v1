package com.batherphilippa.thegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class TheGame extends ApplicationAdapter {
	private Sound dropSound;
	private Music rainMusic;
	// ensure can render using target resolution of 800 by 480 px no matter the screen resolution
	private OrthographicCamera camera;
	// class that is used to draw 2D images, like the Textures loaded
	private SpriteBatch batch;
	private Bucket bucket;
	private Vector3 touchPos;
	// Array is a libGDX utility class (as opposed to using standard Java collections, e.g. ArrayList, which would produce garbage)
	// So, Array is a garbage collector aware collection (also hash-maps or sets)
	private Array<Raindrop> raindrops;
	private long lastDropTime;

	@Override
	public void create() {
		loadSoundFX();

		camera = new OrthographicCamera();
		// ensure that the camera always shows an area of game world that is 800 by 480 units wide
		camera.setToOrtho(false, 800, 400);

		batch = new SpriteBatch();

		bucket = new Bucket(new Texture(Gdx.files.internal("bucket.png")));
		raindrops = new Array<Raindrop>();
		spawnRaindrop();

		// use 3D vector as OrthographicCamera is actually a 3D camera that takes into account the z coordinates also
		touchPos = new Vector3();

		super.create();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1); // r, g, b, a
		// tell camera of update
		// cameras use a mathematical entity called a matrix responsible for setting up the coordinate system for rendering
		// these matrices need to be recomputed everytime we change a property of the camera, e.g. position
		// NOT done here, but good practice to update the camera once per frame
		camera.update();

		// render bucket: use coordinate system specified by camera: camera.combined is a projection matrix
		// SpriteBatch will now render everything onto the coordinate system
		batch.setProjectionMatrix(camera.combined);
		// start new batch
		batch.begin();
		// once end is called, drawing cases re-submitted at once, so can add other batch.draw(...) here
		// speeds up rendering
		batch.draw(bucket.getTexture(), bucket.getBucketXCoord(), bucket.getBucketYCoord());
		for(Raindrop raindrop: raindrops) {
			batch.draw(raindrop.getTexture(), raindrop.getXCoord(), raindrop.getYCoord());
		}
		batch.end();

		bucket.manageInput(camera, touchPos); // manage bucket movement
		bucket.checkBucketInBounds();

		checkLastDropTime(); // check last time raindrop spawned; create if necessary
		moveRaindrops();
	}

	private void loadSoundFX() {
		// load sound effects, which stored in memory - use sound if music is < 10sec
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		// load music, which is streamed from where its is stored (too big to be kept in memory)
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		// start playback of background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();
	}

	private void spawnRaindrop() {
		Raindrop raindrop = new Raindrop(new Texture(Gdx.files.internal("drop.png")));
		raindrops.add(raindrop);
		lastDropTime = raindrop.getLastDropTime();
	}

	private void moveRaindrops() {
		for(Iterator<Raindrop> iter = raindrops.iterator(); iter.hasNext(); ){
			Raindrop raindrop = iter.next();
			raindrop.setYCoord(200 * Gdx.graphics.getDeltaTime());
			if (raindrop.getYCoord() + 64 < 0) {
				iter.remove();
			}
			if (raindrop.getRaindrop().overlaps(bucket.getBucketRectangle())) {
				dropSound.play();
				iter.remove();
			}
		}
	}

	private void checkLastDropTime() {
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000){
			spawnRaindrop();
		}
	}

	@Override
	public void dispose () {
		bucket.getTexture().dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
}
