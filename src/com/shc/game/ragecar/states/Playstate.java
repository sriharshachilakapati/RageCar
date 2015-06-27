package com.shc.game.ragecar.states;

import com.shc.game.ragecar.RageCar;
import com.shc.game.ragecar.entities.CitizenCar;
import com.shc.game.ragecar.entities.PlayerCar;
import com.shc.silenceengine.collision.broadphase.DynamicTree2D;
import com.shc.silenceengine.collision.colliders.SceneCollider2D;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene2D;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    private Scene2D scene;
    private SceneCollider2D collider;

    @Override
    public void onEnter()
    {
        RageCar.GAME_SPEED = 0.5f;

        scene = new Scene2D();

        collider = new SceneCollider2D(new DynamicTree2D());
        collider.setScene(scene);

        collider.register(PlayerCar.class, CitizenCar.class);
        collider.register(CitizenCar.class, CitizenCar.class);

        scene.addChild(new PlayerCar(new Vector2(RageCar.CANVAS_WIDTH / 4, RageCar.CANVAS_HEIGHT - RageCar.CANVAS_HEIGHT / 4)));

        GameTimer timer = new GameTimer(2, TimeUtils.Unit.SECONDS);
        timer.setCallback(() ->
        {
            if (Game.getGameState() instanceof PlayState)
            {
                scene.addChild(new CitizenCar(MathUtils.random_range(0, RageCar.CANVAS_WIDTH - 100)));

                timer.start();
            }
        });
        timer.start();
    }

    @Override
    public void update(float delta)
    {
        scene.update(delta);
        collider.checkCollisions();

        RageCar.GAME_SPEED += 0.001f;
        RageCar.GAME_SPEED = MathUtils.clamp(RageCar.GAME_SPEED, 0, 3f);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        SilenceEngine.graphics.getGraphics2D().getCamera().apply();
        scene.render(delta);
    }

    @Override
    public void onLeave()
    {
        scene.destroy();
    }
}
