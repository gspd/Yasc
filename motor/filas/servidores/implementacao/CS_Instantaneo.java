package yasc.motor.filas.servidores.implementacao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import yasc.Main;
import yasc.arquivo.CParser.CParser;
import yasc.arquivo.CParser_form.CParserForm;
import yasc.arquivo.CParser_table.CParser_table;
import yasc.motor.EventoFuturo;
import yasc.motor.Simulacao;
import yasc.motor.filas.Mensagem;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Comunicacao;
import yasc.motor.filas.servidores.CentroServico;

public class CS_Instantaneo extends CS_Comunicacao implements CS_Vertice {

    private final List<CentroServico> conexoesEntrada;
    private final List<CentroServico> conexoesSaida;
    private final List<Tarefa> filaPacotes;
    private final List<Mensagem> filaMensagens;
    private final ArrayList<String> names;
    private ArrayList<String> values;
    private final String Trans_Func;
    Object resultado;
    private Object solucaoCircuito;
    private final boolean Origem;
    private final boolean Destino;
    public static boolean ori;
    public static boolean dest;
    public static ArrayList<String> id2 = new ArrayList<>();
    public static ArrayList<String> O  = new ArrayList<>();
    public static ArrayList<String> D  = new ArrayList<>();
    public static ArrayList<String> ancestral = new ArrayList<>();
    public static ArrayList<String> goTo = new ArrayList<>();
    public static ArrayList<String> InputColNames = new ArrayList<>();
    public static ArrayList<String> tarefaOrg = new ArrayList<>(); 
    public static ArrayList<String> passagemOrg = new ArrayList<>();
    public static ArrayList<String> elemAncestral = new ArrayList<>();
    public static ArrayList<String> elemAtual = new ArrayList<>();
    public static ArrayList<String> Pass = new ArrayList<>();
    public static ArrayList<CentroServico> ances = new ArrayList<>();
    public static ArrayList<CentroServico> agora = new ArrayList<>();
    public static List<CentroServico> tarefaMotor = new ArrayList<>();
    public static ArrayList<CentroServico> geral = new ArrayList<>();
    public static int numOrigens = 0; 
    public static int numDestinos = 0; 
    public static int t = 0;

    public CS_Instantaneo(String id, double LarguraBanda, double Ocupacao, double Latencia, String Trans_Func, ArrayList<String> Names, ArrayList<String> Values, boolean Origem, boolean Destino) {
        super(id, LarguraBanda, Ocupacao, Latencia);
        this.conexoesEntrada = new ArrayList<>();
        this.conexoesSaida = new ArrayList<>();
        this.filaPacotes = new ArrayList<>();
        this.filaMensagens = new ArrayList<>();
        this.Trans_Func = Trans_Func;
        this.names = Names;
        this.values = Values;
        this.Origem = Origem;
        this.Destino = Destino;
    }
    
    public static void setAllStaticNull() {
        ori = false;
        dest = false;
        id2 = new ArrayList<>();
        O  = new ArrayList<>();
        D  = new ArrayList<>();
        ancestral = new ArrayList<>();
        goTo = new ArrayList<>();
        InputColNames = new ArrayList<>();
        tarefaOrg = new ArrayList<>(); 
        passagemOrg = new ArrayList<>();
        elemAncestral = new ArrayList<>();
        elemAtual = new ArrayList<>();
        Pass = new ArrayList<>();
        ances = new ArrayList<>();
        agora = new ArrayList<>();
        tarefaMotor = new ArrayList<>();
        geral = new ArrayList<>();
        numOrigens = 0; 
        numDestinos = 0; 
        t = 0;
    }

    public boolean isDestino() {
        return Destino;
    }

    public Object getSolucaoCircuito() {
        return this.solucaoCircuito;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public List<CentroServico> getConexoesEntrada() {
        return conexoesEntrada;
    }

    @Override
    public void addConexoesEntrada(CentroServico conexoesEntrada) {
        this.conexoesEntrada.add(conexoesEntrada);
        ancestral.add(conexoesEntrada.getId().toString());
        ances.add(conexoesEntrada);
        if (!geral.contains(conexoesEntrada))
            geral.add(conexoesEntrada);
    }

    @Override
    public List<CentroServico> getConexoesSaida() {
        return conexoesSaida;
    }

    @Override
    public void addConexoesSaida(CentroServico conexoesSaida) {
        this.conexoesSaida.add(conexoesSaida);
        goTo.add(conexoesSaida.getId().toString());
        agora.add(conexoesSaida);
        if (!geral.contains(conexoesSaida))
            geral.add(conexoesSaida);
    }

    @Override
    public void chegadaDeCliente(Simulacao simulacao, Tarefa cliente) {

        if (Main.ordena == true && (Main.tipo.equals("logic") || Main.tipo.equals("table"))) {
            Ordena();
            Main.ordena = false;
        }
        if (cliente.isFalha_atendimento() && this.equals(cliente.getServFalha())) {
            if (cliente.isRecuperavel()) {
                Tarefa tar = criarCopia(simulacao, cliente);
                tar.setFalhaAtendimento(false);
                tar.setRecuperavel(false);
                EventoFuturo evtFut = new EventoFuturo(
                        simulacao.getTime(this) + cliente.getTimeout(),
                        EventoFuturo.CHEGADA,
                        tar.getOrigem(),
                        tar);
                simulacao.addEventoFuturo(evtFut);
                this.setNumTarefasPerdidas_Atend(1);
                this.setNumTarefasReenviadas(1);
            } else {
                this.setNumTarefasPerdidas_Atend(1);
            }
        } else {
            EventoFuturo novoEvt = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.ATENDIMENTO,
                    this,
                    cliente);
            simulacao.addEventoFuturo(novoEvt);
        }
    }

    @Override
    public void atendimento(Simulacao simulacao, Tarefa cliente) {
        cliente.iniciarAtendimentoComunicacao(simulacao.getTime(this));
        // Gera evento para atender cliente
        EventoFuturo evtFut = new EventoFuturo(
                simulacao.getTime(this),
                EventoFuturo.SAIDA,
                this, cliente);
        // Evento adicionado a lista de eventos futuros
        simulacao.addEventoFuturo(evtFut);
        InputStream is = new ByteArrayInputStream(Trans_Func.getBytes());
        // Criação do objeto
       if ("notLogic".equals(Main.tipo)) {
            CParser I = new CParser(is, names, values);
            try {
                resultado = CParser.InterpretaExp(Trans_Func, I);
            } catch (ScriptException ex) {
                Logger.getLogger(CS_Durativo.class.getName()).log(Level.SEVERE, null, ex);
            }
       } else {
            if ("logic".equals(Main.tipo)) {
                CParserForm I = new CParserForm(is, names, values);
                try {
                    ori = Origem;
                    dest = Destino;
                    solucaoCircuito = CParserForm.interpretaExp(Trans_Func, I);
                } catch (ScriptException ex) {
                    Logger.getLogger(CS_Durativo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if ("table".equals(Main.tipo)) {
                CParser_table I = new CParser_table(is, names, values);
                try {
                    ori = Origem;
                    dest = Destino;
                    resultado = CParser_table.interpretaExp(I);
                } catch(ScriptException ex){
                    Logger.getLogger(CS_Durativo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       }
    }

    @Override
    public void saidaDeCliente(Simulacao simulacao, Tarefa cliente) {
        cliente.finalizarAtendimentoComunicacao(simulacao.getTime(this));
        // Gera evento para chegada da tarefa no proximo servidor
        EventoFuturo evtFut;
        if (this.equals(cliente.getOrigem())) {
            List<CentroServico> cs = new ArrayList<>();
            if (this != cliente.getDestino()) {
                if (Main.tipo.equals("notLogic") || numOrigens == 1) {
                    cs = CS_Instantaneo.getMenorCaminho(cliente.getOrigem(), cliente.getDestino());
                } else {
                    cs = getTodoCaminho(cliente.getOrigem(), cliente.getDestino());
                }
                if (cs == null) {
                    throw new IllegalArgumentException("The model has no icons.");
                } else {
                    cliente.setCaminho(cs);
                    cliente.setLocalProcessamento(cliente.getDestino());
                }
            } else {
                cliente.setCaminho(cs);
                cliente.setLocalProcessamento(cliente.getDestino());
            }
        }
        if (!cliente.getCaminho().isEmpty()) {
            evtFut = new EventoFuturo(
                simulacao.getTime(this),
                EventoFuturo.CHEGADA,
                cliente.getCaminho().remove(0),
                cliente);
            simulacao.addEventoFuturo(evtFut);
        }
    }

    @Override
    public void requisicao(Simulacao simulacao, Mensagem cliente, int tipo) {
    }

    @Override
    public Integer getCargaTarefas() {
        return 0;
    }
   
    /*
    Função que analisa os objetos passados no modelo construido e ordena segundo 
    a seguimte caracteristica:
    
    - Objetos marcados somente como origem
    - Objetos sem marcação de origem ou destino
    - Objetos marcados somente como destino
    
    Tal forma de marcação define o inicio e fim da simulação. Assim que finalizada 
    a análise de todos os objetos mapeados pelo motor, a ordem dos objetos é passada
    para a execução no motor.
    */
    
    public static void Ordena() {
       
        int j = 0;
        
        tarefaOrg.clear();
        tarefaMotor.clear();
        
        if (id2.size() == 1) {
          tarefaOrg.add(id2.get(0)); 
        } else {
            /* Armazena primeiro os objetos que são somente Origem */
            for (int i = 0; i < O.size(); i++) {
                if (O.get(i).equals("o")) {
                    tarefaOrg.add(id2.get(i));
                    for (int k = 0; k < ancestral.size(); k++) {
                        if (ancestral.get(k).equals(tarefaOrg.get(j))) {
                            elemAncestral.add(id2.get(i));
                            elemAtual.add(goTo.get(k));
                            if (!Pass.isEmpty())
                                passagemOrg.add(Pass.get(k));
                            numOrigens++;
                        }
                    }
                    j++;
                }
            }
            /* Armazena os objetos de passagem */
            for (int i = 0; i < O.size(); i++) {
                if (O.get(i).equals("x") && D.get(i).equals("x")) {
                    tarefaOrg.add(id2.get(i));
                    for (int k = 0; k < ancestral.size(); k++) {
                        if (ancestral.get(k).equals(tarefaOrg.get(j))) {
                            elemAncestral.add(id2.get(i));
                            elemAtual.add(goTo.get(k));
                            if (!Pass.isEmpty())
                                passagemOrg.add(Pass.get(k));
                        }
                    }
                    j++;
                }
            }
            /* Armazena os objetos que são destino */
            for (int i = 0; i < D.size(); i++) {
                if (D.get(i).equals("d")) {
                    tarefaOrg.add(id2.get(i));
                    for (int k = 0; k < ancestral.size(); k++) {
                        if (goTo.get(k).equals(tarefaOrg.get(j))) {
                            elemAncestral.add(id2.get(i));
                            elemAtual.add(goTo.get(k));
                            if (!Pass.isEmpty())
                                passagemOrg.add(Pass.get(k));
                            numDestinos++;
                        }
                    }
                    j++;
                }
            }   
            
            CParserForm.IdObjFinal = tarefaOrg;
            
            boolean ja;
            String copia;
            // Ordenaçao para tarefaMotor
            for (int i = 0; i < tarefaOrg.size(); i++) {
                ja = false;
                for (int k = 0; k < geral.size(); k++) {
                    copia = geral.get(k).getId().toString();
                    if (ja == false) {
                        if (copia.equals(tarefaOrg.get(i))) {
                           tarefaMotor.add(geral.get(k));
                           ja = true;
                        }
                    }
                }
            }
        }
    }

}
