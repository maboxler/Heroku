package controllers;

import htwg.se.chess.Init;
import htwg.se.view.TUI;
import play.*;
import play.mvc.*;
import views.html.*;
import play.mvc.WebSocket;
import play.libs.F.Callback;
import play.libs.F.Callback0;

public class Application extends Controller {

	TUI tui = Init.getInstance().getTui();

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
					
					System.out.println(event);

					if (event.equals("field")) {
						System.out.println("event: \n" + event);
						String x = tui.getFigures();
						System.out.println("SPIELFELD:\n");
						System.out.println(x);
						out.write(x);
					}

					if (event.charAt(0) == '_') {
						System.out.println("event: \n" + event);
						tui.processInputLine(event.substring(1));
						String t = tui.getFigures();
						System.out.println("MOVE wurde gesendet:\n");
						System.out.println("SPIELFELD:\n");
						System.out.println(t);
						out.write(t);
					}

				});
				in.onClose(() -> {
					System.out.println("USER CLOSED CONNECTION:");
				});
			}
		};
	}

}
