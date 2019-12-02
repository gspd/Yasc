package yasc.motor.carga;

import java.util.List;

import yasc.motor.filas.RedeDeFilas;
import yasc.motor.filas.Tarefa;

/**
 * Descreve forma de criar tarefas durante a simulação
 */
public abstract class GerarCarga {
    public static final int NULL = -1;
    public static final int RANDOM = 0;
    public static final int UNIFORM = 1;
    
    public abstract List<Tarefa> toTarefaList(RedeDeFilas rdf);

    @Override
    public abstract String toString();

    public abstract int getTipo();
}
