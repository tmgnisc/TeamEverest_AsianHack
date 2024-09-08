#include <WiFi.h>
#include <DHT.h>
#include <HTTPClient.h> // Include HTTPClient library

// Define DHT sensor type and pin
#define DHTTYPE DHT11 // or DHT22
#define DHTPIN 15

// Define soil moisture sensor pin
#define SOIL_MOISTURE_PIN 34 // Analog pin

// Define relay pin
#define RELAY_PIN 27

// Define MQ-135 gas sensor pin
#define MQ135_PIN 35 // Analog pin

// Calibration values for MQ-135 (may vary per sensor)
float RLOAD = 10.0; // Load resistance in kilo ohms
float RZERO = 76.63; // Adjust based on clean air calibration

// Adjusted thresholds for agricultural safety
float NH3_THRESHOLD = 25.0; // Ammonia threshold in ppm
float CO2_THRESHOLD = 1500.0; // Upper safe limit for Carbon Dioxide in ppm
float BENZENE_THRESHOLD = 0.1; // Benzene threshold in ppm

// Moisture threshold for triggering relay
int moistureThreshold = 1500; // Adjust based on sensor calibration

// Create DHT object
DHT dht(DHTPIN, DHTTYPE);

const char* serverName = "http://192.168.0.104/smartagro/senddata.php"; // URL of the PHP script

void setup() {
  // Start Serial communication
  Serial.begin(115200);

  // Set ADC resolution to 12 bits (range 0-4095)
  analogReadResolution(12);

  // Initialize DHT sensor
  dht.begin();

  // Set relay pin as output
  pinMode(RELAY_PIN, OUTPUT);
  digitalWrite(RELAY_PIN, LOW); // Start with relay off

  // Initialize WiFi connection
  const char* ssid = "hack16";
  const char* password = "intercollegehack16";
  WiFi.begin(ssid, password);

  Serial.print("Connecting to WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.println("\nConnected to WiFi");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  // Read temperature and humidity
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();

  // Read soil moisture value
  int soilMoistureValue = analogRead(SOIL_MOISTURE_PIN);

  // Read and average gas concentration from MQ-135
  int mq135Value = readMQ135Value(); // Using the filtered read function

  // Convert analog value to resistance
  float resistance = ((4095.0 / mq135Value) - 1.0) * RLOAD;

  // Calculate gas concentrations based on resistance
  float ratio = resistance / RZERO; // Ratio in clean air
  float NH3 = pow(ratio, -1.8); // Estimate for Ammonia (NH3)
  float CO2 = pow(ratio, -1.3); // Estimate for Carbon Dioxide (CO2)
  float Benzene = pow(ratio, -1.5); // Estimate for Benzene (C6H6)

  // Print values to Serial
  Serial.print("Temperature: ");
  Serial.print(temperature);
  Serial.print(" °C, Humidity: ");
  Serial.print(humidity);
  Serial.print(" %, Soil Moisture: ");
  Serial.print(soilMoistureValue);
  Serial.print(", MQ-135 Resistance: ");
  Serial.print(resistance);
  Serial.print(" kOhms, NH3: ");
  Serial.print(isnan(NH3) ? "Invalid" : String(NH3));
  Serial.print(" ppm, CO2: ");
  Serial.print(isnan(CO2) ? "Invalid" : String(CO2));
  Serial.print(" ppm, Benzene: ");
  Serial.println(isnan(Benzene) ? "Invalid" : String(Benzene) + " ppm");

  // Adjust thresholds if calibration seems off
  if (resistance < 5 || resistance > 200) {
    Serial.println("Warning: Abnormal resistance values detected, recalibrate sensor in clean air!");
  }

  // Check if soil moisture is below threshold
  if (soilMoistureValue > moistureThreshold) {
    Serial.println("Soil is dry, triggering relay.");
    digitalWrite(RELAY_PIN, HIGH); // Activate relay
  } else {
    Serial.println("Soil moisture level is sufficient.");
    digitalWrite(RELAY_PIN, LOW); // Deactivate relay
  }

  // Check specific gas levels against agricultural thresholds
  if (NH3 > NH3_THRESHOLD && !isnan(NH3)) {
    Serial.println("Warning: High Ammonia levels detected, harmful to plants!");
  }
  if (CO2 > CO2_THRESHOLD && !isnan(CO2)) {
    Serial.println("Warning: High Carbon Dioxide levels detected, ensure proper ventilation!");
  }
  if (Benzene > BENZENE_THRESHOLD && !isnan(Benzene)) {
    Serial.println("Warning: Benzene detected, harmful to plants!");
  }

  // Send data to server
  sendDataToServer(temperature, humidity, soilMoistureValue, NH3, CO2, Benzene);

  // Delay before next reading
  delay(2000);
}

void sendDataToServer(float temperature, float humidity, int soilMoisture, float NH3, float CO2, float Benzene) {
  if(WiFi.status() == WL_CONNECTED) { // Check WiFi connection
    HTTPClient http;
    http.begin(serverName);

    http.addHeader("Content-Type", "application/x-www-form-urlencoded");

    String httpRequestData = "temperature=" + String(temperature) + "&";
    httpRequestData += "humidity=" + String(humidity) + "&";
    httpRequestData += "soilMoisture=" + String(soilMoisture) + "&";
    httpRequestData += "NH3=" + String(NH3) + "&";
    httpRequestData += "CO2=" + String(CO2) + "&";
    httpRequestData += "Benzene=" + String(Benzene);

    Serial.println("Sending data: " + httpRequestData); // Print the data being sent

    int httpResponseCode = http.POST(httpRequestData);

    if(httpResponseCode > 0) {
      String response = http.getString();
      Serial.println("Server Response: " + response);
    } else {
      Serial.println("Error on sending POST: " + String(httpResponseCode));
    }

    http.end();
  } else {
    Serial.println("Error in WiFi connection");
  }
}


int readMQ135Value() {
  int totalValue = 0;
  const int numReadings = 10; // Number of readings to average
  for (int i = 0; i < numReadings; i++) {
    totalValue += analogRead(MQ135_PIN);
    delay(10); // Short delay between readings
  }
  return totalValue / numReadings;
}
