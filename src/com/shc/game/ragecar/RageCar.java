package com.shc.game.ragecar;

import com.shc.game.ragecar.states.IntroState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.input.Keyboard;

import static com.shc.game.ragecar.Main.*;

/**
 * This class represents the game itself, and implements the game by overriding methods from the Game class.
 *
 * @author Sri Harsha Chilakapati
 */
public class RageCar extends Game
{
    static
    {
        // We're not in development mode anymore
        development = false;
    }

    @Override
    public void init()
    {
        // Initially start at the game's design resolution
        Display.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        Display.centerOnScreen();

        // Load the resources
        Resources.load();

        // Set the intro state
        setGameState(new IntroState());
    }

    @Override
    public void update(float delta)
    {
        // Key F1 is used to switch between fullscreen and windowed modes
        if (Keyboard.isClicked(Keyboard.KEY_F1))
        {
            Display.setFullScreen(!Display.isFullScreen());

            if (Display.isFullScreen())
                Display.hideCursor();
            else
                Display.showCursor();

            resize();
        }

        // Keep the background (road) scrolling
        BACKGROUND_SCROLL += 3 * GAME_SPEED;
        if (BACKGROUND_SCROLL > CANVAS_HEIGHT)
            BACKGROUND_SCROLL = 0;

        // Update the window title to include FPS, UPS and render calls per frame
        Display.setTitle("RageCar | FPS: " + getFPS()
                         + " | UPS: " + getUPS()
                         + " | RC: " + SilenceEngine.graphics.renderCallsPerFrame);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        // Draw the background (road)
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.drawTexture(Resources.Textures.ROAD, 0, BACKGROUND_SCROLL, CANVAS_WIDTH, CANVAS_HEIGHT);

        if (BACKGROUND_SCROLL > 0)
            g2d.drawTexture(Resources.Textures.ROAD, 0, BACKGROUND_SCROLL - CANVAS_HEIGHT, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    @Override
    public void resize()
    {
        // Resize the game according to aspect ratio
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        OrthoCam cam = g2d.getCamera();

        float displayWidth = Display.getWidth();
        float displayHeight = Display.getHeight();

        float aspectRatio = Display.getAspectRatio();

        float cameraWidth, cameraHeight;

        if (displayWidth < displayHeight)
        {
            cameraWidth = CANVAS_WIDTH;
            cameraHeight = CANVAS_WIDTH / aspectRatio;
        }
        else
        {
            cameraWidth = CANVAS_HEIGHT * aspectRatio;
            cameraHeight = CANVAS_HEIGHT;
        }

        cam.initProjection(cameraWidth, cameraHeight);
        cam.center(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2);
    }

    @Override
    public void dispose()
    {
        Resources.dispose();
    }
}
