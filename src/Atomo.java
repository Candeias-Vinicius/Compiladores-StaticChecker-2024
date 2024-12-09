public class Atomo {
    private String codigo;
    private String lexema;
    private int linha;
    private int posicao;

    public Atomo(String codigo, String lexema, int linha) {
        this.codigo = codigo;
        this.lexema = lexema;
        this.linha = linha;
    }
    public Atomo(String codigo, String lexema, int linha, int posicao) {
        this.codigo = codigo;
        this.lexema = lexema;
        this.linha = linha;
        this.posicao = posicao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinha() {
        return linha;
    }

    //Esta nao é a posicao do atomo, é a ultima atualização da posicao do analisador lexico
    public int getPosicao() {
        return posicao;
    }

    @Override
    public String toString() {
        return "Atomo{codigo='" + codigo + "', lexema='" + lexema
                + "', linha=" + linha + '}';
    }
}
