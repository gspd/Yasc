package yasc.gui.configuracao;

import java.util.Locale;
import java.util.ResourceBundle;
import yasc.Main;
import javax.swing.table.AbstractTableModel;
import yasc.gui.entrada.ListasArmazenamento;
import yasc.gui.iconico.grade.ServidorInfinito;

public class ServidorInfinitoTable extends AbstractTableModel {

    // Constantes representando o índice das colunas
    private static final int TYPE = 0;
    private static final int VALUE = 1;
    private static final int LABEL = 0;
    private static final int ORIGINATION = 1;
    private static final int DESTINATION = 2;
    private static final int SERV = 3;
    private static int NUMLINHAS = 4;
    private static final int NUMCOLUNAS = 2;
    // Array com os nomes das linhas
    private ServidorInfinito Serv;
    private boolean preempcao = false, falha = false;

    public ServidorInfinitoTable() {
        if (ListasArmazenamento.isFalha()) {
            NUMLINHAS = NUMLINHAS + 2;
            falha = true;
        }
        preempcao = false;
    }

    public void setServidor_Infinito(ServidorInfinito ServidorInfinito) {
        this.Serv = ServidorInfinito;
        if (!ListasArmazenamento.listaCarregamento.get(Serv.getImgId()).getLabels().get(0).equals("-")) {
            if(!preempcao){
                NUMLINHAS = NUMLINHAS + 1;
                preempcao = true;
            }
        }
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
        if (columnIndex == VALUE && Serv != null) {
            switch (rowIndex) {
                case LABEL:
                    Serv.getId().setNome(aValue.toString());
                    break;
                case SERV:
                    Serv.setServiceRate(Double.valueOf(aValue.toString()));
                    break;
                case ORIGINATION:
                    Serv.setOrigination(Boolean.parseBoolean(aValue.toString()));
                    break;
                case DESTINATION:
                    Serv.setDestination(Boolean.parseBoolean(aValue.toString()));
                    break;
                case 4:
                    if (NUMLINHAS == 5 || (NUMLINHAS == 6 && preempcao) || NUMLINHAS == 7) {
                        Serv.setPreemptionTime(Double.valueOf(aValue.toString()));
                    }
                    if (NUMLINHAS == 6 && falha) {
                        Serv.setProbServerFailure(Double.valueOf(aValue.toString()));
                    }
                    break;
                case 5:
                    if (NUMLINHAS == 6 && falha){
                        Serv.setProbServiceFailure(Double.valueOf(aValue.toString()));
                    }
                    if (NUMLINHAS == 7) {
                        Serv.setProbServerFailure(Double.valueOf(aValue.toString()));
                    }
                    break;
                case 6:
                    if ((NUMLINHAS == 6 && falha) || NUMLINHAS == 7) {
                        Serv.setProbServiceFailure(Double.valueOf(aValue.toString()));
                    }
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
                    case SERV:
                        return Main.languageResource.getString("Service Time");
                    case 4:
                        if (NUMLINHAS == 5  || (NUMLINHAS == 6 && preempcao) || NUMLINHAS == 7) {
                            return (Main.languageResource.getString("Preemption") + " " + ("(%)"));
                        }
                        if ((NUMLINHAS == 6 && falha)) {
                            return Main.languageResource.getString("Probability of server failure");
                        }
                    case 5:
                        if ((NUMLINHAS == 6 && falha)){
                            return Main.languageResource.getString("Probability of attendance failure");
                        }
                        if (NUMLINHAS == 7) {
                            return Main.languageResource.getString("Probability of server failure");
                        }
                    case 6:
                        if ((NUMLINHAS == 6 && preempcao) || NUMLINHAS == 7) {
                            return Main.languageResource.getString("Probability of attendance failure");
                        }
                }
            case VALUE:
                if (Serv != null) {
                    switch (rowIndex) {
                        case LABEL:
                            return Serv.getId().getNome();
                        case ORIGINATION:
                            return Serv.isOrigination();
                        case DESTINATION:
                            return Serv.isDestination();
                        case SERV:
                            return Serv.getServiceRate();
                        case 4:
                        if (NUMLINHAS == 5 || (NUMLINHAS == 6 && preempcao) || NUMLINHAS == 7) {
                            return Serv.getPreemptionTime();
                        }
                        if ((NUMLINHAS == 6 && falha)) {
                            return Serv.getProbServerFailure();
                        }
                    case 5:
                        if ((NUMLINHAS == 6 && falha)){
                            return Serv.getProbServiceFailure();
                        }
                        if (NUMLINHAS == 7) {
                            return Serv.getProbServerFailure();
                        }
                    case 6:
                        if ((NUMLINHAS == 8 && falha) || NUMLINHAS == 7) {
                            return Serv.getProbServiceFailure();
                        }
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
