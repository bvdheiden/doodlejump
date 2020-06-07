package doodlejump.client.fx;

import doodlejump.client.game.GameView;
import doodlejump.client.game.PlayerGameView;
import doodlejump.client.networking.GameClient;
import doodlejump.core.networking.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RoomView extends BorderPane {
    private final ObservableList<Player> playerList = FXCollections.observableArrayList();

    private final Button findRoomButton;
    private final Button readyButton;
    private final ListView<Player> listView;

    private final HBox gameLayout;
    private final PlayerGameView playerGameView;
    private final GameView serverGameView;
    private boolean connected;

    public RoomView() {
        this.findRoomButton = new Button("Find room");
        this.readyButton = new Button("Ready");
        this.listView = new ListView<>(playerList);
        this.playerGameView = new PlayerGameView();
        this.serverGameView = new GameView();

        HBox buttonLayout = new HBox(findRoomButton, readyButton);
        VBox roomLayout = new VBox(buttonLayout, listView);
        setLeft(roomLayout);

        findRoomButton.setOnAction(this::onFindRoomPressed);
        readyButton.setOnAction(this::onReadyPressed);

        this.gameLayout = new HBox(playerGameView, serverGameView);
        setCenter(gameLayout);
    }

    public void stopGame() {
        playerGameView.stop();
        serverGameView.stop();

        for (Player player : playerList)
            player.setReady(false);

        readyButton.setDisable(false);
    }

    public void onRoomConnection(Player hostPlayer) {
        findRoomButton.setText("Disconnect");
        findRoomButton.setDisable(false);
        readyButton.setDisable(false);

        onPlayerConnection(hostPlayer);
        this.connected = true;
    }

    public void onRoomDisconnection() {
        findRoomButton.setText("Connect");
        readyButton.setDisable(true);

        stopGame();
        playerList.clear();
        this.connected = false;
    }

    public void onDisconnection() {
        findRoomButton.setDisable(true);
        readyButton.setDisable(true);

        stopGame();
        playerList.clear();
        this.connected = false;
    }

    public void onPlayerLogin() {
        findRoomButton.setDisable(false);
        readyButton.setDisable(true);
    }

    public void onPlayerConnection(Player player) {
        playerList.add(player);
    }

    public void onPlayerDisconnection(String playerName) {
        Player player = getPlayer(playerName);
        playerList.remove(player);
        stopGame();
    }

    public void onPlayerReady(String playerName) {
        Player player = getPlayer(playerName);
        player.setReady(true);
        listView.refresh();
    }

    public void onGameStart(long seed) {
        readyButton.setDisable(true);

        playerGameView.start(seed, getHostPlayer());
        serverGameView.start(seed, getServerPlayer());
    }

    public void onNewPlayerPosition(String playerName, double x, double y) {
        Player player = getPlayer(playerName);
        player.setPosition(x, y);
    }

    private void onFindRoomPressed(ActionEvent event) {
        if (connected) {
            GameClient.INSTANCE.disconnectRoom();
        } else {
            GameClient.INSTANCE.connectRoom();
        }
    }

    private void onReadyPressed(ActionEvent event) {
        GameClient.INSTANCE.ready();
        getHostPlayer().setReady(true);
        listView.refresh();
    }

    private Player getHostPlayer() {
        return playerList.stream()
                .filter(Player::isHost)
                .findFirst()
                .get();
    }

    private Player getServerPlayer() {
        return playerList.stream()
                .filter(p -> !p.isHost())
                .findFirst()
                .get();
    }

    private Player getPlayer(String playerName) {
        return playerList.stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst()
                .get();
    }

    public void onGameFinish(boolean didWin) {
        playerGameView.stop();
        serverGameView.stop();

        Platform.runLater(() -> {
            for (Player player : playerList) {
                player.setReady(false);
            }

            listView.refresh();

            readyButton.setDisable(false);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game finished");
            alert.setHeaderText(null);
            if (didWin) {
                alert.setContentText("You have won the game!");
            } else {
                alert.setContentText("You have lost the game!");
            }

            alert.show();
        });
    }
}
