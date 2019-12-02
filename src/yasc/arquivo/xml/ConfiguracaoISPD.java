package yasc.arquivo.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Classe que controla arquivo de configuração do iSPD
 */
public class ConfiguracaoISPD {

    public static final byte DEFAULT = 0;
    public static final byte OPTIMISTIC = 1;
    public static final byte GRAPHICAL = 2;
    public static final String FILENAME = "configuration.xml";
    private File configurationFile;
    private byte simulationMode;
    private Integer numberOfThreads;
    private Integer numberOfSimulations;
    private Boolean createProcessingChart;
    private Boolean createCommunicationChart;
    private Boolean createUserThroughTimeChart;
    private Boolean createMachineThroughTimeChart;
    private Boolean createTaskThroughTimeChart;
    private File lastFile;

    /**
     * Busca se arquivo de configuração existe e carrega valores salvos
     */
    public ConfiguracaoISPD() {
        configurationFile = new File(ConfiguracaoISPD.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        if (!configurationFile.getName().endsWith(".jar")) {
            configurationFile = new File(FILENAME);
        } else {
            String file = configurationFile.getParent() + "/" + FILENAME;
            configurationFile = new File(file);
        }
        try {
            Document doc = ManipuladorXML.ler(configurationFile, "configurationFile.dtd");
            load(doc);
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            //carrega valores padrão
            simulationMode = DEFAULT;
            lastFile = configurationFile.getParentFile();
            numberOfThreads = 1;
            numberOfSimulations = 1;
            createProcessingChart = true;
            createCommunicationChart = true;
            createUserThroughTimeChart = true;
            createMachineThroughTimeChart = false;
            createTaskThroughTimeChart = false;
        }
    }

    /**
     * Retorna o tipo de simulação que será utilizada
     *
     * @return pode indicar:
     * [DEFAULT] [OPTIMISTIC] [GRAPHICAL]
     */
    public int getSimulationMode() {
        return simulationMode;
    }

    private void load(Document doc) {
        Element ispd = (Element) doc.getElementsByTagName("yasc").item(0);
        Element chart = (Element) ispd.getElementsByTagName("chart_create").item(0);
        Element files = (Element) ispd.getElementsByTagName("model_open").item(0);
        ispd.getAttribute("simulation_mode");
        String modo = ispd.getAttribute("simulation_mode");
        
        if ("default".equals(modo)) {
            simulationMode = DEFAULT;
        } else if ("optimistic".equals(modo)) {
            simulationMode = OPTIMISTIC;
        } else {
            simulationMode = GRAPHICAL;
        }
        numberOfThreads = Integer.valueOf(ispd.getAttribute("number_threads"));
        numberOfSimulations = Integer.valueOf(ispd.getAttribute("number_simulations"));
        createProcessingChart = Boolean.valueOf(chart.getAttribute("processing"));
        createCommunicationChart = Boolean.valueOf(chart.getAttribute("communication"));
        createUserThroughTimeChart = Boolean.valueOf(chart.getAttribute("user_time"));
        createMachineThroughTimeChart = Boolean.valueOf(chart.getAttribute("machine_time"));
        createTaskThroughTimeChart = Boolean.valueOf(chart.getAttribute("task_time"));
        String endereco = files.getAttribute("last_file");
        if (endereco != null && !"".equals(endereco)) {
            lastFile = new File(endereco);
        }
    }

    /**
     * Salva modificações nas configurações do iSPD
     */
    public void save() {
        Document doc = ManipuladorXML.novoDocumento();
        Element ispd = doc.createElement("yasc");
        switch (simulationMode) {
            case DEFAULT:
                ispd.setAttribute("simulation_mode", "default");
                break;
            case OPTIMISTIC:
                ispd.setAttribute("simulation_mode", "optimistic");
                break;
            case GRAPHICAL:
                ispd.setAttribute("simulation_mode", "graphical");
                break;
        }
        ispd.setAttribute("number_simulations", numberOfSimulations.toString());
        ispd.setAttribute("number_threads", numberOfThreads.toString());
        Element chart = doc.createElement("chart_create");
        chart.setAttribute("processing", createProcessingChart.toString());
        chart.setAttribute("communication", createCommunicationChart.toString());
        chart.setAttribute("user_time", createUserThroughTimeChart.toString());
        chart.setAttribute("machine_time", createMachineThroughTimeChart.toString());
        chart.setAttribute("task_time", createTaskThroughTimeChart.toString());
        ispd.appendChild(chart);
        Element files = doc.createElement("model_open");
        if (lastFile != null) {
            files.setAttribute("last_file", lastFile.getAbsolutePath());
        }
        ispd.appendChild(files);
        doc.appendChild(ispd);
        ManipuladorXML.escrever(doc, configurationFile, "configurationFile.dtd", false);
    }

    public Integer getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(Integer numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public Integer getNumberOfSimulations() {
        return numberOfSimulations;
    }

    public void setNumberOfSimulations(Integer numberOfSimulations) {
        this.numberOfSimulations = numberOfSimulations;
    }

    public Boolean getCreateProcessingChart() {
        return createProcessingChart;
    }

    public void setCreateProcessingChart(Boolean createProcessingPieChart) {
        this.createProcessingChart = createProcessingPieChart;
    }

    public Boolean getCreateCommunicationChart() {
        return createCommunicationChart;
    }

    public void setCreateCommunicationChart(Boolean createCommunicationPieChart) {
        this.createCommunicationChart = createCommunicationPieChart;
    }

    public Boolean getCreateUserThroughTimeChart() {
        return createUserThroughTimeChart;
    }

    public void setCreateUserThroughTimeChart(Boolean createUserThroughTimeChart) {
        this.createUserThroughTimeChart = createUserThroughTimeChart;
    }

    public Boolean getCreateMachineThroughTimeChart() {
        return createMachineThroughTimeChart;
    }

    public void setCreateMachineThroughTimeChart(Boolean createMachineThroughTimeChart) {
        this.createMachineThroughTimeChart = createMachineThroughTimeChart;
    }

    public Boolean getCreateTaskThroughTimeChart() {
        return createTaskThroughTimeChart;
    }

    public void setCreateTaskThroughTimeChart(Boolean createTaskThroughTimeChart) {
        this.createTaskThroughTimeChart = createTaskThroughTimeChart;
    }

    public void setSimulationMode(byte simulationMode) {
        this.simulationMode = simulationMode;
    }

    public File getLastFile() {
        return lastFile;
    }

    public void setLastFile(File lastDir) {
        if (lastDir != null) {
            this.lastFile = lastDir;
        }
    }
}
