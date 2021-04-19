# BitcoinModification

Táto modifikácia pridáva do hry Minecraft ekosystém kryptomeny Bitcoinu pre výučbové účely. Hráči môžu získať prehľad o Bitcoine jednoduchou a zábavnou formou bez nutnosti investovať skutočné peniaze.

# Návod na inštaláciu pre herného klienta

1. Stiahnúť inštalátor forge-1.15.2-31.2.0-installer.jar z priečinku modification v tomto repozitári
2. Vybrať možnosť "install client" a nasmerovať inštalátor do priečinku s hernými súbormi. Predvolená cesta je C:\Users\\{username}\AppData\Roaming\\.minecraft
3. V priečinku s hernými súbormi by sa mal objaviť priečinok mods. Predvolená cesta je C:\Users\\{username}\AppData\Roaming\\.minecraft\mods
4. Nakopírovať do priečinku mods bitcoin_modification-1.0.jar z priečinku modification v tomto repozitári

# Návod na inštaláciu dedikovaného servera (pre účely hry viacerých hráčov)
### !!!K SERVERU JE POTREBNÁ JAVA 8. S INÝMI VERZIAMI JAVA SA SERVER S NAJVACSOU PRAVDEPODOBNOSŤOU NESPUSTÍ!!!

1. Stiahnúť inštalátor forge-1.15.2-31.2.0-installer.jar z priečinku modification v tomto repozitári
2. Vybrať možnosť "install server" a nasmerovať inštalátor do nejakého prázdneho priečinku
3. Pre jednoduchosť premenovať v priečinku s nainštalovaným serverom súbor "forge-1.15.2-31.2.0.jar" na "forgeserver.jar"
4. Spustiť forgeserver.jar obyčajným dvojklikom. Nič sa nestane, ale vytvoria sa v priečinku ďalšie súbory a priečinky
5. Prepísať hodnotu v súbore eula.txt z eula=false na eula=true
6. Do priečinku mods v adresári so serverom skopírovať bitcoin_modification-1.0.jar z priečinku modification v tomto repozitári (Ak sa takýto priečinok sám nevytvoril, tak ho treba vytvoriť)
7. Vytvoriť run.bat v adresári so serverom s príkazom "java -Xmx2048M -Xms2048M -jar forgeserver.jar"
8. Spustiť run.bat z konzolového rozhrania a server by sa mal zapnúť


# Hranie hry s modifikáciou

Modifikácia do hry pridáva 3 použiteľné bloky. Každý z nich poskytuje iné grafické rozhranie a simuluje iný aspekt Bitcoinu:

* Bitcoin Wallet Block - simuluje funkcionalitu Bitcoin Peňaženiek. Pomocou tohoto bloku sa generujú páry Privátny kľúč-Bitcoin adresa v podobe herného predmetu Bitcoin Key Pair, zobrazuje sa zostatok Bitcoinov a vytvárajú sa transakcie
* Mining Block - simuluje funkcionalitu ťažobných softwarov. Umožňuje hráčom získať Bitcoiny z procesu ťaženia
* Blockchain Explorer Block - simuluje prehliadače Blockchainu. Hráči môžu prezerať všetky transakcie, ktoré boli na serveri vytvorené.

![Bloky](/screenshoty/blocks.png)

Tieto bloky je možné vyrobiť nasledujúcimi výrobnými receptami:

* Bitcoin Wallet Block - 2x papier
* Mining Block - 2x drevený kompáč
* Blockchain Explorer Block - 2x kniha

Alebo ich je možné vyvolať administrátorskými príkazmi:
* /give {playername alebo @p} bitcoinmod:wallet_block
* /give {playername alebo @p} bitcoinmod:mining_block
* /give {playername alebo @p} bitcoinmod:blockchain_block


## Generovanie Bitcoin adries

Generovanie privátneho kľúča a Bitcoin Adresy sa vykonáva pomocou Bitcoin Wallet Blocku kliknutím na tlačítko "New Bitcoin Key Pair". To vygeneruje predmet "Bitcoin Key Pair", ktorý má v sebe uložený privátny kľúč a Bitcoin Adresu. Tieto kľúče je možné zobraziť ukázaním myšou na predmet Bitcoin Key Pair v inventárovom zobrazení, kedy sa informácie zobrazia ako Tooltip.

![Bloky](/screenshoty/generovanie.png)

## Vytváranie Transakcií

Vytváranie transakcií sa taktiež vykonáva pomocou Bitcoin Wallet Blocku. Hráč doňho vloží predmet Bitcoin Key Pair, čím mu blok vypočíta zostatok Bitcoinov na danej Bitcoin Adrese. Do prvého textového poľa hráč napíše počet Bitcoinov, ktoré chce previesť a do druhého textového poľa napíše Bitcoin adresu príjemcu. Tlačítkom Send sa transakcia odošle na server.

![Bloky](/screenshoty/posielanie.png)

## Ťažba Bitcoinov

Ťažba sa iniciuje pomocou Mining Blocku. Hráč doňho vloží predmet Bitcoin Key Pair, čím sa daný Mining Block zapojí do losovania o Bitcoiny, ktoré budú pripísané na Bitcoin Adresu vo vloženom predmete Bitcoin Key Pair. Server každú minútu v rámci všetkých Mining Blockov vyberie jednu Bitcoin Adresu, na ktorú pripíše Bitcoiny. Hráč môže vložiť diamanty do Mining Blocku, čím zvýši šancu, že server takýto blok vyberie.

![Bloky](/screenshoty/mining.png)
## Prezeranie Blockchainu

"Blockchain" (list transakcií) si hráči môžu prezerať pomocou Blockchain Explorer Blocku. Ten zobrazuje všetky transakcie a detaily o ich výstupoch ako odosielateľa, príjemcu, množstvo poslaných Bitcoinov a informáciu, či bol výstup už minutý alebo ešte nie.

![Bloky](/screenshoty/blockchain.png)

## Využitie Bitcoinov

Modifikácia neponúka žiadny spôsob, ktorým by užívatelia mohli Bitcoiny minúť (napríklad v rámci nejakého obchodu). Hlavne z toho dôvodu, že by to bolo problematické na implementáciu. Avšak modifikácia do hry pridáva ekosystém, ktorý hráči môžu využiť na obchodovanie medzi sebou.
