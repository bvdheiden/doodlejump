package doodlejump.client;

import doodlejump.client.networking.GameClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {
    private GameClient client;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        GameView gameView = new GameView();
//        gameView.setPrefSize(400, 800);
//        primaryStage.setScene(new Scene(gameView));

        this.client = new GameClient();
        client.start();

//        new Thread(() -> {
//            while (true) {
//                try {
//                    client.sendPlayerPosition(2, 2);
//
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        primaryStage.setTitle("DoodleJump client");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        client.stop();
    }
}
