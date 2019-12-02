package yasc.gui.entrada;

import java.util.ArrayList;

/**
 * Cria variáveis e listas estáticas que armazenam
 * informações de configuração do simulador.
 */
public class ListasArmazenamento {
    static int id, idPasta;
    private static boolean falha, falhaCapacidade;
    private static boolean perda, fila, atendimento;
    private static String event, filaMet, atendimentoMet;
    public static ArrayList<Parser> lista = new ArrayList<>();
    public static ArrayList<Parser> listaCarregamento = new ArrayList<>();
    
    
    
    public static String getEvent() {
        return event;
    }

    public static void setEvent(String event) {
        ListasArmazenamento.event = event;
    }
    
    public static String getFilaMet() {
        return filaMet;
    }

    public static void setFilaMet(String filaMet) {
        ListasArmazenamento.filaMet = filaMet;
    }

    public static String getAtendimentoMet() {
        return atendimentoMet;
    }

    public static void setAtendimentoMet(String atendimento) {
        ListasArmazenamento.atendimentoMet = atendimento;
    }
   
    public static boolean isPerda() {
        return perda;
    }

    public static void setPerda(boolean perda) {
        ListasArmazenamento.perda = perda;
    }

    public static boolean isFila() {
        return fila;
    }

    public static void setFila(boolean fila) {
        ListasArmazenamento.fila = fila;
    }

    public static boolean isAtendimento() {
        return atendimento;
    }

    public static void setAtendimento(boolean atendimento) {
        ListasArmazenamento.atendimento = atendimento;
    }
    
    public static boolean isFalha() {
        return falha;
    }

    public static void setFalha(boolean falha) {
        ListasArmazenamento.falha = falha;
    }

    public static boolean isFalhaCapacidade() {
        return falhaCapacidade;
    }

    public static void setFalhaCapacidade(boolean falhaCapacidade) {
        ListasArmazenamento.falhaCapacidade = falhaCapacidade;
    }

}
