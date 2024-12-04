import java.util.HashMap;
import java.util.Map;

public class TabelaReservadas {

    private static Map<String, String> tabelaReservadas;

    static {
        tabelaReservadas = new HashMap<>();
        inicializarTabelaReservadas();
    }

    private static void inicializarTabelaReservadas() {

        tabelaReservadas.put("BOOLEANO", "A01");
        tabelaReservadas.put("CARACTER", "A02");
        tabelaReservadas.put("CASO", "A03");
        tabelaReservadas.put("COMANDOENTRADA", "A04");
        tabelaReservadas.put("COMANDOSAIDA", "A05");
        tabelaReservadas.put("CONSTANTES", "A06");
        tabelaReservadas.put("DEF", "A07");
        tabelaReservadas.put("DO", "A08");
        tabelaReservadas.put("E", "A09");
        tabelaReservadas.put("ENQUANTO", "A10");
        tabelaReservadas.put("ENTAO", "A11");
        tabelaReservadas.put("ESCOLHA", "A12");
        tabelaReservadas.put("FALSO", "A13");
        tabelaReservadas.put("FIM", "A14");
        tabelaReservadas.put("FIMDEF", "A15");
        tabelaReservadas.put("FIMPROGRAMA", "A16");
        tabelaReservadas.put("IMPRIME", "A17");
        tabelaReservadas.put("INTEIRO", "A18");
        tabelaReservadas.put("LEIA", "A19");
        tabelaReservadas.put("PAUSA", "A20");
        tabelaReservadas.put("PROGRAMA", "A21");
        tabelaReservadas.put("REAL", "A22");
        tabelaReservadas.put("RETORNA", "A23");
        tabelaReservadas.put("SE", "A24");
        tabelaReservadas.put("SENAO", "A25");
        tabelaReservadas.put("TIPOFUNC", "A26");
        tabelaReservadas.put("TIPOPARAM", "A27");
        tabelaReservadas.put("TIPOVAR", "A28");
        tabelaReservadas.put("VERDADEIRO", "A29");
        tabelaReservadas.put("VAZIO", "A30");

        tabelaReservadas.put("%", "B01");
        tabelaReservadas.put("(", "B02");
        tabelaReservadas.put(")", "B03");
        tabelaReservadas.put("=", "B04");
        tabelaReservadas.put("!=", "B05");
        tabelaReservadas.put("#", "B06");
        tabelaReservadas.put("<", "B07");
        tabelaReservadas.put("<=", "B08");
        tabelaReservadas.put(">", "B09");
        tabelaReservadas.put("}", "B10");
        tabelaReservadas.put("]", "B11");
        tabelaReservadas.put("{", "B12");
        tabelaReservadas.put("*", "B13");
        tabelaReservadas.put("/", "B14");
        tabelaReservadas.put("+", "B16");
        tabelaReservadas.put(";", "B17");
        tabelaReservadas.put(",", "B18");
        tabelaReservadas.put(":", "B19");
        tabelaReservadas.put("[", "B20");
        tabelaReservadas.put("==", "B21");
        tabelaReservadas.put(">=", "B22");
        tabelaReservadas.put("-", "B23");
        tabelaReservadas.put("&&", "B24");
        tabelaReservadas.put("||", "B25");

    }

    public static String getCodigoAtomo(String lexema) {
        return tabelaReservadas.get(lexema);
    }

    public static boolean isReservada(String lexema) {
        return tabelaReservadas.containsKey(lexema);
    }
}
