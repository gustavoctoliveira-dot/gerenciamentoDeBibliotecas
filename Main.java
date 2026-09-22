public class Main {
    public static void main(String[] args) {
        // 1. Instanciando a Biblioteca
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central");

        // 2. Criando Usuários
        Bibliotecario admin = new Bibliotecario("Carlos", 100);
        UsuarioComum userComum = new UsuarioComum("Ana", 1);
        UsuarioVIP userVIP = new UsuarioVIP("Lucas", 2);

        biblioteca.registrarUsuario(userComum);
        biblioteca.registrarUsuario(userVIP);

        System.out.println("\n--- Cadastro de Livros ---");
        // 3. Criando e Cadastrando Livros
        Livro l1 = new Livro("O Hobbit", "J.R.R. Tolkien", "978-0547928227");
        Livro l2 = new Livro("Clean Code", "Robert C. Martin", "978-0132350884");

        admin.cadastrarLivro(biblioteca, l1);
        admin.cadastrarLivro(biblioteca, l2);

        // 4. Exibindo Acervo Inicial
        biblioteca.listarAcervo();

        // 5. Testando Empréstimos e Polimorfismo
        System.out.println("--- Testando Empréstimos ---");
        userComum.pegarEmprestado(l1); // Retorna 7 dias
        userVIP.pegarEmprestado(l2);    // Retorna 14 dias

        // Tentativa de pegar livro já emprestado
        userComum.pegarEmprestado(l2);

        // 6. Exibindo Acervo Atualizado
        biblioteca.listarAcervo();

        // 7. Testando Devolução
        System.out.println("--- Testando Devolução ---");
        userVIP.devolverLivro(l2);

        // 8. Exibindo Acervo Final
        biblioteca.listarAcervo();
    }
}
