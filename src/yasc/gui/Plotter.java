package yasc.gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Plotter extends JPanel {

    private ArrayList pointsArray; // Array of Point objects
    private Font font; // Font
    private Color fgColor; // Foreground color
    private Color bgColor; // Background color
    private final PairValue maxValue = new PairValue(); // Hold the maximum values in array
    private final PairValue minValue = new PairValue(); // Hold the minimum values in array
    private final Dimension componentDimension = new Dimension();
    private final DecimalFormat formatter = new DecimalFormat("#.#");
    private final DecimalFormat yFormatter = new DecimalFormat("0.##E0");

    public Plotter() {
        this(null);
    }

    public Plotter(ArrayList pointsArray) {
        this.pointsArray = pointsArray;
        font = null;
        fgColor = null;
        bgColor = null;
    }

    /**
     * Sets the graph to plot
     */
    public void setGraph(ArrayList pointsArray) {
        this.pointsArray = pointsArray;
    }

    /**
     * Sets the foreground color
     */
    public void setForegroundColor(Color color) {
        fgColor = color;
    }

    /**
     * Sets the background color
     */
    public void setBackgroundColor(Color color) {
        bgColor = color;
    }

    /**
     * Sets the font
     */
    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Plots the graph
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Paint background

        // Set the background color
        if (bgColor != null) {
            super.setBackground(bgColor);
        }

        // Set the foreground color
        if (fgColor != null) {
            g.setColor(fgColor);
        }

        if ((pointsArray == null) || (pointsArray.isEmpty())) { // Plot nothing
            return;
        }

        // Set the font
        if (font != null) {
            g.setFont(font);
        }

        // Search for extremes
        PairValue firstArrayPoint = (PairValue) pointsArray.get(0);
        maxValue.x = firstArrayPoint.x;
        maxValue.y = firstArrayPoint.y;
        minValue.x = firstArrayPoint.x;
        minValue.y = firstArrayPoint.y;

        for (int i = 1; i < pointsArray.size(); i++) {
            PairValue currentPair = (PairValue) pointsArray.get(i);

            // Search for max value
            if (currentPair.x > maxValue.x) {
                maxValue.x = currentPair.x;
            }

            if (currentPair.y > maxValue.y) {
                maxValue.y = currentPair.y;
            }

            // Search for min value
            if (currentPair.x < minValue.x) {
                minValue.x = currentPair.x;
            }

            if (currentPair.y < minValue.y) {
                minValue.y = currentPair.y;
            }
        }

        if (maxValue.y == minValue.y) { // Constant graph
            maxValue.y += 0.1;
            minValue.y -= 0.1;
        }

        int maxYStrWidth = g.getFontMetrics().stringWidth(yFormatter.format(maxValue.y));
        int minYStrWidth = g.getFontMetrics().stringWidth(yFormatter.format(minValue.y));
        int maxXStrWidth = g.getFontMetrics().stringWidth(formatter.format(maxValue.x));
        int minXStrWidth = g.getFontMetrics().stringWidth(formatter.format(minValue.x));
        int strHeight = g.getFontMetrics().getHeight();

        // Get the component dimension
        getSize(componentDimension);

        // Plot the rectangle
        int rectangleXCoord = 7 + ((maxYStrWidth >= minYStrWidth) ? maxYStrWidth : minYStrWidth);
        int rectangleYCoord = strHeight / 2;
        int rectangleWidth = componentDimension.width - ((maxYStrWidth >= minYStrWidth) ? maxYStrWidth : minYStrWidth) - 7 - maxXStrWidth / 2;
        int rectangleHeight = componentDimension.height - 3 * strHeight / 2;

        g.drawRect(rectangleXCoord,
                rectangleYCoord,
                rectangleWidth,
                rectangleHeight);

        // Graduate x axis
        double delta = (maxValue.x - minValue.x) / 10;
        double unit = ((double) rectangleWidth) / 10;

        int count = 0;
        for (double i = 0; i < rectangleWidth + 1; i += unit) {
            // Graduate
            g.drawLine(rectangleXCoord + (int) i,
                    rectangleYCoord + rectangleHeight + 1,
                    rectangleXCoord + (int) i,
                    rectangleYCoord + rectangleHeight + ((count % 2 == 0) ? 4 : 2));

            // Draw number
            if (count % 2 == 0) {
                g.drawString(formatter.format(minValue.x + count * delta),
                        rectangleXCoord + (int) i - g.getFontMetrics().stringWidth(formatter.format(minValue.x + count * delta)) / 2,
                        componentDimension.height);
            }

            count++;
        }

        // Graduate y axis
        g.drawString(yFormatter.format(maxValue.y),
                (maxYStrWidth >= minYStrWidth) ? 0 : minYStrWidth - maxYStrWidth,
                strHeight);

        g.drawLine(rectangleXCoord - 1,
                rectangleYCoord,
                rectangleXCoord - 4,
                rectangleYCoord);

        g.drawLine(rectangleXCoord - 1,
                rectangleYCoord + rectangleHeight / 2,
                rectangleXCoord - 2,
                rectangleYCoord + rectangleHeight / 2);

        // Plot minValue.y
        g.drawString(yFormatter.format(minValue.y),
                (minYStrWidth >= maxYStrWidth) ? 0 : maxYStrWidth - minYStrWidth,
                componentDimension.height - strHeight / 2 - 7);

        g.drawLine(rectangleXCoord - 1,
                rectangleYCoord + rectangleHeight,
                rectangleXCoord - 4,
                rectangleYCoord + rectangleHeight);

        // Set the origin point
        g.translate(rectangleXCoord, rectangleYCoord);

        // Draw the graph
        PairValue previous = (PairValue) pointsArray.get(0);
        PairValue current;
        for (int i = 1; i < pointsArray.size(); i++) {
            current = (PairValue) pointsArray.get(i);

            // Connect points
            g.drawLine((int) (rectangleWidth / ((maxValue.x - minValue.x) / (previous.x - minValue.x))),
                    rectangleHeight - (int) (rectangleHeight / ((maxValue.y - minValue.y) / (previous.y - minValue.y))),
                    (int) (rectangleWidth / ((maxValue.x - minValue.x) / (current.x - minValue.x))),
                    rectangleHeight - (int) (rectangleHeight / ((maxValue.y - minValue.y) / (current.y - minValue.y))));

            previous = current;
        }

        // Restore the origin point
        g.translate(-rectangleXCoord, -rectangleYCoord);
    }
}
