import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    private String nome;
    private List<Livro> acervo;
    private List<Usuario> usuarios;

    public Biblioteca(String nome) {
        this.nome = nome;
        this.acervo = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    public void adicionarLivro(Livro livro) {
        acervo.add(livro);
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        System.out.println("👤 Usuário cadastrado: " + usuario.getNome() + " (" + usuario.getClass().getSimpleName() + ")");
    }

    public void listarAcervo() {
        System.out.println("\n--- Acervo da " + nome + " ---");
        if (acervo.isEmpty()) {
            System.out.println("Nenhum livro no acervo.");
            return;
        }
        for (int i = 0; i < acervo.size(); i++) {
            System.out.println((i + 1) + ". " + acervo.get(i));
        }
        System.out.println("-----------------------------\n");
    }
}
