import java.util.*;

public class AnalisadorLexico {

    private String conteudo;
    private int posicao;
    private int linhaAtual;

    private TabelaSimbolos tabelaSimbolos;

    public static final int LIMITE_ATOMO = 30;

    public AnalisadorLexico(String conteudo, TabelaSimbolos tabelaSimbolos) {
        this.conteudo = conteudo.toUpperCase();
        this.posicao = 0;
        this.linhaAtual = 1;
        this.tabelaSimbolos = tabelaSimbolos;
    }

    public Atomo getAtomo() {
        while (posicao < conteudo.length()) {

            char atual = conteudo.charAt(posicao);


            if (atual == '\n') {
                linhaAtual++;
                posicao++;
                continue;
            }


            if (Character.isWhitespace(atual)) {
                posicao++;
                continue;
            }


            if (posicao + 1 < conteudo.length() && atual == '/'
                    && conteudo.charAt(posicao + 1) == '/') {
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
                continue;
            }

            if (posicao + 1 < conteudo.length() && atual == '/'
                    && conteudo.charAt(posicao + 1) == '*') {
                posicao += 2; // Pula "/*"
                boolean encontrouFechamento = false;
                while (posicao < conteudo.length()) {
                    atual = conteudo.charAt(posicao);
                    if (atual == '\n') {
                        linhaAtual++;
                    }
                    if (posicao + 1 < conteudo.length() && atual == '*'
                            && conteudo.charAt(posicao + 1) == '/') {
                        posicao += 2; // Pula "*/"
                        encontrouFechamento = true;
                        break;
                    }
                    posicao++;
                }
                if (!encontrouFechamento) {

                    posicao = conteudo.length();
                }
                continue;
            }

            if (Character.isLetter(atual) || atual == '_') {
                StringBuilder lexemaBuilder = new StringBuilder();
                int contadorCaracteres = 0;

                int tamanhoAntesTruncagem = 0;

                while (posicao < conteudo.length()
                        && (Character.isLetterOrDigit(
                        conteudo.charAt(posicao))
                        || conteudo.charAt(posicao) == '_')) {

                    tamanhoAntesTruncagem++;
                    if (contadorCaracteres < LIMITE_ATOMO) {
                        lexemaBuilder.append(conteudo.charAt(posicao));
                        contadorCaracteres++;
                    }
                    posicao++;
                }

                String lexema = lexemaBuilder.toString();
                int tamanhoDepoisTruncagem = lexema.length();

                String codigoAtomo = TabelaReservadas.getCodigoAtomo(
                        lexema);

                if (codigoAtomo != null) {
                    return new Atomo(codigoAtomo, lexema, linhaAtual);
                }

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
                    continue;
                }

                tabelaSimbolos.adicionarOuAtualizarSimbolo(
                        codigoIdentificador, lexema, tamanhoAntesTruncagem,
                        tamanhoDepoisTruncagem, tipoSimbolo, linhaAtual);

                return new Atomo(codigoIdentificador, lexema, linhaAtual);
            }

            else if (Character.isDigit(atual)) {
                StringBuilder lexemaBuilder = new StringBuilder();
                int contadorCaracteres = 0;
                int tamanhoAntesTruncagem = 0;

                boolean isReal = false;

                while (posicao < conteudo.length()
                        && Character.isDigit(conteudo.charAt(posicao))) {
                    tamanhoAntesTruncagem++;
                    if (contadorCaracteres < LIMITE_ATOMO) {
                        lexemaBuilder.append(conteudo.charAt(posicao));
                        contadorCaracteres++;
                    }
                    posicao++;
                }

                if (posicao < conteudo.length()
                        && conteudo.charAt(posicao) == '.') {
                    if (contadorCaracteres < LIMITE_ATOMO) {
                        lexemaBuilder.append('.');
                        contadorCaracteres++;
                        posicao++;
                        isReal = true;
                    } else {

                        String lexemaStr = lexemaBuilder.toString();
                        int tamanhoDepoisTruncagem = lexemaStr.length();

                        if (RegexValidators.isConsInteiro(lexemaStr)) {

                            tabelaSimbolos.adicionarOuAtualizarSimbolo(
                                    "C03", lexemaStr, tamanhoAntesTruncagem,
                                    tamanhoDepoisTruncagem,
                                    "constanteInteira", linhaAtual);
                            return new Atomo("C03", lexemaStr, linhaAtual);
                        } else {

                            continue;
                        }
                    }

                    while (posicao < conteudo.length()
                            && Character.isDigit(conteudo.charAt(posicao))) {
                        tamanhoAntesTruncagem++;
                        if (contadorCaracteres < LIMITE_ATOMO) {
                            lexemaBuilder.append(
                                    conteudo.charAt(posicao));
                            contadorCaracteres++;
                        }
                        posicao++;
                    }

                    if (posicao < conteudo.length()
                            && (conteudo.charAt(posicao) == 'E')) {
                        if (contadorCaracteres < LIMITE_ATOMO) {
                            lexemaBuilder.append('E');
                            contadorCaracteres++;
                            posicao++;
                        } else {

                            String lexemaStr = lexemaBuilder.toString();
                            int tamanhoDepoisTruncagem = lexemaStr.length();

                            if (RegexValidators.isConsReal(lexemaStr)) {
                                // Armazena na tabela de símbolos
                                tabelaSimbolos.adicionarOuAtualizarSimbolo(
                                        "C04", lexemaStr,
                                        tamanhoAntesTruncagem,
                                        tamanhoDepoisTruncagem,
                                        "constanteReal", linhaAtual);
                                return new Atomo("C04", lexemaStr, linhaAtual);
                            } else {

                                continue;
                            }
                        }

                        if (posicao < conteudo.length()
                                && (conteudo.charAt(posicao) == '+'
                                || conteudo.charAt(posicao) == '-')) {
                            if (contadorCaracteres < LIMITE_ATOMO) {
                                lexemaBuilder.append(
                                        conteudo.charAt(posicao));
                                contadorCaracteres++;
                                posicao++;
                            } else {

                                String lexemaStr = lexemaBuilder.toString();
                                int tamanhoDepoisTruncagem
                                        = lexemaStr.length();

                                if (RegexValidators.isConsReal(
                                        lexemaStr)) {

                                    tabelaSimbolos.adicionarOuAtualizarSimbolo(
                                            "C04", lexemaStr,
                                            tamanhoAntesTruncagem,
                                            tamanhoDepoisTruncagem,
                                            "constanteReal", linhaAtual);
                                    return new Atomo("C04", lexemaStr,
                                            linhaAtual);
                                } else {

                                    continue;
                                }
                            }
                        }

                        while (posicao < conteudo.length()
                                && Character.isDigit(
                                conteudo.charAt(posicao))) {
                            tamanhoAntesTruncagem++;
                            if (contadorCaracteres < LIMITE_ATOMO) {
                                lexemaBuilder.append(
                                        conteudo.charAt(posicao));
                                contadorCaracteres++;
                            }
                            posicao++;
                        }
                    }

                    String lexemaStr = lexemaBuilder.toString();
                    int tamanhoDepoisTruncagem = lexemaStr.length();

                    if (RegexValidators.isConsReal(lexemaStr)) {

                        tabelaSimbolos.adicionarOuAtualizarSimbolo(
                                "C04", lexemaStr, tamanhoAntesTruncagem,
                                tamanhoDepoisTruncagem, "constanteReal",
                                linhaAtual);
                        return new Atomo("C04", lexemaStr, linhaAtual);
                    } else {

                        continue;
                    }
                } else {

                    String lexemaStr = lexemaBuilder.toString();
                    int tamanhoDepoisTruncagem = lexemaStr.length();

                    if (RegexValidators.isConsInteiro(lexemaStr)) {

                        tabelaSimbolos.adicionarOuAtualizarSimbolo(
                                "C03", lexemaStr, tamanhoAntesTruncagem,
                                tamanhoDepoisTruncagem, "constanteInteira",
                                linhaAtual);
                        return new Atomo("C03", lexemaStr, linhaAtual);
                    } else {

                        continue;
                    }
                }
            }

            else if (atual == '"') {
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

                            if (lexemaBuilder.charAt(
                                    lexemaBuilder.length() - 1) != '"') {
                                lexemaBuilder.append('"');
                                contadorCaracteres++;
                            }
                            terminou = true;
                            break;
                        }
                    }
                }

                if (!terminou && contadorCaracteres >= LIMITE_ATOMO) {
                    if (lexemaBuilder.charAt(
                            lexemaBuilder.length() - 1) != '"') {
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

                    tabelaSimbolos.adicionarOuAtualizarSimbolo("C01",
                            lexemaStr, tamanhoAntesTruncagem,
                            tamanhoDepoisTruncagem, "constanteCadeia",
                            linhaAtual);
                    return new Atomo("C01", lexemaStr, linhaAtual);
                } else {

                    continue;
                }
            }

            else if (atual == '\'') {
                StringBuilder lexemaBuilder = new StringBuilder();
                lexemaBuilder.append('\'');
                posicao++;

                int tamanhoAntesTruncagem = 1;

                if (posicao < conteudo.length()) {
                    char c = conteudo.charAt(posicao);
                    tamanhoAntesTruncagem++;
                    lexemaBuilder.append(c);
                    posicao++;

                    if (posicao < conteudo.length()
                            && conteudo.charAt(posicao) == '\'') {
                        tamanhoAntesTruncagem++;
                        lexemaBuilder.append('\'');
                        posicao++;

                        String lexemaStr = lexemaBuilder.toString();
                        int tamanhoDepoisTruncagem = lexemaStr.length();

                        if (RegexValidators.isConsCaracter(lexemaStr)) {

                            tabelaSimbolos.adicionarOuAtualizarSimbolo("C02",
                                    lexemaStr, tamanhoAntesTruncagem,
                                    tamanhoDepoisTruncagem,
                                    "constanteCaracter", linhaAtual);
                            return new Atomo("C02", lexemaStr, linhaAtual);
                        } else {

                            continue;
                        }
                    } else {

                        continue;
                    }
                } else {

                    continue;
                }
            }


            else {

                String[] simbolosOrdenados = {"!=", "<=", ">=", "==", "&&",
                        "||"};
                boolean encontrado = false;

                for (String simbolo : simbolosOrdenados) {
                    int fim = posicao + simbolo.length();
                    if (fim <= conteudo.length()
                            && conteudo.substring(posicao, fim).equals(
                            simbolo)) {
                        posicao += simbolo.length();
                        String codigoAtomo = TabelaReservadas.getCodigoAtomo(
                                simbolo);
                        if (codigoAtomo != null) {
                            return new Atomo(codigoAtomo, simbolo, linhaAtual);
                        } else {

                            encontrado = true;
                            break;
                        }
                    }
                }

                if (!encontrado) {

                    String simbolo = String.valueOf(atual);
                    String codigoAtomo = TabelaReservadas.getCodigoAtomo(
                            simbolo);
                    if (codigoAtomo != null) {
                        posicao++;
                        return new Atomo(codigoAtomo, simbolo, linhaAtual);
                    } else {

                        posicao++;
                        continue;
                    }
                }
            }


            posicao++;
        }

        return null;
    }

    public List<Atomo> analisar() {
        List<Atomo> listaAtomos = new ArrayList<>();
        Atomo atomo;
        while ((atomo = getAtomo()) != null) {
            listaAtomos.add(atomo);
        }
        return listaAtomos;
    }
}
