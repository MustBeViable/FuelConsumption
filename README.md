
## Local run without Docker for GUI

### 1. Start a MariaDB database
You can either install MariaDB locally or start only the DB container:

```bash
docker compose up -d db
```

### 2. Create a local DB config file
Copy the example file:

```bash
cp config/db.properties.example config/db.properties
```

Edit it if needed.

### 3. Run the JavaFX app

```bash
mvn clean javafx:run
```

## Full Docker Compose setup

Build and start services:

```bash
docker compose up --build
```

Services:
- `db`: custom MariaDB image built from `db/Dockerfile`
- `app`: Java application image built from the root `Dockerfile`


## How localization works now

The application no longer reads UI text from properties files at runtime.
Instead:
1. the controller selects a language code such as `en_US`
2. `LocalizationService` loads all matching rows from `localization_strings`
3. labels and button text are refreshed from database values

## How saving calculations works

After a successful calculation:
1. input values are validated
2. total fuel and total cost are calculated
3. a `CalculationRecord` object is created
4. `CalculationService.saveCalculation()` inserts the record into `calculation_records`

## DB configuration

The application reads DB settings in this order:
1. environment variables: `DB_URL`, `DB_USER`, `DB_PASSWORD`
2. `config/db.properties`
3. built-in defaults

Default values:
- `jdbc:mariadb://localhost:3306/fuel_calculator_localization`
- user: `fuel_user`
- password: `fuel_password`

## Example queries for screenshots / verification

### Check saved calculation rows

```sql
USE fuel_calculator_localization;
SELECT id, distance, consumption, price, total_fuel, total_cost, language, created_at
FROM calculation_records;
```

### Check localization rows

```sql
USE fuel_calculator_localization;
SELECT language, `key`, value
FROM localization_strings
ORDER BY language, `key`;
```