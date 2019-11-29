package yasc.escalonador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface ManipularArquivos {

    public ArrayList<String> listar();

    public File getDiretorio();

    public boolean escrever(String nome, String codigo);

    public String compilar(String nome);

    public String ler(String escalonador);

    public boolean remover(String escalonador);

    public boolean importarEscalonadorJava(File arquivo);
    
    public List listarAdicionados();
    
    public List listarRemovidos();
    
}
