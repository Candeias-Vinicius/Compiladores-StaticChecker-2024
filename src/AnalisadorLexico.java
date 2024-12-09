import java.util.*;

public class AnalisadorLexico {

    private String conteudo;
    private int posicao;
    private int linhaAtual;

    private TabelaSimbolos tabelaSimbolos;

    private TabelaReservadas tabelaReservadas;

    public static final int LIMITE_ATOMO = 30;

    public AnalisadorLexico(String conteudo, TabelaSimbolos tabelaSimbolos,TabelaReservadas tabelaReservadas) {
        this.conteudo = conteudo.toUpperCase();
        this.linhaAtual = 1;
        this.tabelaSimbolos = tabelaSimbolos;
        this.tabelaReservadas = tabelaReservadas;
    }

    public Atomo getAtomo(int posicaoAtual) {
        posicao = posicaoAtual;
        while (posicao < conteudo.length()) {
            char atual = conteudo.charAt(posicao);

            if (processarNovaLinha(atual)) continue;
            filtroPrimeiroNivel(atual);
            atual = conteudo.charAt(posicao);
            if (filtroSegundoNivel(atual)) continue;

            if (Character.isLetter(atual) || atual == '_' ) {
                return processarIdentificadorOuPalavraReservada();
            } else if (Character.isDigit(atual)) {
                return processarNumero();
            } else if (atual == '"') {
                return processarCadeiaCaracteres();
            } else if (atual == '\'') {
                return processarCaracter();
            } else {
                Atomo atomo = processarSimbolo(atual);
                if (atomo != null) return atomo;
            }

            posicao++;
        }

        return null;
    }

    private boolean processarNovaLinha(char atual) {
        if (atual == '\n') {
            linhaAtual++;
            posicao++;
            return true;
        }
        return false;
    }

    private Atomo processarIdentificadorOuPalavraReservada() {
        StringBuilder lexemaBuilder = new StringBuilder();
        int contadorCaracteres = 0;
        int tamanhoAntesTruncagem = 0;

        while (posicao < conteudo.length() && (Character.isLetterOrDigit(conteudo.charAt(posicao)) || conteudo.charAt(posicao) == '_' || ehCaractereInvalido(conteudo.charAt(posicao)))) {
            if(posicao != 0){
                if(!lexemaValido(lexemaBuilder.toString()+conteudo.charAt(posicao))){
                    break;
                }
            }
            filtroPrimeiroNivel(conteudo.charAt(posicao));
            if(Character.isWhitespace(conteudo.charAt(posicao))){
                break;
            }
            if (posicao >= conteudo.length()) {
                break;
            }

            tamanhoAntesTruncagem++;
            if (contadorCaracteres < LIMITE_ATOMO) {
                lexemaBuilder.append(conteudo.charAt(posicao));
                contadorCaracteres++;
            }

            posicao++;
        }

        String lexema = lexemaBuilder.toString();
        int tamanhoDepoisTruncagem = lexema.length();

        if (tabelaReservadas.isReservada(lexema)) {
            return new Atomo(tabelaReservadas.getCodigoAtomo(lexema), lexema, linhaAtual, posicao);
        }


        return processarIdentificador(lexema, tamanhoAntesTruncagem, tamanhoDepoisTruncagem);
    }

    private boolean lexemaValido(String lexema) {
        return RegexValidators.isNomPrograma(lexema) || RegexValidators.isNomFuncao(lexema) || RegexValidators.isVariavel(lexema) || tabelaReservadas.isReservada(lexema) || RegexValidators.isConsInteiro(lexema) || RegexValidators.isConsReal(lexema) || RegexValidators.isConsCadeia(lexema) || RegexValidators.isConsCaracter(lexema);
    }

    private Atomo processarIdentificador(String lexema, int tamanhoAntesTruncagem, int tamanhoDepoisTruncagem) {
        String tipoSimbolo;
        String codigoIdentificador;

        if (RegexValidators.isNomPrograma(lexema)) {
            tipoSimbolo = "nomePrograma";
            codigoIdentificador = "C06";
        } else if (RegexValidators.isNomFuncao(lexema)) {
            tipoSimbolo = "nomeFuncao";
            codigoIdentificador = "C05";
        } else if (RegexValidators.isVariavel(lexema)) {
            tipoSimbolo = "variavel";
            codigoIdentificador = "C07";
        } else {
            return null;
        }

        tabelaSimbolos.adicionarOuAtualizarSimbolo(codigoIdentificador, lexema, tamanhoAntesTruncagem, tamanhoDepoisTruncagem, tipoSimbolo, linhaAtual);
        return new Atomo(codigoIdentificador, lexema, linhaAtual, posicao);
    }

    private Atomo processarNumero() {
        StringBuilder lexemaBuilder = new StringBuilder();
        int contadorCaracteres = 0;
        int tamanhoAntesTruncagem = 0;
        boolean isReal = false;

        while (posicao < conteudo.length() && (Character.isDigit(conteudo.charAt(posicao)) || ehCaractereInvalido(conteudo.charAt(posicao)))) {
            filtroPrimeiroNivel(conteudo.charAt(posicao));
            if(posicao != 0){
                if(!lexemaValido(lexemaBuilder.toString()+conteudo.charAt(posicao))){
                    break;
                }
            }
            if(Character.isWhitespace(conteudo.charAt(posicao))){
                break;
            }
            if (posicao >= conteudo.length()) {
                break;
            }
            tamanhoAntesTruncagem++;
            if (contadorCaracteres < LIMITE_ATOMO) {
                lexemaBuilder.append(conteudo.charAt(posicao));
                contadorCaracteres++;
            }
            posicao++;
        }

        if (posicao < conteudo.length() && conteudo.charAt(posicao) == '.') {
            isReal = processarParteDecimal(lexemaBuilder, contadorCaracteres, tamanhoAntesTruncagem);
        }

        String lexemaStr = lexemaBuilder.toString();
        int tamanhoDepoisTruncagem = lexemaStr.length();

        if (isReal) {
            return processarNumeroReal(lexemaStr, tamanhoAntesTruncagem, tamanhoDepoisTruncagem);
        } else {
            return processarNumeroInteiro(lexemaStr, tamanhoAntesTruncagem, tamanhoDepoisTruncagem);
        }
    }

    private boolean processarParteDecimal(StringBuilder lexemaBuilder, int contadorCaracteres, int tamanhoAntesTruncagem) {
        if (contadorCaracteres < LIMITE_ATOMO) {
            lexemaBuilder.append('.');
            contadorCaracteres++;
            posicao++;
            return true;
        } else {
            return false;
        }
    }

    private Atomo processarNumeroReal(String lexemaStr, int tamanhoAntesTruncagem, int tamanhoDepoisTruncagem) {
        if (RegexValidators.isConsReal(lexemaStr)) {
            tabelaSimbolos.adicionarOuAtualizarSimbolo("C04", lexemaStr, tamanhoAntesTruncagem, tamanhoDepoisTruncagem, "constanteReal", linhaAtual);
            return new Atomo("C04", lexemaStr, linhaAtual, posicao);
        } else {
            return null;
        }
    }

    private Atomo processarNumeroInteiro(String lexemaStr, int tamanhoAntesTruncagem, int tamanhoDepoisTruncagem) {
        if (RegexValidators.isConsInteiro(lexemaStr)) {
            tabelaSimbolos.adicionarOuAtualizarSimbolo("C03", lexemaStr, tamanhoAntesTruncagem, tamanhoDepoisTruncagem, "constanteInteira", linhaAtual);
            return new Atomo("C03", lexemaStr, linhaAtual, posicao);
        } else {
            return null;
        }
    }

    private Atomo processarCadeiaCaracteres() {
        StringBuilder lexemaBuilder = new StringBuilder();
        lexemaBuilder.append('"');
        posicao++;

        int contadorCaracteres = 1;
        boolean terminou = false;
        int tamanhoAntesTruncagem = 1;

        while (posicao < conteudo.length()) {
            char c = conteudo.charAt(posicao);
            if (c == '"') {
                tamanhoAntesTruncagem++;
                if (contadorCaracteres < LIMITE_ATOMO) {
                    lexemaBuilder.append('"');
                    contadorCaracteres++;
                }
                posicao++;
                terminou = true;
                break;
            } else {
                tamanhoAntesTruncagem++;
                if (contadorCaracteres < LIMITE_ATOMO - 1) {
                    lexemaBuilder.append(c);
                    contadorCaracteres++;
                    posicao++;
                } else {
                    if (lexemaBuilder.charAt(lexemaBuilder.length() - 1) != '"') {
                        lexemaBuilder.append('"');
                        contadorCaracteres++;
                        while (posicao < conteudo.length() && conteudo.charAt(posicao) != '"') {
                            posicao++;
                        }
                        posicao++;
                    }
                    terminou = true;
                    break;
                }
            }
        }

        if (!terminou && contadorCaracteres >= LIMITE_ATOMO) {
            if (lexemaBuilder.charAt(lexemaBuilder.length() - 1) != '"') {
                lexemaBuilder.append('"');
                contadorCaracteres++;
            }
            terminou = true;
        }

        while (!terminou && posicao < conteudo.length()) {
            char c = conteudo.charAt(posicao);
            if (c == '\n') {
                linhaAtual++;
            }
            posicao++;
            if (c == '"') {
                terminou = true;
                break;
            }
        }

        String lexemaStr = lexemaBuilder.toString();
        int tamanhoDepoisTruncagem = lexemaStr.length();

        if (RegexValidators.isConsCadeia(lexemaStr)) {
            tabelaSimbolos.adicionarOuAtualizarSimbolo("C01", lexemaStr, tamanhoAntesTruncagem, tamanhoDepoisTruncagem, "constanteCadeia", linhaAtual);
            return new Atomo("C01", lexemaStr, linhaAtual, posicao);
        } else {
            return null;
        }
    }

    private Atomo processarCaracter() {
        StringBuilder lexemaBuilder = new StringBuilder();
        lexemaBuilder.append('\'');
        posicao++;

        int tamanhoAntesTruncagem = 1;

        if (posicao < conteudo.length()) {
            char c = conteudo.charAt(posicao);
            tamanhoAntesTruncagem++;
            lexemaBuilder.append(c);
            posicao++;

            if (posicao < conteudo.length() && conteudo.charAt(posicao) == '\'') {
                tamanhoAntesTruncagem++;
                lexemaBuilder.append('\'');
                posicao++;
            } else {
                lexemaBuilder.append('\'');
                while (posicao < conteudo.length()) {
                    posicao++;
                }
            }

            String lexemaStr = lexemaBuilder.toString();
            int tamanhoDepoisTruncagem = lexemaStr.length();

            if (RegexValidators.isConsCaracter(lexemaStr)) {
                tabelaSimbolos.adicionarOuAtualizarSimbolo("C02", lexemaStr, tamanhoAntesTruncagem, tamanhoDepoisTruncagem, "constanteCaracter", linhaAtual);
                return new Atomo("C02", lexemaStr, linhaAtual, posicao);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private Atomo processarSimbolo(char atual) {

        String simbolo = String.valueOf(atual);

        if (tabelaReservadas.isReservada(simbolo)) {
            posicao++;
            return new Atomo(tabelaReservadas.getCodigoAtomo(simbolo), simbolo, linhaAtual,posicao);
        } else {
            filtroPrimeiroNivel(atual);
            return null;
        }
    }

    private boolean filtroSegundoNivel(char atual) {
        if (Character.isWhitespace(atual)) {
            posicao++;
            return true;
        }

        if (isComentarioLinha(atual)) {
            return true;
        }
        if (isComentarioBloco(atual)) {
            return true;
        }
        return false;
    }

    private void filtroPrimeiroNivel(char atual) {
        while (ehCaractereInvalido(atual) && posicao < conteudo.length()) {
            atual = conteudo.charAt(posicao);
            if (atual == '\n') {
                linhaAtual++;
            }

            if(!ehCaractereInvalido(atual)){
                break;
            }
            posicao++;
        }
    }

    private boolean ehCaractereInvalido(char c) {
        if (Character.isWhitespace(c) || c == '\n') {
            return false;
        }

        if (Character.isLetterOrDigit(c) || c == '_') {
            return false;
        }

        if (c == '"' || c == '\'') {
            return false;
        }

        if (tabelaReservadas.isReservada(String.valueOf(c))) {
            return false;
        }

        return true;
    }
    private boolean isComentarioLinha(char atual) {
        if (posicao + 1 < conteudo.length() && atual == '/' && conteudo.charAt(posicao + 1) == '/') {
            posicao += 2;

            while (posicao < conteudo.length()) {
                atual = conteudo.charAt(posicao);
                if (atual == '\n' || atual == '\r') {
                    linhaAtual++;
                    posicao++;
                    break;
                }
                posicao++;
            }
            return true;
        }
        return false;
    }
    private boolean isComentarioBloco(char atual) {
        if (posicao + 1 < conteudo.length() && atual == '/' && conteudo.charAt(posicao + 1) == '*') {
            posicao += 2;
            boolean encontrouFechamento = false;
            while (posicao < conteudo.length()) {
                atual = conteudo.charAt(posicao);
                if (atual == '\n') {
                    linhaAtual++;
                }
                if (posicao + 1 < conteudo.length() && atual == '*' && conteudo.charAt(posicao + 1) == '/') {
                    posicao += 2;
                    encontrouFechamento = true;
                    break;
                }
                posicao++;
            }
            if (!encontrouFechamento) {
                posicao = conteudo.length();
            }
            return true;
        }
        return false;
    }

}
