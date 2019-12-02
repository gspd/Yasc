package yasc.gui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JWindow;

/**
 * Janela de carregamento do iSPD, chamada durante a inicialização do programa
 */
public class SplashWindow extends JWindow {

    private static final int ALTURA = 20;
    private static final int LARGURA = 40;
    private Point posicaoTexto;
    private String text = "";
    private BufferedImage splash;
    private ImageIcon imagem;

    public SplashWindow(ImageIcon image) {
        int width = image.getIconWidth() + LARGURA * 2;
        int height = image.getIconHeight() + ALTURA * 2;
        posicaoTexto = new Point(40, width - 50);
        text = "";
        setSize(new Dimension(width, height));

        setLocationRelativeTo(null);
        Rectangle windowRect = getBounds();
        imagem = image;
        splash = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        try {
            Graphics2D g2 = (Graphics2D) splash.getGraphics();
            Robot robot = new Robot(
                    getGraphicsConfiguration().getDevice());
            // Captura imagem do fundo
            BufferedImage capture = robot.createScreenCapture(
                    new Rectangle(windowRect.x, windowRect.y,
                            windowRect.width,
                            windowRect.height));
            g2.drawImage(capture, 0, 0, null);
            // Desenha retangulo transparente
            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillRoundRect(6, 6, width - 6, height - 6, 12, 12);
            g2.dispose();
        } catch (AWTException ex) {
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics offgc;
        Dimension d = getSize();
        // Create the offscreen buffer and associated Graphics
        Image offscreen = createImage(d.width, d.height);
        offgc = offscreen.getGraphics();
        // Do normal redraw
        offgc.drawImage(splash, 0, 0, null);
        imagem.paintIcon(this, offgc, LARGURA, ALTURA);
        offgc.drawString(text, posicaoTexto.x, posicaoTexto.y);
        // Transfer offscreen to window
        g.drawImage(offscreen, 0, 0, this);
    }

    public void setText(String text) {
        this.text = text;
    }
}
