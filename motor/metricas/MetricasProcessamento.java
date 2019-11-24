package yasc.motor.metricas;

import java.io.Serializable;

/**
 * Cada centro de serviço usado para processamento deve ter um objeto desta classe
 * Responsavel por armazenar o total de processamento realizado em MFlops e segundos
 */
public class MetricasProcessamento implements Serializable{
    /**
     * Armazena o total de processamento realizado em MFlops
     */
    private double MFlopsProcessados;
    /**
     * armazena o total de processamento realizado em segundos
     */
    private double SegundosDeProcessamento;
    private final String id;
    private final String proprietario;
    private final int numeroMaquina;
    
    public MetricasProcessamento(String id, int numeroMaquina, String proprietario) {
        this.id = id;
        this.numeroMaquina = numeroMaquina;
        this.proprietario = proprietario;
    }

    public void incMflopsProcessados(double MflopsProcessados) {
        this.MFlopsProcessados += MflopsProcessados;
    }

    public void incSegundosDeProcessamento(double SegundosProcessados) {
        this.SegundosDeProcessamento += SegundosProcessados;
    }

    public double getMFlopsProcessados() {
        return MFlopsProcessados;
    }

    public double getSegundosDeProcessamento() {
        return SegundosDeProcessamento;
    }

    public String getId() {
        return id;
    }

    public String getProprietario() {
        return proprietario;
    }

    public int getnumeroMaquina() {
        return numeroMaquina;
    }

    void setMflopsProcessados(double d) {
        this.MFlopsProcessados = d;
    }

    void setSegundosDeProcessamento(double d) {
        this.SegundosDeProcessamento = d;
    }
}