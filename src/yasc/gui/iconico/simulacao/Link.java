package yasc.gui.iconico.simulacao;

import java.awt.Color;
import java.awt.Graphics;
import yasc.gui.iconico.Aresta;
import yasc.gui.iconico.Vertice;
import yasc.motor.filas.servidores.implementacao.CS_FilaServidor;

public class Link extends Aresta {

    public CS_FilaServidor link;
    private final DesenhoSimulacao aDesenho;

    public Link(Vertice origem, Vertice destino, CS_FilaServidor link, DesenhoSimulacao aDesenho) {
        super(origem, destino, null, null);
        this.aDesenho = aDesenho;
        this.link = link;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean selected) {
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawLine(getOrigin().getX(), getOrigin().getY(), getDestiny().getX(), getDestiny().getY());
        // Desenha tarefas
        if (link != null) {
            int num = link.getCargaTarefas();
            if (num > 0) {
                String seta;
                if (getOrigin().getX() < getDestiny().getX()) {
                    seta = "▶";
                } else if (getOrigin().getX() > getDestiny().getX()) {
                    seta = "◀";
                } else if (getOrigin().getY() < getDestiny().getY()) {
                    seta = "▼";
                } else {
                    seta = "▲";
                }
                // Movimento
                int incX = (getDestiny().getX() - getOrigin().getX()) / aDesenho.getSetas();
                int incY = (getDestiny().getY() - getOrigin().getY()) / aDesenho.getSetas();
                g.drawString(seta, getOrigin().getX() + incX, getOrigin().getY() + incY    );
                g.drawString(seta, getOrigin().getX() + incX * 2, getOrigin().getY() + incY * 2);
                g.drawString(seta, getOrigin().getX() + incX * 3, getOrigin().getY() + incY * 3);
                g.drawString(seta, getOrigin().getX() + incX * 4, getOrigin().getY() + incY * 4);
                // Tarefas
                g.setColor(Color.YELLOW);
                g.fillRect(getX(), getY(), 30, 30);
                g.setColor(Color.BLACK);
                g.drawString(num + seta, getX(), getY() + 20);
            }
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return false;
    }
}