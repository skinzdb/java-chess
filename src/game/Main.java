package game;

import engine.GameEngine;
import engine.IGameLogic;
 
public class Main {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 480;
	
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new Game();
            GameEngine gameEng = new GameEngine("GAME", WIDTH, HEIGHT, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}