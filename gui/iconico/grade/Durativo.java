package yasc.gui.iconico.grade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import yasc.Main;
import java.util.Set;

public class Durativo extends Item implements ItemGrade {

    private String transFunc;
    private Double timeTransf;
    private ArrayList<String> vars = new ArrayList<>();
    private ArrayList<String> tipos = new ArrayList<>();
    private final ArrayList<String> valores;
    
    public Durativo(int x, int y, int idLocal, int idGlobal, ArrayList<String> vars, ArrayList<String> tipos, String transFunc, int imgId) {
        super(x, y, idLocal, idGlobal, imgId, "Durativo", vars, null);
        this.vars = vars;
        this.tipos = tipos;
        this.transFunc = transFunc;
        this.valores = new ArrayList<>();
    }

    public String getTransFunc() {
        return transFunc;
    }

    public Double getTimeTransf() {
        return timeTransf;
    }
    
    public void setTimeTransf(Double timeTransf) {
        this.timeTransf = timeTransf;
        verificaConfiguracao();
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
    
    @Override
    public String getAtributos() {
        String texto = Main.languageResource.getString("Local ID:") + " " + String.valueOf(getId().getIdLocal())
                + "<br>" + Main.languageResource.getString("Global ID:") + " " + String.valueOf(getId().getIdGlobal())
                + "<br>" + Main.languageResource.getString("Label") + ": " + getId().getNome()
                + "<br>" + Main.languageResource.getString("X-coordinate:") + " " + String.valueOf(getX())
                + "<br>" + Main.languageResource.getString("Y-coordinate:") + " " + String.valueOf(getY())
                + "<br>" + Main.languageResource.getString("Transfer Time") + ": " + String.valueOf(getTimeTransf())
                + "<br>" + Main.languageResource.getString("Transfer Function") + ": " + String.valueOf(getTransFunc());
        for (int i = 0; i < getVars().size(); i++) {
            texto = texto + "<br>" + getTipos().get(i) + " " + getVars().get(i) + ": " + getValores().get(i);           
        }
        return texto;
    }

    @Override
    public Durativo criarCopia(int posicaoMouseX, int posicaoMouseY, int idGlobal, int idLocal) {
        Durativo temp = new Durativo(posicaoMouseX, posicaoMouseY, idGlobal, idLocal, vars, tipos, transFunc, getImgId());     
        
        temp.timeTransf = this.timeTransf;
        temp.transFunc = this.transFunc;
        for (int i = 0; i < getVars().size(); i++) {
            temp.setValores(this.getValores().get(i), i);
        }
        temp.setImgId(getImgId());
        temp.verificaConfiguracao();
        
        return temp;
    }

    @Override
    public List<ItemGrade> getNosEscalonaveis() {
        List<ItemGrade> escalonaveis = new ArrayList<>();
        Set internet = new HashSet();
        for (ItemGrade link : getConexoesSaida()) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getDestiny();
            if (itemGrade instanceof Cluster || itemGrade instanceof Durativo) {
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
            if (item instanceof Cluster || item instanceof Durativo) {
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
       for(int i = 0; i < getVars().size(); i++){
            if (!getValores().get(i).equals("")) {
                setConfigurado(true);
            } else {
                setConfigurado(false);
                i = getVars().size();
            }
        }
        if (timeTransf > 0)
            setConfigurado(true);
        else
            setConfigurado(false);
    }

    @Override
    protected Set<ItemGrade> getNosIndiretosSaida() {
        Set<ItemGrade> indiretosSaida = new HashSet<>();
        for (ItemGrade link : getConexoesSaida()) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getDestiny();
            if (itemGrade instanceof Cluster || itemGrade instanceof Durativo) {
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
            if (item instanceof Cluster || item instanceof Durativo) {
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
            if (itemGrade instanceof Cluster || itemGrade instanceof Durativo) {
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
            if (item instanceof Cluster || item instanceof Durativo) {
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
