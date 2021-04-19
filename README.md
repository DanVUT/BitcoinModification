# BitcoinModification

Táto modifikácia pridáva do hry Minecraft ekosystém Bitcoinu pre výučbové účely. Hráči môžu získať prehľad o Bitcoine jednoduchou
a zábavnou formou bez nutnosti investovať skutočné peniaze.

# Návod na inštaláciu pre herného klienta

1. Stiahnúť inštalátor forge-1.15.2-31.2.0-installer.jar z priečinku modification v tomto repozitári
2. Vybrať možnosť "install client" a nasmerovať inštalátor do priečinku s hernými súbormi. Predvolená cesta je C:\Users\{username}\AppData\Roaming\\.minecraft
3. V priečinku s hernými súbormi by sa mal objaviť priečinok mods. Predvolená cesta je C:\Users\{username}\AppData\Roaming\\.minecraft\mods
4. Nakopírovať do priečinku mods bitcoin_modification-1.0.jar z priečinku modification v tomto repozitári

# Návod na inštaláciu dedikovaného servera (pre účely hry viacerých hráčov)
### !!!K SERVERU JE POTREBNÁ JAVA 8. S INÝMI VERZIAMI JAVA SA SERVER S NAJVACSOU PRAVDEPODOBNOSŤOU NESPUSTÍ!!!

1. Stiahnúť inštalátor forge-1.15.2-31.2.0-installer.jar z priečinku modification v tomto repozitári
2. Vybrať možnosť "install server" a nasmerovať inštalátor do nejakého prázdneho priečinku
3. Pre jednoduchosť premenovať v priečinku so serverom súbor "forge-1.15.2-31.2.0.jar" na "forgeserver.jar"
4. Spustiť forgeserver.jar obyčajným dvojklikom. Nič sa nestane, ale vytvoria sa v priečinku ďalšie súbory
5. Prepísať hodnotu v súbore eula.txt z eula=false na eula=true
6. Do priečinku mods v adresári so serverom skopírovať bitcoin_modification-1.0.jar z priečinku modification v tomto repozitári (Ak sa takýto priečinok sám nevytvoril, tak ho treba vytvoriť)
7. Vytvoriť run.bat v adresári so serverom s príkazom "java -Xmx2048M -Xms2048M -jar forgeserver.jar"
8. Spustiť run.bat z konzolového rozhrania a server by sa mal zapnúť