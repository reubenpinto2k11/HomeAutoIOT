#include "FirebaseESP8266.h"
#include<ESP8266WiFi.h>

// Set these to your WIFI settings
#define WIFI_SSID "rpnet"
#define WIFI_PASSWORD "reubster92"

//Pin setup
#define IS_AUTO_PIN D0
#define IS_FAN_ON_PIN D3
#define IS_LIGHT_ON_PIN D5

//Set these to the Firebase project settings
#define FIREBASE_URL "nodemcutest-b1398.firebaseio.com"
#define FIREBASE_DB_SECRET "CpezrDBOPgiBczOfvSanMRGuxFAF6KByLkj5je01"

FirebaseData firebaseData;

void setup() {
  pinMode(IS_AUTO_PIN, OUTPUT);
  pinMode(IS_FAN_ON_PIN, OUTPUT);
  pinMode(IS_LIGHT_ON_PIN, OUTPUT);
    
  Serial.begin(115200);
  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());

  //begin Firebase
  Firebase.begin(FIREBASE_URL, FIREBASE_DB_SECRET);
  Firebase.reconnectWiFi(true);
  Firebase.setMaxRetry(firebaseData, 3);
  Firebase.setMaxErrorQueue(firebaseData, 30);
  Firebase.setReadTimeout(firebaseData, 1000 * 60);
  Firebase.setwriteSizeLimit(firebaseData, "tiny");
}

void loop() {
    //get the event
    if (Firebase.getBool(firebaseData, "/isAuto")) {
      if (firebaseData.dataType() == "boolean") {
        Serial.println("isAuto");
        Serial.println(firebaseData.boolData() == 1 ? "true" : "false");
        digitalWrite(IS_AUTO_PIN, (firebaseData.boolData() == 1 ? HIGH : LOW));
        if (firebaseData.boolData() != 1) {
          if (Firebase.getBool(firebaseData, "/isFanOn")) {
            if (firebaseData.dataType() == "boolean") {
              Serial.println("isFanOn");
              Serial.println(firebaseData.boolData() == 1 ? "true" : "false");
              digitalWrite(IS_FAN_ON_PIN, (firebaseData.boolData() == 1 ? HIGH : LOW));
            }
          }

          if (Firebase.getBool(firebaseData, "/isLightOn")) {
            if (firebaseData.dataType() == "boolean") {
              Serial.println("isLightOn");
              Serial.println(firebaseData.boolData() == 1 ? "true" : "false");
              digitalWrite(IS_LIGHT_ON_PIN, (firebaseData.boolData() == 1 ? HIGH : LOW));
            }            
          }
        }
      }
    }
    delay(1000);
}
