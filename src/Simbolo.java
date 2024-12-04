import java.util.ArrayList;
import java.util.List;

public class Simbolo {

    private int numeroEntrada;
    private String codigoAtomo;
    private String lexema;
    private int tamanhoAntesTruncagem;
    private int tamanhoDepoisTruncagem;
    private String tipoSimbolo;
    private List<Integer> linhas;

    public Simbolo(int numeroEntrada, String codigoAtomo, String lexema,
                   int tamanhoAntesTruncagem, int tamanhoDepoisTruncagem,
                   String tipoSimbolo) {
        this.numeroEntrada = numeroEntrada;
        this.codigoAtomo = codigoAtomo;
        this.lexema = lexema;
        this.tamanhoAntesTruncagem = tamanhoAntesTruncagem;
        this.tamanhoDepoisTruncagem = tamanhoDepoisTruncagem;
        this.tipoSimbolo = tipoSimbolo;
        this.linhas = new ArrayList<>();
    }

    public void adicionarLinha(int linha) {
        this.linhas.add(linha);
    }

    public int getNumeroEntrada() {
        return numeroEntrada;
    }

    public String getCodigoAtomo() {
        return codigoAtomo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getTamanhoAntesTruncagem() {
        return tamanhoAntesTruncagem;
    }

    public int getTamanhoDepoisTruncagem() {
        return tamanhoDepoisTruncagem;
    }

    public String getTipoSimbolo() {
        return tipoSimbolo;
    }

    public List<Integer> getLinhas() {
        return linhas;
    }

    @Override
    public String toString() {
        return "Simbolo{numeroEntrada=" + numeroEntrada
                + ", codigoAtomo='" + codigoAtomo + "', lexema='" + lexema
                + "', tamanhoAntesTruncagem=" + tamanhoAntesTruncagem
                + ", tamanhoDepoisTruncagem=" + tamanhoDepoisTruncagem
                + ", tipoSimbolo='" + tipoSimbolo + "', linhas=" + linhas
                + '}';
    }
}
