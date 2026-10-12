public class UsuarioComum extends Usuario {
    public UsuarioComum(String nome, int idUsuario) {
        super(nome, idUsuario);
    }

    @Override
    public int calcularPrazoDevolucao() {
        return 7;
    }
}
