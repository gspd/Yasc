package yasc.gui.iconico;

import java.awt.Graphics;

public interface Icone {
    
    public boolean isSelected();
    
    public void setSelected(boolean selected);
    
    public void draw(Graphics g);
    
    public boolean contains(int x, int y);
    /**
     * Posição do objeto no eixo X
     */
    public Integer getX();
    /**
     * Posição do objeto no eixo Y
     */
    public Integer getY();
}
