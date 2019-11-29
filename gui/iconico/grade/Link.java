package yasc.gui.iconico.grade;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import yasc.Main;
import java.util.Set;
import yasc.gui.iconico.Aresta;
import yasc.gui.iconico.Vertice;

public class Link extends Aresta implements ItemGrade {

    private final IdentificadorItemGrade id;
    private boolean selected;
    private final Polygon areaSeta;
    private static final Color DARK_GREEN = new Color(0, 130, 0);
    private double banda;
    private double ocupacao;
    private double latencia;
    private boolean configurado;

    public Link(Vertice origin, Vertice destiny, Vertice originPoint, Vertice destinyPoint, int idLocal, int idGlobal) {
        super(origin, destiny, originPoint, destinyPoint);
        this.selected = true;
        this.areaSeta = new Polygon();
        this.id = new IdentificadorItemGrade(idLocal, idGlobal, "link" + idGlobal);
        verificaConfiguracao();
    }

    @Override
    public IdentificadorItemGrade getId() {
        return this.id;
    }

    @Override
    public Set<ItemGrade> getConexoesEntrada() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<ItemGrade> getConexoesSaida() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAtributos() {
        String texto;
        if (getOriginPoint() == null || getDestinyPoint() == null) {
            texto = Main.languageResource.getString("Local ID:") + " " + getId().getIdLocal()
                    + "<br>" + Main.languageResource.getString("Global ID:") + " " + getId().getIdGlobal()
                    + "<br>" + Main.languageResource.getString("Label") + ": " + getId().getNome()
                    + "<br>" + Main.languageResource.getString("X1-coordinate:") + " " + getOrigin().getX()
                    + "<br>" + Main.languageResource.getString("Y1-coordinate:") + " " + getOrigin().getY()
                    + "<br>" + Main.languageResource.getString("X2-coordinate:") + " " + getDestiny().getX()
                    + "<br>" + Main.languageResource.getString("Y2-coordinate:") + " " + getDestiny().getY();
        } else {
            texto = Main.languageResource.getString("Local ID:") + " " + getId().getIdLocal()
                    + "<br>" + Main.languageResource.getString("Global ID:") + " " + getId().getIdGlobal()
                    + "<br>" + Main.languageResource.getString("Label") + ": " + getId().getNome()
                    + "<br>" + Main.languageResource.getString("X1-coordinate:") + " " + getOriginPoint().getX()
                    + "<br>" + Main.languageResource.getString("Y1-coordinate:") + " " + getOriginPoint().getY()
                    + "<br>" + Main.languageResource.getString("X2-coordinate:") + " " + getDestinyPoint().getX()
                    + "<br>" + Main.languageResource.getString("Y2-coordinate:") + " " + getDestinyPoint().getY();
        }
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
    public Link criarCopia(int posicaoMouseX, int posicaoMouseY, int idGlobal, int idLocal) {
        Link temp = new Link(null, null, null, null, idGlobal, idLocal);
        temp.banda = this.banda;
        temp.latencia = this.latencia;
        temp.ocupacao = this.ocupacao;
        temp.verificaConfiguracao();
        return temp;
    }

    @Override
    public boolean isConfigurado() {
        return configurado;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void draw(Graphics g) {
        double arrowWidth = 11.0f;
        double theta = 0.423f;
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        double[] vecLine = new double[2];
        double[] vecLeft = new double[2];
        double fLength;
        double th;
        double ta;
        double baseX, baseY;

        xPoints[0] = (int) getX();
        yPoints[0] = (int) getY();

        // build the line vector
        vecLine[0] = (double) xPoints[ 0] - getOrigin().getX();
        vecLine[1] = (double) yPoints[ 0] - getOrigin().getY();

        // build the arrow base vector - normal to the line
        vecLeft[0] = -vecLine[1];
        vecLeft[1] = vecLine[0];

        // setup length parameters
        fLength = (double) Math.sqrt(vecLine[0] * vecLine[0] + vecLine[1] * vecLine[1]);
        th = arrowWidth / (2.0f * fLength);
        ta = arrowWidth / (2.0f * ((double) Math.tan(theta) / 2.0f) * fLength);

        // find the base of the arrow
        baseX = ((double) xPoints[ 0] - ta * vecLine[0]);
        baseY = ((double) yPoints[ 0] - ta * vecLine[1]);

        // build the points on the sides of the arrow
        xPoints[1] = (int) (baseX + th * vecLeft[0]);
        yPoints[1] = (int) (baseY + th * vecLeft[1]);
        xPoints[2] = (int) (baseX - th * vecLeft[0]);
        yPoints[2] = (int) (baseY - th * vecLeft[1]);

        if (isSelected()) {
            g.setColor(Color.BLACK);
        } else if (isConfigurado()) {
            g.setColor(DARK_GREEN);
        } else {
            g.setColor(Color.RED);
        }
        
        if (getOriginPoint() == null || getDestinyPoint() == null) {
            areaSeta.reset();
            areaSeta.addPoint(xPoints[0], yPoints[0]);
            areaSeta.addPoint(xPoints[1], yPoints[1]);
            areaSeta.addPoint(xPoints[2], yPoints[2]);
            g.fillPolygon(areaSeta);
            g.drawLine(getOrigin().getX(), getOrigin().getY(), getDestiny().getX(), getDestiny().getY());
        } else {
            g.drawLine(getOriginPoint().getX(), getOriginPoint().getY(), getDestinyPoint().getX(), getDestinyPoint().getY());
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return areaSeta.contains(x, y);
    }

    public double getBanda() {
        return banda;
    }

    public double getTaxaOcupacao() {
        return ocupacao;
    }

    public double getLatencia() {
        return latencia;
    }

    public void setBanda(double banda) {
        this.banda = banda;
        verificaConfiguracao();
    }

    public void setTaxaOcupacao(double taxa) {
        this.ocupacao = taxa;
    }

    public void setLatencia(double latencia) {
        this.latencia = latencia;
        verificaConfiguracao();
    }

    @Override
    public Integer getX() {
        return (((((getOrigin().getX() + getDestiny().getX()) / 2) + getDestiny().getX()) / 2) + getDestiny().getX()) / 2;
    }

    @Override
    public Integer getY() {
        return (((((getOrigin().getY() + getDestiny().getY()) / 2) + getDestiny().getY()) / 2) + getDestiny().getY()) / 2;
    }

    private void verificaConfiguracao() {
        configurado = true;
    }
}
