package yasc.externo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import yasc.escalonador.Escalonador;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;

/**
 * Implementação do algoritmo de escalonamento Round-Robin
 * Atribui a proxima tarefa da fila (FIFO)
 * para o proximo recurso de uma fila circular de recursos
 */
public class RoundRobin extends Escalonador{
    private ListIterator<CS_Processamento> recursos;
    
    public RoundRobin(){
        this.tarefas = new ArrayList<>();
        this.escravos = new LinkedList<>();
    }

    @Override
    public void iniciar() {
        recursos = escravos.listIterator(0);
    }

    @Override
    public Tarefa escalonarTarefa() {
        return tarefas.remove(0);
    }

    @Override
    public CS_Processamento escalonarRecurso() {
        if (recursos.hasNext()) {
            return recursos.next();
        }else{
            recursos = escravos.listIterator(0);
            return recursos.next();
        }
    }

    @Override
    public void escalonar() {
        Tarefa trf = escalonarTarefa();
        CS_Processamento rec = escalonarRecurso();
        trf.setLocalProcessamento(rec);
        trf.setCaminho(escalonarRota(rec));
        mestre.enviarTarefa(trf);
    }

    @Override
    public List<CentroServico> escalonarRota(CentroServico destino) {
        int index = escravos.indexOf(destino);
        return new ArrayList<>((List<CentroServico>) caminhoEscravo.get(index));
    }
}
