#include <SimpleDHT.h>

#define IP_AUTO_PIN 2
#define IP_LIGHT_ON_PIN 5
#define IP_FAN_ON_PIN 7

#define IP_LDR_PIN A0
#define IP_DHT_PIN 9
#define IP_MD_PIN 39

#define OP_FAN_ON 49
#define OP_LIGHT_ON 51

SimpleDHT11 dht11(IP_DHT_PIN);

volatile byte state_auto = LOW;
volatile byte state_fan = LOW;
volatile byte state_light = LOW;

int lastMotionCounter = 0;
int NO_MOTION_THRESHOLD = 40;

int LDRReading;
int LDRThreshold = 50;

void setup() {
  Serial.begin(9600);
  pinMode(IP_AUTO_PIN, INPUT);
  pinMode(IP_LIGHT_ON_PIN, INPUT);
  pinMode(IP_FAN_ON_PIN, INPUT);
  
  pinMode(IP_DHT_PIN, INPUT);
  pinMode(IP_MD_PIN, INPUT);

  pinMode(OP_FAN_ON, OUTPUT);
  pinMode(OP_LIGHT_ON, OUTPUT);
}

void loop() {
  int isAuto = digitalRead(IP_AUTO_PIN);
  if (isAuto == HIGH) {
    //Automatic state
    Serial.println("State auto");
    
    //if is switching from manual to auto reset everything
    if (state_auto == LOW) {
      state_auto = HIGH;
      state_fan = LOW;
      state_light = LOW;
      digitalWrite(OP_FAN_ON, LOW);
      digitalWrite(OP_LIGHT_ON, LOW);
      lastMotionCounter = NO_MOTION_THRESHOLD;
    }

    int hasMotionBeenDetected = digitalRead(IP_MD_PIN);
    Serial.println("Motion detection"); Serial.println(hasMotionBeenDetected);
    // if motion is present reset counter else increment count
    if (hasMotionBeenDetected == HIGH) {
      lastMotionCounter = 0;
    }
    else {
      lastMotionCounter += 1;
    }

    //check count value against threshold (5mins), switch off if no motion for 5 mins
    if (lastMotionCounter < NO_MOTION_THRESHOLD) {
      //check temprature if beyond above threshold and start fan
      byte temperature = 0;
      byte humidity = 0;
      int err = SimpleDHTErrSuccess;
      if ((err = dht11.read(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
        Serial.println("Read DHT11 failed, err="); Serial.println(err);        
      }
      else {
        if (((int)temperature > 25) && (state_fan == LOW)) {
         digitalWrite(OP_FAN_ON, HIGH);
         state_fan = HIGH; 
        }
      }

      //check light intensity if below intensity turn on light
      LDRReading = analogRead(IP_LDR_PIN);
      Serial.println("LDR reading"); Serial.println(LDRReading);
      if ((LDRReading > LDRThreshold) && (state_light == LOW)) {
        digitalWrite(OP_LIGHT_ON, HIGH);
        state_light = HIGH;
      }
    }
    else {
      if (state_fan == HIGH) {
        digitalWrite(OP_FAN_ON, LOW);
        state_fan = LOW;
      }
      if (state_light == HIGH) {
        digitalWrite(OP_LIGHT_ON, LOW);
        state_light = LOW;
      }
    }
  }
  else {
    // Manual state
    Serial.println("State manual");
    if (state_auto == HIGH) {
      state_auto = LOW;
      state_fan = LOW;
      state_light = LOW;
      digitalWrite(OP_FAN_ON, LOW);
      digitalWrite(OP_LIGHT_ON, LOW);
    }

    //check fan inputs
    int isFanOn = digitalRead(IP_FAN_ON_PIN);
    Serial.println("isFanON"); Serial.println(isFanOn);
    if (isFanOn == HIGH) {
      if (state_fan == LOW) {
        digitalWrite(OP_FAN_ON, HIGH);
        state_fan = HIGH;
      }
    }
    else {
      if (state_fan == HIGH) {
        digitalWrite(OP_FAN_ON, LOW);
        state_fan = LOW;              
      }
    }

    //check light inputs
    int isLightOn = digitalRead(IP_LIGHT_ON_PIN);
    Serial.println("is light on"); Serial.println(isLightOn);
    if (isLightOn == HIGH) {
      if (state_light == LOW) {
        digitalWrite(OP_LIGHT_ON, HIGH);
        state_light = HIGH;
      }  
    }
    else {
      if (state_light == HIGH) {
        digitalWrite(OP_LIGHT_ON, LOW);
        state_light = LOW;              
      }
    }
  }
  delay(1500);
}
