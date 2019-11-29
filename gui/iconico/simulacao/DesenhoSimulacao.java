package yasc.gui.iconico.simulacao;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import yasc.gui.iconico.AreaDesenho;
import yasc.gui.iconico.Icone;
import yasc.gui.iconico.Vertice;
import yasc.motor.SimulacaoGrafica;
import yasc.motor.filas.servidores.CS_Comunicacao;
import yasc.motor.filas.servidores.CS_Processamento;
import yasc.motor.filas.servidores.CentroServico;
import yasc.motor.filas.servidores.implementacao.CS_FilaServidor;
import yasc.motor.filas.servidores.implementacao.CS_Maquina;
import yasc.motor.filas.servidores.implementacao.CS_ServidorInfinito;
import yasc.motor.filas.servidores.implementacao.CS_Switch;

public class DesenhoSimulacao extends AreaDesenho {

    private final int INCREMENTO = 100;
    private final Font fonte;
    private final SimulacaoGrafica sim;
    private int setas = 5;
    private int setaCont = 0;

    public DesenhoSimulacao(SimulacaoGrafica sim) {
        super(false, false, true, true);
        this.setUnits(INCREMENTO);
        this.sim = sim;
        this.fonte = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        HashMap<Object, Vertice> posicoes = new HashMap<>();
        int coluna = INCREMENTO;
        int linha = INCREMENTO;
        int pos_coluna = 0;
        int num_coluna = ((int) Math.sqrt(
                sim.getRedeDeFilas().getMaquinas().size()
                + sim.getRedeDeFilas().getMestres().size()
                + sim.getRedeDeFilas().getInternets().size())) + 1;
        for (CS_Maquina icone : sim.getRedeDeFilas().getMaquinas()) {
            Maquina maq = new Maquina(coluna, linha, icone);
            vertices.add(maq);
            posicoes.put(icone, maq);
            coluna += INCREMENTO;
            pos_coluna++;
            if (pos_coluna == num_coluna) {
                pos_coluna = 0;
                coluna = INCREMENTO;
                linha += INCREMENTO;
            }
        }
        for (CS_Processamento icone : sim.getRedeDeFilas().getMestres()) {
            Maquina maq = new Maquina(coluna, linha, icone);
            vertices.add(maq);
            posicoes.put(icone, maq);
            coluna += INCREMENTO;
            pos_coluna++;
            if (pos_coluna == num_coluna) {
                pos_coluna = 0;
                coluna = INCREMENTO;
                linha += INCREMENTO;
            }
        }
        for (CS_ServidorInfinito icone : sim.getRedeDeFilas().getInternets()) {
            Roteador rot = new Roteador(coluna, linha, icone);
            vertices.add(rot);
            posicoes.put(icone, rot);
            coluna += INCREMENTO;
            pos_coluna++;
            if (pos_coluna == num_coluna) {
                pos_coluna = 0;
                coluna = INCREMENTO;
                linha += INCREMENTO;
            }
        }
        for (CS_Comunicacao icone : sim.getRedeDeFilas().getLinks()) {
            if (!(icone instanceof CS_FilaServidor)) {
                Switch sw = new Switch(coluna, linha, (CS_Switch) icone);
                vertices.add(sw);
                posicoes.put(icone, sw);
                coluna += INCREMENTO;
                pos_coluna++;
                if (pos_coluna == num_coluna) {
                    pos_coluna = 0;
                    coluna = INCREMENTO;
                    linha += INCREMENTO;
                }
            }
        }
        
        for (CS_Comunicacao cs_link : sim.getRedeDeFilas().getLinks()) {
            if (cs_link instanceof CS_FilaServidor) {
                CS_FilaServidor link = (CS_FilaServidor) cs_link;
                Link lk = new Link(posicoes.get(link.getConexoesEntrada()), posicoes.get(link.getConexoesSaida()), link, this);
                arestas.add(lk);
            } else {
                Vertice origm = posicoes.get(cs_link);

                for (CentroServico destino : ((CS_Switch) cs_link).getConexoesSaida()) {
                    Vertice destn = posicoes.get(destino);
                    if (destn != null) {
                        Link lk = new Link(origm, destn, null, this);
                        arestas.add(lk);
                    }
                }
            }
        }
        this.setPreferredSize(new Dimension(num_coluna * INCREMENTO + INCREMENTO, linha + INCREMENTO));
    }

    public Font getFonte() {
        return fonte;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Animação das setas transmitindo dados nas conexões
        if (setaCont == 100 && setas == 5) {
            setas = 8;
            setaCont = 0;
        } else if (setaCont == 100) {
            setas = 5;
            setaCont = 0;
        }
        setaCont++;
        g.setFont(fonte);
        super.paintComponent(g);
        if (!sim.isParar()) {
            this.repaint();
        }
    }

    public int getSetas() {
        return setas;
    }

    @Override
    public void adicionarVertice(int x, int y) { }

    @Override
    public void adicionarAresta(Vertice originVertice, Vertice destinyVertice,
            Vertice originPoint, Vertice destinyPoint) { }

    @Override
    public void showActionIcon(MouseEvent me, Icone icon) { }

    @Override
    public void botaoPainelActionPerformed(ActionEvent evt) { }

    @Override
    public void botaoVerticeActionPerformed(ActionEvent evt) { }

    @Override
    public void botaoArestaActionPerformed(ActionEvent evt) { }

    @Override
    public void botaoIconeActionPerformed(ActionEvent evt) { }

    @Override
    public void mouseEntered(MouseEvent me) {
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent me) {
        repaint();
    }

    @Override
    public void showSelectionIcon(MouseEvent me, Icone icon) {}
}
