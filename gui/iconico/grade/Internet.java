package yasc.gui.iconico.grade;

import yasc.Main;

public class Internet extends Item implements ItemGrade {
    
    private double banda;
    private double ocupacao;
    private double latencia;

    public Internet(int x, int y, int idLocal, int idGlobal) {
        super(x, y, idLocal, idGlobal, 0, "Net", null, null);
    }

    @Override
    public String getAtributos() {
        String texto = Main.languageResource.getString("Local ID:") + " " + getId().getIdLocal()
         + "<br>" + Main.languageResource.getString("Global ID:") + " " + getId().getIdGlobal()
         + "<br>" + Main.languageResource.getString("Label") + ": " + getId().getNome()
         + "<br>" + Main.languageResource.getString("X-coordinate:") + " " + getX()
         + "<br>" + Main.languageResource.getString("Y-coordinate:") + " " + getY()
         + "<br>" + Main.languageResource.getString("Bandwidth") + ": " + getBanda()
         + "<br>" + Main.languageResource.getString("Latency") + ": " + getLatencia()
         + "<br>" + Main.languageResource.getString("Load Factor") + ": " + getTaxaOcupacao();
         return texto;
    }
    
    /**
     *
     * @param posicaoMouseX the value of posicaoMouseX
     * @param posicaoMouseY the value of posicaoMouseY
     * @param idGlobal the value of idGlobal
     * @param idLocal the value of idLocal
     */
    @Override
    public Internet criarCopia(int posicaoMouseX, int posicaoMouseY, int idGlobal, int idLocal) {
        Internet temp = new Internet(posicaoMouseX, posicaoMouseY, idGlobal, idLocal);
        
        temp.banda = this.banda;
        temp.ocupacao = this.ocupacao;
        temp.latencia = this.latencia;
        temp.verificaConfiguracao();
        
        return temp;
    }

    public double getBanda() {
        return banda;
    }

    public void setBanda(double banda) {
        this.banda = banda;
        verificaConfiguracao();
    }

    public double getTaxaOcupacao() {
        return ocupacao;
    }

    public void setTaxaOcupacao(double ocupacao) {
        this.ocupacao = ocupacao;
    }

    public double getLatencia() {
        return latencia;
    }

    public void setLatencia(double latencia) {
        this.latencia = latencia;
        verificaConfiguracao();
    }
    
    private void verificaConfiguracao() {
        setConfigurado(banda > 0 && latencia > 0);
    }
}
