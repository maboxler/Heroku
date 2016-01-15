package controllers;

import htwg.se.chess.Init;
import play.mvc.WebSocket.Out;

public class GameInstance {
	
	Out<String> player1;
	Out<String> player2;
	Init instance;
	WebController controller;
	Out<String> challenger;
	
	public GameInstance(Out<String> player1) {
		this.player1 = player1;
		this.instance = new Init();
		this.controller = new WebController( this.instance);
		player1.write(this.controller.getField());
		player1.write("WAIT");
	}

	private void send(String msg) {
		this.player1.write(msg);
		this.player2.write(msg);
	}
	
	public void chat(String msg, Out<String> sender){
		Out<String> receiver = sender.equals(player1) ? player2 : player1;
		System.out.println("chatting");
		System.out.println(receiver);
		receiver.write(msg);
	}
	
	public void reset(boolean force, Out<String> sender) {
		if(force){
			send("QUIT");
			instance.getCc().reset();
		} else {
			if(!sender.equals(challenger)){
				if(challenger == null){
					challenger = sender;
					sender.write("WAIT");
				} else {
					challenger = null;
					instance.getCc().reset();
					send(controller.getStatusMessage());
					send(this.controller.getField());
				}
			}
		}
	}
	
	public void move(String command, Out<String> sender) {
		if(checkPlayersTurn(sender)){
			if(controller.move(command)) {
				send(controller.getStatusMessage());
				send(controller.getField());
			} else {
				if(!controller.getWinner().equals("NONE")){
					send("WINNER " + controller.getWinner());
				}
			}
		} else {
			sender.write("WAIT");
		}
	}
	
	private boolean checkPlayersTurn(Out<String> sender){
		if(sender.equals(player1) && controller.getStatusMessage().equals("black") ||
				sender.equals(player2) && controller.getStatusMessage().equals("white")){
			return true;
		}
		return false;
	}

	public Out<String> getPlayer1() {
		return player1;
	}

	public Out<String> getPlayer2() {
		return player2;
	}

	public void setPlayer2(Out<String> player2) {
		this.player2 = player2;
		send(this.controller.getField());
		send("black");
	}
	
	public WebController getController() {
		return controller;
	}

	

}
