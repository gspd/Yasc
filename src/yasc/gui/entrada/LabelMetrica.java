package yasc.gui.entrada;

import yasc.Main;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LabelMetrica extends javax.swing.JDialog {

    private boolean nDeterministico;
    private String label;
    private String metrica;
    
    /**
     * Creates new form LabelMetrica
     */
    public LabelMetrica(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setjLabelUnidade(boolean jLabelUnidade) {
        this.jLabelUnidade.setVisible(jLabelUnidade);
    }

    public void setjTextFieldMetric(boolean jTextFieldMetric) {
        this.jTextFieldMetric.setVisible(jTextFieldMetric);
    }

    public boolean isNDeterministico() {
        return nDeterministico;
    }

    public void setNDeterministico(boolean NDeterministico) {
        this.nDeterministico = NDeterministico;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String Label) {
        this.label = Label;
    }

    public String getMetrica() {
        return metrica;
    }

    public void setMetrica(String Metrica) {
        this.metrica = Metrica;
    }

    public JCheckBox getjCheckBoxDeterministico() {
        return jCheckBoxDeterministico;
    }

    public void setjCheckBoxDeterministico(boolean jCheckBoxDeterministico) {
        this.jCheckBoxDeterministico.setVisible(jCheckBoxDeterministico);
    }


    public void setjLabel5(String jLabel5) {
        this.jLabel5.setText(jLabel5);
    }

    public JLabel getjLabelNome() {
        return jLabelNome;
    }

    public void setjLabelNome(boolean jLabelNome) {
        this.jLabelNome.setVisible(jLabelNome);
    }

    public JTextField getjTextFieldName() {
        return jTextFieldName;
    }

    public void setjTextFieldName(boolean jTextFieldName) {
        this.jTextFieldName.setVisible(jTextFieldName);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabelUnidade = new javax.swing.JLabel();
        jTextFieldMetric = new javax.swing.JTextField();
        jCheckBoxDeterministico = new javax.swing.JCheckBox();
        jButtonOK = new javax.swing.JButton();
        jTextFieldName = new javax.swing.JTextField();
        jLabelNome = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(500, 327));
        setResizable(false);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel3.setText(Main.languageResource.getString("Please complete the text areas with the labels you want to appear in your simulator interface, referring to the queue parameters")); // NOI18N
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel5.setText("jLabel5");

        jLabelUnidade.setText(Main.languageResource.getString("Measurement Unit ")); // NOI18N

        jTextFieldMetric.setText("Ex: Mbits");
        jTextFieldMetric.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMetricActionPerformed(evt);
            }
        });
        jTextFieldMetric.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                apagarMetrica(evt);
            }
        });

        jCheckBoxDeterministico.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        jCheckBoxDeterministico.setText(Main.languageResource.getString("Add Non-Deterministic behaviour to this metric")); // NOI18N
        jCheckBoxDeterministico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDeterministicoActionPerformed(evt);
            }
        });

        jButtonOK.setText(Main.languageResource.getString("OK")); // NOI18N
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jTextFieldName.setText("Ex: Number of cores");
        jTextFieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNameActionPerformed(evt);
            }
        });
        jTextFieldName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                apagarNome(evt);
            }
        });

        jLabelNome.setText(Main.languageResource.getString("Name")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxDeterministico)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabelNome)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabelUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(jTextFieldMetric, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jButtonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelNome)
                        .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxDeterministico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldMetric, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(jButtonOK)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldMetricActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMetricActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMetricActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        // TODO add your handling code here:
        String aux, aux1;
        if(jLabel5.getText().equals(Main.languageResource.getString("Service Time"))){
            jTextFieldName.setText("-");
        }
        if(jLabel5.getText().equals(Main.languageResource.getString("Number of Queues"))){
            jTextFieldMetric.setText("-");
        }
        if(jLabel5.getText().equals(Main.languageResource.getString("Number of Servers"))){
            jTextFieldMetric.setText("-");
        }
        aux = (jTextFieldName.getText().trim());
        aux1 = (jTextFieldMetric.getText().trim());
        if (!aux.equals("") && !aux1.equals("")) {
            jTextFieldName.setText(jTextFieldName.getText().replaceAll(" ", "_"));
            jTextFieldMetric.setText(jTextFieldMetric.getText().replaceAll(" ", ""));
            setLabel(jTextFieldName.getText());
            setMetrica(jTextFieldMetric.getText());
            setNDeterministico(nDeterministico);
            this.setVisible(false);
        } else
            JOptionPane.showMessageDialog(this, Main.languageResource.getString("Complete all fields to continue"), Main.languageResource.getString("Message"), 1);
    }//GEN-LAST:event_jButtonOKActionPerformed

    private void jCheckBoxDeterministicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDeterministicoActionPerformed
        // TODO add your handling code here:
        nDeterministico = jCheckBoxDeterministico.isSelected();
    }//GEN-LAST:event_jCheckBoxDeterministicoActionPerformed

    private void jTextFieldNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNameActionPerformed

    private void apagarNome(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_apagarNome
        // TODO add your handling code here:
        if(jTextFieldName.getText().equals("Ex: Number of cores"))
            jTextFieldName.setText("");
    }//GEN-LAST:event_apagarNome

    private void apagarMetrica(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_apagarMetrica
        // TODO add your handling code here:
        if(jTextFieldMetric.getText().equals("Ex: Mbits"))
            jTextFieldMetric.setText("");
    }//GEN-LAST:event_apagarMetrica

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonOK;
    private javax.swing.JCheckBox jCheckBoxDeterministico;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelNome;
    private javax.swing.JLabel jLabelUnidade;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldMetric;
    private javax.swing.JTextField jTextFieldName;
    // End of variables declaration//GEN-END:variables
}