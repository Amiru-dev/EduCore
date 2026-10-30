# SmartCity360 – Smart City Management Platform

> **UrbanTech Solutions** | Object-Oriented Design Patterns Assessment  
> Technology Stack: **Java EE 8 / Java 11 / Maven / JUnit 5**

---

## 📌 Executive Summary

**SmartCity360** is an enterprise-grade digital solution that manages city infrastructure, devices, services, and resident interactions. This assessment implements and analyzes five core design patterns across five essential municipal sub-systems:

| Part | Municipal Sub-System | Design Pattern | Pattern Category | Status |
| :--- | :--- | :--- | :--- | :---: |
| **A** | Dynamic Security Wrappers for Data Streams | **Decorator** | Structural | ✅ Verified |
| **B** | Hierarchical Structure of Smart Zones | **Composite** | Structural | ✅ Verified |
| **C** | Escalation of Citizen Complaints | **Chain of Responsibility** | Behavioural | ✅ Verified |
| **D** | Efficient Management of Smart Street Lights | **Flyweight** | Structural | ✅ Verified |
| **E** | Interpreting City Policies Defined in DSL | **Interpreter** | Behavioural | ✅ Verified |

---

## 🏗️ Design Pattern Breakdown

### Part A: Decorator Pattern (Data Stream Security)
- **Role:** Dynamically adds security capabilities (encryption, authentication, compression) to raw energy-meter data streams at runtime without altering core stream logic.
- **Key Classes:**
  - `DataStream`: Component interface declaring `readData()`, `writeData()`, `getDescription()`.
  - `EnergyMeterStream`: Concrete Component returning raw telemetry data.
  - `DataStreamDecorator`: Base abstract decorator delegating to wrapped `DataStream`.
  - `AuthenticationDecorator`: Verifies security tokens before permitting I/O operations.
  - `EncryptionDecorator`: Enforces AES-256 encryption/decryption on the stream.
  - `CompressionDecorator`: Compresses payloads with GZIP ratio reduction.

### Part B: Composite Pattern (Smart Zone Hierarchy)
- **Role:** Models the recursive tree hierarchy of city zones (`City` → `District` → `Street` → `Building`), treating leaves and composites uniformly.
- **Key Classes:**
  - `ZoneComponent`: Common interface for commands like `shutdownPower()`, `restorePower()`, and `reportUsage()`.
  - `Building`: Leaf node executing direct commands and measuring local energy consumption.
  - `CompositeZone`: Composite node containing child `ZoneComponent` elements and propagating commands recursively.
  - `CityHierarchyFactory`: Assembles a multi-tiered urban hierarchy for Colombo.

### Part C: Chain of Responsibility (Complaint Escalation)
- **Role:** Decouples complaint submission from handling by chaining handlers of varying authority (`Local Staff` → `Supervisor` → `City Manager`).
- **Key Classes:**
  - `Complaint`: Encapsulates citizen details, category, severity (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`), and audit trail.
  - `ComplaintHandler`: Abstract handler maintaining `nextHandler` link and dispatch logic.
  - `LocalStaffHandler`: Resolves minor `LOW` severity complaints.
  - `SupervisorHandler`: Resolves moderate `MEDIUM` severity issues.
  - `CityManagerHandler`: Manages `HIGH` and `CRITICAL` issues (emergency deployment).

### Part D: Flyweight Pattern (Smart Streetlight Scaling)
- **Role:** Eliminates duplicate memory allocations across tens of thousands of streetlight instances by segregating intrinsic (shared) and extrinsic (unique) state.
- **Key Classes:**
  - `StreetLightType`: Flyweight containing immutable shared properties (model, wattage, color temp, manufacturer, schedule).
  - `StreetLightFactory`: Flyweight Factory maintaining a cache pool ensuring each configuration is instantiated only once.
  - `StreetLightContext`: Lightweight context object holding unique instance data (GPS location, brightness, power state, hours operated).
  - **Memory Efficiency:** Achieves **80.0% memory reduction** when scaling from 10 to 50,000+ streetlights.

### Part E: Interpreter Pattern (City Policy DSL)
- **Role:** Parses and evaluates municipal domain-specific rules (e.g. noise limits, parking fees, speed enforcement) into an Abstract Syntax Tree (AST).
- **Key Classes:**
  - `Expression`: Abstract expression interface with `interpret(CityContext)`.
  - `ComparisonExpression`: Terminal expression evaluating comparisons (`=`, `!=`, `>`, `<`, `>=`, `<=`).
  - `AndExpression` / `OrExpression` / `NotExpression`: Non-terminal expressions composing logical evaluations.
  - `CityContext`: Variable registry holding real-time municipal state.
  - `PolicyRule`: Combines condition expression with automated action trigger.
  - `PolicyInterpreter`: Lexer and parser constructing expression trees from DSL strings (`IF ... THEN ...`).

---

## 🚀 How to Run the Project

### Prerequisites
- **Java JDK 11** or higher
- **Apache Maven 3.6+**

### 1. Run Automated Unit Tests (23 Test Cases)
Execute the complete test suite verifying all 5 patterns:
```bash
mvn test
```
*Output: 23 tests run, 0 failures, 0 errors, 0 skipped.*

### 2. Run the Standalone Interactive Console Demo
Run the complete end-to-end command-line showcase:
```bash
mvn compile exec:java
```

### 3. Run the Java EE Web Application
To launch the built-in development server:
```bash
mvn jetty:run
```
Open your browser and navigate to:
```
http://localhost:8080/
```
From the home dashboard, you can interact with:
- `/decorator` – Security layer toggling & live stream wrapping
- `/composite` – Interactive tree viewer & power shutdown/restore
- `/chain` – Citizen complaint submission & escalation trace
- `/flyweight` – Memory consumption benchmark & streetlight pool
- `/interpreter` – Real-time DSL rule evaluation & custom rule tester

### 4. Build Production WAR Package
Generate the deployable Java EE web archive (`target/SmartCity360.war`):
```bash
mvn clean package
```
The resulting WAR can be deployed to any compliant Java EE servlet container (Apache Tomcat 9/10, Eclipse Jetty, WildFly).

---

## 📁 Project Structure

```
SmartCity360/
├── pom.xml                                      # Maven POM with Java EE, JUnit 5, Jetty, Exec plugins
├── README.md                                    # Project documentation and run guide
├── src/
│   ├── main/
│   │   ├── java/com/urbantech/smartcity360/
│   │   │   ├── SmartCity360Demo.java            # Standalone console runner
│   │   │   ├── chain/                           # Part C: Chain of Responsibility Pattern
│   │   │   │   ├── Complaint.java
│   │   │   │   ├── ComplaintHandler.java
│   │   │   │   ├── LocalStaffHandler.java
│   │   │   │   ├── SupervisorHandler.java
│   │   │   │   └── CityManagerHandler.java
│   │   │   ├── composite/                       # Part B: Composite Pattern
│   │   │   │   ├── Building.java
│   │   │   │   ├── CompositeZone.java
│   │   │   │   ├── ZoneComponent.java
│   │   │   │   └── CityHierarchyFactory.java
│   │   │   ├── decorator/                       # Part A: Decorator Pattern
│   │   │   │   ├── AuthenticationDecorator.java
│   │   │   │   ├── CompressionDecorator.java
│   │   │   │   ├── DataStream.java
│   │   │   │   ├── DataStreamDecorator.java
│   │   │   │   ├── EncryptionDecorator.java
│   │   │   │   └── EnergyMeterStream.java
│   │   │   ├── flyweight/                       # Part D: Flyweight Pattern
│   │   │   │   ├── StreetLightContext.java
│   │   │   │   ├── StreetLightFactory.java
│   │   │   │   └── StreetLightType.java
│   │   │   ├── interpreter/                     # Part E: Interpreter Pattern
│   │   │   │   ├── AndExpression.java
│   │   │   │   ├── CityContext.java
│   │   │   │   ├── ComparisonExpression.java
│   │   │   │   ├── Expression.java
│   │   │   │   ├── NotExpression.java
│   │   │   │   ├── OrExpression.java
│   │   │   │   ├── PolicyInterpreter.java
│   │   │   │   └── PolicyRule.java
│   │   │   └── servlet/                         # Java EE Web Servlets
│   │   │       ├── ChainServlet.java
│   │   │       ├── CompositeServlet.java
│   │   │       ├── DecoratorServlet.java
│   │   │       ├── FlyweightServlet.java
│   │   │       └── InterpreterServlet.java
│   │   └── webapp/
│   │       ├── index.jsp                        # Main landing dashboard
│   │       ├── css/style.css                    # Modern UI theme with dark mode
│   │       └── WEB-INF/
│   │           ├── web.xml                      # Deployment descriptor
│   │           └── views/                       # Pattern demo views
│   │               ├── chain.jsp
│   │               ├── composite.jsp
│   │               ├── decorator.jsp
│   │               ├── flyweight.jsp
│   │               └── interpreter.jsp
│   └── test/
│       └── java/com/urbantech/smartcity360/
│           ├── chain/ChainPatternTest.java          # 5 tests
│           ├── composite/CompositePatternTest.java  # 4 tests
│           ├── decorator/DecoratorPatternTest.java  # 5 tests
│           ├── flyweight/FlyweightPatternTest.java  # 5 tests
│           └── interpreter/InterpreterPatternTest.java # 4 tests
```
