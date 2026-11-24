import java.util.Scanner;

public class LivrariaCli {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
                case 1 -> System.out.println("-> Cadastrar Produto (em breve)");
                case 2 -> System.out.println("-> Consultar Estoque (em breve)");
                case 3 -> System.out.println("-> Entrada de Estoque (em breve)");
                case 4 -> System.out.println("-> Saída de Estoque (em breve)");
                case 5 -> System.out.println("-> Realizar Venda (em breve)");
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
}
