import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class BibliotecaApi {
    private static final int PORTA_PADRAO = 8080;
    private static final int TAMANHO_MAXIMO_CORPO = 16 * 1024;
    private final Biblioteca biblioteca;

    private BibliotecaApi(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public static void main(String[] args) throws IOException {
        int porta = args.length == 0 ? PORTA_PADRAO : Integer.parseInt(args[0]);
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central");
        HttpServer servidor = HttpServer.create(new InetSocketAddress(porta), 0);
        servidor.createContext("/", new ApiHandler(biblioteca));
        servidor.start();
        System.out.println("API da biblioteca disponível em http://localhost:" + porta);
    }

    private static final class ApiHandler implements com.sun.net.httpserver.HttpHandler {
        private final BibliotecaApi api;

        private ApiHandler(Biblioteca biblioteca) {
            this.api = new BibliotecaApi(biblioteca);
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                synchronized (api.biblioteca) {
                    api.processar(exchange);
                }
            } catch (IllegalArgumentException e) {
                api.responderErro(exchange, 400, e.getMessage());
            } finally {
                exchange.close();
            }
        }
    }

    private void processar(HttpExchange exchange) throws IOException {
        String caminho = exchange.getRequestURI().getPath();
        String metodo = exchange.getRequestMethod();

        if ("/".equals(caminho) && "GET".equals(metodo)) {
            responderHtml(exchange, Files.readAllBytes(Paths.get("index.html")));
        } else if ("/api".equals(caminho) && "GET".equals(metodo)) {
            responder(exchange, 200, "{\"endpoints\":[\"GET /livros\",\"GET /usuarios\","
                    + "\"GET /emprestimos\","
                    + "\"POST /livros\",\"POST /usuarios\",\"POST /emprestimos\","
                    + "\"POST /devolucoes\"]}");
        } else if ("/livros".equals(caminho) && "GET".equals(metodo)) {
            listarLivros(exchange);
        } else if ("/usuarios".equals(caminho) && "GET".equals(metodo)) {
            listarUsuarios(exchange);
        } else if ("/emprestimos".equals(caminho) && "GET".equals(metodo)) {
            listarEmprestimos(exchange);
        } else if ("/livros".equals(caminho) && "POST".equals(metodo)) {
            cadastrarLivro(exchange);
        } else if ("/usuarios".equals(caminho) && "POST".equals(metodo)) {
            cadastrarUsuario(exchange);
        } else if ("/emprestimos".equals(caminho) && "POST".equals(metodo)) {
            emprestarLivro(exchange);
        } else if ("/devolucoes".equals(caminho) && "POST".equals(metodo)) {
            devolverLivro(exchange);
        } else if ("/livros".equals(caminho) || "/usuarios".equals(caminho)
                || "/emprestimos".equals(caminho) || "/devolucoes".equals(caminho)
                || "/".equals(caminho) || "/api".equals(caminho)) {
            responderErro(exchange, 405, "Método não permitido.");
        } else {
            responderErro(exchange, 404, "Endpoint não encontrado.");
        }
    }

    private void listarLivros(HttpExchange exchange) throws IOException {
        StringBuilder json = new StringBuilder("[");
        List<Livro> livros = biblioteca.getAcervo();
        for (int i = 0; i < livros.size(); i++) {
            Livro livro = livros.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"titulo\":").append(jsonString(livro.getTitulo()))
                    .append(",\"autor\":").append(jsonString(livro.getAutor()))
                    .append(",\"isbn\":").append(jsonString(livro.getIsbn()))
                    .append(",\"disponivel\":").append(livro.isDisponivel()).append('}');
        }
        responder(exchange, 200, json.append(']').toString());
    }

    private void listarUsuarios(HttpExchange exchange) throws IOException {
        StringBuilder json = new StringBuilder("[");
        List<Usuario> usuarios = biblioteca.getUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(usuario.getIdUsuario())
                    .append(",\"nome\":").append(jsonString(usuario.getNome()))
                    .append(",\"tipo\":").append(jsonString(usuario.getClass().getSimpleName()))
                    .append('}');
        }
        responder(exchange, 200, json.append(']').toString());
    }

    private void listarEmprestimos(HttpExchange exchange) throws IOException {
        StringBuilder json = new StringBuilder("[");
        boolean primeiro = true;
        for (Usuario usuario : biblioteca.getUsuarios()) {
            for (Livro livro : usuario.getLivrosEmprestados()) {
                if (!primeiro) {
                    json.append(',');
                }
                json.append("{\"isbn\":").append(jsonString(livro.getIsbn()))
                        .append(",\"titulo\":").append(jsonString(livro.getTitulo()))
                        .append(",\"idUsuario\":").append(usuario.getIdUsuario())
                        .append(",\"nomeUsuario\":").append(jsonString(usuario.getNome()))
                        .append(",\"prazoDias\":").append(usuario.calcularPrazoDevolucao()).append('}');
                primeiro = false;
            }
        }
        responder(exchange, 200, json.append(']').toString());
    }

    private void cadastrarLivro(HttpExchange exchange) throws IOException {
        Map<String, String> dados = lerFormulario(exchange);
        String titulo = campoObrigatorio(dados, "titulo");
        String autor = campoObrigatorio(dados, "autor");
        String isbn = campoObrigatorio(dados, "isbn");

        if (biblioteca.buscarLivroPorIsbn(isbn) != null) {
            responderErro(exchange, 409, "Já existe um livro com esse ISBN.");
            return;
        }

        Livro livro = new Livro(titulo, autor, isbn);
        biblioteca.adicionarLivro(livro);
        responder(exchange, 201, "{\"titulo\":" + jsonString(titulo)
                + ",\"autor\":" + jsonString(autor) + ",\"isbn\":" + jsonString(isbn)
                + ",\"disponivel\":true}");
    }

    private void cadastrarUsuario(HttpExchange exchange) throws IOException {
        Map<String, String> dados = lerFormulario(exchange);
        String nome = campoObrigatorio(dados, "nome");
        int id;
        try {
            id = Integer.parseInt(campoObrigatorio(dados, "id"));
        } catch (NumberFormatException e) {
            responderErro(exchange, 400, "O campo 'id' deve ser um número inteiro.");
            return;
        }

        if (biblioteca.buscarUsuarioPorId(id) != null) {
            responderErro(exchange, 409, "Já existe um usuário com esse ID.");
            return;
        }

        String tipo = campoObrigatorio(dados, "tipo").toUpperCase(Locale.ROOT);
        Usuario usuario;
        if ("COMUM".equals(tipo)) {
            usuario = new UsuarioComum(nome, id);
        } else if ("VIP".equals(tipo)) {
            usuario = new UsuarioVIP(nome, id);
        } else {
            responderErro(exchange, 400, "O campo 'tipo' deve ser COMUM ou VIP.");
            return;
        }

        biblioteca.registrarUsuario(usuario);
        responder(exchange, 201, "{\"id\":" + id + ",\"nome\":" + jsonString(nome)
                + ",\"tipo\":" + jsonString(usuario.getClass().getSimpleName()) + "}");
    }

    private void emprestarLivro(HttpExchange exchange) throws IOException {
        Map<String, String> dados = lerFormulario(exchange);
        Usuario usuario = buscarUsuario(exchange, dados);
        if (usuario == null) {
            return;
        }
        Livro livro = buscarLivro(exchange, dados);
        if (livro == null) {
            return;
        }

        try {
            if (!usuario.pegarEmprestado(livro)) {
                responderErro(exchange, 409, "Não foi possível realizar o empréstimo.");
                return;
            }
        } catch (LivroIndisponivelException e) {
            responderErro(exchange, 409, e.getMessage());
            return;
        }

        responder(exchange, 201, "{\"mensagem\":\"Empréstimo realizado.\",\"isbn\":"
                + jsonString(livro.getIsbn()) + ",\"idUsuario\":" + usuario.getIdUsuario()
                + ",\"prazoDias\":" + usuario.calcularPrazoDevolucao() + "}");
    }

    private void devolverLivro(HttpExchange exchange) throws IOException {
        Map<String, String> dados = lerFormulario(exchange);
        Usuario usuario = buscarUsuario(exchange, dados);
        if (usuario == null) {
            return;
        }
        Livro livro = buscarLivro(exchange, dados);
        if (livro == null) {
            return;
        }

        if (!usuario.getLivrosEmprestados().contains(livro)) {
            responderErro(exchange, 409, "Esse usuário não possui o livro emprestado.");
            return;
        }

        usuario.devolverLivro(livro);
        responder(exchange, 200, "{\"mensagem\":\"Devolução realizada.\",\"isbn\":"
                + jsonString(livro.getIsbn()) + "}");
    }

    private Usuario buscarUsuario(HttpExchange exchange, Map<String, String> dados) throws IOException {
        int id;
        try {
            id = Integer.parseInt(campoObrigatorio(dados, "idUsuario"));
        } catch (IllegalArgumentException e) {
            responderErro(exchange, 400, "O campo 'idUsuario' deve ser um número inteiro.");
            return null;
        }

        Usuario usuario = biblioteca.buscarUsuarioPorId(id);
        if (usuario == null) {
            responderErro(exchange, 404, "Usuário não encontrado.");
        }
        return usuario;
    }

    private Livro buscarLivro(HttpExchange exchange, Map<String, String> dados) throws IOException {
        String isbn;
        try {
            isbn = campoObrigatorio(dados, "isbn");
        } catch (IllegalArgumentException e) {
            responderErro(exchange, 400, e.getMessage());
            return null;
        }

        Livro livro = biblioteca.buscarLivroPorIsbn(isbn);
        if (livro == null) {
            responderErro(exchange, 404, "Livro não encontrado.");
        }
        return livro;
    }

    private Map<String, String> lerFormulario(HttpExchange exchange) throws IOException {
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        if (contentType == null
                || !contentType.toLowerCase(Locale.ROOT).startsWith("application/x-www-form-urlencoded")) {
            throw new IllegalArgumentException("Use Content-Type: application/x-www-form-urlencoded.");
        }

        ByteArrayOutputStream corpo = new ByteArrayOutputStream();
        InputStream entrada = exchange.getRequestBody();
        byte[] buffer = new byte[1024];
        int total = 0;
        int lidos;
        while ((lidos = entrada.read(buffer)) != -1) {
            total += lidos;
            if (total > TAMANHO_MAXIMO_CORPO) {
                throw new IllegalArgumentException("Corpo da requisição excede o limite permitido.");
            }
            corpo.write(buffer, 0, lidos);
        }

        String texto = new String(corpo.toByteArray(), StandardCharsets.UTF_8);
        Map<String, String> dados = new HashMap<>();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("O corpo da requisição não pode estar vazio.");
        }
        for (String par : texto.split("&")) {
            int separador = par.indexOf('=');
            if (separador < 1) {
                throw new IllegalArgumentException("Formato de formulário inválido.");
            }
            String chave = URLDecoder.decode(par.substring(0, separador), StandardCharsets.UTF_8.name());
            String valor = URLDecoder.decode(par.substring(separador + 1), StandardCharsets.UTF_8.name());
            if (dados.put(chave, valor) != null) {
                throw new IllegalArgumentException("Campo duplicado: " + chave + ".");
            }
        }
        return dados;
    }

    private String campoObrigatorio(Map<String, String> dados, String campo) {
        String valor = dados.get(campo);
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("O campo '" + campo + "' é obrigatório.");
        }
        return valor.trim();
    }

    private void responder(HttpExchange exchange, int status, String json) throws IOException {
        byte[] corpo = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, corpo.length);
        exchange.getResponseBody().write(corpo);
    }

    private void responderHtml(HttpExchange exchange, byte[] html) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(200, html.length);
        exchange.getResponseBody().write(html);
    }

    private void responderErro(HttpExchange exchange, int status, String mensagem) throws IOException {
        responder(exchange, status, "{\"erro\":" + jsonString(mensagem) + "}");
    }

    private static String jsonString(String valor) {
        StringBuilder json = new StringBuilder("\"");
        for (int i = 0; i < valor.length(); i++) {
            char caractere = valor.charAt(i);
            switch (caractere) {
                case '"':
                    json.append("\\\"");
                    break;
                case '\\':
                    json.append("\\\\");
                    break;
                case '\b':
                    json.append("\\b");
                    break;
                case '\f':
                    json.append("\\f");
                    break;
                case '\n':
                    json.append("\\n");
                    break;
                case '\r':
                    json.append("\\r");
                    break;
                case '\t':
                    json.append("\\t");
                    break;
                default:
                    if (caractere < 0x20) {
                        json.append(String.format("\\u%04x", (int) caractere));
                    } else {
                        json.append(caractere);
                    }
            }
        }
        return json.append('"').toString();
    }
}
