package yasc.arquivo.CParser_form;

import java.io.PrintStream;

/**
 * Token Manager.
 */
public final class CParser_formTokenManager implements CParser_formConstants {

    /**
     * Debug output.
     */
    public java.io.PrintStream debugStream = System.out;

    /**
     * Set debug output.
     */
    public void setDebugStream(PrintStream ds) {
        debugStream = ds;
    }

    private int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch (pos) {
            default:
                return -1;
        }
    }

    private int jjStartNfa_0(int pos, long active0) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private int jjStopAtPos(int pos, int kind) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private int jjMoveStringLiteralDfa0_0() {
        switch (curChar) {
            case 35:
                return jjStopAtPos(0, 7);
            case 59:
                return jjStopAtPos(0, 27);
            case 91:
                return jjStopAtPos(0, 28);
            case 93:
                return jjStopAtPos(0, 29);
            default:
                return jjMoveNfa_0(0, 0);
        }
    }
    
    static final long[] jjbitVec0 = {
        0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
    };

    private int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        jjnewStateCnt = 63;
        int i = 1;
        jjstateSet[0] = startState;
        int kind = 0x7fffffff;
        for (;;) {
            if (++jjround == 0x7fffffff) {
                ReInitRounds();
            }
            if (curChar < 64) {
                long l = 1L << curChar;
                do {
                    switch (jjstateSet[--i]) {
                        case 0:
                            if ((0x3ff000000000000L & l) != 0L) {
                                jjCheckNAddStates(0, 6);
                            } else if ((0xac0000000000L & l) != 0L) {
                                if (kind > 25) {
                                    kind = 25;
                                }
                            } else if (curChar == 36) {
                                if (kind > 22) {
                                    kind = 22;
                                }
                                jjCheckNAdd(30);
                            } else if (curChar == 34) {
                                jjCheckNAddStates(7, 9);
                            } else if (curChar == 39) {
                                jjAddStates(10, 11);
                            } else if (curChar == 46) {
                                jjCheckNAdd(4);
                            }
                            if ((0x3fe000000000000L & l) != 0L) {
                                if (kind > 14) {
                                    kind = 14;
                                }
                                jjCheckNAddTwoStates(1, 2);
                            } else if (curChar == 47) {
                                jjAddStates(12, 13);
                            } else if (curChar == 48) {
                                if (kind > 14) {
                                    kind = 14;
                                }
                                jjCheckNAddStates(14, 16);
                            }
                            break;
                        case 1:
                            if ((0x3ff000000000000L & l) == 0L) {
                                break;
                            }
                            if (kind > 14) {
                                kind = 14;
                            }
                            jjCheckNAddTwoStates(1, 2);
                            break;
                        case 3:
                            if (curChar == 46) {
                                jjCheckNAdd(4);
                            }
                            break;
                        case 4:
                            if ((0x3ff000000000000L & l) == 0L) {
                                break;
                            }
                            if (kind > 18) {
                                kind = 18;
                            }
                            jjCheckNAddStates(17, 19);
                            break;
                        case 6:
                            if ((0x280000000000L & l) != 0L) {
                                jjCheckNAdd(7);
                            }
                            break;
                        case 7:
                            if ((0x3ff000000000000L & l) == 0L) {
                                break;
                            }
                            if (kind > 18) {
                                kind = 18;
                            }
                            jjCheckNAddTwoStates(7, 8);
                            break;
                        case 9:
                            if (curChar == 39) {
                                jjAddStates(10, 11);
                            }
                            break;
                        case 10:
                            if ((0xffffff7fffffdbffL & l) != 0L) {
                                jjCheckNAdd(11);
                            }
                            break;
                        case 11:
                            if (curChar == 39 && kind > 20) {
                                kind = 20;
                            }
                            break;
                        case 13:
                            if ((0x8400000000L & l) != 0L) {
                                jjCheckNAdd(11);
                            }
                            break;
                        case 14:
                            if ((0xff000000000000L & l) != 0L) {
                                jjCheckNAddTwoStates(15, 11);
                            }
                            break;
                        case 15:
                            if ((0xff000000000000L & l) != 0L) {
                                jjCheckNAdd(11);
                            }
                            break;
                        case 16:
                            if ((0xf000000000000L & l) != 0L) {
                                jjstateSet[jjnewStateCnt++] = 17;
                            }
                            break;
                        case 17:
                            if ((0xff000000000000L & l) != 0L) {
                                jjCheckNAdd(15);
                            }
                            break;
                        case 18:
                            if (curChar == 34) {
                                jjCheckNAddStates(7, 9);
                            }
                            break;
                        case 19:
                            if ((0xfffffffbffffdbffL & l) != 0L) {
                                jjCheckNAddStates(7, 9);
                            }
                            break;
                        case 21:
                            if ((0x8400002400L & l) != 0L) {
                                jjCheckNAddStates(7, 9);
                            }
                            break;
                        case 22:
                            if (curChar == 34 && kind > 21) {
                                kind = 21;
                            }
                            break;
                        case 23:
                            if ((0xff000000000000L & l) != 0L) {
                                jjCheckNAddStates(20, 23);
                            }
                            break;
                        case 24:
                            if ((0xff000000000000L & l) != 0L) {
                                jjCheckNAddStates(7, 9);
                            }
                            break;
                        case 25:
                            if ((0xf000000000000L & l) != 0L) {
                                jjstateSet[jjnewStateCnt++] = 26;
                            }
                            break;
                        case 26:
                            if ((0xff000000000000L & l) != 0L) {
                                jjCheckNAdd(24);
                            }
                            break;
                        case 27:
                            if (curChar == 10) {
                                jjCheckNAddStates(7, 9);
                            }
                            break;
                        case 28:
                            if (curChar == 13) {
                                jjstateSet[jjnewStateCnt++] = 27;
                            }
                            break;
                        case 29:
                            if (curChar != 36) {
                                break;
                            }
                            if (kind > 22) {
                                kind = 22;
                            }
                            jjCheckNAdd(30);
                            break;
                        case 30:
                            if ((0x3ff001000000000L & l) == 0L) {
                                break;
                            }
                            if (kind > 22) {
                                kind = 22;
                            }
                            jjCheckNAdd(30);
                            break;
                        case 31:
                            if ((0xac0000000000L & l) != 0L && kind > 25) {
                                kind = 25;
                            }
                            break;
                        case 32:
                            if ((0x3ff000000000000L & l) != 0L) {
                                jjCheckNAddStates(0, 6);
                            }
                            break;
                        case 33:
                            if ((0x3ff000000000000L & l) != 0L) {
                                jjCheckNAddTwoStates(33, 34);
                            }
                            break;
                        case 34:
                            if (curChar != 46) {
                                break;
                            }
                            if (kind > 18) {
                                kind = 18;
                            }
                            jjCheckNAddStates(24, 26);
                            break;
                        case 35:
                            if ((0x3ff000000000000L & l) == 0L) {
                                break;
                            }
                            if (kind > 18) {
                                kind = 18;
                            }
                            jjCheckNAddStates(24, 26);
                            break;
                        case 37:
                            if ((0x280000000000L & l) != 0L) {
                                jjCheckNAdd(38);
                            }
                            break;
                        case 38:
                            if ((0x3ff000000000000L & l) == 0L) {
                                break;
                            }
                            if (kind > 18) {
                                kind = 18;
                            }
                            jjCheckNAddTwoStates(38, 8);
                            break;
                        case 39:
                            if ((0x3ff000000000000L & l) != 0L) {
                                jjCheckNAddTwoStates(39, 40);
                            }
                            break;
                        case 41:
                            if ((0x280000000000L & l) != 0L) {
                                jjCheckNAdd(42);
                            }
                            break;
                        case 42:
                            if ((0x3ff000000000000L & l) == 0L) {
                                break;
                            }
                            if (kind > 18) {
                                kind = 18;
                            }
                            jjCheckNAddTwoStates(42, 8);
                            break;
                        case 43:
                            if ((0x3ff000000000000L & l) != 0L) {
                                jjCheckNAddStates(27, 29);
                            }
                            break;
                        case 45:
                            if ((0x280000000000L & l) != 0L) {
                                jjCheckNAdd(46);
                            }
                            break;
                        case 46:
                            if ((0x3ff000000000000L & l) != 0L) {
                                jjCheckNAddTwoStates(46, 8);
                            }
                            break;
                        case 47:
                            if (curChar != 48) {
                                break;
                            }
                            if (kind > 14) {
                                kind = 14;
                            }
                            jjCheckNAddStates(14, 16);
                            break;
                        case 49:
                            if ((0x3ff000000000000L & l) == 0L) {
                                break;
                            }
                            if (kind > 14) {
                                kind = 14;
                            }
                            jjCheckNAddTwoStates(49, 2);
                            break;
                        case 50:
                            if ((0xff000000000000L & l) == 0L) {
                                break;
                            }
                            if (kind > 14) {
                                kind = 14;
                            }
                            jjCheckNAddTwoStates(50, 2);
                            break;
                        case 51:
                            if (curChar == 47) {
                                jjAddStates(12, 13);
                            }
                            break;
                        case 52:
                            if (curChar == 47) {
                                jjCheckNAddStates(30, 32);
                            }
                            break;
                        case 53:
                            if ((0xffffffffffffdbffL & l) != 0L) {
                                jjCheckNAddStates(30, 32);
                            }
                            break;
                        case 54:
                            if ((0x2400L & l) != 0L && kind > 5) {
                                kind = 5;
                            }
                            break;
                        case 55:
                            if (curChar == 10 && kind > 5) {
                                kind = 5;
                            }
                            break;
                        case 56:
                            if (curChar == 13) {
                                jjstateSet[jjnewStateCnt++] = 55;
                            }
                            break;
                        case 57:
                            if (curChar == 42) {
                                jjCheckNAddTwoStates(58, 59);
                            }
                            break;
                        case 58:
                            if ((0xfffffbffffffffffL & l) != 0L) {
                                jjCheckNAddTwoStates(58, 59);
                            }
                            break;
                        case 59:
                            if (curChar == 42) {
                                jjCheckNAddStates(33, 35);
                            }
                            break;
                        case 60:
                            if ((0xffff7bffffffffffL & l) != 0L) {
                                jjCheckNAddTwoStates(61, 59);
                            }
                            break;
                        case 61:
                            if ((0xfffffbffffffffffL & l) != 0L) {
                                jjCheckNAddTwoStates(61, 59);
                            }
                            break;
                        case 62:
                            if (curChar == 47 && kind > 6) {
                                kind = 6;
                            }
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                do {
                    switch (jjstateSet[--i]) {
                        case 0:
                        case 30:
                            if ((0x7fffffe87fffffeL & l) == 0L) {
                                break;
                            }
                            if (kind > 22) {
                                kind = 22;
                            }
                            jjCheckNAdd(30);
                            break;
                        case 2:
                            if ((0x100000001000L & l) != 0L && kind > 14) {
                                kind = 14;
                            }
                            break;
                        case 5:
                            if ((0x2000000020L & l) != 0L) {
                                jjAddStates(36, 37);
                            }
                            break;
                        case 8:
                            if ((0x5000000050L & l) != 0L && kind > 18) {
                                kind = 18;
                            }
                            break;
                        case 10:
                            if ((0xffffffffefffffffL & l) != 0L) {
                                jjCheckNAdd(11);
                            }
                            break;
                        case 12:
                            if (curChar == 92) {
                                jjAddStates(38, 40);
                            }
                            break;
                        case 13:
                            if ((0x14404410000000L & l) != 0L) {
                                jjCheckNAdd(11);
                            }
                            break;
                        case 19:
                            if ((0xffffffffefffffffL & l) != 0L) {
                                jjCheckNAddStates(7, 9);
                            }
                            break;
                        case 20:
                            if (curChar == 92) {
                                jjAddStates(41, 44);
                            }
                            break;
                        case 21:
                            if ((0x14404410000000L & l) != 0L) {
                                jjCheckNAddStates(7, 9);
                            }
                            break;
                        case 36:
                            if ((0x2000000020L & l) != 0L) {
                                jjAddStates(45, 46);
                            }
                            break;
                        case 40:
                            if ((0x2000000020L & l) != 0L) {
                                jjAddStates(47, 48);
                            }
                            break;
                        case 44:
                            if ((0x2000000020L & l) != 0L) {
                                jjAddStates(49, 50);
                            }
                            break;
                        case 48:
                            if ((0x100000001000000L & l) != 0L) {
                                jjCheckNAdd(49);
                            }
                            break;
                        case 49:
                            if ((0x7e0000007eL & l) == 0L) {
                                break;
                            }
                            if (kind > 14) {
                                kind = 14;
                            }
                            jjCheckNAddTwoStates(49, 2);
                            break;
                        case 53:
                            jjAddStates(30, 32);
                            break;
                        case 58:
                            jjCheckNAddTwoStates(58, 59);
                            break;
                        case 60:
                        case 61:
                            jjCheckNAddTwoStates(61, 59);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                do {
                    switch (jjstateSet[--i]) {
                        case 10:
                            if ((jjbitVec0[i2] & l2) != 0L) {
                                jjstateSet[jjnewStateCnt++] = 11;
                            }
                            break;
                        case 19:
                            if ((jjbitVec0[i2] & l2) != 0L) {
                                jjAddStates(7, 9);
                            }
                            break;
                        case 53:
                            if ((jjbitVec0[i2] & l2) != 0L) {
                                jjAddStates(30, 32);
                            }
                            break;
                        case 58:
                            if ((jjbitVec0[i2] & l2) != 0L) {
                                jjCheckNAddTwoStates(58, 59);
                            }
                            break;
                        case 60:
                        case 61:
                            if ((jjbitVec0[i2] & l2) != 0L) {
                                jjCheckNAddTwoStates(61, 59);
                            }
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != 0x7fffffff) {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 63 - (jjnewStateCnt = startsAt))) {
                return curPos;
            }
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private int jjMoveStringLiteralDfa0_1() {
        switch (curChar) {
            case 10:
                return jjStopAtPos(0, 10);
            case 92:
                return jjMoveStringLiteralDfa1_1(0x1800L);
            default:
                return 1;
        }
    }

    private int jjMoveStringLiteralDfa1_1(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return 1;
        }
        switch (curChar) {
            case 10:
                if ((active0 & 0x800L) != 0L) {
                    return jjStopAtPos(1, 11);
                }
                break;
            case 13:
                return jjMoveStringLiteralDfa2_1(active0, 0x1000L);
            default:
                return 2;
        }
        return 2;
    }

    private int jjMoveStringLiteralDfa2_1(long old0, long active0) {
        if (((active0 &= old0)) == 0L) {
            return 2;
        }
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return 2;
        }
        switch (curChar) {
            case 10:
                if ((active0 & 0x1000L) != 0L) {
                    return jjStopAtPos(2, 12);
                }
                break;
            default:
                return 3;
        }
        return 3;
    }
    static final int[] jjnextStates = {
        33, 34, 39, 40, 43, 44, 8, 19, 20, 22, 10, 12, 52, 57, 48, 50,
        2, 4, 5, 8, 19, 20, 24, 22, 35, 36, 8, 43, 44, 8, 53, 54,
        56, 59, 60, 62, 6, 7, 13, 14, 16, 21, 23, 25, 28, 37, 38, 41,
        42, 45, 46,};

    /**
     * Token literal values.
     */
    public static final String[] jjstrLiteralImages = {
        "", null, null, null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        "\73", "\133", "\135",};

    /**
     * Lexer state names.
     */
    public static final String[] lexStateNames = {
        "DEFAULT",
        "PREPROCESSOR_OUTPUT",};

    /**
     * Lex State array.
     */
    public static final int[] jjnewLexState = {
        -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1,};
    static final long[] jjtoToken = {
        0x3a744001L,};
    static final long[] jjtoSkip = {
        0x7feL,};
    static final long[] jjtoMore = {
        0x3800L,};
    protected SimpleCharStream input_stream;
    private final int[] jjrounds = new int[63];
    private final int[] jjstateSet = new int[126];
    protected char curChar;

    /**
     * Constructor.
     * @param Stream of characters
     */
    public CParser_formTokenManager(SimpleCharStream stream) {
        if (SimpleCharStream.staticFlag) {
            throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        }
        input_stream = stream;
    }

    /**
     * Constructor.
     */
    public CParser_formTokenManager(SimpleCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    /**
     * Reinitialise parser.
     */
    public void ReInit(SimpleCharStream stream) {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 63; i-- > 0;) {
            jjrounds[i] = 0x80000000;
        }
    }

    /**
     * Reinitialise parser.
     */
    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    /**
     * Switch to specified lex state.
     */
    public void SwitchTo(int lexState) {
        if (lexState >= 2 || lexState < 0) {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
        } else {
            curLexState = lexState;
        }
    }

    protected Token jjFillToken() {
        final Token t;
        final String curTokenImage;
        final int beginLine;
        final int endLine;
        final int beginColumn;
        final int endColumn;
        String im = jjstrLiteralImages[jjmatchedKind];
        curTokenImage = (im == null) ? input_stream.GetImage() : im;
        beginLine = input_stream.getBeginLine();
        beginColumn = input_stream.getBeginColumn();
        endLine = input_stream.getEndLine();
        endColumn = input_stream.getEndColumn();
        t = Token.newToken(jjmatchedKind, curTokenImage);

        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;

        return t;
    }

    int curLexState = 0;
    int defaultLexState = 0;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;

    /**
     * @return the next Token.
     */
    public Token getNextToken() {
        Token matchedToken;
        int curPos = 0;

        EOFLoop:
        for (;;) {
            try {
                curChar = input_stream.BeginToken();
            } catch (java.io.IOException e) {
                jjmatchedKind = 0;
                matchedToken = jjFillToken();
                return matchedToken;
            }

            for (;;) {
                switch (curLexState) {
                    case 0:
                        try {
                            input_stream.backup(0);
                            while (curChar <= 41 && (0x30100002600L & (1L << curChar)) != 0L) {
                                curChar = input_stream.BeginToken();
                            }
                        } catch (java.io.IOException e1) {
                            continue EOFLoop;
                        }
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_0();
                        break;
                    case 1:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_1();
                        if (jjmatchedPos == 0 && jjmatchedKind > 13) {
                            jjmatchedKind = 13;
                        }
                        break;
                }
                if (jjmatchedKind != 0x7fffffff) {
                    if (jjmatchedPos + 1 < curPos) {
                        input_stream.backup(curPos - jjmatchedPos - 1);
                    }
                    if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        matchedToken = jjFillToken();
                        if (jjnewLexState[jjmatchedKind] != -1) {
                            curLexState = jjnewLexState[jjmatchedKind];
                        }
                        return matchedToken;
                    } else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        if (jjnewLexState[jjmatchedKind] != -1) {
                            curLexState = jjnewLexState[jjmatchedKind];
                        }
                        continue EOFLoop;
                    }
                    if (jjnewLexState[jjmatchedKind] != -1) {
                        curLexState = jjnewLexState[jjmatchedKind];
                    }
                    curPos = 0;
                    jjmatchedKind = 0x7fffffff;
                    try {
                        curChar = input_stream.readChar();
                        continue;
                    } catch (java.io.IOException e1) {
                    }
                }
                int error_line = input_stream.getEndLine();
                int error_column = input_stream.getEndColumn();
                String error_after = null;
                boolean EOFSeen = false;
                try {
                    input_stream.readChar();
                    input_stream.backup(1);
                } catch (java.io.IOException e1) {
                    EOFSeen = true;
                    error_after = curPos <= 1 ? "" : input_stream.GetImage();
                    if (curChar == '\n' || curChar == '\r') {
                        error_line++;
                        error_column = 0;
                    } else {
                        error_column++;
                    }
                }
                if (!EOFSeen) {
                    input_stream.backup(1);
                    error_after = curPos <= 1 ? "" : input_stream.GetImage();
                }
                throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
            }
        }
    }

    private void jjCheckNAdd(int state) {
        if (jjrounds[state] != jjround) {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private void jjAddStates(int start, int end) {
        do {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        } while (start++ != end);
    }

    private void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private void jjCheckNAddStates(int start, int end) {
        do {
            jjCheckNAdd(jjnextStates[start]);
        } while (start++ != end);
    }
}
