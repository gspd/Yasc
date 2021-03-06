package yasc.gui.entrada;

import static yasc.gui.entrada.FinalizaConfiguracao.escrita;
import java.awt.Toolkit;
import java.io.IOException;
import yasc.Main;
import java.util.logging.Level;
import java.util.logging.Logger;
import yasc.gui.JPrincipal;
import yasc.gui.LogExceptions;
import javax.swing.JFrame;

public class Encerramento extends JFrame {
    String stringCaminho;

    public Encerramento(String stringCaminho) {
        this.stringCaminho = stringCaminho;
        initComponents();
    }

    /**
     * This method is called from within thedessa vez fiz 6*, se a gte perder a culpa eh do Voti, n eh toco? constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        EncerramentoSim = new javax.swing.JButton();
        EncerramentoNao = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("imagens/gspd.png")));
        setResizable(false);

        jPanel1.setLayout(new java.awt.GridLayout());

        EncerramentoSim.setText(Main.languageResource.getString("Yes")); // NOI18N
        EncerramentoSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncerramentoSimActionPerformed(evt);
            }
        });

        EncerramentoNao.setText(Main.languageResource.getString("No")); // NOI18N
        EncerramentoNao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncerramentoNaoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabel1.setText(Main.languageResource.getString("Is your description of simulator completed")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(42, 42, 42)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(EncerramentoSim, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(EncerramentoNao, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EncerramentoSim)
                    .addComponent(EncerramentoNao))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void EncerramentoNaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncerramentoNaoActionPerformed
        // TODO add your handling code here:
        if (evt.getSource() == EncerramentoNao){
            FinalizaConfiguracao dialog = new FinalizaConfiguracao();
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }   
            });
            this.setVisible(false);
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_EncerramentoNaoActionPerformed

    private void EncerramentoSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncerramentoSimActionPerformed
        // TODO add your handling code here:
        if (evt.getSource() == EncerramentoSim) {
            try {
                escrita(ListasArmazenamento.lista);
            } catch (IOException ex) {
                Logger.getLogger(Gerar.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setVisible(false);
            CarregaCria executa = new CarregaCria();
            String separator = System.getProperty("file.separator");
            // WTF?????
            executa.leituraArquivo("simulatorlibrary" + separator + "simulador.conf");
            LogExceptions logExceptions = new LogExceptions(null);
            JPrincipal gui = null;
            try {
                gui = new JPrincipal();
            } catch (IOException ex) {
                Logger.getLogger(Encerramento.class.getName()).log(Level.SEVERE, null, ex);
            }
            gui.setLocationRelativeTo(null);
            logExceptions.setParentComponent(gui);
            gui.setVisible(true);
        }
    }//GEN-LAST:event_EncerramentoSimActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EncerramentoNao;
    private javax.swing.JButton EncerramentoSim;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
