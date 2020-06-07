package doodlejump.core.fx;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class ControlFactory {
    private static final Font[] FONTS = {
            new Font(20),
            new Font(16),
            new Font(12),
            new Font(10)
    };

    public static Label createLabel(String text, LabelStyle style) {
        Label label = new Label(text);
        label.setFont(FONTS[style.ordinal()]);
        return label;
    }

    public enum LabelStyle {
        HEADER_1,
        HEADER_2,
        HEADER_3,
        PARAGRAPH
    }
}
