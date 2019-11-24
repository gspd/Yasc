package yasc.gui.iconico.grade;

import yasc.Main;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;
import yasc.ValidaValores;
import yasc.arquivo.xml.IconicoXML;
import yasc.gui.JPrincipal;
import yasc.gui.entrada.ListasArmazenamento;
import yasc.gui.iconico.AreaDesenho;
import yasc.gui.iconico.Aresta;
import yasc.gui.iconico.Icone;
import yasc.gui.iconico.Vertice;
import yasc.motor.carga.CargaConst;
import yasc.motor.carga.CargaRandom;
import yasc.motor.carga.GerarCarga;

public class DesenhoGrade extends AreaDesenho {
    
    protected static Image IMACHINE;
    protected static Image ICLUSTER;
    protected static Image IINTERNET;
    protected static Image VECTOR[] = new Image[ListasArmazenamento.listaCarregamento.size()];
    protected static Image IVERDE;
    protected static Image IVERMELHO;
    public static final int MACHINE = 1;
    public static final int NETWORK = 2;
    public static final int CLUSTER = 3;
    public static final int INTERNET = 4;
    public static final int FILA_SERVIDOR = 5;
    public static final int FILAS_SERVIDOR = 6;
    public static final int FILAS_SERVIDORES = 7;
    public static final int FILA_SERVIDORES = 8;
    public static final int SERVIDOR_INFINITO = 9;
    public static final int INSTANTANEO = 10;
    public static final int DURATIVO = 11;
    
    /**
     * Lista com os usuarios/proprietarios do modelo criado
     */
    private HashSet<String> usuarios;
    /**
     * Objeto para Manipular as cargas
     */
    private GerarCarga cargasConfiguracao;
    /**
     * Número de icones excluindo os links
     */
    private int numArestas;
    /**
     * Número de links
     */
    private int numVertices;
    /**
     * Número total de icones
     */
    private int numIcones;
    /**
     * Objetos advindo da classe JanelaPrincipal
     */
    private JPrincipal janelaPrincipal;
    /**
     * Objetos do cursor
     */
    private final Cursor hourglassCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    private final Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    /**
     * Objetos para Selecionar texto na Area Lateral
     */
    private boolean imprimeNosConectados;
    private boolean imprimeNosIndiretos;
    private boolean imprimeNosEscalonaveis;
    /**
     * Obejtos para copiar um ícone
     */
    private Vertice iconeCopiado;
    private int tipoDeVertice;
    private int index;
    private String identificador;

    public DesenhoGrade(int w, int h) throws IOException {
        super(true, true, true, false);
        criarImagens();
        setSize(w, h);
        this.numArestas = 0;
        this.numVertices = 0;
        this.numIcones = 0;
        this.tipoDeVertice = -1;
        usuarios = new HashSet<>();
        usuarios.add("user1");
        ValidaValores.removeTodosNomeIcone();
        cargasConfiguracao = null;
        imprimeNosConectados = false;
        imprimeNosIndiretos = false;
        imprimeNosEscalonaveis = true;
    }

    public void setPaineis(JPrincipal janelaPrincipal) {
        this.janelaPrincipal = janelaPrincipal;
        this.initTexts();
    }

    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Utilizado para inserir novo valor nas Strings dos componentes
     */
    private void initTexts() {
        setPopupButtonText(
                Main.languageResource.getString("Remove"),
                Main.languageResource.getString("Copy"),
                Main.languageResource.getString("Turn Over"),
                Main.languageResource.getString("Paste"));
        setErrorText(
                Main.languageResource.getString("You must click an icon."),
                Main.languageResource.getString("WARNING"));
    }

    @Override
    public void adicionarAresta(Vertice origem, Vertice destino, Vertice originPoint, Vertice destinyPoint) {
        Link link = new Link(origem, destino, originPoint, destinyPoint, numArestas, numIcones);
        link.setLinkDistance();
        
        ((ItemGrade) origem).getConexoesSaida().add(link);
        ((ItemGrade) destino).getConexoesEntrada().add(link);
        
        numArestas++; numIcones++;
        ValidaValores.addNomeIcone(link.getId().getNome());
        arestas.add(link);
        for (Icone icon : selecionados) {
            icon.setSelected(false);
        }
        selecionados.clear();
        selecionados.add(link);
        this.janelaPrincipal.appendNotificacao(Main.languageResource.getString("Link button added."));
        this.janelaPrincipal.modificar();
        this.setLabelAtributos(link);
    }

    @Override
    public void adicionarVertice(int x, int y) {
        ItemGrade vertice = null;
        switch (tipoDeVertice) {
            case FILA_SERVIDOR:
                vertice = new FilaServidor(x, y, numVertices, numIcones, index);
                ValidaValores.addNomeIcone(vertice.getId().getNome());
                this.janelaPrincipal.appendNotificacao(Main.languageResource.getString("One queue one server added."));
                break;
            case FILAS_SERVIDOR:
                vertice = new FilasServidor(x, y, numVertices, numIcones, index);
                ValidaValores.addNomeIcone(vertice.getId().getNome());
                this.janelaPrincipal.appendNotificacao(Main.languageResource.getString("Multiple queues one server added."));
                break;
            case FILAS_SERVIDORES:
                vertice = new FilasServidores(x, y, numVertices, numIcones, index);
                ValidaValores.addNomeIcone(vertice.getId().getNome());
                this.janelaPrincipal.appendNotificacao(Main.languageResource.getString("Multiple queues multiple servers added."));
                break;
            case FILA_SERVIDORES:
                vertice = new FilaServidores(x, y, numVertices, numIcones, index);
                ValidaValores.addNomeIcone(vertice.getId().getNome());
                this.janelaPrincipal.appendNotificacao(Main.languageResource.getString("One queue multiple servers added."));
                break;
            case SERVIDOR_INFINITO:
                vertice = new ServidorInfinito(x, y, numVertices, numIcones, index);
                ValidaValores.addNomeIcone(vertice.getId().getNome());
                this.janelaPrincipal.appendNotificacao(Main.languageResource.getString("Infinity server added."));
                break;
            case DURATIVO:
                vertice = new Durativo(x, y, numVertices,  numIcones, 
                        ListasArmazenamento.listaCarregamento.get(index).getVars(), 
                        ListasArmazenamento.listaCarregamento.get(index).getTipos(), 
                        ListasArmazenamento.listaCarregamento.get(index).getFuncaoTrans(), 
                        index);
                
                ValidaValores.addNomeIcone(vertice.getId().getNome());
                this.janelaPrincipal.appendNotificacao(Main.languageResource.getString("Durational icon added."));
                break;                
            case INSTANTANEO:
                int imgId;
                if (ListasArmazenamento.listaCarregamento.get(index).getGraphicRepresentation().startsWith("__")) {
                    imgId = 0;
                } else
                    imgId = index;
                
                vertice = new Instantaneo(x, y, numVertices,  numIcones, 
                        ListasArmazenamento.listaCarregamento.get(index).getVars(), 
                        ListasArmazenamento.listaCarregamento.get(index).getTipos(), 
                        ListasArmazenamento.listaCarregamento.get(index).getFuncaoTrans(), 
                        imgId, ListasArmazenamento.listaCarregamento.get(index).getInputColunas(), 
                        ListasArmazenamento.listaCarregamento.get(index).getOutputColunas(), 
                        ListasArmazenamento.listaCarregamento.get(index).getInputMatriz(), 
                        ListasArmazenamento.listaCarregamento.get(index).getOutputMatriz(),
                        ListasArmazenamento.listaCarregamento.get(index).getGraphicRepresentation());
                
                ValidaValores.addNomeIcone(vertice.getId().getNome());
                this.janelaPrincipal.appendNotificacao(Main.languageResource.getString("Instantaneous icon added."));
                break;
        }
        if (vertice != null) {
            vertices.add((Vertice) vertice);
            numVertices++;
            numIcones++;
            this.janelaPrincipal.modificar();
            this.setLabelAtributos(vertice);
        }
    }

    public int getIndex() {
        return index;
    }

    public String getIdentificador() {
        return identificador;
    }   

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return this.getSize();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    
    public void setConectados(boolean imprimeNosConectados) {
        this.imprimeNosConectados = imprimeNosConectados;
    }

    public void setIndiretos(boolean imprimeNosIndiretos) {
        this.imprimeNosIndiretos = imprimeNosIndiretos;
    }

    public void setEscalonaveis(boolean imprimeNosEscalonaveis) {
        this.imprimeNosEscalonaveis = imprimeNosEscalonaveis;
    }

    public HashSet<String> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(HashSet<String> usuarios) {
        this.usuarios = usuarios;
    }

    public GerarCarga getCargasConfiguracao() {
        return cargasConfiguracao;
    }

    public void setCargasConfiguracao(GerarCarga cargasConfiguracao) {
        this.cargasConfiguracao = cargasConfiguracao;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent me) {
        repaint();
    }

    @Override
    public void botaoIconeActionPerformed(ActionEvent evt) {
        if (selecionados.isEmpty()) {
            JOptionPane.showMessageDialog(null, Main.languageResource.getString("No icon selected."), Main.languageResource.getString("WARNING"), JOptionPane.WARNING_MESSAGE);
        } else {
            int opcao = JOptionPane.showConfirmDialog(null, Main.languageResource.getString("Remove this icon?"), Main.languageResource.getString("Remove"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opcao == JOptionPane.YES_OPTION) {
                for (Icone iconeRemover : selecionados) {
                    if (iconeRemover instanceof Aresta) {
                        ItemGrade or = (ItemGrade) ((Aresta) iconeRemover).getOrigin();
                        or.getConexoesSaida().remove((ItemGrade) iconeRemover);
                        ItemGrade de = (ItemGrade) ((Aresta) iconeRemover).getDestiny();
                        de.getConexoesEntrada().remove((ItemGrade) iconeRemover);
                        ValidaValores.removeNomeIcone(((ItemGrade) iconeRemover).getId().getNome());
                        arestas.remove((Aresta) iconeRemover);
                        this.janelaPrincipal.modificar();
                    } else {
                        // Remover dados das conexoes que entram
                        Set<ItemGrade> listanos = ((ItemGrade) iconeRemover).getConexoesEntrada();
                        for (ItemGrade I : listanos) {
                            arestas.remove((Aresta) I);
                            ValidaValores.removeNomeIcone(I.getId().getNome());
                        }
                        // Remover dados das conexoes que saem
                        listanos = ((ItemGrade) iconeRemover).getConexoesSaida();
                        for (ItemGrade I : listanos) {
                            arestas.remove((Aresta) I);
                            ValidaValores.removeNomeIcone(I.getId().getNome());
                        }
                        ValidaValores.removeNomeIcone(((ItemGrade) iconeRemover).getId().getNome());
                        vertices.remove((Vertice) iconeRemover);
                        this.janelaPrincipal.modificar();
                    }
                }
                repaint();
            }
        }
    }

    @Override
    public void botaoVerticeActionPerformed(java.awt.event.ActionEvent evt) {
        // Não copia conexão de rede
        if (selecionados.isEmpty()) {
            JOptionPane.showMessageDialog(null, Main.languageResource.getString("No icon selected."), Main.languageResource.getString("WARNING"), JOptionPane.WARNING_MESSAGE);
        } else if (selecionados.size() == 1) {
            Icone item = selecionados.iterator().next();
            if (item instanceof Vertice) {
                iconeCopiado = (Vertice) item;
                this.popupGeral.getComponent(0).setEnabled(true);
            } else {
                iconeCopiado = null;
            }
        }
        if (iconeCopiado == null) {
            this.popupGeral.getComponent(0).setEnabled(false);
        }
    }

    @Override
    public void botaoPainelActionPerformed(java.awt.event.ActionEvent evt) {
        if (iconeCopiado != null) {
            ItemGrade copy = ((ItemGrade) iconeCopiado).criarCopia(getPosicaoMouseX(), getPosicaoMouseY(), numIcones, numVertices);
            vertices.add((Vertice) copy);
            ValidaValores.addNomeIcone(copy.getId().getNome());
            numIcones++;
            numVertices++;
            selecionados.add((Icone) copy);
            this.janelaPrincipal.modificar();
            this.setLabelAtributos(copy);
            repaint();
        }
    }

    @Override
    public void botaoArestaActionPerformed(java.awt.event.ActionEvent evt) {
        if (!selecionados.isEmpty() && selecionados.size() == 1) {
            Link link = (Link) selecionados.iterator().next();         
            selecionados.remove(link);
            link.setSelected(false);
            
            Link temp = link.criarCopia(0, 0, numIcones, numArestas);
            numArestas++; numIcones++;
            temp.setPosition(link.getDestiny(), link.getOrigin());
            ((ItemGrade) temp.getOrigin()).getConexoesSaida().add(temp);
            ((ItemGrade) temp.getDestiny()).getConexoesSaida().add(temp);
            selecionados.add(temp);
            arestas.add(temp);
            
            ValidaValores.addNomeIcone(temp.getId().getNome());
            this.janelaPrincipal.appendNotificacao(Main.languageResource.getString("Network connection added."));
            this.janelaPrincipal.modificar();
            this.setLabelAtributos(temp);
        }
    }

    @Override
    public String toString() {
        StringBuilder saida = new StringBuilder();
        for (Icone icon : vertices) {
            if (icon instanceof Machine) {
                Machine I = (Machine) icon;
                saida.append(String.format("MAQ %s %f %f ", I.getId().getNome(), I.getPoderComputacional(), I.getTaxaOcupacao()));
                if (((Machine) icon).isMestre()) {
                    saida.append(String.format("MESTRE " + I.getAlgoritmo() + " LMAQ"));
                    List<ItemGrade> lista = ((Machine) icon).getEscravos();
                    for (ItemGrade slv : lista) {
                        if (vertices.contains((Vertice) slv)) {
                            saida.append(" ").append(slv.getId().getNome());
                        }
                    }
                } else {
                    saida.append("ESCRAVO");
                }
                saida.append("\n");
            }
        }
        
        for (Icone icon : vertices) {
            if (icon instanceof Cluster) {
                Cluster I = (Cluster) icon;
                saida.append(String.format("CLUSTER %s %d %f %f %f %s\n", I.getId().getNome(), I.getNumeroEscravos(), I.getPoderComputacional(), I.getBanda(), I.getLatencia(), I.getAlgoritmo()));
            } else if (icon instanceof Internet) {
                Internet I = (Internet) icon;
                saida.append(String.format("INET %s %f %f %f\n", I.getId().getNome(), I.getBanda(), I.getLatencia(), I.getTaxaOcupacao()));
            }
        }

        for (Aresta icon : arestas) {
            Link I = (Link) icon;
            saida.append(String.format("REDE %s %f %f %f CONECTA", I.getId().getNome(), I.getBanda(), I.getLatencia(), I.getTaxaOcupacao()));
            saida.append(" ").append(((ItemGrade) icon.getOrigin()).getId().getNome());
            saida.append(" ").append(((ItemGrade) icon.getDestiny()).getId().getNome());
            saida.append("\n");
        }
        saida.append("CARGA");
        
        return saida.toString();
    }

    public void setLabelAtributos(ItemGrade icon) {
        String Texto = "<html>";
        Texto += icon.getAtributos();
        if (imprimeNosConectados && icon instanceof Vertice) {
            Texto = Texto + "<br>" + Main.languageResource.getString("Output Connection:");
            for (ItemGrade i : icon.getConexoesSaida()) {
                ItemGrade saida = (ItemGrade) ((Link) i).getDestiny();
                Texto = Texto + "<br>" + saida.getId().getNome();
            }
            Texto = Texto + "<br>" + Main.languageResource.getString("Input Connection:");
            for (ItemGrade i : icon.getConexoesEntrada()) {
                ItemGrade entrada = (ItemGrade) ((Link) i).getOrigin();
                Texto = Texto + "<br>" + entrada.getId().getNome();
            }
        }
        if (imprimeNosConectados && icon instanceof Aresta) {
            for (ItemGrade i : icon.getConexoesEntrada()) {
                Texto = Texto + "<br>" + Main.languageResource.getString("Source Node:") + " " + i.getConexoesEntrada();
            }
            for (ItemGrade i : icon.getConexoesEntrada()) {
                Texto = Texto + "<br>" + Main.languageResource.getString("Destination Node:") + " " + i.getConexoesSaida();
            }
        }
        if (imprimeNosIndiretos && icon instanceof Machine) {
            Machine I = (Machine) icon;
            Set<ItemGrade> listaEntrada = I.getNosIndiretosEntrada();
            Set<ItemGrade> listaSaida = I.getNosIndiretosSaida();
            Texto = Texto + "<br>" + Main.languageResource.getString("Output Nodes Indirectly Connected:");
            for (ItemGrade i : listaSaida) {
                Texto = Texto + "<br>" + String.valueOf(i.getId().getIdGlobal());
            }
            Texto = Texto + "<br>" + Main.languageResource.getString("Input Nodes Indirectly Connected:");
            for (ItemGrade i : listaEntrada) {
                Texto = Texto + "<br>" + String.valueOf(i.getId().getIdGlobal());
            }
        }
        if (imprimeNosEscalonaveis && icon instanceof Machine) {
            Machine I = (Machine) icon;
            Texto = Texto + "<br>" + Main.languageResource.getString("Schedulable Nodes:");
            for (ItemGrade i : I.getNosEscalonaveis()) {
                Texto = Texto + "<br>" + String.valueOf(i.getId().getIdGlobal());
            }
            if (I.isMestre()) {
                List<ItemGrade> escravos = ((Machine) icon).getEscravos();
                Texto = Texto + "<br>" + Main.languageResource.getString("Slave Nodes:");
                for (ItemGrade i : escravos) {
                    Texto = Texto + "<br>" + i.getId().getNome();
                }
            }
        }
        Texto += "</html>";
        janelaPrincipal.setSelectedIcon(icon, Texto);
    }
    
    /**
     * Define valores para variáveis da classe
     * @param v
     */
    private void inicializaVar(Item v, 
            int capacity,
            boolean fcapacity,
            double preemp,
            double probServ,
            double probServi)
    {
        if (ListasArmazenamento.isFalha()) {
            probServ = v.getProbServerFailure();
            probServi = v.getProbServiceFailure();
        } else {
            probServ = 0.0;
            probServi = 0.0;
        }

        if (ListasArmazenamento.isFalhaCapacidade()) {
            capacity = v.getCapacity();
            fcapacity = true;
        } else {
            capacity = 0;
        }

        if (ListasArmazenamento.listaCarregamento.get(v.getImgId()).getLabels().get(0).equals("Preemption")) {
            preemp = v.getPreemptionTime();
        } else {
            preemp = 0.0;
        }
    }

    /**
     * Transforma os ícones da área de desenho em um Document xml dom
     */
    public Document getGrade() {
        int capacity = 0;
        boolean fcapacity  = false;
        IconicoXML xml = new IconicoXML();
        double preemp = 0, probServer = 0, probService = 0;
        
        for (Vertice v : vertices) {
            if (v instanceof FilaServidor) {
                FilaServidor fs = (FilaServidor) v;
                inicializaVar(fs, capacity, fcapacity, preemp, probServer, probService);
            } else if (v instanceof FilasServidor) {
                FilasServidor fs = (FilasServidor) v;
                inicializaVar(fs, capacity, fcapacity, preemp, probServer, probService);
                xml.addFilasServidor(
                        fs.getX(), fs.getY(),
                        fs.getId().getIdLocal(), fs.getId().getIdGlobal(), fs.getImgId(),
                        fs.getId().getNome(), fs.getServiceRate(),  0.0,
                        fs.getNumQueues(), preemp, fs.getScheduler(), capacity, probServer,
                        probService, fcapacity, fs.isOrigination(), fs.isDestination());
            } else if (v instanceof FilaServidores) {
                FilaServidores fs = (FilaServidores) v;
                inicializaVar(fs, capacity, fcapacity, preemp, probServer, probService);
                xml.addFilaServidores(
                        fs.getX(), fs.getY(),
                        fs.getId().getIdLocal(), fs.getId().getIdGlobal(), fs.getImgId(),
                        fs.getId().getNome(), fs.getServiceRate(),  0.0,
                        fs.getNumServ(), preemp, fs.getScheduler(), capacity, probServer,
                        probService,fcapacity, fs.isOrigination(), fs.isDestination());
            } else if (v instanceof FilasServidores) {
                FilasServidores fs = (FilasServidores) v;
                inicializaVar(fs, capacity, fcapacity, preemp, probServer, probService);
                xml.addFilasServidores(
                        fs.getX(), fs.getY(), 
                        fs.getId().getIdLocal(), fs.getId().getIdGlobal(), fs.getImgId(),
                        fs.getId().getNome(), fs.getServiceRate(),  0.0, fs.getNumServ(),
                        fs.getNumQueues(), preemp, fs.getScheduler(), capacity, probServer,
                        probService, fcapacity, fs.isOrigination(), fs.isDestination());
            } else if (v instanceof ServidorInfinito) {
                ServidorInfinito si = (ServidorInfinito) v;
                inicializaVar(si, capacity, fcapacity, preemp, probServer, probService);
                xml.addServidorInfinito(
                        si.getX(), si.getY(), 
                        si.getId().getIdLocal(), si.getId().getIdGlobal(), si.getImgId(),
                        si.getId().getNome(), si.getServiceRate(),  0.0,
                        preemp, probServer, probService, si.isOrigination(), si.isDestination());
            } else if (v instanceof Instantaneo) {
                Instantaneo i = (Instantaneo) v;
                xml.addInstantaneo(
                            i.getX(), i.getY(), 
                            i.getId().getIdLocal(), i.getId().getIdGlobal(), i.getImgId(),
                            i.getDiagramComponent().toString(), i.getId().getNome(), i.getTransFunc(), i.getTipos(),
                            i.getVars(), i.getValores(), i.isOrigination(), i.isDestination());
            } else {
                Durativo d = (Durativo) v;
                xml.addDurativo(d.getX(), d.getY(), 
                            d.getId().getIdLocal(), d.getId().getIdGlobal(), d.getImgId(),
                            d.getId().getNome(), d.getTransFunc(), d.getTimeTransf(), d.getTipos(),
                            d.getVars(), d.getValores(), d.isOrigination(), d.isDestination());
            }
        }

        for (Aresta link : arestas) {
            Link I = (Link) link;
            xml.addLink(I.getOrigin().getX(), I.getOrigin().getY(), I.getDestiny().getX(), I.getDestiny().getY(),
                    ((Aresta) I).getOriginPoint(), ((Aresta) I).getDestinyPoint(),
                    I.getId().getIdLocal(), I.getId().getIdGlobal(), I.getId().getNome(),
                    ((ItemGrade) I.getOrigin()).getId().getIdGlobal(), ((ItemGrade) I.getDestiny()).getId().getIdGlobal());
        }
        //configurar carga
        if (cargasConfiguracao != null) {
            if (cargasConfiguracao instanceof CargaConst || cargasConfiguracao.getTipo() == GerarCarga.UNIFORM) {
                  CargaConst task = (CargaConst) cargasConfiguracao;
                  xml.setLoadUniform(
                          task.getNumTars(),
                          task.getTam(),
                          task.isDistribuirIgual(),
                          task.getArrivalTime()
                          );      
            } else if (cargasConfiguracao instanceof CargaRandom || cargasConfiguracao.getTipo() == GerarCarga.RANDOM){
                CargaRandom task = (CargaRandom) cargasConfiguracao;
                xml.setLoadRandom(task.getNumeroTarefas(), task.getMax(), task.getAvg2(),
                            task.getMin(), task.getAvgPoisson(), task.getAvgNormal(), task.getDesvPad(),
                            task.getTipoDistr(), task.isDistribuirIgual(), task.getArrivalTime());
            }
        }
        
        return xml.getDescricao();
    }

    public BufferedImage createImage() {
        int maiorx = 0;
        int maiory = 0;
        for (Icone I : vertices) {
            if (I.getX() > maiorx) {
                maiorx = I.getX();
            }
            if (I.getY() > maiory) {
                maiory = I.getY();
            }
        }
        BufferedImage image = new BufferedImage(maiorx + 50, maiory + 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D gc = (Graphics2D) image.getGraphics();
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gc.setColor(new Color(255, 255, 255));
        gc.fillRect(0, 0, maiorx + 50, maiory + 50);
        gc.setColor(new Color(220, 220, 220));
        int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
        int units = (int) (this.isMetric() ? (INCH / 2.54) : (INCH / 2));
        
        if (isGridOn()) {
            for (int _w = 0; _w
                    <= maiorx + 50; _w += units) {
                gc.drawLine(_w, 0, _w, maiory + 50);
            }
            for (int _h = 0; _h
                    <= maiory + 50; _h += units) {
                gc.drawLine(0, _h, maiorx + 50, _h);
            }
        }
        // Desenhamos todos os icones
        for (Icone I : arestas) {
            I.draw(gc);
        }
        for (Icone I : vertices) {
            I.draw(gc);
        }
        
        return image;
    }

    /**
     * Metodo publico para efetuar a copia dos valores de uma conexão de rede
     * especifica informada pelo usuário para as demais conexões de rede.
     */
    public void matchNetwork() {
        if (!selecionados.isEmpty() && selecionados.size() == 1) {
            Link link = (Link) selecionados.iterator().next();
            double banda, taxa, latencia;
            banda = link.getBanda();
            taxa = link.getTaxaOcupacao();
            latencia = link.getLatencia();
            for (Aresta I : arestas) {
                ((Link) I).setBanda(banda);
                ((Link) I).setTaxaOcupacao(taxa);
                ((Link) I).setLatencia(latencia);
            }
        } else {
            JOptionPane.showMessageDialog(null, Main.languageResource.getString("Please select a network icon"), Main.languageResource.getString("WARNING"), JOptionPane.WARNING_MESSAGE);
        }
    }

    /*
     * Organiza ícones na área de desenho
     */
    public void iconArrange() {
        //Distancia entre os icones
        int TAMANHO = 100;
        //posição inicial
        int linha = TAMANHO, coluna = TAMANHO;
        int pos_coluna = 0;
        int totalVertice = vertices.size();
        //número de elementos por linha
        int num_coluna = ((int) Math.sqrt(totalVertice)) + 1;
        //Organiza os icones na tela
        for (Vertice icone : vertices) {
            icone.setPosition(coluna, linha);
            //busca por arestas conectadas ao vertice
            coluna += TAMANHO;
            pos_coluna++;
            if (pos_coluna == num_coluna) {
                pos_coluna = 0;
                coluna = TAMANHO;
                linha += TAMANHO;
            }
        }
    }

    public List<String> getNosEscalonadores() {
        List<String> maquinas = new ArrayList<>();
        for (Icone icon : vertices) {
            if (icon instanceof Machine && ((Machine) icon).isMestre()) {
                maquinas.add(((ItemGrade) icon).getId().getNome());
            }
            if (icon instanceof Cluster && ((Cluster) icon).isMestre()) {
                maquinas.add(((ItemGrade) icon).getId().getNome());
            }
        }
        return maquinas;
    }

    @Override
    public void showSelectionIcon(MouseEvent me, Icone icon) {
        this.setLabelAtributos((ItemGrade) icon);
    }

    @Override
    public void showActionIcon(MouseEvent me, yasc.gui.iconico.Icone icon) {
        this.janelaPrincipal.modificar();
        if (icon instanceof Machine || icon instanceof Cluster) {
            this.janelaPrincipal.getjPanelConfiguracao().setIcone((ItemGrade) icon, usuarios);
            JOptionPane.showMessageDialog(
                    janelaPrincipal,
                    this.janelaPrincipal.getjPanelConfiguracao(),
                    this.janelaPrincipal.getjPanelConfiguracao().getTitle(),
                    JOptionPane.PLAIN_MESSAGE);
        } else {
            this.janelaPrincipal.getjPanelConfiguracao().setIcone((ItemGrade) icon);
            JOptionPane.showMessageDialog(
                    janelaPrincipal,
                    this.janelaPrincipal.getjPanelConfiguracao(),
                    this.janelaPrincipal.getjPanelConfiguracao().getTitle(),
                    JOptionPane.PLAIN_MESSAGE);
        }
        this.setLabelAtributos((ItemGrade) icon);
    }

    public void setIconeSelecionado(Integer object, String id, int ind) {
        if (object != null) {
            if (object == NETWORK) {
                setAddAresta(true);
                setAddVertice(false);
                setCursor(hourglassCursor);
            } else {
                tipoDeVertice = object;
                identificador = id;
                index = ind;
                setAddAresta(false);
                setAddVertice(true);
                setCursor(hourglassCursor);
            }
        } else {
            setAddAresta(false);
            setAddVertice(false);
            setCursor(normalCursor);
        }
    }

    /**
     * 
     * @param descricao 
     */
    public void setGrade(Document descricao) {
        //Realiza leitura dos icones
        IconicoXML.newGrade(descricao, vertices, arestas);
        //Realiza leitura da configuração de carga do modelo
        this.cargasConfiguracao = IconicoXML.newGerarCarga(descricao);
        //Atualiza número de vértices e arestas
        for (Icone icone : vertices) {
            ItemGrade vertc = (ItemGrade) icone;
            if (this.numVertices < vertc.getId().getIdLocal())
                this.numVertices = vertc.getId().getIdLocal();
            
            if (this.numIcones < vertc.getId().getIdGlobal())
                this.numIcones = vertc.getId().getIdGlobal();

        }
        for (Icone icone : arestas) {
            Link link = (Link) icone;
            if (this.numArestas < link.getId().getIdLocal())
                this.numArestas = link.getId().getIdLocal();
            
            if (this.numIcones < link.getId().getIdGlobal())
                this.numIcones = link.getId().getIdGlobal();
        }
        
        this.numIcones++;
        this.numVertices++;
        this.numArestas++;
        repaint();
    }

    /**
     * Carrega as imagens necessárias para os botões da área de desenho
     * @throws IOException
     */
    private static void criarImagens() throws IOException {
        if (IMACHINE == null) {
            ImageIcon verd = new ImageIcon(JPrincipal.class.getResource("imagens/verde.png"));
            IVERDE = verd.getImage();
            ImageIcon verm = new ImageIcon(JPrincipal.class.getResource("imagens/vermelho.png"));
            IVERMELHO = verm.getImage();
            ImageIcon img;
            
            for (int i = 0; i < ListasArmazenamento.listaCarregamento.size(); i++) {
                if (!ListasArmazenamento.listaCarregamento.get(i).getGraphicRepresentation().equals("-") &&
                        !ListasArmazenamento.listaCarregamento.get(i).getGraphicRepresentation().startsWith("__")) {
                    File sourceimage = new File(ListasArmazenamento.listaCarregamento.get(i).getGraphicRepresentation());
                    img = new ImageIcon(ImageIO.read(sourceimage));
                    VECTOR[i] = img.getImage();
                } else if (ListasArmazenamento.listaCarregamento.get(i).getGraphicRepresentation().equals("-")) {
                    if (ListasArmazenamento.listaCarregamento.get(i).isFila()) {
                        switch (ListasArmazenamento.listaCarregamento.get(i).getFilaTipo()) {
                            case "One_queue_one_server":
                                img = new ImageIcon(JPrincipal.class.getResource("imagens/fila1.gif"));
                                VECTOR[i] = img.getImage();
                                break;
                            case "One_queue_multiple_servers":
                                img = new ImageIcon(JPrincipal.class.getResource("imagens/fila3.gif"));
                                VECTOR[i] = img.getImage();
                                break;
                            case "Multiple_queues_multiple_servers":
                                img = new ImageIcon(JPrincipal.class.getResource("imagens/fila2.gif"));
                                VECTOR[i] = img.getImage();
                                break;
                            case "Multiple_queues_one_server":
                                img = new ImageIcon(JPrincipal.class.getResource("imagens/fila4.gif"));
                                VECTOR[i] = img.getImage();
                                break;
                            case "Infinity_Server":
                                img = new ImageIcon(JPrincipal.class.getResource("imagens/fila5.gif"));
                                VECTOR[i] = img.getImage();
                                break;
                        }
                    } else if (ListasArmazenamento.listaCarregamento.get(i).isDurativo()) {
                        img = new ImageIcon(JPrincipal.class.getResource("imagens/durativo.gif"));
                        VECTOR[i] = img.getImage();
                    } else { // Instantaneo
                        img = new ImageIcon(JPrincipal.class.getResource("imagens/instantaneo.gif"));
                        VECTOR[i] = img.getImage();
                    }
                }
            }
        }
    }

    public Set<Vertice> getVertices() {
        return vertices;
    }
    
}