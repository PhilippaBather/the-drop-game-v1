package com.batherphilippa.thegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Bucket {
    private final Texture texture; // loaded img stored into RAM
    private final Rectangle bucket;

    public Bucket(Texture texture) {
        this.texture = texture;
        this.bucket = new Rectangle();
        // position bucket in the middle of the x-axis
        this.bucket.x = (800 / 2) - (64 / 2);
        // 20px from bottom of screen; y-axis is pointing upward (hence not 480 - 20)
        this.bucket.y = 20;
        this.bucket.width = 64;
        this.bucket.height = 64;
    }

    public Rectangle getBucketRectangle() {
        return this.bucket;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getBucketXCoord() {
        return this.bucket.x;
    }

    public float getBucketYCoord() {
        return this.bucket.y;
    }

    public void manageInput(OrthographicCamera camera, Vector3 touchPos) {
        if (!Gdx.input.isTouched()) {
            manageKeyPress();
        } else {
            manageTouchMousePress(camera, touchPos);        }

    }

    private void manageKeyPress() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // Gdx.graphics.getDeltaTime() returns the time passed between the last and the current frame in seconds.
            this.bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // Gdx.graphics.getDeltaTime() returns the time passed between the last and the current frame in seconds.
            this.bucket.x += 200 * Gdx.graphics.getDeltaTime();
        }
    }

    private void manageTouchMousePress(OrthographicCamera camera, Vector3 touchPos) {
        if (Gdx.input.isTouched()) {
            // transform touch/mouse coordinates to camer'as coordinate system.
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            // vector now contains touch/mouse coordinates in the coordinate system our bucket lives in
            camera.unproject(touchPos);
            // change position of bucket to be centered around the touch/mouse coordinates
            this.bucket.x = touchPos.x - 64 / 2;
        }
    }


    public void checkBucketInBounds() {
        if (this.bucket.x < 0) {
            this.bucket.x = 0;
        }

        if (this.bucket.x > 800 - 64) {
            this.bucket.x = (800 - 64);
        }
    }

}
