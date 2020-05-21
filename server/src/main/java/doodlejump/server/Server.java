package doodlejump.server;

import doodlejump.server.networking.GameServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application {
    GameServer server = new GameServer();

    @Override
    public void start(Stage primaryStage) throws Exception {
        server.start();

        RoomView roomView = new RoomView();

        server.setOnRoomCreation(roomView);
        server.setOnRoomDestruction(roomView);
        server.setOnRoomUpdate(roomView);

        primaryStage.setScene(new Scene(roomView));
        primaryStage.setTitle("DoodleJump server");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        server.stop();
    }
}
