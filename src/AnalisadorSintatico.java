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

            TabelaSimbolos tabelaSimbolos = new TabelaSimbolos();

            AnalisadorLexico analisadorLexico = new AnalisadorLexico(content, tabelaSimbolos);

            List<Atomo> listaAtomos = analisadorLexico.analisar();

            gerarRelatorioLexico(listaAtomos, tabelaSimbolos, fileName);

            gerarRelatorioTabelaSimbolos(tabelaSimbolos, fileName);

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

        String codigoEquipe = "EQ08";
        String[] nomesMembros = {"Cauã Vivas Martins da Cruz",
                                 "Guilherme Santos Ferreira",
                                 "João Marcos Gatis Araujo Silva",
                                 "Vinicius dos Santos Candeia"};
        String[] emailsMembros = {"caua.cruz@ucsal.edu.br",
                                  "guilherme,ferreira@ucsal.edu.br",
                                  "joaomg.silva@ucsal.edu.br",
                                  "vinicius.candeias@ucsal.edu.br"};
        String[] telefonesMembros = {"71 9 9279-6647",
                                     "71 9 9630-4941",
                                     "71 9 9900-9154",
                                     "71 9 8435-3370"};

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

            for (Atomo atomo : listaAtomos) {
                String lexema = atomo.getLexema();
                String codigoAtomo = atomo.getCodigo();

                Simbolo simbolo = tabelaSimbolos.getSimbolo(lexema);
                String indiceTabela = simbolo != null
                        ? String.valueOf(simbolo.getNumeroEntrada())
                        : "N/A";

                int linha = atomo.getLinha();

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

    private static void gerarRelatorioTabelaSimbolos(
            TabelaSimbolos tabelaSimbolos, String nomeArquivo) {

        String nomeArquivoTab = nomeArquivo.replace(".242", ".TAB");

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(nomeArquivoTab))) {

            writer.write("RELATÓRIO DA TABELA DE SÍMBOLOS\n");
            writer.write("Arquivo analisado: " + nomeArquivo + "\n\n");

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
