public class Livro {
    private final String titulo;
    private final String autor;
    private final String isbn;
    private boolean disponivel;
    private Usuario usuarioEmprestimo;

    public Livro(String titulo, String autor, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.disponivel = true;
        this.usuarioEmprestimo = null;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public Usuario getUsuarioEmprestimo() {
        return usuarioEmprestimo;
    }

    public boolean emprestar(Usuario usuario) throws LivroIndisponivelException {
        if (usuario == null) {
            return false;
        }

        if (!disponivel) {
            throw new LivroIndisponivelException("O livro '" + titulo + "' não está disponível.");
        }

        disponivel = false;
        usuarioEmprestimo = usuario;
        return true;
    }

    public void devolver() {
        this.disponivel = true;
        this.usuarioEmprestimo = null;
    }

    @Override
    public String toString() {
        String status = disponivel ? "Disponível" : "Emprestado";
        String comUsuario = usuarioEmprestimo != null ? " | Emprestado para: " + usuarioEmprestimo.getNome() : "";
        return String.format("'%s' - %s (ISBN: %s) [%s]%s", titulo, autor, isbn, status, comUsuario);
    }
}
