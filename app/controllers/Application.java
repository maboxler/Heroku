package controllers;

import java.util.ArrayList;
import java.util.List;

import htwg.se.chess.Init;
import htwg.se.controller.Icontroller;
import htwg.se.view.TUI;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.*;

public class Application extends Controller {
	GameInstance game;
	ArrayList<WebSocket.Out<String> > playerList = new ArrayList<WebSocket.Out<String> >();
	
	public Result index() {
		return ok(index.render("UChess Titel"));
	}

	public Result game() {
		return ok(ng_wui.render());
	}

	public WebSocket<String> webSocket() {
		return new WebSocket<String>() {
			public void onReady(WebSocket.In<String> in, WebSocket.Out<String>  out) {
				if(playerList.isEmpty()) {
					game = new GameInstance(out);
					playerList.add(out);
				}
				else {
					playerList.remove(0);
					game.setPlayer2(out);
				}
				
				in.onMessage(event -> {
					System.out.println(event);
					switch(event.substring(0, 4)) {
					case "RESE":	
						game.reset(false, out);
						break;
					case "CHAT":
						game.chat(event, out);
						break;
					default:	
						game.move(event, out);
					}					

				});
				in.onClose(() -> {
					game.reset(true, out);
					System.out.println("USER CLOSED CONNECTION:");
				});
			}
		};
}

}
