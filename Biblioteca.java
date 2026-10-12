import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    private final String nome;
    private final List<Livro> acervo;
    private final List<Usuario> usuarios;

    public Biblioteca(String nome) {
        this.nome = nome;
        this.acervo = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void adicionarLivro(Livro livro) {
        if (livro == null) {
            System.out.println("⚠️ Livro inválido.");
            return;
        }

        for (Livro livroExistente : acervo) {
            if (livroExistente.getIsbn().equalsIgnoreCase(livro.getIsbn())) {
                System.out.println("⚠️ Já existe um livro com o ISBN '" + livro.getIsbn() + "'.");
                return;
            }
        }

        acervo.add(livro);
        System.out.println("📚 Livro cadastrado: '" + livro.getTitulo() + "'.");
    }

    public void removerLivro(Livro livro) {
        if (livro == null) {
            System.out.println("⚠️ Livro inválido.");
            return;
        }

        if (acervo.remove(livro)) {
            System.out.println("📚 Livro removido: '" + livro.getTitulo() + "'.");
        } else {
            System.out.println("⚠️ Livro não encontrado no acervo.");
        }
    }

    public void registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            System.out.println("⚠️ Usuário inválido.");
            return;
        }

        for (Usuario usuarioExistente : usuarios) {
            if (usuarioExistente.getIdUsuario() == usuario.getIdUsuario()) {
                System.out.println("⚠️ Já existe um usuário com o ID " + usuario.getIdUsuario() + ".");
                return;
            }
        }

        usuarios.add(usuario);
        System.out.println("👤 Usuário cadastrado: " + usuario.getNome() + " (" + usuario.getClass().getSimpleName() + ")");
    }

    public List<Livro> getAcervo() {
        return acervo;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Livro buscarLivroPorIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return null;
        }

        for (Livro livro : acervo) {
            if (livro.getIsbn().equalsIgnoreCase(isbn)) {
                return livro;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorId(int idUsuario) {
        for (Usuario usuario : usuarios) {
            if (usuario.getIdUsuario() == idUsuario) {
                return usuario;
            }
        }
        return null;
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

    public void listarUsuarios() {
        System.out.println("\n--- Usuários cadastrados ---");
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        for (Usuario usuario : usuarios) {
            System.out.println("- " + usuario.getNome() + " | ID: " + usuario.getIdUsuario() + " | Tipo: " + usuario.getClass().getSimpleName());
        }
        System.out.println("-----------------------------\n");
    }
}
