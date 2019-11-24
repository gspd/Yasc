package yasc.motor.metricas;

import java.io.Serializable;
import java.util.List;
import yasc.motor.filas.RedeDeFilas;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Comunicacao;
import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;

public class MetricasGlobais implements Serializable {

    private double tempoSimulacao;
    private double eficiencia;
    private int tarefasPerdidas;
    private int tarefasReenviadas;
    private int totalPerdido;
    private int totalTarefas;
    private int total;
    private double tempoFila;
    private double tempoAtendimento;
    private double tempoMFila;
    private double tempoMAtendimento;

    public MetricasGlobais(RedeDeFilas redeDeFilas, double tempoSimulacao, List<Tarefa> tarefas) {
        this.tempoSimulacao = tempoSimulacao;
        this.totalPerdido = addTarefasPerdidas(redeDeFilas.getCs());
        this.totalTarefas = addTotalTarefas(tarefas);
        addTempos(tarefas);
        this.eficiencia = getEficiencia(tarefas);
    }

    public MetricasGlobais() {
    }

    public int getTotalPerdido() {
        return totalPerdido;
    }

    public int getTotalTarefas() {
        return totalTarefas;
    }

    public int getTarefasPerdidas() {
        return tarefasPerdidas;
    }

    public int getTarefasReenviadas() {
        return tarefasReenviadas;
    }

    public double getEficiencia() {
        return eficiencia;
    }

    public double getTempoSimulacao() {
        return tempoSimulacao;
    }

    public double getTempo_MFila() {
        return tempoMFila;
    }

    public void setTempo_MFila(double Tempo_MFila) {
        this.tempoMFila = Tempo_MFila;
    }

    public double getTempo_MAtendimento() {
        return tempoMAtendimento;
    }

    public void setTempo_MAtendimento(double Tempo_MAtendimento) {
        this.tempoMAtendimento = Tempo_MAtendimento;
    }

    public double getTempo_Fila() {
        return tempoFila;
    }

    public void setTempo_Fila(double Tempo_Fila) {
        this.tempoFila = Tempo_Fila;
    }

    public double getTempo_Atendimento() {
        return tempoAtendimento;
    }

    public void setTempo_Atendimento(double Tempo_Atendimento) {
        this.tempoAtendimento = Tempo_Atendimento;
    }

    private double getEficiencia(List<Tarefa> tarefas) {
        double somaEfic = 0;
        for (Tarefa tar : tarefas) {
            somaEfic += tar.getMetricas().getEficiencia();
        }

        return somaEfic / tarefas.size();
    }

    public void setTotalPerdido(int TotalPerdido) {
        this.totalPerdido = TotalPerdido;
    }

    public void setTotalTarefas(int TotalTarefas) {
        this.totalTarefas = TotalTarefas;
    }

    public void setTarefasPerdidas(int TarefasPerdidas) {
        this.tarefasPerdidas = TarefasPerdidas;
    }

    public void setTarefasReenviadas(int TarefasReenviadas) {
        this.tarefasReenviadas = TarefasReenviadas;
    }

    public void setTempoSimulacao(double tempoSimulacao) {
        this.tempoSimulacao = tempoSimulacao;
    }

    public void setEficiencia(double eficiencia) {
        this.eficiencia = eficiencia;
    }

    public void add(MetricasGlobais global) {
        tempoSimulacao += global.getTempoSimulacao();
        tarefasPerdidas += global.getTarefasPerdidas();
        tarefasReenviadas += global.getTarefasReenviadas();
        total++;
    }

    private int addTarefasPerdidas(List<CentroServico> cs) {
        for (int i = 0; i < cs.size(); i++) {
            CS_Comunicacao aux = (CS_Comunicacao) cs.get(i);
            this.tarefasPerdidas = this.tarefasPerdidas + aux.getNumTarefasPerdidasServ() + aux.getNumTarefasPerdidas_Atend();
            this.tarefasReenviadas = this.tarefasReenviadas + aux.getNumTarefasReenviadas();
        }
        return this.tarefasPerdidas - this.tarefasReenviadas;
    }

    private void addTempos(List<Tarefa> tars) {
        for (int i = 0; i < tars.size(); i++) {
            this.tempoFila += tars.get(i).getMetricas().getTempoEsperaComu();
            this.tempoAtendimento += tars.get(i).getMetricas().getTempoComunicacao();
        }
        this.tempoMFila += this.tempoFila / tars.size();
        this.tempoMAtendimento += this.tempoAtendimento / tars.size();
    }

    private int addTotalTarefas(List<Tarefa> Tarefas) {
        return Tarefas.size();
    }

    @Override
    public String toString() {
        double ef = (totalPerdido * 100) / totalTarefas;
        int totalTemp = 1;
        if (total > 0) {
            totalTemp = total;
        }
        String texto = "\t\tSimulation Results\n\n";
        texto += String.format("\tTotal Simulated Time = %g \n", tempoSimulacao / totalTemp);
        texto += String.format("\tTotal of Lost Tasks = %d \n", totalPerdido);
        texto += String.format("\tTotal of Resubmitted Tasks = %d \n", tarefasReenviadas);
        texto += String.format("\tEfficiency = %.2f %%\n", ef);
        if (ef > 70.0) {
            texto += "\tEfficiency GOOD\n ";
        } else if (ef > 40.0) {
            texto += "\tEfficiency MEDIA\n ";
        } else {
            texto += "\tEfficiency BAD\n ";
        }
        return texto;
    }
}
