package yasc.motor;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import yasc.Main;
import yasc.arquivo.xml.IconicoXML;
import yasc.arquivo.xml.ManipuladorXML;
import yasc.gui.JPrincipal;
import yasc.gui.iconico.grade.Instantaneo;
import yasc.motor.filas.RedeDeFilas;
import yasc.motor.filas.Tarefa;
import yasc.motor.filas.servidores.CentroServico;
import yasc.motor.filas.servidores.implementacao.CS_Instantaneo;
import yasc.motor.metricas.Metricas;

/**
 * Classe de conexão entre interface de usuario e motor de simulação
 */
public abstract class ProgressoSimulacao {

    public void println(String text, Color cor) {
        this.print(text, cor);
        this.print("\n", cor);
    }

    public void println(String text) {
        this.print(text, Color.black);
        this.print("\n", Color.black);
    }

    public void print(String text) {
        this.print(text, Color.black);
    }

    public abstract void incProgresso(int n);

    public abstract void print(String text, Color cor);

    public void validarInicioSimulacao(Document modelo) throws IllegalArgumentException {
        if (modelo == null) {
            this.println("Error!", Color.red);
            throw new IllegalArgumentException("The model has no icons.");
        }
        try {
            IconicoXML.validarModelo(modelo);
        } catch (IllegalArgumentException e) {
            this.println("Error!", Color.red);
            throw e;
        }
        this.incProgresso(5);
    }
    
    public void validarRDF(RedeDeFilas redeDeFilas) throws IllegalArgumentException {
        if (redeDeFilas.getOrigens().isEmpty()) {
            this.println("Error!", Color.red);
            throw new IllegalArgumentException("The model has no originations.");
        }
        if (redeDeFilas.getDestinos().isEmpty()) {
            this.println("Error!", Color.red);
            throw new IllegalArgumentException("The model has no destinations.");
        }
    }
    
    // Simulação de eventos com base no tempo
    // Necessário atualizar os valores das entradas dos objetos de origem antes de chamar essa função
    public Metricas simulacaoDiscreta(Document doc) {
        // Criar grade
        this.print("Mounting network queue.");
        
        this.print(" -> ");
        incProgresso(5);//[5%] --> 5%
        this.println("OK", Color.green);
        incProgresso(10);//[10%] --> 15%
        this.println("OK", Color.green);
        incProgresso(5);//[5%] --> 20 %
        this.print("Creating routing.");
        this.print(" -> ");
        incProgresso(15);//[15%] --> 35 %
        this.println("OK", Color.green);
        // Realiza a simulação
        this.println("Simulating.");
        // Recebe instante de tempo em milissegundos ao iniciar a simulação
        double t1 = System.currentTimeMillis();
        
        // Gerar eventos
        SimulacaoSequencial sim = null;
        List<Tarefa> tarefas = null;
        RedeDeFilas redeDeFilas = null;
        Map<Double, String> res = new HashMap<>();
        int i;
        
        for (i = 0; i < JPrincipal.clock.getListPulse().size(); i++) {
            tarefas = new ArrayList<>();
            int identificador = 0;
            
            validarInicioSimulacao(doc);
            redeDeFilas = IconicoXML.newRedeDeFilas(doc);
            validarRDF(redeDeFilas);
            
            // Atualiza os valores das variáveis a cada pulso do relógio
            for (CentroServico obj : redeDeFilas.getOrigens()) {
                int l = 0;
                String id = ((Instantaneo) JPrincipal.listOrigin.get(l)).getId().getNome();
                String obj_id = obj.getId();
                
                while (!obj_id.equals(id)) {
                    l++;
                    id = ((Instantaneo) JPrincipal.listOrigin.get(l)).getId().getNome();
                }
                Instantaneo origin = (Instantaneo) JPrincipal.listOrigin.get(l);
                ArrayList<String> entradas = new ArrayList<>();

                for (int p = 0; p < origin.getEntradas().size(); p++) {
                    entradas.add(origin.getEntradas().get(p).get(i));
                }
                ((CS_Instantaneo) obj).setValues(entradas);
            }
            
            // Gera a tarefa que gera os eventos
            //for (CentroServico cs : redeDeFilas.getOrigens()) {
                tarefas.add(gerarTarefa(identificador, redeDeFilas.getOrigens().get(0),
                        (CS_Instantaneo) redeDeFilas.getDestinos().get(0), JPrincipal.clock.getListPulse().get(i)));
                //identificador++;
            //}
            sim = new SimulacaoSequencial(this, redeDeFilas, tarefas);
            sim.simular();
            // Salva a solucao do circuito por pulso de clock
            CS_Instantaneo o = (CS_Instantaneo) redeDeFilas.getDestinos().get(0);
            String r = (String) o.getSolucaoCircuito();
            res.put(JPrincipal.clock.getListPulse().get(i), r);
            CS_Instantaneo.setAllStaticNull();
            Main.ordena = true;
        }
        incProgresso(30);//[30%] --> 65%
        this.println("OK", Color.green);
        // Recebe instante de tempo em milissegundos ao fim da execução da simulação
        double t2 = System.currentTimeMillis();
        // Calcula tempo de simulação em segundos
        double tempototal = (t2 - t1) / (1000 * i);
        this.println("Simulation Execution Time = " + tempototal + "seconds");
        // Obter Resultados
        this.print("Getting Results.");
        this.print(" -> ");
        Metricas metrica = sim.getMetricas();
        metrica.setResultadoPorClock(res);
        incProgresso(10);//[10%] --> 75%
        this.println("OK", Color.green);
        metrica.getMetricasGlobais();
        metrica.setRedeDeFilas(redeDeFilas);
        metrica.setTarefas(tarefas);
        incProgresso(25); //[75%] --> 100%
        
        return metrica;
    }
    
    private Tarefa gerarTarefa(int identificador, CentroServico csOrigem, CentroServico csDestino, double clockTime) {
        Tarefa tarefa = new Tarefa(identificador, "", "", csOrigem, csDestino, 1, clockTime);
        
        return tarefa;
    }

    public Metricas simulacaoSequencial(Document doc) {
        // Criar grade
        this.print("Mounting network queue.");
        validarInicioSimulacao(doc);
        this.print(" -> ");
        RedeDeFilas redeDeFilas = IconicoXML.newRedeDeFilas(doc);
        validarRDF(redeDeFilas);
        incProgresso(5);//[5%] --> 5%
        this.println("OK", Color.green);
        // Criar tarefas
        this.print("Creating tasks.");
        this.print(" -> ");
        List<Tarefa> tarefas = IconicoXML.newGerarCarga(doc).toTarefaList(redeDeFilas);
        incProgresso(10);//[10%] --> 15%
        this.println("OK", Color.green);
        // Verifica recursos do modelo e define roteamento
        Simulacao sim = new SimulacaoSequencial(this, redeDeFilas, tarefas);
        incProgresso(5);//[5%] --> 20 %
        this.print("Creating routing.");
        this.print(" -> ");
        incProgresso(15);//[15%] --> 35 %
        this.println("OK", Color.green);
        // Realiza a simulação
        this.println("Simulating.");
        // Recebe instante de tempo em milissegundos ao iniciar a simulação
        double t1 = System.currentTimeMillis();
        sim.simular();
        incProgresso(30);//[30%] --> 65%
        this.println("OK", Color.green);
        // Recebe instante de tempo em milissegundos ao fim da execução da simulação
        double t2 = System.currentTimeMillis();
        // Calcula tempo de simulação em segundos
        double tempototal = (t2 - t1) / 1000;
        this.println("Simulation Execution Time = " + tempototal + "seconds");
        // Obter Resultados
        this.print("Getting Results.");
        this.print(" -> ");
        Metricas metrica = sim.getMetricas();
        incProgresso(10);//[10%] --> 75%
        this.println("OK", Color.green);
        metrica.getMetricasGlobais();
        metrica.setRedeDeFilas(redeDeFilas);
        metrica.setTarefas(tarefas);
        incProgresso(25); //[75%] --> 100%
        
        return metrica;
    }

    public Metricas simulacaoSequencial(Document doc, int simNumbers) {
        Metricas metricas = new Metricas(IconicoXML.newListUsers(doc));
        RedeDeFilas redeDeFilas = null;
        List<Tarefa> tarefas = null;
        if (simNumbers <= 0) {
            throw new ExceptionInInitializerError("Number of simulations must be positive!");
        }
        int progresso = 70 / simNumbers;
        double t1 = System.currentTimeMillis();
        for (int i = 0; i < simNumbers; i++) {
            this.print("\n");
            this.print("Prepare Simulation");
            this.print(" " + i + " -> ");

            // Recebe instante de tempo em milissegundos ao iniciar a simulação
            double start = System.currentTimeMillis();
            double temp1 = System.currentTimeMillis();
            redeDeFilas = IconicoXML.newRedeDeFilas(doc);
            // Rriar tarefas
            tarefas = IconicoXML.newGerarCarga(doc).toTarefaList(redeDeFilas);
            // Verifica recursos do modelo e define roteamento
            Simulacao sim = new SimulacaoSequencial(this, redeDeFilas, tarefas);//[10%] --> 40 %
            // Define roteamento
            sim.criarRoteamento();
            // Recebe instante de tempo em milissegundos ao fim da execução da simulação
            double t2 = System.currentTimeMillis();
            this.print("OK", Color.green);
            this.println(" time " + (t2 - temp1) + " ms");

            // Realiza asimulação
            this.print("Simulating");
            this.print(" -> ");
            temp1 = System.currentTimeMillis();
            sim.simular();//[30%] --> 70%
            t2 = System.currentTimeMillis();
            this.print("OK", Color.green);
            this.println(" time " + (t2 - temp1) + " ms");

            this.print("Getting Results.");
            this.print(" -> ");
            // Obter Resultados
            temp1 = System.currentTimeMillis();
            Metricas metrica = sim.getMetricas();
            metricas.addMetrica(metrica);
            t2 = System.currentTimeMillis();
            incProgresso(progresso);
            this.print("OK", Color.green);
            this.println(" time " + (t2 - temp1) + " ms");
            this.println("Execution Time = " + (t2 - start) + "ms");
        }
        // Calcula tempo de simulação em segundos
        double tempototal = (System.currentTimeMillis() - t1) / 1000;
        this.print("\n");
        this.println("Total Execution Time = " + tempototal + "seconds");
        if (simNumbers > 1) {
            metricas.calculaMedia();
        }
        incProgresso(75-(progresso*simNumbers));
        metricas.setRedeDeFilas(redeDeFilas);
        metricas.setTarefas(tarefas);
        
        return metricas;
    }

    public Metricas simulacaoOtimista(Document doc, int threads, int simNumbers) {
        Metricas metricas = new Metricas(IconicoXML.newListUsers(doc));
        RedeDeFilas redeDeFilas = null;
        List<Tarefa> tarefas = null;
        if (simNumbers <= 0) {
            throw new ExceptionInInitializerError("Number of simulations must be positive!");
        }
        if (threads <= 0) {
            throw new ExceptionInInitializerError("Number of threads must be positive!");
        }
        int progresso = 70 / simNumbers;
        double t1 = System.currentTimeMillis();
        for (int i = 1; i <= simNumbers; i++) {
            this.print("\n");
            this.print("Prepare Simulation");
            this.print(" " + i + " -> ");

            // Recebe instante de tempo em milissegundos ao iniciar a simulação
            double start = System.currentTimeMillis();
            double temp1 = System.currentTimeMillis();
            // Criar grade
            redeDeFilas = IconicoXML.newRedeDeFilas(doc);
            tarefas = IconicoXML.newGerarCarga(doc).toTarefaList(redeDeFilas);
            // Verifica recursos do modelo e define roteamento
            Simulacao sim = new SimulacaoParalela(this, redeDeFilas, tarefas, threads);
            // Define roteamento
            sim.criarRoteamento();
            // Recebe instante de tempo em milissegundos ao fim da execução da simulação
            double t2 = System.currentTimeMillis();
            this.print("OK", Color.green);
            this.println(" time " + (t2 - temp1) + " ms");

            // Realiza asimulação
            this.print("Simulating");
            this.print(" -> ");
            temp1 = System.currentTimeMillis();
            sim.simular();
            t2 = System.currentTimeMillis();
            this.print("OK", Color.green);
            this.println(" time " + (t2 - temp1) + " ms");

            this.print("Getting Results.");
            this.print(" -> ");
            // Obter Resultados
            Metricas temp = sim.getMetricas();
            metricas.addMetrica(temp);
            t2 = System.currentTimeMillis();
            incProgresso(progresso);
            this.print("OK", Color.green);
            this.println(" time " + (t2 - temp1) + " ms");
            this.println("Execution Time = " + (t2 - start) + "ms");
        }
        // Calcula tempo de simulação em segundos
        double tempototal = (System.currentTimeMillis() - t1) / 1000;
        this.print("\n");
        this.println("Total Execution Time = " + tempototal + "seconds");
        if (simNumbers > 1) {
            metricas.calculaMedia();
        }
        incProgresso(75 - (progresso*simNumbers));
        metricas.setRedeDeFilas(redeDeFilas);
        metricas.setTarefas(tarefas);
        
        return metricas;
    }

    public Metricas simulacoesParalelas(File arquivo, int numThreads, int numExecucoes) throws Exception {
        Document modelo[] = ManipuladorXML.clone(arquivo, numThreads);
        // Verifica se foi construido modelo corretamente
        this.validarInicioSimulacao(modelo[0]);
        return simulacoesParalelas(modelo, numThreads, numExecucoes);
    }

    public Metricas simulacoesParalelas(Document documento, int numThreads, int numExecucoes) throws Exception {
        Document modelo[] = ManipuladorXML.clone(documento, numThreads);
        return simulacoesParalelas(modelo, numThreads, numExecucoes);
    }

    private Metricas simulacoesParalelas(Document modelo[], int numThreads, int numExecucoes) throws Exception {
        if (numExecucoes <= 0) {
            throw new ExceptionInInitializerError("Number of simulations must be positive!");
        }
        if (numThreads <= 0) {
            throw new ExceptionInInitializerError("Number of threads must be positive!");
        }
        if (numThreads > numExecucoes) {
            numThreads = numExecucoes;
        }
        Metricas metricas = new Metricas(IconicoXML.newListUsers(modelo[0]));
        int progresso = 70 / numThreads;
        int inicio = 0, incremento = numExecucoes / numThreads;
        RunnableImpl[] trabalhador = new RunnableImpl[numThreads];
        Thread[] thread = new Thread[numThreads];
        this.println("Will run " + numThreads + " threads");
        double t1 = System.currentTimeMillis();
        for (int i = 0; i < numThreads - 1; i++) {
            trabalhador[i] = new RunnableImpl(modelo[i], incremento);
            thread[i] = new Thread(trabalhador[i]);
            thread[i].start();
            inicio += incremento;
        }
        trabalhador[numThreads - 1] = new RunnableImpl(modelo[numThreads - 1], numExecucoes - inicio);
        thread[numThreads - 1] = new Thread(trabalhador[numThreads - 1]);
        thread[numThreads - 1].start();
        for (int i = 0; i < numThreads; i++) {
            thread[i].join();
            incProgresso(progresso);
        }
        double t2 = System.currentTimeMillis();
        // Calcula tempo de simulação em segundos
        double tempototal = (t2 - t1) / 1000;
        this.println("Total Execution Time = " + tempototal + " seconds");
        this.print("Getting Results.");
        this.print(" -> ");
        if (numExecucoes > 1) {
            for (int i = 0; i < numThreads; i++) {
                metricas.addMetrica(trabalhador[i].getMetricas());
            }
            metricas.calculaMedia();
        }
        this.println("OK", Color.GREEN);
        incProgresso(75 - (progresso*numThreads));
        metricas.setRedeDeFilas(trabalhador[numThreads - 1].metricas.getRedeDeFilas());
        metricas.setTarefas(trabalhador[numThreads - 1].metricas.getTarefas());
        return metricas;
    }

    /**
     * Classe interna para executar uma simulação em thread
     */
    private class RunnableImpl implements Runnable {

        private final Document modelo;
        private final int numExecucaoThread;
        private Metricas metricas;
        private final ProgressoSimulacao progSim = new ProgressoSimulacao() {
            @Override
            public void incProgresso(int n) {
            }

            @Override
            public void print(String text, Color cor) {
            }
        };

        public RunnableImpl(Document modelo, int numExecucao) {
            this.modelo = modelo;
            this.numExecucaoThread = numExecucao;
            this.metricas = new Metricas(null);
        }

        public Metricas getMetricas() {
            return metricas;
        }

        @Override
        public void run() {
            RedeDeFilas redeDeFilas = null;
            List<Tarefa> tarefas = null;
            for (int i = 0; i < numExecucaoThread; i++) {
                // Criar grade
                redeDeFilas = IconicoXML.newRedeDeFilas(modelo);
                tarefas = IconicoXML.newGerarCarga(modelo).toTarefaList(redeDeFilas);
                // Verifica recursos do modelo e define roteamento
                Simulacao sim = new SimulacaoSequencial(progSim, redeDeFilas, tarefas);
                // Define roteamento
                sim.criarRoteamento();
                // Realiza asimulação
                sim.simular();
                Metricas temp = sim.getMetricas();
                metricas.addMetrica(temp);
            }
            metricas.setRedeDeFilas(redeDeFilas);
            metricas.setTarefas(tarefas);
        }
    }
}
