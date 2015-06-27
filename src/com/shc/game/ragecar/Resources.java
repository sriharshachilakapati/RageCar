package com.shc.game.ragecar;

import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteSheet;
import com.shc.silenceengine.graphics.opengl.Texture;
import org.lwjgl.openal.AL10;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Resources
{
    public static final class Textures
    {
        public static Texture ROAD;
    }

    public static final class Sprites
    {
        public static Sprite CAR_BLUE;
        public static Sprite CAR_RED;
        public static Sprite CAR_GREEN;
        public static Sprite CAR_WHITE;
        public static Sprite CAR_YELLOW;
        public static Sprite CAR_BLACK;
        public static Sprite CAR_POLICE;
    }

    public static final class Sounds
    {
        public static Sound MUSIC;
    }

    public static void load()
    {
        ResourceLoader loader = ResourceLoader.getInstance();

        int carSheetID = loader.defineTexture("resources/cars_sheet.png");
        int roadTexID = loader.defineTexture("resources/road_texture.png");
        int musicID = loader.defineSound("resources/music.ogg");

        loader.startLoading();

        Textures.ROAD = loader.getTexture(roadTexID);
        SpriteSheet carSheet = new SpriteSheet(loader.getTexture(carSheetID), 363, 521);

        // Resize and get the Textures
        Texture carBlue = resizeTexture(carSheet.getCell(0, 0), 96, 160);
        Texture carRed = resizeTexture(carSheet.getCell(0, 1), 96, 160);
        Texture carGreen = resizeTexture(carSheet.getCell(0, 2), 96, 160);
        Texture carWhite = resizeTexture(carSheet.getCell(0, 3), 96, 160);
        Texture carYellow = resizeTexture(carSheet.getCell(1, 0), 96, 160);
        Texture carBlack = resizeTexture(carSheet.getCell(1, 1), 96, 160);
        Texture carPolice = resizeTexture(carSheet.getCell(1, 2), 96, 160);

        // Create the sprites
        Sprites.CAR_BLUE = new Sprite(carBlue);
        Sprites.CAR_RED = new Sprite(carRed);
        Sprites.CAR_GREEN = new Sprite(carGreen);
        Sprites.CAR_WHITE = new Sprite(carWhite);
        Sprites.CAR_YELLOW = new Sprite(carYellow);
        Sprites.CAR_BLACK = new Sprite(carBlack);
        Sprites.CAR_POLICE = new Sprite(carPolice);

        // Create the sounds
        Sounds.MUSIC = loader.getSound(musicID);
        Sounds.MUSIC.setLooping(true);
        Sounds.MUSIC.getSource().setParameter(AL10.AL_PITCH, 0.5f);
        Sounds.MUSIC.play();
    }

    private static Texture resizeTexture(Texture texture, float newWidth, float newHeight)
    {
        return texture.getSubTexture(texture.getMinU(), texture.getMinV(),
                texture.getMaxU(), texture.getMaxV(), newWidth, newHeight);
    }

    public static void dispose()
    {
        ResourceLoader.getInstance().dispose();
    }
}
