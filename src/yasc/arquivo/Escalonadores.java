package yasc.arquivo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import yasc.escalonador.ManipularArquivos;

/**
 * Esta classe realiza o gerenciamento dos arquivos de escalonamento do
 * simulador
 */
public class Escalonadores implements ManipularArquivos {

    private final String DIRETORIO = "yasc/externo";
    /**
     * Guarda a lista de escalonadores implementados no iSPD, e que já estão
     * disponiveis para o usuario por padrão
     */
    public final static String[] ESCALONADORES = {"---", "RoundRobin", "Workqueue", "WQR", "DynamicFPLTF", "M_OSEP", "OSEP"};
    /**
     * Guarda a lista de escalonadores disponiveis
     */
    private ArrayList<String> escalonadores;
    /**
     * Mantém o caminho do pacote escalonador
     */
    private File diretorio = null;
    private ArrayList<String> adicionados;
    private ArrayList<String> removidos;

    /**
     * @return Diretório onde fica os arquivos dos escalonadores
     */
    @Override
    public File getDiretorio() {
        return diretorio;
    }

    /**
     * Atribui o caminho do pacote escalonador e os escalonadores (.class)
     * contidos nele
     */
    public Escalonadores() {
        try {
            diretorio = new File(Escalonadores.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        } catch (Throwable ex) {
            diretorio = new File(".");
        }
        if (!diretorio.getName().endsWith(".jar")) {
            diretorio = new File(DIRETORIO);
        } else {
            String file = diretorio.getParent() + "/" + DIRETORIO;
            diretorio = new File(file);
        }
        escalonadores = new ArrayList<>();
        adicionados = new ArrayList<>();
        removidos = new ArrayList<>();
        // Verifica se pacote existe caso não exista cria ele
        if (!diretorio.exists()) {
            diretorio.mkdirs();
            // Executando a partir de um jar
            if (getClass().getResource("Escalonadores.class").toString().startsWith("jar:")) {
                File jar = new File(System.getProperty("java.class.path"));
                // Carrega dependencias para compilação
                try {
                    extrairDiretorioJar(jar, "escalonador");
                    extrairDiretorioJar(jar, "motor");
                } catch (ZipException ex) {
                    Logger.getLogger(Escalonadores.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Escalonadores.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // Busca apenas arquivos .class
            FilenameFilter ff = (File b, String name) -> name.endsWith(".class");
            String[] aux = diretorio.list(ff);
            for (int i = 0; i < aux.length; i++) {
                // Remove o .class da string
                aux[i] = aux[i].substring(0, aux[i].length() - 6);
                escalonadores.add(aux[i]);
            }
        }
    }

    /**
     * Método responsável por listar os escalonadores existentes no simulador
     * ele retorna o nome de cada escalonador contido no pacote com arquivo
     * .class
     */
    @Override
    public ArrayList<String> listar() {
        return escalonadores;
    }

    /**
     * Método responsável por remover um escalonador no simulador ele recebe o
     * nome do escalonador e remove do pacote a classe .java e .class
     */
    @Override
    public boolean remover(String nomeEscalonador) {
        boolean deletado = false;
        File escalonador = new File(diretorio, nomeEscalonador + ".class");
        if (escalonador.exists()) {
            escalonador.delete();
            removerLista(nomeEscalonador);
            deletado = true;
        }
        escalonador = new File(diretorio, nomeEscalonador + ".java");
        if (escalonador.exists()) {
            escalonador.delete();
            deletado = true;
        }
        return deletado;
    }

    /**
     * Realiza a leitura do arquivo .java do escalonador e retorna um String do
     * conteudo
     */
    @Override
    public String ler(String escalonador) {
        try {
            FileReader fileInput = new FileReader(new File(diretorio, escalonador + ".java"));
            BufferedReader leitor = new BufferedReader(fileInput);
            StringBuilder buffer = new StringBuilder();
            String linha = leitor.readLine();
            while (linha != null) {
                buffer.append(linha);
                buffer.append('\n');
                linha = leitor.readLine();
            }
            if (buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
            return buffer.toString();
        } catch (IOException ex) {
            Logger.getLogger(Escalonadores.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Este método sobrescreve o arquivo .java do escalonador informado com o
     * buffer
     */
    @Override
    public boolean escrever(String escalonador, String conteudo) {
        FileWriter arquivoFonte;
        try {
            File local = new File(diretorio, escalonador + ".java");
            arquivoFonte = new FileWriter(local);
            arquivoFonte.write(conteudo); //grava no arquivo o codigo-fonte Java
            arquivoFonte.close();
        } catch (IOException ex) {
            Logger.getLogger(Escalonadores.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Compila o arquivo .java do escalonador informado caso ocorra algum erro
     * retorna o erro caso contrario retorna null
     *
     * @param escalonador nome do escalonador
     * @return erros da compilação
     */
    @Override
    public String compilar(String escalonador) {
        //Compilação
        File arquivo = new File(diretorio, escalonador + ".java");
        String errosStr;
        JavaCompiler compilador = ToolProvider.getSystemJavaCompiler();
        if (compilador == null) {
            try {
                Process processo = Runtime.getRuntime().exec("javac " + arquivo.getPath());
                StringBuilder errosdoComando = new StringBuilder();
                InputStream StreamErro = processo.getErrorStream();
                InputStreamReader inpStrAux = new InputStreamReader(StreamErro);
                try (BufferedReader SaidadoProcesso = new BufferedReader(inpStrAux)) {
                    String linha = SaidadoProcesso.readLine();
                    while (linha != null) {
                        errosdoComando.append(linha).append("\n");
                        linha = SaidadoProcesso.readLine();
                    }
                }
                errosStr = errosdoComando.toString();
            } catch (IOException ex) {
                errosStr = "Não foi possível compilar";
                Logger.getLogger(Escalonadores.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            OutputStream erros = new ByteArrayOutputStream();
            compilador.run(null, null, erros, arquivo.getPath());
            errosStr = erros.toString();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Escalonadores.class.getName()).log(Level.SEVERE, null, ex);
        }
        File test = new File(diretorio, escalonador + ".class");
        if (test.exists()) {
            inserirLista(escalonador);
        }
        if (errosStr.equals("")) {
            return null;
        }
        return errosStr;
    }

    /**
     * Recebe nome do escalonar e remove ele da lista de escalonadores
     */
    private void removerLista(String nomeEscalonador) {
        if (escalonadores.contains(nomeEscalonador)) {
            escalonadores.remove(nomeEscalonador);
            removidos.add(nomeEscalonador);
        }
    }

    /**
     * recebe nome do escalonar e adiciona ele na lista de escalonadores
     */
    private void inserirLista(String nome) {
        if (!escalonadores.contains(nome)) {
            escalonadores.add(nome);
            adicionados.add(nome);
        }
    }

    /**
     * @param escalonador
     * @return conteudo básico para criar uma classe que implemente um
     * escalonador
     */
    public static String getEscalonadorJava(String escalonador) {
        String saida =
                "package yasc.externo;"
                + "\n" + "import yasc.escalonador.Escalonador;"
                + "\n" + "import yasc.motor.filas.Tarefa;"
                + "\n" + "import yasc.motor.filas.servidores.CS_Processamento;"
                + "\n" + "import yasc.motor.filas.servidores.CentroServico;"
                + "\n" + "import java.util.ArrayList;"
                + "\n" + "import java.util.List;"
                + "\n"
                + "\n" + "public class " + escalonador + " extends Escalonador{"
                + "\n"
                + "\n" + "    @Override"
                + "\n" + "    public void iniciar() {"
                + "\n" + "        throw new UnsupportedOperationException(\"Not supported yet.\");"
                + "\n" + "    }"
                + "\n"
                + "\n" + "    @Override"
                + "\n" + "    public Tarefa escalonarTarefa() {"
                + "\n" + "        throw new UnsupportedOperationException(\"Not supported yet.\");"
                + "\n" + "    }"
                + "\n"
                + "\n" + "    @Override"
                + "\n" + "    public CS_Processamento escalonarRecurso() {"
                + "\n" + "        throw new UnsupportedOperationException(\"Not supported yet.\");"
                + "\n" + "    }"
                + "\n"
                + "\n" + "    @Override"
                + "\n" + "    public List<CentroServico> escalonarRota(CentroServico destino) {"
                + "\n" + "        throw new UnsupportedOperationException(\"Not supported yet.\");"
                + "\n" + "    }"
                + "\n"
                + "\n" + "    @Override"
                + "\n" + "    public void escalonar() {"
                + "\n" + "        throw new UnsupportedOperationException(\"Not supported yet.\");"
                + "\n" + "    }"
                + "\n"
                + "\n" + "}";
        return saida;
    }

    /**
     * Extrai arquivos que são necessarios fora do jar
     */
    private void extrairDiretorioJar(File arquivoJar, String diretorio) throws ZipException, IOException {
        ZipFile jar = null;
        File arquivo;
        InputStream is = null;
        OutputStream os = null;
        byte[] buffer = new byte[2048]; // 2 Kb //TAMANHO_BUFFER
        try {
            jar = new JarFile(arquivoJar);
            Enumeration e = jar.entries();
            while (e.hasMoreElements()) {
                ZipEntry entrada = (JarEntry) e.nextElement();
                if (entrada.getName().contains(diretorio)) {
                    arquivo = new File(entrada.getName());
                    // Se for diretório inexistente, cria a estrutura
                    // e pula pra próxima entrada
                    if (entrada.isDirectory() && !arquivo.exists()) {
                        arquivo.mkdirs();
                        continue;
                    }
                    // Se a estrutura de diretórios não existe, cria
                    if (!arquivo.getParentFile().exists()) {
                        arquivo.getParentFile().mkdirs();
                    }
                    try {
                        // Lê o arquivo do zip e grava em disco
                        is = jar.getInputStream(entrada);
                        os = new FileOutputStream(arquivo);
                        int bytesLidos;
                        if (is == null) {
                            throw new ZipException("Erro ao ler a entrada do zip: " + entrada.getName());
                        }
                        while ((bytesLidos = is.read(buffer)) > 0) {
                            os.write(buffer, 0, bytesLidos);
                        }
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException ex) {
                            }
                        }
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException ex) {
                            }
                        }
                    }
                }
            }
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Método responsável por adicionar um escalonador no simulador ele recebe
     * uma classe Java compila e adiciona ao pacote a classe .java e .class
     *
     * @param nomeArquivoJava
     * @return true se importar corretamente e false se ocorrer algum erro no
     * processo
     */
    @Override
    public boolean importarEscalonadorJava(File nomeArquivoJava) {
        // Streams
        File localDestino = new File(diretorio, nomeArquivoJava.getName());
        String errosStr;
        // Copiar para diretório
        copiarArquivo(nomeArquivoJava, localDestino);
        // Compilação
        JavaCompiler compilador = ToolProvider.getSystemJavaCompiler();
        if (compilador == null) {
            try {
                Process processo = Runtime.getRuntime().exec("javac " + localDestino.getPath());
                StringBuilder errosdoComando = new StringBuilder();
                InputStream StreamErro = processo.getErrorStream();
                InputStreamReader inpStrAux = new InputStreamReader(StreamErro);
                try (BufferedReader saidaDoProcesso = new BufferedReader(inpStrAux)) {
                    String linha = saidaDoProcesso.readLine();
                    while (linha != null) {
                        errosdoComando.append(linha).append("\n");
                        linha = saidaDoProcesso.readLine();
                    }
                }
                errosStr = errosdoComando.toString();
            } catch (IOException ex) {
                return false;
            }
        } else {
            OutputStream erros = new ByteArrayOutputStream();
            compilador.run(null, null, erros, localDestino.getPath());
            errosStr = erros.toString();
        }
        if (errosStr.length() != 0) {
            return false;
        } else {
            String nome = nomeArquivoJava.getName().substring(0, nomeArquivoJava.getName().length() - 5);
            File test = new File(diretorio, nome + ".class");
            if (!test.exists()) {
                return false;
            }
            inserirLista(nome);
        }
        return true;
    }

    private void copiarArquivo(File arquivoOrigem, File arquivoDestino) {
        // Copiar para diretório
        if (!arquivoDestino.getPath().equals(arquivoOrigem.getPath())) {
            FileInputStream origem;
            FileOutputStream destino;
            FileChannel fcOrigem;
            FileChannel fcDestino;
            try {
                origem = new FileInputStream(arquivoOrigem);
                destino = new FileOutputStream(arquivoDestino);
                fcOrigem = origem.getChannel();
                fcDestino = destino.getChannel();
                // Faz a copia
                fcOrigem.transferTo(0, fcOrigem.size(), fcDestino);
                origem.close();
                destino.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Escalonadores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Escalonadores.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public List listarAdicionados() {
        return adicionados;
    }

    @Override
    public List listarRemovidos() {
        return removidos;
    }
}