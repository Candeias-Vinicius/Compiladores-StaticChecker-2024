import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class AnalisadorSintatico {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do arquivo fonte (sem extensão .242): ");
        String inputName = scanner.nextLine().trim();
        scanner.close();

        String fileName = inputName.endsWith(".242") ? inputName : inputName + ".242";
        File file;

        if (Paths.get(fileName).isAbsolute()) {
            file = new File(fileName);
        } else {
            file = new File(System.getProperty("user.dir"), fileName);
        }

        if (!file.exists()) {
            System.err.println("Erro: Arquivo não encontrado - " + file.getAbsolutePath());
            System.exit(1);
        } else if (!file.getName().endsWith(".242")) {
            System.err.println("Erro: Extensão do arquivo incorreta. Deve ser .242");
            System.exit(1);
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));

            // Instancia a tabela de símbolos
            TabelaSimbolos tabelaSimbolos = new TabelaSimbolos();

            // Instancia o analisador léxico
            AnalisadorLexico analisadorLexico = new AnalisadorLexico(content, tabelaSimbolos);

            // Analisa o conteúdo
            List<Atomo> listaAtomos = analisadorLexico.analisar();

            // Gera o relatório da análise léxica
            gerarRelatorioLexico(listaAtomos, tabelaSimbolos, inputName);

            // Gera o relatório da tabela de símbolos
            gerarRelatorioTabelaSimbolos(tabelaSimbolos, inputName);

            System.out.println("Análise concluída com sucesso para o arquivo: " + file.getName());
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Gera o relatório da análise léxica e salva em um arquivo .LEX.
     *
     * @param listaAtomos    Lista de átomos identificados.
     * @param tabelaSimbolos Tabela de símbolos preenchida.
     * @param nomeArquivo    Nome do arquivo fonte analisado.
     */
    private static void gerarRelatorioLexico(List<Atomo> listaAtomos,
                                             TabelaSimbolos tabelaSimbolos, String nomeArquivo) {

        // Informações da equipe
        String codigoEquipe = "Equipe 42";
        String[] nomesMembros = {"Alice", "Bob", "Charlie"};
        String[] emailsMembros = {"alice@example.com",
                "bob@example.com", "charlie@example.com"};
        String[] telefonesMembros = {"(11) 99999-1111",
                "(11) 99999-2222", "(11) 99999-3333"};

        String nomeArquivoLex = nomeArquivo.replace(".242", ".LEX");

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(nomeArquivoLex))) {

            // Cabeçalho
            writer.write("Código da Equipe: " + codigoEquipe + "\n");
            writer.write("Membros da Equipe:\n");
            for (int i = 0; i < nomesMembros.length; i++) {
                writer.write("Nome: " + nomesMembros[i] + ", Email: "
                        + emailsMembros[i] + ", Telefone: "
                        + telefonesMembros[i] + "\n");
            }
            writer.write("RELATÓRIO DA ANÁLISE LÉXICA\n");
            writer.write("Arquivo analisado: " + nomeArquivo + "\n\n");

            // Corpo do relatório
            for (Atomo atomo : listaAtomos) {
                String lexema = atomo.getLexema();
                String codigoAtomo = atomo.getCodigo();

                // Verifica se o lexema está na tabela de símbolos
                Simbolo simbolo = tabelaSimbolos.getSimbolo(lexema);
                String indiceTabela = simbolo != null
                        ? String.valueOf(simbolo.getNumeroEntrada())
                        : "-";

                // Linha onde o lexema foi encontrado
                int linha = atomo.getLinha();

                // Escreve a linha do relatório
                writer.write("Lexema: " + lexema + ", Código: "
                        + codigoAtomo + ", Índice: " + indiceTabela
                        + ", Linha: " + linha + "\n");
            }

            System.out.println("Relatório da análise léxica salvo em: "
                    + nomeArquivoLex);

        } catch (IOException e) {
            System.err.println("Erro ao gerar o relatório léxico: "
                    + e.getMessage());
        }
    }

    /**
     * Gera o relatório da tabela de símbolos e salva em um arquivo .TAB.
     *
     * @param tabelaSimbolos Tabela de símbolos preenchida.
     * @param nomeArquivo    Nome do arquivo fonte analisado.
     */
    private static void gerarRelatorioTabelaSimbolos(
            TabelaSimbolos tabelaSimbolos, String nomeArquivo) {

        String nomeArquivoTab = nomeArquivo.replace(".242", ".TAB");

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(nomeArquivoTab))) {

            // Cabeçalho
            writer.write("RELATÓRIO DA TABELA DE SÍMBOLOS\n");
            writer.write("Arquivo analisado: " + nomeArquivo + "\n\n");

            // Corpo do relatório
            for (Simbolo simbolo : tabelaSimbolos.getSimbolos()) {
                writer.write("Número de Entrada: "
                        + simbolo.getNumeroEntrada() + "\n");
                writer.write("Código do Átomo: " + simbolo.getCodigoAtomo()
                        + "\n");
                writer.write("Lexema: " + simbolo.getLexema() + "\n");
                writer.write("Tamanho Antes da Truncagem: "
                        + simbolo.getTamanhoAntesTruncagem() + "\n");
                writer.write("Tamanho Depois da Truncagem: "
                        + simbolo.getTamanhoDepoisTruncagem() + "\n");
                writer.write("Tipo do Símbolo: " + simbolo.getTipoSimbolo()
                        + "\n");
                writer.write("Linhas onde aparece: "
                        + simbolo.getLinhas().toString() + "\n");
                writer.write("---------------------------------------\n");
            }

            System.out.println("Relatório da tabela de símbolos salvo em: "
                    + nomeArquivoTab);

        } catch (IOException e) {
            System.err.println("Erro ao gerar o relatório da tabela de símbolos: "
                    + e.getMessage());
        }
    }
}
