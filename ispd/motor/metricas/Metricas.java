/* ==========================================================
 * iSPD : iconic Simulator of Parallel and Distributed System
 * ==========================================================
 *
 * (C) Copyright 2010-2014, by Grupo de pesquisas em Sistemas Paralelos e Distribuídos da Unesp (GSPD).
 *
 * Project Info:  http://gspd.dcce.ibilce.unesp.br/
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ---------------
 * Metricas.java
 * ---------------
 * (C) Copyright 2014, by Grupo de pesquisas em Sistemas Paralelos e Distribuídos da Unesp (GSPD).
 *
 * Original Author:  Denison Menezes (for GSPD);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 
 * 09-Set-2014 : Version 2.0;
 *
 */
package ispd.motor.metricas;

import ispd.motor.filas.RedeDeFilas;
import ispd.motor.filas.Tarefa;
import ispd.motor.filas.servidores.CS_Comunicacao;
import ispd.motor.filas.servidores.CS_Processamento;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author denison
 */
public class Metricas implements Serializable {

    private int numeroDeSimulacoes;
    private RedeDeFilas redeDeFilas;
    private List<Tarefa> tarefas;
    /**
     * Armazena métricas obtidas da simulação
     */
    private MetricasGlobais metricasGlobais;
    private List<String> usuarios;
    private Map<String, MetricasComunicacao> metricasComunicacao;
    private Map<String, MetricasProcessamento> metricasProcessamento;
    private Map<String, Double> metricasSatisfacao;
    private Map<String, Integer> tarefasConcluidas;
    private double tempoMedioFilaComunicacao;
    private double tempoMedioComunicacao;
    private double tempoMedioFilaProcessamento;
    private double tempoMedioProcessamento;
    private double UnudadesDesperdicio;
    private int numTarefasCanceladas;
    private int numTarefas;

    public Metricas(List<String> usuarios) {
        this.numeroDeSimulacoes = 0;
        this.metricasGlobais = new MetricasGlobais();
        this.usuarios = usuarios;
        tempoMedioFilaComunicacao = 0;
        tempoMedioComunicacao = 0;
        tempoMedioFilaProcessamento = 0;
        tempoMedioProcessamento = 0;
        UnudadesDesperdicio = 0;
        numTarefasCanceladas = 0;
        numTarefas = 0;
    }

    public Metricas(RedeDeFilas redeDeFilas, double time, List<Tarefa> tarefas) {
        this.numeroDeSimulacoes = 1;
        this.metricasGlobais = new MetricasGlobais(redeDeFilas, time, tarefas);
        metricasSatisfacao = new HashMap<String, Double>();
        tarefasConcluidas = new HashMap<String, Integer>();
        this.usuarios = redeDeFilas.getUsuarios();
        for (String user : usuarios) {
            metricasSatisfacao.put(user, 0.0);
            tarefasConcluidas.put(user, 0);
        }
        getMetricaFilaTarefa(tarefas, redeDeFilas);
        getMetricaComunicacao(redeDeFilas);
        getMetricaProcessamento(redeDeFilas);


    }

    public void addMetrica(Metricas metrica) {
        addMetricasGlobais(metrica.getMetricasGlobais());
        addMetricaFilaTarefa(metrica);
        addMetricaComunicacao(metrica.getMetricasComunicacao());
        addMetricaProcessamento(metrica.getMetricasProcessamento());
        addMetricaSatisfacao(metrica.getMetricasSatisfacao(), metrica.tarefasConcluidas);
        this.numeroDeSimulacoes += metrica.numeroDeSimulacoes;
    }

    public RedeDeFilas getRedeDeFilas() {
        return redeDeFilas;
    }

    public void setRedeDeFilas(RedeDeFilas redeDeFilas) {
        this.redeDeFilas = redeDeFilas;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }
    
    public int getNumeroDeSimulacoes() {
        return numeroDeSimulacoes;
    }

    public MetricasGlobais getMetricasGlobais() {
        return metricasGlobais;
    }

    public List<String> getUsuarios() {
        return usuarios;
    }

    public Map<String, MetricasComunicacao> getMetricasComunicacao() {
        return metricasComunicacao;
    }

    public Map<String, MetricasProcessamento> getMetricasProcessamento() {
        return metricasProcessamento;
    }

    public Map<String, Double> getMetricasSatisfacao() {
        return metricasSatisfacao;
    }

    public double getTempoMedioFilaComunicacao() {
        return tempoMedioFilaComunicacao;
    }

    public double getTempoMedioComunicacao() {
        return tempoMedioComunicacao;
    }

    public double getTempoMedioFilaProcessamento() {
        return tempoMedioFilaProcessamento;
    }

    public double getTempoMedioProcessamento() {
        return tempoMedioProcessamento;
    }

    public double getUnudadesDesperdicio() {
        return UnudadesDesperdicio;
    }

    public int getNumTarefasCanceladas() {
        return numTarefasCanceladas;
    }

    public int getNumTarefas() {
        return numTarefas;
    }

    public void calculaMedia() {
        //Média das Metricas Globais
        metricasGlobais.setTempoSimulacao(metricasGlobais.getTempoSimulacao() / numeroDeSimulacoes);
        metricasGlobais.setSatisfacaoMedia(metricasGlobais.getSatisfacaoMedia() / numeroDeSimulacoes);
        metricasGlobais.setOciosidadeTarefa(metricasGlobais.getOciosidadeTarefa() / numeroDeSimulacoes);
        metricasGlobais.setOciosidadeComunicacaoTarefa(metricasGlobais.getOciosidadeTarefa() / numeroDeSimulacoes);
        metricasGlobais.setEficiencia(metricasGlobais.getEficiencia() / numeroDeSimulacoes);
        //Média das Metricas da rede de filas
        this.tempoMedioFilaComunicacao = this.tempoMedioFilaComunicacao / numeroDeSimulacoes;
        this.tempoMedioComunicacao = this.tempoMedioComunicacao / numeroDeSimulacoes;
        this.tempoMedioFilaProcessamento = this.tempoMedioFilaProcessamento / numeroDeSimulacoes;
        this.tempoMedioProcessamento = this.tempoMedioFilaProcessamento / numeroDeSimulacoes;
        this.UnudadesDesperdicio = this.UnudadesDesperdicio / numeroDeSimulacoes;
        this.numTarefasCanceladas = this.numTarefasCanceladas / numeroDeSimulacoes;
        //Média das Metricas de Comunicação
        for (Map.Entry<String, MetricasComunicacao> entry : metricasComunicacao.entrySet()) {
            String key = entry.getKey();
            MetricasComunicacao item = entry.getValue();
            item.setUnidadesTransmitidas(item.getUnidadesTransmitidas() / numeroDeSimulacoes);
            item.setSegundosDeTransmissao(item.getSegundosDeTransmissao() / numeroDeSimulacoes);
        }
        //Média das Metricas de Processamento
        for (Map.Entry<String, MetricasProcessamento> entry : metricasProcessamento.entrySet()) {
            String key = entry.getKey();
            MetricasProcessamento item = entry.getValue();
            item.setUnidadeProcessada(item.getUnidadeProcessada() / numeroDeSimulacoes);
            item.setSegundosDeProcessamento(item.getSegundosDeProcessamento() / numeroDeSimulacoes);
        }

        for (Map.Entry<String, Double> entry : this.metricasSatisfacao.entrySet()) {

            entry.setValue(entry.getValue() / numeroDeSimulacoes);

        }
    }

    private void getMetricaFilaTarefa(List<Tarefa> tarefas, RedeDeFilas rede) {
        this.tempoMedioFilaComunicacao = 0;
        this.tempoMedioComunicacao = 0;
        this.tempoMedioFilaProcessamento = 0;
        this.tempoMedioProcessamento = 0;
        this.numTarefasCanceladas = 0;
        this.UnudadesDesperdicio = 0;
        this.numTarefas = 0;

        Double mediaPoder = 0.0;
        for (int i = 0; i < rede.getMaquinas().size(); i++) {
            mediaPoder += rede.getMaquinas().get(i).getPoderComputacional();
        }
        mediaPoder = mediaPoder / rede.getMaquinas().size();
        for (Tarefa no : tarefas) {
            if (no.getEstado() == Tarefa.CONCLUIDO) {

                Double suij;
                CS_Processamento maq = (CS_Processamento) no.getHistoricoProcessamento().get(0);
                suij = (no.getTamProcessamento() / mediaPoder / (no.getTempoFinal().get(no.getTempoFinal().size() - 1) - no.getTimeCriacao())) * (100);
                metricasSatisfacao.put(no.getProprietario(), suij + metricasSatisfacao.get(no.getProprietario()));
                tarefasConcluidas.put(no.getProprietario(), 1 + tarefasConcluidas.get(no.getProprietario()));

            }
            if (no.getEstado() == Tarefa.CONCLUIDO) {
                tempoMedioFilaComunicacao += no.getMetricas().getTempoEsperaComu();
                tempoMedioComunicacao += no.getMetricas().getTempoComunicacao();
                tempoMedioFilaProcessamento = no.getMetricas().getTempoEspera();
                tempoMedioProcessamento = no.getMetricas().getTempoProcessamento();
                numTarefas++;
            } else if (no.getEstado() == Tarefa.CANCELADO) {
                UnudadesDesperdicio += no.getTamProcessamento() * no.getMflopsProcessado();
                numTarefasCanceladas++;
            }
            //Rever, se for informação pertinente adicionar nas métricas da tarefa ou CS_Processamento e calcula durante a simulação
            CS_Processamento temp = (CS_Processamento) no.getLocalProcessamento();
            if (temp != null) {
                for (int i = 0; i < no.getTempoInicial().size(); i++) {
                    temp.setTempoProcessamento(no.getTempoInicial().get(i), no.getTempoFinal().get(i));
                }
            }
        }

        for (Map.Entry<String, Double> entry : metricasSatisfacao.entrySet()) {

            String string = entry.getKey();
            entry.setValue(entry.getValue() / tarefasConcluidas.get(string));

        }

        tempoMedioFilaComunicacao = tempoMedioFilaComunicacao / numTarefas;
        tempoMedioComunicacao = tempoMedioComunicacao / numTarefas;
        tempoMedioFilaProcessamento = tempoMedioFilaProcessamento / numTarefas;
        tempoMedioProcessamento = tempoMedioProcessamento / numTarefas;
    }

    private void getMetricaComunicacao(RedeDeFilas redeDeFilas) {
        metricasProcessamento = new HashMap<String, MetricasProcessamento>();
        for (CS_Processamento maq : redeDeFilas.getMestres()) {
            metricasProcessamento.put(maq.getId() + maq.getnumeroMaquina(), maq.getMetrica());
        }
        for (CS_Processamento maq : redeDeFilas.getMaquinas()) {
            metricasProcessamento.put(maq.getId() + maq.getnumeroMaquina(), maq.getMetrica());
        }
    }

    private void getMetricaProcessamento(RedeDeFilas redeDeFilas) {
        metricasComunicacao = new HashMap<String, MetricasComunicacao>();
        for (CS_Comunicacao link : redeDeFilas.getInternets()) {
            metricasComunicacao.put(link.getId(), link.getMetrica());
        }
        for (CS_Comunicacao link : redeDeFilas.getLinks()) {
            metricasComunicacao.put(link.getId(), link.getMetrica());
        }
    }

    private void addMetricasGlobais(MetricasGlobais global) {
        metricasGlobais.setTempoSimulacao(metricasGlobais.getTempoSimulacao() + global.getTempoSimulacao());
        metricasGlobais.setSatisfacaoMedia(metricasGlobais.getSatisfacaoMedia() + global.getSatisfacaoMedia());
        metricasGlobais.setOciosidadeTarefa(metricasGlobais.getOciosidadeTarefa() + global.getOciosidadeTarefa());
        metricasGlobais.setOciosidadeComunicacaoTarefa(metricasGlobais.getOciosidadeComunicacaoTarefa() + global.getOciosidadeComunicacaoTarefa());
        metricasGlobais.setEficiencia(metricasGlobais.getEficiencia() + global.getEficiencia());
    }

    private void addMetricaComunicacao(Map<String, MetricasComunicacao> metricasComunicacao) {
        if (numeroDeSimulacoes == 0) {
            this.metricasComunicacao = metricasComunicacao;
        } else {
            for (Map.Entry<String, MetricasComunicacao> entry : metricasComunicacao.entrySet()) {
                String key = entry.getKey();
                MetricasComunicacao item = entry.getValue();
                MetricasComunicacao base = this.metricasComunicacao.get(key);
                base.incUnidadesTransmitidas(item.getUnidadesTransmitidas());
                base.incSegundosDeTransmissao(item.getSegundosDeTransmissao());
            }
        }
    }

    private void addMetricaProcessamento(Map<String, MetricasProcessamento> metricasProcessamento) {
        if (numeroDeSimulacoes == 0) {
            this.metricasProcessamento = metricasProcessamento;
        } else {
            for (Map.Entry<String, MetricasProcessamento> entry : metricasProcessamento.entrySet()) {
                String key = entry.getKey();
                MetricasProcessamento item = entry.getValue();
                MetricasProcessamento base = this.metricasProcessamento.get(key);
                base.incUnidadeProcessada(item.getUnidadeProcessada());
                base.incSegundosProcessados(item.getSegundosDeProcessamento());
            }
        }
    }

    private void addMetricaFilaTarefa(Metricas metrica) {
        this.tempoMedioFilaComunicacao += metrica.tempoMedioFilaComunicacao;
        this.tempoMedioComunicacao += metrica.tempoMedioComunicacao;
        this.tempoMedioFilaProcessamento += metrica.tempoMedioFilaProcessamento;
        this.tempoMedioProcessamento += metrica.tempoMedioFilaProcessamento;
        this.UnudadesDesperdicio += metrica.UnudadesDesperdicio;
        this.numTarefasCanceladas += metrica.numTarefasCanceladas;
    }

    private void addMetricaSatisfacao(Map<String, Double> metricasSatisfacao, Map<String, Integer> tarefasConcluidasUser) {
        if (numeroDeSimulacoes == 0) {
            this.metricasSatisfacao = metricasSatisfacao;
            this.tarefasConcluidas = tarefasConcluidasUser;
        } else {
            for (Map.Entry<String, Double> entry : this.metricasSatisfacao.entrySet()) {
                String string = entry.getKey();
                entry.setValue(entry.getValue() + metricasSatisfacao.get(string));
            }
        }
    }
}
