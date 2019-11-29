package yasc.gui.configuracao;

import java.util.Locale;
import java.util.ResourceBundle;
import yasc.Main;
import javax.swing.table.AbstractTableModel;
import yasc.gui.iconico.grade.Instantaneo;

public class InstantaneoTable extends AbstractTableModel {

    // Constantes representando o índice das colunas
    private static final int TYPE = 0;
    private static final int VALUE = 1;
    private static final int LABEL = 0;
    private static final int ORIGINATION = 1;
    private static final int DESTINATION = 2;
    private static final int VAR = 3;
    private static int NUMLINHAS;
    private static final int NUMCOLUNAS = 2;
    // Array com os nomes das linhas
    private Instantaneo Inst;
    
    public void setInstantaneo(Instantaneo Instantaneo) {
        this.Inst = Instantaneo;
        NUMLINHAS = 3 + Inst.getTipos().size();
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
        if (columnIndex == VALUE && Inst != null) {
            switch (rowIndex) {
                case LABEL:
                    Inst.getId().setNome(aValue.toString());
                    break;
                case ORIGINATION:
                    Inst.setOrigination(Boolean.parseBoolean(aValue.toString()));
                    break;
                case DESTINATION:
                    Inst.setDestination(Boolean.parseBoolean(aValue.toString()));
                    break;
                default:
                    Inst.setValores(aValue.toString(), rowIndex - 3);
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
                    case ORIGINATION:
                        return Main.languageResource.getString("Origination");
                    case DESTINATION:
                        return Main.languageResource.getString("Destination");                       
                    default:
                        return (Inst.getTipos().get(rowIndex-3) + " " + Inst.getVars().get(rowIndex-3));         
                }
            case VALUE:
                if (Inst != null) {
                    switch (rowIndex) {
                        case LABEL:
                            return Inst.getId().getNome();
                        case ORIGINATION:
                            return Inst.isOrigination();
                        case DESTINATION:
                            return Inst.isDestination();
                        default:
                            return Inst.getValores().get(rowIndex-3);
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
