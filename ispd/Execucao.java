/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ispd;

import ispd.arquivo.xml.IconicoXML;
import ispd.motor.ProgressoSimulacao;
import ispd.motor.metricas.Metricas;
import java.awt.Color;
import java.io.File;
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
                simulador.println("Tempo: "+result.getMetricasGlobais().getTempoSimulacao());
            } catch (Exception ex) {
                Logger.getLogger(Execucao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("iSPD can not open the file: " + arquivoIn.getName());
        }
    }

    @Override
    public void incProgresso(int n) {
        
    }

    @Override
    public void print(String text, Color cor) {
        System.out.print(text);
    }
    
}
