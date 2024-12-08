import java.util.HashMap;
import java.util.Map;

public class TabelaReservadas {

    private static Map<String, String> tabelaReservadas;

    static {
        tabelaReservadas = new HashMap<>();
        inicializarTabelaReservadas();
    }

    private static void inicializarTabelaReservadas() {

        tabelaReservadas.put("CADEIA", "A01");
        tabelaReservadas.put("CARACTER", "A02");
        tabelaReservadas.put("DECLARACOES", "A03");
        tabelaReservadas.put("ENQUANTO", "A04");
        tabelaReservadas.put("FALSE", "A05");
        tabelaReservadas.put("FIMDECLARACOES", "A06");
        tabelaReservadas.put("FIMENQUANTO", "A07");
        tabelaReservadas.put("FIMFUNCAO", "A08");
        tabelaReservadas.put("FIMFUNCOES", "A09");
        tabelaReservadas.put("FIMPROGRAMA", "A10");
        tabelaReservadas.put("FIMSE", "A11");
        tabelaReservadas.put("FUNCOES", "A12");
        tabelaReservadas.put("IMPRIME", "A13");
        tabelaReservadas.put("INTEIRO", "A14");
        tabelaReservadas.put("LOGICO", "A15");
        tabelaReservadas.put("PAUSA", "A16");
        tabelaReservadas.put("PROGRAMA", "A17");
        tabelaReservadas.put("REAL", "A18");
        tabelaReservadas.put("RETORNA", "A19");
        tabelaReservadas.put("SE", "A20");
        tabelaReservadas.put("SENAO", "A21");
        tabelaReservadas.put("TIPOFUNCAO", "A22");
        tabelaReservadas.put("TIPOPARAM", "A23");
        tabelaReservadas.put("TIPOVAR", "A24");
        tabelaReservadas.put("TRUE", "A25");
        tabelaReservadas.put("VAZIO", "A26");

        // Adicionando os símbolos
        tabelaReservadas.put("%", "B01");
        tabelaReservadas.put("&", "B02");
        tabelaReservadas.put("/", "B03");
        tabelaReservadas.put("(", "B04");
        tabelaReservadas.put(")", "B05");
        tabelaReservadas.put("*", "B06");
        tabelaReservadas.put("+", "B07");
        tabelaReservadas.put("-", "B08");
        tabelaReservadas.put(";", "B09");
        tabelaReservadas.put("<", "B10");
        tabelaReservadas.put("=", "B11");
        tabelaReservadas.put(">", "B12");
        tabelaReservadas.put("{", "B13");
        tabelaReservadas.put("}", "B14");
        tabelaReservadas.put("!=", "B15");
        tabelaReservadas.put("<=", "B16");
        tabelaReservadas.put(">=", "B17");

    }

    public static String getCodigoAtomo(String lexema) {
        return tabelaReservadas.get(lexema);
    }

    public static boolean isReservada(String lexema) {
        return tabelaReservadas.containsKey(lexema);
    }
}
