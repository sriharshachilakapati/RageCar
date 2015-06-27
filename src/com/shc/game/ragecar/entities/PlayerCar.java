package com.shc.game.ragecar.entities;

import com.shc.game.ragecar.RageCar;
import com.shc.game.ragecar.Resources;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayerCar extends Entity2D
{
    public PlayerCar(Vector2 position)
    {
        super(Resources.Sprites.CAR_RED, new Rectangle(96, 160));
        setCenter(position);
    }

    @Override
    public void update(float delta)
    {
        getVelocity().set(0);

        if (Keyboard.isPressed('W') || Keyboard.isPressed(Keyboard.KEY_UP))
            getVelocity().y -= 4;

        if (Keyboard.isPressed('S') || Keyboard.isPressed(Keyboard.KEY_DOWN))
            getVelocity().y += 4;

        if (Keyboard.isPressed('A') || Keyboard.isPressed(Keyboard.KEY_LEFT))
            getVelocity().x -= 4;

        if (Keyboard.isPressed('D') || Keyboard.isPressed(Keyboard.KEY_RIGHT))
            getVelocity().x += 4;

        getVelocity().scaleSelf(RageCar.GAME_SPEED);
        getVelocity().x = MathUtils.clamp(getVelocity().x, -8, 8);
        getVelocity().y = MathUtils.clamp(getVelocity().y, -8, 8);

        getPosition().x = MathUtils.clamp(getPosition().x, 4, RageCar.CANVAS_WIDTH - 100);

        setRotation(0);

        if (getVelocity().y == 0)
        {
            if (getVelocity().x < 0)
                setRotation(-15);

            if (getVelocity().x > 0)
                setRotation(15);
        }
        else
        {
            if (getVelocity().x < 0)
                setRotation(15 * Math.signum(getVelocity().y));

            if (getVelocity().x > 0)
                setRotation(-15 * Math.signum(getVelocity().y));
        }
    }

    @Override
    public void collision(Entity2D other)
    {
        alignNextTo(other);
    }
}
