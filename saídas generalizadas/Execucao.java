/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ispd;

import ispd.arquivo.xml.ConfiguracaoISPD;
import ispd.arquivo.xml.IconicoXML;
import ispd.motor.ProgressoSimulacao;
import ispd.motor.Simulacao;
import ispd.motor.SimulacaoSequencial;
import ispd.gui.JResultados;
import ispd.motor.filas.RedeDeFilas;
import ispd.motor.filas.Tarefa;
import ispd.motor.metricas.Metricas;
import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;

/**
 *
 * @author gabriel
 */
public class Execucao extends ProgressoSimulacao{
    /**
     * @param args the command line arguments
     */
    public static void execucao() {
        File arquivoIn = null;
        Execucao simulador = new Execucao();
        //if (args.length == 1) {
        //    arquivoIn = new File(args[0]);
        //} else {
       
        arquivoIn = new File("./src/ispd/test.imsx");
        //}
        if (arquivoIn.getName().endsWith(".imsx") && arquivoIn.exists()) {
            try {
                Document modelo = IconicoXML.ler(arquivoIn);
                Metricas result = simulador.simulacaoSequencial(modelo);
                ConfiguracaoISPD configuracao = new ConfiguracaoISPD();
                JResultados resultado = null; 
                resultado = new JResultados(resultado, result, result.getRedeDeFilas(), result.getTarefas(), configuracao);
                resultado.setLocationRelativeTo(resultado);
                resultado.setVisible(true);
                simulador.println("Tempo: "+result.getMetricasGlobais().getTempoSimulacao());
            } catch (Exception ex) {
                Logger.getLogger(Execucao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("iSPD can not open the file: " + arquivoIn.getName());
        }
    }
    
    public Metricas simulacaoSequencial(Document doc) {
        //criar grade
        this.print("Mounting network queue.");
        this.print(" -> ");
        RedeDeFilas redeDeFilas = IconicoXML.newRedeDeFilas(doc);
        incProgresso(5);//[5%] --> 5%
        this.println("OK", Color.green);
        //criar tarefas
        this.print("Creating tasks.");
        this.print(" -> ");
        List<Tarefa> tarefas = IconicoXML.newGerarCarga(doc).toTarefaList(redeDeFilas);
        incProgresso(10);//[10%] --> 15%
        this.println("OK", Color.green);
        //Verifica recursos do modelo e define roteamento
        Simulacao sim = new SimulacaoSequencial(this, redeDeFilas, tarefas);
        incProgresso(5);//[5%] --> 20 %
        this.print("Creating routing.");
        this.print(" -> ");
        sim.criarRoteamento();
        incProgresso(15);//[15%] --> 35 %
        this.println("OK", Color.green);
        //Realiza asimulação
        this.println("Simulating.");
        //recebe instante de tempo em milissegundos ao iniciar a simulação
        double t1 = System.currentTimeMillis();
        sim.simular();
        incProgresso(30);//[30%] --> 65%
        this.println("OK", Color.green);
        //Recebe instnte de tempo em milissegundos ao fim da execução da simulação
        double t2 = System.currentTimeMillis();
        //Calcula tempo de simulação em segundos
        double tempototal = (t2 - t1) / 1000;
        this.println("Simulation Execution Time = " + tempototal + "seconds");
        //Obter Resultados
        this.print("Getting Results.");
        this.print(" -> ");
        Metricas metrica = sim.getMetricas();
        incProgresso(10);//[10%] --> 75%
        this.println("OK", Color.green);
        metrica.setRedeDeFilas(redeDeFilas);
        metrica.setTarefas(tarefas);
        return metrica;
    }

    @Override
    public void incProgresso(int n) {
        
    }

    @Override
    public void print(String text, Color cor) {
        System.out.print(text);
    }
    
}
