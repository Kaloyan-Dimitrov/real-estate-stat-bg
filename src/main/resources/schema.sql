CREATE TABLE IF NOT EXISTS average_region_prices (
   id SERIAL PRIMARY KEY,
   price DECIMAL(10,2) NOT NULL,
   currency VARCHAR(3) NOT NULL,
   type VARCHAR(50) NOT NULL,
   region_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS regions (
   id SERIAL PRIMARY KEY,
   name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS key_interest_rates (
   id SERIAL PRIMARY KEY,
   marginal_lending_facility DECIMAL(10,2) NOT NULL,
   deposit_facility DECIMAL(10,2) NOT NULL,
   date DATE NOT NULL
);