package controllers;

import htwg.se.chess.Init;
import htwg.se.controller.Icontroller;
import htwg.se.model.Field;
import htwg.se.view.TUI;
import htwg.util.Event;
import htwg.util.IObserver;
import htwg.util.Point;

public class WebController {
	
	Init instance;

	public WebController(Init instance) {
		this.instance = instance;
	}

	public String getField() {
		return instance.getTui().getFigures();
	}

	public String getStatusMessage() {
		return this.instance.getTui().getTuiController().getStatusMessage();
	}
	
	public boolean move(String command) {
		
		instance.getTui().processInputLine(command);
		if(instance.getTui().getTuiController().checkWin()) {
			return false;
		} 
		
		return true;
	}
	
	public String getWinner(){
		return instance.getTui().getTuiController().getWinner();
	}

}
