#include <SimpleDHT.h>

#define IP_FAN1 2
#define IP_LIGHT1 3
#define IP_LIGHT2 4

#define OP_FAN1 35
#define OP_LIGHT1 39
#define OP_LIGHT2 43

void setup() {
  Serial.begin(9600);
  pinMode(IP_FAN1, INPUT);
  pinMode(IP_LIGHT1, INPUT);
  pinMode(IP_LIGHT2, INPUT);
  
  pinMode(OP_FAN1, OUTPUT);
  pinMode(OP_LIGHT1, OUTPUT);
  pinMode(OP_LIGHT2, OUTPUT);
}

void loop() {
  Serial.print("fan: ");Serial.println(digitalRead(IP_FAN1));
  if (digitalRead(IP_FAN1) == HIGH) {
    digitalWrite(OP_FAN1, LOW);
    Serial.println("fanOP: LOW");
  }
  else {
    digitalWrite(OP_FAN1, HIGH);
    Serial.println("fanOP: Low");
  }

  Serial.print("light1: ");Serial.println(digitalRead(IP_LIGHT1));
  if (digitalRead(IP_LIGHT1) == HIGH) {
    digitalWrite(OP_LIGHT1, LOW);    
  }
  else {
    digitalWrite(OP_LIGHT1, HIGH);    
  }

  Serial.print("light2: ");Serial.println(digitalRead(IP_LIGHT2));
  if (digitalRead(IP_LIGHT2) == HIGH) {
    digitalWrite(OP_LIGHT2, LOW);    
  }
  else {
    digitalWrite(OP_LIGHT2, HIGH);    
  }
  delay(500);
}
