package yasc.gui.configuracao;

import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import yasc.Main;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;
import yasc.gui.entrada.ListasArmazenamento;
import yasc.gui.iconico.grade.FilasServidores;

public class FilasServidoresTable extends AbstractTableModel {

    // Constantes representando o índice das colunas
    private static final int TYPE = 0;
    private static final int VALUE = 1;
    private static final int LABEL = 0;
    private static final int ORIGINATION = 1;
    private static final int DESTINATION = 2;
    private static final int SERV = 3;   
    private static final int SCHED = 4;
    private static final int N_FILAS = 5;
    private static final int N_SERV = 6;
    private static int NUMLINHAS = 7;
    private static final int NUMCOLUNAS = 2;
    // Array com os nomes das linhas
    private FilasServidores filasServs;
    public static String[] ESCALONADORES = {"SJF", "MJF"};
    private JComboBox escalonador;
    private boolean capacidade = false, preempcao = false, falha = false;

    public FilasServidoresTable() {
        if (ListasArmazenamento.isFalhaCapacidade()) {
            NUMLINHAS = NUMLINHAS + 1;
            capacidade = true;
        }
        if (ListasArmazenamento.isFalha()) {
            NUMLINHAS = NUMLINHAS + 2;
            falha = true; 
        }
        preempcao = false;
        this.escalonador = new JComboBox(ESCALONADORES);
        escalonador.addActionListener((ActionEvent evt) -> {
            setValueAt(escalonador.getSelectedItem(), 4, 1);
        });
    }
    
    public void setFilas_Servidores(FilasServidores Filas_Servidores) {
        this.filasServs = Filas_Servidores;
        if (!ListasArmazenamento.listaCarregamento.get(filasServs.getImgId()).getLabels().get(0).equals("-")) {
            if(!preempcao){
                NUMLINHAS = NUMLINHAS + 1;
                preempcao = true;
            }
        }
        this.escalonador.setSelectedItem(filasServs.getScheduler());
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
        if (columnIndex == VALUE && filasServs != null) {
            switch (rowIndex) {
                case LABEL:
                    filasServs.getId().setNome(aValue.toString());
                    break;
                case ORIGINATION:
                    filasServs.setOrigination(Boolean.valueOf(aValue.toString()));
                    break;
                case DESTINATION:
                    filasServs.setDestination(Boolean.valueOf(aValue.toString()));
                    break;    
                case SERV:
                    filasServs.setServiceRate(Double.valueOf(aValue.toString()));
                    break;
                case SCHED:
                    filasServs.setScheduler(escalonador.getSelectedItem().toString());
                    break;
                case N_FILAS:
                    filasServs.setNumQueues(Integer.valueOf(aValue.toString()));
                    break;
                case N_SERV:
                    filasServs.setNumServ(Integer.valueOf(aValue.toString()));
                    break;
                // Criação de elementos que variam de acordo com a configuração escolhida pelo usuário ao
                // Criar o simulador
                case 7:
                    if (NUMLINHAS == 8 || (NUMLINHAS == 9 && preempcao) || (NUMLINHAS == 10 && preempcao) || NUMLINHAS == 11) {
                        filasServs.setPreemptionTime(Double.valueOf(aValue.toString()));
                    }
                    if ((NUMLINHAS == 9 && falha)|| (NUMLINHAS == 10 && capacidade)) {
                        filasServs.setProbServerFailure(Double.valueOf(aValue.toString()));
                    }
                    break;
                case 8:
                    if ((NUMLINHAS == 9 && falha) || (NUMLINHAS == 10 && capacidade)){
                        filasServs.setProbServiceFailure(Double.valueOf(aValue.toString()));
                    }
                    if ((NUMLINHAS == 10 && preempcao) || NUMLINHAS == 11) {
                        filasServs.setProbServerFailure(Double.valueOf(aValue.toString()));
                    }
                    break;
                case 9:
                    if ((NUMLINHAS == 10 && preempcao) || NUMLINHAS == 11) {
                        filasServs.setProbServiceFailure(Double.valueOf(aValue.toString()));
                    }
                    if((NUMLINHAS == 10 && capacidade))
                        filasServs.setCapacity(Integer.valueOf(aValue.toString()));
                    break;
                case 10:
                    if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 11) {
                        filasServs.setCapacity(Integer.valueOf(aValue.toString()));
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
                    case SCHED:
                        return Main.languageResource.getString("Scheduler");
                    case SERV:
                        return (Main.languageResource.getString("Service Time") + " " + "(" + ListasArmazenamento.listaCarregamento.get(filasServs.getImgId()).getMetricas().get(1) + ")");
                    case N_FILAS:
                        return ListasArmazenamento.listaCarregamento.get(filasServs.getImgId()).getLabels().get(2);
                    case N_SERV:
                        return ListasArmazenamento.listaCarregamento.get(filasServs.getImgId()).getLabels().get(3);
                    // Criação de elementos que variam de acordo com a configuração escolhida pelo usuário ao
                    // Criar o simulador
                    case 7:
                        if (NUMLINHAS == 8 || (NUMLINHAS == 9 && preempcao) || (NUMLINHAS == 10 && preempcao) || NUMLINHAS == 11) {
                            return (Main.languageResource.getString("Preemption") + " " + ("(%)"));
                        }
                        if ((NUMLINHAS == 9 && falha) || (NUMLINHAS == 10 && capacidade)) {
                            return Main.languageResource.getString("Probability of server failure");
                        }
                    case 8:
                        if ((NUMLINHAS == 9 && falha) || (NUMLINHAS == 10 && capacidade)){
                            return Main.languageResource.getString("Probability of attendance failure");
                        }
                        if ((NUMLINHAS == 10 && preempcao) || NUMLINHAS == 11) {
                            return Main.languageResource.getString("Probability of server failure");
                        }
                    case 9:
                        if ((NUMLINHAS == 10 && preempcao) || NUMLINHAS == 11) {
                            return Main.languageResource.getString("Probability of attendance failure");
                        }
                        if (NUMLINHAS == 10 && capacidade){
                            return Main.languageResource.getString("Capacity");
                        }
                            
                    case 10:
                        if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 11) {
                            return Main.languageResource.getString("Capacity");
                        }    
                }
            case VALUE:
                if (filasServs != null) {
                    switch (rowIndex) {
                        case LABEL:
                            return filasServs.getId().getNome();
                        case ORIGINATION:
                            return filasServs.isOrigination();
                        case DESTINATION:
                            return filasServs.isDestination();
                        case SERV:
                            return filasServs.getServiceRate();
                        case SCHED:
                            return escalonador;
                        case N_FILAS:
                            return filasServs.getNumQueues();
                        case N_SERV:
                            return filasServs.getNumServ();                  
                        // Criação de elementos que variam de acordo com a configuração escolhida pelo usuário ao
                        // Criar o simulador
                        case 7:
                            if (NUMLINHAS == 8 || (NUMLINHAS == 9 && preempcao) || (NUMLINHAS == 10 && preempcao) || NUMLINHAS == 11) {
                                return filasServs.getPreemptionTime();
                            }
                            if ((NUMLINHAS == 9 && falha) || (NUMLINHAS == 10 && capacidade)) {
                                return filasServs.getProbServerFailure();
                            }
                        case 8:
                            if ((NUMLINHAS == 9 && falha) || (NUMLINHAS == 10 && capacidade)){
                                return filasServs.getProbServiceFailure();
                            }
                            if ((NUMLINHAS == 10 && preempcao) || NUMLINHAS == 11) {
                                return filasServs.getProbServerFailure();
                            }
                        case 9:
                            if ((NUMLINHAS == 10 && preempcao) || NUMLINHAS == 11) {
                                return filasServs.getProbServiceFailure();
                            }
                            if ((NUMLINHAS == 10  && capacidade)){
                                return filasServs.getCapacity();
                            }
                        case 10:
                            if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 11) {
                                return filasServs.getCapacity();
                            }
                    }
                } else {
                    switch(rowIndex){
                        case SCHED:
                            return escalonador;
                        default:
                            return "null";
                    }
                }
            default:
                // Não deve ocorrer, pois só existem 2 colunas
                throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }
    }
}
