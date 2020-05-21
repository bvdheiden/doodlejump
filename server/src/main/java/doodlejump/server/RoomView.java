package doodlejump.server;

import doodlejump.core.fx.ControlFactory;
import doodlejump.core.networking.Room;
import doodlejump.server.networking.listeners.RoomCreationListener;
import doodlejump.server.networking.listeners.RoomDestructionListener;
import doodlejump.server.networking.listeners.RoomUpdateListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class RoomView extends VBox implements RoomCreationListener, RoomDestructionListener, RoomUpdateListener {
    private final ObservableList<Room> roomObservableArray = FXCollections.observableArrayList();
    private final ListView<Room> listView = new ListView<>(roomObservableArray);

    public RoomView() {
        setSpacing(10);
        getChildren().addAll(
            ControlFactory.createLabel("Rooms", ControlFactory.LabelStyle.HEADER_1),
            listView
        );
    }

    @Override
    public void onRoomCreated(Room room) {
        Platform.runLater(() -> {
            roomObservableArray.add(room);
        });
    }

    @Override
    public void onRoomDestruction(Room room) {
        Platform.runLater(() -> {
            roomObservableArray.remove(room);
        });
    }

    @Override
    public void onRoomUpdate(Room room) {
        Platform.runLater(listView::refresh);
    }
}
