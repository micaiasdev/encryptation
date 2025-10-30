# Ferramenta Criptográfica Desktop

Uma aplicação desktop desenvolvida em Java 17 e JavaFX para demonstrar e utilizar algoritmos de criptografia simétrica (AES) e assimétrica (RSA), incluindo geração de chaves e assinaturas digitais.

## 🚀 Sobre o Projeto

Este projeto é uma ferramenta gráfica (GUI) que permite ao usuário realizar operações criptográficas comuns de forma visual e intuitiva. Ele foi criado para aplicar conceitos fundamentais de segurança da informação, utilizando a biblioteca Bouncy Castle para as operações criptográficas.

![Screenshot da Aplicação](imagem.png)

## ✨ Funcionalidades Principais

O menu principal permite navegar entre cinco módulos principais:

* **1. Criptografia AES:** Criptografa um arquivo ou texto usando o algoritmo AES.
* **2. Descriptografia AES:** Descriptografa um arquivo ou texto previamente criptografado com AES.
* **3. Geração de Chaves RSA:** Gera um par de chaves (pública e privada) do algoritmo RSA e as salva em arquivos `.pem`.
* **4. Assinatura Digital RSA:** Assina um arquivo digitalmente usando uma chave privada RSA para garantir autenticidade e integridade.
* **5. Verificação de Assinatura RSA:** Verifica se uma assinatura digital é válida para um determinado arquivo usando a chave pública RSA correspondente.

## 🛠️ Tecnologias Utilizadas

* **Java 17:** Versão da linguagem utilizada.
* **JavaFX:** Biblioteca para construção da interface gráfica (GUI).
* **Maven:** Gerenciador de dependências e build do projeto.
* **Bouncy Castle:** Provedor de criptografia avançada para Java, usado para as operações com RSA e chaves PEM.

## ⚙️ Detalhes Técnicos

### Módulo AES
* **Algoritmo:** `AES`
* **Modos de Operação:** Suporta `CBC` (requer IV) e `ECB`.
* **Padding:** Utiliza `PKCS5Padding`.
* **Validação:** A interface valida o tamanho da chave (16, 24 ou 32 bytes) e do Vetor de Inicialização (IV) (16 bytes) em tempo real, aceitando entradas em formato Hexadecimal ou UTF-8.

### Módulo RSA
* **Geração de Chaves:** Permite gerar pares de chaves RSA de 1024, 2048 (ou outros) bits e salvá-los nos formatos `-----BEGIN PUBLIC KEY-----` e `-----BEGIN PRIVATE KEY-----` (PEM).
* **Assinatura Digital:** Utiliza o algoritmo `SHA256withRSA` para criar e verificar assinaturas.
* **Leitura de Chaves:** O sistema carrega as chaves pública e privada a partir dos arquivos `.pem` selecionados pelo usuário para realizar as operações.

## 🚀 Como Executar

### Pré-requisitos

* Java JDK 17 ou superior
* Apache Maven

### Passos

1.  **Clone o repositório:**
    ```sh
    git clone [URL-DO-SEU-REPOSITÓRIO]
    cd [NOME-DO-SEU-REPOSITÓRIO]
    ```

2.  **Compile e execute com Maven:**
    O projeto está configurado com o `javafx-maven-plugin`. Para executar, rode o comando:
    ```sh
    mvn clean javafx:run
    ```

3.  **(Alternativo) Gere o JAR executável:**
    Você pode gerar um "fat-jar" que inclui todas as dependências usando o `maven-shade-plugin`.
    ```sh
    # 1. Empacote o projeto
    mvn clean package
    
    # 2. O JAR estará na pasta 'target/' (ex: demo-0.1.jar)
    # 3. Execute o JAR
    java -jar target/demo-0.1.jar 
    ```
    *(Nota: o nome do JAR pode variar com base no `<artifactId>` e `<version>` no `pom.xml`)*
