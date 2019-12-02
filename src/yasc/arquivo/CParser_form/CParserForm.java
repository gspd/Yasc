package yasc.arquivo.CParser_form;

import java.util.*;
import javax.script.*;
import yasc.gui.iconico.grade.Instantaneo;
import yasc.motor.filas.servidores.implementacao.CS_Instantaneo;
import yasc.motor.metricas.MetricasComunicacao;

public class CParserForm implements CParser_formConstants {

    public static List<String> listOfNames = new ArrayList<String>();
    public static List<String> listOfValues = new ArrayList<String>();
    public static String str = "";
    public static ArrayList<String> valores = new ArrayList<String>();
    public static ArrayList<String> sinais = new ArrayList<String>();
    public static String valorFinal;
    public static String a = "";
    public static int cont = 0;
    public static int pos;
    public static String var;
    public static boolean origem;
    public static boolean destino;
    public static String id;
    public static int k;
    //public static String link;
    public static ArrayList<String> idDestino = new ArrayList<String>();
    public static ArrayList<String> valorDestino = new ArrayList<String>();
    /*arrays para criação para tabela*/
    //Tabela para resolução do objeto
    public static ArrayList<ArrayList<String>> TableResolve = new ArrayList<ArrayList<String>>();
    //posição 0 no TableResolve
    public static ArrayList<String> idObj = new ArrayList<String>();
    //posição 1 no TableResolve
    public static ArrayList<String> nomeVar = new ArrayList<String>();
    //posição 2 no TableResolve
    public static ArrayList<String> valorVar = new ArrayList<String>();
    /* Arrays para armazenar valores finais */
    public static ArrayList<String> IdObjFinal = new ArrayList<String>();
    public static ArrayList<String> valorResult = new ArrayList<String>();

    public static ArrayList<ArrayList<String>> Passagem = new ArrayList<ArrayList<String>>();

    /** Hastable for storing typedef types */
    private static Set types = new HashSet();

    /** Stack for determining when the parser
     * is parsing a typdef definition.
     */
    static private int[] jj_la1_0;
    static {
        jj_la1_init_0();
    }
    
    public static Object interpretaExp(String Str, CParserForm parser) throws ScriptException {
        str = Str;
        origem = CS_Instantaneo.ori;
        destino = CS_Instantaneo.dest;

        try {
            parser.Formula();
            ResolveFormula();
        } catch (ParseException e) {
            System.out.println("Erro");
            System.out.println("C Parser Version 0.1Alpha:  Encountered errors during parse.");
        }
        return valorFinal;
    }

    /*-------Análise da fórmula e armazenamento dos valores iniciais------------*/
    /** This method recive the signal of equation and add at ArrayList sinais */
    public static void retornarSinais(String variavel) {
        sinais.add(variavel);
    }

    /** This method recive the name of a variable and add his value at ArrayList valores */
    public static void retornar(String variavel) {
        int posicao = listOfNames.indexOf(variavel);
        
        if (posicao != -1) {
            String valor = listOfValues.get(posicao);
            valores.add(valor);
        }
    }

    /*-------parte referente a análise da fómula como operação lógica------------*/
    public static String ResolveFormula() {
        String res;
        k = Instantaneo.j - 1;

        // Caso o objeto seja de origem ou objeto unico
        if (true == origem) {
            if (1 == valores.size()) {
                if (listOfNames.size() == 1) {
                    res = AnalisaArray(valores, sinais);
                    valorDestino.add(res);
                    return res;
                } else {
                    res = AnalisaArray(valores, sinais);
                    idDestino.add(IdObjFinal.get(k));
                    valorDestino.add(res);
                    return res;
                }
            } else {
                preencheTable();
                res = AnalisaArray(valores, sinais);
                valorResult.add(res);
            }
            Instantaneo.v++;
        } else {
            // Caso o objeto seja somente destino
            if (true == destino) {
                // Controla o valor de Instantaneo.j
                preencheTable();
                trocaVars();
                res = AnalisaArray(valores, sinais);
                valorResult.add(res);
                valorDestino.add(res);
                // Atualiza valor de k
                k = Instantaneo.j - 1;
                idDestino.add(CS_Instantaneo.tarefaOrg.get(k));

                valorDestino.add(res);
                TableResolve.add(idObj);
                TableResolve.add(nomeVar);
                TableResolve.add(valorVar);
                Passagem.add(CS_Instantaneo.ancestral);
                Passagem.add(CS_Instantaneo.goTo);
            } // Caso o objeto seja um no de passagem
            else {
                if (false == origem) {
                    preencheTable();
                    trocaVars();
                    res = AnalisaArray(valores, sinais);
                    valorResult.add(res);
                }
            }
        }

        return valorFinal;
    }

    // Preenche valores
    public static void preencheTable() {
        if (Instantaneo.j >= CS_Instantaneo.tarefaOrg.size())
            Instantaneo.j = 0;
        
        for (int i = 0; i < listOfValues.size(); i++) {
            nomeVar.add(listOfNames.get(i));
            valorVar.add(valores.get(i));
        }
        idObj.add(CS_Instantaneo.tarefaOrg.get(Instantaneo.j));
        Instantaneo.j++;
    }

    public static void trocaVars() {
        // Realiza controle da variável de contagem v
        if (Instantaneo.v >= CS_Instantaneo.tarefaOrg.size())
            Instantaneo.v = 0;
        
        String atual = CS_Instantaneo.tarefaOrg.get(Instantaneo.v);
        String l, changeVar; // Variável que será trocada

        for (int j = 0; j < CS_Instantaneo.elemAtual.size(); j++) {
            if (atual.equals(CS_Instantaneo.elemAtual.get(j)) && !CS_Instantaneo.passagemOrg.isEmpty()) {
                System.out.println("elemAtual: " + CS_Instantaneo.elemAtual);
                changeVar = CS_Instantaneo.passagemOrg.get(j);
                System.out.println("passagemOrg: " + CS_Instantaneo.passagemOrg);
                System.out.println("valorResult: " + valorResult);
                System.out.println(j);
                
                if (j > valorResult.size()) {
                    l = "0";
                } else {
                    l = valorResult.get(j);
                    System.out.println("l: " + l);
                }

                CorrigeValor(l, changeVar);
                CorrigeArray(l, changeVar);

            }
        }
        Instantaneo.v++;
    }

    public static void CorrigeValor(String l, String changeVar /*,String atual*/) {
        for (int i = 0; i < idObj.size(); i++) {
            if (IdObjFinal.get(k).equals(idObj.get(i)) && nomeVar.get(i).equals(changeVar)) {
                valorVar.remove(i);
                valorVar.add(i, l);
            }
        }
    }

    public static void CorrigeArray(String l, String changeVar) {
        int position;
        position = listOfNames.indexOf(changeVar);
        if (position != -1) {
            valores.remove(position);
            valores.add(position, l);
        }
    }

    public static String AnalisaArray(ArrayList<String> valores, ArrayList<String> sinais) {
        String value;
        String signal;
        String parcial = valores.get(0);
        int ka = 0;

        if (valores.size() == 1) {
            valorFinal = parcial;
            MetricasComunicacao.valorSaida.add(valorFinal);
            valores.clear();
        } else {
            for (int i = 1; i < valores.size(); i++) {
                value = valores.get(i);
                if (ka < sinais.size()) {
                    signal = sinais.get(ka);
                    analise(parcial, value, signal);
                }
                ka++;
                parcial = valorFinal;
            }
            MetricasComunicacao.valorSaida.add(valorFinal);
            //Limpa os Arrays para receber o próximo elemento
            valores.clear();
            sinais.clear();
        }

        return valorFinal;
    }

    private static void analise(String parcial, String value, String signal) {
        switch (signal) {
            //AND
            case "*":
                if ("x".equals(value) || "x".equals(parcial)) {
                    break;
                } else {
                    analiseAND(parcial, value);
                    break;
                }
            //OR
            case "+":
                if ("x".equals(value) || "x".equals(parcial)) {
                    break;
                } else {
                    analiseOR(parcial, value);
                    break;
                }

            //XOR
            case "/":
                if ("x".equals(value) || "x".equals(parcial)) {
                    break;
                } else {
                    analiseXOR(parcial, value);
                    break;
                }

            //Not	
            case "-":
                if ("x".equals(value) || "x".equals(parcial)) {
                    break;
                } else {
                    analiseNOT(parcial, value);
                    break;
                }
        }
    }

    //AND analise
    private static void analiseAND(String parcial, String value) {
        if ("1".equals(parcial) && "1".equals(value)) {
            valorFinal = "1";
        } else {
            valorFinal = "0";
        }
    }

    //OR analise
    private static void analiseOR(String parcial, String value) {
        if ("1".equals(parcial) || "1".equals(value)) {
            valorFinal = "1";
        } else {
            valorFinal = "0";
        }
    }

    //XOR analise
    private static void analiseXOR(String parcial, String value) {
        if ("1".equals(parcial) && "1".equals(value)
                || "0".equals(parcial) && "0".equals(value)) {
            valorFinal = "0";
        } else {
            valorFinal = "1";
        }
    }

    //NOT analise
    private static void analiseNOT(String parcial, String value) {
        if ("1".equals(parcial) || "1".equals(value)) {
            valorFinal = "0";
        } else {
            valorFinal = "1";
        }
    }
    private static void jj_la1_init_0() {
        jj_la1_0 = new int[]{0x10400000, 0x2000000, 0x10400000,};
    }


    /**
     * Generated Token Manager.
     */
    public CParser_formTokenManager token_source;
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
    final private int[] jj_la1 = new int[3];
    private java.util.List<int[]> jj_expentries = new java.util.ArrayList<>();

    private int[] jj_expentry;
    private int jj_kind = -1;

    /**
     * Constructor with InputStream.
     */
    public CParserForm(java.io.InputStream stream, List<String> names, List<String> values) {
        this(stream, null);
        CParserForm.listOfNames = names;
        CParserForm.listOfValues = values;
    }

    /**
     * Constructor with InputStream and supplied encoding
     */
    public CParserForm(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source = new CParser_formTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 3; i++) {
            jj_la1[i] = -1;
        }
    }
    /**
     * Constructor.
     */
    public CParserForm(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new CParser_formTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 3; i++) {
            jj_la1[i] = -1;
        }
    }
    /**
     * Constructor with generated Token Manager.
     */
    public CParserForm(CParser_formTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 3; i++) {
            jj_la1[i] = -1;
        }
    }
    final public void Formula() throws ParseException {
        label_1:
        while (true) {
            switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case IDENTIFIER:
                case 28:
                    ;
                    break;
                default:
                    jj_la1[0] = jj_gen;
                    break label_1;
            }
            Expression();
        }
        jj_consume_token(27);
    }
    final public void Expression() throws ParseException {
        UnaryExpression();
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case IDSIGNAL:
                Signal();
                break;
            default:
                jj_la1[1] = jj_gen;
                ;
        }
    }
    final public void Signal() throws ParseException {
        Token s;
        s = jj_consume_token(IDSIGNAL);
        retornarSinais(s.image);
    }
    final public void UnaryExpression() throws ParseException {
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case IDENTIFIER:
                Id();
                break;
            case 28:
                Constant();
                break;
            default:
                jj_la1[2] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
    }
    final public void Id() throws ParseException {
        Token t;
        t = jj_consume_token(IDENTIFIER);
        retornar(t.image);
    }
    final public void Constant() throws ParseException {
        jj_consume_token(28);
        jj_consume_token(INTEGER_LITERAL);
        jj_consume_token(29);
    }

    /**
     * Reinitialise.
     */
    public void ReInit(java.io.InputStream stream) {
        ReInit(stream, null);
    }

    /**
     * Reinitialise.
     */
    public void ReInit(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream.ReInit(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 3; i++) {
            jj_la1[i] = -1;
        }
    }


    /**
     * Reinitialise.
     */
    public void ReInit(java.io.Reader stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 3; i++) {
            jj_la1[i] = -1;
        }
    }


    /**
     * Reinitialise.
     */
    public void ReInit(CParser_formTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 3; i++) {
            jj_la1[i] = -1;
        }
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken;
        if ((oldToken = token).next != null) {
            token = token.next;
        } else {
            token = token.next = token_source.getNextToken();
        }
        jj_ntk = -1;
        if (token.kind == kind) {
            jj_gen++;
            return token;
        }
        token = oldToken;
        jj_kind = kind;
        throw generateParseException();
    }

    /**
     * Get the next Token.
     */
    final public Token getNextToken() {
        if (token.next != null) {
            token = token.next;
        } else {
            token = token.next = token_source.getNextToken();
        }
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    /**
     * Get the specific Token.
     */
    final public Token getToken(int index) {
        Token t = token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) {
                t = t.next;
            } else {
                t = t.next = token_source.getNextToken();
            }
        }
        return t;
    }

    private int jj_ntk() {
        if ((jj_nt = token.next) == null) {
            return (jj_ntk = (token.next = token_source.getNextToken()).kind);
        } else {
            return (jj_ntk = jj_nt.kind);
        }
    }


    /**
     * Generate ParseException.
     */
    public ParseException generateParseException() {
        jj_expentries.clear();
        boolean[] la1tokens = new boolean[30];
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 3; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 30; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.add(jj_expentry);
            }
        }
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = jj_expentries.get(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    /**
     * Enable tracing.
     */
    final public void enable_tracing() {
    }

    /**
     * Disable tracing.
     */
    final public void disable_tracing() {
    }

}
