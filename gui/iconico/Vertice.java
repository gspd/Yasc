package yasc.gui.iconico;

import java.awt.event.ActionEvent;

public abstract class Vertice implements Icone {

    private String exclusiveItem;
    private boolean selected;
    private int baseX;
    private int baseY;
    private Integer x;
    private Integer y;

    public Vertice(Integer x, Integer y) {
        this.x = x;
        this.y = y;
        this.selected = false;
    }

    @Override
    public Integer getX() {
        return x;
    }

    @Override
    public Integer getY() {
        return y;
    }
    
    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void exclusiveItemActionPerformed(ActionEvent evt) { }
    
    public int getBaseX() {
        return baseX;
    }

    public int getBaseY() {
        return baseY;
    }

    public void setBase(Integer x, Integer y) {
        baseX = x;
        baseY = y;
    }

    public void setPosition(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public String getExclusiveItem() {
        return exclusiveItem;
    }

    public void setExclusiveItem(String exclusiveItem) {
        this.exclusiveItem = exclusiveItem;
    }
}
