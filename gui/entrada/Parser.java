package yasc.gui.entrada;

import java.util.ArrayList;

public class Parser {
    private String id;
    private String graphicRepresentation; // Pode ser uma imagem (com seu caminho) ou um desenho
    private boolean nDeterministico;
    private boolean instantaneo;
    private boolean durativo;
    private boolean funcTrans;
    private boolean fila;
    private boolean form;
    private String filaTipo;
    private String funcaoTrans;
    private String escalonador;
    private String inputTrans;
    private String outputTrans;
    private String logicOperation;
    // Armazena os valores da matriz em uma única tabela (é provisório, será trocado pelo conteúdo abaixo)
    private ArrayList<ArrayList<String>> valorMatriz;
    //-------------------------------------------------------------------------------------
    // Armazena valores de output da tabela
    public static ArrayList<ArrayList<String>> outputTable;
    // Armazena valores de input da tabela
    public static ArrayList<ArrayList<String>> inputTable;
    // Armazena os nomes das colunas que representam as variaveis de saida
    public static ArrayList<String> outputColunas;
    // Armazena os nomes das colunas que representam as variaveis de entrada
    public static ArrayList<String> inputColunas;
    //---------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------------
    //SIM! Variáveis globais para passagens de parâmetro para o Cparser_table
    // Armazena valores de output da tabela
    public static ArrayList<ArrayList<String>> outTable;
    // Armazena valores de input da tabela
    public static ArrayList<ArrayList<String>> inTable;
    // Armazena os nomes das colunas que representam as variaveis de saida
    public static ArrayList<String> colunasOut;
    // Armazena os nomes das colunas que representam as variaveis de entrada
    public static ArrayList<String> colunasIn;
    //---------------------------------------------------------------------------------------

    private final ArrayList<String> vars;
    private final ArrayList<String> tipos;
    private ArrayList<String> values;
    private ArrayList<String> labels;
    private ArrayList<String> metricas;
    private final ArrayList<String> listaColunas;
    public static String ID;

    public Parser(String id,
            String graphicRepresentation,
            ArrayList<ArrayList<String>> inTable,
            ArrayList<ArrayList<String>> outTable,
            ArrayList<String> inColunas,
            ArrayList<String> outColunas,
            ArrayList<ArrayList<String>> valorMatriz,
            boolean instantaneo,
            boolean durativo,
            boolean funcTrans,
            boolean fila,
            boolean nDeterministico,
            boolean form,
            String funcaoTrans,
            String filaTipo,
            String inputTrans,
            String outputTrans,
            String logicOperation,
            ArrayList<String> listaColunas,
            ArrayList<String> vars,
            ArrayList<String> tipos,
            ArrayList<String> labels,
            ArrayList<String> metricas) {
        this.id = id;
        ID = id;
        this.graphicRepresentation = graphicRepresentation;
        this.instantaneo = instantaneo;
        this.durativo = durativo;
        this.funcTrans = funcTrans;
        this.fila = fila;
        this.filaTipo = filaTipo;
        this.nDeterministico = nDeterministico;
        this.funcaoTrans = funcaoTrans;
        this.vars = vars;
        this.tipos = tipos;
        this.labels = labels;
        this.metricas = metricas;
        this.inputTrans = inputTrans;
        this.outputTrans = outputTrans;
        this.listaColunas = listaColunas;
        this.valorMatriz = valorMatriz;
        inputColunas = inColunas;
        outputColunas = outColunas;
        inputTable = inTable;
        outputTable = outTable;
        colunasIn = inColunas;
        colunasOut = outColunas;
    }

    public Parser() {
        ID = "";
        this.vars = new ArrayList<>();
        this.tipos = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.metricas = new ArrayList<>();
        this.listaColunas = new ArrayList<>();
        this.valorMatriz = null;
        inputColunas = null;
        outputColunas = null;
        inputTable = null;
        outputTable = null;
        colunasIn = null;
        colunasOut = null;
    }
    
    public boolean isNDeterministico() {
        return nDeterministico;
    }

    public void setNDeterministico(boolean NDeterministico) {
        this.nDeterministico = NDeterministico;
    }

    public String getEscalonador() {
        return escalonador;
    }

    public void setEscalonador(String escalonador) {
        this.escalonador = escalonador;
    }

    public ArrayList<String> getMetricas() {
        return metricas;
    }

    public void setMetricas(ArrayList<String> Metricas) {
        this.metricas = Metricas;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> Labels) {
        this.labels = Labels;
    }

    public ArrayList<String> getVars() {
        return vars;
    }

    public void setVars(String Vars) {
        this.vars.add(Vars);
    }

    public ArrayList<String> getTipos() {
        return tipos;
    }
    
    // Define o tipo de uma variável da função de transferência
    public void setTipos(String name) {
        tipos.add(name);
    }

    // Define o nome das variáveis da função de transferência
    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(String Values) {
        this.values.add(Values);
    }

    public String getFilaTipo() {
        return filaTipo;
    }

    public void setFilaTipo(String FilaTipo) {
        this.filaTipo = FilaTipo;
    }

    public boolean isInstantaneo() {
        return instantaneo;
    }

    public void setInstantaneo(boolean Instantaneo) {
        this.instantaneo = Instantaneo;
    }

    public boolean isDurativo() {
        return durativo;
    }

    public void setDurativo(boolean Durativo) {
        this.durativo = Durativo;
    }

    public boolean isFuncTrans() {
        return funcTrans;
    }

    public void setFuncTrans(boolean FuncTrans) {
        this.funcTrans = FuncTrans;
    }

    public boolean isFila() {
        return fila;
    }

    public void setFila(boolean Fila) {
        this.fila = Fila;
    }

    public String getFuncaoTrans() {
        return funcaoTrans;
    }

    public void setFuncaoTrans(String FuncaoTrans) {
        this.funcaoTrans = FuncaoTrans;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGraphicRepresentation() {
        return graphicRepresentation;
    }

    public void setGraphicRepresentation(String graphicRepresentation) {
        this.graphicRepresentation = graphicRepresentation;
    }

    // Funções para pegar o InputTrans e OutputTrans e escrever na tela final
    public String getInputTrans() {
        return inputTrans;
    }

    public void setInputTrans(String InputTrans) {
        this.inputTrans = InputTrans;
        System.out.println("Inputtrans: " + InputTrans);
    }

    public String getOutputTrans() {
        return outputTrans;
    }

    public void setOutputTrans(String OutputTrans) {
        this.outputTrans = OutputTrans;
        System.out.println("Outputtrans: " + OutputTrans);
    }

    public void setLogicOperation(String LogicOperation) {
        this.logicOperation = LogicOperation;
    }

    public String getLogicOperation() {
        return logicOperation;
    }

    public boolean isForm() {
        return form;
    }

    public void setForm(boolean Form) {
        this.form = Form;
    }

    public void setColunas(String var) {
        this.listaColunas.add(var);
    }

    public ArrayList<String> getNomeColunas() {
        return listaColunas;
    }

    public void setInputColunas(String var) {
        inputColunas.add(var);
    }

    public ArrayList<String> getInputColunas() {
        return inputColunas;
    }

    public void setOutputColunas(String var) {
        outputColunas.add(var);
    }

    public ArrayList<String> getOutputColunas() {
        return outputColunas;
    }

    public void setValorMatriz(ArrayList<ArrayList<String>> matriz) {
        valorMatriz = matriz;
    }

    public String ValorMatriz(int i, int j) {
        ArrayList<String> secArray;
        secArray = valorMatriz.get(i);

        return secArray.get(j);
    }

    public void setInputMatriz(ArrayList<ArrayList<String>> matriz) {
        inputTable = matriz;
    }

    public ArrayList<ArrayList<String>> getInputMatriz() {
        return inputTable;
    }

    public void setOutputMatriz(ArrayList<ArrayList<String>> matriz) {
        outputTable = matriz;
    }

    public ArrayList<ArrayList<String>> getOutputMatriz() {
        return outputTable;
    }

    public ArrayList<String> getNumArray() {
        ArrayList<String> num = valorMatriz.get(1);
        return num;
    }
}
