package doodlejump.client;

import doodlejump.client.networking.ClientSdk;
import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {
    private ClientSdk sdk;
    private boolean connected;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        GameView gameView = new GameView();
//        gameView.setPrefSize(400, 800);

//        primaryStage.setScene(new Scene(gameView));

        this.sdk = new ClientSdk("localhost", 10000);
        sdk.connect();

        sdk.setOnConnection(() -> {
            connected = true;
            System.out.println("On connection");

            while (connected) {
                sdk.onPlayerMovement(20, 20);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        sdk.setOnDisconnection(() -> {
            connected = false;
        });

        primaryStage.setTitle("DoodleJump client");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        sdk.disconnect();
    }
}
