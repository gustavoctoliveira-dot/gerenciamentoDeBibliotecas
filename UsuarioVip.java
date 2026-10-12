public class UsuarioVIP extends Usuario {
    public UsuarioVIP(String nome, int idUsuario) {
        super(nome, idUsuario);
    }

    @Override
    public int calcularPrazoDevolucao() {
        return 14;
    }
}
