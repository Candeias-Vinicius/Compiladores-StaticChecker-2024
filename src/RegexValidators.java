import java.util.regex.Pattern;

public class RegexValidators {

    private static final Pattern NOM_PROGRAMA_PATTERN = Pattern.compile("^[A-Z][A-Z0-9_]{0,29}$");
    private static final Pattern VARIAVEL_PATTERN = Pattern.compile("^([A-Z]|_)[A-Z0-9_]{0,29}$");
    private static final Pattern NOM_FUNCAO_PATTERN = Pattern.compile("^[A-Z][A-Z0-9_]{0,29}$");
    private static final Pattern CONS_INTEIRO_PATTERN = Pattern.compile("^[0-9]{1,30}$");
    private static final Pattern CONS_REAL_PATTERN = Pattern.compile("^[0-9]+\\.[0-9]+(?:[eE][+-]?[0-9]+)?$");
    private static final Pattern CONS_CADEIA_PATTERN = Pattern.compile("^\".{0,28}\"$");
    private static final Pattern CONS_CARACTER_PATTERN = Pattern.compile("^'.{1}'$");

    public static boolean isNomPrograma(String s) {
        return NOM_PROGRAMA_PATTERN.matcher(s).matches();
    }

    public static boolean isVariavel(String s) {
        return VARIAVEL_PATTERN.matcher(s).matches();
    }

    public static boolean isNomFuncao(String s) {
        return NOM_FUNCAO_PATTERN.matcher(s).matches();
    }

    public static boolean isConsInteiro(String s) {
        return CONS_INTEIRO_PATTERN.matcher(s).matches();
    }

    public static boolean isConsReal(String s) {
        return CONS_REAL_PATTERN.matcher(s).matches();
    }

    public static boolean isConsCadeia(String s) {
        return CONS_CADEIA_PATTERN.matcher(s).matches();
    }

    public static boolean isConsCaracter(String s) {
        return CONS_CARACTER_PATTERN.matcher(s).matches();
    }

}
