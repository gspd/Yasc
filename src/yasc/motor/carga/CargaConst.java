package yasc.motor.carga;

import java.util.ArrayList;
import java.util.List;
import yasc.motor.filas.RedeDeFilas;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Comunicacao;
import yasc.motor.filas.servidores.CentroServico;
import yasc.motor.random.Distribution;
import java.util.Random;

/**
 * Descreve como gerar tarefas e falhas
 */
public class CargaConst extends GerarCarga {

    private int NumTars;
    private double Tam;
    private double ArrivalTime;
    private boolean distribuirIgual;
    
    public CargaConst(int NumTars, double Tam, boolean distribuirIgual, double ArrivalTime) {
        this.NumTars = NumTars;
        this.Tam = Tam;
        this.distribuirIgual = distribuirIgual;
        this.ArrivalTime = ArrivalTime;
    }

    @Override
    public List<Tarefa> toTarefaList(RedeDeFilas rdf) {
        List<Tarefa> tarefas = new ArrayList<>();
        
        if (!distribuirIgual) {
            List<CentroServico> listaDestinosPossiveis;
            int identificador = 0;
            int quantidadePorOrigem = this.getNumTars() / rdf.getOrigens().size();
            int resto = this.getNumTars() % rdf.getOrigens().size();
            Distribution gerador = new Distribution((int) System.currentTimeMillis());
            
            for (CentroServico csOrigem : rdf.getOrigens()) {
                int indexDest = 0;
                listaDestinosPossiveis = new ArrayList<>();
                
                // Quando o objeto é origem e destino ao mesmo tempo
                for (CentroServico csDestino : rdf.getDestinos())
                    if (csOrigem == csDestino || getMenorCaminho(csOrigem, csDestino) != null)
                        listaDestinosPossiveis.add(csDestino);
                
                Tarefa tarefa;
                for (int i = 0; i < quantidadePorOrigem; i++) {
                    if (!listaDestinosPossiveis.isEmpty()) {
                        tarefa = new Tarefa(
                            identificador,
                            "",
                            "",
                            csOrigem,
                            listaDestinosPossiveis.get(indexDest),
                            getTam(),
                            gerador.nextExponential(ArrivalTime)/*tempo de criação*/);
                    } else {
                        tarefa = new Tarefa(
                            identificador,
                            "",
                            "",
                            csOrigem,
                            null,
                            getTam(),
                            gerador.nextExponential(ArrivalTime)/*tempo de criação*/);
                    }
                    
                    tarefas.add(tarefa);
                    identificador++;
                    if (indexDest + 1 >= listaDestinosPossiveis.size()) {
                        indexDest = 0;
                    } else {
                        indexDest++;
                    }
                }
            }
            for (int i = 0; i < resto; i++) {
                listaDestinosPossiveis = new ArrayList<>();
                
                for (CentroServico Dest : rdf.getDestinos())
                    if (getMenorCaminho(rdf.getOrigens().get(0), Dest) != null || rdf.getOrigens().get(0) == Dest)
                        listaDestinosPossiveis.add(Dest);
                
                Tarefa tarefa;
                if (!listaDestinosPossiveis.isEmpty()) {
                    tarefa = new Tarefa(
                        identificador,
                        "",
                        "",
                        rdf.getOrigens().get(0),
                        listaDestinosPossiveis.get(0),
                        getTam(),
                        gerador.nextExponential(ArrivalTime)/*tempo de criação*/);
                } else {
                    tarefa = new Tarefa(
                        identificador,
                        "",
                        "",
                        rdf.getOrigens().get(0),
                        null,
                        getTam(),
                        gerador.nextExponential(ArrivalTime)/*tempo de criação*/);
                }
                
                tarefas.add(tarefa);
                identificador++;
            }
        } else {
            for (int i = 0; i < this.getNumTars(); i++) {
                Distribution gerador = new Distribution((int) System.currentTimeMillis());
                CentroServico csOrigem, csDestino;
                Random rd = new Random(System.currentTimeMillis());
                int origemIndex, destinoIndex, id = 0;
                
                do {
                    origemIndex = Math.abs(rd.nextInt()%rdf.getOrigens().size());
                    destinoIndex = Math.abs(rd.nextInt()%rdf.getDestinos().size());
                    csOrigem = rdf.getOrigens().get(origemIndex);
                    csDestino = rdf.getDestinos().get(destinoIndex);
                } while (getMenorCaminho(csOrigem, csDestino) == null);
                
                Tarefa tarefa = new Tarefa(
                        id,
                        "",
                        "",
                        csOrigem,
                        csDestino,
                        getTam(),
                        gerador.nextExponential(ArrivalTime)/*tempo de criação*/);
                tarefas.add(tarefa);
                id++;
            }
        }
        return tarefas;
    }

    public static List<CentroServico> getMenorCaminho(CentroServico origem, CentroServico destino) {
        // Cria vetor com distancia acumulada
        List<CentroServico> nosExpandidos = new ArrayList<>();
        List<Object[]> caminho = new ArrayList<>();
        CentroServico atual = origem;
        // Armazena valor acumulado até atingir o nó atual
        Double acumulado = 0.0;
        do {
            // Recebe a lista de saidas de um objeto
            ArrayList<CentroServico> lista = (ArrayList<CentroServico>) atual.getConexoesSaida();
            // Percorre a lista procurando o caminho
            for (CentroServico cs : lista) {
                Object caminhoItem[] = new Object[4];
                caminhoItem[0] = atual;

                if (cs instanceof CS_Comunicacao && cs != destino) {
                    CS_Comunicacao comu = (CS_Comunicacao) cs;
                    caminhoItem[1] = comu.tempoTransmitir(10000) + acumulado;
                    caminhoItem[2] = cs;
                } else {
                    caminhoItem[1] = 0.0 + acumulado;
                    caminhoItem[2] = cs;
                }
                caminhoItem[3] = acumulado;
                caminho.add(caminhoItem);
            }
            // Marca que o nó atual foi expandido
            nosExpandidos.add(atual);
            // Inicia variavel de menor caminho com maior valor possivel
            Object[] menorCaminho = new Object[4];
            menorCaminho[0] = null;
            menorCaminho[1] = Double.MAX_VALUE;
            menorCaminho[2] = null;
            menorCaminho[3] = Double.MAX_VALUE;
            // Busca menor caminho não expandido
            for (Object[] obj : caminho) {
                Double menor = (Double) menorCaminho[1];
                Double objAtual = (Double) obj[1];
                
                if (menor > objAtual && !(nosExpandidos.contains((CentroServico) obj[2]))) {
                    menorCaminho = obj;
                }
            }
            // Atribui valor a atual com resultado da busca do menor caminho
            atual = (CentroServico) menorCaminho[2];
            acumulado = (Double) menorCaminho[1];
        } while (atual != null && atual != destino);
        
        if (atual == null) {
            return null;
        } else if (atual == destino) {
            List<CentroServico> menorCaminho = new ArrayList<>();
            List<CentroServico> inverso = new ArrayList<>();
            Object[] obj;
            while (atual != origem) {
                int i = 0;
                do {
                    obj = caminho.get(i);
                    i++;
                } while (obj[2] != atual);
                inverso.add(atual);
                atual = (CentroServico) obj[0];
            }
            for (int j = inverso.size() - 1; j >= 0; j--) {
                menorCaminho.add(inverso.get(j));
            }
            return menorCaminho;
        } else {
            return null;
        }
    }
    
    @Override
    public String toString() {
        return String.format("%d %f",
                this.NumTars,
                this.Tam);
    }

    public boolean isDistribuirIgual() {
        return distribuirIgual;
    }

    public void setDistribuirIgual(boolean distribuirIgual) {
        this.distribuirIgual = distribuirIgual;
    }

    public double getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(double ArrivalTime) {
        this.ArrivalTime = ArrivalTime;
    }

    @Override
    public int getTipo() {
        return GerarCarga.UNIFORM;
    }

    public int getNumTars() {
        return NumTars;
    }

    public void setNumTars(int NumTars) {
        this.NumTars = NumTars;
    }

    public double getTam() {
        return Tam;
    }

    public void setTam(double Tam) {
        this.Tam = Tam;
    }
}
