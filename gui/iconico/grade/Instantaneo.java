package yasc.gui.iconico.grade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import yasc.Main;
import java.util.Set;
import yasc.arquivo.CParser_form.CParserForm;

public class Instantaneo extends Item implements ItemGrade {
    private String transFunc;
    private final String diagramType;
    private final ArrayList<String> vars;
    private final ArrayList<String> tipos;
    private final ArrayList<String> valores = new ArrayList<>();
    public static int j = 0;
    public static int v = 0;
    public ArrayList<String> colInput;
    public ArrayList<String> colOutput;
    public ArrayList<ArrayList<String>> outputTabela;
    public ArrayList<ArrayList<String>> inputTabela;
    private ArrayList<ArrayList<String>> entradas;
    
    public Instantaneo(int x, int y, int idLocal, int idGlobal, ArrayList<String> vars, 
            ArrayList<String> tipos, String transFunc, int imgId, ArrayList<String> colInput, 
            ArrayList<String> colOutput, ArrayList<ArrayList<String>> inputTabela, 
            ArrayList<ArrayList<String>> outputTabela, String diagramType)
    {
        super(x, y, idLocal, idGlobal, imgId, "Instantaneo", vars, diagramType);
        this.entradas = new ArrayList<>();
        this.transFunc = transFunc;
        this.vars = vars;
        this.tipos = tipos;
        for (int i = 0; i < vars.size(); i++)
            this.valores.add(i, "0");
        CParserForm.id = idLocal + "";
        this.colInput = colInput;
        this.colOutput = colOutput;
        this.inputTabela =  inputTabela;
        this.outputTabela = outputTabela;
        this.diagramType = diagramType;
        verificaConfiguracao();
    }

    public ArrayList<ArrayList<String>> getEntradas() {
        return entradas;
    }

    public void setEntradas(ArrayList<ArrayList<String>> entradas) {
        this.entradas = entradas;
    }

    public String getTransFunc() {
        return transFunc;
    }
        
    public ArrayList<String> getVars() {
        return vars;
    }

    public ArrayList<String> getTipos() {
        return tipos;
    }

    public ArrayList<String> getValores() {
        return valores;
    }
    
    public void setValores(String valor, int i) {
        this.valores.add(i, valor);
        verificaConfiguracao();
    }
    
    public void setEntradaColuna(String valor, int i){
        this.colInput.add(i, valor);
        verificaConfiguracao();
    }
    
    public ArrayList<String> getEntradaColuna(){
        return colInput;
    }
    
    public void setSaidaColuna(ArrayList<String> saida){
        colOutput = saida;
    }
    
    public ArrayList<String> getSaidaColuna(){
        return colOutput;
    }
    
    public void setEntradaValores(ArrayList<ArrayList<String>> entradaInput){
        inputTabela = entradaInput;
    }
    
    public ArrayList<ArrayList<String>> getEntradaValores(){
        return inputTabela;
    }
    
    public void setSaidaEntradaValores(ArrayList<ArrayList<String>> saidaOutput){
        outputTabela = saidaOutput;
    }
    
    public ArrayList<ArrayList<String>> getSaidaValores(){
        return outputTabela;
    }

    @Override
    public String getAtributos() {
        String texto = Main.languageResource.getString("Local ID:") + " " + String.valueOf(getId().getIdLocal())
                + "<br>" + Main.languageResource.getString("Global ID:") + " " + String.valueOf(getId().getIdGlobal())
                + "<br>" + Main.languageResource.getString("Label") + ": " + getId().getNome()
                + "<br>" + Main.languageResource.getString("Transfer Function") + ": " + String.valueOf(getTransFunc())
                + "<br>" + Main.languageResource.getString("X-coordinate:") + " " + String.valueOf(getX())
                + "<br>" + Main.languageResource.getString("Y-coordinate:") + " " + String.valueOf(getY());
        for (int i = 0; i < getVars().size(); i++) {
            texto = texto + "<br>" + getTipos().get(i) + " " + getVars().get(i) + ": " + getValores().get(i);           
        }
        return texto;
    }

    @Override
    public Instantaneo criarCopia(int posicaoMouseX, int posicaoMouseY, int idGlobal, int idLocal) {
        Instantaneo temp = new Instantaneo(posicaoMouseX, posicaoMouseY, idGlobal, 
                idLocal, vars, tipos, transFunc, getImgId(), colInput, colOutput, 
                inputTabela, outputTabela, diagramType);     
        temp.transFunc = this.transFunc;        
        temp.setImgId(getImgId());
        for (int i = 0; i < getVars().size(); i++) {
            temp.setValores(this.getValores().get(i), i);
        }
        temp.verificaConfiguracao();
        
        return temp;
    }

    @Override
    public List<ItemGrade> getNosEscalonaveis() {
        List<ItemGrade> escalonaveis = new ArrayList<>();
        Set internet = new HashSet();
        
        for (ItemGrade link : getConexoesSaida()) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getDestiny();
            if (itemGrade instanceof Cluster || itemGrade instanceof Instantaneo) {
                if (!escalonaveis.contains(itemGrade)) {
                    escalonaveis.add(itemGrade);
                }
            } else if (itemGrade instanceof Internet) {
                internet.add(itemGrade);
                getIndiretosEscalonaveis(itemGrade, escalonaveis, internet);
            }
        }
        escalonaveis.remove(this);
        return escalonaveis;
    }

    private void getIndiretosEscalonaveis(ItemGrade itemGrade, List<ItemGrade> escalonaveis, Set internet) {
        for (ItemGrade link : itemGrade.getConexoesSaida()) {
            ItemGrade item = (ItemGrade) ((Link) link).getDestiny();
            
            if (item instanceof Cluster || item instanceof Instantaneo) {
                if (!escalonaveis.contains(item)) {
                    escalonaveis.add(item);
                }
            } else if (item instanceof Internet) {
                if (!internet.contains(item)) {
                    internet.add(item);
                    getIndiretosEscalonaveis(item, escalonaveis, internet);
                }
            }
        }
    }

    private void verificaConfiguracao() {
        if (this.getValores().isEmpty()) {
                setConfigurado(true);
        } else {
            for (int i = 0; i < getVars().size(); i++) {
                if (!this.getValores().get(i).equals("")) {
                    setConfigurado(true);
                } else {
                    setConfigurado(false);
                    i = getVars().size();
                }
            }
        }
    }

    @Override
    protected Set<ItemGrade> getNosIndiretosSaida() {
        Set<ItemGrade> indiretosSaida = new HashSet<>();
        
        for (ItemGrade link : getConexoesSaida()) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getDestiny();
            if (itemGrade instanceof Cluster || itemGrade instanceof Instantaneo) {
                indiretosSaida.add(itemGrade);
            } else if (itemGrade instanceof Internet) {
                indiretosSaida.add(itemGrade);
                getIndiretosSaida(itemGrade, indiretosSaida);
            }
        }
        return indiretosSaida;
    }

    private void getIndiretosSaida(ItemGrade internet, Set<ItemGrade> indiretosSaida) {
        for (ItemGrade link : internet.getConexoesSaida()) {
            ItemGrade item = (ItemGrade) ((Link) link).getDestiny();
            
            if (item instanceof Cluster || item instanceof Instantaneo) {
                indiretosSaida.add(item);
            } else if (item instanceof Internet) {
                if (!indiretosSaida.contains(item)) {
                    indiretosSaida.add(item);
                    getIndiretosSaida(item, indiretosSaida);
                }
            }
        }
    }

    @Override
    protected Set<ItemGrade> getNosIndiretosEntrada() {
        Set<ItemGrade> indiretosEntrada = new HashSet<>();
        
        for (ItemGrade link : getConexoesEntrada()) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getOrigin();
            if (itemGrade instanceof Cluster || itemGrade instanceof Instantaneo) {
                indiretosEntrada.add(itemGrade);
            } else if (itemGrade instanceof Internet) {
                indiretosEntrada.add(itemGrade);
                getIndiretosEntrada(itemGrade, indiretosEntrada);
            }
        }
        return indiretosEntrada;
    }

    private void getIndiretosEntrada(ItemGrade internet, Set<ItemGrade> indiretosEntrada) {
        for (ItemGrade link : internet.getConexoesEntrada()) {
            ItemGrade item = (ItemGrade) ((Link) link).getOrigin();
            if (item instanceof Cluster || item instanceof Instantaneo) {
                indiretosEntrada.add(item);
            } else if (item instanceof Internet) {
                if (!indiretosEntrada.contains(item)) {
                    indiretosEntrada.add(item);
                    getIndiretosSaida(item, indiretosEntrada);
                }
            }
        }
    }
}
