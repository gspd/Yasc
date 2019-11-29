package yasc.motor.metricas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import yasc.motor.filas.Tarefa;

public class MetricasUsuarios {

    private final HashMap<String, Integer> usuarios;
    private final List<String> listaUsuarios;
    private final List<Double> poderComputacional;
    private final List<HashSet<Tarefa>> tarefasSubmetidas;
    private final List<HashSet<Tarefa>> tarefasConcluidas;

    public MetricasUsuarios() {
        usuarios = new HashMap<>();
        listaUsuarios = new ArrayList<>();
        poderComputacional = new ArrayList<>();
        tarefasSubmetidas = new ArrayList<>();
        tarefasConcluidas = new ArrayList<>();
    }

    public void addUsuario(String nome, Double poderComputacional) {
        this.listaUsuarios.add(nome);
        this.usuarios.put(nome, listaUsuarios.indexOf(nome));
        this.poderComputacional.add(poderComputacional);
        this.tarefasSubmetidas.add(new HashSet<>());
        this.tarefasConcluidas.add(new HashSet<>());
    }

    public void addAllUsuarios(List<String> nomes, List<Double> poderComputacional) {
        for (int i = 0; i < nomes.size(); i++) {
            this.listaUsuarios.add(nomes.get(i));
            this.usuarios.put(nomes.get(i), i);
            this.poderComputacional.add(poderComputacional.get(i));
            this.tarefasSubmetidas.add(new HashSet<>());
            this.tarefasConcluidas.add(new HashSet<>());
        }
    }

    public void addMetricasUsuarios(MetricasUsuarios mtc) {
        for (int i = 0; i < mtc.usuarios.size(); i++) {
            Integer index = this.usuarios.get(mtc.listaUsuarios.get(i));
            if (index == null) {
                this.listaUsuarios.add(mtc.listaUsuarios.get(i));
                this.usuarios.put(mtc.listaUsuarios.get(i), this.listaUsuarios.indexOf(mtc.listaUsuarios.get(i)));
                this.poderComputacional.add(mtc.poderComputacional.get(i));
                this.tarefasSubmetidas.add(mtc.tarefasSubmetidas.get(i));
                this.tarefasConcluidas.add(mtc.tarefasConcluidas.get(i));
            } else {
                this.tarefasSubmetidas.get(index).addAll(mtc.tarefasSubmetidas.get(i));
                this.tarefasConcluidas.get(index).addAll(mtc.tarefasConcluidas.get(i));
            }
        }
    }

    public void incTarefasSubmetidas(Tarefa tarefa) {
        int index = this.usuarios.get(tarefa.getProprietario());
        this.tarefasSubmetidas.get(index).add(tarefa);
    }

    public void incTarefasConcluidas(Tarefa tarefa) {
        int index = this.usuarios.get(tarefa.getProprietario());
        this.tarefasConcluidas.get(index).add(tarefa);
    }

    public HashSet<Tarefa> getTarefasConcluidas(String user) {
        Integer index = this.usuarios.get(user);
        if (index != null) {
            return tarefasConcluidas.get(index);
        }
        return null;
    }

    public int getSizeTarefasConcluidas(String user) {
        Integer index = usuarios.get(user);
        if (index != null) {
            return tarefasConcluidas.get(index).size();
        } else {
            return -1;
        }
    }

    public int getSizeTarefasSubmetidas(String user) {
        Integer index = usuarios.get(user);
        if (index != null) {
            return tarefasSubmetidas.get(index).size();
        } else {
            return -1;
        }
    }

    public double getMflopsTarefasSubmetidas(String user) {
        Integer index = usuarios.get(user);
        if (index != null) {
            double mflops = 0;
            for (Object tar : tarefasSubmetidas.get(index)) {
                Tarefa tarefa = (Tarefa) tar;
                mflops += tarefa.getTamProcessamento();
            }
            return mflops;
        } else {
            return -1;
        }
    }

    public double getMflopsTarefasConcluidas(String user) {
        Integer index = usuarios.get(user);
        if (index != null) {
            double mflops = 0;
            for (Object tar : tarefasConcluidas.get(index)) {
                Tarefa tarefa = (Tarefa) tar;
                mflops += tarefa.getTamProcessamento();
            }
            return mflops;
        } else {
            return -1;
        }
    }

    public List<HashSet<Tarefa>> getTarefasConcluidas() {
        return tarefasConcluidas;
    }

    public List<HashSet<Tarefa>> getTarefasSubmetidas() {
        return tarefasSubmetidas;
    }

    public double getPoderComputacional(String user) {
        Integer index = usuarios.get(user);
        if (index != -1) {
            return poderComputacional.get(index);
        } else {
            return -1;
        }
    }

    public List<String> getUsuarios() {
        return listaUsuarios;
    }

    public HashMap<String, Integer> getUsuariosMap() {
        return usuarios;
    }

    @Override
    public String toString() {
        String texto = "";
        for (int i = 0; i < usuarios.size(); i++) {
            texto += "Usuario: " + usuarios.get(i).toString() + " tarefas: sub " + tarefasSubmetidas.get(i).size() + " con " + tarefasConcluidas.get(i).size() + "\n";
        }
        return texto;
    }
}
