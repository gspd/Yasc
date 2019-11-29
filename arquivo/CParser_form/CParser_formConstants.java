package yasc.arquivo.CParser_form;

public interface CParser_formConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int INTEGER_LITERAL = 14;
  /** RegularExpression Id. */
  int DECIMAL_LITERAL = 15;
  /** RegularExpression Id. */
  int HEX_LITERAL = 16;
  /** RegularExpression Id. */
  int OCTAL_LITERAL = 17;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 18;
  /** RegularExpression Id. */
  int EXPONENT = 19;
  /** RegularExpression Id. */
  int CHARACTER_LITERAL = 20;
  /** RegularExpression Id. */
  int STRING_LITERAL = 21;
  /** RegularExpression Id. */
  int IDENTIFIER = 22;
  /** RegularExpression Id. */
  int LETTER = 23;
  /** RegularExpression Id. */
  int DIGIT = 24;
  /** RegularExpression Id. */
  int IDSIGNAL = 25;
  /** RegularExpression Id. */
  int SIG = 26;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int PREPROCESSOR_OUTPUT = 1;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "<token of kind 5>",
    "<token of kind 6>",
    "\"#\"",
    "\"(\"",
    "\")\"",
    "\"\\n\"",
    "\"\\\\\\n\"",
    "\"\\\\\\r\\n\"",
    "<token of kind 13>",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<OCTAL_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<CHARACTER_LITERAL>",
    "<STRING_LITERAL>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
    "<IDSIGNAL>",
    "<SIG>",
    "\";\"",
    "\"[\"",
    "\"]\"",
  };
}
