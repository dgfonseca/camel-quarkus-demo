# Camel Quarkus Integration Demo

This project demonstrates a **Proof of Concept (PoC)** showcasing the powerful combination of **Quarkus** and **Apache Camel** for building enterprise integration solutions. The project simulates a real-world scenario where payment files from an external system are processed and integrated into an enterprise messaging infrastructure.

## 🎯 Project Overview

This integration service demonstrates a complete end-to-end payment processing pipeline:

1. **SFTP File Consumption**: Monitors an SFTP server for incoming CSV payment files
2. **CSV Processing**: Unmarshals CSV files into Java POJOs using Camel Bindy
3. **Data Transformation**: Applies business logic based on payment types
4. **Message Queue Integration**: Publishes processed payments to an AMQ (ActiveMQ) queue

### Real-World Scenario

The service simulates a typical enterprise integration pattern where:
- An external system deposits payment files in CSV format on an SFTP server
- The integration service automatically detects and processes these files
- Each payment record is transformed and validated
- Processed payments are sent to a message queue for downstream processing by other systems

## 🚀 Key Technologies & Benefits

### Quarkus Framework

**Quarkus** is a Kubernetes-native Java framework designed for cloud-native applications with exceptional performance characteristics:

- **Fast Startup Time**: Applications start in milliseconds, making them ideal for serverless and containerized environments
- **Low Memory Footprint**: Optimized for minimal resource consumption
- **Developer Experience**: Live reload, unified configuration, and excellent tooling
- **Cloud-Native**: Built from the ground up for Kubernetes and OpenShift

### Apache Camel

**Apache Camel** is a powerful integration framework that implements Enterprise Integration Patterns (EIP):

- **300+ Components**: Pre-built connectors for virtually any system (SFTP, JMS, HTTP, databases, etc.)
- **Declarative Routing**: Define complex integration flows using a simple, readable DSL
- **Enterprise Patterns**: Built-in support for patterns like routing, transformation, error handling, and more
- **Type-Safe Data Transformation**: Seamless conversion between formats (CSV, JSON, XML, etc.)

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

The native executable will be available at: `./target/camel-poc-1.0.0-SNAPSHOT-runner`

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
- Mounts volumes for certificates and keys

## 📐 Architecture

### Integration Flow

```
┌─────────────┐
│  SFTP Server│
│  (CSV Files)│
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│  SFTPConsumerRoute  │  ← Monitors SFTP, downloads files
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ TransformationRoute │  ← Unmarshals CSV → POJO
│                     │     Applies business logic
│                     │     Marshals to JSON
└──────┬──────────────┘
       │
       ▼
┌──────────────────────┐
│  AMQProducerRoute    │  ← Publishes to AMQ queue
└──────┬───────────────┘
       │
       ▼
┌─────────────┐
│  AMQ Queue  │
│  (ActiveMQ) │
└─────────────┘
```

### Key Components

#### 1. **SFTPConsumerRoute**
- Monitors SFTP server for new CSV files
- Configurable polling interval and connection settings
- Automatic file deletion after processing
- Error handling and reconnection logic

#### 2. **TransformationRoute**
- **CSV Unmarshalling**: Uses Camel Bindy to convert CSV rows to `Pago` POJOs
- **Content-Based Routing**: Routes payments based on `codigoImpuesto` field
- **Data Transformation**: Applies business logic via bean components
- **JSON Marshalling**: Converts POJOs to JSON for message queue

#### 3. **AMQProducerRoute**
- Publishes messages to ActiveMQ queue
- Exception handling with retry logic
- Configurable queue names and connection settings

#### 4. **Pago Model**
- POJO representing payment records
- Annotated with Bindy CSV annotations for automatic mapping
- Fields: `idPago`, `nit`, `valor`, `fechaRecaudo`, `codigoImpuesto`

## 🛠️ Camel Components Used

This project demonstrates the ease of integration using Camel components:

- **camel-quarkus-ftp**: SFTP file consumption with authentication
- **camel-quarkus-bindy**: CSV to POJO mapping with annotations
- **camel-quarkus-jackson**: JSON serialization/deserialization
- **camel-quarkus-amqp**: ActiveMQ message queue integration
- **camel-quarkus-direct**: Internal route communication
- **camel-quarkus-bean**: Java bean method invocation
- **camel-quarkus-microprofile-health**: Health check endpoints

### Example: Adding a New Integration

Adding a new integration endpoint is straightforward with Camel:

```java
from("sftp://host/path?username=user&privateKeyFile=key")
    .unmarshal(csvFormat)
    .to("amqp:queue:name");
```

This declarative approach eliminates boilerplate code and makes integrations easy to understand and maintain.

## ⚙️ Configuration

Configuration is managed through `application.properties` with support for environment variables:

### SFTP Configuration
```properties
sftp.consumer.get-host-name=${SFTP_CONSUMER_HOST:localhost}
sftp.consumer.get-port=${SFTP_CONSUMER_PORT:22}
sftp.consumer.get-auth-user=${SFTP_PRODUCER_USER}
sftp.consumer.get-path-download=${SFTP_CONSUMER_PATH_DOWNLOAD:/tmp/}
```

### AMQ Configuration
```properties
amq.producer.get-host-name=${AMQ_PRODUCER_HOST_NAME:localhost}
amq.producer.get-port=${AMQ_PRODUCER_PORT:61616}
amq.producer.get-queue-name=${AMQ_PRODUCER_QUEUE_NAME:test.queue}
```

### Native Image Configuration
```properties
quarkus.native.additional-build-args=-H:DynamicProxyConfigurationFiles=dynamic-proxy.json
```

## 🏃 Running the Application

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
./target/camel-poc-1.0.0-SNAPSHOT-runner
```

### Docker/Container

Build and run with Docker:

```bash
# Build container image
docker build -f src/main/docker/Dockerfile.native -t camel-poc:latest .

# Run container
docker run -i --rm -p 8080:8080 camel-poc:latest
```

## 📦 Project Structure

```
src/main/java/com/minhacienda/esb/
├── configuration/          # CDI configuration beans
├── model/                  # Data models (Pago POJO)
├── properties/             # Configuration properties classes
├── routes/                 # Camel route definitions
│   ├── SFTPConsumerRoute.java
│   ├── TransformationRoute.java
│   └── AMQProducerRoute.java
└── transformation/         # Business logic components
```

## 🔍 Key Features Demonstrated

1. **Enterprise Integration Patterns**: File polling, content-based routing, message transformation
2. **Type-Safe Data Binding**: CSV to POJO mapping with Bindy annotations
3. **Error Handling**: Retry logic, exception handling, logging
4. **Configuration Management**: Environment-based configuration with defaults
5. **Cloud-Native Deployment**: OpenShift-ready with health checks and probes
6. **Native Compilation**: GraalVM native image support for optimal performance

## 📚 Related Guides

- [Quarkus Documentation](https://quarkus.io/)
- [Camel Quarkus Extensions](https://camel.apache.org/camel-quarkus/latest/reference/index.html)
- [Camel Core](https://camel.apache.org/camel-quarkus/latest/reference/extensions/core.html)
- [Camel FTP/SFTP](https://camel.apache.org/camel-quarkus/latest/reference/extensions/ftp.html)
- [Camel AMQP](https://camel.apache.org/camel-quarkus/latest/reference/extensions/amqp.html)
- [Camel Bindy](https://camel.apache.org/components/4.0.x/dataformats/bindy-dataformat.html)
- [Quarkus Native Image Guide](https://quarkus.io/guides/building-native-image)

## 🎓 Learning Outcomes

This PoC demonstrates:

- How to build integration services with Quarkus and Camel
- The simplicity of defining integration routes with Camel DSL
- Native compilation benefits for cloud-native deployments
- OpenShift deployment patterns and best practices
- Configuration management in containerized environments
- Error handling and resilience patterns in integration services

---

**Built with ❤️ using Quarkus and Apache Camel**
