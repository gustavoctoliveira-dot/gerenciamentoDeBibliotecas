public class Livro {
    private String titulo;
    private String autor;
    private String isbn;
    private boolean disponivel; // Atributo privado protegendo o estado

    public Livro(String titulo, String autor, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.disponivel = true;
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

    public boolean emprestar() {
        if (this.disponivel) {
            this.disponivel = false;
            return true;
        }
        return false;
    }

    public void devolver() {
        this.disponivel = true;
    }

    @Override
    public String toString() {
        String status = disponivel ? "Disponível" : "Emprestado";
        return String.format("'%s' - %s (ISBN: %s) [%s]", titulo, autor, isbn, status);
    }
}
