package com.shc.game.ragecar;

/**
 * The main class of the game. Used to hold the main function and some variables and constants that I use from all the
 * other classes in the game. It just starts a new instance of RageCar.
 *
 * @author Sri Harsha Chilakapati
 */
public class Main
{
    // Variables and constants for the game
    public static float BACKGROUND_SCROLL;
    public static float GAME_SPEED;
    public static float DISTANCE;
    public static int   DAMAGE;

    // The resolution of the game (not the monitor)
    public static final int CANVAS_WIDTH  = 1280;
    public static final int CANVAS_HEIGHT = 720;

    /**
     * The entry point of the application.
     *
     * @param args The arguments passed if any.
     */
    public static void main(String[] args)
    {
        new RageCar().start();
    }
}
