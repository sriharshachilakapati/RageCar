package com.shc.game.ragecar.states;

import com.shc.game.ragecar.Resources;
import com.shc.game.ragecar.entities.CitizenCar;
import com.shc.game.ragecar.entities.PlayerCar;
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
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene2D;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.TimeUtils;

import static com.shc.game.ragecar.Main.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    private Scene2D         scene;
    private SceneCollider2D collider;

    @Override
    public void onEnter()
    {
        GAME_SPEED = 0.5f;
        DISTANCE = 0;
        DAMAGE = 0;

        scene = new Scene2D();

        collider = new SceneCollider2D(new DynamicTree2D());
        collider.setScene(scene);

        collider.register(PlayerCar.class, CitizenCar.class);
        collider.register(CitizenCar.class, CitizenCar.class);
        collider.register(PoliceCar.class, PlayerCar.class);
        collider.register(PoliceCar.class, CitizenCar.class);

        scene.addChild(new PlayerCar(new Vector2(CANVAS_WIDTH / 4, CANVAS_HEIGHT - CANVAS_HEIGHT / 4)));

        GameTimer timer = new GameTimer(2, TimeUtils.Unit.SECONDS);
        timer.setCallback(() ->
        {
            if (Game.getGameState() instanceof PlayState)
            {
                scene.addChild(new CitizenCar(MathUtils.random_range(0, CANVAS_WIDTH - 100)));

                if (PoliceCar.INSTANCE_COUNT == 0 && GAME_SPEED > 2 && MathUtils.chance(10))
                    scene.addChild(new PoliceCar(MathUtils.random_range(0, CANVAS_WIDTH - 100)));

                timer.start();
            }
        });
        timer.start();
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.setGameState(new IntroState());

        scene.update(delta);
        collider.checkCollisions();

        GAME_SPEED += 0.001f;
        DISTANCE += 2 * delta;
        GAME_SPEED = MathUtils.clamp(GAME_SPEED, 0, 3.5f);

        DAMAGE = MathUtils.clamp(DAMAGE, 0, 100);

        if (DAMAGE >= 100)
            Game.setGameState(new IntroState());
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();

        g2d.getCamera().apply();
        scene.render(delta);

        g2d.drawTexture(Resources.Textures.STREET_LIGHTS, 0, BACKGROUND_SCROLL, CANVAS_WIDTH, CANVAS_HEIGHT);

        if (BACKGROUND_SCROLL > 0)
            g2d.drawTexture(Resources.Textures.STREET_LIGHTS, 0, BACKGROUND_SCROLL - CANVAS_HEIGHT, CANVAS_WIDTH, CANVAS_HEIGHT);

        if (Keyboard.isPressed(Keyboard.KEY_ENTER))
            debugRender();

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
    }

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
