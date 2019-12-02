package yasc.gui.iconico.grade;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public abstract class DiagramComponent {
    protected DiagramComponentConnector p; // Connector point
    protected ArrayList<DiagramComponentConnector> connectors;
    protected ArrayList<String> inputList; // Labels of input signals
    protected int numInput; // Number of inputs
    protected Dimension totalDimension; // Component dimension + input and output signal space
    protected Dimension componentDimension;
    protected Point originPoint; // Point to draw the component
    protected int inputWidth; // Width reserved to input(s)
    protected int outputWidth; // Wisth reserved to output(s)
    protected int gap; // Gap between signal label
    protected static final int ORIENTATION_UP = 0;
    protected static final int ORIENTATION_RIGHT = 1;
    protected static final int ORIENTATION_DOWN = 2;
    protected static final int ORIENTATION_LEFT = 3;
    
    protected DiagramComponent(ArrayList<String> inputList) {
        this.inputList = inputList;
        this.totalDimension = new Dimension();
        this.componentDimension = new Dimension();
        this.originPoint = new Point();
        this.connectors = new ArrayList<>();
        this.inputWidth = 40;
        this.outputWidth = 25;
        this.gap = 4;
    }
    
    /** Draws the component */
    abstract void draw(int x, int y, int orientation, Graphics g);
    
    /** Returns the component dimension **/
    Dimension getDimension() {
        return totalDimension;
    }
    
    /** Return the origin point */
    Point getOriginPoint() {
        return originPoint;
    }
    
    /** Draws a signal line */
    void drawConnector(int x1, int y1, int x2, int y2, Graphics g) {
	int middle = Math.abs((x1 + x2) / 2);

	// Draw the first horizontal line
	g.drawLine(x1, y1, middle, y1);

	// Draw the vertical line
	g.drawLine(middle, y1, middle, y2);

	// Draw the second horizontal line
	g.drawLine(middle, y2, x2, y2);
        
        // Add the input point to the connectors list
        if (connectors.size() == numInput + 1)
            connectors.clear();
        p = new DiagramComponentConnector(originPoint.x + x1, originPoint.y + y1, inputWidth, false);
        connectors.add(p);
    }
    
    public ArrayList<DiagramComponentConnector> getConnectors() {
        return connectors;
    }
    
}
