#include <deprecated.h>
#include <MFRC522.h>
#include <MFRC522Debug.h>
#include <MFRC522Extended.h>
#include <MFRC522Hack.h>
#include <require_cpp11.h>

#include <SPI.h>


#define SS_PIN 10
#define RST_PIN 9

MFRC522 mfrc522(SS_PIN, RST_PIN);

void setup()
{
  SPI.begin();
  mfrc522.PCD_Init();
  Serial.begin(9600);
}
  
void loop() {
  if ( ! mfrc522.PICC_IsNewCardPresent()) {
    return;
  }
  if ( ! mfrc522.PICC_ReadCardSerial()) {
    return;
  }
  int a = 0;
  int b;
  for (byte i = 0; i < mfrc522.uid.size; i++)
  {
    b = mfrc522.uid.uidByte[i];
    a = a * 256 + b;
  }
  Serial.println(a);
}


