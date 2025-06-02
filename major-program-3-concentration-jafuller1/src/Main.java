/**
 * Jy'Mir Fuller
 * Section 3
 * This Program is created to provide a matching game to the user, in this game the user will have a counter which
 * counts how many attempts they have taken. Each match is rewarded with a sound "ahh hell nah" that indicates a
 * successful match. Ultimately the user can complete levels 1-6 and finish the game.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        // Create an instance of the custom GamePane with a card size of 60 pixels
        GamePane gamePane = new GamePane(85);
        // Create a Scene with the GamePane as the root node, and dimensions of 500x500
        Scene scene = new Scene(gamePane, 700, 700);
        // Sets the stage title
        stage.setTitle("Concentration");
        //Sets the stage with the created scene
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
