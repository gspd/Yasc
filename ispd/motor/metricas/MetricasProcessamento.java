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
 * MetricasProcessamento.java
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

import java.io.Serializable;

/**
 * Cada centro de serviço usado para processamento deve ter um objeto desta classe
 * Responsavel por armazenar o total de processamento realizado em MFlops e segundos
 * @author denison
 */
public class MetricasProcessamento implements Serializable{
    /**
     * Armazena o total de processamento realizado em MFlops
     */
    /**
     * Comentário pós alteração:
     * --Armazena o total de processamento realizado na unidade da simulação--
     */
    private double UnidadeProcessada;
    /**
     * armazena o total de processamento realizado em segundos
     */
    private double SegundosDeProcessamento;
    private String id;
    private String proprietario;
    private int numeroProcesso;
    
    public MetricasProcessamento(String id, int numeroProcesso, String proprietario) {
        this.UnidadeProcessada = 0;
        this.SegundosDeProcessamento = 0;
        this.id = id;
        this.numeroProcesso = numeroProcesso;
        this.proprietario = proprietario;
    }

    public void incUnidadeProcessada(double UnidadeProcessada) {
        this.UnidadeProcessada += UnidadeProcessada;
    }

    public void incSegundosProcessados(double SegundosProcessados) {
        this.SegundosDeProcessamento += SegundosProcessados;
    }

    public double getUnidadeProcessada() {
        return UnidadeProcessada;
    }

    public double getSegundosDeProcessamento() {
        return SegundosDeProcessamento;
    }

    public String getId() {
        return id;
    }

    public String getProprietario() {
        return proprietario;
    }

    public int getNumeroProcesso() {
        return numeroProcesso;
    }

    void setUnidadeProcessada(double d) {
        this.UnidadeProcessada = d;
    }

    void setSegundosDeProcessamento(double d) {
        this.SegundosDeProcessamento = d;
    }
}