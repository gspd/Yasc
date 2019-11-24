package yasc.arquivo.CParser;

import java.util.*;
import javax.script.*;

public class CParser implements CParserConstants {

    public static List<String> tableOfNames = new ArrayList<String>();
    public static List<String> tableOfValues = new ArrayList<String>();
    public static String str = "";

    public static Object InterpretaExp(String Str, CParser parser) throws ScriptException {
        str = Str;
        System.out.println("Func:" + str);

        try {
            parser.Formula();
            try {
                ScriptEngine js = new ScriptEngineManager().getEngineByName("JavaScript");
                Object result;
                result = js.eval(str);
                
                return result;
            } catch (ScriptException ex) {
                return null;
            }
        } catch (ParseException e) {
            System.out.println("C Parser Version Alpha:  Encountered errors during parse.");
        }
        return null;
    }

    public static void retornar(String variavel) {//this method receive the name of a variable and returns your value
        int posicao = tableOfNames.indexOf(variavel);
        if (posicao != -1) {
            String valor = tableOfValues.get(posicao);
            str = str.replaceAll(variavel, valor);
        }
    }

    final public void Formula() throws ParseException {
        label_1:
        while (true) {
            switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case IDENTIFIER:
                case 30:
        ;
                    break;
                default:
                    jj_la1[0] = jj_gen;
                    break label_1;
            }
            Expression();
        }
        jj_consume_token(25);
    }

    final public void Expression() throws ParseException {
        UnaryExpression();
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case 26:
            case 27:
            case 28:
            case 29:
                Signal();
                break;
            default:
                jj_la1[1] = jj_gen;
                ;
        }
    }

    final public void Signal() throws ParseException {
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case 26:
                jj_consume_token(26);
                break;
            case 27:
                jj_consume_token(27);
                break;
            case 28:
                jj_consume_token(28);
                break;
            case 29:
                jj_consume_token(29);
                break;
            default:
                jj_la1[2] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    final public void UnaryExpression() throws ParseException {
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case IDENTIFIER:
                Id();
                break;
            case 30:
                Constant();
                break;
            default:
                jj_la1[3] = jj_gen;
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
        jj_consume_token(30);
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case INTEGER_LITERAL:
                jj_consume_token(INTEGER_LITERAL);
                break;
            case FLOATING_POINT_LITERAL:
                jj_consume_token(FLOATING_POINT_LITERAL);
                break;
            default:
                jj_la1[4] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        jj_consume_token(31);
    }

    /**
     * Generated Token Manager.
     */
    public CParserTokenManager token_source;
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
    final private int[] jj_la1 = new int[5];
    static private int[] jj_la1_0;

    static {
        jj_la1_init_0();
    }

    private static void jj_la1_init_0() {
        jj_la1_0 = new int[]{0x40400000, 0x3c000000, 0x3c000000, 0x40400000, 0x44000,};
    }

    /**
     * Constructor with InputStream.
     */
    public CParser(java.io.InputStream stream, List<String> Names, List<String> Values) {
        this(stream, null);
        CParser.tableOfNames = Names;
        CParser.tableOfValues = Values;
    }

    /**
     * Constructor with InputStream and supplied encoding
     */
    public CParser(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source = new CParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 5; i++) {
            jj_la1[i] = -1;
        }
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
        for (int i = 0; i < 5; i++) {
            jj_la1[i] = -1;
        }
    }

    /**
     * Constructor.
     */
    public CParser(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new CParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 5; i++) {
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
        for (int i = 0; i < 5; i++) {
            jj_la1[i] = -1;
        }
    }

    /**
     * Constructor with generated Token Manager.
     */
    public CParser(CParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 5; i++) {
            jj_la1[i] = -1;
        }
    }

    /**
     * Reinitialise.
     */
    public void ReInit(CParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 5; i++) {
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

    private java.util.List<int[]> jj_expentries = new java.util.ArrayList<>();
    private int[] jj_expentry;
    private int jj_kind = -1;

    /**
     * Generate ParseException.
     */
    public ParseException generateParseException() {
        jj_expentries.clear();
        boolean[] la1tokens = new boolean[32];
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 5; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 32; i++) {
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
