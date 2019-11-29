package yasc.motor.filas;

import java.util.List;

import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;

public class Mensagem implements Cliente {
    
    private final int tipo;
    private Tarefa tarefa;
    private final CentroServico origem;
    private List<CentroServico> caminho;
    private final double tamComunicacao;
    private List<Tarefa> filaEscravo;
    private List<Tarefa> processadorEscravo;

    public Mensagem(CS_Processamento origem, int tipo) {
        this.origem = origem;
        this.tipo = tipo;
        this.tamComunicacao = 0.011444091796875;
    }
    
    public Mensagem(CS_Processamento origem, int tipo, Tarefa tarefa) {
        this.origem = origem;
        this.tipo = tipo;
        this.tamComunicacao = 0.011444091796875;
        this.tarefa = tarefa;
    }
    
    public Mensagem(CentroServico origem, int tipo, Tarefa tarefa) {
        this.origem = origem;
        this.tipo = tipo;
        this.tamComunicacao = 0.011444091796875;
        this.tarefa = tarefa;
    }
    
    public Mensagem(CS_Processamento origem, double tamComunicacao, int tipo) {
        this.origem = origem;
        this.tipo = tipo;
        this.tamComunicacao = tamComunicacao; 
    }
    
    @Override
    public double getTamComunicacao() {
        return tamComunicacao;
    }

    @Override
    public double getTamProcessamento() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CentroServico getOrigem() {
        return origem;
    }

    @Override
    public List<CentroServico> getCaminho() {
        return caminho;
    }

    @Override
    public void setCaminho(List<CentroServico> caminho) {
        this.caminho = caminho;
    }

    public List<Tarefa> getFilaEscravo() {
        return filaEscravo;
    }

    public void setFilaEscravo(List<Tarefa> filaEscravo) {
        this.filaEscravo = filaEscravo;
    }

    public List<Tarefa> getProcessadorEscravo() {
        return processadorEscravo;
    }

    public void setProcessadorEscravo(List<Tarefa> processadorEscravo) {
        this.processadorEscravo = processadorEscravo;
    }
    
    public int getTipo(){
        return tipo;
    }
    
    public Tarefa getTarefa(){
        return tarefa;
    }

    @Override
    public double getTimeCriacao() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
