package com.shc.game.ragecar;

import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Animation;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteSheet;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * This class is used to manage the resources of the game. Contains sub classes that allows for easy access of loaded
 * resources from any part of the game.
 *
 * @author Sri Harsha Chilakapati
 */
public final class Resources
{
    public static final class Textures
    {
        public static Texture LOGO;
        public static Texture ROAD;
        public static Texture STREET_LIGHTS;
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
        public static Sound SIREN;
        public static Sound COLLISION;
    }

    public static void load()
    {
        ResourceLoader loader = ResourceLoader.getInstance();

        // Define the resources to load
        int logoID = loader.defineTexture("resources/rageCarLogo.png");
        int carSheetID = loader.defineTexture("resources/cars_sheet.png");
        int roadTexID = loader.defineTexture("resources/road_texture.png");
        int lightTexID = loader.defineTexture("resources/street_lights.png");
        int musicID = loader.defineSound("resources/music.ogg");
        int sirenID = loader.defineSound("resources/siren.wav");
        int collisionID = loader.defineSound("resources/collision.wav");
        int fontID = loader.defineFont("resources/New Art Deco.ttf", TrueTypeFont.STYLE_NORMAL, 40);

        // Start the resource loader
        loader.startLoading();

        // Retrieve the loaded textures
        Textures.LOGO = loader.getTexture(logoID);
        Textures.ROAD = loader.getTexture(roadTexID);
        Textures.STREET_LIGHTS = loader.getTexture(lightTexID);
        SpriteSheet carSheet = new SpriteSheet(loader.getTexture(carSheetID), 363, 521);
        Texture carBlue = resizeTexture(carSheet.getCell(0, 0), 96, 160);
        Texture carRed = resizeTexture(carSheet.getCell(0, 1), 96, 160);
        Texture carGreen = resizeTexture(carSheet.getCell(0, 2), 96, 160);
        Texture carWhite = resizeTexture(carSheet.getCell(0, 3), 96, 160);
        Texture carYellow = resizeTexture(carSheet.getCell(1, 0), 96, 160);
        Texture carBlack = resizeTexture(carSheet.getCell(1, 1), 96, 160);
        Texture carPolice1 = resizeTexture(carSheet.getCell(1, 2), 96, 160);
        Texture carPolice2 = resizeTexture(carSheet.getCell(1, 3), 96, 160);

        // The animation of the police
        Animation carPolice = new Animation();
        carPolice.addFrame(carPolice1, 150, TimeUtils.Unit.MILLIS);
        carPolice.addFrame(carPolice2, 150, TimeUtils.Unit.MILLIS);

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
        Sounds.MUSIC.play();
        Sounds.SIREN = loader.getSound(sirenID);
        Sounds.SIREN.setLooping(true);
        Sounds.COLLISION = loader.getSound(collisionID);

        // Set the font we use
        SilenceEngine.graphics.getGraphics2D().setFont(loader.getFont(fontID));
    }

    /**
     * This is a utility method to resize the textures.
     *
     * @param texture   The texture to resize.
     * @param newWidth  The new width of the texture.
     * @param newHeight The new height of the texture.
     *
     * @return The resized texture.
     */
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
