package controllers;



import htwg.se.chess.Init;
import play.*;
import play.mvc.*;
import views.html.*;
import play.mvc.WebSocket;
import play.libs.F.Callback;
import play.libs.F.Callback0;

public class Application extends Controller {
	
	Init init = null;
	
    public Result index() {   	
    	//Init.main(null); //ruft Spiel auf    	
        return ok(index.render("UChess Titel"));
        
    }
    
    public Result game(){   
    	
    	init = Init.getInstance();
    	
    	return ok(main.render("Welcome To UChess",init.getWTui().replaceAll(" ", "&nbsp;")));
    }
    
      public Result wui(){   
    	return ok(wui.render());
    }
      
      public Result wuii(){   
      	return ok(ng_wui.render());
      }
    
    public Result reset(){   	
    	
    	Init.getInstance().getCc().reset();
    	
    	return ok(main.render("Welcome To UChess",Init.getInstance().getWTui().replaceAll(" ", "&nbsp;")));
    }
    
    public Result move(String command){
    	Init init = Init.getInstance();
    	init.getTui().processInputLine(command);
    	
    	return ok(main.render("WTUI",Init.getInstance().getWTui().replaceAll(" ", "&nbsp;")));
    }
    
    
    public WebSocket<String> webSocket() {
        return new WebSocket<String>() {
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
                in.onMessage(new Callback<String>() {
                        public void invoke(String event) {
                            out.write("Nachricht " + event);
                        }
                    });
                in.onClose(new Callback0() {
                        public void invoke() { out.write("und Tschues");}
                    });
            }
        };
    }

    

}
