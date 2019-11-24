package yasc.motor;

import yasc.motor.filas.Mensagem;

public interface Mensagens {

    public static final int CANCELAR = 1;
    public static final int PARAR = 2;
    public static final int DEVOLVER = 3;
    public static final int DEVOLVER_COM_PREEMPCAO = 4;
    public static final int ATUALIZAR = 5;
    public static final int RESULTADO_ATUALIZAR = 6;
    public static final int FALHAR = 7;

    public void atenderCancelamento(Simulacao simulacao, Mensagem mensagem);
    public void atenderParada(Simulacao simulacao, Mensagem mensagem);
    public void atenderDevolucao(Simulacao simulacao, Mensagem mensagem);
    public void atenderDevolucaoPreemptiva(Simulacao simulacao, Mensagem mensagem);
    public void atenderAtualizacao(Simulacao simulacao, Mensagem mensagem);
    public void atenderRetornoAtualizacao(Simulacao simulacao, Mensagem mensagem);
    public void atenderFalha(Simulacao simulacao, Mensagem mensagem);
}