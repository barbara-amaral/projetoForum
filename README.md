
# Projeto Fórum

[![<barbara-amaral>](https://circleci.com/gh/barbara-amaral/projetoforum.svg?style=shield)](<https://circleci.com/gh/barbara-amaral/projetoforum>)

[![codecov](https://codecov.io/gh/barbara-amaral/projetoforum/branch/master/graph/badge.svg)](https://codecov.io/gh/barbara-amaral/projetoforum)

Projeto que se trata de um fórum de perguntas e respostas.
Desenvolvido em Java, utilizando Spring Boot e o banco de dados MongoDB.

## Documentação

Você pode testar essa aplicação através do [Swagger](https://projeto-forum.herokuapp.com/swagger-ui.html).

## Informações importantes:
  
  • Para a autenticação é utilizado o token JWT, e a autenticação é do tipo Bearer. <br />
  • Quase todos os métodos necessitam de autenticação, com excessão dos métodos de listar os tópicos e as respostas. <br />
  • Para fazer o login e se autenticar, é preciso ter um cadastro antes. <br />
  • Para testar métodos restritos, como o de listar os usuários cadastrados, é necessário ser um ADMIN. <br />
  • Para cadastrar um usuário ADMIN, basta cadastrar um e-mail que termine com "@admin.com". Todos os outros recebem perfil de acesso USER. <br />
  
  #### Atenção: 
  
  • Se quiser receber o e-mail de boas vindas, cadastre um e-mail válido. <br />
  • A aplicação também envia, todos os dias, e-mails de recomendações de tópicos baseados nos tópicos que você cadastrou. <br />
  • Se não quiser mais receber os e-mails, basta deletar seu usuário.
  
  
  
