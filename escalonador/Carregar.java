package yasc.escalonador;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Carrega as classes dos escalonadores dinamicamente
 */
public class Carregar {

    private static final String DIRETORIO = ".";
    private static final String CAMINHO_CLASSE = "yasc.externo.";
    private static URLClassLoader loader = null;

    /**
     * Recebe o nome de um algoritmo de escalonamento e retorna uma nova
     * instancia de um objeto com este nome ou null caso não encontre ou ocorra
     * um erro.
     *
     * @param nome
     * @return Nova instancia do objeto Escalonador
     */
    public static Escalonador getNewEscalonador(String nome) {
        if (loader == null) {
            File diretorio = new File(Escalonador.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            if (!diretorio.getName().endsWith(".jar")) {
                diretorio = new File(DIRETORIO);
            } else {
                String file = diretorio.getParent() + "/" + DIRETORIO;
                diretorio = new File(file);
            }
            if (diretorio.exists()) {
                try {
                    Carregar ref = new Carregar();
                    URL[] aux = new URL[1];
                    aux[0] = diretorio.toURI().toURL();
                    Carregar.loader = URLClassLoader.newInstance(aux, ref.getClass().getClassLoader());
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Carregar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            Class cl = loader.loadClass(CAMINHO_CLASSE + nome);
            Escalonador escalonador = (Escalonador) cl.newInstance();
            
            return escalonador;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            Logger.getLogger(Carregar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}