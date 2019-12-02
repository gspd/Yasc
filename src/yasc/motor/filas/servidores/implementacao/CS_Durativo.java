package yasc.motor.filas.servidores.implementacao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import yasc.arquivo.CParser.CParser;
import yasc.motor.EventoFuturo;
import yasc.motor.Simulacao;
import yasc.motor.filas.Mensagem;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Comunicacao;
import yasc.motor.filas.servidores.CentroServico;

public class CS_Durativo extends CS_Comunicacao implements CS_Vertice {

    private final List<CentroServico> conexoesEntrada;
    private final List<CentroServico> conexoesSaida;
    private final String transFunc;
    private final double tempoTransf;
    private final List<Tarefa> filaPacotes;
    private final List<Mensagem> filaMensagens;
    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> Values = new ArrayList<>();
    private Object resultado;
    private final boolean origem;
    private final boolean destino;

    public CS_Durativo(String id, double larguraBanda, double ocupacao, double latencia, String funcTrans, double tempoTransf, ArrayList<String> names, ArrayList<String> values, boolean origem, boolean destino) {
        super(id, larguraBanda, ocupacao, latencia);
        this.conexoesEntrada = new ArrayList<>();
        this.conexoesSaida = new ArrayList<>();
        this.filaPacotes = new ArrayList<>();
        this.filaMensagens = new ArrayList<>();
        this.tempoTransf = tempoTransf;
        this.transFunc = funcTrans;
        this.Names = names;
        this.Values = values;
        this.origem = origem;
        this.destino = destino;
    }

    public List<CentroServico> getConexoesEntrada() {
        return conexoesEntrada;
    }

    @Override
    public void addConexoesEntrada(CentroServico conexoesEntrada) {
        this.conexoesEntrada.add(conexoesEntrada);
    }

    @Override
    public List<CentroServico> getConexoesSaida() {
        return conexoesSaida;
    }

    @Override
    public void addConexoesSaida(CentroServico conexoesSaida) {
        this.conexoesSaida.add(conexoesSaida);
    }

    @Override
    public void chegadaDeCliente(Simulacao simulacao, Tarefa cliente) {
        // Cria evento para iniciar o atendimento imediatamente
        if (cliente.isFalha_atendimento() && this.equals(cliente.getServFalha())) {// Realiza falha de atendimento
            if (cliente.isRecuperavel()) {// Com recuperação
                Tarefa tar = criarCopia(simulacao, cliente);
                tar.setFalhaAtendimento(false);
                EventoFuturo evtFut = new EventoFuturo(
                        simulacao.getTime(this) + cliente.getTimeout(),
                        EventoFuturo.CHEGADA,
                        tar.getOrigem(),
                        tar);
                this.setNumTarefasPerdidas_Atend(1);
                this.setNumTarefasReenviadas(1);
                simulacao.addEventoFuturo(evtFut);
            } else {// Sem recuperação 
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
                simulacao.getTime(this) + tempoTransf,
                EventoFuturo.SAIDA,
                this, cliente);
        // Evento adicionado a lista de eventos futuros
        simulacao.addEventoFuturo(evtFut);
    }

    @Override
    public void saidaDeCliente(Simulacao simulacao, Tarefa cliente) {
        this.getMetrica().incSegundosDeTransmissao(tempoTransf);
        // Incrementa o tempo de transmissão no pacote
        cliente.finalizarAtendimentoComunicacao(simulacao.getTime(this));
        // Gera evento para chegada da tarefa no proximo servidor
        EventoFuturo evtFut;
        if (this == cliente.getOrigem()) {
            List<CentroServico> cs = new ArrayList<>();
            if (this != cliente.getDestino()) {
                cs = CS_Durativo.getMenorCaminho(cliente.getOrigem(), cliente.getDestino());
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
        // Evento adicionado a lista de eventos futuros
    }

    @Override
    public void requisicao(Simulacao simulacao, Mensagem cliente, int tipo) {
        
    }

    @Override
    public Integer getCargaTarefas() {
        return 0;
    }
}
