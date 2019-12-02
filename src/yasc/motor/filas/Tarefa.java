package yasc.motor.filas;

import java.util.ArrayList;
import java.util.List;
import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;
import yasc.motor.metricas.MetricasTarefa;

/**
 * Classe que representa o cliente do modelo de filas, ele será atendido pelos
 * centros de serviços Os clientes podem ser: Tarefas
 */
public class Tarefa implements Cliente {
    // Estados que a tarefa pode estar
    public static final int PARADO = 1;
    public static final int PROCESSANDO = 2;
    public static final int CANCELADO = 3;
    public static final int CONCLUIDO = 4;
    public static final int FALHA = 5;
    
    private final String proprietario;
    private final String aplicacao;
    private final int identificador;
    private final boolean copia;
    private final List<CS_Processamento> historicoProcessamento = new ArrayList<>();
    private boolean falha_atendimento = false;
    private boolean recuperavel = false;
    private int tipo_falha;
    // Timeout para reenvio de tarefas
    private double timeout;
    private CentroServico Serv_falha;
    private double TempoEsperaLocal, TempoAtendLocal;

    public CentroServico getServFalha() {
        return Serv_falha;
    }

    public double getTempoEsperaLocal() {
        return TempoEsperaLocal;
    }

    public double getTempoAtendLocal() {
        return TempoAtendLocal;
    }

    public double getTimeout() {
        return timeout;
    }

    public int getTipoFalha() {
        return tipo_falha;
    }

    public void setTipoFalha(int tipoFalha) {
        this.tipo_falha = tipoFalha;
    }
    
    
    public boolean isRecuperavel() {
        return recuperavel;
    }

    public void setRecuperavel(boolean recuperavel) {
        this.recuperavel = recuperavel;
    }
   
    public boolean isFalha_atendimento() {
        return falha_atendimento;
    }

    public void setFalhaAtendimento(boolean falhaAtendimento) {
        this.falha_atendimento = falhaAtendimento;
    }
    
    /**
     * Indica a quantidade de mflops já processados no momento de um bloqueio
     */
    private double mflopsProcessado;
    /**
     * Indica a quantidade de mflops desperdiçados por uma preempção ou cancelamento
     */
    private double mflopsDesperdicados = 0;
    /**
     * Tamanho do arquivo em Mbits que será enviado para o escravo
     */
    private final double arquivoEnvio;
    /**
     * Tamanho do arquivo em Mbits que será devolvido para o mestre
     */
    private double arquivoRecebimento;
    /**
     * Tamanho em Mflops para processar
     */
    private double tamProcessamento;
    /**
     * Local de origem da mensagem/tarefa
     */
    private final CentroServico origem;
    /**
     * Local de destino da mensagem/tarefa
     */
    private CentroServico destino;
    /**
     * Local de destino da mensagem/tarefa
     */
    private CentroServico localProcessamento;
    /**
     * Caminho que o pacote deve percorrer até o destino O destino é o ultimo
     * item desta lista
     */
    private List<CentroServico> caminho;
    private double inicioEspera;
    private final MetricasTarefa metricas;
    private final double tempoCriacao;
    // Criando o tempo em que a tarefa acabou.
    private final List<Double> tempoFinal;
    // Criando o tempo em que a tarefa começou a ser executada.
    private final List<Double> tempoInicial;
    private int estado;
    private double tamComunicacao;
    
    public Tarefa(int id, String proprietario, String aplicacao, CentroServico origem, CentroServico destino, double ArquivoEnvio, double tempoCriacao, boolean falha, boolean recuperavel, double timeout) {//construtor com recuperação de falha de atendimento
        this.proprietario = proprietario;
        this.aplicacao = aplicacao;
        this.identificador = id;
        this.copia = false;
        this.origem = origem;
        this.destino = destino;
        this.tamComunicacao = ArquivoEnvio;
        this.arquivoEnvio = ArquivoEnvio;
        this.metricas = new MetricasTarefa();
        this.tempoCriacao = tempoCriacao;
        this.estado = PARADO;
        this.tempoInicial = new ArrayList<>();
        this.tempoFinal = new ArrayList<>();
        this.falha_atendimento = falha;
        this.recuperavel = recuperavel;
        this.timeout = timeout;
    }
    
    public Tarefa(int id, String proprietario, String aplicacao, CentroServico origem, 
            CentroServico destino, double ArquivoEnvio, double tempoCriacao) {//construtor sem recuperação de falha de atendimento
        this.proprietario = proprietario;
        this.aplicacao = aplicacao;
        this.identificador = id;
        this.copia = false;
        this.origem = origem;
        this.destino = destino;
        this.tamComunicacao = ArquivoEnvio;
        this.arquivoEnvio = ArquivoEnvio;
        this.metricas = new MetricasTarefa();
        this.tempoCriacao = tempoCriacao;
        this.estado = PARADO;
        this.tempoInicial = new ArrayList<>();
        this.tempoFinal = new ArrayList<>();
    }

    public Tarefa(int id, String proprietario, String aplicacao, CentroServico origem, 
            CentroServico destino, double arquivoEnvio, double tamProcessamento, double tempoCriacao) {
        this.proprietario = proprietario;
        this.aplicacao = aplicacao;
        this.identificador = id;
        this.copia = false;
        this.origem = origem;
        this.destino = destino;
        this.tamComunicacao = arquivoEnvio;
        this.arquivoEnvio = arquivoEnvio;
        this.arquivoRecebimento = 0;
        this.tamProcessamento = tamProcessamento;
        this.metricas = new MetricasTarefa();
        this.tempoCriacao = tempoCriacao;
        this.estado = PARADO;
        this.tempoInicial = new ArrayList<>();
        this.tempoFinal = new ArrayList<>();
    }

    public Tarefa(int id, String proprietario, String aplicacao, CentroServico origem, 
            CentroServico destino, double arquivoEnvio, double arquivoRecebimento, 
            double tamProcessamento, double tempoCriacao) {
        this.identificador = id;
        this.proprietario = proprietario;
        this.aplicacao = aplicacao;
        this.copia = false;
        this.origem = origem;
        this.destino = destino;
        this.tamComunicacao = arquivoEnvio;
        this.arquivoEnvio = arquivoEnvio;
        this.arquivoRecebimento = arquivoRecebimento;
        this.tamProcessamento = tamProcessamento;
        this.metricas = new MetricasTarefa();
        this.tempoCriacao = tempoCriacao;
        this.estado = PARADO;
        this.tempoInicial = new ArrayList<>();
        this.tempoFinal = new ArrayList<>();
    }
    
    public Tarefa(int id, String proprietario, String aplicacao, CentroServico origem, 
            double arquivoEnvio, double arquivoRecebimento, double tamProcessamento, double tempoCriacao) {
        this.identificador = id;
        this.proprietario = proprietario;
        this.aplicacao = aplicacao;
        this.copia = false;
        this.origem = origem;
        this.tamComunicacao = arquivoEnvio;
        this.arquivoEnvio = arquivoEnvio;
        this.arquivoRecebimento = arquivoRecebimento;
        this.tamProcessamento = tamProcessamento;
        this.metricas = new MetricasTarefa();
        this.tempoCriacao = tempoCriacao;
        this.estado = PARADO;
        this.tempoInicial = new ArrayList<>();
        this.tempoFinal = new ArrayList<>();
    }

    public Tarefa(Tarefa tarefa) {
        this.proprietario = tarefa.proprietario;
        this.aplicacao = tarefa.getAplicacao();
        this.identificador = tarefa.identificador;
        this.copia = true;
        this.origem = tarefa.getOrigem();
        this.destino = tarefa.getDestino();
        this.tamComunicacao = tarefa.arquivoEnvio;
        this.arquivoEnvio = tarefa.arquivoEnvio;
        this.arquivoRecebimento = tarefa.arquivoRecebimento;
        this.tamProcessamento = tarefa.getTamProcessamento();
        this.metricas = new MetricasTarefa();
        this.tempoCriacao = tarefa.getTimeCriacao();
        this.estado = PARADO;
        this.mflopsProcessado = 0;
        this.tempoInicial = new ArrayList<>();
        this.tempoFinal = new ArrayList<>();
    }

    @Override
    public double getTamComunicacao() {
        return tamComunicacao;
    }

    @Override
    public double getTamProcessamento() {
        return tamProcessamento;
    }

    public String getProprietario() {
        return proprietario;
    }

    @Override
    public CentroServico getOrigem() {
        return origem;
    }
    
    public CentroServico getDestino() {
        return destino;
    }

    public CentroServico getLocalProcessamento() {
        return localProcessamento;
    }

    public CS_Processamento getCSLProcessamento() {
        return (CS_Processamento) localProcessamento;
    }

    @Override
    public List<CentroServico> getCaminho() {
        return caminho;
    }

    public void setLocalProcessamento(CentroServico localProcessamento) {
        this.localProcessamento = localProcessamento;
    }

    @Override
    public void setCaminho(List<CentroServico> caminho) {
        this.caminho = caminho;
    }

    public void iniciarEsperaComunicacao(double tempo) {
        this.inicioEspera = tempo;
    }

    public void finalizarEsperaComunicacao(double tempo) {
        this.metricas.incTempoEsperaComu(tempo - inicioEspera);
        this.TempoEsperaLocal = tempo - inicioEspera;
    }

    public void iniciarAtendimentoComunicacao(double tempo) {
        this.inicioEspera = tempo;
    }

    public void finalizarAtendimentoComunicacao(double tempo) {
        this.metricas.incTempoComunicacao(tempo - inicioEspera);
        this.TempoAtendLocal = tempo - inicioEspera;
    }

    public void iniciarEsperaProcessamento(double tempo) {
        this.inicioEspera = tempo;
    }

    public void finalizarEsperaProcessamento(double tempo) {
        this.metricas.incTempoEsperaProc(tempo - inicioEspera);
    }

    public void iniciarAtendimentoProcessamento(double tempo) {
        this.estado = PROCESSANDO;
        this.inicioEspera = tempo;
        this.tempoInicial.add(tempo);
        this.historicoProcessamento.add((CS_Processamento) localProcessamento);
    }
    
    public List<CS_Processamento> getHistoricoProcessamento(){
        return this.historicoProcessamento;
    }

    public void finalizarAtendimentoProcessamento(double tempo) {
        this.estado = CONCLUIDO;
        this.metricas.incTempoProcessamento(tempo - inicioEspera);
        if (this.tempoFinal.size() < this.tempoInicial.size()) {
            this.tempoFinal.add(tempo);
        }
        this.tamComunicacao = arquivoRecebimento;
    }

    public double cancelar(double tempo) {
        if (estado == PARADO || estado == PROCESSANDO) {
            this.estado = CANCELADO;
            this.metricas.incTempoProcessamento(tempo - inicioEspera);
            if (this.tempoFinal.size() < this.tempoInicial.size()) {
                this.tempoFinal.add(tempo);
            }
            return inicioEspera;
        } else {
            this.estado = CANCELADO;
            return tempo;
        }
    }

    public double parar(double tempo) {
        if (estado == PROCESSANDO) {
            this.estado = PARADO;
            this.metricas.incTempoProcessamento(tempo - inicioEspera);
            if (this.tempoFinal.size() < this.tempoInicial.size()) {
                this.tempoFinal.add(tempo);
            }
            return inicioEspera;
        } else {
            return tempo;
        }
    }

    public void calcEficiencia(double capacidadeRecebida) {
        this.metricas.calcEficiencia(capacidadeRecebida, tamProcessamento);
    }

    @Override
    public double getTimeCriacao() {
        return tempoCriacao;
    }

    public List<Double> getTempoInicial() {
        return tempoInicial;
    }

    public List<Double> getTempoFinal() {
        return tempoFinal;
    }

    public MetricasTarefa getMetricas() {
        return metricas;
    }

    public int getEstado() {
        return this.estado;
    }
    
    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdentificador() {
        return this.identificador;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    public boolean isCopy() {
        return copia;
    }

    public boolean isCopyOf(Tarefa tarefa) {
        return (this.identificador == tarefa.identificador && !this.equals(tarefa));
    }

    public double getMflopsProcessado() {
        return mflopsProcessado;
    }

    public void setMflopsProcessado(double mflopsProcessado) {
        this.mflopsProcessado = mflopsProcessado;
    }

    public double getMflopsDesperdicados() {
        return mflopsDesperdicados;
    }

    public void incMflopsDesperdicados(double mflopsDesperdicados) {
        this.mflopsDesperdicados += mflopsDesperdicados;
    }
    
    public double getCheckPoint() {
        return 0.0;
    }

    public double getArquivoEnvio() {
        return arquivoEnvio;
    }
}