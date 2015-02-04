/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ispd.escalonador;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author denison_usuario
 */
public interface ManipularArquivos {

    public ArrayList<String> listar();

    public File getDiretorio();

    public boolean escrever(String nome, String codigo);

    public String compilar(String nome);

    public String ler(String escalonador);

    public boolean remover(String escalonador);

    public boolean importarEscalonadorJava(File arquivo);
    
}
