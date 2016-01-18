package controllers;

import java.util.ArrayList;
import java.util.LinkedList;
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
	ArrayList<WebSocket.Out<String>> playerList = new ArrayList<WebSocket.Out<String>>();
	List<GameInstance> gameList = new LinkedList<GameInstance>();

	public Result index() {
		return ok(index.render("UChess Titel"));
	}

	public Result game() {
		return ok(ng_wui.render());
	}

	public WebSocket<String> webSocket() {
		return new WebSocket<String>() {
			public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
				if (playerList.isEmpty()) {
					game = new GameInstance(out);
					playerList.add(out);
					gameList.add(game);
					System.out.println("count list " + playerList.size());
				} else {
					playerList.clear();
					System.out.println("clear " + playerList.size());
					// playerList.remove(0);
					game.setPlayer2(out);
				}

				in.onMessage(event -> {
					game = checkWhichGame(out);
					// System.out.println(event);
					switch (event.substring(0, 4)) {
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

			private GameInstance checkWhichGame(Out<String> out) {
				if (gameList.size() > 1) {
					for (GameInstance gameInstance : gameList) {
						if (gameInstance.player1.equals(out) || gameInstance.player2.equals(out))
							return gameInstance;
					}

				}
				return gameList.get(0);
			}
		};
	}

}
