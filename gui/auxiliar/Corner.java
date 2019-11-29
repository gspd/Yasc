package yasc.gui.auxiliar;

import java.awt.*;
import javax.swing.*;

public class Corner extends JComponent {
    @Override
    protected void paintComponent(Graphics g) {
        // Fill me with dirty brown/orange.
	g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
