package Main;


/**
 * Created by AlexVR on 7/1/2018.
 */

public class Launch {

    public static void main(String[] args) {
        GameSetUp game = new GameSetUp("Snake", 780, 780); // Changed to 780 for display to be exact
        game.start();
    }
}
