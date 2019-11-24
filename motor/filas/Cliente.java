package yasc.motor.filas;

import java.util.List;
import yasc.motor.filas.servidores.CentroServico;

public interface Cliente {
    public double getTamComunicacao();
    public double getTamProcessamento();
    public double getTimeCriacao();
    public CentroServico getOrigem();
    public List<CentroServico> getCaminho();
    public void setCaminho(List<CentroServico> caminho);
}
