package doodlejump.client;

import doodlejump.client.game.GameView;
import doodlejump.client.networking.GameClient;
import doodlejump.core.networking.Player;
import doodlejump.core.networking.listeners.PlayerLoginListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Client extends Application {
    private GameClient client = new GameClient();
    private Label playerNameLabel;
    private TextField playerNameField;
    private Button loginButton;
    private Button roomButton;
    private Label roomPlayersLabel;
    private Player currentPlayer;
    private List<Player> players = new ArrayList<>();
    private List<Player> readyPlayers = new ArrayList<>();
    private boolean inRoom;
    private Label startedLabel;
    private Button readyButton;
    private Label readyLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.playerNameLabel = new Label("Enter player name: ");
        this.playerNameField = new TextField();
        this.loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            if (playerNameField.getText().length() == 0) {
                return;
            }

            client.login(playerNameField.getText());
        });

        HBox loginLayout = new HBox(playerNameLabel, playerNameField, loginButton);

        this.roomButton = new Button("Connect room");
        roomButton.setDisable(true);
        roomButton.setOnAction(event -> {
            if (inRoom) {
                client.disconnectRoom();
            } else {
                client.connectRoom();
            }
        });

        this.roomPlayersLabel = new Label();
        this.readyLabel = new Label();
        this.readyButton = new Button("Ready");
        readyButton.setDisable(true);
        readyButton.setOnAction(event -> {
            client.ready();
        });
        this.startedLabel = new Label("Not started");

        HBox roomLayout = new HBox(roomButton, roomPlayersLabel, readyButton, readyLabel, startedLabel);

        GameView gameView = new GameView();
        gameView.setPrefSize(400, 800);

        VBox mainLayout = new VBox(loginLayout, roomLayout, gameView);

        primaryStage.setScene(new Scene(mainLayout));


        clientStuff();

        primaryStage.setTitle("DoodleJump client");
        primaryStage.show();
    }

    private void clientStuff() {
        client.start();

        client.setOnPlayerLogin(new PlayerLoginListener() {
            @Override
            public void onPlayerLogin(Player player) {
                Platform.runLater(() -> {
                    playerNameField.setDisable(true);
                    loginButton.setDisable(true);
                    roomButton.setDisable(false);
                    currentPlayer = player;
                    playerNameLabel.setText("Logged in as: ");
                });
            }

            @Override
            public void onPlayerLoginNameInUse() {
                Platform.runLater(() -> {
                    playerNameLabel.setText("Enter player name (in use): ");
                });
            }
        });

        client.setOnRoomConnection(() -> {
            Platform.runLater(() -> {
                roomButton.setText("Disconnect room");
                this.inRoom = true;
                players.clear();
                players.add(currentPlayer);
                updateRoomPlayersLabel();
                readyButton.setDisable(false);
                startedLabel.setText("Not started");
            });
        });

        client.setOnRoomDisconnection(() -> {
            Platform.runLater(() -> {
                roomButton.setText("Connect room");
                this.inRoom = false;
                players.clear();
                updateRoomPlayersLabel();
                readyButton.setDisable(true);
                startedLabel.setText("Not started");
            });
        });

        client.setOnPlayerConnection(player -> {
            Platform.runLater(() -> {
                players.add(player);
                updateRoomPlayersLabel();
            });
        });

        client.setOnPlayerDisconnection(player -> {
            Platform.runLater(() -> {
                players.remove(player);
                updateRoomPlayersLabel();
                updateReadyPlayersLabel();
            });
        });

        client.setOnPlayerReady(player -> {
            Platform.runLater(() -> {
                readyPlayers.add(player);
                updateReadyPlayersLabel();
            });
        });

        client.setOnGameStart(() -> {
            Platform.runLater(() -> {
                startedLabel.setText("Started!");
            });
        });
    }

    private void updateRoomPlayersLabel() {
        StringJoiner joiner = new StringJoiner(", ");
        for (Player player : players) {
            joiner.add(player.getName());
        }

        roomPlayersLabel.setText(joiner.toString());
    }

    private void updateReadyPlayersLabel() {
        StringJoiner joiner = new StringJoiner(", ");
        for (Player player : readyPlayers) {
            joiner.add(player.getName());
        }

        readyLabel.setText(joiner.toString());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        client.stop();
    }
}
