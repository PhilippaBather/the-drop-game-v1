package com.batherphilippa.thegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class Raindrop {

    private final Texture texture; // loaded img stored into RAM
    private final Rectangle raindrop;
    private final long lastDropTime;

    public Raindrop(Texture texture) {
        this.raindrop = new Rectangle();
        this.texture = texture;
        this.lastDropTime = TimeUtils.nanoTime();
        setCoordinates();
    }

    private void setCoordinates() {
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
    }

    public Rectangle getRaindrop() {
        return raindrop;
    }

    public long getLastDropTime() {
        return lastDropTime;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getXCoord() {
        return this.raindrop.x;
    }


    public float getYCoord() {
        return raindrop.y;
    }

    public void setYCoord(float yCoord) {
        this.raindrop.y -= yCoord;
    }

}
