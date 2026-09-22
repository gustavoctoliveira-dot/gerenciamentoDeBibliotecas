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

    // Método abstrato/polimórfico: cada subclasse define seu prazo
    public abstract int calcularPrazoDevolucao();

    public boolean pegarEmprestado(Livro livro) {
        if (livro.isDisponivel()) {
            if (livro.emprestar()) {
                livrosEmprestados.add(livro);
                int dias = calcularPrazoDevolucao();
                System.out.println("✅ " + nome + " pegou '" + livro.getTitulo() + "'. Prazo de devolução: " + dias + " dias.");
                return true;
            }
        }
        System.out.println("❌ Não foi possível realizar o empréstimo de '" + livro.getTitulo() + "'.");
        return false;
    }

    public void devolverLivro(Livro livro) {
        if (livrosEmprestados.contains(livro)) {
            livro.devolver();
            livrosEmprestados.remove(livro);
            System.out.println("🔄 " + nome + " devolveu '" + livro.getTitulo() + "'.");
        } else {
            System.out.println("⚠️ " + nome + " não possui o livro '" + livro.getTitulo() + "'.");
        }
    }
}
