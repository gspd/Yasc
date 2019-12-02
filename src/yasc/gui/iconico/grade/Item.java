package yasc.gui.iconico.grade;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import yasc.gui.iconico.Vertice;

public abstract class Item extends Vertice implements ItemGrade {
    private final IdentificadorItemGrade id;
    // Tabelas Hash com todas as conexões de entradas ou saídas organizadas por prioridade
    private final HashSet<ItemGrade> conexoesEntrada;
    private final HashSet<ItemGrade> conexoesSaida;
    
    private Double serviceRate;
    private Double preemptionTime;
    
    // Labels input signals
    private final ArrayList<String> inputList;
    // Diagram component
    private DiagramComponent dg;
    // Diagram type
    private final String diagram;
    
    // Define o algoritmo de escalonamento do objeto
    private String scheduler;
    // Define o objeto como origem das tarefas ou como destino das tarefas
    private boolean origination;
    private boolean destination;
    
    private int capacity;
    private double probServerFailure;
    private double probServiceFailure;
    // Define se o objeto está corretamente configurado
    private boolean configurado;
    // Id da imagem do objeto
    private int imgId;
    
    public Item(int x, int y, int idLocal, int idGlobal, int imgId, String tipoFila, ArrayList<String> inputList, String diagram) {
        super(x, y);
        this.id = new IdentificadorItemGrade(idLocal, idGlobal, tipoFila + idGlobal);
        this.imgId = imgId;
        conexoesEntrada = new HashSet<>();
        conexoesSaida = new HashSet<>();
        this.inputList = inputList;
        this.dg = null;
        this.diagram = diagram;
    }

    public DiagramComponent getDiagramComponent() {
        return dg;
    }
    
    public double getProbServerFailure() {
        return probServerFailure;
    }

    public void setProbServerFailure(double probServerFailure) {
        this.probServerFailure = probServerFailure;
    }

    public double getProbServiceFailure() {
        return probServiceFailure;
    }

    public void setProbServiceFailure(double probServiceFailure) {
        this.probServiceFailure = probServiceFailure;
    }

    public boolean isOrigination() {
        return origination;
    }

    public void setOrigination(boolean origination) {
        this.origination = origination;
    }

    public boolean isDestination() {
        return destination;
        
    }

    public void setDestination(boolean destination) {
        this.destination = destination;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
    
    public Double getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(Double serviceRate) {
        this.serviceRate = serviceRate;
        verificaConfiguracao();
    }


    public Double getPreemptionTime() {
        return preemptionTime;
    }

    public void setPreemptionTime(Double preemptionTime) {
        this.preemptionTime = preemptionTime;
        verificaConfiguracao();
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
        verificaConfiguracao();
    }

    @Override
    public IdentificadorItemGrade getId() {
        return this.id;
    }

    @Override
    public Set<ItemGrade> getConexoesEntrada() {
        return conexoesEntrada;
    }

    @Override
    public Set<ItemGrade> getConexoesSaida() {
        return conexoesSaida;
    }

    @Override
    public String toString() {
        return "id: " + getId().getIdGlobal() + " " + getId().getNome();
    }

    public void setConfigurado(boolean bool) {
        this.configurado = bool;
    }
    
    @Override
    public boolean isConfigurado() {
        return configurado;
    }

    @Override
    public void draw(Graphics g) {
        if (imgId != 0) {
            g.drawImage(DesenhoGrade.VECTOR[imgId], getX() - 15, getY() - 15, null);
        } else {
            switch (diagram) {
                case "__AND_LOGIC_GATE":
                    dg = new AndLogicGate(inputList);
                    break;
                case "__OR_LOGIC_GATE":
                    dg = new OrLogicGate(inputList);
                    break;
                case "__XOR_LOGIC_GATE":
                    dg = new XorLogicGate(inputList);
                    break;
                case "__NOT_LOGIC_GATE":
                    dg = new NotLogicGate(inputList);
                    break;
            }
            dg.draw(getX(), getY(), 0, g);
        }
        
        g.setColor(Color.BLACK);
        if (isConfigurado() && dg != null) { // If is configured and is a diagram
            g.drawImage(DesenhoGrade.IVERDE, getX() + dg.getDimension().width,
                    getY() + dg.getDimension().height, null);
            g.drawString(String.valueOf(getId().getIdGlobal()), 
                    getX(), getY() + dg.getDimension().height + 10);
        }
        else if (isConfigurado() && dg == null) { // If is configured and is not a diagram
            g.drawImage(DesenhoGrade.IVERDE, getX() + 15, getY() + 15, null);
            g.drawString(String.valueOf(getId().getIdGlobal()), getX(), getY() + 30);
        }
        else if (!isConfigurado() && dg != null) { // If is not configured and is a diagram
            g.drawImage(DesenhoGrade.IVERMELHO, getX() + dg.getDimension().width,
                    getY() + dg.getDimension().height, null);
            g.drawString(String.valueOf(getId().getIdGlobal()), 
                    getX(), getY() + dg.getDimension().height + 10);
        }
        else { // If is not configured and is not a diagram
            g.drawImage(DesenhoGrade.IVERMELHO, getX() + 15, getY() + 15, null);
            g.drawString(String.valueOf(getId().getIdGlobal()), getX(), getY() + 30);
        }
        
        // Se o icone estiver ativo, desenhamos uma margem nele
        if (isSelected()) {
            g.setColor(Color.RED);
            if (dg == null)
                g.drawRect(getX() - 19, getY() - 17, 37, 34);
            else
                g.drawRect(getX(), getY(),
                dg.getDimension().width, dg.getDimension().height);
        }
    }

    @Override
    public boolean contains(int x, int y) {
        if (dg == null) {
            if (x < getX() + 17 && x > getX() - 17) {
                if (y < getY() + 17 && y > getY() - 17) {
                    return true;
                }
            }
        } else {
            if (x < getX() + dg.getDimension().width && x > getX()) {
                if (y < getY() + dg.getDimension().height && y > getY()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<ItemGrade> getNosEscalonaveis() {
        List<ItemGrade> escalonaveis = new ArrayList<>();
        Set internet = new HashSet();
        
        for (ItemGrade link : conexoesSaida) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getDestiny();
            if (itemGrade instanceof Cluster || itemGrade instanceof Item) {
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
            if (item instanceof Cluster || item instanceof Item) {
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

    private void verificaConfiguracao() { }

    protected Set<ItemGrade> getNosIndiretosSaida() {
        Set<ItemGrade> indiretosSaida = new HashSet<>();
        for (ItemGrade link : conexoesSaida) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getDestiny();
            if (itemGrade instanceof Cluster || itemGrade instanceof Item) {
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
            if (item instanceof Cluster || item instanceof Item) {
                indiretosSaida.add(item);
            } else if (item instanceof Internet) {
                if (!indiretosSaida.contains(item)) {
                    indiretosSaida.add(item);
                    getIndiretosSaida(item, indiretosSaida);
                }
            }
        }
    }

    protected Set<ItemGrade> getNosIndiretosEntrada() {
        Set<ItemGrade> indiretosEntrada = new HashSet<>();
        for (ItemGrade link : conexoesEntrada) {
            ItemGrade itemGrade = (ItemGrade) ((Link) link).getOrigin();
            if (itemGrade instanceof Cluster || itemGrade instanceof Item) {
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
            if (item instanceof Cluster || item instanceof Item) {
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
