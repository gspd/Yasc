package yasc.motor.metricas;

import java.io.Serializable;
import java.util.ArrayList;
import yasc.arquivo.CParser_form.CParserForm;

/**
 * Cada centro de serviço usado para conexão deve ter um objeto desta classe
 * Responsavel por armazenar o total de comunicação realizada em Mbits e segundos
 */
public class MetricasComunicacao implements Serializable {
    /**
     * Armazena o total de comunicação realizada em Mbits
     */
    private double MbitsTransmitidos;
    public static ArrayList<String> valorSaida;
    /**
     * Armazena o total de comunicação realizada em segundos
     */
    private double SegundosDeTransmissao;
    private final String id;
    private int result;
    
    public MetricasComunicacao(String id) {
        this.id = id;
        valorSaida = new ArrayList<>();
    }

    public void incMbitsTransmitidos(double MbitsTransmitidos) {
        this.MbitsTransmitidos += MbitsTransmitidos;
    }

    public void incSegundosDeTransmissao(double SegundosDeTransmissao) {
        this.SegundosDeTransmissao += SegundosDeTransmissao;
    }

    public double getMbitsTransmitidos() {
        return MbitsTransmitidos;
    }
    
    public double getSegundosDeTransmissao() {
        return SegundosDeTransmissao;
    }

   public String getId() {
       CParserForm.id = id;
        return id;
    }

    void setMbitsTransmitidos(double d) {
        this.MbitsTransmitidos = d;
    }

    void setSegundosDeTransmissao(double d) {
        this.SegundosDeTransmissao = d;
    }
    
    /* Médodos para serem chamados em caso de uso lógico */
    
     public String buscaValoresResultantes(int result){
        return valorSaida.get(result);
    }
    
    // Armazena os valores de saída no vetor
    public void addValoresResultantes(int result){
        String j = result + "";
        valorSaida.add(j);
    }
     
    // Retorna valor da saida
    public int getValoresResultantes(){
        return result;
    }
    
    // Aplica valor da saída a variavel result. Para que esta seja buscada e adicionada ao arraylist
    public void setValoresResultantes(int d){
        this.result = d;
    }
    
    
}
