import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class LivrariaCli {
    private static final String BASE_URL = "http://localhost:8080/loja";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao = -1;


        while (opcao != 0) {
            exibirMenu();

            if (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Digite o número da opção.");
                scanner.nextLine();
                continue;
            }

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 3 -> opcaoEntradaEstoque();
                case 2 -> opcaoConsultarEstoque();
                case 1 -> opcaoCadastrarLivro();
                case 4 -> opcaoSaidaEstoque();
                case 5 -> opcaoRealizarVenda();
                case 0 -> System.out.println("Encerrando aplicação...");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n=== LIVRARIA - CONTROLE DE ESTOQUE ===");
        System.out.println("1 - Cadastrar Produto");
        System.out.println("2 - Consultar Estoque");
        System.out.println("3 - Entrada de Estoque");
        System.out.println("4 - Saída de Estoque");
        System.out.println("5 - Realizar Venda");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void opcaoEntradaEstoque() {
        try {
            System.out.print("Código do livro (inteiro): ");
            int codigo = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Quantidade de entrada (inteiro > 0): ");
            int quantidade = Integer.parseInt(scanner.nextLine().trim());

            String json = String.format(Locale.US, "{\"quantidade\": %d}", quantidade);

            HttpRequest request = HttpRequest.newBuilder()
                     .uri(URI.create(BASE_URL + "/produto/" + codigo + "/entrada"))
                     .header("Content-Type", "application/json")
                     .PUT(HttpRequest.BodyPublishers.ofString(json))
                     .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status HTTP: " + response.statusCode());
            System.out.println("Resposta: " + response.body());
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite valores numéricos inteiros válidos.");
        } catch (Exception e) {
            System.out.println("Erro ao comunicar com a API: " + e.getMessage());
        }
    }


    private static void opcaoConsultarEstoque() {
        try {
            System.out.print("Código do livro (inteiro): ");
            int codigo = Integer.parseInt(scanner.nextLine().trim());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/produto/" + codigo + "/estoque"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status HTTP: " + response.statusCode());
            System.out.println("Resposta: " + response.body());
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite um código numérico inteiro válido.");
        } catch (Exception e) {
            System.out.println("Erro ao comunicar com a API: " + e.getMessage());
        }
    }

    private static void opcaoCadastrarLivro() {
        try {
            System.out.print("Código do livro (inteiro): ");
            int codigo = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Preço do livro (ex: 59.90): ");
            String precoStr = scanner.nextLine().trim().replace(",", ".");
            BigDecimal preco = new BigDecimal(precoStr);

            String json = String.format(Locale.US, "{\"codigo\": %d, \"preco\": %.2f}", codigo, preco);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/produto"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status HTTP: " + response.statusCode());
            System.out.println("Resposta: " + response.body());
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite valores numéricos válidos.");
        } catch (Exception e) {
            System.out.println("Erro ao comunicar com a API: " + e.getMessage());
        }
    }

private static void opcaoRealizarVenda() {
        try {
            List<String> itensJson = new ArrayList<>();

            while (true) {
                System.out.print("Código do livro (inteiro): ");
                int codigo = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Quantidade (inteiro > 0): ");
                int quantidade = Integer.parseInt(scanner.nextLine().trim());

                itensJson.add(String.format(Locale.US, "{\"codigo\": %d, \"quantidade\": %d}", codigo, quantidade));

                System.out.print("Adicionar outro item? (s/n): ");
                String continuar = scanner.nextLine().trim();
                if (!continuar.equalsIgnoreCase("s")) {
                    break;
                }
            }

            String json = "{\"itens\": [" + String.join(", ", itensJson) + "]}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/venda"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
              .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status HTTP: " + response.statusCode());
            System.out.println("Resposta: " + response.body());
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite valores numéricos inteiros válidos.");
        } catch (Exception e) {
            System.out.println("Erro ao comunicar com a API: " + e.getMessage());
        }
    }

    private static void opcaoSaidaEstoque() {
        try {
            System.out.print("Código do livro (inteiro): ");
            int codigo = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Quantidade de saída (inteiro > 0): ");
            int quantidade = Integer.parseInt(scanner.nextLine().trim());

            String json = String.format(Locale.US, "{\"quantidade\": %d}", quantidade);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/produto/" + codigo + "/saida"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status HTTP: " + response.statusCode());
            System.out.println("Resposta: " + response.body());
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite valores numéricos inteiros válidos.");
        } catch (Exception e) {
            System.out.println("Erro ao comunicar com a API: " + e.getMessage());
        }
    }
}
