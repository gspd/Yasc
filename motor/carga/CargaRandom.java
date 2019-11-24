package yasc.motor.carga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import yasc.motor.filas.RedeDeFilas;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CS_Comunicacao;
import yasc.motor.filas.servidores.CentroServico;
import yasc.motor.random.Distribution;

/**
 * Descreve como gerar tarefas na forma randomica
 */
public class CargaRandom extends GerarCarga {

    private int numeroTarefas;
    private double min;
    private double max;
    private double avg2;
    private double avgPoisson;
    private double avgNormal;
    private double desvPad;
    private double arrivalTime;
    private int tipoDistr;
    private boolean distribuirIgual;

    public CargaRandom(int numeroTarefas, double min, double max, double Avg2,
            double AvgPoisson, double AvgNormal, double DesvPad, int TipoDistr, boolean distribuirIgual, double ArrivalTime) {
        this.numeroTarefas = numeroTarefas;
        this.min = min;
        this.max = max;
        this.avg2 = Avg2;
        this.avgPoisson = AvgPoisson;
        this.avgNormal = AvgNormal;
        this.desvPad = DesvPad;
        this.tipoDistr = TipoDistr;
        this.distribuirIgual = distribuirIgual;
        this.arrivalTime = ArrivalTime;
    }

    @Override
    public List<Tarefa> toTarefaList(RedeDeFilas rdf) {
        List<Tarefa> tarefas = new ArrayList<>();
                
        if (!distribuirIgual) {
            List<CentroServico> destinosPossiveis;
            int identificador = 0;
            int quantidadePorOrigem = this.getNumeroTarefas() / rdf.getOrigens().size();
            int resto = this.getNumeroTarefas() % rdf.getOrigens().size();
            Distribution gerador = new Distribution((int) System.currentTimeMillis());
            
            for (CentroServico Or : rdf.getOrigens()) {
                int indexDest = 0;
                destinosPossiveis = new ArrayList<>();
                
                for (CentroServico Dest : rdf.getDestinos())
                    if (getMenorCaminho(Or, Dest) != null || Or == Dest)
                        destinosPossiveis.add(Dest);
                        
                for (int i = 0; i < quantidadePorOrigem; i++) {
                    double tam = 0;
                    
                    switch (tipoDistr) {
                        case 0:
                            tam = gerador.twoStageUniform(min, avg2, max);
                            break;
                        case 1:
                            tam = gerador.nextPoisson(avgPoisson);
                            break;
                        case 2:
                            tam = gerador.nextNormal(avgNormal, desvPad);
                            break;
                        default:
                            System.out.println("Variável de tipo de distribuição com valor inválido");
                            break;
                    }
                    
                    Tarefa tarefa = new Tarefa(
                            identificador,
                            "",
                            "",
                            Or,
                            destinosPossiveis.get(indexDest),
                            tam,
                            gerador.nextExponential(arrivalTime)/*tempo de criação*/);
                    tarefas.add(tarefa);
                    identificador++;
                    if (indexDest + 1 >= destinosPossiveis.size()) {
                        indexDest = 0;
                    } else {
                        indexDest++;
                    }
                }
            }
            
            for (int i = 0; i < resto; i++) {
                double tam = 0;
                
                switch (tipoDistr) {
                    case 0:
                        tam = gerador.twoStageUniform(min, avg2, max);
                        break;
                    case 1:
                        tam = gerador.nextPoisson(avgPoisson);
                        break;
                    case 2:
                        tam = gerador.nextNormal(avgNormal, desvPad);
                        break;
                    default:
                        System.out.println("Variável de tipo de distribuição com valor inválido");
                        break;
                }
                destinosPossiveis = new ArrayList<>();
                for (CentroServico Dest : rdf.getDestinos())
                    if (getMenorCaminho(rdf.getOrigens().get(0), Dest) != null || rdf.getOrigens().get(0) == Dest)
                        destinosPossiveis.add(Dest);

                Tarefa tarefa = new Tarefa(
                        identificador,
                        "",
                        "",
                        rdf.getOrigens().get(0),
                        destinosPossiveis.get(0),
                        tam,
                        gerador.nextExponential(arrivalTime)/*tempo de criação*/);
                tarefas.add(tarefa);
                identificador++;
            }
        } else {
            for (int i = 0; i < this.getNumeroTarefas(); i++) {
                Distribution gerador = new Distribution((int) System.currentTimeMillis());
                CentroServico Origem, Destino;
                Random rd = new Random(System.currentTimeMillis());
                int origemIndex, destinoIndex, id = 0;
                
                do {
                    origemIndex = Math.abs(rd.nextInt()%rdf.getOrigens().size());
                    destinoIndex = Math.abs(rd.nextInt()%rdf.getDestinos().size());
                    Origem = rdf.getOrigens().get(origemIndex);
                    Destino = rdf.getDestinos().get(destinoIndex);
                } while (getMenorCaminho(Origem, Destino) == null);
                
                double tam = 0;
                
                switch (tipoDistr) {
                    case 0:
                        tam = gerador.twoStageUniform(min, avg2, max);
                        break;
                    case 1:
                        tam = gerador.nextPoisson(avgPoisson);
                        break;
                    case 2:
                        tam = gerador.nextNormal(avgNormal, desvPad);
                        break;
                }
                
                Tarefa tarefa = new Tarefa(
                        id,
                        "",
                        "",
                        Origem,
                        Destino,
                        tam,
                        gerador.nextExponential(arrivalTime)/*tempo de criação*/);
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
                if (menor > objAtual && !nosExpandidos.contains((CentroServico) obj[2])) {
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
        return String.format("%f %f %f\n%f\n%f %f\n%d %d",
                this.min, this.avg2, this.max,
                this.avgPoisson, this.avgNormal, this.desvPad, this.tipoDistr, this.numeroTarefas);
    }
    
    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double ArrivalTime) {
        this.arrivalTime = ArrivalTime;
    }
    
    public boolean isDistribuirIgual() {
        return distribuirIgual;
    }

    public void setDistribuirIgual(boolean distribuirIgual) {
        this.distribuirIgual = distribuirIgual;
    }
    
    @Override
    public int getTipo() {
        return GerarCarga.RANDOM;
    }

    public double getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public double getAvg2() {
        return avg2;
    }

    public void setAvg2(int Avg2) {
        this.avg2 = Avg2;
    }

    public double getAvgPoisson() {
        return avgPoisson;
    }

    public void setAvgPoisson(double AvgPoisson) {
        this.avgPoisson = AvgPoisson;
    }

    public double getAvgNormal() {
        return avgNormal;
    }

    public void setAvgNormal(double AvgNormal) {
        this.avgNormal = AvgNormal;
    }

    public double getDesvPad() {
        return desvPad;
    }

    public void setDesvPad(double DesvPad) {
        this.desvPad = DesvPad;
    }

    public int getTipoDistr() {
        return tipoDistr;
    }

    public void setTipoDistr(int TipoDistr) {
        this.tipoDistr = TipoDistr;
    }

    public Integer getNumeroTarefas() {
        return numeroTarefas;
    }

    public void setNumeroTarefas(int numeroTarefas) {
        this.numeroTarefas = numeroTarefas;
    }
}
