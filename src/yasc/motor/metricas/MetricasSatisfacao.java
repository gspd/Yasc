package yasc.motor.metricas;

import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Processamento;

public class MetricasSatisfacao {

    private final String usuario;
    private Double satisfacao;

    public MetricasSatisfacao(String usuario) {
        this.usuario = usuario;
    }

    public Double getSatisfacao(Metricas metricas) {
        return (this.satisfacao / metricas.getNumeroDeSimulacoes());
    }
    
    public Double getSatisfacao() {
        return this.satisfacao;
    }
    
    public void addSatisfacao(MetricasUsuarios metricasUsuarios) {
        Double suij = 0.0;
        for (Tarefa j : metricasUsuarios.getTarefasConcluidas(usuario)) {
            CS_Processamento maq = (CS_Processamento) j.getHistoricoProcessamento().get(0);
            suij += ((j.getTimeCriacao() + maq.tempoProcessar(j.getTamProcessamento())) / (j.getTempoFinal().get(j.getTempoFinal().size() - 1) - j.getTimeCriacao())) * (100);
        }
        this.satisfacao += suij/metricasUsuarios.getTarefasConcluidas(usuario).size();
    }
}
