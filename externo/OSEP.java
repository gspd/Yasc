package yasc.externo;

import java.util.ArrayList;
import java.util.List;
import yasc.escalonador.Escalonador;
import yasc.escalonador.Mestre;
import yasc.motor.Mensagens;
import yasc.motor.filas.Mensagem;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;

public class OSEP extends Escalonador {

    Tarefa tarefaSelec;
    List<StatusUser> status;
    List<ControleEscravos> controleEscravos;
    List<Tarefa> esperaTarefas;
    List<ControlePreempcao> controlePreempcao;
    int contadorEscravos;
    List<List> processadorEscravos;
    
    public OSEP() {
        this.tarefas = new ArrayList<>();
        this.escravos = new ArrayList<>();
        this.controleEscravos = new ArrayList<>();
        this.esperaTarefas = new ArrayList<>();
        this.controlePreempcao = new ArrayList<>();
        this.filaEscravo = new ArrayList<>();
        this.processadorEscravos = new ArrayList<>();
        this.contadorEscravos = 0;
    }

    @Override
    public void iniciar() {
        // Escalonamento quando chegam tarefas e quando tarefas são concluídas
        this.mestre.setTipoEscalonamento(Mestre.AMBOS);
        status = new ArrayList<>();

        for (int i = 0; i < metricaUsuarios.getUsuarios().size(); i++) {//Objetos de controle de uso e cota para cada um dos usuários
            status.add(new StatusUser(metricaUsuarios.getUsuarios().get(i), metricaUsuarios.getPoderComputacional(metricaUsuarios.getUsuarios().get(i))));
        }

        for (CS_Processamento escravo : escravos) {
            // Contadores para lidar com a dinamicidade dos dados
            controleEscravos.add(new ControleEscravos());
            filaEscravo.add(new ArrayList<>());
            processadorEscravos.add(new ArrayList<>());
        }
    }

    @Override
    public Tarefa escalonarTarefa() {
        // Usuários com maior diferença entre uso e posse terão preferência
        int difUsuarioMinimo =  -1;
        int indexUsuarioMinimo = -1;
        // Encontrar o usuário que está mais abaixo da sua propriedade 
        for (int i = 0; i < metricaUsuarios.getUsuarios().size(); i++) {
            // Verificar se existem tarefas do usuário corrente
            boolean demanda = false;

            for (int j = 0; j < tarefas.size(); j++) {
                if (tarefas.get(j).getProprietario().equals(metricaUsuarios.getUsuarios().get(i))) {
                    demanda = true;
                    break;
                }
            }

            // Caso existam tarefas do usuário corrente e ele esteja com uso menor que sua posse
            if ((status.get(i).GetNumUso() < status.get(i).GetNumCota()) && demanda) {

                if (difUsuarioMinimo == -1) {
                    difUsuarioMinimo = status.get(i).GetNumCota() - status.get(i).GetNumUso();
                    indexUsuarioMinimo = i;
                } else {
                    if (difUsuarioMinimo < status.get(i).GetNumCota() - status.get(i).GetNumUso()) {
                        difUsuarioMinimo = status.get(i).GetNumCota() - status.get(i).GetNumUso();
                        indexUsuarioMinimo = i;
                    }
                }
            }
        }

        if (indexUsuarioMinimo != -1) {
            int indexTarefa = -1;

            for (int i = 0; i < tarefas.size(); i++) {
                if (tarefas.get(i).getProprietario().equals(metricaUsuarios.getUsuarios().get(indexUsuarioMinimo))) {
                    if (indexTarefa == -1) {
                        indexTarefa = i;
                        break;
                    } 
                }
            }

            if (indexTarefa != -1) {
                return tarefas.remove(indexTarefa);
            }
        }

        if (tarefas.size() > 0) {
            return tarefas.remove(0);
        } else {
            return null;
        }

    }

    @Override
    public CS_Processamento escalonarRecurso() {
        //Buscando recurso livre
        CS_Processamento selec = null;

        for (int i = 0; i < escravos.size(); i++) {
            if (filaEscravo.get(i).isEmpty() && processadorEscravos.get(i).isEmpty() && controleEscravos.get(i).Livre()) {//Garantir que o escravo está de fato livre e que não há nenhuma tarefa em trânsito para o escravo
                if (selec == null) {
                    selec = escravos.get(i);
                    break;
                }
            }
        }

        if (selec != null) {
            controleEscravos.get(escravos.indexOf(selec)).SetBloqueado();//Inidcar que uma tarefa será enviada e que , portanto , este escravo deve ser bloqueada até a próxima atualização
            return selec;
        }

        String usermax = null;
        int diff = -1;

        for (int i = 0; i < metricaUsuarios.getUsuarios().size(); i++) {
            if (status.get(i).GetNumUso() > status.get(i).GetNumCota() && !metricaUsuarios.getUsuarios().get(i).equals(tarefaSelec.getProprietario())) {
                if (diff == -1) {
                    usermax = metricaUsuarios.getUsuarios().get(i);
                    diff = status.get(i).GetNumUso() - status.get(i).GetNumCota();
                } else {
                    if (status.get(i).GetNumUso() - status.get(i).GetNumCota() > diff) {
                        usermax = metricaUsuarios.getUsuarios().get(i);
                        diff = status.get(i).GetNumUso() - status.get(i).GetNumCota();
                    }
                }
            }
        }

        int index = -1;
        if (usermax != null) {
            for (int i = 0; i < escravos.size(); i++) {
                if (processadorEscravos.get(i).size() == 1 && controleEscravos.get(i).Ocupado() && filaEscravo.get(i).isEmpty() && ((Tarefa) processadorEscravos.get(i).get(0)).getProprietario().equals(usermax)) {
                        index = i;
                        break;
                }
            }
        }

        // Fazer a preempção
        if (index != -1) {
            selec = escravos.get(index);
            int index_selec = escravos.indexOf(selec);
            controleEscravos.get(escravos.indexOf(selec)).setPreemp();
            mestre.enviarMensagem((Tarefa) processadorEscravos.get(index_selec).get(0), selec, Mensagens.DEVOLVER_COM_PREEMPCAO);
            return selec;
        }
        return null;
    }

    @Override
    public List<CentroServico> escalonarRota(CentroServico destino) {
        int index = escravos.indexOf(destino);
        return new ArrayList<>((List<CentroServico>) caminhoEscravo.get(index));
    }

    @Override
    public void resultadoAtualizar(Mensagem mensagem) {
        super.resultadoAtualizar(mensagem);
        int index = escravos.indexOf(mensagem.getOrigem());
        processadorEscravos.set(index, mensagem.getProcessadorEscravo());
        contadorEscravos++;
        for (int i = 0; i < escravos.size(); i++) {
            if (processadorEscravos.get(i).size() > 1) {
                System.out.printf("Escravo %s executando %d\n", escravos.get(i).getId(), processadorEscravos.get(i).size());
                System.out.println("PROBLEMA!");
            }
            if (filaEscravo.get(i).size() > 0) {
                System.out.println("Tem Fila");
            }
        }
        if (contadorEscravos == escravos.size()) {
            boolean escalona = false;
            for (int i = 0; i < escravos.size(); i++) {
                if (processadorEscravos.get(i).size() == 1 && !controleEscravos.get(i).Preemp()) {
                    controleEscravos.get(i).SetOcupado();
                } else if (processadorEscravos.get(i).isEmpty() && !controleEscravos.get(i).Preemp()) {
                    escalona = true;
                    controleEscravos.get(i).SetLivre();
                } else if (controleEscravos.get(i).Preemp()) {
                    controleEscravos.get(i).SetBloqueado();
                }
            }
            contadorEscravos = 0;
            if (tarefas.size() > 0 && escalona) {
                mestre.executarEscalonamento();
            }
        }
    }

    @Override
    public void escalonar() {
        Tarefa trf = escalonarTarefa();
        tarefaSelec = trf;
        if (trf != null) {
            CS_Processamento rec = escalonarRecurso();
            if (rec != null) {
                trf.setLocalProcessamento(rec);
                trf.setCaminho(escalonarRota(rec));
                // Verifica se não é caso de preempção
                if (!controleEscravos.get(escravos.indexOf(rec)).Preemp()) {
                    status.get(metricaUsuarios.getUsuarios().indexOf(trf.getProprietario())).AtualizaUso(rec.getPoderComputacional(), 1);
                    mestre.enviarTarefa(trf);
                } else {
                    int index_rec = escravos.indexOf(rec);
                    esperaTarefas.add(trf);
                    controlePreempcao.add(new ControlePreempcao(((Tarefa) processadorEscravos.get(index_rec).get(0)).getProprietario(), ((Tarefa) processadorEscravos.get(index_rec).get(0)).getIdentificador(), trf.getProprietario(), trf.getIdentificador()));
                    int indexUser = metricaUsuarios.getUsuarios().indexOf(((Tarefa) processadorEscravos.get(index_rec).get(0)).getProprietario());
                    status.get(indexUser).AtualizaUso(rec.getPoderComputacional(), 0);
                }

                for (int i = 0; i < escravos.size(); i++) {
                    if (processadorEscravos.get(i).size() > 1) {
                        System.out.printf("Escravo %s executando %d\n", escravos.get(i).getId(), processadorEscravos.get(i).size());
                        System.out.println("PROBLEMA1");
                    }
                    if (filaEscravo.get(i).size() > 0) {
                        System.out.println("Tem Fila");
                    }
                }
            } else {
                tarefas.add(trf);
                tarefaSelec = null;
            }
        }
    }

    @Override
    public void addTarefaConcluida(Tarefa tarefa) {
        super.addTarefaConcluida(tarefa);
        CS_Processamento maq = (CS_Processamento) tarefa.getLocalProcessamento();
        int indexUser = metricaUsuarios.getUsuarios().indexOf(tarefa.getProprietario());
        status.get(indexUser).AtualizaUso(maq.getPoderComputacional(), 0);
    }

    @Override
    public void adicionarTarefa(Tarefa tarefa) {
        super.adicionarTarefa(tarefa);
        CS_Processamento maq = (CS_Processamento) tarefa.getLocalProcessamento();
        int indexUser;
        // Em caso de preempção, é procurada a tarefa correspondente para ser enviada ao escravo agora desocupado
        if (tarefa.getLocalProcessamento() != null) {
            int j;
            int indexControle = -1;
            for (j = 0; j < controlePreempcao.size(); j++) {
                if (controlePreempcao.get(j).getPreempID() == tarefa.getIdentificador() && controlePreempcao.get(j).getUsuarioPreemp().equals(tarefa.getProprietario())) {
                    indexControle = j;
                    break;
                }
            }

            for (int i = 0; i < esperaTarefas.size(); i++) {
                if (esperaTarefas.get(i).getProprietario().equals(controlePreempcao.get(indexControle).getUsuarioAlloc()) && esperaTarefas.get(i).getIdentificador() == controlePreempcao.get(j).getAllocID()) {
                    indexUser = metricaUsuarios.getUsuarios().indexOf(controlePreempcao.get(indexControle).getUsuarioAlloc());
                    status.get(indexUser).AtualizaUso(maq.getPoderComputacional(), 1);
                    mestre.enviarTarefa(esperaTarefas.get(i));
                    esperaTarefas.remove(i);
                    controlePreempcao.remove(j);
                    break;
                }
            }
        }
    }

    @Override
    public Double getTempoAtualizar() {
        return 15.0;
    }

    private class StatusUser {

        private final String usuario;
        private Double PoderEmUso;
        private final Double Cota;
        private int numCota;
        private int numUso;

        public StatusUser(String usuario, Double poder) {
            this.usuario = usuario;
            this.PoderEmUso = 0.0;
            this.Cota = poder;
            this.numCota = 0;
            this.numUso = 0;

            for (int i = 0; i < escravos.size(); i++) {
                if (escravos.get(i).getProprietario().equals(this.usuario)) {
                    numCota++;
                }
            }
        }

        public void AtualizaUso(Double poder, int opc) {
            if (opc == 1) {
                this.PoderEmUso = this.PoderEmUso + poder;
                this.numUso++;
            } else {
                this.PoderEmUso = this.PoderEmUso - poder;
                this.numUso--;
            }
        }

        public Double GetCota() {
            return this.Cota;
        }

        public Double GetUso() {
            return this.PoderEmUso;
        }

        public int GetNumCota() {
            return this.numCota;
        }

        public int GetNumUso() {
            return this.numUso;
        }
    }

    private class ControleEscravos {

        private int contador;

        public ControleEscravos() {
            this.contador = 0;
        }

        public boolean Ocupado() {
            return (this.contador == 1);
        }

        public boolean Livre() {
            return (this.contador == 0);
        }

        public boolean Bloqueado() {
            return (this.contador == 2);
        }

        public boolean Preemp() {
            return (this.contador == 3);
        }

        public void SetOcupado() {
            this.contador = 1;
        }

        public void SetLivre() {
            this.contador = 0;
        }

        public void SetBloqueado() {
            this.contador = 2;
        }

        public void setPreemp() {
            this.contador = 3;
        }
    }

    public class ControlePreempcao {

        private final String usuarioPreemp;
        private final String usuarioAlloc;
        private final int preempID;
        private final int allocID;

        public ControlePreempcao(String user1, int pID, String user2, int aID) {
            this.usuarioPreemp = user1;
            this.preempID = pID;
            this.usuarioAlloc = user2;
            this.allocID = aID;
        }

        public String getUsuarioPreemp() {
            return this.usuarioPreemp;
        }

        public int getPreempID() {
            return this.preempID;
        }

        public String getUsuarioAlloc() {
            return this.usuarioAlloc;
        }

        public int getAllocID() {
            return this.allocID;
        }
    }
}