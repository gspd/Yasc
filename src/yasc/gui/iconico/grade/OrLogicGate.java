package yasc.gui.iconico.grade;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

public class OrLogicGate extends DiagramComponent {
    
    protected OrLogicGate(ArrayList<String> inputList) {
        super(inputList);
        componentDimension.width = 40;
        componentDimension.height = 40;
    }
    
    @Override
    void draw(int x, int y, int orientation, Graphics g) {
        g.setColor(Color.BLACK);
        FontMetrics fontMetrics = g.getFontMetrics();
	numInput = inputList.size();
        
        // Determine the origin points
	originPoint.x = x;
	originPoint.y = y;

	// Determine the width of the whole component
	totalDimension.width = componentDimension.width + inputWidth + outputWidth;

	// Determine the height of the whole component
	if (numInput * (fontMetrics.getAscent() + gap) > componentDimension.height) {
	    totalDimension.height = numInput * (fontMetrics.getAscent() + fontMetrics.getDescent() + gap);
	} else {
	    totalDimension.height = componentDimension.height;
	}
        
        g.translate(x, y);

	// Draw the signals input lines
	int majorPartSize = totalDimension.height / (numInput + 1);
	int minorPartSize = componentDimension.height / (numInput + 1);
	int componentYOrigin = (totalDimension.height - componentDimension.height) / 2;

	for (int i = 1; i <= numInput; i++) {
	    // Draw connectors
	    drawConnector(totalDimension.width,
			  majorPartSize * i,
			  totalDimension.width - inputWidth,
			  componentYOrigin + minorPartSize * i,
			  g);

	    // Draw signal label
	    g.drawString((String)inputList.get(i - 1),
			 totalDimension.width - (inputWidth / 2) + 5,
			 (majorPartSize * i) - (gap / 2));
	}
        
        // Draw the output signal
	g.drawLine(outputWidth,
		   totalDimension.height / 2,
		   0,
		   totalDimension.height / 2);
        
        // Draw the output point to link
        p = new DiagramComponentConnector(originPoint.x, originPoint.y + totalDimension.height / 2, outputWidth, true);
        connectors.add(p);
        
        g.translate(-x, -y);
        
        int base = totalDimension.width - inputWidth;
        g.drawArc(x + base, y, 10, totalDimension.height, 90, 180);
        g.drawArc(x + outputWidth, y, totalDimension.width - 15, 
                totalDimension.height, 90, 88);
        g.drawArc(x + outputWidth, y, totalDimension.width - 15, 
                totalDimension.height, 180, 90);
        
    }
    
    @Override
    public String toString() {
        return "__OR_LOGIC_GATE";
    }
}
