# faturamento

Microserviço Spring Boot responsável pelo faturamento de pedidos, geração de notas fiscais e integração com Kafka e MinIO.

## Tecnologias
- Java 21
- Spring Boot 3.5.3
- Spring Kafka
- JasperReports (geração de PDF)
- MinIO (armazenamento de arquivos)
- Lombok
- Docker Compose (serviços externos)

## Estrutura
- `src/main/java`: código-fonte principal
- `src/test/java`: testes automatizados
- `src/main/resources`: configurações (ex: application.properties)

## Comunicação

### Kafka
O serviço consome eventos de pedidos pagos via Kafka para iniciar o processo de faturamento:
- **Consumo:** O tópico `${icompras.config.kafka.topics.pedidos-pagos}` é monitorado pelo subscriber `PedidoPagoSubscriber`.
- **Processo:** Ao receber o evento, o serviço converte o payload, processa o pedido e gera a nota fiscal.

#### Exemplo de consumo
```java
@KafkaListener(groupId = "${spring.kafka.consumer.group-id}",
    topics = "${icompras.config.kafka.topics.pedidos-pagos}")
public void listen(String json) {
    // Processa o evento de pedido pago
}
```

### MinIO
Após gerar a nota fiscal, o serviço armazena o arquivo PDF no bucket MinIO para consulta posterior.

### JasperReports
Utilizado para geração de notas fiscais em PDF a partir dos dados do pedido.

## Requisitos
- Java 21+
- Maven
- Docker e Docker Compose (para Kafka, MinIO, etc.)

## Como executar
1. **Suba os serviços necessários:**
   - Kafka/Zookeeper: `docker-compose -f ../broker/docker-compose.yml up -d`
   - MinIO: `docker-compose -f ../bucket/docker-compose.yml up -d`
2. **Compile e rode o projeto:**
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## Testes
Execute:
```sh
mvn test
```

## Observações
- O serviço depende dos tópicos Kafka e do bucket MinIO configurados em `application.properties`.
- Dados sensíveis e arquivos temporários estão protegidos pelo `.gitignore`.
- Ajuste as configurações de endpoints e credenciais conforme o ambiente.

