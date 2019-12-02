package yasc.gui.iconico;

import java.awt.Point;
import yasc.gui.iconico.grade.Item;

public abstract class Aresta implements Icone {

    private Vertice origin;
    private Vertice destiny;
    private Vertice originPoint;
    private Vertice destinyPoint;
    private final Point originDistance;
    private final Point destinyDistance;

    public Aresta(Vertice origin, Vertice destiny, Vertice originPoint, Vertice destinyPoint) {
        this.origin = origin;
        this.destiny = destiny;
        this.originPoint = originPoint;
        this.destinyPoint = destinyPoint;
        this.originDistance = new Point();
        this.destinyDistance = new Point();
        setLinkDistance();
    }
    
    public Point getLinkDistance() {
        return originDistance;
    }

    public void setLinkDistance() {
        this.originDistance.x = Math.abs(originPoint.getX() - ((Item) origin).getX());
        this.originDistance.y = Math.abs(originPoint.getY() - ((Item) origin).getY());
        this.destinyDistance.x = Math.abs(destinyPoint.getX() - ((Item) destiny).getX());
        this.destinyDistance.y = Math.abs(destinyPoint.getY() - ((Item) destiny).getY());
    }

    public Vertice getOriginPoint() {
        return originPoint;
    }

    public Vertice getDestinyPoint() {
        return destinyPoint;
    }
    
    public Vertice getOrigin() {
        return origin;
    }

    public Vertice getDestiny() {
        return destiny;
    }
    
    public void setPosition(Vertice origem, Vertice destino) {
        this.origin = origem;
        this.destiny = destino;
    }
    
    public void setPointPosition(Vertice originPoint, Vertice destinyPoint) {
        this.originPoint = originPoint;
        this.destinyPoint = destinyPoint;
    }
    
    public void updatePointPosition() {
        originPoint.setPosition(((Item) origin).getX() + originDistance.x, ((Item) origin).getY() + originDistance.y);
        destinyPoint.setPosition(((Item) destiny).getX() + destinyDistance.x, ((Item) destiny).getY() + destinyDistance.y);
    }
    
    /**
     * @return Posição central da aresta no eixo X
     */
    @Override
    public Integer getX() {
        return getOrigin().getX() + (getDestiny().getX() - getOrigin().getX()) / 2;
    }
    
    /**
     * @return Posição central da aresta no eixo Y
     */
    @Override
    public Integer getY() {
        return getOrigin().getY() + (getDestiny().getY() - getOrigin().getY()) / 2;
    }
}
