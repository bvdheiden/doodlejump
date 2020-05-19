package doodlejump.server;

import doodlejump.server.networking.ServerSdk;
import javafx.application.Application;
import javafx.stage.Stage;

public class Server extends Application {
    private ServerSdk sdk;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.sdk = new ServerSdk(10000);
        sdk.connect();

        primaryStage.setTitle("DoodleJump server");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        sdk.disconnect();
    }
}
