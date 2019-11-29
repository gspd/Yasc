package yasc.gui.iconico.grade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import yasc.Main;
import java.util.Set;

public class Machine extends Item implements ItemGrade {

    private String algoritmo;
    private Double poderComputacional;
    private Integer nucleosProcessador;
    private Double ocupacao;
    private Boolean mestre;
    private Double memoriaRAM;
    private Double discoRigido;
    private String proprietario;
    private List<ItemGrade> escravos;

    public Machine(int x, int y, int idLocal, int idGlobal) {
        super(x, y, idLocal, idGlobal, 0, "Mac", null, null);
        this.escravos = new ArrayList<>();
        this.proprietario = "user1";
        this.algoritmo = "---";
        this.nucleosProcessador = 1;
    }

    @Override
    public String getAtributos() {
        String texto = Main.languageResource.getString("Local ID:") + " " + String.valueOf(getId().getIdLocal())
                + "<br>" + Main.languageResource.getString("Global ID:") + " " + String.valueOf(getId().getIdGlobal())
                + "<br>" + Main.languageResource.getString("Label") + ": " + getId().getNome()
                + "<br>" + Main.languageResource.getString("X-coordinate:") + " " + String.valueOf(getX())
                + "<br>" + Main.languageResource.getString("Y-coordinate:") + " " + String.valueOf(getY())
                + "<br>" + Main.languageResource.getString("Computing power") + ": " + String.valueOf(getPoderComputacional())
                + "<br>" + Main.languageResource.getString("Load Factor") + ": " + String.valueOf(getTaxaOcupacao());
        if (isMestre()) {
            texto = texto
                    + "<br>" + Main.languageResource.getString("Master")
                    + "<br>" + Main.languageResource.getString("Scheduling algorithm") + ": " + getAlgoritmo();
        } else {
            texto = texto
                    + "<br>" + Main.languageResource.getString("Slave");
        }
        return texto;
    }

    @Override
    public Machine criarCopia(int posicaoMouseX, int posicaoMouseY, int idGlobal, int idLocal) {
        Machine temp = new Machine(posicaoMouseX, posicaoMouseY, idGlobal, idLocal);
        
        temp.algoritmo = this.algoritmo;
        temp.poderComputacional = this.poderComputacional;
        temp.ocupacao = this.ocupacao;
        temp.mestre = this.mestre;
        temp.proprietario = this.proprietario;
        temp.verificaConfiguracao();
        
        return temp;
    }

    public void setMestre(Boolean mestre) {
        this.mestre = mestre;
        verificaConfiguracao();
    }

    public Boolean isMestre() {
        return mestre;
    }

    public List<ItemGrade> getEscravos() {
        return escravos;
    }

    public void setEscravos(List<ItemGrade> escravos) {
        this.escravos = escravos;
    }

    @Override
    public List<ItemGrade> getNosEscalonaveis() {
        List<ItemGrade> escalonaveis = new ArrayList<>();
        Set internet = new HashSet();
        for (ItemGrade link : getConexoesSaida()) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getDestiny();
            if (itemGrade instanceof Cluster || itemGrade instanceof Machine) {
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
            if (item instanceof Cluster || item instanceof Machine) {
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

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
        verificaConfiguracao();
    }

    public Double getPoderComputacional() {
        return poderComputacional;
    }

    public void setPoderComputacional(double poderComputacional) {
        this.poderComputacional = poderComputacional;
        verificaConfiguracao();
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }
    
    public Double getTaxaOcupacao() {
        return ocupacao;
    }

    public void setTaxaOcupacao(Double ocupacao) {
        this.ocupacao = ocupacao;
    }

    public Integer getNucleosProcessador() {
        return nucleosProcessador;
    }

    public void setNucleosProcessador(Integer nucleosProcessador) {
        this.nucleosProcessador = nucleosProcessador;
    }

    public Double getMemoriaRAM() {
        return memoriaRAM;
    }

    public void setMemoriaRAM(Double memoriaRAM) {
        this.memoriaRAM = memoriaRAM;
    }

    public Double getDiscoRigido() {
        return discoRigido;
    }

    public void setDiscoRigido(Double discoRigido) {
        this.discoRigido = discoRigido;
    }

    private void verificaConfiguracao() {
        if (poderComputacional > 0) {
            setConfigurado(true);
            if (mestre && "---".equals(algoritmo)) {
                setConfigurado(false);
            }
        } else {
            setConfigurado(false);
        }
    }

    @Override
    protected Set<ItemGrade> getNosIndiretosSaida() {
        Set<ItemGrade> indiretosSaida = new HashSet<>();
        for (ItemGrade link : getConexoesSaida()) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getDestiny();
            if (itemGrade instanceof Cluster || itemGrade instanceof Machine) {
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
            if (item instanceof Cluster || item instanceof Machine) {
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
            if (itemGrade instanceof Cluster || itemGrade instanceof Machine) {
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
            if (item instanceof Cluster || item instanceof Machine) {
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
