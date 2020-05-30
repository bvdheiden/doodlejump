package doodlejump.client;

import doodlejump.client.fx.LoginView;
import doodlejump.client.fx.RoomView;
import doodlejump.client.networking.GameClient;
import doodlejump.core.networking.Player;
import doodlejump.core.networking.listeners.PlayerLoginListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Client extends Application {
    private final GameClient client = GameClient.INSTANCE;

    private LoginView loginView;
    private RoomView roomView;

    private Player hostPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.loginView = new LoginView();
        this.roomView = new RoomView();
        roomView.setVisible(false);

        VBox mainLayout = new VBox(loginView, roomView);
        mainLayout.setSpacing(20);

        BorderPane main = new BorderPane();
        main.setCenter(mainLayout);


        clientStuff();

        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(main));
        primaryStage.setTitle("DoodleJump client");
        primaryStage.show();
    }

    private void clientStuff() {
        client.start();

        client.setOnConnection(() -> {
           Platform.runLater(() -> {
               loginView.onConnection();
           });
        });

        client.setOnDisconnection(() -> {
            Platform.runLater(() -> {
                loginView.onDisconnection();
                roomView.onDisconnection();
                roomView.setVisible(true);
            });
        });

        client.setOnPlayerLogin(new PlayerLoginListener() {
            @Override
            public void onPlayerLogin(Player player) {
                Platform.runLater(() -> {
                    loginView.onPlayerLogin();
                    roomView.onPlayerLogin();
                    player.setHost(true);
                    hostPlayer = player;
                    roomView.setVisible(true);
                });
            }

            @Override
            public void onPlayerLoginNameInUse() {
                Platform.runLater(() -> {
                    loginView.onLoginInvalid();
                });
            }
        });

        client.setOnRoomConnection(() -> {
            Platform.runLater(() -> {
                roomView.onRoomConnection(hostPlayer);
            });
        });

        client.setOnRoomDisconnection(() -> {
            Platform.runLater(() -> {
                roomView.onRoomDisconnection();
            });
        });

        client.setOnPlayerConnection(player -> {
            Platform.runLater(() -> {
                roomView.onPlayerConnection(player);
            });
        });

        client.setOnPlayerDisconnection(player -> {
            Platform.runLater(() -> {
                roomView.onPlayerDisconnection(player.getName());
            });
        });

        client.setOnPlayerReady(player -> {
            Platform.runLater(() -> {
                roomView.onPlayerReady(player.getName());
            });
        });

        client.setOnGameStart((seed) -> {
            Platform.runLater(() -> {
                roomView.onGameStart(seed);
            });
        });

        client.setOnNewPlayerPosition(player -> {
            roomView.onNewPlayerPosition(player.getName(), player.getX(), player.getY(), player.getVelocityX(), player.getVelocityY());
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        client.stop();
    }


}
