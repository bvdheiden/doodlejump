package doodlejump.client;

import doodlejump.client.game.GameView;
import doodlejump.client.networking.GameClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {
    private GameClient client = new GameClient();

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameView gameView = new GameView();
        gameView.setPrefSize(400, 800);
        primaryStage.setScene(new Scene(gameView));

        client.start();

        primaryStage.setTitle("DoodleJump client");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        client.stop();
    }
}
