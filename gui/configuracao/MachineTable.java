package yasc.gui.configuracao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import yasc.Main;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import yasc.arquivo.Escalonadores;
import yasc.gui.iconico.grade.ItemGrade;
import yasc.gui.iconico.grade.Machine;

public class MachineTable extends AbstractTableModel {

    // Constantes representando o índice das colunas
    private static final int TYPE = 0;
    private static final int VALUE = 1;
    private static final int LABEL = 0;
    private static final int OWNER = 1;
    private static final int PROCS = 2;
    private static final int LOADF = 3;
    private static final int CORES = 4;
    private static final int MERAM = 5;
    private static final int HDISK = 6;
    private static final int MASTR = 7;
    private static final int SCHED = 8;
    private static final int SLAVE = 9;
    private static final int NUMLINHAS = 10;
    private static final int NUMCOLUNAS = 2;
    // Array com os nomes das linhas
    private Machine maquina;
    private JButton escravos;
    private final JComboBox escalonador;
    private final JComboBox usuarios;
    private JList selecionadorEscravos;

    public MachineTable() {
        selecionadorEscravos = new JList();
        escravos = new JButton();
        escravos.addActionListener((java.awt.event.ActionEvent evt) -> {
            // Cria lista com nós escalonaveis
            if (!selecionadorEscravos.isVisible()) {
                DefaultListModel listModel = new DefaultListModel();
                List<ItemGrade> listaConectados = maquina.getNosEscalonaveis();
                for (ItemGrade item : listaConectados) {
                    listModel.addElement(item);
                }
                selecionadorEscravos.setModel(listModel);
                for (ItemGrade escravo : maquina.getEscravos()) {
                    int index = listaConectados.indexOf(escravo);
                    selecionadorEscravos.addSelectionInterval(index, index);
                }
                selecionadorEscravos.setVisible(true);
            }
            if (selecionadorEscravos.getModel().getSize() > 0) {
                int opcao = JOptionPane.showConfirmDialog(
                        escravos,
                        selecionadorEscravos,
                        "Select the slaves",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                if (opcao == JOptionPane.OK_OPTION) {
                    List<ItemGrade> escravosList = new ArrayList<>(selecionadorEscravos.getSelectedValuesList());
                    maquina.setEscravos(escravosList);
                    escravos.setText(maquina.getEscravos().toString());
                }
            }
        });
        escalonador = new JComboBox(Escalonadores.ESCALONADORES);
        usuarios = new JComboBox();
    }

    public void setMaquina(Machine maquina, HashSet users) {
        this.maquina = maquina;
        this.escalonador.setSelectedItem(this.maquina.getAlgoritmo());
        this.usuarios.removeAllItems();
        for (Object object : users) {
            this.usuarios.addItem(object);
        }
        this.usuarios.setSelectedItem(maquina.getProprietario());
        this.selecionadorEscravos.setVisible(false);
        escravos.setText(maquina.getEscravos().toString());
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

    public JComboBox getEscalonadores() {
        return escalonador;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // Pega o sócio referente a linha especificada.
        if (columnIndex == VALUE && maquina != null) {
            switch (rowIndex) {
                case LABEL:
                    maquina.getId().setNome(aValue.toString());
                    break;
                case OWNER:
                    maquina.setProprietario(usuarios.getSelectedItem().toString());
                    break;
                case PROCS:
                    maquina.setPoderComputacional(Double.valueOf(aValue.toString()));
                    break;
                case LOADF:
                    maquina.setTaxaOcupacao(Double.valueOf(aValue.toString()));
                    break;
                case MERAM:
                    maquina.setMemoriaRAM(Double.valueOf(aValue.toString()));
                    break;
                case HDISK:
                    maquina.setDiscoRigido(Double.valueOf(aValue.toString()));
                    break;
                case CORES:
                    maquina.setNucleosProcessador(Integer.valueOf(aValue.toString()));
                    break;
                case MASTR:
                    maquina.setMestre(Boolean.valueOf(aValue.toString()));
                    break;
                case SCHED:
                    maquina.setAlgoritmo(escalonador.getSelectedItem().toString());
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
                    case OWNER:
                        return Main.languageResource.getString("Owner");
                    case PROCS:
                        return Main.languageResource.getString("Computing power") + " (Mflop/s)";
                    case LOADF:
                        return Main.languageResource.getString("Load Factor");
                    case MERAM:
                        return "Primary Storage";
                    case HDISK:
                        return "Secondary Storage";
                    case CORES:
                        return "Cores";
                    case MASTR:
                        return Main.languageResource.getString("Master");
                    case SCHED:
                        return Main.languageResource.getString("Scheduling algorithm");
                    case SLAVE:
                        return "Slave Nodes";
                }
            case VALUE:
                if (maquina != null) {
                    switch (rowIndex) {
                        case LABEL:
                            return maquina.getId().getNome();
                        case OWNER:
                            return usuarios;
                        case PROCS:
                            return maquina.getPoderComputacional();
                        case LOADF:
                            return maquina.getTaxaOcupacao();
                        case MERAM:
                            return maquina.getMemoriaRAM();
                        case HDISK:
                            return maquina.getDiscoRigido();
                        case CORES:
                            return maquina.getNucleosProcessador();
                        case MASTR:
                            return maquina.isMestre();
                        case SCHED:
                            return escalonador;
                        case SLAVE:
                            return escravos;
                    }
                } else {
                    switch (rowIndex) {
                        case OWNER:
                            return usuarios;
                        case SCHED:
                            return escalonador;
                        case SLAVE:
                            return escravos;
                        default:
                            return "null";
                    }
                }
            default:
                // Não deve ocorrer, pois só existem 2 colunas
                throw new IndexOutOfBoundsException("ColumnIndex out of bounds");
        }
    }
}
