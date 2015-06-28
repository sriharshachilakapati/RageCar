package com.shc.game.ragecar.entities;

import com.shc.game.ragecar.Resources;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.MathUtils;

import static com.shc.game.ragecar.Main.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayerCar extends Entity2D
{
    private boolean reachedPos = false;
    private Vector2 centerPos;

    public PlayerCar(Vector2 position)
    {
        super(Resources.Sprites.CAR_RED, new Rectangle(96, 160));
        setCenter(new Vector2(position.x, CANVAS_HEIGHT + 200));

        centerPos = position.subtractSelf(96/2, 150);
    }

    @Override
    public void update(float delta)
    {
        if (!reachedPos)
        {
            reachedPos = moveTo(centerPos, 4);
            return;
        }

        getVelocity().set(0);

        if (Keyboard.isPressed('W') || Keyboard.isPressed(Keyboard.KEY_UP))
            getVelocity().y -= 4;

        if (Keyboard.isPressed('S') || Keyboard.isPressed(Keyboard.KEY_DOWN))
            getVelocity().y += 4;

        if (Keyboard.isPressed('A') || Keyboard.isPressed(Keyboard.KEY_LEFT))
            getVelocity().x -= 4;

        if (Keyboard.isPressed('D') || Keyboard.isPressed(Keyboard.KEY_RIGHT))
            getVelocity().x += 4;

        getVelocity().scaleSelf(GAME_SPEED);
        getVelocity().x = MathUtils.clamp(getVelocity().x, -8, 8);
        getVelocity().y = MathUtils.clamp(getVelocity().y, -8, 8);

        getPosition().x = MathUtils.clamp(getPosition().x, 4, CANVAS_WIDTH - 100);

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

        // Move inside when outside the game area
        if (getCenter().x < 0 || getCenter().x > CANVAS_WIDTH ||
                getCenter().y < 0 || getCenter().y > CANVAS_HEIGHT)
            moveTo(getPosition().x, CANVAS_HEIGHT / 2, 4);
    }

    @Override
    public void collision(Entity2D other)
    {
        DAMAGE++;

        alignNextTo(other);
        Resources.Sounds.COLLISION.play();

        // Move out to prevent repeated collision events
        Vector2 temp = Vector2.REUSABLE_STACK.pop();

        temp.set(getCenter()).subtractSelf(other.getCenter());
        getPosition().addSelf(temp.normalizeSelf().scaleSelf(20));

        Vector2.REUSABLE_STACK.push(temp);
    }
}
