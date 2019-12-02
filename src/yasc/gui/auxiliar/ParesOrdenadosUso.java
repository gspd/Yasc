package yasc.gui.auxiliar;

/**
 * Classe que possui apenas doubles que indicam intervalos de tempos onde a máquina executou.
 */
public class ParesOrdenadosUso implements Comparable<ParesOrdenadosUso> {

    Double inicio;
    Double fim;
    
    // Recebe o intervalo.
    public ParesOrdenadosUso(double inicio, double fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    public Double getInicio() {
        return this.inicio;
    }

    public Double getFim() {
        return this.fim;
    }

    @Override
    public String toString() {
        return inicio + " " + fim;
    }

    @Override
    public int compareTo(ParesOrdenadosUso o) {
        return inicio.compareTo(o.inicio);
    }
}
