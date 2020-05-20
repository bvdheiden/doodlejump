package doodlejump.server.fx;

import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Callback;

public class RoomBean {
    private SimpleIntegerProperty participants = new SimpleIntegerProperty();

    public static Callback<RoomBean, Observable[]> extractor() {
        return new Callback<RoomBean, Observable[]>() {
            @Override
            public Observable[] call(RoomBean param) {
                return new Observable[]{param.participants};
            }
        };
    }
}
