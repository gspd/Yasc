package yasc.arquivo.xml;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

class SimpleErrorHandler implements ErrorHandler {

    @Override
    public void warning(SAXParseException e) {
        JFrame frame = new JFrame("Warning");
        JOptionPane.showMessageDialog(frame, e.getMessage());
        System.exit(0);
    }

    @Override
    public void error(SAXParseException e) {
        JFrame frame = new JFrame("Warning");
        JOptionPane.showMessageDialog(frame, e.getMessage());
        System.exit(0);
    }

    @Override
    public void fatalError(SAXParseException e) {
        JFrame frame = new JFrame("Warning");
        JOptionPane.showMessageDialog(frame, e.getMessage());
        System.exit(0);
    }
}
