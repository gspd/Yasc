package yasc.escalonador;

import java.util.List;

import yasc.motor.filas.Mensagem;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;
import yasc.motor.metricas.MetricasUsuarios;

public abstract class Escalonador {
    // Atributos
    protected List<CS_Processamento> escravos;
    protected List<List> filaEscravo;
    protected List<Tarefa> tarefas;
    protected MetricasUsuarios metricaUsuarios;
    protected Mestre mestre;
    /**
     * Armazena os caminhos possiveis para alcançar cada escravo
     */
    protected List<List> caminhoEscravo;

    public abstract void iniciar();

    public abstract Tarefa escalonarTarefa();

    public abstract CS_Processamento escalonarRecurso();

    public abstract List<CentroServico> escalonarRota(CentroServico destino);

    public abstract void escalonar();

    public void adicionarTarefa(Tarefa tarefa){
        if (tarefa.getOrigem().equals(mestre)) {
            this.metricaUsuarios.incTarefasSubmetidas(tarefa);
        }
        this.tarefas.add(tarefa);
    }

    public List<CS_Processamento> getEscravos() {
        return escravos;
    }

    public void setCaminhoEscravo(List<List> caminhoEscravo) {
        this.caminhoEscravo = caminhoEscravo;
    }

    public void addEscravo(CS_Processamento maquina) {
        this.escravos.add(maquina);
    }
    
    public void addTarefaConcluida(Tarefa tarefa) {
        if(tarefa.getOrigem().equals(mestre)){
            this.metricaUsuarios.incTarefasConcluidas(tarefa);
        }
    }
    
    public List<Tarefa> getFilaTarefas() {
        return this.tarefas;
    }

    public MetricasUsuarios getMetricaUsuarios() {
        return metricaUsuarios;
    }

    public void setMetricaUsuarios(MetricasUsuarios metricaUsuarios) {
        this.metricaUsuarios = metricaUsuarios;
    }

    public void setMestre(Mestre mestre) {
        this.mestre = mestre;
    }

    public List<List> getCaminhoEscravo() {
        return caminhoEscravo;
    }
    
    /**
     * Indica o intervalo de tempo utilizado pelo escalonador para realizar atualização dos dados dos escravos
     * Retornar null para escalonadores estáticos, nos dinâmicos o método deve ser reescrito
     * @return Intervalo em segundos para atualização
     */
    public Double getTempoAtualizar(){
        return null;
    }

    public void resultadoAtualizar(Mensagem mensagem) {
        int index = escravos.indexOf(mensagem.getOrigem());
        filaEscravo.set(index, mensagem.getFilaEscravo());
    }
}
