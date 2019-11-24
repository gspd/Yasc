package yasc.gui.iconico.grade;

import java.util.Set;

/**
 * Objeto da grade de desenho
 */
public interface ItemGrade {

    /**
     * Identifica o objeto pelo seu id
     * @return 
     */
    public IdentificadorItemGrade getId();

    /**
     * @return Conexões de entrada do objeto
     */
    public Set<ItemGrade> getConexoesEntrada();

    /**
     * @return Conexões de saída do objeto
     */
    public Set<ItemGrade> getConexoesSaida();
    
    /**
     * @return Atributos do objeto
     */
    public String getAtributos();

    /**
     * Faz a cópia de um objeto selecionado
     * @param posicaoMouseX the value of posicaoMouseX
     * @param posicaoMouseY the value of posicaoMouseY
     * @param idGlobal the value of idGlobal
     * @param idLocal the value of idLocal
     */
    public ItemGrade criarCopia(int posicaoMouseX, int posicaoMouseY, int idGlobal, int idLocal);
    
    /**
     * Verifica se o objeto foi configurado corretamente
     * @return Objeto configurado
     */
    public boolean isConfigurado();
}
