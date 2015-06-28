package com.shc.game.ragecar.states;

import com.shc.game.ragecar.Resources;
import com.shc.game.ragecar.entities.CitizenCar;
import com.shc.game.ragecar.entities.PoliceCar;
import com.shc.silenceengine.collision.broadphase.DynamicTree2D;
import com.shc.silenceengine.collision.colliders.SceneCollider2D;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.scene.Scene2D;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.TimeUtils;

import static com.shc.game.ragecar.Main.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class IntroState extends GameState
{
    private Scene2D         scene;
    private SceneCollider2D collider;

    @Override
    public void onEnter()
    {
        GAME_SPEED = 0.5f;

        // Create a scene (used to show dynamic levels in background)
        scene = new Scene2D();

        // Setup a collider (used to prevent cars from colliding)
        collider = new SceneCollider2D(new DynamicTree2D());
        collider.setScene(scene);

        collider.register(CitizenCar.class, CitizenCar.class);
        collider.register(PoliceCar.class, CitizenCar.class);

        // Setup a timer to spawn cars every two seconds
        GameTimer timer = new GameTimer(2, TimeUtils.Unit.SECONDS);
        timer.setCallback(() ->
        {
            if (Game.getGameState() instanceof IntroState)
            {
                scene.addChild(new CitizenCar(MathUtils.random_range(0, CANVAS_WIDTH - 100)));

                if (PoliceCar.INSTANCE_COUNT == 0 && GAME_SPEED > 2 && MathUtils.chance(10))
                    scene.addChild(new PoliceCar(MathUtils.random_range(0, CANVAS_WIDTH - 100)));

                // Start the timer again
                timer.start();
            }
        });
        timer.start();
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();

        if (Keyboard.isClicked(Keyboard.KEY_SPACE))
            Game.setGameState(new PlayState());

        scene.update(delta);
        collider.checkCollisions();

        // Increase the game speed a bit
        GAME_SPEED += 0.001f;
        GAME_SPEED = MathUtils.clamp(GAME_SPEED, 0, 3.5f);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();

        g2d.getCamera().apply();
        scene.render(delta);

        // Render the street lights
        g2d.drawTexture(Resources.Textures.STREET_LIGHTS, 0, BACKGROUND_SCROLL, CANVAS_WIDTH, CANVAS_HEIGHT);

        if (BACKGROUND_SCROLL > 0)
            g2d.drawTexture(Resources.Textures.STREET_LIGHTS, 0, BACKGROUND_SCROLL - CANVAS_HEIGHT, CANVAS_WIDTH, CANVAS_HEIGHT);

        // The ENTER key is used for debug rendering
        if (Keyboard.isPressed(Keyboard.KEY_ENTER))
            debugRender();

        // Render the logo in the center
        g2d.drawTexture(Resources.Sprites.CAR_RED.getTexture(), 350, CANVAS_HEIGHT/2 - 160, 96 * 2, 160 * 2);
        g2d.drawTexture(Resources.Textures.LOGO, 350 + 96 * 2, CANVAS_HEIGHT/2 - Resources.Textures.LOGO.getHeight()/2);

        String distanceString = "Distance: " + ((int) DISTANCE) + "m";
        String damageString = "Damage: " + DAMAGE;

        float x, y;

        float distanceStringWidth = g2d.getFont().getWidth(distanceString);
        float damageStringWidth = g2d.getFont().getWidth(damageString);
        float boxWidth = Math.max(distanceStringWidth, damageStringWidth);

        // Draw the black box in the center
        g2d.setColor(Color.BLACK);
        g2d.fillRect(CANVAS_WIDTH / 2 - boxWidth / 2 - 10, 0, boxWidth + 20, 20 + 2 * g2d.getFont().getHeight());
        g2d.setColor(Color.WHITE);
        g2d.drawRect(CANVAS_WIDTH/2 - boxWidth/2 - 10, 0, boxWidth + 20, 20 + 2 * g2d.getFont().getHeight());

        // Draw the distance string
        x = CANVAS_WIDTH/2 - distanceStringWidth/2;
        y = 10;
        g2d.setColor(Color.WHITE);
        g2d.drawString(distanceString, x, y);

        // Draw the damage string
        Color color = Color.GREEN;
        if (DAMAGE > (100/4))
            color = Color.YELLOW;

        if (DAMAGE > 2 * (100/4))
            color = Color.ORANGE;

        if (DAMAGE > 3 * (100/4))
            color = Color.RED;

        x = CANVAS_WIDTH/2 - damageStringWidth/2;
        y = 10 + g2d.getFont().getHeight();
        g2d.setColor(color);
        g2d.drawString(damageString, x, y);

        // Draw the state message in the bottom
        String stateMessage = "Press SPACE to find out how much distance\nyou can drive on a highway of cars in RAGE!";
        float stateMessageWidth = g2d.getFont().getWidth(stateMessage);

        x = CANVAS_WIDTH/2 - stateMessageWidth/2;
        y = CANVAS_HEIGHT - 2 * g2d.getFont().getHeight() - 15;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(x - 20, y - 10, stateMessageWidth + 20, CANVAS_HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.drawString(stateMessage, x, y);
    }

    /**
     * Utility method to debug drawing.
     */
    private void debugRender()
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.setColor(Color.WHITE);

        for (Entity2D e : scene.getEntities())
        {
            g2d.drawPolygon(e.getPolygon());
        }
    }

    @Override
    public void onLeave()
    {
        scene.destroy();
    }
}
