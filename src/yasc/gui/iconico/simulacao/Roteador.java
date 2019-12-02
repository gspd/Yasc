package yasc.gui.iconico.simulacao;

import java.awt.Color;
import java.awt.Graphics;

import yasc.gui.iconico.Vertice;
import yasc.motor.filas.servidores.implementacao.CS_ServidorInfinito;

public class Roteador extends Vertice {

    private final int TAMANHO = 50;
    private final CS_ServidorInfinito net;
    
    Roteador(int coluna, int linha, CS_ServidorInfinito icone) {
        super(linha, coluna);
        this.net = icone;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(getX() - 25, getY() - 25, TAMANHO, TAMANHO);
        g.setColor(Color.BLACK);
        // Desenha tarefas
        Integer num = net.getCargaTarefas();
        g.setColor(Color.WHITE);
        g.drawString("↖   ↗", getX() - 17, getY() - 5);
        if (num > 0) {
            g.drawString(num.toString(), getX() - 5, getY() + 5);
        }
        g.drawString("↙   ↘", getX() - 17, getY() + 15);
        g.setColor(Color.BLACK);
        g.drawOval(getX() - 25, getY() - 25, TAMANHO, TAMANHO);
        g.drawString(net.getId(), getX() - 25, getY() - 35);
    }

    @Override
    public boolean contains(int x, int y) {
        if (x < getX() + 25 && x > getX() - 25) {
            if (y < getY() + 25 && y > getY() - 25) {
                return true;
            }
        }
        return false;
    }
    
}
