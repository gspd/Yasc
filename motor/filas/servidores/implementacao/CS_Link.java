package yasc.motor.filas.servidores.implementacao;

import java.util.ArrayList;
import java.util.List;
import yasc.motor.Simulacao;
import yasc.motor.filas.Mensagem;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Comunicacao;
import yasc.motor.filas.servidores.CentroServico;

public class CS_Link extends CS_Comunicacao {

    private CentroServico conexoesEntrada;
    private CentroServico conexoesSaida;
    private final List<Tarefa> filaPacotes;
    private final List<Mensagem> filaMensagens;
    private final boolean linkDisponivel;
    private final boolean linkDisponivelMensagem;
    private final double tempoTransmitirMensagem;
    public static String idPass;

    public CS_Link(String id, double LarguraBanda, double Ocupacao, double Latencia) {
        super(id, LarguraBanda, Ocupacao, Latencia);
        this.conexoesEntrada = null;
        this.conexoesSaida = null;
        this.linkDisponivel = true;
        this.filaPacotes = new ArrayList<>();
        this.filaMensagens = new ArrayList<>();
        this.tempoTransmitirMensagem = 0;
        this.linkDisponivelMensagem = true;
        idPass = id;
        //System.out.println("idpass: " + idPass);
        CS_Instantaneo.Pass.add(idPass);
    }

    public CentroServico getConexoesEntrada() {
        return conexoesEntrada;
    }

    public void setConexoesEntrada(CentroServico conexoesEntrada) {
        this.conexoesEntrada = conexoesEntrada;
    }

    @Override
    public CentroServico getConexoesSaida() {
        return conexoesSaida;
    }

    public void setConexoesSaida(CentroServico conexoesSaida) {
        this.conexoesSaida = conexoesSaida;
    }

    @Override
    public void chegadaDeCliente(Simulacao simulacao, Tarefa cliente) {
        
    }

    @Override
    public void atendimento(Simulacao simulacao, Tarefa cliente) {
        
    }

    @Override
    public void saidaDeCliente(Simulacao simulacao, Tarefa cliente) {
        
    }

    @Override
    public void requisicao(Simulacao simulacao, Mensagem cliente, int tipo) {
        
    }

    @Override
    public Integer getCargaTarefas() {
        return 0;
    }
}
