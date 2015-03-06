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
 * LogExceptions.java
 * ---------------
 * (C) Copyright 2014, by Grupo de pesquisas em Sistemas Paralelos e Distribuídos da Unesp (GSPD).
 *
 * Original Author:  Aldo Ianelo Guerra;
 * Contributor(s):   Denison Menezes;
 *
 * Changes
 * -------
 * 
 * 09-Set-2014 : Version 2.0;
 * 16-Out-2014 : change the location of the iSPD base directory;
 *
 */
package ispd.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogExceptions implements Thread.UncaughtExceptionHandler {

    private Component parentComponent;
    private JTextArea area;
    private JScrollPane scroll;

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public LogExceptions(Component gui) {
        this.parentComponent = gui;
        File diretorio = new File(LogExceptions.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        if (!diretorio.getName().endsWith(".jar")) {
            diretorio = new File("Erros");
        } else {
            diretorio = new File(diretorio.getParent() + "/Erros");
        }
        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
        //iniciar parte grafica
        area = new JTextArea();
        area.setEditable(false);
        scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 300));
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        ByteArrayOutputStream fosErr = new ByteArrayOutputStream();
        PrintStream psErr = new PrintStream(fosErr);
        e.printStackTrace(psErr);
        mostrarErro(fosErr);
    }

    private void mostrarErro(ByteArrayOutputStream objErr) {
        try {
            if (objErr.size() > 0) {
                String erro = "";
                erro += "\n---------- error description ----------\n";
                erro += objErr.toString();
                erro += "\n---------- error description ----------\n";
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date();
                String codigo = dateFormat.format(date);
                File file = new File("Erros/Error_iSPD_" + codigo);
                FileWriter writer = new FileWriter(file);
                PrintWriter saida = new PrintWriter(writer, true);
                saida.print(erro);
                saida.close();
                writer.close();
                String saidaString = "";
                saidaString += "Error encountered during system operation.\n";
                saidaString += "Error saved in the file: " + file.getAbsolutePath() + "\n";
                saidaString += "Please send the error to the developers.\n";
                saidaString += erro;
                area.setText(saidaString);
                JOptionPane.showMessageDialog(parentComponent, scroll, "System Error", JOptionPane.ERROR_MESSAGE);
                objErr.reset();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentComponent, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
