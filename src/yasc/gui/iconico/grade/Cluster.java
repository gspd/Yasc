package yasc.gui.iconico.grade;

import yasc.Main;

public class Cluster extends Item implements ItemGrade {
    private Double banda;
    private Double latencia;
    private String algoritmo;
    private Double poderComputacional;
    private Integer nucleosProcessador;
    private Integer numeroEscravos;
    private Boolean mestre;
    private Double memoriaRAM;
    private Double discoRigido;
    private String proprietario;

    public Cluster(Integer x, Integer y, int idLocal, int idGlobal) {
        super(x, y, idLocal, idGlobal, 0, "Cluster", null, null);
        this.algoritmo = "---";
        this.proprietario = "user1";
        this.nucleosProcessador = 1;
        this.mestre = true;
    }
    
    @Override
    public String getAtributos() {
        String texto = Main.languageResource.getString("Local ID:") + " " + this.getId().getIdLocal()
                + "<br>" + Main.languageResource.getString("Global ID:") + " " + this.getId().getIdGlobal()
                + "<br>" + Main.languageResource.getString("Label") + ": " + this.getId().getNome()
                + "<br>" + Main.languageResource.getString("X-coordinate:") + " " + this.getX()
                + "<br>" + Main.languageResource.getString("Y-coordinate:") + " " + this.getY()
                + "<br>" + Main.languageResource.getString("Number of slaves") + ": " + getNumeroEscravos()
                + "<br>" + Main.languageResource.getString("Computing power") + ": " + getPoderComputacional()
                + "<br>" + Main.languageResource.getString("Bandwidth") + ": " + getBanda()
                + "<br>" + Main.languageResource.getString("Latency") + ": " + getLatencia()
                + "<br>" + Main.languageResource.getString("Scheduling algorithm") + ": " + getAlgoritmo();
        return texto;
    }

    @Override
    public Cluster criarCopia(int posicaoMouseX, int posicaoMouseY, int idGlobal, int idLocal) {
        Cluster temp = new Cluster(posicaoMouseX, posicaoMouseY, idGlobal, idLocal);
        temp.algoritmo = this.algoritmo;
        temp.poderComputacional = this.poderComputacional;
        temp.mestre = this.mestre;
        temp.proprietario = this.proprietario;
        temp.banda = this.banda;
        temp.latencia = this.latencia;
        temp.numeroEscravos = this.numeroEscravos;
        temp.verificaConfiguracao();
        return temp;
    }

    public Boolean isMestre() {
        return mestre;
    }

    public void setMestre(Boolean mestre) {
        this.mestre = mestre;
        verificaConfiguracao();
    }

    public Double getBanda() {
        return banda;
    }

    public void setBanda(Double banda) {
        this.banda = banda;
        verificaConfiguracao();
    }

    public Double getLatencia() {
        return latencia;
    }

    public void setLatencia(Double latencia) {
        this.latencia = latencia;
        verificaConfiguracao();
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

    public void setPoderComputacional(Double poderComputacional) {
        this.poderComputacional = poderComputacional;
        verificaConfiguracao();
    }

    public Integer getNumeroEscravos() {
        return numeroEscravos;

    }

    public void setNumeroEscravos(Integer numeroEscravos) {
        this.numeroEscravos = numeroEscravos;
        verificaConfiguracao();
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
        if (banda > 0 && latencia > 0 && poderComputacional > 0 && numeroEscravos > 0) {
            setConfigurado(true);
            if (mestre && algoritmo.equals("---")) {
                setConfigurado(false);
            }
        } else {
            setConfigurado(false);
        }
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String string) {
        this.proprietario = string;
    }
}
