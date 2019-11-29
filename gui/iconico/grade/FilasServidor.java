package yasc.gui.iconico.grade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import yasc.Main;
import java.util.Set;
import yasc.gui.entrada.ListasArmazenamento;

public class FilasServidor extends Item implements ItemGrade {

    private int numQueues;
    
    public FilasServidor(int x, int y, int idLocal, int idGlobal, int imgId) {
        super(x, y, idLocal, idGlobal, imgId, "FilasServidor", null, null);
        setScheduler("SJF");
    }
    
    public int getNumQueues() {
        return numQueues;
    }

    public void setNumQueues(int numQueues) {
        this.numQueues = numQueues;
        verificaConfiguracao();
    }
    
    @Override
    public String getAtributos() {
        String texto = Main.languageResource.getString("Local ID:") + " " + String.valueOf(getId().getIdLocal())
                + "<br>" + Main.languageResource.getString("Global ID:") + " " + String.valueOf(getId().getIdGlobal())
                + "<br>" + Main.languageResource.getString("Label") + ": " + getId().getNome()
                + "<br>" + Main.languageResource.getString("X-coordinate:") + " " + String.valueOf(getX())
                + "<br>" + Main.languageResource.getString("Y-coordinate:") + " " + String.valueOf(getY())
                + "<br>" + Main.languageResource.getString("Service Time") + ": " + String.valueOf(getServiceRate())
                + "<br>" + Main.languageResource.getString("Preemption") + ": " + String.valueOf(getPreemptionTime())
                + "<br>" + Main.languageResource.getString("Number of Queues") + ": " + String.valueOf(getNumQueues())
                + "<br>" + Main.languageResource.getString("Scheduler") + ": " + String.valueOf(getScheduler());
        if (ListasArmazenamento.isFalhaCapacidade()) {
                texto = texto + "<br>" + Main.languageResource.getString("Capacity") + ": " + String.valueOf(getCapacity());
        }
        if (ListasArmazenamento.isFalha()) {
                texto = texto + "<br>" + Main.languageResource.getString("Probability of server failure") + ": " + String.valueOf(getProbServerFailure())
                        + "<br>" + Main.languageResource.getString("Probability of attendance failure") + ": " + String.valueOf(getProbServiceFailure());
        }        
        return texto;
    }

    @Override
    public FilasServidor criarCopia(int posicaoMouseX, int posicaoMouseY, int idGlobal, int idLocal) {
        FilasServidor temp = new FilasServidor(posicaoMouseX, posicaoMouseY, idGlobal, idLocal, getImgId());

        temp.setServiceRate(getServiceRate());
        temp.setPreemptionTime(getPreemptionTime());
        temp.setImgId(getImgId());
        temp.setCapacity(getCapacity());
        temp.setOrigination(isOrigination());
        temp.setDestination(isDestination());
        temp.setProbServerFailure(getProbServerFailure());
        temp.setProbServiceFailure(getProbServiceFailure());
        temp.setScheduler("SJF");
        temp.numQueues = this.numQueues;
        temp.verificaConfiguracao();
        
        return temp;
    }

    @Override
    public List<ItemGrade> getNosEscalonaveis() {
        List<ItemGrade> escalonaveis = new ArrayList<>();
        Set internet = new HashSet();
        for (ItemGrade link : getConexoesSaida()) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getDestiny();
            if (itemGrade instanceof Cluster || itemGrade instanceof FilasServidor) {
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
            if (item instanceof Cluster || item instanceof FilasServidor) {
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
        if (getServiceRate() > 0
                && (getPreemptionTime() >= 0 && getPreemptionTime() < 1)
                && (getProbServerFailure() >= 0 && getProbServerFailure() <= 1)
                && (getProbServiceFailure() >= 0 && getProbServiceFailure() <= 1)
                && getCapacity() >= 0
                && this.numQueues > 0)
        {
            setConfigurado(true);
            if ("".equals(getScheduler())) {
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
            if (itemGrade instanceof Cluster || itemGrade instanceof FilasServidor) {
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
            if (item instanceof Cluster || item instanceof FilasServidor) {
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
            if (itemGrade instanceof Cluster || itemGrade instanceof FilasServidor) {
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
            if (item instanceof Cluster || item instanceof FilasServidor) {
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
