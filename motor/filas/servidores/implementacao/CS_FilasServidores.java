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

public class CS_FilasServidores extends CS_Comunicacao implements CS_Vertice{

    private final List<CentroServico> conexoesEntrada;
    private final List<CentroServico> conexoesSaida;
    private final ArrayList<Tarefa>[] filaPacotes;
    private final List<Mensagem> filaMensagens;
    private final boolean recursoDisponivel;
    private final boolean linkDisponivelMensagem;
    private final double tempoTransmitirMensagem;
    private int numServ = 0;
    private final String Escalonador;
    private int numFilas = 0;
    private int cont_sjf = 0, cont_mjf = numFilas;
    private final int Capacidade_Fila;
    private final double Prob_Falha_Serv;
    private final double Prob_Falha_Clie;
    private final boolean Capacidade;
    private final boolean Origem;
    private final boolean Destino;
    
 

    public CS_FilasServidores(String id, double TxServico, double TempoPreempcao, double TxAtraso, String Escalonador, int Num_serv, int Num_filas, int Capacidade_Fila, double Prob_Falha_Serv, double Prob_Falha_Clie, boolean Capacidade, boolean Origem, boolean Destino) {
        super(id, TxServico, TempoPreempcao, TxAtraso);
        this.conexoesEntrada = new ArrayList<>();
        this.conexoesSaida = new ArrayList<>();
        this.recursoDisponivel = true;
        this.filaPacotes = new ArrayList[Num_filas];
        for (int i = 0; i < Num_filas; i++) {
            this.filaPacotes[i] = new ArrayList<>();
        }
        this.filaMensagens = new ArrayList<>();
        this.tempoTransmitirMensagem = 0;
        this.linkDisponivelMensagem = true;
        this.numServ = Num_serv;
        this.numFilas = Num_filas;
        this.Escalonador = Escalonador;
        this.setFalha(false);
        this.Capacidade_Fila = Capacidade_Fila;
        this.Prob_Falha_Clie = Prob_Falha_Clie;
        this.Prob_Falha_Serv = Prob_Falha_Serv;
        this.Capacidade = Capacidade;      
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
        gerarFalhasTarefa(1, 1, Prob_Falha_Clie, cliente);//shape = 1 (falhas constantes e independentes de outros eventos, scale = 1 (dispersar falhas ao longo da simulação)
        gerarFalhasServidor(1, 1, Prob_Falha_Serv);
        
        if (isFalha()) {//realiza falha de servidor
            Mensagem msg = new Mensagem(this, Mensagens.FALHAR, cliente);
            EventoFuturo evt = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.MENSAGEM,
                    this, msg);
            simulacao.addEventoFuturo(evt);
        } else if(cliente.isFalha_atendimento()){//realiza falha de atendimento
            if(cliente.isRecuperavel()){//com recuperação
                Tarefa tar = criarCopia(simulacao, cliente);
                tar.setFalhaAtendimento(false);
                EventoFuturo evtFut = new EventoFuturo(
                            simulacao.getTime(this) + cliente.getTimeout(),
                            EventoFuturo.CHEGADA,
                            tar.getOrigem(),
                            tar);
                this.setNumTarefasReenviadas(1);
                this.setNumTarefasPerdidas_Atend(1);
                simulacao.addEventoFuturo(evtFut);
                
            } else{//sem recuperação 
                this.setNumTarefasPerdidas_Atend(1);
            }
        } else if (this.numServ != 0) {
            //indica que recurso está ocupado
            this.numServ--;
            //cria evento para iniciar o atendimento imediatamente
            EventoFuturo novoEvt = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.ATENDIMENTO,
                    this,
                    cliente);
            simulacao.addEventoFuturo(novoEvt);
        } else {    
            Inserir(cliente);
        }
    }

    public void Inserir(Tarefa cliente) {//algoritmo de inserção dos clientes nas filas
        float lim = 0, lim1 = 10000 / numFilas;
        int marcador = 0;
        int i = 0;
        while (marcador != 1) {
            if (lim < cliente.getTamComunicacao() && cliente.getTamComunicacao() <= lim1) {
                if(Capacidade){
                    if(this.filaPacotes[i].size() < Capacidade_Fila){
                        this.filaPacotes[i].add(cliente);
                    }
                    else{
                           this.setNumTarefasPerdidas_Atend(1);
                    }
                }
                else{
                    this.filaPacotes[i].add(cliente);
                }
                marcador = 1;
            } else {
                lim = lim1;
                lim1 = lim1 * 2;
                if (i + 1 >= numFilas) {
                    i = 0;
                } else {
                    i++;
                }
            }
        }
        //System.out.println("Aqui");
    }

    @Override
    public void atendimento(Simulacao simulacao, Tarefa cliente) {
        cliente.finalizarEsperaComunicacao(simulacao.getTime(this));
        //contabiliza tempo de espera em fila
        this.setTempoFila(cliente.getTempoEsperaLocal());
        cliente.iniciarAtendimentoComunicacao(simulacao.getTime(this));
        //Gera evento para atender proximo cliente da lista
        EventoFuturo evtFut = new EventoFuturo(
                simulacao.getTime(this) + tempoTransmitir(cliente.getTamComunicacao()),
                EventoFuturo.SAIDA,
                this, cliente);
        //Event adicionado a lista de evntos futuros
        simulacao.addEventoFuturo(evtFut);


    }

    @Override
    public void saidaDeCliente(Simulacao simulacao, Tarefa cliente) {
        //Incrementa o número de Mbits transmitido por este link
        this.getMetrica().incMbitsTransmitidos(cliente.getTamComunicacao());
        //Incrementa o tempo de transmissão
        double tempoTrans = this.tempoTransmitir(cliente.getTamComunicacao());
        this.getMetrica().incSegundosDeTransmissao(tempoTrans);
        //Incrementa o tempo de transmissão no pacote
        cliente.finalizarAtendimentoComunicacao(simulacao.getTime(this));
        //contabiliza tempo de atendimento realizado pelo servidor
        this.setTempoAtend(cliente.getTempoAtendLocal());
        if (this == cliente.getOrigem()) {
            List<CentroServico> cs = new ArrayList<>();
            if(this != cliente.getDestino()){
                cs = CS_FilasServidores.getMenorCaminho(cliente.getOrigem(), cliente.getDestino());
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
        //Gera evento para chegada da tarefa no proximo servidor
        EventoFuturo evtFut;
        if (!cliente.getCaminho().isEmpty()) {
            evtFut = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.CHEGADA,
                    cliente.getCaminho().remove(0), cliente);
            //Event adicionado a lista de evntos futuros
            simulacao.addEventoFuturo(evtFut);
        }         
        int vazio = 0;
        for (int i = 0; i < numFilas; i++) {
            vazio = vazio + filaPacotes[i].size();
        }
        if (vazio == 0) {
            //Indica que está livre
            this.numServ++;
        } else {
            //Gera evento para atender proximo cliente da lista
            Tarefa proxCliente = Remover(Escalonador);
            evtFut = new EventoFuturo(
                    simulacao.getTime(this),
                    EventoFuturo.ATENDIMENTO,
                    this, proxCliente);
            //Event adicionado a lista de evntos futuros
            simulacao.addEventoFuturo(evtFut);
        }
    }

    public Tarefa Remover(String str) {// algoritmos de escalonamento que removem os clientes das filas e os colocam nos servidores
        Tarefa proxCliente = null;
        switch (str) {
            case "SJF":
                if (filaPacotes[cont_sjf].isEmpty()) {
                    while (filaPacotes[cont_sjf].isEmpty()) {
                        //System.out.println(cont_sjf);
                        if (cont_sjf + 1 >= numFilas) {
                            cont_sjf = 0;
                        } else {
                            cont_sjf++;
                        }
                    }
                }
                proxCliente = filaPacotes[cont_sjf].remove(0);
                if (cont_sjf + 1 >= numFilas) {
                    cont_sjf = 0;
                } else {
                    cont_sjf++;
                }
                break;
            case "MJF":
                if (filaPacotes[cont_mjf].isEmpty()) {
                    while (filaPacotes[cont_mjf].isEmpty()) {
                        if (cont_mjf - 1 < 0) {
                            cont_mjf = numFilas - 1;
                        } else {
                            cont_mjf--;
                        }
                    }
                }
                proxCliente = filaPacotes[cont_mjf].remove(0);
                if (cont_mjf - 1 < 0) {
                    cont_mjf = numFilas - 1;
                } else {
                    cont_mjf--;
                }
                break;
        }
        return proxCliente;
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
            int num = 0;
            for (int i = 0; i < numFilas; i++) {
                num = num + filaPacotes[i].size();
            }
            return (filaMensagens.size() + num + 1);
        }
    }
}