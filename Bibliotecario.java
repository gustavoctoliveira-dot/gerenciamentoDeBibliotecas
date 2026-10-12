public class Bibliotecario extends Usuario {
    public Bibliotecario(String nome, int idUsuario) {
        super(nome, idUsuario);
    }

    @Override
    public int calcularPrazoDevolucao() {
        return 30;
    }

    public void cadastrarLivro(Biblioteca biblioteca, Livro livro) {
        if (biblioteca == null || livro == null) {
            System.out.println("⚠️ Não foi possível cadastrar o livro.");
            return;
        }

        biblioteca.adicionarLivro(livro);
        System.out.println("🛠️ Bibliotecário(a) " + nome + " cadastrou o livro '" + livro.getTitulo() + "'.");
    }

    public void removerLivro(Biblioteca biblioteca, Livro livro) {
        if (biblioteca == null || livro == null) {
            System.out.println("⚠️ Não foi possível remover o livro.");
            return;
        }

        biblioteca.removerLivro(livro);
        System.out.println("🗑️ Bibliotecário(a) " + nome + " removeu o livro '" + livro.getTitulo() + "'.");
    }

    public void registrarUsuario(Biblioteca biblioteca, Usuario usuario) {
        if (biblioteca == null || usuario == null) {
            System.out.println("⚠️ Não foi possível registrar o usuário.");
            return;
        }

        biblioteca.registrarUsuario(usuario);
    }
}
