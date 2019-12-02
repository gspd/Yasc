package yasc.motor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import yasc.motor.filas.Cliente;
import yasc.motor.filas.Mensagem;
import yasc.motor.filas.RedeDeFilas;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;
import yasc.motor.filas.servidores.implementacao.CS_Mestre;

/**
 * Realiza o controle da simulação junto a uma interface gráfica,
 * permitindo acompanhar cada passo da simulação de forma interativa
 */
public class SimulacaoGrafica extends Simulacao {

    private double time;
    private double incremento;
    private boolean finalizar;
    private boolean parar;
    private final RedeDeFilas redeDeFilas;
    private final List<Tarefa> tarefas;
    private final PriorityQueue<EventoFuturo> eventos;
    private final JLabel tempo;

    public SimulacaoGrafica(ProgressoSimulacao janela, JLabel tempo, RedeDeFilas redeDeFilas, List<Tarefa> tarefas, double sleep) throws IllegalArgumentException {
        super(janela, redeDeFilas, tarefas);
        
        this.tempo = tempo;
        this.incremento = sleep;
        this.eventos = new PriorityQueue<>();
        this.redeDeFilas = redeDeFilas;
        this.tarefas = tarefas;
        
        if (redeDeFilas == null) {
            throw new IllegalArgumentException("The model has no icons.");
        } else if (redeDeFilas.getMestres() == null || redeDeFilas.getMestres().isEmpty()) {
            throw new IllegalArgumentException("The model has no Masters.");
        } else if (redeDeFilas.getLinks() == null || redeDeFilas.getLinks().isEmpty()) {
            janela.println("The model has no Networks.", Color.orange);
        }
        if (tarefas == null || tarefas.isEmpty()) {
            throw new IllegalArgumentException("One or more  workloads have not been configured.");
        }
    }

    @Override
    public void simular() {
        iniciarEscalonadores();
        // Adiciona chegada das tarefas na lista de eventos futuros
        addEventos(tarefas);
        
        if (atualizarEscalonadores()) {
            realizarSimulacaoAtualizaTime();
        } else {
            realizarSimulacao();
        }
        getJanela().incProgresso(35);
        getJanela().println("Simulation completed.", Color.green);
    }

    public void addEventos(List<Tarefa> tarefas) {
        for (Tarefa tarefa : tarefas) {
            EventoFuturo evt = new EventoFuturo(tarefa.getTimeCriacao(), EventoFuturo.CHEGADA, tarefa.getOrigem(), tarefa);
            eventos.add(evt);
        }
    }

    @Override
    public void addEventoFuturo(EventoFuturo ev) {
        eventos.offer(ev);
    }

    @Override
    public boolean removeEventoFuturo(int tipoEv, CentroServico servidorEv, Cliente clienteEv) {
        // Remover evento de saida do cliente do servidor
        java.util.Iterator<EventoFuturo> interator = this.eventos.iterator();
        while (interator.hasNext()) {
            EventoFuturo ev = interator.next();
            if (ev.getTipo() == tipoEv
                    && ev.getServidor().equals(servidorEv)
                    && ev.getCliente().equals(clienteEv)) {
                this.eventos.remove(ev);
                return true;
            }
        }
        return false;
    }

    @Override
    public double getTime(Object origem) {
        return time;
    }

    public void setFinalizar(boolean fim) {
        finalizar = fim;
    }

    public boolean isParar() {
        return parar;
    }

    public void setParar(boolean parar) {
        this.parar = parar;
    }

    public boolean isFinalizar() {
        return finalizar;
    }

    private boolean atualizarEscalonadores() {
        for (CS_Processamento mst : redeDeFilas.getMestres()) {
            CS_Mestre mestre = (CS_Mestre) mst;
            if (mestre.getEscalonador().getTempoAtualizar() != null) {
                return true;
            }
        }
        return false;
    }

    public void setIncremento(double inc) {
        incremento = inc;
    }

    private void realizarSimulacao() {
        double next = 0;
        while (!eventos.isEmpty() && !finalizar) {
            if (time > next) {
                try {
                    while (parar) {
                        Thread.sleep(50);
                    }
                    Thread.sleep(500);
                    next += incremento;
                    tempo.setText("Time: " + String.format("%.2f", time) + " s");
                } catch (InterruptedException ex) {
                    Logger.getLogger(SimulacaoGrafica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Recupera o próximo evento e o executa.
            // executa estes eventos de acordo com sua ordem de chegada
            // de forma a evitar a execução de um evento antes de outro
            // que seria criado anteriormente
            EventoFuturo eventoAtual = eventos.poll();
            time = eventoAtual.getTempoOcorrencia();
            switch (eventoAtual.getTipo()) {
                case EventoFuturo.CHEGADA:
                    eventoAtual.getServidor().chegadaDeCliente(this, (Tarefa) eventoAtual.getCliente());
                    break;
                case EventoFuturo.ATENDIMENTO:
                    eventoAtual.getServidor().atendimento(this, (Tarefa) eventoAtual.getCliente());
                    break;
                case EventoFuturo.SAIDA:
                    eventoAtual.getServidor().saidaDeCliente(this, (Tarefa) eventoAtual.getCliente());
                    break;
                case EventoFuturo.ESCALONAR:
                    eventoAtual.getServidor().requisicao(this, null, EventoFuturo.ESCALONAR);
                    break;
                default:
                    eventoAtual.getServidor().requisicao(this, (Mensagem) eventoAtual.getCliente(), eventoAtual.getTipo());
                    break;
            }
        }
    }

    /**
     * Executa o laço de repetição responsavel por atender todos eventos da
     * simulação, e adiciona o evento para atualizar os escalonadores.
     */
    private void realizarSimulacaoAtualizaTime() {
        List<Object[]> Arrayatualizar = new ArrayList<>();
        for (CS_Processamento mst : redeDeFilas.getMestres()) {
            CS_Mestre mestre = (CS_Mestre) mst;
            if (mestre.getEscalonador().getTempoAtualizar() != null) {
                Object[] item = new Object[3];
                item[0] = mestre;
                item[1] = mestre.getEscalonador().getTempoAtualizar();
                item[2] = mestre.getEscalonador().getTempoAtualizar();
                Arrayatualizar.add(item);
            }
        }
        double next = 0;
        while (!eventos.isEmpty()) {
            if (time > next) {
                try {
                    Thread.sleep(500);
                    next += incremento;
                    tempo.setText("Time: " + String.format("%.2f", time) + " s");
                } catch (InterruptedException ex) {
                    Logger.getLogger(SimulacaoGrafica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Recupera o próximo evento e o executa.
            // executa estes eventos de acordo com sua ordem de chegada
            // de forma a evitar a execução de um evento antes de outro
            // que seria criado anteriormente
            for (Object[] ob : Arrayatualizar) {
                if ((Double) ob[2] < eventos.peek().getTempoOcorrencia()) {
                    CS_Mestre mestre = (CS_Mestre) ob[0];
                    for (CS_Processamento maq : mestre.getEscalonador().getEscravos()) {
                        mestre.atualizar(maq, (Double) ob[2]);
                    }
                    ob[2] = (Double) ob[2] + (Double) ob[1];
                }
            }
            EventoFuturo eventoAtual = eventos.poll();
            time = eventoAtual.getTempoOcorrencia();
            switch (eventoAtual.getTipo()) {
                case EventoFuturo.CHEGADA:
                    eventoAtual.getServidor().chegadaDeCliente(this, (Tarefa) eventoAtual.getCliente());
                    break;
                case EventoFuturo.ATENDIMENTO:
                    eventoAtual.getServidor().atendimento(this, (Tarefa) eventoAtual.getCliente());
                    break;
                case EventoFuturo.SAIDA:
                    eventoAtual.getServidor().saidaDeCliente(this, (Tarefa) eventoAtual.getCliente());
                    break;
                case EventoFuturo.ESCALONAR:
                    eventoAtual.getServidor().requisicao(this, null, EventoFuturo.ESCALONAR);
                    break;
                default:
                    eventoAtual.getServidor().requisicao(this, (Mensagem) eventoAtual.getCliente(), eventoAtual.getTipo());
                    break;
            }
        }
    }
}