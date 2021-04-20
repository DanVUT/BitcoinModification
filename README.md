# Bitcoin Modification SK

Táto modifikácia pridáva do hry Minecraft ekosystém kryptomeny Bitcoinu pre výučbové účely. Hráči môžu získať prehľad o Bitcoine jednoduchou a zábavnou formou bez nutnosti investovať skutočné peniaze.

## Návod na inštaláciu pre herného klienta

1. Stiahnúť inštalátor forge-1.15.2-31.2.0-installer.jar z priečinku modification v tomto repozitári
2. Vybrať možnosť "install client" a nasmerovať inštalátor do priečinku s hernými súbormi. Predvolená cesta je C:\Users\\{username}\AppData\Roaming\\.minecraft
3. V priečinku s hernými súbormi by sa mal objaviť priečinok mods. Predvolená cesta je C:\Users\\{username}\AppData\Roaming\\.minecraft\mods
4. Nakopírovať do priečinku mods bitcoin_modification-1.0.jar z priečinku modification v tomto repozitári

## Návod na inštaláciu dedikovaného servera (pre účely hry viacerých hráčov)
### !!!K SERVERU JE POTREBNÁ JAVA 8. S INÝMI VERZIAMI JAVA SA SERVER S NAJVACSOU PRAVDEPODOBNOSŤOU NESPUSTÍ!!!

1. Stiahnúť inštalátor forge-1.15.2-31.2.0-installer.jar z priečinku modification v tomto repozitári
2. Vybrať možnosť "install server" a nasmerovať inštalátor do nejakého prázdneho priečinku
3. Pre jednoduchosť premenovať v priečinku s nainštalovaným serverom súbor "forge-1.15.2-31.2.0.jar" na "forgeserver.jar"
4. Spustiť forgeserver.jar obyčajným dvojklikom. Nič sa nestane, ale vytvoria sa v priečinku ďalšie súbory a priečinky
5. Prepísať hodnotu v súbore eula.txt z eula=false na eula=true
6. Do priečinku mods v adresári so serverom skopírovať bitcoin_modification-1.0.jar z priečinku modification v tomto repozitári (Ak sa takýto priečinok sám nevytvoril, tak ho treba vytvoriť)
7. Vytvoriť run.bat v adresári so serverom s príkazom "java -Xmx2048M -Xms2048M -jar forgeserver.jar"
8. Spustiť run.bat z konzolového rozhrania a server by sa mal zapnúť


## Hranie hry s modifikáciou

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


### Generovanie Bitcoin adries

Generovanie privátneho kľúča a Bitcoin Adresy sa vykonáva pomocou Bitcoin Wallet Blocku kliknutím na tlačítko "New Bitcoin Key Pair". To vygeneruje predmet "Bitcoin Key Pair", ktorý má v sebe uložený privátny kľúč a Bitcoin Adresu. Tieto kľúče je možné zobraziť ukázaním myšou na predmet Bitcoin Key Pair v inventárovom zobrazení, kedy sa informácie zobrazia ako Tooltip.

![Bloky](/screenshoty/generovanie.png)

### Vytváranie Transakcií

Vytváranie transakcií sa taktiež vykonáva pomocou Bitcoin Wallet Blocku. Hráč doňho vloží predmet Bitcoin Key Pair, čím mu blok vypočíta zostatok Bitcoinov na danej Bitcoin Adrese. Do prvého textového poľa hráč napíše počet Bitcoinov, ktoré chce previesť a do druhého textového poľa napíše Bitcoin adresu príjemcu. Tlačítkom Send sa transakcia odošle na server.

![Bloky](/screenshoty/posielanie.png)

### Ťažba Bitcoinov

Ťažba sa iniciuje pomocou Mining Blocku. Hráč doňho vloží predmet Bitcoin Key Pair, čím sa daný Mining Block zapojí do losovania o Bitcoiny, ktoré budú pripísané na Bitcoin Adresu vo vloženom predmete Bitcoin Key Pair. Server každú minútu v rámci všetkých Mining Blockov vyberie jednu Bitcoin Adresu, na ktorú pripíše Bitcoiny. Hráč môže vložiť diamanty do Mining Blocku, čím zvýši šancu, že server takýto blok vyberie.

![Bloky](/screenshoty/mining.png)
### Prezeranie Blockchainu

"Blockchain" (list transakcií) si hráči môžu prezerať pomocou Blockchain Explorer Blocku. Ten zobrazuje všetky transakcie a detaily o ich výstupoch ako odosielateľa, príjemcu, množstvo poslaných Bitcoinov a informáciu, či bol výstup už minutý alebo ešte nie.

![Bloky](/screenshoty/blockchain.png)

### Využitie Bitcoinov

Modifikácia neponúka žiadny spôsob, ktorým by užívatelia mohli Bitcoiny minúť (napríklad v rámci nejakého obchodu). Hlavne z toho dôvodu, že by to bolo problematické na implementáciu. Avšak modifikácia do hry pridáva ekosystém, ktorý hráči môžu využiť na obchodovanie medzi sebou.


# Bitcoin Modification ENG

This modification adds Bitcoin cryptocurrency ecosystem into Minecraft for educational purposes. Players can try Bitcoin simple and fun way without investing real money into it.

## Client installation instructions

1. Download installer "forge-1.15.2-31.2.0-installer.jar" from directory "modification" from this repository
2. Choose option "install client" and set installer into game files directory. Default path is C:\Users\\{username}\AppData\Roaming\\.minecraft
3. In game's folder "mods" folder should appear. Default path is C:\Users\\{username}\AppData\Roaming\\.minecraft\mods
4. Copy "bitcoin_modification-1.0.jar" from directory "modification" in this repository into "mods" folder

## Server installation instructions
### !!!JAVA 8 IS NEEDED TO RUN SERVER. OTHER VERSIONS WILL PROBABLY NOT WORK!!!

1. Download installer "forge-1.15.2-31.2.0-installer.jar" from directory "modification" from this repository
2. Choose option "install server" and set installer into a empty folder.
3. Rename file "forge-1.15.2-31.2.0.jar" to "forgeserver.jar"
4. Try to run forgeserver.jar. Nothing will run, but more files and folders should appear.
5. Open eula.txt and change value from eula=false to eula=true
6. Copy "bitcoin_modification-1.0.jar" from directory "modification" in this repository into "mods" folder in server's folder (if this directory was not created automatically create it manually)
7. Create run.bat file in server folder with following command "java -Xmx2048M -Xms2048M -jar forgeserver.jar"
8. Execute run.bat from command line

## Playing game with modification

Modification adds 3 usable blocks into the game:

1. Bitcoin Wallet Block - simulates Bitcoin Wallet software behavior. Can generate key pairs (private key - Bitcoin Address) as Bitcoin Key Pair item, create transactions, show Bitcoin balance
2. Mining Block - simulates Mining software bahavior. By placing this block and inserting Bitcoin Key Pair into it player has chance to recieve ("mine") Bitcoins.
3. Blockchain Explorer Block - simulates Blockchain explorers behavior. Players can view all transactions that were created on that server

These blocks can be crafted: 

1. Bitcoin Wallet Block - combine 2x paper
2. Mining Block - combine 2x wooden pickaxe
3. Blockchain Explorer Block - combine 2x book

If player has administation rights these blocks can be spawned by following commands:

1. /give {playername or @p} bitcoinmod:wallet_block
2. /give {playername or @p} bitcoinmod:mining_block
3. /give {playername or @p} bitcoinmod:blockchain_block

![Bloky](/screenshoty/blocks.png)

### Generation of Bitcoin Addresses

Generation of Bitcoin Addresses is simple. In Bitcoin Wallet Block interface player clicks on "New Bitcoin Key Pair" button which will generate item Bitcoin Key Pair. This item contains information about private key and Bitcoin Address that has been generated from this private key. This information can be displayed by hovering mouse over this item in any inventory interface and private key with Bitcoin Address will display as Tooltip.

### Creating transactions

Transactions are again created in Bitcoin Wallet Block interface. Into lower slot player can place Bitcoin Key Pair item. The GUI will calculate Bitcoin balance from all transactions for Bitcoin address in given Bitcoin Key Pair item. Player can write Bitcoin amount that he wants to send into first text box. Into second text box player can write recipient's Bitcoin Address. By clicking Send, player's application will send request about new transaction to server and it will check the request and approve (or disapprove) this new transaction.

### Mining Bitcoins

Mining is done via Mining Block. Into this Mining Block player can place Bitcoin Key Pair item. Server every minute picks one Bitcoin Address from all Mining Blocks which will recieve Bitcoins as reward in the form of Coinbase Transaction. Players can create as many Mining Blocks as they like. Moreover players can mine diamonds and insert them into Mining Block which will increase the chance of being picked by server for this Mining Block. The more diamonds player inserts into the block the higher chance for being picked this Mining Block has.

### Browsing transactions

Every player can browse all transactions that were created on the server via Blockchain Explorer Block. The GUI of this block provides list view of all transactions and details for these transactions. The detail for each transaction contains information like: sender of the transaction, reciever of the transaction, number of sent Bitcoins, status of the transaction outputs (unspent/spent), or information if transaction is Coinbase transaction (product of mining).

### Use of Bitcoins

For now there is no use for these Bitcoins from modification perspective. There is no shop or anything similar. But this modification adds Bitcoin ecosystem to the game so players can use it as currency between themselves.
