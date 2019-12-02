package yasc.gui.configuracao;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CheckListRenderer extends DefaultListSelectionModel implements ListCellRenderer {

    private final JCheckBox jcb;
    private Color background;
    private final Color selectionBackground;

    public CheckListRenderer(JList jlist) {
        jcb = new JCheckBox();
        selectionBackground = jlist.getSelectionBackground();
        jlist.setCellRenderer(getInstance());
        jlist.setSelectionModel(getInstance());
        jlist.setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
        jlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList jList1 = (JList) event.getSource();
                int index = jList1.locationToIndex(event.getPoint());
                if (jList1.isSelectedIndex(index)) {
                    jList1.removeSelectionInterval(index, index);
                } else {
                    jList1.addSelectionInterval(index, index);
                }
            }
        });
    }
    
    public final CheckListRenderer getInstance() {
        return this;
    }

    @Override
    public void setSelectionInterval(int i, int i1) {
    }

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean hasFocus) {
        jcb.setSelected(isSelected);
        if (isSelected) {
            jcb.setBackground(selectionBackground);
        } else {
            jcb.setBackground(background);
        }
        jcb.setText(value.toString());
        return jcb;
    }
}