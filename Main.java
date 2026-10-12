import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central");
        Bibliotecario admin = new Bibliotecario("Carlos", 100);

        biblioteca.registrarUsuario(admin);

        System.out.println("\n=== Sistema de Gerenciamento de Biblioteca ===\n");

        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    cadastrarUsuario(biblioteca, scanner);
                    break;
                case 2:
                    cadastrarLivro(admin, biblioteca, scanner);
                    break;
                case 3:
                    biblioteca.listarAcervo();
                    break;
                case 4:
                    biblioteca.listarUsuarios();
                    break;
                case 5:
                    realizarEmprestimo(biblioteca, scanner);
                    break;
                case 6:
                    realizarDevolucao(biblioteca, scanner);
                    break;
                case 7:
                    System.out.println("Saindo do sistema...");
                    continuar = false;
                    break;
                default:
                    System.out.println("⚠️ Opção inválida. Tente novamente.");
                    break;
            }
        }

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Cadastrar usuário");
        System.out.println("2. Cadastrar livro");
        System.out.println("3. Listar acervo");
        System.out.println("4. Listar usuários");
        System.out.println("5. Emprestar livro");
        System.out.println("6. Devolver livro");
        System.out.println("7. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Digite um número válido.");
                System.out.print("Escolha uma opção: ");
            }
        }
    }

    private static String lerTexto(Scanner scanner) {
        String texto = scanner.nextLine().trim();
        while (texto.isEmpty()) {
            System.out.print("Digite um valor válido: ");
            texto = scanner.nextLine().trim();
        }
        return texto;
    }

    private static void cadastrarUsuario(Biblioteca biblioteca, Scanner scanner) {
        System.out.print("Digite o nome do usuário: ");
        String nome = lerTexto(scanner);
        System.out.print("Digite o ID do usuário: ");
        int id = lerOpcao(scanner);

        System.out.println("Tipo de usuário:");
        System.out.println("1. Comum");
        System.out.println("2. VIP");
        System.out.print("Escolha: ");
        int tipo = lerOpcao(scanner);

        Usuario usuario;
        switch (tipo) {
            case 1:
                usuario = new UsuarioComum(nome, id);
                break;
            case 2:
                usuario = new UsuarioVIP(nome, id);
                break;
            default:
                System.out.println("⚠️ Tipo inválido.");
                return;
        }

        biblioteca.registrarUsuario(usuario);
    }

    private static void cadastrarLivro(Bibliotecario admin, Biblioteca biblioteca, Scanner scanner) {
        System.out.print("Digite o título do livro: ");
        String titulo = lerTexto(scanner);
        System.out.print("Digite o autor: ");
        String autor = lerTexto(scanner);
        System.out.print("Digite o ISBN: ");
        String isbn = lerTexto(scanner);

        Livro livro = new Livro(titulo, autor, isbn);
        admin.cadastrarLivro(biblioteca, livro);
    }

    private static void realizarEmprestimo(Biblioteca biblioteca, Scanner scanner) {
        System.out.print("Digite o ID do usuário: ");
        int idUsuario = lerOpcao(scanner);
        Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);

        if (usuario == null) {
            System.out.println("⚠️ Usuário não encontrado.");
            return;
        }

        System.out.print("Digite o ISBN do livro: ");
        String isbn = lerTexto(scanner);
        Livro livro = biblioteca.buscarLivroPorIsbn(isbn);

        if (livro == null) {
            System.out.println("⚠️ Livro não encontrado no acervo.");
            return;
        }

        usuario.pegarEmprestado(livro);
    }

    private static void realizarDevolucao(Biblioteca biblioteca, Scanner scanner) {
        System.out.print("Digite o ID do usuário: ");
        int idUsuario = lerOpcao(scanner);
        Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);

        if (usuario == null) {
            System.out.println("⚠️ Usuário não encontrado.");
            return;
        }

        System.out.print("Digite o ISBN do livro a devolver: ");
        String isbn = lerTexto(scanner);
        Livro livro = biblioteca.buscarLivroPorIsbn(isbn);

        if (livro == null) {
            System.out.println("⚠️ Livro não encontrado no acervo.");
            return;
        }

        usuario.devolverLivro(livro);
    }
}
