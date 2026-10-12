public class Bibliotecario extends Usuario {
    public Bibliotecario(String nome, int idUsuario) {
        super(nome, idUsuario);
    }

    @Override
    public int calcularPrazoDevolucao() {
        return 30;
    }

    public void cadastrarLivro(Biblioteca biblioteca, Livro livro) {
        biblioteca.adicionarLivro(livro);
        System.out.println("🛠️ Bibliotecário(a) " + nome + " cadastrou o livro '" + livro.getTitulo() + "'.");
    }
}
