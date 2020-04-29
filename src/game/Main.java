package game;

import engine.GameEngine;
import engine.IGameLogic;
 
public class Main {

    public static final int WIDTH = 480;
    public static final int HEIGHT = 600;
	
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new Game();
            GameEngine gameEng = new GameEngine("dave_chess", WIDTH, HEIGHT, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}