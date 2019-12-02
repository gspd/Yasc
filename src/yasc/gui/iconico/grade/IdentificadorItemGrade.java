package yasc.gui.iconico.grade;

public class IdentificadorItemGrade {

    private Integer idLocal;
    private Integer idGlobal;
    private String nome;

    public IdentificadorItemGrade(int idLocal, int idGlobla, String nome) {
        this.idLocal = idLocal;
        this.idGlobal = idGlobla;
        this.nome = nome;
    }

    public Integer getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Integer idLocal) {
        this.idLocal = idLocal;
    }

    public Integer getIdGlobal() {
        return idGlobal;
    }

    public void setIdGlobal(Integer idGlobal) {
        this.idGlobal = idGlobal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
