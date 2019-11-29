package yasc.gui.configuracao;

import yasc.Main;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;
import yasc.gui.entrada.ListasArmazenamento;
import yasc.gui.iconico.grade.FilaServidor;

public class FilaServidorTable extends AbstractTableModel {

    // Constantes representando o índice das colunas
    private static final int TYPE = 0;
    private static final int VALUE = 1;
    private static final int LABEL = 0;
    private static final int ORIGINATION = 1;
    private static final int DESTINATION = 2;
    private static final int SERV = 3;//Service Time
    private static final int SCHED = 4;
    private static int NUMLINHAS = 5;
    private static final int NUMCOLUNAS = 2;
    // Array com os nomes das linhas
    private FilaServidor Fila_Serv;
    public static String[] ESCALONADORES = {"FIFO", "LIFO", "SJF"};
    private JComboBox escalonador;
    private boolean capacidade = false, preempcao = false, falha = false;

    public FilaServidorTable() {
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

    public void setFila_Servidor(FilaServidor Fila_Servidor) {
        this.Fila_Serv = Fila_Servidor;
        if (!ListasArmazenamento.listaCarregamento.get(Fila_Serv.getImgId()).getLabels().get(0).equals("-")) {
            if(!preempcao){
                NUMLINHAS = NUMLINHAS + 1;
                preempcao = true;
            }
        }
        this.escalonador.setSelectedItem(Fila_Serv.getScheduler());
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
        if (columnIndex == VALUE && Fila_Serv != null) {
            switch (rowIndex) {
                case LABEL:
                    Fila_Serv.getId().setNome(aValue.toString());
                    break;
                case SERV:
                    Fila_Serv.setServiceRate(Double.valueOf(aValue.toString()));
                    break;
                case ORIGINATION:
                    Fila_Serv.setOrigination(Boolean.valueOf(aValue.toString()));
                    break;
                case DESTINATION:
                    Fila_Serv.setDestination(Boolean.valueOf(aValue.toString()));
                    break;
                case SCHED:
                    Fila_Serv.setScheduler(escalonador.getSelectedItem().toString());
                    break;
                //criação de elementos que variam de acordo com a configuração escolhida pelo usuário ao
                //criar o simulador
                case 5:
                    if (NUMLINHAS == 6 || (NUMLINHAS == 7 && preempcao) || (NUMLINHAS == 8 && preempcao) || NUMLINHAS == 9) {
                        Fila_Serv.setPreemptionTime(Double.valueOf(aValue.toString()));
                    }
                    if ((NUMLINHAS == 7 && falha) || (NUMLINHAS == 8 && capacidade)) {
                        Fila_Serv.setProbServerFailure(Double.valueOf(aValue.toString()));
                    }
                    break;
                case 6:
                    if ((NUMLINHAS == 7 && falha) || (NUMLINHAS == 8 && capacidade)){
                        Fila_Serv.setProbServiceFailure(Double.valueOf(aValue.toString()));
                    }
                    if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 9) {
                        Fila_Serv.setProbServerFailure(Double.valueOf(aValue.toString()));
                    }
                    break;
                case 7:
                    if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 9) {
                        Fila_Serv.setProbServiceFailure(Double.valueOf(aValue.toString()));
                    }
                    if((NUMLINHAS == 8 && capacidade))
                        Fila_Serv.setCapacity(Integer.valueOf(aValue.toString()));
                    break;
                case 8:
                    if ((NUMLINHAS == 7 && preempcao) || NUMLINHAS == 9) {
                        Fila_Serv.setCapacity(Integer.valueOf(aValue.toString()));
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
                    case SCHED:
                        return Main.languageResource.getString("Scheduler");
                    case SERV:
                        return (Main.languageResource.getString("Service Time") + " " + "(" + ListasArmazenamento.listaCarregamento.get(Fila_Serv.getImgId()).getMetricas().get(1) + ")");
                    case ORIGINATION:
                        return Main.languageResource.getString("Origination");
                    case DESTINATION:
                        return Main.languageResource.getString("Destination");
                    // Criação de elementos que variam de acordo com a configuração escolhida pelo usuário ao
                    // Criar o simulador
                    case 5:
                        if (NUMLINHAS == 6 || (NUMLINHAS == 7 && preempcao) || (NUMLINHAS == 8 && preempcao) || NUMLINHAS == 9) {
                            return (Main.languageResource.getString("Preemption") + " " + ("(%)"));
                        }
                        if ((NUMLINHAS == 7 && falha) || (NUMLINHAS == 8 && capacidade)) {
                            return Main.languageResource.getString("Probability of server failure");
                        }
                    case 6:
                        if ((NUMLINHAS == 7 && falha) || (NUMLINHAS == 8 && capacidade)){
                            return Main.languageResource.getString("Probability of attendance failure");
                        }
                        if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 9) {
                            return Main.languageResource.getString("Probability of server failure");
                        }
                    case 7:
                        if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 9) {
                            return Main.languageResource.getString("Probability of attendance failure");
                        }
                        if (NUMLINHAS == 8 && capacidade){
                            return Main.languageResource.getString("Capacity");
                        }
                            
                    case 8:
                        if ((NUMLINHAS == 7 && preempcao) || NUMLINHAS == 9) {
                            return Main.languageResource.getString("Capacity");
                        }
                }
            case VALUE:
                if (Fila_Serv != null) {
                    switch (rowIndex) {
                        case LABEL:
                            return Fila_Serv.getId().getNome();
                        case SERV:
                            return Fila_Serv.getServiceRate();
                        case SCHED:
                            return escalonador;
                        case ORIGINATION:
                            return Fila_Serv.isOrigination();
                        case DESTINATION:
                            return Fila_Serv.isDestination();
                        // Criação de elementos que variam de acordo com a configuração escolhida pelo usuário ao
                        // Criar o simulador
                        case 5:
                            if (NUMLINHAS == 6 || (NUMLINHAS == 7 && preempcao) || (NUMLINHAS == 8 && preempcao) || NUMLINHAS == 9) {
                                return Fila_Serv.getPreemptionTime();
                            }
                            if ((NUMLINHAS == 7 && falha) || (NUMLINHAS == 8 && capacidade)) {
                                return Fila_Serv.getProbServerFailure();
                            }
                        case 6:
                            if ((NUMLINHAS == 7 && falha) || (NUMLINHAS == 8 && capacidade)){
                                return Fila_Serv.getProbServiceFailure();
                            }
                            if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 9) {
                                return Fila_Serv.getProbServerFailure();
                            }
                        case 7:
                            if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 9) {
                                return Fila_Serv.getProbServiceFailure();
                            }
                            if ((NUMLINHAS == 8  && capacidade)){
                                return Fila_Serv.getCapacity();
                            }
                        case 8:
                            if ((NUMLINHAS == 7 && preempcao) || NUMLINHAS == 9) {
                                return Fila_Serv.getCapacity();
                            }
                    }
                } else {
                    switch (rowIndex) {
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
