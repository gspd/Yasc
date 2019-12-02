package yasc.motor;

import java.awt.Color;
import java.util.List;
import yasc.escalonador.Mestre;
import yasc.motor.filas.Cliente;
import yasc.motor.filas.RedeDeFilas;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;
import yasc.motor.filas.servidores.implementacao.CS_Maquina;
import yasc.motor.filas.servidores.implementacao.CS_Mestre;
import yasc.motor.metricas.Metricas;

public abstract class Simulacao {

    private final RedeDeFilas redeDeFilas;
    private final List<Tarefa> tarefas;
    private final ProgressoSimulacao janela;
    
    public Simulacao(ProgressoSimulacao janela, RedeDeFilas redeDeFilas, List<Tarefa> tarefas){
        this.tarefas = tarefas;
        this.redeDeFilas = redeDeFilas;
        this.janela = janela;
    }

    public ProgressoSimulacao getJanela() {
        return janela;
    }

    public RedeDeFilas getRedeDeFilas() {
        return redeDeFilas;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public abstract void simular();

    public abstract double getTime(Object origem);
    
    public abstract void addEventoFuturo(EventoFuturo ev);
    
    public abstract boolean removeEventoFuturo(int tipoEv, CentroServico servidorEv, Cliente clienteEv);

    public void addTarefa(Tarefa tarefa) {
        tarefas.add(tarefa);
    }

    public void iniciarEscalonadores() {
        for (CS_Processamento mst : redeDeFilas.getMestres()) {
            CS_Mestre mestre = (CS_Mestre) mst;
            // Utilisa a classe de escalonamento diretamente 
            // pode ser modificado para gerar um evento 
            // mas deve ser o primeiro evento executado nos mestres
            mestre.getEscalonador().iniciar();
        }
    }
    
    public void criarRoteamento() {
        for (CS_Processamento mst : redeDeFilas.getMestres()) {
            Mestre temp = (Mestre) mst;
            // Cede acesso ao mestre a fila de eventos futuros
            temp.setSimulacao(this);
            // Encontra menor caminho entre o mestre e seus escravos
            mst.determinarCaminhos();
        }
        if (redeDeFilas.getMaquinas() == null || redeDeFilas.getMaquinas().isEmpty()) {
            janela.println("The model has no processing slaves.", Color.orange);
        } else {
            for (CS_Maquina maq : redeDeFilas.getMaquinas()) {
                // Encontra menor caminho entre o escravo e seu mestre
                maq.determinarCaminhos();
            }
        }
    }

    public Metricas getMetricas() {
        Metricas metrica = new Metricas(redeDeFilas, getTime(null), tarefas);
        return metrica;
    }
}