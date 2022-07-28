package marvel;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;


//die Klasse für eine Karte des Memory-Spiels
//Sie erbt von Button
public class MemoryKarte extends Button {
	//die Instanzvariablen
	//eine eindeutige ID zur Identifizierung des Bildes
	private int bildID;
	//für die Vorder- und Rückseite
	public ImageView bildVorne, bildHinten;
	
	//wo liegt die Karte im Spielfeld
	private int bildPos;

	//ist die Karte umgedreht?
	private boolean umgedreht;
	//ist die Karte noch im Spiel?
	public boolean nochImSpiel;
	
	//das Spielfeld für die Karte
	private MemoryFeld spielfeld;
	
	//die innere Klasse für den Eventhandler der Karte
	class KartenHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent arg0) {
			//ist die Karte überhaupt noch im Spiel?
			//und sind Züge erlaubt
			if ((nochImSpiel == false) || (spielfeld.zugErlaubt() == false))
				return;
			//wenn die Rückseite zu sehen ist, die Vorderseite anzeigen
			if (umgedreht == false) {
				vorderseiteZeigen();
				//die Methode karteOeffnen() im Spielfeld aufrufen
				//übergeben wird dabei die Karte
				//also die this-Referenz der äußeren Klasse
				spielfeld.karteOeffnen(MemoryKarte.this);
			}
		}
	}
	

	//der Konstruktor
	//er setzt die Bilder
	public MemoryKarte(String vorne, int bildID, MemoryFeld spielfeld) {
		//die Vorderseite, der Dateiname des Bildes wird an den Konstruktor übergeben
		bildVorne = new ImageView(vorne);
		//die Rückseite, sie wird fest gesetzt
		bildHinten = new ImageView("icons/a.png");
		setGraphic(bildHinten);
		//die Bild-ID
		this.bildID = bildID;
	 	//die Karte ist erst einmal umgedreht und noch im Feld
		umgedreht = false;
		nochImSpiel = true;
		//mit dem Spielfeld verbinden
		this.spielfeld = spielfeld;

		//die Action setzen
		setOnAction(new KartenHandler());
	}
	
	//die Methode zeigt die Vorderseite der Karte an
	public void vorderseiteZeigen() {
		setGraphic(bildVorne);
		umgedreht = true;
	}

	//die Methode zeigt die Rückseite der Karte an
	public void rueckseiteZeigen(boolean rausnehmen) {
		//soll die Karten komplett aus dem Spiel genommen werden?
		if (rausnehmen == true) {
			//das Bild aufgedeckt zeigen und die Karte aus dem Spiel nehmen
			setGraphic(new ImageView("icons/b.png"));
			nochImSpiel = false;
		}
		else {
			//sonst nur die Rückseite zeigen
			setGraphic(bildHinten);
			umgedreht = false;
		}
	}
	
	//die Methode liefert die Bild-ID einer Karte
	public int getBildID() {
		return bildID;
	}

	//die Methode liefert die Position einer Karte
	public int getBildPos() {
		return bildPos;
	}

	//die Methode setzt die Position einer Karte
	public void setBildPos(int bildPos) {
		this.bildPos = bildPos;
	}
	
	//die Methode liefert den Wert der Variablen umgedreht
	public boolean isUmgedreht() {
		return umgedreht;
	}

	//die Methode liefert den Wert der Variablen nochImSpiel
	public boolean isNochImSpiel() {
		return nochImSpiel;
	}
	
}
