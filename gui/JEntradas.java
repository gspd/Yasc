/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yasc.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.*;
import sun.swing.table.DefaultTableCellHeaderRenderer;
import yasc.gui.iconico.AreaDesenho;
import yasc.gui.iconico.Vertice;
import yasc.gui.iconico.grade.Instantaneo;

public class JEntradas extends javax.swing.JDialog {

    private final LinkedList<Vertice> listOrigin;
    private final AreaDesenho ad;
    private DefaultTableModel tm;
    
    public JEntradas(Frame parent, boolean modal, AreaDesenho ad, LinkedList<Vertice> listOrigin) {
        super(parent, modal);
        initComponents();
        this.ad = ad;
        this.listOrigin = listOrigin;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Definir valores das variáveis do sistema");
        setMinimumSize(new java.awt.Dimension(660, 254));

        jScrollPane1.setBorder(null);
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable1.setMinimumSize(new java.awt.Dimension(10, 0));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void inicializar() {
        setListOrigin();
        gerarTabela();
    }
    
    private void setListOrigin() {
        if (listOrigin.isEmpty()) {
            if (!ad.getVertices().isEmpty()) {
                for (Vertice v : ad.getVertices()) {
                    if (((Instantaneo) v).isOrigination()) {
                        listOrigin.add(v);
                    }
                }
            }
        }
    }
    
    private void gerarTabela() {
        //Criar colunas
        tm = new DefaultTableModel();
        jTable1.setModel(tm);
        double f = JPrincipal.clock.getFrequency();        
        
        tm.addColumn("Objetos");
        for (Vertice v : listOrigin) {
            String[] obj = { ((Instantaneo) v).getId().getNome() };
            tm.addRow(obj);
            
            for (int i = 0; i < ((Instantaneo) v).getVars().size(); i++) {
                String[] entrada = { ((Instantaneo) v).getVars().get(i) };
                tm.addRow(entrada);
            }
        }
        
        // Craete the pulse columns
        for (int i = 0; i < JPrincipal.clock.getNumberPulse(); i++) {
            tm.addColumn(i+f);
        }
        
        // Sets dimension of rows and columns and text aligment
        jTable1.setRowHeight(35);
        TableColumnModel column = jTable1.getColumnModel();
        
        column.getColumn(0).setMinWidth(90);
        for (int i = 1; i < column.getColumnCount(); i++)
            column.getColumn(i).setMinWidth(70);
        
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Resgata os valores inseridos na tabela
        if (!listOrigin.isEmpty()) {
            int vars, i, j, k = 1;
            
            try {
                jTable1.getCellEditor().stopCellEditing();
            } catch (NullPointerException e) { }
            
            for (Vertice v : listOrigin) {
                vars = ((Instantaneo) v).getVars().size();
                for (i = 0; i < vars; i++) {
                    ArrayList<String> listaEntradas = new ArrayList<>();
                    for (j = 1; j <= JPrincipal.clock.getNumberPulse(); j++) {
                        System.out.println(tm.getValueAt(i + k, j));
                        listaEntradas.add(tm.getValueAt(i + k, j).toString());
                    }
                    ((Instantaneo) v).getEntradas().add(listaEntradas);
                }
                k = (k + i + vars) - 1;
            }
        }
        for (int i = 0; i < tm.getRowCount(); i++) {
            tm.removeRow(i);
        }
        
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
