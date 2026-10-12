# 📚 Sistema de Gerenciamento de Bibliotecas

Projeto desenvolvido em **Java** para exercitar e demonstrar os pilares fundamentais da **Programação Orientada a Objetos (POO)**.

## 🛠️ Conceitos de POO Aplicados

* **Encapsulamento:** Proteção do estado interno dos objetos (ex: disponibilidade dos livros e atributos privados) através de métodos de acesso e modificação.
* **Abstração:** Isolamento da complexidade de operações como empréstimo e devolução.
* **Herança:** Criação da classe base `Usuario` estendida pelas subclasses `UsuarioComum`, `UsuarioVIP` e `Bibliotecario`.
* **Polimorfismo:** Diferentes regras de prazo de devolução e permissões aplicadas dinamicamente para cada tipo de utilizador.

## 📁 Estrutura do Projeto

* `Livro.java` - Representa os livros do acervo.
* `Usuario.java` - Classe abstrata base para os utilizadores.
* `UsuarioComum.java` - Subclasse com prazo padrão de empréstimo (7 dias).
* `UsuarioVip.java` - Subclasse com prazo alargado de empréstimo (14 dias).
* `Bibliotecario.java` - Subclasse com permissões de gestão do acervo (30 dias).
* `Biblioteca.java` - Gestora do acervo de livros e utilizadores registados.
* `Main.java` - Classe principal para simulação e teste do sistema.

## 🚀 Como Executar o Projeto

### Pré-requisitos
* Java JDK 8 ou superior instalado no computador.

### Passo a passo no Terminal / Prompt de Comando

1. Clone este repositório (ou transfira os ficheiros para uma pasta):
   ```bash
   git clone [https://github.com/gustavoctoliveira-dot/gerenciamentoDeBibliotecas.git](https://github.com/gustavoctoliveira-dot/gerenciamentoDeBibliotecas.git)
   cd gerenciamentoDeBibliotecas
