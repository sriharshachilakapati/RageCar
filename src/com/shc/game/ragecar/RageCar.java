package com.shc.game.ragecar;

import com.shc.game.ragecar.states.PlayState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class RageCar extends Game
{
    public static final int CANVAS_WIDTH = 1280;
    public static final int CANVAS_HEIGHT = 720;

    public static float GAME_SPEED = 0.5f;

    private float backgroundY = 0;

    @Override
    public void init()
    {
        Display.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        Display.centerOnScreen();

        Resources.load();

        setGameState(new PlayState());
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();

        if (Keyboard.isClicked(Keyboard.KEY_F1))
            Display.setFullScreen(!Display.isFullScreen());

        backgroundY += 4 * GAME_SPEED;
        if (backgroundY > CANVAS_HEIGHT)
            backgroundY = 0;

        Display.setTitle("RageCar | FPS: " + getFPS() + " | UPS: " + getUPS() + " | RC: " + SilenceEngine.graphics.renderCallsPerFrame);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.drawTexture(Resources.Textures.ROAD, 0, backgroundY, CANVAS_WIDTH, CANVAS_HEIGHT);

        if (backgroundY > 0)
            g2d.drawTexture(Resources.Textures.ROAD, 0, backgroundY - CANVAS_HEIGHT, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    @Override
    public void resize()
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        OrthoCam cam = g2d.getCamera();

        float displayWidth = Display.getWidth();
        float displayHeight = Display.getHeight();

        float aspectRatio = Display.getAspectRatio();

        float viewportWidth, viewportHeight;

        if (displayWidth < displayHeight)
        {
            viewportWidth = CANVAS_WIDTH;
            viewportHeight = CANVAS_WIDTH / aspectRatio;
        }
        else
        {
            viewportWidth = CANVAS_HEIGHT * aspectRatio;
            viewportHeight = CANVAS_HEIGHT;
        }

        cam.initProjection(viewportWidth, viewportHeight);
        cam.center(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
    }

    @Override
    public void dispose()
    {
        Resources.dispose();
    }
}
