markdown_content = """# D&D 5e Bestiary ETL Pipeline

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)

A lightweight, local Data Engineering pipeline built to demonstrate core **ETL (Extract, Transform, Load)** principles. This project ingests raw monster data from the public D&D 5e API, normalizes the chaotic JSON structures, and loads the cleaned data into a local relational database.

## Project Goals

This repository serves as a practical introduction to data engineering workflows, bridging the gap between standard software development and data infrastructure.

The primary objectives are:
1. **Master the ETL Pattern:** Build a robust, sequential pipeline that extracts data from a remote source, transforms it in-memory, and loads it into persistent storage.
2. **Handle Messy Data:** Parse deeply nested, inconsistent JSON payloads from external APIs and flatten them into clean, structured Object-Oriented models.
3. **Pipeline Resilience:** Write comprehensive unit tests to ensure the transformation logic doesn't break when encountering edge cases (e.g., missing fields like a monster lacking a "swim speed").
4. **Local Database Integration:** Learn to interact with a lightweight relational database (SQLite) using JDBC without needing to provision cloud infrastructure.

## Architecture

The pipeline is split into three distinct stages executed sequentially:

### 1. Extract (E)
- Connects to the [D&D 5e API](https://www.dnd5eapi.co/).
- Fetches the master index of monsters.
- Iterates through the index to download the raw JSON payload for individual creatures.

### 2. Transform (T)
- Utilizes JSON parsing libraries (e.g., Jackson or Gson) to map raw strings to Java objects.
- **Flattening:** Extracts nested attributes (like reducing a complex `Speed` object into simple integer variables like `walkSpeed` and `flySpeed`).
- **Cleaning:** Standardizes data types, handles null values gracefully, and drops unnecessary or redundant fields.

### 3. Load (L)
- Connects to a local `bestiary.db` SQLite database using a JDBC driver.
- Automatically creates the necessary SQL schemas if they do not exist.
- Executes batch `INSERT` statements to populate the database with the transformed monster models.

## Tech Stack

* **Language:** Java
* **Build Tool:** Maven
* **Database:** SQLite
* **Testing:** JUnit 5
* **Libraries:** Gson / Jackson (JSON parsing), SQLite JDBC Driver

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 11 or higher installed.
* Maven installed and configured in your environment.
* A standard terminal environment (PowerShell, bash, or Pop!_OS terminal).

### Installation & Execution

## 1. **Clone the repository**
Clone the repo:
```bash
   git clone [https://github.com/yourusername/dnd-bestiary-etl.git](https://github.com/yourusername/dnd-bestiary-etl.git)
   cd dnd-bestiary-etl
## 2. Build the project and run tests

Compile the Java source files, resolve Maven dependencies, and execute the JUnit test suite to ensure the transformation logic is sound.

```bash
mvn clean install
```

## 3. Run the Pipeline

Execute the main application class to trigger the ETL process.

```bash
mvn exec:java -Dexec.mainClass="za.co.wethinkcode.etl.ETLPipeline"
```

## 4. Verify the Data

Once the pipeline finishes, a `bestiary.db` file will appear in your project root. You can open this using any standard SQLite viewer or via the command line:

```bash
sqlite3 bestiary.db
sqlite> SELECT name, type, armor_speed FROM monsters LIMIT 5;
```

## Testing Strategy

Data pipelines are only as good as their reliability. The `src/test/java` directory contains JUnit tests that simulate bad API responses. The tests guarantee that:

- Missing keys in the JSON do not throw exceptions.
- Numerical fields containing unexpected string data are safely caught.
- The Object-Oriented models map perfectly to the expected SQL schema.

## Future Expansions

Once the core local pipeline is functional, potential next steps include:

- **Scheduling:** Automating the execution to sync the database weekly.
- **Logging:** Replacing standard output with a proper logging framework (like Logback/SLF4J) to track pipeline health.
- **Cloud Migration:** Replacing SQLite with a cloud data warehouse like Google BigQuery or AWS RDS.
