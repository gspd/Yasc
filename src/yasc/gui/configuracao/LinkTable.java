package yasc.gui.configuracao;

import yasc.Main;
import javax.swing.table.AbstractTableModel;
import yasc.gui.iconico.grade.ItemGrade;

public class LinkTable extends AbstractTableModel {

    // Constantes representando o índice das colunas
    private static final int TYPE = 0;
    private static final int VALUE = 1;
    private static final int LABEL = 0;
    private static final int NUMLINHAS = 1;
    private static final int NUMCOLUNAS = 2;
    // Array com os nomes das linhas
    private ItemGrade link;
    
    public void setLink(ItemGrade link) {
        this.link = link;
    }

    @Override
    public int getRowCount() {
        return NUMLINHAS;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case TYPE:
                return Main.languageResource.getString("Properties");
            case VALUE:
                return Main.languageResource.getString("Values");
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return !(columnIndex == TYPE);
    }

    @Override
    public int getColumnCount() {
        return NUMCOLUNAS;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // Pega o sócio referente a linha especificada.
        if (columnIndex == VALUE && link != null) {
            switch (rowIndex) {
                case LABEL:
                    link.getId().setNome(aValue.toString());
                    break;
            }
            fireTableCellUpdated(rowIndex, columnIndex); // Notifica a atualização da célula
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case TYPE:
                switch (rowIndex) {
                    case LABEL:
                        return Main.languageResource.getString("Label");
                }
            case VALUE:
                if (link != null) {
                    switch (rowIndex) {
                        case LABEL:
                            return link.getId().getNome();
                    }
                } else {
                    return "null";
                }
            default:
                // Não deve ocorrer, pois só existem 2 colunas
                throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }
    }
}
