package yasc.motor.filas.servidores.implementacao;

import yasc.motor.filas.servidores.CentroServico;

public interface CS_Vertice {
    public void addConexoesEntrada(CentroServico conexao);
    public void addConexoesSaida(CentroServico conexao);
}
