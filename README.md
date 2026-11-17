# 📚 Sistema de Gerenciamento de Bibliotecas

Projeto em Java para controlar usuários, livros, empréstimos e devoluções de uma biblioteca. O sistema foi desenvolvido com foco em POO, encapsulamento, herança e polimorfismo.

## ✅ Funcionalidades implementadas

- Cadastro de usuários comuns e VIP
- Cadastro de bibliotecário
- Cadastro de livros no acervo
- Listagem do acervo
- Listagem de usuários cadastrados
- Empréstimo de livros
- Devolução de livros
- Exceção personalizada `LivroIndisponivelException` ao tentar emprestar um livro já emprestado
- API REST HTTP com respostas JSON
- Validação básica de duplicidade e regras de negócio
- Menu interativo no terminal

## 🧱 Conceitos de POO aplicados

- Encapsulamento: estados protegidos por métodos e controle de acesso
- Herança: `Usuario` é a classe base e `UsuarioComum`, `UsuarioVIP` e `Bibliotecario` herdam dela
- Polimorfismo: cada tipo de usuário possui regra específica de prazo de devolução
- Abstração: operações como empréstimo e devolução ficam encapsuladas nas classes corretas

## 📁 Estrutura do projeto

- `Main.java` – menu principal e fluxo interativo
- `Biblioteca.java` – gestão do acervo e usuários
- `BibliotecaApi.java` – API REST para administrar livros, usuários, empréstimos e devoluções
- `index.html` – interface web responsiva para gerenciar o acervo no navegador
- `LivroIndisponivelException.java` – exceção lançada quando um livro não está disponível
- `Livro.java` – representação dos livros
- `Usuario.java` – classe abstrata base dos usuários
- `UsuarioComum.java` – usuário comum, prazo de 7 dias
- `UsuarioVIP.java` – usuário VIP, prazo de 14 dias
- `Bibliotecario.java` – gestão do acervo e permissões de bibliotecário

## 🚀 Como executar

### Pré-requisitos

- Java JDK instalado
- Terminal ou PowerShell

### Comandos no terminal

No PowerShell do Windows:

```powershell
cd "c:\Users\Pichau\Desktop\gerenciamentoDeBibliotecas-main\gerenciamentoDeBibliotecas-main"
javac *.java
java Main
```

Ou em uma linha:

```powershell
cd "c:\Users\Pichau\Desktop\gerenciamentoDeBibliotecas-main\gerenciamentoDeBibliotecas-main"; javac *.java; java Main
```

### Executar a API REST

Compile os arquivos e inicie o servidor na porta padrão 8080:

```powershell
javac *.java
java BibliotecaApi
```

Uma porta alternativa pode ser informada como argumento: `java BibliotecaApi 8081`.

Abra `http://localhost:8080` no navegador para usar a interface gráfica. A página e a API são servidas pelo mesmo programa.

Endpoints disponíveis:

| Método | Caminho | Campos do formulário |
| --- | --- | --- |
| GET | `/api` | — |
| GET | `/livros` | — |
| GET | `/usuarios` | — |
| GET | `/emprestimos` | — |
| POST | `/livros` | `titulo`, `autor`, `isbn` |
| POST | `/usuarios` | `nome`, `id`, `tipo` (`COMUM` ou `VIP`) |
| POST | `/emprestimos` | `idUsuario`, `isbn` |
| POST | `/devolucoes` | `idUsuario`, `isbn` |

Requisições POST usam `Content-Type: application/x-www-form-urlencoded`; os endpoints da API respondem em JSON. A rota `/` serve a interface HTML. Empréstimos de livros indisponíveis retornam HTTP 409 com a mensagem da `LivroIndisponivelException`.

## 🧪 Exemplo de uso

1. Escolha a opção 1 para cadastrar um usuário
2. Escolha a opção 2 para cadastrar um livro
3. Escolha a opção 5 para emprestar um livro
4. Escolha a opção 6 para devolver um livro
5. Escolha a opção 7 para sair

## 🔎 Observação

O programa foi validado com execução em terminal, incluindo cadastro, listagem e processo de empréstimo. O sistema está funcionando corretamente no ambiente atual.

## 📌 Status

Projeto em estado funcional e pronto para uso básico.
