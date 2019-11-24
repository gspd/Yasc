package yasc.gui.entrada;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Classe para validação e controle de nomes e valores da interface gráfica do iSPD
 */
public class ValidaValores {

    /** Lista com os nomes dos icones */
    private static HashSet<String> listaNos = new HashSet<String>();

    private static final List<String> LIST_PALAVRAS_RESERVADAS_JAVA
            = Arrays.asList(
                    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
                     "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally",
                     "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface",
                     "long", "native", "new", "null", "package", "private", "protected", "public", "return",
                     "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
                     "transient", "true", "try", "void", "volatile", "while"
            );

    public static void setListaNos(HashSet<String> listaNos) {
        ValidaValores.listaNos = listaNos;
    }
    
    /**
     * Adiciona um nome a lista com os nomes dos icones exitentes
     * @param temp valor a ser adicionado
     */
    public static void addNomeIcone(String temp) {
        listaNos.add(temp);
    }
    
    /**
     * Remove um nome a lista com os nomes dos icones exitentes
     * @param temp valor a ser removido
     */
    public static void removeNomeIcone(String temp) {
        listaNos.remove(temp);
    }
    
    /** Inicia lista sem valores para os nomes dos icones */
    public static void removeTodosNomeIcone() {
        listaNos = new HashSet<>();
    }
    
    /**
     * Verifica se já existe um icone com o nome informado
     * @param temp
     * @return true se não exister e false se existir
     */
    public static boolean NomeIconeNaoExiste(String temp) {
        if (!listaNos.contains(temp)) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "There's already an icon named \'" + temp + "\'.\nPlease enter a different name.", "WARNING", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
    }

    /** Método para validar se o valor fornecido pelo usuário corresponde a um inteiro. */
    public static boolean validaInteiro(String temp) {
        if (temp.matches("\\d*")) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Please enter an integer number.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    /** Método para validar se o nome fornecido pelo usuário corresponde a a um nome valido para um icone. */
    public static boolean validaNomeIcone(String temp) {
        if (temp.matches("[a-zA-Z](.)*")) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid name.\nThe name must begin with a letter.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    /** 
     * Método para validar se o nome fornecido pelo usuário
     * corresponde a um nome valido para uma classe Java.
     */
    public static boolean validaNomeClasse(String temp) {
        if (temp.matches("[a-zA-Z$_][a-zA-Z0-9$_]*")) {
            return !LIST_PALAVRAS_RESERVADAS_JAVA.contains(temp);
        } else {
            return false;
        }
    }

    /**
     * Método para validar se o valor fornecido pelo usuário
     * corresponde a um double valido.
     */
    public static boolean validaDouble(String temp) {
        if (temp.matches("[0-9]+,[0-9]+")) {
            JOptionPane.showMessageDialog(null, "Please use \'.\' to specify floating point numbers.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (temp.matches("[0-9]+.[0-9]+") || temp.matches("")) {
            return true;
        } else if (temp.matches("[0-9]+")) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a floating point number.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
}
