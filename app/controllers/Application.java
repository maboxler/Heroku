package controllers;

import htwg.se.chess.Init;
import htwg.se.controller.Icontroller;
import htwg.se.view.TUI;
import play.*;
import play.mvc.*;
import views.html.*;
import play.mvc.WebSocket;
import play.libs.F.Callback;
import play.libs.F.Callback0;

public class Application extends Controller {

	TUI tui = Init.getInstance().getTui();
	Icontroller controller = tui.getTuiController();
	String gamefield;
	
	public Result index() {
		// Init.main(null); //ruft Spiel auf
		return ok(index.render("UChess Titel"));

	}

	public Result game() {

		return ok(main.render("Welcome To UChess", Init.getInstance().getWTui().replaceAll(" ", "&nbsp;")));
	}

	public Result wui() {
		return ok(wui.render());
	}

	public Result wuii() {
		return ok(ng_wui.render());
	}

	public Result reset() {

		Init.getInstance().getCc().reset();

		return ok(main.render("Welcome To UChess", Init.getInstance().getWTui().replaceAll(" ", "&nbsp;")));
	}

	public Result move(String command) {
		tui.processInputLine(command);

		return ok(main.render("WTUI", Init.getInstance().getWTui().replaceAll(" ", "&nbsp;")));
	}

	public WebSocket<String> webSocket() {
		return new WebSocket<String>() {
			public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
				in.onMessage(event -> {
					
					
					switch(event) {
					case "field":	
						String x = tui.getFigures();	
						out.write(x);
						break;
					case "reset":	
						Init.getInstance().getCc().reset();
						gamefield = tui.getFigures();
						out.write(controller.getStatusMessage());
						out.write(gamefield);
						break;	
					default:	
						tui.processInputLine(event);
						gamefield = tui.getFigures();						
						if(controller.checkWin()) {
							out.write("Winner");
						}
						else {
							out.write(controller.getStatusMessage());
							out.write(gamefield);
						}
					}					

				});
				in.onClose(() -> {
					Init.getInstance().getCc().reset();
					System.out.println("reset erfolgreich");
					tui = Init.getInstance().getTui();
					controller = tui.getTuiController();
					System.out.println("USER CLOSED CONNECTION:");
				});
			}
		};
}

}
