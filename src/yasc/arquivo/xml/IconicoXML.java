package yasc.arquivo.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import yasc.ValidaValores;
import yasc.arquivo.CParser_table.CParser_table;
import yasc.gui.JPrincipal;
import yasc.gui.iconico.Aresta;
import yasc.gui.iconico.Vertice;
import yasc.gui.iconico.grade.DiagramComponentConnector;
import yasc.gui.iconico.grade.Durativo;
import yasc.gui.iconico.grade.FilaServidor;
import yasc.gui.iconico.grade.FilaServidores;
import yasc.gui.iconico.grade.FilasServidor;
import yasc.gui.iconico.grade.FilasServidores;
import yasc.gui.iconico.grade.Instantaneo;
import yasc.gui.iconico.grade.Item;
import yasc.gui.iconico.grade.ItemGrade;
import yasc.gui.iconico.grade.Link;
import yasc.gui.iconico.grade.ServidorInfinito;
import yasc.motor.carga.CargaConst;
import yasc.motor.carga.CargaRandom;
import yasc.motor.carga.GerarCarga;
import yasc.motor.filas.RedeDeFilas;
import yasc.motor.filas.servidores.CentroServico;
import yasc.motor.filas.servidores.implementacao.CS_Durativo;
import yasc.motor.filas.servidores.implementacao.CS_FilaServidor;
import yasc.motor.filas.servidores.implementacao.CS_FilaServidores;
import yasc.motor.filas.servidores.implementacao.CS_FilasServidor;
import yasc.motor.filas.servidores.implementacao.CS_FilasServidores;
import yasc.motor.filas.servidores.implementacao.CS_Instantaneo;
import yasc.motor.filas.servidores.implementacao.CS_ServidorInfinito;
import yasc.motor.filas.servidores.implementacao.CS_Vertice;

/**
 * Realiza manupulações com o arquivo xml do modelo icônico
 *
 * @author denison
 */
public class IconicoXML {

    private final Document descricao;
    private final Element system;
    private Element load;
    public static String teste;

    public IconicoXML() {
        descricao = ManipuladorXML.novoDocumento();
        system = descricao.createElement("system");
        system.setAttribute("version", "1.2");
        load = null;
        descricao.appendChild(system);
    }

    /**
     * Este método sobrescreve ou cria arquivo xml do modelo iconico
     *
     * @param documento modelo iconico
     * @param arquivo local que será salvo
     * @return indica se arquivo foi salvo corretamente
     */
    public static boolean escrever(Document documento, File arquivo) {
        return ManipuladorXML.escrever(documento, arquivo, "iSPD.dtd", false);
    }

    /**
     * Realiza a leitura de um arquivo xml contendo o modelo iconico
     * especificado pelo iSPD.dtd
     *
     * @param xmlFile endereço do arquivo xml
     * @return modelo iconico obtido do arquivo
     */
    public static Document ler(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        return ManipuladorXML.leitura(xmlFile, "valida.xsd");
    }

    /**
     * Verifica se modelo está completo
     */
    public static void validarModelo(Document documento) throws IllegalArgumentException {
        org.w3c.dom.NodeList FilaServidor = documento.getElementsByTagName("FilaServidor");
        org.w3c.dom.NodeList FilasServidor = documento.getElementsByTagName("FilasServidor");
        org.w3c.dom.NodeList FilaServidores = documento.getElementsByTagName("FilaServidores");
        org.w3c.dom.NodeList FilasServidores = documento.getElementsByTagName("FilasServidores");
        org.w3c.dom.NodeList ServidorInfinito = documento.getElementsByTagName("ServidorInfinito");
        org.w3c.dom.NodeList Instantaneo = documento.getElementsByTagName("Instantaneo");
        org.w3c.dom.NodeList Durativo = documento.getElementsByTagName("Durativo");
        org.w3c.dom.NodeList cargas = documento.getElementsByTagName("load");

        if (FilaServidor.getLength() == 0 && FilaServidores.getLength() == 0 && FilasServidores.getLength() == 0
                && FilasServidor.getLength() == 0 && ServidorInfinito.getLength() == 0 && Instantaneo.getLength() == 0
                && Durativo.getLength() == 0) {
            throw new IllegalArgumentException("The model has no icons.");
        }
        if (cargas.getLength() == 0  && JPrincipal.clock == null) {
            throw new IllegalArgumentException("One or more workloads have not been configured.");
        }
        int i;
        // Laço para validar cada elemento do modelo
        for (i = 0; i < FilaServidor.getLength(); i++) {
            FilaServidor.item(i).getAttributes();
        }
        for (i = 0; i < FilasServidor.getLength(); i++) {
            FilasServidor.item(i).getAttributes();
        }
        for (i = 0; i < FilaServidores.getLength(); i++) {
            FilaServidores.item(i).getAttributes();
        }
        for (i = 0; i < FilasServidores.getLength(); i++) {
            FilasServidores.item(i).getAttributes();
        }
        for (i = 0; i < ServidorInfinito.getLength(); i++) {
            ServidorInfinito.item(i).getAttributes();
        }
        for (i = 0; i < Instantaneo.getLength(); i++) {
            Instantaneo.item(i).getAttributes();
        }
        for (i = 0; i < Durativo.getLength(); i++) {
            Durativo.item(i).getAttributes();
        }
    }

    /**
     * Converte um modelo iconico em uma rede de filas para o motor de simulação
     *
     * @param modelo Objeto obtido a partir do xml com a grade computacional
     * modelada
     * @return Rede de filas simulável contruida conforme modelo
     */
    public static RedeDeFilas newRedeDeFilas(Document modelo) {
        NodeList docFilaServidor = modelo.getElementsByTagName("FilaServidor");
        NodeList docFilasServidor = modelo.getElementsByTagName("FilasServidor");
        NodeList docFilaServidores = modelo.getElementsByTagName("FilaServidores");
        NodeList docFilasServidores = modelo.getElementsByTagName("FilasServidores");
        NodeList docServidorInfinito = modelo.getElementsByTagName("ServidorInfinito");
        NodeList docInstantaneo = modelo.getElementsByTagName("Instantaneo");
        NodeList docDurativo = modelo.getElementsByTagName("Durativo");
        NodeList doclinks = modelo.getElementsByTagName("link");

        HashMap<Integer, CentroServico> centroDeServicos = new HashMap<>();
        List<CentroServico> cs = new ArrayList<>();
        List<CentroServico> Origens = new ArrayList<>();
        List<CentroServico> Destinos = new ArrayList<>();
        boolean origem;
        boolean destino;

        // Realiza leitura dos icones de FilaServidor
        for (int i = 0; i < docFilaServidor.getLength(); i++) {
            Element filaServ = (Element) docFilaServidor.item(i);
            Element id = (Element) filaServ.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            CS_FilaServidor FS = new CS_FilaServidor(
                    filaServ.getAttribute("id"),
                    Double.parseDouble(filaServ.getAttribute("ServiceRate")),
                    Double.parseDouble(filaServ.getAttribute("PreemptionTime")),
                    Double.parseDouble(filaServ.getAttribute("DelayRate")),
                    filaServ.getAttribute("Scheduler"),
                    Integer.parseInt(filaServ.getAttribute("Capacity")),
                    Double.parseDouble(filaServ.getAttribute("ProbServerFailure")),
                    Double.parseDouble(filaServ.getAttribute("ProbServiceFailure")),
                    Boolean.parseBoolean(filaServ.getAttribute("FCapacity")),
                    Boolean.parseBoolean(filaServ.getAttribute("Origination")),
                    Boolean.parseBoolean(filaServ.getAttribute("Destination")));
            cs.add(FS);
            centroDeServicos.put(global, FS);
            origem = Boolean.parseBoolean(filaServ.getAttribute("Origination"));
            destino = Boolean.parseBoolean(filaServ.getAttribute("Destination"));
            if (origem) {
                Origens.add(FS);
            }
            if (destino) {
                Destinos.add(FS);
            }
        }
        // Realiza leitura dos icones de FilasServidor
        for (int i = 0; i < docFilasServidor.getLength(); i++) {
            Element filasServ = (Element) docFilasServidor.item(i);
            Element id = (Element) filasServ.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            CS_FilasServidor FsS = new CS_FilasServidor(
                    filasServ.getAttribute("id"),
                    Double.parseDouble(filasServ.getAttribute("ServiceRate")),
                    Double.parseDouble(filasServ.getAttribute("PreemptionTime")),
                    Double.parseDouble(filasServ.getAttribute("DelayRate")),
                    Integer.parseInt(filasServ.getAttribute("NumQueues")),
                    filasServ.getAttribute("Scheduler"),
                    Integer.parseInt(filasServ.getAttribute("Capacity")),
                    Double.parseDouble(filasServ.getAttribute("ProbServerFailure")),
                    Double.parseDouble(filasServ.getAttribute("ProbServiceFailure")),
                    Boolean.parseBoolean(filasServ.getAttribute("FCapacity")),
                    Boolean.parseBoolean(filasServ.getAttribute("Origination")),
                    Boolean.parseBoolean(filasServ.getAttribute("Destination")));
            cs.add(FsS);
            centroDeServicos.put(global, FsS);
            origem = Boolean.parseBoolean(filasServ.getAttribute("Origination"));
            destino = Boolean.parseBoolean(filasServ.getAttribute("Destination"));
            if (origem) {
                Origens.add(FsS);
            }
            if (destino) {
                Destinos.add(FsS);
            }
        }
        // Realiza leitura dos icones de FilasServidores
        for (int i = 0; i < docFilasServidores.getLength(); i++) {
            Element filasServs = (Element) docFilasServidores.item(i);
            Element id = (Element) filasServs.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            CS_FilasServidores FsSs = new CS_FilasServidores(
                    filasServs.getAttribute("id"),
                    Double.parseDouble(filasServs.getAttribute("ServiceRate")),
                    Double.parseDouble(filasServs.getAttribute("PreemptionTime")),
                    Double.parseDouble(filasServs.getAttribute("DelayRate")),
                    filasServs.getAttribute("Scheduler"),
                    Integer.parseInt(filasServs.getAttribute("NumQueues")),
                    Integer.parseInt(filasServs.getAttribute("NumServ")),
                    Integer.parseInt(filasServs.getAttribute("Capacity")),
                    Double.parseDouble(filasServs.getAttribute("ProbServerFailure")),
                    Double.parseDouble(filasServs.getAttribute("ProbServiceFailure")),
                    Boolean.parseBoolean(filasServs.getAttribute("FCapacity")),
                    Boolean.parseBoolean(filasServs.getAttribute("Origination")),
                    Boolean.parseBoolean(filasServs.getAttribute("Destination")));
            cs.add(FsSs);
            centroDeServicos.put(global, FsSs);
            origem = Boolean.parseBoolean(filasServs.getAttribute("Origination"));
            destino = Boolean.parseBoolean(filasServs.getAttribute("Destination"));
            if (origem) {
                Origens.add(FsSs);
            }
            if (destino) {
                Destinos.add(FsSs);
            }
        }
        // Realiza leitura dos icones de FilaServidores
        for (int i = 0; i < docFilaServidores.getLength(); i++) {
            Element filaServs = (Element) docFilaServidores.item(i);
            Element id = (Element) filaServs.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            CS_FilaServidores FSs = new CS_FilaServidores(
                    filaServs.getAttribute("id"),
                    Double.parseDouble(filaServs.getAttribute("ServiceRate")),
                    Double.parseDouble(filaServs.getAttribute("PreemptionTime")),
                    Double.parseDouble(filaServs.getAttribute("DelayRate")),
                    filaServs.getAttribute("Scheduler"),
                    Integer.parseInt(filaServs.getAttribute("NumServ")),
                    Integer.parseInt(filaServs.getAttribute("Capacity")),
                    Double.parseDouble(filaServs.getAttribute("ProbServerFailure")),
                    Double.parseDouble(filaServs.getAttribute("ProbServiceFailure")),
                    Boolean.parseBoolean(filaServs.getAttribute("FCapacity")),
                    Boolean.parseBoolean(filaServs.getAttribute("Origination")),
                    Boolean.parseBoolean(filaServs.getAttribute("Destination")));
            cs.add(FSs);
            centroDeServicos.put(global, FSs);
            origem = Boolean.parseBoolean(filaServs.getAttribute("Origination"));
            destino = Boolean.parseBoolean(filaServs.getAttribute("Destination"));
            if (origem) {
                Origens.add(FSs);
            }
            if (destino) {
                Destinos.add(FSs);
            }
        }
        // Realiza leitura dos icones de ServidorInfinito
        for (int i = 0; i < docServidorInfinito.getLength(); i++) {
            Element Serv = (Element) docServidorInfinito.item(i);
            Element id = (Element) Serv.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            CS_ServidorInfinito SI = new CS_ServidorInfinito(
                    Serv.getAttribute("id"),
                    Double.parseDouble(Serv.getAttribute("ServiceRate")),
                    Double.parseDouble(Serv.getAttribute("PreemptionTime")),
                    Double.parseDouble(Serv.getAttribute("DelayRate")),
                    Double.parseDouble(Serv.getAttribute("ProbServerFailure")),
                    Double.parseDouble(Serv.getAttribute("ProbServiceFailure")),
                    Boolean.parseBoolean(Serv.getAttribute("Origination")),
                    Boolean.parseBoolean(Serv.getAttribute("Destination")));
            cs.add(SI);
            centroDeServicos.put(global, SI);
            origem = Boolean.parseBoolean(Serv.getAttribute("Origination"));
            destino = Boolean.parseBoolean(Serv.getAttribute("Destination"));
            if (origem) {
                Origens.add(SI);
            }
            if (destino) {
                Destinos.add(SI);
            }
        }
        // Realiza leitura dos icones Instantaneos
        for (int i = 0; i < docInstantaneo.getLength(); i++) {
            Element Inst = (Element) docInstantaneo.item(i);
            Element id = (Element) Inst.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            ArrayList<String> Names = new ArrayList<>();
            ArrayList<String> Val = new ArrayList<>();
            ArrayList<String> colInput = new ArrayList<>();
            ArrayList<String> colOutput = new ArrayList<>();
            Element I;
            for (int x = 0; x < Inst.getElementsByTagName("Vars").getLength(); x++) {
                I = (Element) Inst.getElementsByTagName("Vars").item(x);
                Names.add(I.getAttribute("name"));
                Val.add(I.getAttribute("value"));
            }
            CParser_table.inputColNames = colInput;
            CParser_table.OutputColNames = colOutput;
            CS_Instantaneo.id2.add(Inst.getAttribute("id"));
            CS_Instantaneo In = new CS_Instantaneo(
                    Inst.getAttribute("id"),
                    2.2,
                    0.3,
                    2.2,
                    Inst.getAttribute("TransFunc"),
                    Names,
                    Val,
                    Boolean.parseBoolean(Inst.getAttribute("Origination")),
                    Boolean.parseBoolean(Inst.getAttribute("Destination")));
            cs.add(In);
            centroDeServicos.put(global, In);
            origem = Boolean.parseBoolean(Inst.getAttribute("Origination"));
            destino = Boolean.parseBoolean(Inst.getAttribute("Destination"));
            teste = Inst.getAttribute("id");
            if (origem) {
                Origens.add(In);
                CS_Instantaneo.O.add("o");
            } else {
                CS_Instantaneo.O.add("x");
            }
            if (destino) {
                Destinos.add(In);
                CS_Instantaneo.D.add("d");
            } else {
                CS_Instantaneo.D.add("x");
            }
        }
        // Realiza leitura dos icones Durativos
        for (int i = 0; i < docDurativo.getLength(); i++) {
            Element Dur = (Element) docDurativo.item(i);
            Element id = (Element) Dur.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            ArrayList<String> Namesd = new ArrayList<>();
            ArrayList<String> Vald = new ArrayList<>();
            Element I;
            for (int x = 0; x < Dur.getElementsByTagName("Vars").getLength(); x++) {
                I = (Element) Dur.getElementsByTagName("Vars").item(x);
                Namesd.add(I.getAttribute("name"));
                Vald.add(I.getAttribute("value"));
            }
            CS_Durativo In = new CS_Durativo(
                    Dur.getAttribute("id"),
                    2.2,
                    0.3,
                    2.2,
                    Dur.getAttribute("TransFunc"),
                    Double.parseDouble(Dur.getAttribute("TimeTransf")),
                    Namesd,
                    Vald,
                    Boolean.parseBoolean(Dur.getAttribute("Origination")),
                    Boolean.parseBoolean(Dur.getAttribute("Destination")));
            cs.add(In);
            centroDeServicos.put(global, In);
            origem = Boolean.parseBoolean(Dur.getAttribute("Origination"));
            destino = Boolean.parseBoolean(Dur.getAttribute("Destination"));
            if (origem) {
                Origens.add(In);
            }
            if (destino) {
                Destinos.add(In);
            }
        }
        // Cria os links e realiza a conexão entre os recursos
        for (int i = 0; i < doclinks.getLength(); i++) {
            Element link = (Element) doclinks.item(i);
            Element id = (Element) link.getElementsByTagName("icon_id").item(0);

            // Adiciona entrada e saida desta conexão
            Element connect = (Element) link.getElementsByTagName("connect").item(0);
            CS_Vertice Origem = (CS_Vertice) centroDeServicos.get(Integer.parseInt(connect.getAttribute("origination")));
            CS_Vertice Destino = (CS_Vertice) centroDeServicos.get(Integer.parseInt(connect.getAttribute("destination")));
            Origem.addConexoesSaida((CentroServico) Destino);
            Destino.addConexoesEntrada((CentroServico) Origem);
        }

        RedeDeFilas rdf = new RedeDeFilas(cs, Origens, Destinos);
        return rdf;
    }

    /**
     * Obtem a configuração da carga de trabalho contida em um modelo iconico
     *
     * @param modelo contem conteudo recuperado de um arquivo xml
     * @return carga de trabalho contida no modelo
     */
    public static GerarCarga newGerarCarga(Document modelo) {
        org.w3c.dom.NodeList cargas = modelo.getElementsByTagName("load");
        GerarCarga cargasConfiguracao = null;
        int numeroTarefas, TipoDistr;
        double min, max, avg2, AvgPoisson, AvgNormal, DesvPad, Tam, ArrivalTime;
        boolean distrIgual;
        // Realiza leitura da configuração de carga do modelo
        if (cargas.getLength() != 0) {
            Element cargaAux = (Element) cargas.item(0);
            cargas = cargaAux.getElementsByTagName("random");
            if (cargas.getLength() != 0) {
                Element aux = (Element) cargas.item(0);
                numeroTarefas = Integer.parseInt(aux.getAttribute("Tasks").toString());
                aux = (Element) cargaAux.getElementsByTagName("size").item(0);
                min = Double.parseDouble(aux.getAttribute("minimum").toString());
                max = Double.parseDouble(aux.getAttribute("maximum").toString());
                avg2 = Double.parseDouble(aux.getAttribute("average2").toString());
                AvgPoisson = Double.parseDouble(aux.getAttribute("averagePoisson").toString());
                AvgNormal = Double.parseDouble(aux.getAttribute("averageNormal").toString());
                DesvPad = Double.parseDouble(aux.getAttribute("standardDeviation").toString());
                TipoDistr = Integer.parseInt(aux.getAttribute("tipoDistr").toString());
                distrIgual = Boolean.parseBoolean(aux.getAttribute("distrIgual").toString());
                ArrivalTime = Double.parseDouble(aux.getAttribute("ArrivalTime").toString());
                cargasConfiguracao = new CargaRandom(numeroTarefas, min, max, avg2, AvgPoisson, AvgNormal, DesvPad, TipoDistr, distrIgual, ArrivalTime);
            }
            cargas = cargaAux.getElementsByTagName("uniform");
            if (cargas.getLength() != 0) {
                Element aux = (Element) cargas.item(0);
                numeroTarefas = Integer.parseInt(aux.getAttribute("Tasks").toString());
                aux = (Element) cargaAux.getElementsByTagName("size").item(0);
                Tam = Double.parseDouble(aux.getAttribute("Size").toString());
                distrIgual = Boolean.parseBoolean(aux.getAttribute("distrIgual").toString());
                ArrivalTime = Double.parseDouble(aux.getAttribute("ArrivalTime").toString());
                cargasConfiguracao = new CargaConst(numeroTarefas, Tam, distrIgual, ArrivalTime);
            }
        }
        return cargasConfiguracao;
    }

    public static void newGrade(Document descricao, Set<Vertice> vertices, Set<Aresta> arestas) {//leitura2: pega do documento e escreve na interface
        HashMap<Integer, Object> icones = new HashMap<>();
        NodeList FilaServidor = descricao.getElementsByTagName("FilaServidor");
        NodeList FilasServidor = descricao.getElementsByTagName("FilasServidor");
        NodeList FilaServidores = descricao.getElementsByTagName("FilaServidores");
        NodeList FilasServidores = descricao.getElementsByTagName("FilasServidores");
        NodeList ServidorInfinito = descricao.getElementsByTagName("ServidorInfinito");
        NodeList Instantaneo = descricao.getElementsByTagName("Instantaneo");
        NodeList Durativo = descricao.getElementsByTagName("Durativo");
        NodeList Link = descricao.getElementsByTagName("link");

        // Realiza leitura dos icones de FilaServidor
        for (int i = 0; i < FilaServidor.getLength(); i++) {
            Element FilaServ = (Element) FilaServidor.item(i);
            Element pos = (Element) FilaServ.getElementsByTagName("position").item(0);
            int x = Integer.parseInt(pos.getAttribute("x"));
            int y = Integer.parseInt(pos.getAttribute("y"));
            Element id = (Element) FilaServ.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            int local = Integer.parseInt(id.getAttribute("local"));
            int imgId = Integer.parseInt(id.getAttribute("imgId"));
            FilaServidor newFilaServ = new FilaServidor(x, y, local, global, imgId);
            newFilaServ.setSelected(false);
            vertices.add(newFilaServ);
            icones.put(global, newFilaServ);
            newFilaServ.getId().setNome(FilaServ.getAttribute("id"));
            ValidaValores.addNomeIcone(newFilaServ.getId().getNome());
            newFilaServ.setPreemptionTime(Double.parseDouble(FilaServ.getAttribute("PreemptionTime")));
            newFilaServ.setServiceRate(Double.parseDouble(FilaServ.getAttribute("ServiceRate")));
            newFilaServ.setScheduler(FilaServ.getAttribute("Scheduler"));
            newFilaServ.setProbServerFailure(Double.parseDouble(FilaServ.getAttribute("ProbServerFailure")));
            newFilaServ.setProbServiceFailure(Double.parseDouble(FilaServ.getAttribute("ProbServiceFailure")));
            newFilaServ.setCapacity(Integer.parseInt(FilaServ.getAttribute("Capacity")));
            newFilaServ.setOrigination(Boolean.parseBoolean(FilaServ.getAttribute("Origination")));
            newFilaServ.setDestination(Boolean.parseBoolean(FilaServ.getAttribute("Destination")));

        }
        // Realiza leitura dos icones de FilasServidor
        for (int i = 0; i < FilasServidor.getLength(); i++) {
            Element FilasServ = (Element) FilasServidor.item(i);
            Element pos = (Element) FilasServ.getElementsByTagName("position").item(0);
            int x = Integer.parseInt(pos.getAttribute("x"));
            int y = Integer.parseInt(pos.getAttribute("y"));
            Element id = (Element) FilasServ.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            int local = Integer.parseInt(id.getAttribute("local"));
            int imgId = Integer.parseInt(id.getAttribute("imgId"));
            FilasServidor newFilasServ = new FilasServidor(x, y, local, global, imgId);
            newFilasServ.setSelected(false);
            vertices.add(newFilasServ);
            icones.put(global, newFilasServ);
            newFilasServ.getId().setNome(FilasServ.getAttribute("id"));
            ValidaValores.addNomeIcone(newFilasServ.getId().getNome());
            newFilasServ.setPreemptionTime(Double.parseDouble(FilasServ.getAttribute("PreemptionTime")));
            newFilasServ.setServiceRate(Double.parseDouble(FilasServ.getAttribute("ServiceRate")));
            newFilasServ.setScheduler(FilasServ.getAttribute("Scheduler"));
            newFilasServ.setNumQueues(Integer.parseInt(FilasServ.getAttribute("NumQueues")));
            newFilasServ.setProbServerFailure(Double.parseDouble(FilasServ.getAttribute("ProbServerFailure")));
            newFilasServ.setProbServiceFailure(Double.parseDouble(FilasServ.getAttribute("ProbServiceFailure")));
            newFilasServ.setCapacity(Integer.parseInt(FilasServ.getAttribute("Capacity")));
            newFilasServ.setOrigination(Boolean.parseBoolean(FilasServ.getAttribute("Origination")));
            newFilasServ.setDestination(Boolean.parseBoolean(FilasServ.getAttribute("Destination")));
        }
        // Realiza leitura dos icones de FilasServidores
        for (int i = 0; i < FilasServidores.getLength(); i++) {
            Element FilasServs = (Element) FilasServidores.item(i);
            Element pos = (Element) FilasServs.getElementsByTagName("position").item(0);
            int x = Integer.parseInt(pos.getAttribute("x"));
            int y = Integer.parseInt(pos.getAttribute("y"));
            Element id = (Element) FilasServs.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            int local = Integer.parseInt(id.getAttribute("local"));
            int imgId = Integer.parseInt(id.getAttribute("imgId"));
            FilasServidores newFilasServs = new FilasServidores(x, y, local, global, imgId);
            newFilasServs.setSelected(false);
            vertices.add(newFilasServs);
            icones.put(global, newFilasServs);
            newFilasServs.getId().setNome(FilasServs.getAttribute("id"));
            ValidaValores.addNomeIcone(newFilasServs.getId().getNome());
            newFilasServs.setPreemptionTime(Double.parseDouble(FilasServs.getAttribute("PreemptionTime")));
            newFilasServs.setServiceRate(Double.parseDouble(FilasServs.getAttribute("ServiceRate")));
            newFilasServs.setScheduler(FilasServs.getAttribute("Scheduler"));
            newFilasServs.setNumQueues(Integer.parseInt(FilasServs.getAttribute("NumQueues")));
            newFilasServs.setNumServ(Integer.parseInt(FilasServs.getAttribute("NumServ")));
            newFilasServs.setProbServerFailure(Double.parseDouble(FilasServs.getAttribute("ProbServerFailure")));
            newFilasServs.setProbServiceFailure(Double.parseDouble(FilasServs.getAttribute("ProbServiceFailure")));
            newFilasServs.setCapacity(Integer.parseInt(FilasServs.getAttribute("Capacity")));
            newFilasServs.setOrigination(Boolean.parseBoolean(FilasServs.getAttribute("Origination")));
            newFilasServs.setDestination(Boolean.parseBoolean(FilasServs.getAttribute("Destination")));
        }
        // Realiza leitura dos FilaServidores
        for (int i = 0; i < FilaServidores.getLength(); i++) {
            Element FilaServs = (Element) FilaServidores.item(i);
            Element pos = (Element) FilaServs.getElementsByTagName("position").item(0);
            int x = Integer.parseInt(pos.getAttribute("x"));
            int y = Integer.parseInt(pos.getAttribute("y"));
            Element id = (Element) FilaServs.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            int local = Integer.parseInt(id.getAttribute("local"));
            int imgId = Integer.parseInt(id.getAttribute("imgId"));
            FilaServidores newFilaServs = new FilaServidores(x, y, local, global, imgId);
            newFilaServs.setSelected(false);
            vertices.add(newFilaServs);
            icones.put(global, newFilaServs);
            newFilaServs.getId().setNome(FilaServs.getAttribute("id"));
            ValidaValores.addNomeIcone(newFilaServs.getId().getNome());
            newFilaServs.setPreemptionTime(Double.parseDouble(FilaServs.getAttribute("PreemptionTime")));
            newFilaServs.setServiceRate(Double.parseDouble(FilaServs.getAttribute("ServiceRate")));
            newFilaServs.setScheduler(FilaServs.getAttribute("Scheduler"));
            newFilaServs.setNumServ(Integer.parseInt(FilaServs.getAttribute("NumServ")));
            newFilaServs.setProbServerFailure(Double.parseDouble(FilaServs.getAttribute("ProbServerFailure")));
            newFilaServs.setProbServiceFailure(Double.parseDouble(FilaServs.getAttribute("ProbServiceFailure")));
            newFilaServs.setCapacity(Integer.parseInt(FilaServs.getAttribute("Capacity")));
            newFilaServs.setOrigination(Boolean.parseBoolean(FilaServs.getAttribute("Origination")));
            newFilaServs.setDestination(Boolean.parseBoolean(FilaServs.getAttribute("Destination")));
        }
        // Realiza leitura dos Servidores Infinitos
        for (int i = 0; i < ServidorInfinito.getLength(); i++) {
            Element Serv = (Element) ServidorInfinito.item(i);
            Element pos = (Element) Serv.getElementsByTagName("position").item(0);
            int x = Integer.parseInt(pos.getAttribute("x"));
            int y = Integer.parseInt(pos.getAttribute("y"));
            Element id = (Element) Serv.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            int local = Integer.parseInt(id.getAttribute("local"));
            int imgId = Integer.parseInt(id.getAttribute("imgId"));
            ServidorInfinito newServ = new ServidorInfinito(x, y, local, global, imgId);
            newServ.setSelected(false);
            vertices.add(newServ);
            icones.put(global, newServ);
            newServ.getId().setNome(Serv.getAttribute("id"));
            ValidaValores.addNomeIcone(newServ.getId().getNome());
            newServ.setPreemptionTime(Double.parseDouble(Serv.getAttribute("PreemptionTime")));
            newServ.setServiceRate(Double.parseDouble(Serv.getAttribute("ServiceRate")));
            newServ.setProbServerFailure(Double.parseDouble(Serv.getAttribute("ProbServerFailure")));
            newServ.setProbServiceFailure(Double.parseDouble(Serv.getAttribute("ProbServiceFailure")));
            newServ.setOrigination(Boolean.parseBoolean(Serv.getAttribute("Origination")));
            newServ.setDestination(Boolean.parseBoolean(Serv.getAttribute("Destination")));
        }
        // Realiza leitura dos icones Instantaneos
        for (int i = 0; i < Instantaneo.getLength(); i++) {
            Element Inst = (Element) Instantaneo.item(i);
            Element pos = (Element) Inst.getElementsByTagName("position").item(0);
            int x = Integer.parseInt(pos.getAttribute("x"));
            int y = Integer.parseInt(pos.getAttribute("y"));
            Element id = (Element) Inst.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            int local = Integer.parseInt(id.getAttribute("local"));
            int imgId = Integer.parseInt(id.getAttribute("imgId"));
            int cont = Inst.getElementsByTagName("Vars").getLength();
            ArrayList<String> tipos = new ArrayList<>();
            ArrayList<String> var = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            ArrayList<String> colInput = new ArrayList<>();
            ArrayList<String> colOutput = new ArrayList<>();
            ArrayList<ArrayList<String>> inputTabela = new ArrayList<>();
            ArrayList<ArrayList<String>> outputTabela = new ArrayList<>();

            for (int c = 0; c < cont; c++) {
                Element vars = (Element) Inst.getElementsByTagName("Vars").item(c);
                tipos.add(vars.getAttribute("type"));
                var.add(vars.getAttribute("name"));
                values.add(vars.getAttribute("value"));
            }
            String TransFunc = Inst.getAttribute("TransFunc").toString();
            String diagramType = Inst.getAttribute("Diagram");
            Instantaneo newInst = new Instantaneo(x, y, local, global, var, tipos, 
                    TransFunc, imgId, colInput, colOutput, inputTabela, outputTabela, diagramType);
            newInst.setSelected(false);
            newInst.setOrigination(Boolean.parseBoolean(Inst.getAttribute("Origination")));
            newInst.setDestination(Boolean.parseBoolean(Inst.getAttribute("Destination")));
            vertices.add(newInst);
            icones.put(global, newInst);
            newInst.getId().setNome(Inst.getAttribute("id"));
            for (int c = 0; c < tipos.size(); c++) {
                newInst.setValores(values.get(c), c);
            }
        }
        // Realiza leitura dos icones durativos
        for (int i = 0; i < Durativo.getLength(); i++) {
            Element Dur = (Element) Durativo.item(i);
            Element pos = (Element) Dur.getElementsByTagName("position").item(0);
            int x = Integer.parseInt(pos.getAttribute("x"));
            int y = Integer.parseInt(pos.getAttribute("y"));
            Element id = (Element) Dur.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            int local = Integer.parseInt(id.getAttribute("local"));
            int imgId = Integer.parseInt(id.getAttribute("imgId"));
            int cont = Dur.getElementsByTagName("Vars").getLength();
            ArrayList<String> tipos = new ArrayList<>();
            ArrayList<String> var = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            for (int c = 0; c < cont; c++) {
                Element vars = (Element) Dur.getElementsByTagName("Vars").item(c);
                tipos.add(vars.getAttribute("type"));
                var.add(vars.getAttribute("name"));
                values.add(vars.getAttribute("value"));
            }
            String TransFunc = Dur.getAttribute("TransFunc");
            Durativo newDur = new Durativo(x, y, local, global, var, tipos, TransFunc, imgId);
            newDur.setSelected(false);
            newDur.setOrigination(Boolean.parseBoolean(Dur.getAttribute("Origination")));
            newDur.setDestination(Boolean.parseBoolean(Dur.getAttribute("Destination")));
            vertices.add(newDur);
            icones.put(global, newDur);
            newDur.getId().setNome(Dur.getAttribute("id"));
            newDur.setTimeTransf(Double.parseDouble(Dur.getAttribute("TimeTransf")));
            for (int c = 0; c < tipos.size(); c++) {
                newDur.setValores(values.get(c), c);
            }
        }
        // Realiza leitura dos icones de ligação
        for (int i = 0; i < Link.getLength(); i++) {
            Element link = (Element) Link.item(i);
            Element id = (Element) link.getElementsByTagName("icon_id").item(0);
            int global = Integer.parseInt(id.getAttribute("global"));
            int local = Integer.parseInt(id.getAttribute("local"));
            Element connect = (Element) link.getElementsByTagName("connect").item(0);
            Vertice origem = (Vertice) icones.get(Integer.parseInt(connect.getAttribute("origination")));
            Vertice destino = (Vertice) icones.get(Integer.parseInt(connect.getAttribute("destination")));
            Element pos = (Element) link.getElementsByTagName("position").item(0);
            int originPointX = Integer.parseInt(pos.getAttribute("originPointX"));
            int originPointY = Integer.parseInt(pos.getAttribute("originPointY"));
            pos = (Element) link.getElementsByTagName("position").item(1);
            int destinyPointX = Integer.parseInt(pos.getAttribute("destinyPointX"));
            int destinyPointY = Integer.parseInt(pos.getAttribute("destinyPointY"));
            
            DiagramComponentConnector originPoint = new DiagramComponentConnector(originPointX, originPointY,
                    0, false);
            DiagramComponentConnector destinyPoint = new DiagramComponentConnector(destinyPointX, destinyPointY,
                    0, true);

            Link lk = new Link(origem, destino, (Vertice) originPoint, (Vertice) destinyPoint, local, global);
            lk.setSelected(false);
            ((ItemGrade) origem).getConexoesSaida().add(lk);
            ((ItemGrade) destino).getConexoesEntrada().add(lk);
            arestas.add(lk);
            lk.getId().setNome(link.getAttribute("id"));
            ValidaValores.addNomeIcone(lk.getId().getNome());
        }
    }

    public static HashSet<String> newSetUsers(Document descricao) {
        NodeList owners = descricao.getElementsByTagName("owner");
        HashSet<String> usuarios = new HashSet<>();
        // Realiza leitura dos usuários/proprietários do modelo
        for (int i = 0; i < owners.getLength(); i++) {
            Element owner = (Element) owners.item(i);
            usuarios.add(owner.getAttribute("id"));
        }
        return usuarios;
    }

    public static List<String> newListUsers(Document descricao) {
        NodeList owners = descricao.getElementsByTagName("owner");
        List<String> usuarios = new ArrayList<>();
        // Realiza leitura dos usuários/proprietários do modelo
        for (int i = 0; i < owners.getLength(); i++) {
            Element owner = (Element) owners.item(i);
            usuarios.add(owner.getAttribute("id"));
        }
        return usuarios;
    }

    public void addUsers(Collection<String> usuarios) {
        for (String user : usuarios) {
            Element owner = descricao.createElement("owner");
            owner.setAttribute("id", user);
            system.appendChild(owner);
        }
    }

    public void addInternet(int x, int y, int idLocal, int idGlobal, String nome,
            double banda, double ocupacao, double latencia) {//mais simples, escrita da configuração para o document
        Element aux;
        Element posicao = descricao.createElement("position");
        posicao.setAttribute("x", Integer.toString(x));
        posicao.setAttribute("y", Integer.toString(y));
        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", Integer.toString(idGlobal));
        icon_id.setAttribute("local", Integer.toString(idLocal));

        aux = descricao.createElement("internet");
        aux.setAttribute("bandwidth", Double.toString(banda));
        aux.setAttribute("load", Double.toString(ocupacao));
        aux.setAttribute("latency", Double.toString(latencia));

        aux.setAttribute("id", nome);
        aux.appendChild(posicao);
        aux.appendChild(icon_id);
        system.appendChild(aux);
    }

    public void addCluster(Integer x, Integer y, Integer idLocal, Integer idGlobal, String nome,
            Integer numeroEscravos, Double poderComputacional, Integer numeroNucleos,
            Double memoriaRAM, Double discoRigido,
            Double banda, Double latencia,
            String algoritmo, String proprietario, Boolean mestre) {
        Element aux;
        Element posicao = descricao.createElement("position");
        posicao.setAttribute("x", x.toString());
        posicao.setAttribute("y", y.toString());
        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", idGlobal.toString());
        icon_id.setAttribute("local", idLocal.toString());

        aux = descricao.createElement("cluster");
        aux.setAttribute("nodes", numeroEscravos.toString());
        aux.setAttribute("power", poderComputacional.toString());
        aux.setAttribute("bandwidth", banda.toString());
        aux.setAttribute("latency", latencia.toString());
        aux.setAttribute("scheduler", algoritmo);
        aux.setAttribute("owner", proprietario);
        aux.setAttribute("master", mestre.toString());

        aux.setAttribute("id", nome);
        aux.appendChild(posicao);
        aux.appendChild(icon_id);
        aux.appendChild(newCharacteristic(poderComputacional, numeroNucleos, memoriaRAM, discoRigido));
        system.appendChild(aux);
    }

    public void addMachine(Integer x, Integer y, Integer idLocal, Integer idGlobal, String nome,
            Double poderComputacional, Double ocupacao, String algoritmo, String proprietario,
            Integer numeroNucleos, Double memoriaRAM, Double discoRigido,
            boolean mestre, Collection<Integer> escravos) {
        Element aux;
        Element posicao = descricao.createElement("position");
        posicao.setAttribute("x", x.toString());
        posicao.setAttribute("y", y.toString());
        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", idGlobal.toString());
        icon_id.setAttribute("local", idLocal.toString());

        aux = descricao.createElement("machine");
        aux.setAttribute("power", Double.toString(poderComputacional));
        aux.setAttribute("load", Double.toString(ocupacao));
        aux.setAttribute("owner", proprietario);
        if (mestre) {
            //preenche escravos
            Element master = descricao.createElement("master");
            master.setAttribute("scheduler", algoritmo);
            for (Integer escravo : escravos) {
                Element slave = descricao.createElement("slave");
                slave.setAttribute("id", escravo.toString());
                master.appendChild(slave);
            }
            aux.appendChild(master);
        }
        aux.setAttribute("id", nome);
        aux.appendChild(posicao);
        aux.appendChild(icon_id);
        aux.appendChild(newCharacteristic(poderComputacional, numeroNucleos, memoriaRAM, discoRigido));
        system.appendChild(aux);
    }

    public void addLink(int x0, int y0, int x1, int y1, Vertice originPoint,
            Vertice destinyPoint, int idLocal, int idGlobal, String nome,
            int origem, int destino) {
        Element aux;
        Element posicao = descricao.createElement("position");
        posicao.setAttribute("originPointX", Integer.toString(((DiagramComponentConnector) originPoint).getX()));
        posicao.setAttribute("originPointY", Integer.toString(((DiagramComponentConnector) originPoint).getY()));
        posicao.setAttribute("x", Integer.toString(x0));
        posicao.setAttribute("y", Integer.toString(y0));

        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", Integer.toString(idGlobal));
        icon_id.setAttribute("local", Integer.toString(idLocal));

        aux = descricao.createElement("link");

        Element connect = descricao.createElement("connect");
        connect.setAttribute("origination", Integer.toString(origem));
        connect.setAttribute("destination", Integer.toString(destino));
        aux.appendChild(connect);
        aux.appendChild(posicao);
        posicao = descricao.createElement("position");
        posicao.setAttribute("destinyPointX", Integer.toString(((DiagramComponentConnector) destinyPoint).getX()));
        posicao.setAttribute("destinyPointY", Integer.toString(((DiagramComponentConnector) destinyPoint).getY()));
        posicao.setAttribute("x", Integer.toString(x1));
        posicao.setAttribute("y", Integer.toString(y1));

        aux.setAttribute("id", nome);
        aux.appendChild(posicao);
        aux.appendChild(icon_id);
        system.appendChild(aux);
    }

    public void addFilaServidor(int x, int y, int idLocal, int idGlobal, int imgId, String nome,
            double serviceRate, double PreemptionTime, double DelayRate, String scheduler, int capacity, double probServerFailure,
            double probServiceFailure, boolean fcapacity, boolean Origination, boolean Destination) {
        Element aux;
        Element posicao = descricao.createElement("position");
        posicao.setAttribute("x", Integer.toString(x));
        posicao.setAttribute("y", Integer.toString(y));
        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", Integer.toString(idGlobal));
        icon_id.setAttribute("local", Integer.toString(idLocal));
        icon_id.setAttribute("imgId", Integer.toString(imgId));

        aux = descricao.createElement("FilaServidor");
        aux.setAttribute("ServiceRate", Double.toString(serviceRate));
        aux.setAttribute("DelayRate", "0.0");
        aux.setAttribute("PreemptionTime", Double.toString(PreemptionTime));
        aux.setAttribute("Scheduler", scheduler);
        aux.setAttribute("Capacity", Integer.toString(capacity));
        aux.setAttribute("ProbServerFailure", Double.toString(probServerFailure));
        aux.setAttribute("ProbServiceFailure", Double.toString(probServiceFailure));
        aux.setAttribute("FCapacity", Boolean.toString(fcapacity));
        aux.setAttribute("Origination", Boolean.toString(Origination));
        aux.setAttribute("Destination", Boolean.toString(Destination));

        aux.appendChild(posicao);
        aux.setAttribute("id", nome);
        aux.appendChild(icon_id);
        system.appendChild(aux);
    }

    public void addFilasServidor(int x, int y, int idLocal, int idGlobal, int imgId, String nome,
            double serviceRate, double delayRate, int Num_filas, double PreemptionTime, String scheduler,
            int capacity, double probServerFailure, double probServiceFailure, boolean fcapacity,
            boolean Origination, boolean Destination) {

        Element aux;

        Element posicao = descricao.createElement("position");
        posicao.setAttribute("x", Integer.toString(x));
        posicao.setAttribute("y", Integer.toString(y));

        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", Integer.toString(idGlobal));
        icon_id.setAttribute("local", Integer.toString(idLocal));
        icon_id.setAttribute("imgId", Integer.toString(imgId));

        aux = descricao.createElement("FilasServidor");
        aux.setAttribute("ServiceRate", Double.toString(serviceRate));
        aux.setAttribute("DelayRate", "0.0");
        aux.setAttribute("PreemptionTime", Double.toString(PreemptionTime));
        aux.setAttribute("Scheduler", scheduler);
        aux.setAttribute("NumQueues", Integer.toString(Num_filas));
        aux.setAttribute("Capacity", Integer.toString(capacity));
        aux.setAttribute("ProbServerFailure", Double.toString(probServerFailure));
        aux.setAttribute("ProbServiceFailure", Double.toString(probServiceFailure));
        aux.setAttribute("FCapacity", Boolean.toString(fcapacity));
        aux.setAttribute("Origination", Boolean.toString(Origination));
        aux.setAttribute("Destination", Boolean.toString(Destination));

        aux.appendChild(posicao);

        aux.setAttribute("id", nome);
        aux.appendChild(icon_id);
        system.appendChild(aux);
    }

    public void addFilaServidores(int x, int y, int idLocal, int idGlobal, int imgId, String nome,
            double serviceRate, double delayRate, int Num_serv, double PreemptionTime, String scheduler,
            int capacity, double probServerFailure, double probServiceFailure, boolean fcapacity,
            boolean Origination, boolean Destination) {
        Element aux;

        Element posicao = descricao.createElement("position");
        posicao.setAttribute("x", Integer.toString(x));
        posicao.setAttribute("y", Integer.toString(y));

        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", Integer.toString(idGlobal));
        icon_id.setAttribute("local", Integer.toString(idLocal));
        icon_id.setAttribute("imgId", Integer.toString(imgId));

        aux = descricao.createElement("FilaServidores");
        aux.setAttribute("ServiceRate", Double.toString(serviceRate));
        aux.setAttribute("DelayRate", "0.0");
        aux.setAttribute("PreemptionTime", Double.toString(PreemptionTime));
        aux.setAttribute("Scheduler", scheduler);
        aux.setAttribute("NumServ", Integer.toString(Num_serv));
        aux.setAttribute("Capacity", Integer.toString(capacity));
        aux.setAttribute("ProbServerFailure", Double.toString(probServerFailure));
        aux.setAttribute("ProbServiceFailure", Double.toString(probServiceFailure));
        aux.setAttribute("FCapacity", Boolean.toString(fcapacity));
        aux.setAttribute("Origination", Boolean.toString(Origination));
        aux.setAttribute("Destination", Boolean.toString(Destination));

        aux.appendChild(posicao);

        aux.setAttribute("id", nome);
        aux.appendChild(icon_id);
        system.appendChild(aux);
    }

    public void addFilasServidores(int x, int y, int idLocal, int idGlobal, int imgId, String nome,
            double serviceRate, double delayRate, int Num_serv, int Num_Filas, double PreemptionTime, String scheduler,
            int capacity, double probServerFailure, double probServiceFailure, boolean fcapacity,
            boolean Origination, boolean Destination) {

        Element aux;

        Element posicao = descricao.createElement("position");
        posicao.setAttribute("x", Integer.toString(x));
        posicao.setAttribute("y", Integer.toString(y));

        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", Integer.toString(idGlobal));
        icon_id.setAttribute("local", Integer.toString(idLocal));
        icon_id.setAttribute("imgId", Integer.toString(imgId));

        aux = descricao.createElement("FilasServidores");
        aux.setAttribute("ServiceRate", Double.toString(serviceRate));
        aux.setAttribute("DelayRate", "0.0");
        aux.setAttribute("PreemptionTime", Double.toString(PreemptionTime));
        aux.setAttribute("Scheduler", scheduler);
        aux.setAttribute("NumServ", Integer.toString(Num_serv));
        aux.setAttribute("NumQueues", Integer.toString(Num_Filas));
        aux.setAttribute("Capacity", Integer.toString(capacity));
        aux.setAttribute("ProbServerFailure", Double.toString(probServerFailure));
        aux.setAttribute("ProbServiceFailure", Double.toString(probServiceFailure));
        aux.setAttribute("FCapacity", Boolean.toString(fcapacity));
        aux.setAttribute("Origination", Boolean.toString(Origination));
        aux.setAttribute("Destination", Boolean.toString(Destination));

        aux.appendChild(posicao);

        aux.setAttribute("id", nome);
        aux.appendChild(icon_id);
        system.appendChild(aux);
    }

    public void addServidorInfinito(int x, int y, int idLocal, int idGlobal, int imgId, String nome,
            double serviceRate, double delayRate, double PreemptionTime,
            double probServerFailure, double probServiceFailure,
            boolean Origination, boolean Destination) {

        Element aux;

        Element posicao = descricao.createElement("position");
        posicao.setAttribute("x", Integer.toString(x));
        posicao.setAttribute("y", Integer.toString(y));

        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", Integer.toString(idGlobal));
        icon_id.setAttribute("local", Integer.toString(idLocal));
        icon_id.setAttribute("imgId", Integer.toString(imgId));

        aux = descricao.createElement("ServidorInfinito");
        aux.setAttribute("ServiceRate", Double.toString(serviceRate));
        aux.setAttribute("DelayRate", "0.0");
        aux.setAttribute("PreemptionTime", Double.toString(PreemptionTime));
        aux.setAttribute("ProbServerFailure", Double.toString(probServerFailure));
        aux.setAttribute("ProbServiceFailure", Double.toString(probServiceFailure));
        aux.setAttribute("Origination", Boolean.toString(Origination));
        aux.setAttribute("Destination", Boolean.toString(Destination));

        aux.appendChild(posicao);

        aux.setAttribute("id", nome);
        aux.appendChild(icon_id);
        system.appendChild(aux);
    }

    public void addInstantaneo(int x, int y, int idLocal, int idGlobal, int imgId, String diagramType, String nome,
            String Trans_Func, ArrayList<String> Tipos, ArrayList<String> Vars, ArrayList<String> Value,
            boolean Origination, boolean Destination) {
        Element aux;

        Element posicao = descricao.createElement("position");
        posicao.setAttribute("x", Integer.toString(x));
        posicao.setAttribute("y", Integer.toString(y));

        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", Integer.toString(idGlobal));
        icon_id.setAttribute("local", Integer.toString(idLocal));
        icon_id.setAttribute("imgId", Integer.toString(imgId));

        aux = descricao.createElement("Instantaneo");
        aux.setAttribute("TransFunc", Trans_Func);
        aux.setAttribute("Origination", Boolean.toString(Origination));
        aux.setAttribute("Destination", Boolean.toString(Destination));
        aux.setAttribute("Diagram", diagramType);

        for (int i = 0; i < Vars.size(); i++) {
            Element vars = descricao.createElement("Vars");
            vars.setAttribute("type", Tipos.get(i));
            vars.setAttribute("name", Vars.get(i));
            vars.setAttribute("value", Value.get(i));
            aux.appendChild(vars);
        }

        aux.appendChild(posicao);

        aux.setAttribute("id", nome);
        aux.appendChild(icon_id);
        system.appendChild(aux);
    }

    public void addDurativo(int x, int y, int idLocal, int idGlobal, int imgId, String nome,
            String Trans_Func, double Time_Trans, ArrayList<String> Tipos, ArrayList<String> Vars, ArrayList<String> Value,
            boolean Origination, boolean Destination) {
        Element aux;

        Element posicao = descricao.createElement("position");
        posicao.setAttribute("x", Integer.toString(x));
        posicao.setAttribute("y", Integer.toString(y));

        Element icon_id = descricao.createElement("icon_id");
        icon_id.setAttribute("global", Integer.toString(idGlobal));
        icon_id.setAttribute("local", Integer.toString(idLocal));
        icon_id.setAttribute("imgId", Integer.toString(imgId));

        aux = descricao.createElement("Durativo");
        aux.setAttribute("TimeTransf", Double.toString(Time_Trans));
        aux.setAttribute("TransFunc", Trans_Func);
        aux.setAttribute("Origination", Boolean.toString(Origination));
        aux.setAttribute("Destination", Boolean.toString(Destination));

        for (int i = 0; i < Vars.size(); i++) {
            Element vars = descricao.createElement("Vars");
            vars.setAttribute("type", Tipos.get(i));
            vars.setAttribute("name", Vars.get(i));
            vars.setAttribute("value", Value.get(i));
            aux.appendChild(vars);
        }

        aux.appendChild(posicao);

        aux.setAttribute("id", nome);
        aux.appendChild(icon_id);
        system.appendChild(aux);
    }

    public void setLoadUniform(int numeroTarefas, double Tam, boolean distrIgual, double ArrivalTime) {
        if (load == null) {
            load = descricao.createElement("load");
            system.appendChild(load);
        }
        Element xmlTasks = descricao.createElement("uniform");
        xmlTasks.setAttribute("Tasks", Integer.toString(numeroTarefas));

        Element size = descricao.createElement("size");
        size.setAttribute("Size", Double.toString(Tam));
        size.setAttribute("distrIgual", Boolean.toString(distrIgual));
        size.setAttribute("ArrivalTime", Double.toString(ArrivalTime));
        xmlTasks.appendChild(size);

        load.appendChild(xmlTasks);

    }

    public void setLoadRandom(Integer numeroTarefas, double max, double avg2, double min,
            double avgPoisson, double avgNormal, double DesvPad, int TipoDistr, boolean distrIgual, double ArrivalTime) {
        if (load == null) {
            load = descricao.createElement("load");
            system.appendChild(load);
        }
        Element xmlRandom = descricao.createElement("random");
        xmlRandom.setAttribute("Tasks", numeroTarefas.toString());

        Element size = descricao.createElement("size");
        size.setAttribute("maximum", Double.toString(max));
        size.setAttribute("average2", Double.toString(avg2));
        size.setAttribute("minimum", Double.toString(min));
        size.setAttribute("averagePoisson", Double.toString(avgPoisson));
        size.setAttribute("averageNormal", Double.toString(avgNormal));
        size.setAttribute("standardDeviation", Double.toString(DesvPad));
        size.setAttribute("tipoDistr", Integer.toString(TipoDistr));
        size.setAttribute("distrIgual", Boolean.toString(distrIgual));
        size.setAttribute("ArrivalTime", Double.toString(ArrivalTime));

        xmlRandom.appendChild(size);
        load.appendChild(xmlRandom);
    }

    public Document getDescricao() {
        return descricao;
    }

    private Node newCharacteristic(Double poderComputacional, Integer numeroNucleos, Double memoriaRAM, Double discoRigido) {
        Element characteristic = descricao.createElement("characteristic");
        Element process = descricao.createElement("process");
        process.setAttribute("power", poderComputacional.toString());
        process.setAttribute("number", numeroNucleos.toString());
        Element memory = descricao.createElement("memory");
        memory.setAttribute("size", memoriaRAM.toString());
        Element hard_disk = descricao.createElement("hard_disk");
        hard_disk.setAttribute("size", discoRigido.toString());
        characteristic.appendChild(process);
        characteristic.appendChild(memory);
        characteristic.appendChild(hard_disk);
        return characteristic;
    }
}
