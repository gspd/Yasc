package yasc.gui.configuracao;

import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import yasc.Main;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;
import yasc.gui.entrada.ListasArmazenamento;
import yasc.gui.iconico.grade.FilasServidor;

public class FilasServidorTable extends AbstractTableModel {

    // Constantes representando o índice das colunas
    private static final int TYPE = 0;
    private static final int VALUE = 1;
    private static final int LABEL = 0;
    private static final int ORIGINATION = 1;
    private static final int DESTINATION = 2;
    private static final int SERV = 3;   
    private static final int SCHED = 4;
    private static final int N_FILAS = 5;
    private static int NUMLINHAS = 6;
    private static final int NUMCOLUNAS = 2;
    // Array com os nomes das linhas
    private FilasServidor Filas_Serv;
    public static String[] ESCALONADORES = {"SJF", "MJF"};
    private JComboBox escalonador;
    private boolean capacidade = false, preempcao = false, falha = false;

    public FilasServidorTable() {
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
    
    public void setFilas_Servidor(FilasServidor Filas_Servidor) {
        this.Filas_Serv = Filas_Servidor;
        if (!ListasArmazenamento.listaCarregamento.get(Filas_Serv.getImgId()).getLabels().get(0).equals("-")) {
            if(!preempcao){
                NUMLINHAS = NUMLINHAS + 1;
                preempcao = true;
            }
        }
        this.escalonador.setSelectedItem(Filas_Serv.getScheduler());
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
        if (columnIndex == VALUE && Filas_Serv != null) {
            switch (rowIndex) {
                case LABEL:
                    Filas_Serv.getId().setNome(aValue.toString());
                    break;
                case ORIGINATION:
                    Filas_Serv.setOrigination(Boolean.valueOf(aValue.toString()));
                    break;
                case DESTINATION:
                    Filas_Serv.setDestination(Boolean.valueOf(aValue.toString()));
                    break;
                case SERV:
                    Filas_Serv.setServiceRate(Double.valueOf(aValue.toString()));
                    break;
                case SCHED:
                    Filas_Serv.setScheduler(escalonador.getSelectedItem().toString());
                    break;
                case N_FILAS:
                    Filas_Serv.setNumQueues(Integer.valueOf(aValue.toString()));
                    break;   
                case 6:
                    if (NUMLINHAS == 7 || (NUMLINHAS == 8 && preempcao) || (NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                        Filas_Serv.setPreemptionTime(Double.valueOf(aValue.toString()));
                    }
                    if ((NUMLINHAS == 8 && falha) || (NUMLINHAS == 9 && capacidade)) {
                        Filas_Serv.setProbServerFailure(Double.valueOf(aValue.toString()));
                    }
                    break;
                case 7:
                    if ((NUMLINHAS == 8 && falha) || (NUMLINHAS == 9 && capacidade)){
                        Filas_Serv.setProbServiceFailure(Double.valueOf(aValue.toString()));
                    }
                    if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                        Filas_Serv.setProbServerFailure(Double.valueOf(aValue.toString()));
                    }
                    break;
                case 8:
                    if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                        Filas_Serv.setProbServiceFailure(Double.valueOf(aValue.toString()));
                    }
                    if((NUMLINHAS == 9 && capacidade))
                        Filas_Serv.setCapacity(Integer.valueOf(aValue.toString()));
                    break;
                case 9:
                    if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 10) {
                        Filas_Serv.setCapacity(Integer.valueOf(aValue.toString()));
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
                        return (Main.languageResource.getString("Service Time") + " " + "(" + ListasArmazenamento.listaCarregamento.get(Filas_Serv.getImgId()).getMetricas().get(1) + ")");
                    case N_FILAS:
                        return  ListasArmazenamento.listaCarregamento.get(Filas_Serv.getImgId()).getLabels().get(2);
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
                if (Filas_Serv != null) {
                    switch (rowIndex) {
                        case LABEL:
                            return Filas_Serv.getId().getNome();
                        case ORIGINATION:
                            return Filas_Serv.isOrigination();
                        case DESTINATION:
                            return Filas_Serv.isDestination();
                        case SERV:
                            return Filas_Serv.getServiceRate();
                        case SCHED:
                            return escalonador;
                        case N_FILAS:
                            return Filas_Serv.getNumQueues();
                        // Criação de elementos que variam de acordo com a configuração escolhida pelo usuário ao
                        // Criar o simulador
                        case 6:
                            if (NUMLINHAS == 7 || (NUMLINHAS == 8 && preempcao) || (NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                                return Filas_Serv.getPreemptionTime();
                            }
                            if ((NUMLINHAS == 8 && falha) || (NUMLINHAS == 9 && capacidade)) {
                                return Filas_Serv.getProbServerFailure();
                            }
                        case 7:
                            if ((NUMLINHAS == 8 && falha) || (NUMLINHAS == 9 && capacidade)){
                                return Filas_Serv.getProbServiceFailure();
                            }
                            if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                                return Filas_Serv.getProbServerFailure();
                            }
                        case 8:
                            if ((NUMLINHAS == 9 && preempcao) || NUMLINHAS == 10) {
                                return Filas_Serv.getProbServiceFailure();
                            }
                            if ((NUMLINHAS == 9  && capacidade)){
                                return Filas_Serv.getCapacity();
                            }
                        case 9:
                            if ((NUMLINHAS == 8 && preempcao) || NUMLINHAS == 10) {
                                return Filas_Serv.getCapacity();
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
