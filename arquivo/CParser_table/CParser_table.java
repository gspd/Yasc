package yasc.arquivo.CParser_table;

import java.util.*;
import javax.script.*;
import yasc.gui.entrada.Gerar;
import yasc.gui.iconico.grade.Instantaneo;
import yasc.motor.filas.servidores.implementacao.CS_Instantaneo;
import yasc.motor.metricas.MetricasComunicacao;

public class CParser_table implements CParser_tableConstants {

    public static List<String> tableOfNames = new ArrayList<String>();
    public static List<String> tableOfValues = new ArrayList<String>();
    public static String str = "";
    public static String valorFinal;
    public static int k;
    public static boolean origem;
    public static boolean destino;
    public static ArrayList<String> valores = new ArrayList<String>();
    public static ArrayList<String> ID = new ArrayList<String>();
    //Declarações das listas que armazenan as colunas
    public static ArrayList<String> inputColNames = new ArrayList<String>();
    public static ArrayList<String> OutputColNames = new ArrayList<String>();
    //Armazena valores da tabela também armazenado no parcer
    public static ArrayList<ArrayList<String>> InputValues = new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<String>> OutputValues = new ArrayList<ArrayList<String>>();
    /*Armazena valores referentes aos ids dos objs e valores*/
    public static ArrayList<ArrayList<String>> TableResolve = new ArrayList<ArrayList<String>>();
    public static ArrayList<String> idObj = new ArrayList<String>(); //posição 0 no TableResolve
    public static ArrayList<String> nomeVar = new ArrayList<String>(); //posição 1 no TableResolve
    public static ArrayList<String> valorVar = new ArrayList<String>(); //posição 2 no TableResolve
    /*Arrays para armazenar valores finais dos objs*/
    public static ArrayList<String> IdObjFinal = new ArrayList<String>();
    public static ArrayList<String> valorResult = new ArrayList<String>();
    /*Armazena objetos de origem e objetos destino*/
    public static ArrayList<ArrayList<String>> Passagem = new ArrayList<ArrayList<String>>();
    /*Armazena se o objeto é somente destino*/
    public static ArrayList<String> idDestino = new ArrayList<String>();
    public static ArrayList<String> valorDestino = new ArrayList<String>();
    public static ArrayList<String> tableNames = new ArrayList<String>();
    public static ArrayList<String> tableValues = new ArrayList<String>();
    public static int t;

    public static Object interpretaExp(CParser_table parser) throws ScriptException {
        int table;
        for (int i = 0; i < Gerar.entradas.size(); i++) {
            table = tableOfNames.size() - 1;
            if (table == Gerar.entradas.get(i).size()) {
                t = i;
            }
        }
        
        origem = CS_Instantaneo.ori;
        destino = CS_Instantaneo.dest;

        InputValues = Gerar.inputValues.get(t);
        OutputValues = Gerar.OutputValores.get(t);
        inputColNames = Gerar.entradas.get(t);
        OutputColNames = Gerar.saidas.get(t);
        System.out.println("colInput: " + inputColNames);
        System.out.println(InputValues);
        System.out.println("colOutput: " + OutputColNames);
        System.out.println(OutputValues);

        retornar();
        resolveFormula();
        return null;
    }

    // Hastable for storing typedef types
    private static Set types = new HashSet();

    // Stack for determining when the parser
    // is parsing a typdef definition.
    private static Stack typedefParsingStack = new Stack();

    // Returns true if the given string is
    // a typedef type.
    private static boolean isType(String type) {
        return types.contains(type);
    }

    // Add a typedef type to those already defined
    private static void addType(String type) {
        types.add(type);
    }

    // Prints out all the types used in parsing the c source
    private static void printTypes() {
        for (Iterator i = types.iterator(); i.hasNext();) {
            System.out.println(i.next());
        }
    }

    /*--------- Armazena os valores passados pelo usuário para o Array a ser analisado--------*/
    //This method recive the name of a variable and add his value at ArrayList valores 
    public static void retornar() {
        for (int i = 0; i < inputColNames.size(); i++) {
            if (inputColNames.get(i).equals(tableOfNames.get(i))) {
                tableNames.add(tableOfNames.get(i));
                tableValues.add(tableOfValues.get(i));
            }
        }
    }

    /*---------- analise dos valores armazenados ---------------------------------------------*/
    public static String resolveFormula() {
        System.out.println("Resolve formula");
        String res;
        k = Instantaneo.j;
        
        //caso o objeto seja de origem ou objeto unico
        if (true == origem) {
            if (true == destino) {
                res = analise(tableValues);
                idDestino.add(CS_Instantaneo.tarefaOrg.get(k));
                valorDestino.add(res);
                return res;
            } else {
                preencheTable();
                //System.out.println("pos preenche table");
                res = analise(tableValues);
                //System.out.println("pos res");
                valorResult.add(res);
                //System.out.println("pos valorResult");
            }
            Instantaneo.v++;
        } else {
            //caso o objeto seja somente destino
            if (true == destino) {
                //System.out.println("destino");
                preencheTable();
                trocaVars();
                res = analise(tableValues);
                valorResult.add(res);
                valorDestino.add(res);
                idDestino.add(CS_Instantaneo.tarefaOrg.get(k));
                valorDestino.add(res);
                TableResolve.add(idObj);
                TableResolve.add(nomeVar);
                TableResolve.add(valorVar);
                Passagem.add(CS_Instantaneo.ancestral);
                Passagem.add(CS_Instantaneo.goTo);
            } //caso o objeto seja um no de passagem
            else {
                if (false == origem) {
                    preencheTable();
                    trocaVars();
                    res = analise(tableValues);
                    valorResult.add(res);
                }
            }
        }
        return valorFinal;
    }

    //Preenche valores
    public static void preencheTable() {
        for (int i = 0; i < tableValues.size(); i++) {
            idObj.add(CS_Instantaneo.tarefaOrg.get(Instantaneo.j));
            nomeVar.add(tableNames.get(i));
            valorVar.add(tableValues.get(i));
        }
        Instantaneo.j++;
    }

    public static void trocaVars() {
        String atual = CS_Instantaneo.tarefaOrg.get(Instantaneo.v);
        String l, changeVar; // Variável que será trocada
        
        for (int j = 0; j < CS_Instantaneo.elemAtual.size(); j++) {
            if (atual.equals(CS_Instantaneo.elemAtual.get(j))) {
                changeVar = CS_Instantaneo.passagemOrg.get(j);

                if (j > valorResult.size())
                    l = "0";
                else
                    l = valorResult.get(j);
                
                //Troca valores na Table1
                corrigeValor(l, changeVar);
                //troca valores no array valores que será analisado
                corrigeArray(l, changeVar);
            }
        }
        Instantaneo.v++;
    }

    public static void corrigeValor(String l, String changeVar) {
        for (int i = 0; i < idObj.size(); i++) {
            if (CS_Instantaneo.tarefaOrg.get(Instantaneo.v).equals(idObj.get(i)) && nomeVar.get(i).equals(changeVar)) {
                valorVar.remove(i);
                valorVar.add(i, l);
            }
        }
    }

    public static void corrigeArray(String l, String changeVar) {
        int position;
        position = tableNames.indexOf(changeVar);
        tableValues.remove(position);
        tableValues.add(position, l);
    }

    //Analisa valores armazenados no ArrayList valores
    public static String analise(ArrayList<String> valores) {
        ArrayList<String> arrayInput;
        boolean cont = false;
        String retorno = "";
        int posicaoLinha;

        for (int i = 0; i < InputValues.size(); i++) {
            arrayInput = InputValues.get(i);

            for (int j = 0; j < tableNames.size(); j++) {
                cont = arrayInput.get(j).equals(valores.get(j));
            }

            if (cont == true) {
                posicaoLinha = i;
                i = InputValues.size();
                retorno = analiseOutput(posicaoLinha);
            }
        }//fim for externo
        tableValues.clear();
        tableNames.clear();
        MetricasComunicacao.valorSaida.add(retorno);

        return retorno;
    }

    public static String analiseOutput(int k) {
        ArrayList<String> arrayOutput;
        arrayOutput = OutputValues.get(k);
        
        return arrayOutput.get(0);
    }

    final public void constant() throws ParseException {
        jj_consume_token(26);
        jj_consume_token(INTEGER_LITERAL);
        jj_consume_token(27);
    }

    /**
     * Generated Token Manager.
     */
    public CParser_tableTokenManager token_source;
    SimpleCharStream jj_input_stream;
    /**
     * Current token.
     */
    public Token token;
    /**
     * Next token.
     */
    public Token jj_nt;
    private int jj_ntk;
    private int jj_gen;
    final private int[] jj_la1 = new int[2];
    static private int[] jj_la1_0;

    static {
        jj_la1_init_0();
    }

    private static void jj_la1_init_0() {
        jj_la1_0 = new int[]{0x4400000, 0x4400000,};
    }

    /**
     * Constructor with InputStream.
     * @param stream
     * @param Names
     * @param Values
     */
    public CParser_table(java.io.InputStream stream, List<String> Names, List<String> Values) {
        this(stream, null);
        CParser_table.tableOfNames = Names;
        CParser_table.tableOfValues = Values;
    }

    /**
     * Constructor with InputStream and supplied encoding
     */
    public CParser_table(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        
        token_source = new CParser_tableTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        
        for (int i = 0; i < 2; i++) {
            jj_la1[i] = -1;
        }
    }

    /**
     * Reinitialise.
     * @param stream
     */
    public void reInit(java.io.InputStream stream) {
        CParser_table.this.reInit(stream, null);
    }

    /**
     * Reinitialise.
     * @param stream
     * @param encoding
     */
    public void reInit(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream.ReInit(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        
        for (int i = 0; i < 2; i++) {
            jj_la1[i] = -1;
        }
    }

    /**
     * Constructor.
     * @param stream
     */
    public CParser_table(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new CParser_tableTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        
        for (int i = 0; i < 2; i++)
            jj_la1[i] = -1;
    }

    /**
     * Reinitialise.
     * @param stream
     */
    public void reInit(java.io.Reader stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        
        for (int i = 0; i < 2; i++)
            jj_la1[i] = -1;
    }

    /**
     * Constructor with generated Token Manager.
     * @param tm
     */
    public CParser_table(CParser_tableTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        
        for (int i = 0; i < 2; i++)
            jj_la1[i] = -1;
    }

    /**
     * Reinitialise.
     * @param tm
     */
    public void reInit(CParser_tableTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        
        for (int i = 0; i < 2; i++)
            jj_la1[i] = -1;
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken;
        System.out.println("In consome token");
        
        if ((oldToken = token).next != null)
            token = token.next;
        else
            token = token.next = token_source.getNextToken();
        
        jj_ntk = -1;
        if (token.kind == kind) {
            System.out.println("token.kind");
            jj_gen++;
            return token;
        }
        
        System.out.println("out if");
        token = oldToken;
        System.out.println(token);
        jj_kind = kind;
        System.out.println(jj_kind);
        throw generateParseException();
    }

    /**
     * Get the next Token.
     * @return 
     */
    final public Token getNextToken() {
        if (token.next != null)
            token = token.next;
        else
            token = token.next = token_source.getNextToken();
            
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    /**
     * Get the specific Token.
     * @param index
     * @return 
     */
    final public Token getToken(int index) {
        Token tk = token;
        
        for (int i = 0; i < index; i++) {
            if (tk.next != null) {
                tk = tk.next;
            } else {
                tk = tk.next = token_source.getNextToken();
            }
        }
        return tk;
    }

    private int jj_ntk() {
        if ((jj_nt = token.next) == null) {
            return (jj_ntk = (token.next = token_source.getNextToken()).kind);
        } else {
            return (jj_ntk = jj_nt.kind);
        }
    }

    private java.util.List<int[]> jj_expentries = new java.util.ArrayList<>();
    private int[] jj_expentry;
    private int jj_kind = -1;

    /**
     * Generate ParseException.
     * @return 
     */
    public ParseException generateParseException() {
        System.out.println("generateParseException()");
        jj_expentries.clear();
        System.out.println(" jj_expentries.clear()");
        boolean[] la1tokens = new boolean[28];
        System.out.println(" boolean[] la1tokens = new boolean[28];");
        
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        
        for (int i = 0; i < 2; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        
        for (int i = 0; i < 28; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.add(jj_expentry);
            }
        }
        int[][] exptokseq = new int[jj_expentries.size()][];
        
        for (int i = 0; i < jj_expentries.size(); i++)
            exptokseq[i] = jj_expentries.get(i);
        
        return new ParseException(token, exptokseq, tokenImage);
    }
}
