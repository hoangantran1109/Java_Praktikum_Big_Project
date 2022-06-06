# Praktikum_RoboRallyGame
# Die Robo Rally
Der Lockeren Lilien
Features
- Der Server verwendet grundsätzlich die Portnummer 7070
- Dem Client können beim starten des Jar-files optional bis zu drei Argumente übergeben werden in dem Muster: „java -jar .\Client -h localhost -p Portnummer -u ki“
- Zum Test mit einen anderem Server wird wahrscheinlich die entsprechende Portnummer übergeben werden müssen
- Die logs werden in einem Ordner „logs“ gespeichert, welcher automatisch im „temp“ Ordner angelegt wird. Der „temp“ Ordner kann mittels Suche nach „%temp%“ im Explorer oder Finder gefunden werden
Chat
- Nach dem Starten eines Severs können noch bis zu 6 Clients gestartet werden.
- Beim Start eines Clients erscheint als ersten ein Login-Fenster, in dem Name und Roboter ausgewählt werden müssen. Außerdem entscheidet man durch Setzen eines Hakens, ob der Client eine KI sein soll.
- Nach der erfolgreichen Anmeldung öffnet sich das Chatfenster.
- Im Chatfenster werden Nachrichten an alle einfach in das Eingabefeld unten geschrieben und mit einem Klick auf den „Senden“ Button oder mittels Entertaste abgeschickt.
- Privatnachrichten an einen bestimmten User werden durch ein Voranstellen von „@Username “ vor die eigentliche Nachricht im Eingabefeld ermöglicht.
- Durch Klicken des Buttons „ready to play” signalisiert man seine Bereitschaft, das Spiel zu starten.
- Mit Klicken des anschließend erscheinenden Buttons „Not ready“ kann man diese Bereitschaft wieder zurückziehen.
- Der erste Spieler, der Bereitschaft signalisiert, kann nach einem Klick auf den Button „Map“ im Dropdown-Menü zwischen den beiden Spielfeldern wählen. Anschließend muss er seine Wahl mit „Send selected Map“ bestätigen. Wird keine Map ausgewählt, startet das Spiel mit Dizzy Highway.
Spiel
- Sobald alle Spieler Bereitschaft signalisiert haben, öffnet sich das Spielfenster. Dieses kann beliebig in der Größe verändert werden.
- Unten links werden die eigene Spielfigur, die eigenen Energycubes, die noch im Programmierkarten-Stapel verbleibenden Karten und erreichte Checkpoints angezeigt (erst nach Erreichen des ersten Checkpoints).
- Oben rechts werden der eigene Name, der Name des Spielers, der an der Reihe ist, die aktuelle Spielphase sowie die bereits gespielten Runden angezeigt. Außerdem kann hier durch Setzen eines Hakens hervorragende 8-Bit Musik an- oder ausgeschalten werden.
- Im Fenster rechts neben dem Spielfeld werden Meldungen über den Spielverlauf angezeigt. Z.B. wer eine Schadenskarte oder einen Energycube bekommen hat.
- Das Spiel zeigt stets mit grünen Markierungen an, welche Eingaben die Spieler tätigen müssen.
- Zunächst muss von jedem Spieler reihum eine der 6 Startpositionen mittels Anklicken ausgewählt werden.
- Anschließend beginnt die erste Programming Phase. Jeder Spieler erhält 9 Programmier-
Karten auf seine „Hand“ (ganz unten im Spielfenster). Durch Klicken auf diese Hand-Karten
werden sie nacheinander in die 5 Register oberhalb der „Hand“ bewegt. Solange noch nicht
alle 5 Register aufgefüllt sind, kann man durch Klicken auf eine Registerkarte diese wieder
zurück in die Handkarten legen.
- Hat ein Spieler alle 5 Register belegt, startet der Timer und den anderen Spielern bleiben 30
Sekunden, um ihre Register zu befüllen, bevor dies nach Ablauf der Zeit automatisch geschieht.
- Haben alle Spieler 5 belegte Register, beginnt die Activation Phase. Rechts unten neben den
Handkarten werden die Karten aller Spieler in ihrem 1. Register angezeigt. Jeder Spieler muss
nun durch Anklicken des „Play It!“ Buttons alle Karten dieses Registers bestätigen. Haben das
alle Spieler getan, vollführen die Roboter die programmierten Bewegungen dieser
Registerkarten. Dies wiederholt sich nun für alle 5 Register.
- Nach dem Ausführen aller 5 Register-Karten startet eine neue Programming Phase.
- Falls alle Schadenskarten eines Typs bereits ausgeteilt wurden und eine weitere gezogen
werden müsste, erscheint rechts unten eine Auswahl aller vier Schadenskarten sowie die
Aufforderung eine bestimmte Anzahl davon auszuwählen. Durch Klicken auf eine der vier
Abbildungen wird die entsprechende Ersatz-Schadenskarte ausgewählt.
- Hat ein Spieler alle Checkpoints einer Map erreicht, beendet sich das Spielfenster und es
erscheint eine Gewinner- bzw. Verlierernachricht.
Hotkeys
1. Im Login-Fenster können Roboter mit den Pfleiltasten „oben“ und „unten“ ausgewählt werden
2. Im Chatfenster können Nachrichten mit der Entertaste abgeschickt werden
Cheats
Alle Cheats beginnen mit „$$“ und werden wie normale Chatnachrichten in das Chatfenster
eingegeben und abgeschickt. Cheats können grundsätzlich nur in der Programming Phase eingegeben
werden.
1. $$moveTo:x,y platziert einen Roboter auf dem Feld mit den Koordinaten
x, y (Zählung auf x- und y-Achse beginnt jeweils bei 0)
2. $$rotate rotiert den Roboter um 90° im Uhrzeigersinn
3. $$clearSpamCardPile leert den Stapel mit den Spam-Damagekarten
4. $$clearDamageCardPiles lehrt alle vier Damagekarten Stapel
5. $$populateDamageCardPiles befüllt alle Damagekarten Stapel mit der gleichen Anzahl an
Karten wie zu Beginn des Spiels
6. $$setCheckpoints:x erhöht die Zahl der vom Spieler erreichten Checkpoints auf
den eingegebenen Wert x
7. $$reboot Der Spieler rebooted
