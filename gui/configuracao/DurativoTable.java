package yasc.gui.configuracao;

import java.util.Locale;
import java.util.ResourceBundle;
import yasc.Main;
import javax.swing.table.AbstractTableModel;
import yasc.gui.iconico.grade.Durativo;

public class DurativoTable extends AbstractTableModel {

    // Constantes representando o índice das colunas
    private static final int TYPE = 0;
    private static final int VALUE = 1;
    private static final int LABEL = 0;
    private static final int ORIGINATION = 1;
    private static final int DESTINATION = 2;
    private static final int TIME = 3;
    private static int NUMLINHAS;
    private static final int NUMCOLUNAS = 2;
    // Array com os nomes das linhas
    private Durativo Durativo;
    
    public void setDurativo(Durativo Durativo) {
        this.Durativo = Durativo;
        NUMLINHAS = 4 + Durativo.getTipos().size();
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
        if (columnIndex == VALUE && Durativo != null) {
            switch (rowIndex) {
                case LABEL:
                    Durativo.getId().setNome(aValue.toString());
                    break;
                case ORIGINATION:
                    Durativo.setOrigination(Boolean.parseBoolean(aValue.toString()));
                    break;
                case DESTINATION:
                    Durativo.setDestination(Boolean.parseBoolean(aValue.toString()));
                    break;
                case TIME:
                    Durativo.setTimeTransf(Double.valueOf(aValue.toString()));
                    break;
                default:
                    Durativo.setValores(aValue.toString(), rowIndex - 4);
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
                    case TIME:
                        return Main.languageResource.getString("Transfer Time");
                    default:
                        return Durativo.getTipos().get(rowIndex-4) + " " + Durativo.getVars().get(rowIndex-4);
                        
                }
            case VALUE:
                if (Durativo != null) {
                    switch (rowIndex) {
                        case LABEL:
                            return Durativo.getId().getNome();
                        case ORIGINATION:
                            return Durativo.isOrigination();
                        case DESTINATION:
                            return Durativo.isDestination();
                        case TIME:
                            return Durativo.getTimeTransf();
                        default:
                            return Durativo.getValores().get(rowIndex-4);
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
