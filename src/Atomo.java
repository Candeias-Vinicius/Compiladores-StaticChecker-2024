public class Atomo {
    private String codigo;
    private String lexema;
    private int linha;

    public Atomo(String codigo, String lexema, int linha) {
        this.codigo = codigo;
        this.lexema = lexema;
        this.linha = linha;
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

    @Override
    public String toString() {
        return "Atomo{codigo='" + codigo + "', lexema='" + lexema
                + "', linha=" + linha + '}';
    }
}
