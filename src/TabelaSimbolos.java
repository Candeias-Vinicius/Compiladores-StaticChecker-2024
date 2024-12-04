import java.util.*;

public class TabelaSimbolos {

    private Map<String, Simbolo> simbolos;
    private int contadorEntrada;

    public TabelaSimbolos() {
        this.simbolos = new LinkedHashMap<>();
        this.contadorEntrada = 1;
    }

    public void adicionarOuAtualizarSimbolo(String codigoAtomo, String lexema,
                                            int tamanhoAntesTruncagem, int tamanhoDepoisTruncagem,
                                            String tipoSimbolo, int linha) {

        Simbolo simbolo = simbolos.get(lexema);
        if (simbolo == null) {
            simbolo = new Simbolo(contadorEntrada++, codigoAtomo, lexema,
                    tamanhoAntesTruncagem, tamanhoDepoisTruncagem,
                    tipoSimbolo);
            simbolos.put(lexema, simbolo);
        }
        simbolo.adicionarLinha(linha);
    }

    public Simbolo getSimbolo(String lexema) {
        return simbolos.get(lexema);
    }

    public Collection<Simbolo> getSimbolos() {
        return simbolos.values();
    }

    public void imprimirTabela() {
        for (Simbolo simbolo : simbolos.values()) {
            System.out.println(simbolo);
        }
    }
}
