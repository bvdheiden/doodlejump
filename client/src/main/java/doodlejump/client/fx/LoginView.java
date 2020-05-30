package doodlejump.client.fx;

import doodlejump.client.networking.GameClient;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginView extends VBox {
    private final Label connectionLabel;
    private final Label nameLabel;
    private final TextField nameField;
    private final Button loginButton;

    public LoginView() {
        this.connectionLabel = new Label("Connecting...");

        this.nameLabel = new Label("Enter name: ");
        this.nameField = new TextField();
        this.loginButton = new Button("Login");

        nameField.setDisable(true);
        loginButton.setDisable(true);
        loginButton.setOnAction(this::onLoginButtonPressed);

        HBox loginForm = new HBox(nameLabel, nameField, loginButton);
        getChildren().addAll(connectionLabel, loginForm);
        setSpacing(10);
    }

    public void onConnection() {
        connectionLabel.setText("Connected!");
        nameField.setDisable(false);
        loginButton.setDisable(false);
    }

    public void onDisconnection() {
        connectionLabel.setText("Disconnected!");
        nameField.setDisable(true);
        loginButton.setDisable(true);
    }

    public void onLoginInvalid() {
        nameLabel.setText("Enter name (invalid): ");
    }

    private void onLoginButtonPressed(ActionEvent event) {
        if (nameField.getText().length() == 0) {
            return;
        }

        GameClient.INSTANCE.login(nameField.getText());
    }

    public void onPlayerLogin() {
        connectionLabel.setText("Logged in!");
        nameField.setDisable(true);
        loginButton.setDisable(true);
    }
}
