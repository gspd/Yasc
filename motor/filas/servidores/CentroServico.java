package yasc.motor.filas.servidores;

import yasc.motor.Simulacao;
import yasc.motor.filas.Mensagem;
import yasc.motor.filas.Tarefa;

/**
 * Elemento servidor do modelo de fila. Podendo representar:
 * 
 * Recurso de comunicação: FilaServidor, FilasServidor, FilaServidores, FilasServidores, ServidorInfinito, Instantaneo, Durativo 
 * Esta classe abstrata indica todos os eventos que um servidor pode realizar no modelo de fila desenvolvido
 */
public abstract class CentroServico {
    
    private boolean falha;
    /** Os eventos basicos de um servidor são: */
    /**
     * Executa as ações necessárias durante a chegada de um cliente na fila do servidor
     * @param simulacao obtem acesso a lista de eventos futuros para adicionar eventos criados durante a execução deste evento
     * @param cliente cliente que acabou de chegar, neste caso uma tarefa
     */
    public abstract void chegadaDeCliente(Simulacao simulacao, Tarefa cliente);
    /**
     * Executa as ações necessárias durante o atendimento de um cliente pelo servidor
     * @param simulacao obtem acesso a lista de eventos futuros para adicionar eventos criados durante a execução deste evento
     * @param cliente cliente atendido, neste caso uma tarefa
     */
    public abstract void atendimento(Simulacao simulacao, Tarefa cliente);
    /**
     * Executa as ações necessárias durante a saida de um cliente após ser atendido pelo servidor
     * @param simulacao obtem acesso a lista de eventos futuros para adicionar eventos criados durante a execução deste evento
     * @param cliente cliente saiu do servidor, neste caso uma tarefa
     */
    public abstract void saidaDeCliente(Simulacao simulacao, Tarefa cliente);
    /**
     * Evento que possibilita o atendimento de uma requisição, diferente de um cliente,
     * mas pode alterar o estado de um cliente, por exemplo cancelando seu atendimento
     * @param simulacao obtem acesso a lista de eventos futuros para adicionar eventos criados durante a execução deste evento
     * @param cliente cliente que será alterado pela requisição
     * @param tipo constante que indica tipo de requisição
     */
    public abstract void requisicao(Simulacao simulacao, Mensagem cliente, int tipo);
    // Obs.: o método requisição pode necessitar de uma nova classe para ser transferida na rede interna
    // e executada apenas nnos centros de serviços
    public abstract String getId();
    /**
     * Retorna conexões de saida do recurso
     * @return conexões de saída do recurso
     */
    public abstract Object getConexoesSaida();
    /**
     * Indica o númro de tarefas sendo atendidas ou na fila do centro de serviço
     * @return número de tarefas
     */
    public abstract Integer getCargaTarefas();
    /**
     * Indica falha de servidor
     * @return falha
     */
    public boolean isFalha() {
        return falha;
    }
    /**
     * Define a opção de falha no servidor
     * @param falha
     */
    public void setFalha(boolean falha) {
        this.falha = falha;
    }
    
}
