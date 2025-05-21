
# Desafio - Spotify API

## 📋 Descrição

Este projeto foi desenvolvido como parte de um desafio técnico com o objetivo de demonstrar habilidades práticas de desenvolvimento Android, consumo de APIs REST e boas práticas de arquitetura e codificação.

A proposta consiste em criar um aplicativo Android nativo que se comunica com a API do Spotify, permitindo listar os artistas mais ouvidos, visualizar seus álbuns, consultar playlists do usuário, criar novas playlists e exibir os dados do perfil.

## 🎯 Objetivos

- ✅ Autenticar com o Spotify via OAuth.
- ✅ Listar os artistas que o usuário mais ouve.
- ✅ Listar os álbuns dos artistas.
- ✅ Listar as playlists do usuário.
- ✅ Criar uma nova playlist.
- ✅ Exibir os dados do perfil do usuário.

A interface foi construída com base no protótipo disponibilizado no Figma

---

## 🏛️ Arquitetura e Tecnologias

- **Arquitetura:** MVVM (Model - View - ViewModel) 
- **Persistência:** Room Database para armazenamento local e funcionamento offline
- **Injeção de dependência:** ViewModel com viewModels e lifecycle-aware components
- **Gerenciamento de estado:** LiveData
- **Corrotinas:** Kotlin Coroutines para chamadas assíncronas
- **API:** Retrofit para consumo da API do Spotify
- **Paginação:** Android Paging 3
- **Cache:** Banco de dados local e SharedPreferences
- **UI:** ViewBinding, RecyclerView, Material Design Components
- **Imagens:** Glide para carregamento e cache de imagens

---

## 🏁 Como executar o projeto

### ✅ Pré-requisitos

- Android Studio 
- Conta de desenvolvedor no [Spotify for Developers](https://developer.spotify.com/)
- Configurar uma aplicação no Spotify e obter `Client ID` e `Redirect URI`
- SDK mínimo: 24 (Android 7.0)
- Conexão com a internet para consumo da API

### ✅ Configurações iniciais

1. Clone este repositório:
   ```bash
   git clone https://github.com/joaovitormo/desafio-luizalabs.git
   ```
   
2. Abra o projeto no Android Studio.

3. Configure os valores de `CLIENT_ID` e `REDIRECT_URI` no arquivo de configuração adequado (local.properties).

4. Execute o projeto em um dispositivo ou emulador.

---

## ✅ Funcionalidades implementadas

- [x] Autenticação via Spotify OAuth
- [x] Listagem de artistas mais ouvidos
- [x] Listagem de álbuns dos artistas
- [x] Listagem de playlists do usuário
- [x] Criação de nova playlist
- [x] Exibição dos dados do perfil do usuário
- [x] Paginação com suporte a scroll infinito
- [x] Funcionamento offline com armazenamento local
- [x] Segmentação de commits
- [x] Testes unitários básicos

---

## 📚 Documentação da API utilizada

Este projeto utiliza as seguintes rotas da [Spotify Web API](https://developer.spotify.com/documentation/web-api):

- ✅ [Get User's Top Artists and Tracks](https://developer.spotify.com/documentation/web-api/reference/get-users-top-artists-and-tracks)
- ✅ [Get an Artist's Albums](https://developer.spotify.com/documentation/web-api/reference/get-an-artists-albums)
- ✅ [Get a List of Current User's Playlists](https://developer.spotify.com/documentation/web-api/reference/get-a-list-of-current-users-playlists)
- ✅ [Create Playlist](https://developer.spotify.com/documentation/web-api/reference/create-playlist)
- ✅ [Get Current User's Profile](https://developer.spotify.com/documentation/web-api/reference/get-current-users-profile)

---

## 🛠️ Padrões e boas práticas adotados

- Arquitetura MVVM para desacoplamento e testabilidade.
- Camada de repositório para intermediar dados locais e remotos.
- Paginação com controle de estado de carregamento.
- Persistência local com Room para dados offline.
- Tratamento de erros com exibição de mensagens amigáveis.
- Navegação segura com Navigation Component.
- Glide para carregamento eficiente de imagens.
- Segregação de responsabilidades e organização em pacotes.

---

## 👨‍💻 Autor

João Vitor Oliveira  
[🔗 LinkedIn](https://www.linkedin.com/in/joaovitormo) • [🔗 GitHub](https://github.com/joaovitormo)


