package yasc.motor;

import java.util.List;
import java.util.PriorityQueue;
import yasc.motor.filas.Cliente;
import yasc.motor.filas.Mensagem;
import yasc.motor.filas.RedeDeFilas;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CentroServico;

public class SimulacaoSequencial extends Simulacao {

    private double time = 0;
    private final PriorityQueue<EventoFuturo> eventos;
    
    public SimulacaoSequencial(ProgressoSimulacao janela, RedeDeFilas redeDeFilas, List<Tarefa> tarefas) throws IllegalArgumentException {
        super(janela, redeDeFilas, tarefas);
        this.time = 0;
        this.eventos = new PriorityQueue<>();
    }

    @Override
    public void simular() {
        addEventos(getTarefas());
        realizarSimulacao();
    }

    /** Adiciona chegada das tarefas na lista de eventos futuros. */
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

    /** Remove evento de saída do cliente do servidor. */
    @Override
    public boolean removeEventoFuturo(int tipoEv, CentroServico servidorEv, Cliente clienteEv) {
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
    
    /**
     * Recupera o próximo evento e o executa. Estes eventos são executados
     * de acordo com a sua ordem de chegada de forma a evitar a execução de
     * um evento, que seria criado anteriormente, antes do outro
     */
    private void realizarSimulacao() {
        while (!eventos.isEmpty()) {
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