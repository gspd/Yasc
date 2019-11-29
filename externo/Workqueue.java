package yasc.externo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import yasc.escalonador.Escalonador;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;

/**
 * Implementação do algoritmo de escalonamento Workqueue
 * Atribui a proxima tarefa da fila (FIFO)
 * para um recurso que está livre
 */
public class Workqueue extends Escalonador {

    private final LinkedList<Tarefa> ultimaTarefaConcluida;
    private List<Tarefa> tarefaEnviada;

    public Workqueue() {
        this.tarefas = new ArrayList<>();
        this.escravos = new ArrayList<>();
        this.ultimaTarefaConcluida = new LinkedList<>();
    }

    @Override
    public void iniciar() {
        tarefaEnviada = new ArrayList<>(escravos.size());
        for (CS_Processamento escravo : escravos) {
            tarefaEnviada.add(null);
        }
    }

    @Override
    public Tarefa escalonarTarefa() {
        if (tarefas.size() > 0) {
            return tarefas.remove(0);
        }
        return null;
    }

    @Override
    public CS_Processamento escalonarRecurso() {
        if (!ultimaTarefaConcluida.isEmpty() && !ultimaTarefaConcluida.getLast().isCopy()) {
            int index = tarefaEnviada.indexOf(ultimaTarefaConcluida.getLast());
            return this.escravos.get(index);
        } else {
            for (int i = 0; i < tarefaEnviada.size(); i++) {
                if (tarefaEnviada.get(i) == null) {
                    return this.escravos.get(i);
                }
            }
        }
        return null;
    }

    @Override
    public List<CentroServico> escalonarRota(CentroServico destino) {
        int index = escravos.indexOf(destino);
        return new ArrayList<>((List<CentroServico>) caminhoEscravo.get(index));
    }

    @Override
    public void escalonar() {
        CS_Processamento rec = escalonarRecurso();
        if (rec != null) {
            Tarefa trf = escalonarTarefa();
            if (trf != null) {
                tarefaEnviada.set(escravos.indexOf(rec), trf);
                if(!ultimaTarefaConcluida.isEmpty()){
                    ultimaTarefaConcluida.removeLast();
                }
                trf.setLocalProcessamento(rec);
                trf.setCaminho(escalonarRota(rec));
                mestre.enviarTarefa(trf);
            }
        }
    }
    
    @Override
    public void adicionarTarefa(Tarefa tarefa){
        super.adicionarTarefa(tarefa);
        if(tarefaEnviada.contains(tarefa)){
            int index = tarefaEnviada.indexOf(tarefa);
            tarefaEnviada.set(index, null);
            mestre.executarEscalonamento();
        }
    }
    
    @Override
    public void addTarefaConcluida(Tarefa tarefa) {
        super.addTarefaConcluida(tarefa);
        ultimaTarefaConcluida.add(tarefa);
        if (!tarefas.isEmpty()) {
            mestre.executarEscalonamento();
        }
    }
}
