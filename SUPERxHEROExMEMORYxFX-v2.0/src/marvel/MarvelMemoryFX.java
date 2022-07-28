package marvel;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class MarvelMemoryFX extends Application{
	MemoryFeld memoryFeld;
	@Override
	public void start(Stage meineStage) throws Exception {
		memoryFeld = new MemoryFeld();
		//den obersten Knoten erzeugen
		//hier verwenden wir ein FlowPane
		//erzeugt wird die Oberfläche über eine eigene Methode in der Klasse MemoryFeld
		FlowPane rootNode = memoryFeld.initGUI(new FlowPane(Orientation.VERTICAL, 1,1));
		//die Szene erzeugen
		//an den Konstruktor werden der oberste Knoten und die Größe übergeben
		Scene meineScene = new Scene(rootNode, 1370, 840);
		memoryFeld.setMeineStage(meineStage);
		//den Titel über stage setzen
		meineStage.setTitle("MARVELxMEMORYxFX");
		//die Szene setzen
		meineStage.setScene(meineScene);
		//Größenänderungen verhindern
		meineStage.setResizable(true);
		//und anzeigen
		
		meineStage.show();
	}
	
	public void zeichneNeu(Stage meineStage) {
		memoryFeld = new MemoryFeld();
		FlowPane rootNode = memoryFeld.initGUI(new FlowPane(Orientation.VERTICAL, 1,1));
		//die Szene erzeugen
		//an den Konstruktor werden der oberste Knoten und die Größe übergeben
		Scene meineScene = new Scene(rootNode, 1370, 840);
		memoryFeld.setMeineStage(meineStage);
		//den Titel über stage setzen
		meineStage.setTitle("MARVELxMEMORYxFX");
		//die Szene setzen
		meineStage.setScene(meineScene);
		//Größenänderungen verhindern
		meineStage.setResizable(true);
		//und anzeigen
		memoryFeld.setPunkteMensch(10);
		memoryFeld.setPunkteCom(10);
		meineStage.show();
	}
}
