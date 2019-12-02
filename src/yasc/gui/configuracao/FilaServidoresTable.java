package yasc.gui.configuracao;

import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import yasc.Main;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;
import yasc.gui.entrada.ListasArmazenamento;
import yasc.gui.iconico.grade.FilaServidores;

public class FilaServidoresTable extends AbstractTableModel {

    // Constantes representando o índice das colunas
    private static final int TYPE = 0;
    private static final int VALUE = 1;
    private static final int LABEL = 0;
    private static final int ORIGINATION = 1;
    private static final int DESTINATION = 2;
    private static final int SERV = 3;   
    private static final int SCHED = 4;
    private static final int N_SERV = 5;
    private static int NUMLINHAS = 6;
    private static final int NUMCOLUNAS = 2;
    // Array com os nomes das linhas
    private FilaServidores Fila_Servs;
    public static String[] ESCALONADORES = {"FIFO", "LIFO", "SJF"};
    private JComboBox escalonador;
    private boolean capacidade = false, preempcao = false, falha = false;

    public FilaServidoresTable() {
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
    
    public void setFila_Servidores(FilaServidores Fila_Servidores) {
        this.Fila_Servs = Fila_Servidores;
        if (!ListasArmazenamento.listaCarregamento.get(Fila_Servs.getImgId()).getLabels().get(0).equals("-")) {
            if(!preempcao){
                NUMLINHAS = NUMLINHAS + 1;
                preempcao = true;
            }
        }
        this.escalonador.setSelectedItem(Fila_Servs.getScheduler());
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
        if (columnIndex == VALUE && Fila_Servs != null) {
            switch (rowIndex) {
                case LABEL:
                    Fila_Servs.getId().setNome(aValue.toString());
                    break;
                case ORIGINATION:
                    Fila_Servs.setOrigination(Boolean.valueOf(aValue.toString()));
                    break;
                case DESTINATION:
                    Fila_Servs.setDestination(Boolean.valueOf(aValue.toString()));
                    break;
                case SERV:
                    Fila_Servs.setServiceRate(Double.valueOf(aValue.toString()));
                    break;
                case SCHED:
                    Fila_Servs.setScheduler(escalonador.getSelectedItem().toString());   
                    break;
                case N_SERV:
                    Fila_Servs.setNumServ(Integer.valueOf(aValue.toString()));
                    break;              
                case 6:
                    if (NUMLINHAS == 7 || (NUMLINHAS == 8 && preempcao) || (NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                        Fila_Servs.setPreemptionTime(Double.valueOf(aValue.toString()));
                    }
                    if ((NUMLINHAS == 8 && falha) || (NUMLINHAS == 9 && capacidade)) {
                        Fila_Servs.setProbServerFailure(Double.valueOf(aValue.toString()));
                    }
                    break;
                case 7:
                    if ((NUMLINHAS == 8 && falha) || (NUMLINHAS == 9 && capacidade)){
                        Fila_Servs.setProbServiceFailure(Double.valueOf(aValue.toString()));
                    }
                    if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                        Fila_Servs.setProbServerFailure(Double.valueOf(aValue.toString()));
                    }
                    break;
                case 8:
                    if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                        Fila_Servs.setProbServiceFailure(Double.valueOf(aValue.toString()));
                    }
                    if((NUMLINHAS == 9 && capacidade))
                        Fila_Servs.setCapacity(Integer.valueOf(aValue.toString()));
                    break;
                case 9:
                    if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 10) {
                        Fila_Servs.setCapacity(Integer.valueOf(aValue.toString()));
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
                        return (Main.languageResource.getString("Service Time") + " " + "(" + ListasArmazenamento.listaCarregamento.get(Fila_Servs.getImgId()).getMetricas().get(1) + ")");
                    case N_SERV:
                        return ListasArmazenamento.listaCarregamento.get(Fila_Servs.getImgId()).getLabels().get(2);
                    // Criação de elementos que variam de acordo com a configuração escolhida pelo usuário ao
                    // Criar o simulador
                    case 6:
                        if (NUMLINHAS == 7 || (NUMLINHAS == 8 && preempcao) || (NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                            return (Main.languageResource.getString("Preemption") + " " + ("(%)"));
                        }
                        if ((NUMLINHAS == 8 && falha) || (NUMLINHAS == 9 && capacidade)) {
                            return Main.languageResource.getString("Probability of server failure");
                        }
                    case 7:
                        if ((NUMLINHAS == 8 && falha) || (NUMLINHAS == 9 && capacidade)){
                            return Main.languageResource.getString("Probability of attendance failure");
                        }
                        if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                            return Main.languageResource.getString("Probability of server failure");
                        }
                    case 8:
                        if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                            return Main.languageResource.getString("Probability of attendance failure");
                        }
                        if (NUMLINHAS == 9 && capacidade){
                            return Main.languageResource.getString("Capacity");
                        }
                            
                    case 9:
                        if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 10) {
                            return Main.languageResource.getString("Capacity");
                        }
                }
            case VALUE:
                if (Fila_Servs != null) {
                    switch (rowIndex) {
                        case LABEL:
                            return Fila_Servs.getId().getNome();
                        case ORIGINATION:
                            return Fila_Servs.isOrigination();
                        case DESTINATION:
                            return Fila_Servs.isDestination();
                        case SERV:
                            return Fila_Servs.getServiceRate();
                        case SCHED:
                            return escalonador;
                        case N_SERV:
                            return Fila_Servs.getNumServ();
                        // Criação de elementos que variam de acordo com a configuração escolhida pelo usuário ao
                        // Criar o simulador
                        case 6:
                            if (NUMLINHAS == 7 || (NUMLINHAS == 8 && preempcao) || (NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                                return Fila_Servs.getPreemptionTime();
                            }
                            if ((NUMLINHAS == 8 && falha) || (NUMLINHAS == 9 && capacidade)) {
                                return Fila_Servs.getProbServerFailure();
                            }
                        case 7:
                            if ((NUMLINHAS == 8 && falha) || (NUMLINHAS == 9 && capacidade)){
                                return Fila_Servs.getProbServiceFailure();
                            }
                            if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                                return Fila_Servs.getProbServerFailure();
                            }
                        case 8:
                            if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                                return Fila_Servs.getProbServiceFailure();
                            }
                            if ((NUMLINHAS == 9  && capacidade)){
                                return Fila_Servs.getCapacity();
                            }
                        case 9:
                            if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 10) {
                                return Fila_Servs.getCapacity();
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
