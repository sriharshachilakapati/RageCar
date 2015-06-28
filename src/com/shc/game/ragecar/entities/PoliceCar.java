package com.shc.game.ragecar.entities;

import com.shc.game.ragecar.Resources;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.entity.Entity2D;

import static com.shc.game.ragecar.Main.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class PoliceCar extends Entity2D
{
    public static int INSTANCE_COUNT = 0;

    public PoliceCar(float randomX)
    {
        super(Resources.Sprites.CAR_POLICE, new Rectangle(96, 160));

        Vector2 position = new Vector2(randomX, 0);

        if (position.x < CANVAS_WIDTH/2)
            getVelocity().y = -4;
        else
        {
            getVelocity().y = 4 + 3 * GAME_SPEED;
            rotate(180);
            getSprite().setScaleX(-1);
        }

        getVelocity().scaleSelf(GAME_SPEED);

        if (getVelocity().y < 0)
            position.y = CANVAS_HEIGHT + 200;
        else
            position.y = -200;

        setPosition(position);

        INSTANCE_COUNT++;
        Resources.Sounds.SIREN.play();

        // Loop the animation
        getSprite().getAnimation().setEndCallback(getSprite().getAnimation()::start);
        getSprite().getAnimation().start();
    }

    @Override
    public void update(float delta)
    {
        if (getVelocity().y < 0 && getPosition().y < -200)
            destroy();

        if (getVelocity().y > 0 && getPosition().y > CANVAS_HEIGHT + 200)
            destroy();
    }

    @Override
    public void collision(Entity2D other)
    {
        if (other instanceof PlayerCar)
            DAMAGE++;

        other.alignNextTo(this);
        Resources.Sounds.COLLISION.play();

        // Move out to prevent repeated collision events
        Vector2 temp = Vector2.REUSABLE_STACK.pop();

        temp.set(getCenter()).subtractSelf(other.getCenter());
        other.getPosition().subtractSelf(temp.normalizeSelf().scaleSelf(20));

        Vector2.REUSABLE_STACK.push(temp);
    }

    @Override
    public void destroy()
    {
        super.destroy();

        INSTANCE_COUNT--;
        Resources.Sounds.SIREN.stop();
    }
}
