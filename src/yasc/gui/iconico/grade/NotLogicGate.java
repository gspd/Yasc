package yasc.gui.iconico.grade;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

public class NotLogicGate extends DiagramComponent {
    
    NotLogicGate(ArrayList<String> inputList) {
        super(inputList);
        componentDimension.width = 20;
        componentDimension.height = 20;
    }
    
    /** Draws an inverter */
    @Override
    void draw(int x, int y, int orientation, Graphics g) {
	g.setColor(Color.BLACK);

        // Save the coordinates from orignal system
	originPoint.x = x;
	originPoint.y = y;

	// Determine the width and height of the whole component
	totalDimension.width = componentDimension.width + inputWidth + outputWidth;
	totalDimension.height = componentDimension.height;

	// Set the origin point
	g.translate(x, y);

	// Create the triangle
	Polygon triangle = new Polygon();
	triangle.addPoint(outputWidth,
			  totalDimension.height / 2);
	triangle.addPoint(totalDimension.width - inputWidth,
			  0);
	triangle.addPoint(totalDimension.width - inputWidth,
			  totalDimension.height);

	// Draw the triangle
	g.drawPolygon(triangle);

	// Draw the input signal
	g.drawLine(totalDimension.width - inputWidth,
		   totalDimension.height / 2,
		   totalDimension.width,
		   totalDimension.height / 2);
        
        // Add the input point to the connectors list
        p = new DiagramComponentConnector(originPoint.x + totalDimension.width, originPoint.y + totalDimension.height / 2, inputWidth, false);
        connectors.add(p);
        
	// Draw the input signal label
	g.drawString((String)inputList.get(0),
		     totalDimension.width - inputWidth + 5,
		     totalDimension.height / 2 - (gap / 2));

	// Draw the output signal
	g.drawLine(0,
		   totalDimension.height / 2,
		   outputWidth,
		   totalDimension.height / 2);
        
        // Add the output point to the connectors list
        p = new DiagramComponentConnector(originPoint.x, originPoint.y + totalDimension.height / 2, outputWidth, true);
        connectors.add(p);

	// Restore the origin point
	g.translate(-x, -y);
    }
    
    @Override
    public String toString() {
        return "__NOT_LOGIC_GATE";
    }
}
