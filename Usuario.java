import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {
    protected String nome;
    protected int idUsuario;
    protected List<Livro> livrosEmprestados;

    public Usuario(String nome, int idUsuario) {
        this.nome = nome;
        this.idUsuario = idUsuario;
        this.livrosEmprestados = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public List<Livro> getLivrosEmprestados() {
        return livrosEmprestados;
    }

    public abstract int calcularPrazoDevolucao();

    public boolean pegarEmprestado(Livro livro) {
        if (livro == null) {
            System.out.println("⚠️ Livro inválido.");
            return false;
        }

        if (!livro.isDisponivel()) {
            System.out.println("❌ O livro '" + livro.getTitulo() + "' já está emprestado.");
            return false;
        }

        if (livrosEmprestados.contains(livro)) {
            System.out.println("⚠️ " + nome + " já possui o livro '" + livro.getTitulo() + "'.");
            return false;
        }

        if (livro.emprestar(this)) {
            livrosEmprestados.add(livro);
            int dias = calcularPrazoDevolucao();
            System.out.println("✅ " + nome + " pegou '" + livro.getTitulo() + "'. Prazo de devolução: " + dias + " dias.");
            return true;
        }

        System.out.println("❌ Não foi possível realizar o empréstimo de '" + livro.getTitulo() + "'.");
        return false;
    }

    public void devolverLivro(Livro livro) {
        if (livro == null) {
            System.out.println("⚠️ Livro inválido.");
            return;
        }

        if (!livrosEmprestados.contains(livro)) {
            System.out.println("⚠️ " + nome + " não possui o livro '" + livro.getTitulo() + "'.");
            return;
        }

        livro.devolver();
        livrosEmprestados.remove(livro);
        System.out.println("🔄 " + nome + " devolveu '" + livro.getTitulo() + "'.");
    }

    public void listarLivrosEmprestados() {
        if (livrosEmprestados.isEmpty()) {
            System.out.println("📚 " + nome + " não possui livros emprestados.");
            return;
        }

        System.out.println("📚 Livros de " + nome + ":");
        for (Livro livro : livrosEmprestados) {
            System.out.println("- " + livro.getTitulo() + " (prazo: " + calcularPrazoDevolucao() + " dias)");
        }
    }
}
