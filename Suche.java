import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class Suche {
	Hamster h = Hamster.getStandardHamster();	//Standardhamster initialisieren
	RouteLaufen route = new RouteLaufen();
	boolean found = false; //Wahrheitswert 'found', welcher aussagt, ob Target gefunden wurde wird mit FALSCH initialisiert
	int targetCol, targetRow; //Variablen für Koordinaten des Target anlegen
	//Anzahl der Reihen und Spalten auslesen
	int rows = Territorium.getAnzahlReihen();
	int cols = Territorium.getAnzahlSpalten();
	
	/*+++++++++++++++++++++++++++++++++++++++++++++++++++++
	Zweidimensionales Integer Array, welches das 
	Territorium mit folgenden Werten repraesentiert:
	-------------------------------------------------------
	-1 = Mauer
	0 = frei
	1 = Start
	Werte > 1 sind Indizes
	+++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	int[][] map = new int[cols][rows];


	void getDetails (Hamster h) {
    	//Iteriere über jededes Feld im Territorium
    	for(int i = 0; i < cols; i++){
    		for(int j = 0; j < rows; j++) {
    			if(Territorium.mauerDa(j, i)){
    				map[i][j] = -1; //Markiere Mauern mit -1
    			} else {
    				map[i][j] = 0; //Markiere freie Felder mit 0
    			}
    			if(Territorium.getAnzahlKoerner(j, i) > 0){ //Wenn auf dem Feld Koerner liegen, speichere seine Koordinaten als die des Target
    				targetCol = i;
    				targetRow = j;
    			}
    		}
    	}
    	map[h.getSpalte()][h.getReihe()] = 1; //Markiere die Position des Hamsters mit 1
	}

	void sucheWeg (Hamster h) {
		getDetails(h);
	    int ind = 1; //Erstelle Indexvariable und setze sie auf 1 //Diese wird verwendet, um die Reihenfolge der Felder zu speichern
    
	    //So lange das Target nicht erreicht wurde iteriere durch das Array
	    while (!found){ 
	    	for(int i = 0; i < cols; i++){
	    		for(int j = 0; j < rows; j++) {    		
	    			if(map[i][j] == ind){ //wenn das Feld mit dem aktuellen Index gefunden wurde
	    				//Markiere die Nachbarfelder mit dem Index + 1 
	    				//Diese werden in der naechsten Iteration auf die gleiche Weise behandelt
	    				mark(i-1, j, ind+1);
	    				mark(i+1, j, ind+1);
	    				mark(i, j-1, ind+1);
	    				mark(i, j+1, ind+1);
	    			}
	    		}
	    	}
	    	ind++; //Erhöhe den Index um 1
	    }
	   int row = targetRow, col = targetCol;
       route.erstelleRoute(ind, row, col, map, h);
       route.hamsterLauf(h);
	}

	void mark(int col, int row, int ind){
		//Methode, die gültige Nachbarn makiert
   		if(col == targetCol && row == targetRow) { //Wenn dieses Feld die Koordinaten des Target hat
   			found = true; //Setze 'gefunden' auf WAHR
   		}
   		if(row >= 0 && col >= 0 && row < rows && col < cols && map[col][row] == 0){ //Wenn Feld innerhalb des Territoriums liegt und frei ist
   			map[col][row] = ind; //Markiere es mit einem Index
   		}
   	}
}
