package yasc.motor.filas.servidores.implementacao;

import java.util.ArrayList;
import java.util.List;
import yasc.motor.EventoFuturo;
import yasc.motor.Mensagens;
import yasc.motor.Simulacao;
import yasc.motor.filas.Mensagem;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Comunicacao;
import yasc.motor.filas.servidores.CentroServico;

public class CS_FilaServidores extends CS_Comunicacao implements CS_Vertice {

    private final List<CentroServico> conexoesEntrada;
    private final List<CentroServico> conexoesSaida;
    private final List<Tarefa> filaPacotes;
    private final List<Mensagem> filaMensagens;
    private final boolean recursoDisponivel;
    private final boolean linkDisponivelMensagem;
    private final double tempoTransmitirMensagem;
    private int numServ = 0;
    private final String Escalonador;
    private final int capacidadeFila;
    private final double probFalhaServ;
    private final double probFalhaClie;
    private final boolean capacidade;
    private final boolean Origem;
    private final boolean Destino;

    public CS_FilaServidores(String id, double TxServico, double TempoPreempcao, double TxAtraso, String Escalonador, int Num_serv, int Capacidade_Fila, double Prob_Falha_Serv, double Prob_Falha_Clie, boolean Capacidade, boolean Origem, boolean Destino) {
        super(id, TxServico, TempoPreempcao, TxAtraso);
        this.conexoesEntrada = new ArrayList<>();
        this.conexoesSaida = new ArrayList<>();
        this.recursoDisponivel = true;
        this.filaPacotes = new ArrayList<>();
        this.filaMensagens = new ArrayList<>();
        this.tempoTransmitirMensagem = 0;
        this.linkDisponivelMensagem = true;
        this.numServ = Num_serv;
        this.Escalonador = Escalonador;
        this.setFalha(false);
        this.capacidadeFila = Capacidade_Fila;
        this.probFalhaClie = Prob_Falha_Clie;
        this.probFalhaServ = Prob_Falha_Serv;
        this.capacidade = Capacidade;
        this.Origem = Origem;
        this.Destino = Destino;
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
        cliente.iniciarEsperaComunicacao(simulacao.getTime(this));
        gerarFalhasTarefa(1, 1, probFalhaClie, cliente);//shape = 1 (falhas constantes e independentes de outros eventos, scale = 1 (dispersar falhas ao longo da simulação)
        gerarFalhasServidor(1, 1, probFalhaServ);
        
        if (isFalha()) {// Realiza falha de servidor
            Mensagem msg = new Mensagem(this, Mensagens.FALHAR, cliente);
            EventoFuturo evt = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.MENSAGEM,
                    this, msg);
            simulacao.addEventoFuturo(evt);
        } else if (cliente.isFalha_atendimento()) {// Realiza falha de prestação de serviço
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
                
            } else{// Sem recuperação 
                this.setNumTarefasPerdidas_Atend(1);
            }
        } else if (this.numServ != 0) {
            // Indica que recurso está ocupado
            this.numServ--;
            // Cria evento para iniciar o atendimento imediatamente
            EventoFuturo novoEvt = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.ATENDIMENTO,
                    this,
                    cliente);
            simulacao.addEventoFuturo(novoEvt);
        } else {// Inserção de clientes na fila de maneira organizada por algoritmos de escalonamento
            String fila = Escalonador;
            switch (fila) {
                case "FIFO":
                    Fifo(cliente);
                    break;
                case "LIFO":
                    Pilha(cliente);
                    break;
                case "SJF":
                    Sjf(cliente);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal scheduling algorithm"); 
            }
        }
    }

    public void Fifo(Tarefa cliente) {
        if (capacidade) {
            if (filaPacotes.size() < capacidadeFila) {
                filaPacotes.add(cliente);
            } else {
                this.setNumTarefasPerdidas_Atend(1);
            }
        } else {
            filaPacotes.add(cliente);
        }

    }

    public void Pilha(Tarefa cliente) {
        if (capacidade) {
            if (filaPacotes.size() < capacidadeFila) {
                filaPacotes.add(0, cliente);
            } else {
                this.setNumTarefasPerdidas_Atend(1);
            }
        } else {
            filaPacotes.add(0, cliente);
        }
    }

    public void Sjf(Tarefa cliente) {
        double tam;
        if (capacidade) {
            if (filaPacotes.size() < capacidadeFila) {
                if (!filaPacotes.isEmpty()) {
                    tam = filaPacotes.get(0).getTamComunicacao();
                    int i;
                    for (i = 0; tam < cliente.getTamComunicacao() && i < filaPacotes.size(); i++) {
                        tam = filaPacotes.get(i).getTamComunicacao();
                    }
                    filaPacotes.add(i, cliente);
                } else {
                    filaPacotes.add(cliente);
                }
            } else {
                this.setNumTarefasPerdidas_Atend(1);
            }
        } else {
            if (!filaPacotes.isEmpty()) {
                tam = filaPacotes.get(0).getTamComunicacao();
                int i;
                for (i = 0; tam < cliente.getTamComunicacao() && i < filaPacotes.size(); i++) {
                    tam = filaPacotes.get(i).getTamComunicacao();
                }
                filaPacotes.add(i, cliente);
            } else {
                filaPacotes.add(cliente);
            }
        }
    }

    @Override
    public void atendimento(Simulacao simulacao, Tarefa cliente) {

        cliente.finalizarEsperaComunicacao(simulacao.getTime(this));
        // Contabiliza tempo de espera em fila
        this.setTempoFila(cliente.getTempoEsperaLocal());
        cliente.iniciarAtendimentoComunicacao(simulacao.getTime(this));
        // Gera evento para atender proximo cliente da lista
        EventoFuturo evtFut = new EventoFuturo(
                simulacao.getTime(this) + tempoTransmitir(cliente.getTamComunicacao()),
                EventoFuturo.SAIDA,
                this, cliente);
        // Evento adicionado a lista de evntos futuros
        simulacao.addEventoFuturo(evtFut);


    }

    @Override
    public void saidaDeCliente(Simulacao simulacao, Tarefa cliente) {
        // Incrementa o número de Mbits transmitido por este link
        this.getMetrica().incMbitsTransmitidos(cliente.getTamComunicacao());
        // Incrementa o tempo de transmissão
        double tempoTrans = this.tempoTransmitir(cliente.getTamComunicacao());
        this.getMetrica().incSegundosDeTransmissao(tempoTrans);
        // Incrementa o tempo de transmissão no pacote
        cliente.finalizarAtendimentoComunicacao(simulacao.getTime(this));
        // Contabiliza tempo de atendimento realizado pelo servidor
        this.setTempoAtend(cliente.getTempoAtendLocal());
        if (this == cliente.getOrigem()) {// Acha o caminho a ser percorrido pelas tarefas
            List<CentroServico> cs = new ArrayList<>();
            if(this != cliente.getDestino()){
                cs = CS_FilaServidores.getMenorCaminho(cliente.getOrigem(), cliente.getDestino());
                if (cs == null) {
                    throw new IllegalArgumentException("No route found, please select a valid route.");
                } else {
                    cliente.setCaminho(cs);
                    cliente.setLocalProcessamento(cliente.getDestino());
                }
            }
            else{
                cliente.setCaminho(cs);
                cliente.setLocalProcessamento(cliente.getDestino());
            }
        }
        // Gera evento para chegada da tarefa no proximo servidor
        EventoFuturo evtFut;
        if (!cliente.getCaminho().isEmpty()) {
            evtFut = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.CHEGADA,
                    cliente.getCaminho().remove(0), cliente);
            // Evento adicionado a lista de evntos futuros
            simulacao.addEventoFuturo(evtFut);
        }
        if (filaPacotes.isEmpty()) {
            // Indica que está livre
            this.numServ++;
        } else {
            // Gera evento para atender proximo cliente da lista
            Tarefa proxCliente = filaPacotes.remove(0);
            evtFut = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.ATENDIMENTO,
                    this, proxCliente);
            // Evento adicionado a lista de evntos futuros
            simulacao.addEventoFuturo(evtFut);
        }
    }

    public void atenderFalha(CS_Comunicacao simulacao, Mensagem cliente) {
        simulacao.setNumTarefasPerdidas(1);
    }

    @Override
    public void requisicao(Simulacao simulacao, Mensagem cliente, int tipo) {
        if (cliente.getTipo() == Mensagens.FALHAR) {
            atenderFalha(this, cliente);
        }
    }

    @Override
    public Integer getCargaTarefas() {
        if (this.numServ == numServ && linkDisponivelMensagem) {
            return 0;
        } else {
            return (filaMensagens.size() + filaPacotes.size()) + 1;
        }
    }
}