package yasc.motor.metricas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import yasc.Main;
import yasc.motor.filas.RedeDeFilas;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Comunicacao;
import yasc.motor.filas.servidores.CentroServico;

public class Metricas implements Serializable {

    private int numeroDeSimulacoes;
    private RedeDeFilas redeDeFilas;
    private List<Tarefa> tarefas;
    /**
     * Armazena métricas obtidas da simulação
     */
    private final MetricasGlobais metricasGlobais;
    private final List<String> usuarios;
    private Map<String, MetricasComunicacao> metricasComunicacao;
    private Map<String, MetricasProcessamento> metricasProcessamento;
    private Map<String, Double> metricasSatisfacao;
    private Map<String, Integer> tarefasConcluidas;
    private Map<Double, String> resultadoPorClock;
    private double tempoMedioFilaComunicacao;
    private double tempoMedioComunicacao;
    private double tempoMedioFilaProcessamento;
    private double tempoMedioProcessamento;
    private double MflopsDesperdicio;
    private int numTarefasCanceladas;
    private int numTarefas;
    private int numTarefasPerdidas;
    private int numTarefasReenviadas;
    private int totalPerdas;

    public Metricas(List<String> usuarios) {
        this.metricasGlobais = new MetricasGlobais();
        this.usuarios = usuarios;
    }

    public Metricas(RedeDeFilas redeDeFilas, double time, List<Tarefa> tarefas) {
        this.numeroDeSimulacoes = 1;
        this.metricasGlobais = new MetricasGlobais(redeDeFilas, time, tarefas);
        metricasSatisfacao = new HashMap<>();
        tarefasConcluidas = new HashMap<>();
        resultadoPorClock = new HashMap<>();
        this.usuarios = redeDeFilas.getUsuarios();
    }

    public Map<Double, String> getResultadoPorClock() {
        return resultadoPorClock;
    }

    public void setResultadoPorClock(Map<Double, String> resultadoPorClock) {
        this.resultadoPorClock = resultadoPorClock;
    }
    
    public void addTarefasPerdidas(List<CentroServico> cs) {
        for (int i = 0; i < cs.size(); i++) {
            CS_Comunicacao aux = (CS_Comunicacao) cs.get(i);
            this.numTarefasPerdidas = this.numTarefasPerdidas + aux.getNumTarefasPerdidasServ() + aux.getNumTarefasPerdidas_Atend();
            this.numTarefasReenviadas = this.numTarefasReenviadas + aux.getNumTarefasReenviadas();
        }
        this.totalPerdas = this.numTarefasPerdidas - this.numTarefasReenviadas;
    }

    public int getNumTarefasPerdidas() {
        return this.numTarefasPerdidas;
    }

    public int getNumTarefasReenviadas() {
        return numTarefasReenviadas;
    }

    public int getTotalPerdas() {
        return totalPerdas;
    }

    public void addMetrica(Metricas metrica) {
        addMetricasGlobais(metrica.getMetricasGlobais());
        addMetricaFilaTarefa(metrica);
        addMetricaComunicacao(metrica.getMetricasComunicacao());
        addMetricaProcessamento(metrica.getMetricasProcessamento());
        addMetricaSatisfacao(metrica.getMetricasSatisfacao(), metrica.tarefasConcluidas);
        this.numeroDeSimulacoes += metrica.numeroDeSimulacoes;
    }

    public RedeDeFilas getRedeDeFilas() {
        return redeDeFilas;
    }

    public void setRedeDeFilas(RedeDeFilas redeDeFilas) {
        this.redeDeFilas = redeDeFilas;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public int getNumeroDeSimulacoes() {
        return numeroDeSimulacoes;
    }

    public MetricasGlobais getMetricasGlobais() {
        return metricasGlobais;
    }

    public List<String> getUsuarios() {
        return usuarios;
    }

    public Map<String, MetricasComunicacao> getMetricasComunicacao() {
        return metricasComunicacao;
    }

    public Map<String, MetricasProcessamento> getMetricasProcessamento() {
        return metricasProcessamento;
    }

    public Map<String, Double> getMetricasSatisfacao() {
        return metricasSatisfacao;
    }

    /*public String getSaida(int i){
        String a = getValoresResultantes(i);
        return a;
    }*/
    public double getTempoMedioFilaComunicacao() {
        return tempoMedioFilaComunicacao;
    }

    public double getTempoMedioComunicacao() {
        return tempoMedioComunicacao;
    }

    public double getTempoMedioFilaProcessamento() {
        return tempoMedioFilaProcessamento;
    }

    public double getTempoMedioProcessamento() {
        return tempoMedioProcessamento;
    }

    public double getMflopsDesperdicio() {
        return MflopsDesperdicio;
    }

    public int getNumTarefasCanceladas() {
        return numTarefasCanceladas;
    }

    public int getNumTarefas() {
        return numTarefas;
    }

    public void calculaMedia() {
        // Média das Metricas Globais
        metricasGlobais.setTempoSimulacao(metricasGlobais.getTempoSimulacao() / numeroDeSimulacoes);
        metricasGlobais.setEficiencia(metricasGlobais.getEficiencia() / numeroDeSimulacoes);
        // Média das Metricas da rede de filas
        this.tempoMedioFilaComunicacao = this.tempoMedioFilaComunicacao / numeroDeSimulacoes;
        this.tempoMedioComunicacao = this.tempoMedioComunicacao / numeroDeSimulacoes;
        this.tempoMedioFilaProcessamento = this.tempoMedioFilaProcessamento / numeroDeSimulacoes;
        this.tempoMedioProcessamento = this.tempoMedioFilaProcessamento / numeroDeSimulacoes;
        this.MflopsDesperdicio = this.MflopsDesperdicio / numeroDeSimulacoes;
        this.numTarefasCanceladas = this.numTarefasCanceladas / numeroDeSimulacoes;
    }

    private void addMetricasGlobais(MetricasGlobais global) {
        metricasGlobais.setTempoSimulacao(metricasGlobais.getTempoSimulacao() + global.getTempoSimulacao());
        metricasGlobais.setEficiencia(metricasGlobais.getEficiencia() + global.getEficiencia());
        metricasGlobais.setTarefasPerdidas(metricasGlobais.getTarefasPerdidas() + global.getTarefasPerdidas());
        metricasGlobais.setTarefasReenviadas(metricasGlobais.getTarefasReenviadas() + global.getTarefasReenviadas());
    }

    // Olhar aqui
    private void addMetricaComunicacao(Map<String, MetricasComunicacao> metricasComunicacao) {
        if (numeroDeSimulacoes == 0) {
            this.metricasComunicacao = metricasComunicacao;
        } else {
            for (Map.Entry<String, MetricasComunicacao> entry : metricasComunicacao.entrySet()) {
                String key = entry.getKey();
                MetricasComunicacao item = entry.getValue();
                MetricasComunicacao base = this.metricasComunicacao.get(key);
                /* Incrementa metrica de Mbits transmitido, utilizando o getM... para a função de 
                incrementar.
                No caso de funções de transição adicionar a função que adiciona o valor da 
                saida no ArrayList */
                if ("logic".equals(Main.tipo)) {
                    base.addValoresResultantes(item.getValoresResultantes());
                } else {
                    base.incMbitsTransmitidos(item.getMbitsTransmitidos());
                }
                base.incSegundosDeTransmissao(item.getSegundosDeTransmissao());
            }
        }
    }

    private void addMetricaProcessamento(Map<String, MetricasProcessamento> metricasProcessamento) {
        if (numeroDeSimulacoes == 0) {
            this.metricasProcessamento = metricasProcessamento;
        } else {
            for (Map.Entry<String, MetricasProcessamento> entry : metricasProcessamento.entrySet()) {
                String key = entry.getKey();
                MetricasProcessamento item = entry.getValue();
                MetricasProcessamento base = this.metricasProcessamento.get(key);
                base.incMflopsProcessados(item.getMFlopsProcessados());
                base.incSegundosDeProcessamento(item.getSegundosDeProcessamento());
            }
        }
    }

    private void addMetricaFilaTarefa(Metricas metrica) {
        this.tempoMedioFilaComunicacao += metrica.tempoMedioFilaComunicacao;
        this.tempoMedioComunicacao += metrica.tempoMedioComunicacao;
    }

    private void addMetricaSatisfacao(Map<String, Double> metricasSatisfacao, Map<String, Integer> tarefasConcluidasUser) {
        if (numeroDeSimulacoes == 0) {
            this.metricasSatisfacao = metricasSatisfacao;
            this.tarefasConcluidas = tarefasConcluidasUser;
        } else {
            for (Map.Entry<String, Double> entry : this.metricasSatisfacao.entrySet()) {
                String string = entry.getKey();
                entry.setValue(entry.getValue() + metricasSatisfacao.get(string));
            }
        }
    }
}
