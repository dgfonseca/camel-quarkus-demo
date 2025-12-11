# Camel Quarkus Integration Demo

This project demonstrates a **Proof of Concept (PoC)** showcasing the powerful combination of **Quarkus** and **Apache Camel** for building enterprise integration solutions. The project simulates a real-world scenario where payment events from a message queue are consumed and persisted into a PostgreSQL database.

## 🎯 Project Overview

This integration service demonstrates a complete end-to-end message processing pipeline:

1. **AMQ Message Consumption**: Consumes payment events from an ActiveMQ (AMQ) message queue
2. **JSON Processing**: Unmarshals JSON messages into Java POJOs using Camel Jackson
3. **Data Transformation**: Extracts payment data and prepares it for database insertion
4. **Database Persistence**: Inserts processed payments into a PostgreSQL database using parameterized SQL queries

### Real-World Scenario

The service simulates a typical enterprise integration pattern where:
- Payment events are published to an AMQ queue by upstream systems (e.g., payment processors)
- The integration service automatically consumes these messages from the queue
- Each payment record is transformed and validated
- Processed payments are persisted to a PostgreSQL database for downstream analytics, reporting, and audit purposes

## 🚀 Key Technologies & Benefits

### Quarkus Framework

**Quarkus** is a Kubernetes-native Java framework designed for cloud-native applications with exceptional performance characteristics:

- **Fast Startup Time**: Applications start in milliseconds, making them ideal for serverless and containerized environments
- **Low Memory Footprint**: Optimized for minimal resource consumption
- **Developer Experience**: Live reload, unified configuration, and excellent tooling
- **Cloud-Native**: Built from the ground up for Kubernetes and OpenShift

### Apache Camel

**Apache Camel** is a powerful integration framework that implements Enterprise Integration Patterns (EIP):

- **300+ Components**: Pre-built connectors for virtually any system (AMQ, SQL, HTTP, databases, etc.)
- **Declarative Routing**: Define complex integration flows using a simple, readable DSL
- **Enterprise Patterns**: Built-in support for patterns like routing, transformation, error handling, and more
- **Type-Safe Data Transformation**: Seamless conversion between formats (JSON, XML, CSV, etc.)

### Native Compilation

This project supports **GraalVM Native Image** compilation, which provides:

- **Ultra-Fast Startup**: Native executables start in ~10-50ms (vs seconds for JVM)
- **Minimal Memory Usage**: Typically 10-50MB heap (vs 100-200MB+ for JVM)
- **Smaller Container Images**: Native images are self-contained and optimized
- **Better Resource Efficiency**: Ideal for high-density deployments in Kubernetes/OpenShift

#### Building Native Executable

```bash
# Build native executable (requires GraalVM)
./mvnw package -Dnative

# Or build in container (no GraalVM needed)
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

The native executable will be available at: `./target/camel-poc-sql-1.0.0-SNAPSHOT-runner`

### OpenShift Deployment

Quarkus provides excellent support for OpenShift with minimal configuration:

- **Automatic Resource Generation**: Generates Kubernetes/OpenShift manifests automatically
- **Container Image Builds**: Integrated support for S2I (Source-to-Image) builds
- **Health Checks**: Built-in health endpoints for OpenShift probes
- **ConfigMaps & Secrets**: Native support for OpenShift configuration management
- **Service Discovery**: Automatic service registration and discovery

#### OpenShift Configuration

The project includes OpenShift-specific configuration in `application.properties`:

```properties
quarkus.openshift.build-strategy=docker
quarkus.openshift.route.expose=true
quarkus.kubernetes.deploy=true
```

The service automatically:
- Creates DeploymentConfig, Service, and Route resources
- Configures health checks and probes
- Sets up ConfigMaps and Secrets for configuration
- Manages database connection pooling and resource limits

## 📐 Architecture

### Integration Flow

```
┌─────────────┐
│  AMQ Queue  │
│  (ActiveMQ) │
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│  AMQProducerRoute  │  ← Consumes messages from AMQ
│  (Consumer)         │     Handles connection & retries
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ TransformationRoute │  ← Unmarshals JSON → POJO
│                     │     Extracts payment fields
│                     │     Sets SQL parameters
└──────┬──────────────┘
       │
       ▼
┌──────────────────────┐
│  SqlProducerRoute    │  ← Executes parameterized SQL
│                      │     Inserts into PostgreSQL
└──────┬───────────────┘
       │
       ▼
┌─────────────┐
│ PostgreSQL  │
│  Database   │
└─────────────┘
```

### Key Components

#### 1. **AMQProducerRoute**
- Consumes messages from ActiveMQ queue using AMQP protocol
- Configurable queue names and connection settings
- Automatic reconnection and retry logic
- Exception handling with configurable retry attempts

#### 2. **TransformationRoute**
- **JSON Unmarshalling**: Uses Camel Jackson to convert JSON messages to `Pago` POJOs
- **Data Extraction**: Extracts payment fields from POJO
- **Header Mapping**: Sets Camel headers for SQL parameter binding (`nit`, `valor`, `fecha_recaudo`, `codigo_impuesto`)
- **Type Safety**: Ensures data integrity through POJO validation

#### 3. **SqlProducerRoute**
- Executes parameterized SQL INSERT statements
- Uses Quarkus Agroal connection pooling for optimal database performance
- Configurable SQL queries via application properties
- Transaction management and error handling

#### 4. **Pago Model**
- POJO representing payment records
- Fields: `idPago`, `nit`, `valor`, `fechaRecaudo`, `codigoImpuesto`
- Used for JSON deserialization and data validation

## 🛠️ Camel Components Used

This project demonstrates the ease of integration using Camel components:

- **camel-quarkus-amqp**: ActiveMQ message queue consumption with AMQP protocol
- **camel-quarkus-jackson**: JSON serialization/deserialization
- **camel-quarkus-sql**: SQL database operations with parameterized queries
- **camel-quarkus-direct**: Internal route communication
- **camel-quarkus-bean**: Java bean method invocation
- **camel-quarkus-microprofile-health**: Health check endpoints
- **quarkus-jdbc-postgresql**: PostgreSQL JDBC driver
- **quarkus-agroal**: Database connection pooling

### Example: Adding a New Integration

Adding a new integration endpoint is straightforward with Camel:

```java
from("amqp:queue:payments")
    .unmarshal(jsonFormat)
    .to("sql:INSERT INTO table VALUES (:#param1, :#param2)?dataSource=#datasource");
```

This declarative approach eliminates boilerplate code and makes integrations easy to understand and maintain.

## ⚙️ Configuration

Configuration is managed through `application.properties` with support for environment variables:

### AMQ Configuration
```properties
amq.consumer.get-user=${AMQ_PRODUCER_USER:admin}
amq.consumer.get-passwd=${AMQ_PRODUCER_PASSWD:redhat}
amq.consumer.get-queue-name=${AMQ_PRODUCER_QUEUE_NAME:test.queue}
amq.consumer.get-host-name=${AMQ_PRODUCER_HOST_NAME:localhost}
amq.consumer.get-port=${AMQ_PRODUCER_PORT:61616}
```

### PostgreSQL Configuration
```properties
quarkus.datasource.datasourceProducer.db-kind=${SQL_PRODUCER_DBKIND:postgresql}
quarkus.datasource.datasourceProducer.jdbc.url=${SQL_PRODUCER_URL:jdbc:postgresql://postgresql:5432/sampledb}
quarkus.datasource.datasourceProducer.username=${SQL_PRODUCER_USER:admin}
quarkus.datasource.datasourceProducer.password=${SQL_PRODUCER_PASSWORD:redhat}
quarkus.datasource.datasourceProducer.jdbc.min-size=${SQL_PRODUCER_MINSIZE:5}
quarkus.datasource.datasourceProducer.jdbc.max-size=${SQL_PRODUCER_MAXSIZE:20}
```

### SQL Query Configuration
```properties
sql.producer.get-query=${SQL_PRODUCER_QUERY:INSERT INTO pagos (nit, valor, fecha_recaudo, codigo_impuesto) VALUES (:#nit, :#valor, :#fecha_recaudo, :#codigo_impuesto);}
```

### Native Image Configuration
```properties
quarkus.native.additional-build-args=-H:DynamicProxyConfigurationFiles=dynamic-proxy.json
```

## 🏃 Running the Application

### Prerequisites

- **ActiveMQ**: Running and accessible AMQ broker
- **PostgreSQL**: Database server with the `pagos` table created

#### Database Schema Example

```sql
CREATE TABLE pagos (
    id SERIAL PRIMARY KEY,
    nit VARCHAR(50) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    fecha_recaudo VARCHAR(50) NOT NULL,
    codigo_impuesto VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Development Mode

Run the application in dev mode with live reload:

```bash
./mvnw quarkus:dev
```

The application will be available and will automatically reload on code changes. Dev UI is available at: <http://localhost:8080/q/dev/>

### Production Mode

#### JVM Mode
```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

#### Native Mode
```bash
./mvnw package -Dnative
./target/camel-poc-sql-1.0.0-SNAPSHOT-runner
```

### Docker/Container

Build and run with Docker:

```bash
# Build container image
docker build -f src/main/docker/Dockerfile.native -t camel-poc-sql:latest .

# Run container
docker run -i --rm -p 8080:8080 \
  -e AMQ_PRODUCER_HOST_NAME=amq-host \
  -e SQL_PRODUCER_URL=jdbc:postgresql://postgres:5432/sampledb \
  camel-poc-sql:latest
```

## 📦 Project Structure

```
src/main/java/com/minhacienda/esb/
├── configuration/          # CDI configuration beans
│   ├── AmqConfigurationProducer.java
│   └── ReflectionConfiguration.java
├── model/                  # Data models (Pago POJO)
│   └── Pago.java
├── properties/             # Configuration properties classes
│   ├── AmqProducerConfig.java
│   └── SqlProducerConfig.java
├── routes/                 # Camel route definitions
│   ├── AMQProducerRoute.java
│   ├── TransformationRoute.java
│   └── SqlProducerRoute.java
└── transformation/         # Business logic components
    └── TransformationComponent.java
```

## 🔍 Key Features Demonstrated

1. **Enterprise Integration Patterns**: Message consumption, content-based routing, data transformation
2. **Type-Safe Data Binding**: JSON to POJO mapping with Jackson annotations
3. **Database Integration**: Parameterized SQL queries with connection pooling
4. **Error Handling**: Retry logic, exception handling, logging
5. **Configuration Management**: Environment-based configuration with defaults
6. **Cloud-Native Deployment**: OpenShift-ready with health checks and probes
7. **Native Compilation**: GraalVM native image support for optimal performance
8. **Connection Pooling**: Efficient database connection management with Agroal

## 📚 Related Guides

- [Quarkus Documentation](https://quarkus.io/)
- [Camel Quarkus Extensions](https://camel.apache.org/camel-quarkus/latest/reference/index.html)
- [Camel Core](https://camel.apache.org/camel-quarkus/latest/reference/extensions/core.html)
- [Camel AMQP](https://camel.apache.org/camel-quarkus/latest/reference/extensions/amqp.html)
- [Camel SQL](https://camel.apache.org/camel-quarkus/latest/reference/extensions/sql.html)
- [Camel Jackson](https://camel.apache.org/components/4.0.x/dataformats/jackson-dataformat.html)
- [Quarkus Native Image Guide](https://quarkus.io/guides/building-native-image)
- [Quarkus Datasource Guide](https://quarkus.io/guides/datasource)

## 🎓 Learning Outcomes

This PoC demonstrates:

- How to build integration services with Quarkus and Camel
- The simplicity of defining integration routes with Camel DSL
- Message queue consumption patterns with ActiveMQ
- Database integration with parameterized SQL queries
- Native compilation benefits for cloud-native deployments
- OpenShift deployment patterns and best practices
- Configuration management in containerized environments
- Error handling and resilience patterns in integration services
- Connection pooling and database performance optimization

---

**Built with ❤️ using Quarkus and Apache Camel**
