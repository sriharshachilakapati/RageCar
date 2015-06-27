package com.shc.game.ragecar.entities;

import com.shc.game.ragecar.RageCar;
import com.shc.game.ragecar.Resources;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class CitizenCar extends Entity2D
{
    public CitizenCar(float randomX)
    {
        super(Sprite.EMPTY, new Rectangle(96, 160));

        // Select a random sprite!
        switch (MathUtils.random_range(0, 5))
        {
            case 0: setSprite(Resources.Sprites.CAR_BLACK);  break;
            case 1: setSprite(Resources.Sprites.CAR_YELLOW); break;
            case 2: setSprite(Resources.Sprites.CAR_WHITE);  break;
            case 3: setSprite(Resources.Sprites.CAR_GREEN);  break;
            case 4: setSprite(Resources.Sprites.CAR_BLUE);   break;
        }

        Vector2 position = new Vector2(randomX, 0);

        if (position.x < RageCar.CANVAS_WIDTH/2)
            getVelocity().y = -2;
        else
        {
            getVelocity().y = 6;
            rotate(180);
            getSprite().setScaleX(-1);
        }

        getVelocity().scaleSelf(RageCar.GAME_SPEED);
        getVelocity().x = MathUtils.clamp(getVelocity().x, -8, 8);
        getVelocity().y = MathUtils.clamp(getVelocity().y, -8, 8);

        if (getVelocity().y < 0)
            position.y = RageCar.CANVAS_HEIGHT + 200;
        else
            position.y = -200;

        setPosition(position);
    }

    @Override
    public void update(float delta)
    {
        if (getPosition().y > RageCar.CANVAS_HEIGHT + 200 && getVelocity().y > 0)
            destroy();

        if (getPosition().y < -200 && getVelocity().y < 0)
            destroy();
    }

    @Override
    public void collision(Entity2D other)
    {
        alignNextTo(other);
    }
}
