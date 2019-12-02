package yasc.externo;

import java.util.ArrayList;
import java.util.List;
import yasc.escalonador.Escalonador;
import yasc.motor.Mensagens;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;
import yasc.motor.filas.servidores.implementacao.CS_Maquina;

public class DynamicFPLTF extends Escalonador {

    private List<Double> tempoTornaDisponivel;
    private Tarefa tarefaSelecionada;

    public DynamicFPLTF() {
        this.tarefas = new ArrayList<>();
        this.escravos = new ArrayList<>();
        this.filaEscravo = new ArrayList<>();
        this.tarefaSelecionada = null;
    }

    @Override
    public void iniciar() {
        tempoTornaDisponivel = new ArrayList<>(escravos.size());
        for (CS_Processamento escravo : escravos) {
            tempoTornaDisponivel.add(0.0);
            this.filaEscravo.add(new ArrayList());
        }
    }

    @Override
    public Tarefa escalonarTarefa() {
        return tarefas.remove(0);
    }

    @Override
    public CS_Processamento escalonarRecurso() {
        int index = 0;
        double menorTempo = escravos.get(index).tempoProcessar(tarefaSelecionada.getTamProcessamento());
        for (int i = 1; i < escravos.size(); i++) {
            double tempoEscravoI = escravos.get(i).tempoProcessar(tarefaSelecionada.getTamProcessamento());
            if (tempoTornaDisponivel.get(index) + menorTempo
                    > tempoTornaDisponivel.get(i) + tempoEscravoI) {
                menorTempo = tempoEscravoI;
                index = i;
            }
        }
        return escravos.get(index);
    }

    @Override
    public List<CentroServico> escalonarRota(CentroServico destino) {
        int index = escravos.indexOf(destino);
        return new ArrayList<>((List<CentroServico>) caminhoEscravo.get(index));
    }

    @Override
    public void escalonar() {
        Tarefa trf = escalonarTarefa();
        tarefaSelecionada = trf;
        if (trf != null) {
            CS_Processamento rec = escalonarRecurso();
            int index = escravos.indexOf(rec);
            double custo = rec.tempoProcessar(trf.getTamProcessamento());
            tempoTornaDisponivel.set(index, tempoTornaDisponivel.get(index) + custo);
            trf.setLocalProcessamento(rec);
            trf.setCaminho(escalonarRota(rec));
            mestre.enviarTarefa(trf);
        }
    }

    @Override
    public void adicionarTarefa(Tarefa tarefa) {
        if(tarefa.getOrigem().equals(mestre)){
            this.metricaUsuarios.incTarefasSubmetidas(tarefa);
        }
        int k = 0;
        while (k < tarefas.size() && tarefas.get(k).getTamProcessamento() > tarefa.getTamProcessamento()) {
            k++;
        }
        tarefas.add(k, tarefa);
    }

    @Override
    public void addTarefaConcluida(Tarefa tarefa) {
        super.addTarefaConcluida(tarefa);
        int index = escravos.indexOf(tarefa.getLocalProcessamento());
        if (index != -1) {
            double custo = escravos.get(index).tempoProcessar(tarefa.getTamProcessamento());
            if (tempoTornaDisponivel.get(index) - custo > 0) {
                tempoTornaDisponivel.set(index, tempoTornaDisponivel.get(index) - custo);
            }
        }
        for (int i = 0; i < escravos.size(); i++) {
            if (escravos.get(i) instanceof CS_Maquina) {
                CS_Processamento escravo = escravos.get(i);
                for (int j = 0; j < filaEscravo.get(i).size(); j++) {
                    Tarefa trf = (Tarefa) filaEscravo.get(i).get(j);
                    double custo = escravo.tempoProcessar(trf.getTamProcessamento());
                    if (tempoTornaDisponivel.get(i) - custo > 0) {
                        tempoTornaDisponivel.set(i, tempoTornaDisponivel.get(i) - custo);
                    }
                    mestre.enviarMensagem(trf, escravo, Mensagens.DEVOLVER);
                }
                filaEscravo.get(i).clear();
            }
        }
    }
    
    @Override
    public Double getTempoAtualizar(){
        return 60.0;
    }
}
