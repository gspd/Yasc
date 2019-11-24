package yasc.gui.iconico.grade;

import java.awt.Graphics;
import java.awt.Point;
import yasc.gui.iconico.Vertice;

public class DiagramComponentConnector extends Vertice {
    
    private final Point originPoint = new Point();
    boolean output;
    int connectorWidth;
    
    public DiagramComponentConnector(int x, int y, int connectorWidth, boolean isOutput) {
        super(x,y);
        originPoint.x = x;
        originPoint.y = y;
        this.output = isOutput;
        this.connectorWidth = connectorWidth;
    }

    public boolean isOutput() {
        return output;
    }

    @Override
    public void draw(Graphics g) { }

    @Override
    public boolean contains(int x, int y) {
        if (y >= originPoint.y - 10 && y <= originPoint.y + 10) {
            if (!output) {
                if (x >= originPoint.x - connectorWidth && x <= originPoint.x) {
                    return true;
                }
            } else {
                if (x >= originPoint.x && x <= originPoint.x + connectorWidth) {
                    return true;
                }
            }
        }
        return false;
    }
    
}