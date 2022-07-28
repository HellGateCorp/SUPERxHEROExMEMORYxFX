package marvel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
//Das Spielfeld
public class MemoryFeld extends MarvelMemoryFX{
	//eine innere Klasse für den Eventhandler des Timer
	class TimerHandler implements EventHandler<ActionEvent> {
			@Override
			//die Methode ruft die Methode karteSchliessen() 
			//oder die Methode schummelnVorwaerts auf
			public void handle(ActionEvent arg0) {
				if(arg0.getSource() instanceof Button) 
					schummelnVorwaerts();				
				else
					karteSchliessen();
			}		 	
			
		}
	//das Array für die Karten
	private MemoryKarte[] karten;
	
	//das Array für die Namen der Grafiken
	private String[] bilder = {"icons/1.png", "icons/2.png", "icons/3.png", "icons/5.png", "icons/6.png", "icons/7.png", 
			"icons/8.png", "icons/9.png", "icons/10.png", "icons/11.png", "icons/12.png", "icons/13.png", "icons/14.png", 
			"icons/15.png", "icons/16.png", "icons/17.png", "icons/18.png", "icons/19.png", "icons/20.png", "icons/21.png"};
	//für die Punkte
	private int menschPunkte=0, computerPunkte=0;
	//zwei Labels für die Punkte
	private Label menschPunkteLabel, computerPunkteLabel;
	//zwei Labels für die Ausgabe 
	//des aktuellen Spielers
	private Label amZugLabel;
	//wie viele Karten sind aktuell umgedreht?
	private int umgedrehteKarten;
	//für das aktuell umdrehte Paar
	private MemoryKarte[] paar;
	//für den aktuellen Spieler
	private int spieler;
	//für die Ausgabe des aktuellen Spielers
	private String player;
	//das "Gedächtnis" für den Computer
	//er speichert hier wo das Gegenstück liegt
	private int[][] gemerkteKarten;
	//für die Spielstärke
	private int spielstaerke, flag,flag2;
	//für den Timer
	private Timeline timer, timer2;
	//für die Schummelfunktion
	private Button schummeln;
	public Stage meineStage;
	private Media media,media2,media3,media4,media5,media6,media7;
	private MediaView mediaView=new MediaView(), mediaView2=new MediaView(),mediaView3=new MediaView(),mediaView4=new MediaView(),mediaView5=new MediaView(),mediaView6=new MediaView(),mediaView7=new MediaView();
	private MediaPlayer mplayer,mplayer2,mplayer3,mplayer4,mplayer5,mplayer6,mplayer7;
	boolean raus;
	//der Konstruktor
	public MemoryFeld() {
		//das Array für die Karten erstellen, insgesamt 42 Stück
		karten = new MemoryKarte[40];

		//für das Paar
		paar = new MemoryKarte[2];

		//für das Gedächtnis
		//es speichert für jede Karte paarweise die Position im Spielfeld
		gemerkteKarten = new int[2][20];
		
		//es ist keine Karte umgedreht
		umgedrehteKarten = 0;
		
		//der Mensch fängt an
		spieler = 0;
		
		//die Spielstärke ist 10
		spielstaerke = 5;
		
		//de aktuellen Spieler setzen
		player = ("@PLAYER");
		
		meineStage = new Stage();
		
		
		setAudio();
		
		
		//es gibt keine gemerkten Karten
		for (int aussen = 0; aussen < 2; aussen++)
			for (int innen = 0; innen < 20; innen++)
				gemerkteKarten[aussen][innen] = -1;
	}
	

	public void setAudio() {
		media5 = new Media(getClass().getResource("/sounds/mariogo1.wav").toExternalForm());
		mplayer5 = new MediaPlayer(media5);
		mediaView5.setMediaPlayer(mplayer5);
		mplayer5.setVolume(75);
		mplayer5.play();
		
		media = new Media(getClass().getResource("/sounds/mintro.mp3").toExternalForm());
		mplayer = new MediaPlayer(media);
		mediaView.setMediaPlayer(mplayer);
		mplayer.setVolume(75);
		mplayer.play();
		
		media2 = new Media(getClass().getResource("/sounds/bubble.wav").toExternalForm());
		mplayer2 = new MediaPlayer(media2);
		mediaView2.setMediaPlayer(mplayer2);
		mplayer2.setVolume(75);
		
		media3 = new Media(getClass().getResource("/sounds/applaus.m4a").toExternalForm());
		mplayer3 = new MediaPlayer(media3);
		mediaView3.setMediaPlayer(mplayer3);
		mplayer3.setVolume(75);
		
		media4 = new Media(getClass().getResource("/sounds/mtheme.mp3").toExternalForm());
		mplayer4 = new MediaPlayer(media4);
		mediaView4.setMediaPlayer(mplayer4);
		mplayer4.setVolume(75);
		
		media6 = new Media(getClass().getResource("/sounds/firework.m4a").toExternalForm());
		mplayer6 = new MediaPlayer(media6);
		mediaView6.setMediaPlayer(mplayer6);
		mplayer6.setVolume(75);
		
		media7 = new Media(getClass().getResource("/sounds/bowser.wav").toExternalForm());
		mplayer7 = new MediaPlayer(media7);
		mediaView7.setMediaPlayer(mplayer7);
		mplayer7.setVolume(75);
		
	}
	
	public void setMeineStage(Stage meineStage) {
		this.meineStage = meineStage;
	}
	
	public void setPunkteMensch(int punkte)  {
		this.menschPunkte = punkte;
	}

	public void setPunkteCom(int punkte)  {
		this.computerPunkte = punkte;
	}

	//die Methode erstellt die Oberfläche und zeichnet die Karten über eine eigene Methode
	//übergeben wird ein FlowPane
	public FlowPane initGUI(FlowPane feld) {
		
		//für die Ausgaben
		kartenZeichnen(feld);
		menschPunkteLabel = new Label();
		computerPunkteLabel = new Label();
		amZugLabel = new Label();
		schummeln = new Button("CHEATING");
		schummeln.setOnAction(new TimerHandler());
		menschPunkteLabel.setText(Integer.toString(menschPunkte));
		computerPunkteLabel.setText(Integer.toString(computerPunkte));
		amZugLabel.setText(player);
		
		//in zwei Spalten anzeigen
		GridPane tempGrid = new GridPane();
		//und einfügen, dabei werden die Koordinaten angegeben
		tempGrid.add(new Label("Player: "), 0 , 0 );
		tempGrid.add(menschPunkteLabel, 1, 0);
		tempGrid.add(new Label("Bot: "), 0, 1);
		tempGrid.add(computerPunkteLabel, 1 ,1);
		tempGrid.add(new Label("OnTurn: "), 0, 2);
		tempGrid.add(amZugLabel, 1, 2);
		tempGrid.add(schummeln, 0, 4);
		feld.getChildren().add(tempGrid);
		return feld;
	}
	
	//das eigentliche Spielfeld erstellen
	public void kartenZeichnen(FlowPane feld) {
	
		int count = 0;
		for (int i = 0; i <= 39; i++) {
			//eine neue Karte erzeugen
			karten[i] = new MemoryKarte(bilder[count], count, this);
			//bei jeder zweiten Karte kommt auch ein neues Bild
			if ((i + 1) % 2 == 0)
				count++;
		}
		//die Karten werden gemischt 
		Collections.shuffle(Arrays.asList(karten));

		//und ins Spielfeld gesetzt
		for (int i = 0; i <= 39; i++) {
			feld.getChildren().add(karten[i]);
			//die Position der Karte setzen
			karten[i].setBildPos(i);
		}
	}
	
	//die Methode übernimmt die wesentliche Steuerung des Spiels
	//Sie wird beim Anklicken einer Karte ausgeführt
	public void karteOeffnen(MemoryKarte karte) {
		mplayer2.stop();
		mplayer2.play();
		if(flag==0) {
			flag++;
			mplayer4.setAutoPlay(true);
		}
		//zum Zwischenspeichern der ID und der Position
		int kartenID, kartenPos;

		//die Karten zwischenspeichern
		paar[umgedrehteKarten]=karte;
		
		//die ID und die Position beschaffen
		kartenID = karte.getBildID();
		kartenPos = karte.getBildPos();
		
		//die Karte in das Gedächtnis des Computers eintragen
		//aber nur dann, wenn es noch keinen Eintrag an der entsprechenden Stelle gibt
		if ((gemerkteKarten[0][kartenID] == -1)) 
			gemerkteKarten[0][kartenID] = kartenPos;
		else
			//wenn es schon einen Eintrag gibt 
			//und der nicht mit der aktuellen Position übereinstimmt, dann haben wir die
			//zweite Karte gefunden
			//die wird dann in die zweite Dimension eingetragen
			if (gemerkteKarten[0][kartenID] != kartenPos) 
				gemerkteKarten[1][kartenID] = kartenPos;
		//umgedrehte Karten erhöhen
		umgedrehteKarten++;
		
		//sind 2 Karten umgedreht worden?
		if (umgedrehteKarten == 2) {
			//dann prüfen wir, ob es ein Paar ist
			paarPruefen(kartenID);
			karteGefundenAnimation();
		}
		//haben wir zusammen 21 Paare, dann ist das Spiel vorbei
		if (computerPunkte + menschPunkte == 21) {
			//den Dialog für die Ausgabe des Gewinners 
			//ansteuern
			ausgabeGewinner();
			//nach ablaufen des Timers
			//Programm beenden
			Platform.exit(); 
		}
		else if(flag2 == 0 && computerPunkte + menschPunkte == 20 && computerPunkte == menschPunkte) {
			flag2++;
			ausgabeUnentschieden();
		}
		else if(flag2 == 0 && computerPunkte + menschPunkte == 20) {
			//den Dialog für die Ausgabe des Gewinners 
			//ansteuern
			ausgabeGewinner();
			//nach ablaufen des Timers
			//Programm beenden
			Platform.exit(); 
		}
	}
	
	//die Methode dreht die Karten wieder auf die Rückseite
	//bzw. nimmt sie aus dem Spiel
	private void karteSchliessen() {
		boolean raus = false;
		//ist es ein Paar?
		if (paar[0].getBildID() == paar[1].getBildID()) 
			raus = true;
		//wenn es ein Paar war, nehmen wir die Karten aus dem Spiel
		//sonst drehen wir sie nur wieder um
		paar[0].rueckseiteZeigen(raus);
		paar[1].rueckseiteZeigen(raus);
		//es ist keine Karte mehr geöffnet
		umgedrehteKarten = 0;
		//hat der Spieler kein Paar gefunden?
		if (raus == false) 
			//dann wird der Spieler gewechselt
			spielerWechseln();
		else
				//hat der Computer ein Paar gefunden?
				//dann ist er noch einmal an der Reihe
				if (spieler == 1)
					computerZug();
		}
	
	private void karteGefundenAnimation() {
		
	if (paar[0].getBildID() == paar[1].getBildID()) { 	
		
		FadeTransition fade = new FadeTransition();
		FadeTransition fade2 = new FadeTransition();
		RotateTransition rotate = new RotateTransition();
		RotateTransition rotate2 = new RotateTransition();
	
		fade.setNode(paar[0]);
		fade.setDuration(Duration.millis(500));
		fade.setCycleCount(4);
		fade.setInterpolator(Interpolator.LINEAR);
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setAutoReverse(true);
	
	
		fade2.setNode(paar[1]);
		fade2.setDuration(Duration.millis(500));
		fade2.setCycleCount(4);
		fade2.setInterpolator(Interpolator.LINEAR);
		fade2.setFromValue(1);
		fade2.setToValue(0);
		fade2.setAutoReverse(true);
		
		rotate.setNode(paar[0]);
		rotate.setDuration(Duration.millis(500));
		rotate.setCycleCount(4);
		rotate.setInterpolator(Interpolator.EASE_OUT);
		//Winkelbestimmung
		rotate.setByAngle(360);
		//Achsenbestimmung
		rotate.setAxis(Rotate.Z_AXIS);
		//
		rotate.setAutoReverse(true);
		
		rotate2.setNode(paar[1]);
		rotate2.setDuration(Duration.millis(500));
		rotate2.setCycleCount(4);
		rotate2.setInterpolator(Interpolator.EASE_OUT);
		//Winkelbestimmung
		rotate2.setByAngle(360);
		//Achsenbestimmung
		rotate2.setAxis(Rotate.Z_AXIS);
		rotate2.setAutoReverse(true);
		
		rotate.play();
		fade.play();
		fade2.play();
		rotate2.play();
		}
	timer = new Timeline(new KeyFrame(Duration.millis(2000), new TimerHandler()));
	//und starten
	timer.play();
			}
	
	
	//die Methode prüft, ob ein Paar gefunden wurde
	private void paarPruefen(int kartenID) {
		mplayer3.stop();
		if (paar[0].getBildID() == paar[1].getBildID()) {
			//die Punkte setzen
			paarGefunden();
			mplayer3.play();
			//die Karten aus dem Gedächtnis löschen
			gemerkteKarten[0][kartenID]=-2;
			gemerkteKarten[1][kartenID]=-2;
		}
	}
	
	private Alert ausgabeGewinner() {
		
		//lokale Variable um den aktuellen
		// Gewinner zwischenzuspeichen 
		String winner = null; 
		if(menschPunkte == computerPunkte)
			ausgabeUnentschieden();
		//ist die Anzahl der Punkte des Spielers
		//größer als die das Computers,
		else if(menschPunkte > computerPunkte) {
 			//dann Spieler als Gewinner 
 			//zwischenapeichern,
 			winner = ("@PLAYER");
 			mplayer6.play();
	}
 		else if(menschPunkte < computerPunkte){
 			//wenn nicht dann Computer 
 			//als Gewinner markieren
 			winner = ("@MrROBOT");
 			mplayer4.stop();
 			mplayer7.play();
 		}
 		
		//den Dialog erzeugen
 		Alert meinDialog = new Alert(AlertType.INFORMATION, "Der Gewinner dieser Partie ist: " + winner);
 		//den Text setzen
 		meinDialog.setHeaderText("WINNER");
 		// und anzeigen
 		meinDialog.showAndWait();
 		//und den Dialog zurück geben
 		return meinDialog;
		}
	
	private void ausgabeUnentschieden() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Unentschieden");
		alert.setHeaderText("Wollen Sie ein"
				+ " SuddenDeath spielen ?");
		alert.setContentText("Das letzte Paar entscheidet!");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) 
				super.zeichneNeu(meineStage);
			else 
				Platform.exit();
	}
	
	//die Methode setzt die Punkte, wenn ein Paar gefunden wurde
	private void paarGefunden() {
		
		//spielt gerade der Mensch?
		if (spieler == 0) {
			menschPunkte++;
			menschPunkteLabel.setText(Integer.toString(menschPunkte));
		}
		else {
			computerPunkte++;
			computerPunkteLabel.setText(Integer.toString(computerPunkte));
		}
	}
	
	//die Methode wechselt den Spieler
	private void spielerWechseln() {
		//wenn der Mensch an der Reihe war,
		//kommt jetzt der Computer
		if (spieler == 0) {
			spieler = 1;
			//wenn Spieler =1 
			if (spieler == 1)
				//Button ausblenden und
				schummeln.setVisible(false);
				//aktuellen Spieler Maschine setzen
				this.player = ("@MrROBOT");
				//und im Label ausgeben
				amZugLabel.setText(player);
			computerZug();
		}
		else {
			spieler = 0;
			//wenn Spieler = 0
			if (spieler == 0) {
				//Button einblenden
				schummeln.setVisible(true);
				//aktuellen Spieler Mensch setzen
				this.player = ("@YOU");
				//und im Label ausgeben.
				amZugLabel.setText(player);
				
			}		
		}
	}
	//die Methode setzt die Computerzüge um
	private void computerZug() {
		int kartenZaehler = 0;
		int zufall = 0;
		boolean treffer = false;
		//zur Steuerung über die Spielstärke
		if ((int)(Math.random() * spielstaerke) == 0) {
			//erst einmal nach einem Paar suchen
			//dazu durchsuchen wir das Array gemerkteKarten, bis wir in beiden Dimensionen
			//einen Wert finden
			while ((kartenZaehler < 20) && (treffer == false)) {
				//gibt es in beiden Dimensionen einen Wert größer oder gleich 0?
				if ((gemerkteKarten[0][kartenZaehler] >=0) &&  (gemerkteKarten[1][kartenZaehler] >=0)) {
					//dann haben wir ein Paar
					treffer = true;
					//die Vorderseite der Karte zeigen
					karten[gemerkteKarten[0][kartenZaehler]].vorderseiteZeigen();
					//und dann die Karte öffnen
					karteOeffnen(karten[gemerkteKarten[0][kartenZaehler]]);
					//die zweite Karte auch
					karten[gemerkteKarten[1][kartenZaehler]].vorderseiteZeigen();
					karteOeffnen(karten[gemerkteKarten[1][kartenZaehler]]);
				}
				kartenZaehler++;
			}
		}
		//wenn wir kein Paar gefunden haben, drehen wir zufällig zwei Karten um
		if (treffer == false) {
			//solange eine Zufallszahl suchen, bis eine Karte gefunden wird, die noch im Spiel ist
			do {
				zufall = (int)(Math.random() * karten.length);
			} while (karten[zufall].isNochImSpiel() == false);
			//die erste Karte umdrehen
			//die Vorderseite der Karte zeigen
			karten[zufall].vorderseiteZeigen();
			//und dann die Karte öffnen
			karteOeffnen(karten[zufall]);

			//für die zweite Karte müssen wir außerdem prüfen, ob sie nicht gerade angezeigt wird
			do {
				zufall = (int)(Math.random() * karten.length);
			} while ((karten[zufall].isNochImSpiel() == false) || (karten[zufall].isUmgedreht() == true));
			//und die zweite Karte umdrehen
			karten[zufall].vorderseiteZeigen();
			karteOeffnen(karten[zufall]);
		}
	}
	
	//die Methode liefert, ob Züge des Menschen erlaubt sind
	//die Rückgabe ist false, wenn gerade der Computer zieht
	//oder wenn schon zwei Karten umgedreht sind
	//sonst ist die Rückgabe true
	public boolean zugErlaubt() {
		boolean erlaubt = true;
		//zieht der Computer?
		if (spieler == 1)
			erlaubt = false;
		//sind schon zwei Karten umdreht?
		if (umgedrehteKarten == 2)
			erlaubt = false;
		return erlaubt;
	}
	//die Methode die nach dem Auslösen des Buttons
	//die Karten für 3 Sekunden aufdeckt
	public void schummelnVorwaerts() {
		//ist aktuell keine Karte umgedreht dann
		if(umgedrehteKarten == 0)
		//Schleife um sämtliche Karten durchzugehen und
		for (int i = 0; i <=39; i++) {
			//wenn sie noch im Spiel sind
			if(karten[i].isNochImSpiel() == true) 
				//werden die Karten aufgedeckt
				karten[i].vorderseiteZeigen();
			//Timeline erzeugen welche nach 3 Sec die Methode 
			//rueckwaertsSchummeln() aufruft
			timer2 = new Timeline(new KeyFrame(Duration.seconds(2), event -> schummelnRueckwaerts()));
			timer2.play();
		}
		//Wenn aber eine Karte schon aufgedeckt ist dann
		if(umgedrehteKarten == 1)
			//Methode fehlermeldung auslösen
			fehlermeldung();
	}
	//die Methode welche die Karten wieder umderehen
	public void schummelnRueckwaerts() {
		//Schleife geht wieder sämtliche Karten die 
		//noch im Spiel sind durch
		for (int i = 0; i <= 39; i++) 
			if(karten[i].isNochImSpiel() == true) 
				//und dreht Sie wieder um
				karten[i].rueckseiteZeigen(false);			
			}
	public Alert fehlermeldung() {
		//den Dialog erzeugen
 		Alert meinDialog = new Alert(AlertType.INFORMATION, "Um diese Funktion nutzen zu können darf keine Karte aufgedeckt sein. "
 				+ "Beenden Sie zuerst ihren Zug");
 		//den Text setzen
 		meinDialog.setHeaderText("NICHT ERLAUBT!");
 		// und anzeigen
 		meinDialog.showAndWait();
 		//und den Dialog zurück geben
 		return meinDialog;
	}
}
