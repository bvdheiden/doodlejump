package doodlejump.server;

import doodlejump.server.networking.GameServer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Server extends Application {
    GameServer server = new GameServer();

    @Override
    public void start(Stage primaryStage) throws Exception {
        server.start();

        primaryStage.setTitle("DoodleJump server");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        server.stop();
    }
}
