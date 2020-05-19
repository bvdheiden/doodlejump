package doodlejump.client;

import doodlejump.client.game.GameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GameView gameView = new GameView();
        gameView.setPrefSize(400, 800);

        primaryStage.setScene(new Scene(gameView));
        primaryStage.setTitle("DoodleJump client");
        primaryStage.show();
    }
}
