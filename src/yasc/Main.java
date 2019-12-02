package yasc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import yasc.gui.JPrincipal;
import yasc.gui.entrada.JRecepcao;
import yasc.gui.entrada.ListasArmazenamento;
import yasc.gui.entrada.Parser;

public class Main {

    private Parser tableCarregar;
    public static ResourceBundle languageResource;
    public static String tipo;
    public static boolean ordena = true;
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | 
                InstantiationException | 
                IllegalAccessException | 
                UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        setLanguage();
        
        if (Paths.get("simulatorlibrary").toFile().exists()) {
            String separator = System.getProperty("file.separator");  
            main.leituraArquivo("simulatorlibrary" + separator + "simulador.conf");
            
            JPrincipal gui = new JPrincipal();
            gui.setLocationRelativeTo(null);
            gui.setVisible(true);
        } else {
            java.awt.EventQueue.invokeLater(() -> {
                new JRecepcao().setVisible(true);
            });
        }
    }
    
    // Set default  language 
    public static void setLanguage() {
        if ("pt_BR".equals(Locale.getDefault().toString()))
            languageResource = ResourceBundle.getBundle("yasc.idioma.Idioma_pt_BR");
        else
            languageResource = ResourceBundle.getBundle("yasc.idioma.Idioma_en_US");
    }
    
    // Read the file configuration
    private void leituraArquivo(String arq) {
        
        try (FileReader arquivo = new FileReader(arq)) {
            BufferedReader lerArq = new BufferedReader(arquivo);
            String linha = lerArq.readLine(); // Lê a primeira linha
            // A variável "linha" recebe o valor "null" quando o processo de repetição atingir o final do arquivo texto
            while (linha != null) {
                String[] b = linha.split(" ");

                switch (b[0]) {
                    case "1":
                        tableCarregar = new Parser();
                        tableCarregar.setId(b[1]);
                        tableCarregar.setGraphicRepresentation(b[2]);
                        tableCarregar.setFila(true);
                        tableCarregar.setFilaTipo(b[3]);
                        switch(b[3]){
                            case "One_queue_one_server":
                                for(int j = 0; j < 2; j++){
                                    linha = lerArq.readLine();
                                    String[] c = linha.split(" ");
                                    if (c[0].equals("a")) {
                                        tableCarregar.getLabels().add(c[1]);
                                        tableCarregar.getMetricas().add(c[2]);
                                        if(j == 1){
                                            if("NonDeterministic".equals(c[3]))
                                                tableCarregar.setNDeterministico(true);
                                        }
                                    }
                                }
                                break;
                            case "Multiple_queues_one_server":
                                for(int j = 0; j < 3; j++){
                                    linha = lerArq.readLine();
                                    String[] c = linha.split(" ");
                                    if (c[0].equals("a")) {
                                        tableCarregar.getLabels().add(c[1]);
                                        tableCarregar.getMetricas().add(c[2]);
                                        if(j == 1){
                                            if("NonDeterministic".equals(c[3]))
                                                tableCarregar.setNDeterministico(true);
                                        }
                                    }
                                }
                                break;
                            case "Multiple_queues_multiple_servers":
                                for(int j = 0; j < 4; j++){
                                    linha = lerArq.readLine();
                                    String[] c = linha.split(" ");
                                    if (c[0].equals("a")) {
                                        tableCarregar.getLabels().add(c[1]);
                                        tableCarregar.getMetricas().add(c[2]);
                                        if(j == 1){
                                            if("NonDeterministic".equals(c[3]))
                                                tableCarregar.setNDeterministico(true);
                                        }
                                    }
                                }
                                break;
                            case "One_queue_multiple_servers":
                                for(int j = 0; j < 3; j++){
                                    linha = lerArq.readLine();
                                    String[] c = linha.split(" ");
                                    if (c[0].equals("a")) {
                                        tableCarregar.getLabels().add(c[1]);
                                        tableCarregar.getMetricas().add(c[2]);
                                        if(j == 1){
                                            if("NonDeterministic".equals(c[3]))
                                                tableCarregar.setNDeterministico(true);
                                        }
                                    }
                                }
                                break;
                            case "Infinity_Server":
                                for(int j = 0; j < 2; j++){
                                    linha = lerArq.readLine();
                                    String[] c = linha.split(" ");
                                    if (c[0].equals("a")) {
                                        tableCarregar.getLabels().add(c[1]);
                                        tableCarregar.getMetricas().add(c[2]);
                                        if(j == 1){
                                            if("NonDeterministic".equals(c[3]))
                                                tableCarregar.setNDeterministico(true);
                                        }
                                    }
                                }
                                break;
                        }
                        ListasArmazenamento.listaCarregamento.add(tableCarregar);
                        break;
                    case "2":
                        tableCarregar = new Parser();
                        tableCarregar.setId(b[1]);
                        tableCarregar.setGraphicRepresentation(b[2]);
                        tableCarregar.setDurativo(true);
                        tableCarregar.setFuncTrans(true);
                        tableCarregar.setFuncaoTrans(b[3]);
                        for (int k = 0; k < Integer.parseInt(b[4]); k++) {
                            linha = lerArq.readLine();
                            String[] c = linha.split(" ");
                            if (c[0].equals("b")) {
                                if (c.length > 3) {
                                    tableCarregar.setTipos(c[1]+ " " + c[2]);
                                    tableCarregar.setVars(c[3]);
                                } else {
                                    tableCarregar.setTipos(c[1]);
                                    tableCarregar.setVars(c[2]);
                                }
                            }
                        }
                        ListasArmazenamento.listaCarregamento.add(tableCarregar);
                        break;
                    case "3":
                        switch(b[1]) {
                            /*Obs: O ELSE trabalha com a abertura de arquivos cujos objetos sejam caracterizados
                            a partir da tabela. O programa não admite a abertura de bibliotecas que sejam formados
                            por objetos caracterizados por fórmulas e tabelas. Ou seja, a biblioteca deve ser composta
                            por objetos caracterizados somente por fórmula ou somente por tabela.*/
                            case "y": //Caso em que logicOperation é setado como sim
                                tableCarregar = new Parser("", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, false, false, false, false, "", "", "", "", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                                tableCarregar.setLogicOperation(b[1]);
                                tableCarregar.setId(b[2]);
                                tableCarregar.setGraphicRepresentation(b[3]);
                                tableCarregar.setInstantaneo(true);
                                tableCarregar.setFuncaoTrans(b[4]);
                                tipo = "logic";

                                for (int k = 0; k < Integer.parseInt(b[5]); k++) {
                                    linha = lerArq.readLine();
                                    String[] c = linha.split(" ");
                                    if (c[0].equals("b")) {
                                        if (c.length > 3) {
                                            tableCarregar.setTipos(c[1]+ " " + c[2]);
                                            tableCarregar.setVars(c[3]);
                                        } else {
                                            tableCarregar.setTipos(c[1]);
                                            tableCarregar.setVars(c[2]);
                                        }
                                    }
                                }

                                ListasArmazenamento.listaCarregamento.add(tableCarregar);
                                break;
                            case  "n"://Caso do uso de fórmula em que logicOperation é setado como não
                                tableCarregar = new Parser("", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, false, false, false, false, "", "", "", "", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                                tableCarregar.setLogicOperation(b[1]);
                                tableCarregar.setId(b[2]);
                                tableCarregar.setGraphicRepresentation(b[3]);
                                tableCarregar.setInstantaneo(true);
                                tableCarregar.setFuncaoTrans(b[4]);
                                
                                for (int k = 0; k < Integer.parseInt(b[5]); k++) {
                                    linha = lerArq.readLine();
                                    String[] c = linha.split(" ");
                                    if (c[0].equals("b")) {
                                        if (c.length > 3) {
                                            tableCarregar.setTipos(c[1]+ " " + c[2]);
                                            tableCarregar.setVars(c[3]);
                                        } else {
                                            tableCarregar.setTipos(c[1]);
                                            tableCarregar.setVars(c[2]);
                                        }
                                    }
                                }
                                ListasArmazenamento.listaCarregamento.add(tableCarregar);
                                tipo = "notLogic";

                                break;
                            default://Caso da tabela
                                tableCarregar = new Parser("", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, false, false, false, false, "", "", "", "", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                                tableCarregar.setId(b[1]);
                                tableCarregar.setGraphicRepresentation(b[2]);
                                tableCarregar.setInstantaneo(true);
                                tableCarregar.setInputTrans(b[3]);
                                tableCarregar.setOutputTrans(b[4]);
                                for (int k = 0; k < Integer.parseInt(b[5]); k++) {
                                    linha = lerArq.readLine();
                                    String[] c = linha.split(" ");
                                    if (c[0].equals("b")) {
                                        if(c.length > 3){
                                            tableCarregar.setTipos(c[1]+ " " + c[2]);
                                            tableCarregar.setVars(c[3]);
                                        }
                                        else{
                                            tableCarregar.setTipos(c[1]);
                                            tableCarregar.setVars(c[2]);
                                        }
                                    }
                                }
                                ListasArmazenamento.listaCarregamento.add(tableCarregar);
                                tipo = "table";

                                break;
                        }
                    case "4":
                        ListasArmazenamento.setFalha(true);
                        break;
                    case "5":
                        ListasArmazenamento.setFalhaCapacidade(true);
                        break;
                    case "6":
                        ListasArmazenamento.setEvent(b[1]);
                        break;
                    case "7":
                        switch(b[1]){
                            case "Loss":
                                ListasArmazenamento.setPerda(true);
                                break;
                            case "Service":
                                ListasArmazenamento.setAtendimento(true);
                                ListasArmazenamento.setAtendimentoMet(b[2]);
                                break;
                            case "Queue":
                                ListasArmazenamento.setFila(true);
                                ListasArmazenamento.setFilaMet(b[2]);
                                break;
                        }
                }
                linha = lerArq.readLine();
            } // lê da segunda até a última linha 
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
    }
}