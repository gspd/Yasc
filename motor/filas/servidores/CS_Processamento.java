package yasc.motor.filas.servidores;

import java.util.ArrayList;
import java.util.List;
import yasc.escalonador.Mestre;
import yasc.gui.auxiliar.ParesOrdenadosUso;
import yasc.motor.filas.servidores.implementacao.CS_FilaServidor;
import yasc.motor.filas.servidores.implementacao.CS_ServidorInfinito;
import yasc.motor.filas.servidores.implementacao.CS_Switch;
import yasc.motor.metricas.MetricasProcessamento;
import java.util.Collections;

/**
 * Classe abstrata que representa os servidores de processamento do modelo de fila,
 * Esta classe possui atributos referente a este ripo de servidor, e indica como
 * calcular o tempo gasto para processar uma tarefa.
 */
public abstract class CS_Processamento extends CentroServico {

    /**
     * Identificador do centro de serviço, deve ser o mesmo do modelo icônico
     */
    private final double poderComputacional;
    private final int numeroProcessadores;
    private final double ocupacao;
    private final double poderComputacionalDisponivelPorProcessador;
    private final MetricasProcessamento metrica;
    private final List<ParesOrdenadosUso> listaPares = new ArrayList<>();
    
    public CS_Processamento(String id, String proprietario, double PoderComputacional, int numeroProcessadores, double Ocupacao, int numeroMaquina) {
        this.poderComputacional = PoderComputacional;
        this.numeroProcessadores = numeroProcessadores;
        this.ocupacao = Ocupacao;
        this.metrica = new MetricasProcessamento(id, numeroMaquina, proprietario);
        this.poderComputacionalDisponivelPorProcessador =
                (this.poderComputacional - (this.poderComputacional * this.ocupacao))
                / this.numeroProcessadores;
    }
    
    public int getnumeroMaquina(){
        return metrica.getnumeroMaquina();
    }
    
    public double getOcupacao() {
        return ocupacao;
    }

    public double getPoderComputacional() {
        return poderComputacional;
    }

    @Override
    public String getId() {
        return metrica.getId();
    }
    
    public String getProprietario() {
        return metrica.getProprietario();
    }

    public int getNumeroProcessadores() {
        return numeroProcessadores;
    }

    public double tempoProcessar(double Mflops) {
        return (Mflops / poderComputacionalDisponivelPorProcessador);
    }
    
    public double getMflopsProcessados(double tempoProc) {
        return (tempoProc * poderComputacionalDisponivelPorProcessador);
    }

    public MetricasProcessamento getMetrica() {
        return metrica;
    }

    /**
     * Utilizado para buscar as rotas entre os recursos e armazenar em uma tabela,
     * deve retornar em erro se não encontrar nenhum caminho
     */
    public abstract void determinarCaminhos() throws LinkageError;
    /**
     * Método que determina todas as conexões entre dois recursos
     * podendo haver conexões indiretas, passando por diverssos elementos de comunicação
     * @param inicio CS_Servidor_Infinito inicial
     * @param fim Recurso que está sendo buscado um caminho
     * @param itensVerificados
     * @return lista de caminho existentes
     */
    protected List<List> getCaminhos(CS_Comunicacao inicio, CentroServico fim, List itensVerificados) {
        List<List> lista = new ArrayList<>();
        // Adiciona camino para elementos diretamente conectados
        if (inicio instanceof CS_FilaServidor && inicio.getConexoesSaida().equals(fim)) {
            List<CentroServico> novoCaminho = new ArrayList<>();
            novoCaminho.add(inicio);
            novoCaminho.add(fim);
            lista.add(novoCaminho);
        } // Adiciona caminho para elementos diretamentes conectados por um switch
        else if (inicio instanceof CS_Switch) {
            CS_Switch Switch = (CS_Switch) inicio;
            for (CentroServico saida : Switch.getConexoesSaida()) {
                if (saida.equals(fim)) {
                    List<CentroServico> novoCaminho = new ArrayList<>();
                    novoCaminho.add(inicio);
                    novoCaminho.add(fim);
                    lista.add(novoCaminho);
                }
            }
        } // Faz chamada recursiva para novo caminho
        else if (inicio.getConexoesSaida() instanceof CS_ServidorInfinito && !itensVerificados.contains(inicio.getConexoesSaida())) {
            CS_ServidorInfinito net = (CS_ServidorInfinito) inicio.getConexoesSaida();
            itensVerificados.add(net);
            itensVerificados.remove(net);
        }
        if (lista.isEmpty()) {
            return null;
        } else {
            return lista;
        }
    }

    /**
     * Retorna o menor caminho entre dois recursos de processamento
     * @param origem recurso origem
     * @param destino recurso destino
     * @return caminho completo a partir do primeiro link até o recurso destino
     */
    public static List<CentroServico> getMenorCaminho(CS_Processamento origem, CS_Processamento destino) {
        // Cria vetor com distancia acumulada
        List<CentroServico> nosExpandidos = new ArrayList<>();
        List<Object[]> caminho = new ArrayList<>();
        CentroServico atual = origem;
        // Armazena valor acumulado até atingir o nó atual
        Double acumulado = 0.0;
        do {
            // Busca valores das conexões de saida do recurso atual e coloca no vetor caminho
            if (atual instanceof CS_FilaServidor) {
                Object caminhoItem[] = new Object[4];
                caminhoItem[0] = atual;
                if (atual.getConexoesSaida() instanceof CS_Processamento && atual.getConexoesSaida() != destino) {
                    caminhoItem[1] = Double.MAX_VALUE;
                    caminhoItem[2] = null;
                } else if (atual.getConexoesSaida() instanceof CS_Comunicacao) {
                    CS_Comunicacao cs = (CS_Comunicacao) atual.getConexoesSaida();
                    caminhoItem[1] = cs.tempoTransmitir(10000) + acumulado;
                    caminhoItem[2] = atual.getConexoesSaida();
                } else {
                    caminhoItem[1] = 0.0 + acumulado;
                    caminhoItem[2] = atual.getConexoesSaida();
                }
                caminhoItem[3] = acumulado;
                caminho.add(caminhoItem);
            } else {
                ArrayList<CentroServico> lista = (ArrayList<CentroServico>) atual.getConexoesSaida();
                for (CentroServico cs : lista) {
                    Object caminhoItem[] = new Object[4];
                    caminhoItem[0] = atual;
                    if (cs instanceof CS_Processamento && cs != destino) {
                        caminhoItem[1] = Double.MAX_VALUE;
                        caminhoItem[2] = null;
                    } else if (cs instanceof CS_Comunicacao) {
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
        if (atual == destino) {
            List<CentroServico> menorCaminho = new ArrayList<>();
            List<CentroServico> inverso = new ArrayList<>();
            Object[] obj;
            while (atual != origem) {
                System.out.println(atual);
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
        }
        return null;
    }

    /**
     * Retorna o menor caminho entre dois recursos de processamento indiretamente conectados
     * passando por mestres no caminho
     * @param origem recurso origem
     * @param destino recurso destino
     * @return caminho completo a partir do primeiro link até o recurso destino
     */
    public static List<CentroServico> getMenorCaminhoIndireto(CS_Processamento origem, CS_Processamento destino) {
        // Cria vetor com distancia acumulada
        ArrayList<CentroServico> nosExpandidos = new ArrayList<>();
        ArrayList<Object[]> caminho = new ArrayList<>();
        CentroServico atual = origem;
        // Armazena valor acumulado até atingir o nó atual
        Double acumulado = 0.0;
        do {
            // Busca valores das conexões de saida do recurso atual e coloca no vetor caminho
            if (atual instanceof CS_FilaServidor) {
                Object caminhoItem[] = new Object[4];
                caminhoItem[0] = atual;
                if (atual.getConexoesSaida() instanceof CS_Comunicacao) {
                    CS_Comunicacao cs = (CS_Comunicacao) atual.getConexoesSaida();
                    caminhoItem[1] = cs.tempoTransmitir(10000) + acumulado;
                    caminhoItem[2] = atual.getConexoesSaida();
                } else if (atual.getConexoesSaida() instanceof Mestre || atual.getConexoesSaida() == destino) {
                    caminhoItem[1] = 0.0 + acumulado;
                    caminhoItem[2] = atual.getConexoesSaida();
                } else {
                    caminhoItem[1] = Double.MAX_VALUE;
                    caminhoItem[2] = null;
                }
                caminhoItem[3] = acumulado;
                caminho.add(caminhoItem);
            } else {
                ArrayList<CentroServico> lista = (ArrayList<CentroServico>) atual.getConexoesSaida();
                for (CentroServico cs : lista) {
                    Object caminhoItem[] = new Object[4];
                    caminhoItem[0] = atual;
                    if (cs instanceof CS_Comunicacao) {
                        CS_Comunicacao comu = (CS_Comunicacao) cs;
                        caminhoItem[1] = comu.tempoTransmitir(10000) + acumulado;
                        caminhoItem[2] = cs;
                    } else if (cs instanceof Mestre || cs == destino) {
                        caminhoItem[1] = 0.0 + acumulado;
                        caminhoItem[2] = cs;
                    } else {
                        caminhoItem[1] = Double.MAX_VALUE;
                        caminhoItem[2] = null;
                    }
                    caminhoItem[3] = acumulado;
                    caminho.add(caminhoItem);
                }
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
        if (atual == destino) {
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
        }
        return null;
    }
    
     public void setTempoProcessamento(double inicio, double fim){
        ParesOrdenadosUso par = new ParesOrdenadosUso(inicio,fim);
        listaPares.add(par); 
    }
    
    public List getListaProcessamento(){
        Collections.sort(listaPares);
        return (this.listaPares);
    }

}