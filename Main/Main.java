package Main;

import CPanels.CPanel;
import Nodes.MapNode;
import Nodes.PathNode;
import Other.Walker;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class Main extends JFrame implements  MouseMotionListener, KeyListener {

    public static int currentMapX, currentMapY, mouseCordX, mouseCordY, lastMouseCordX, lastMouseCordY;
    public static final int SIZE_WIDTH = 100, SIZE_HEIGHT = 50, WINDOW_WIDTH = 2500, WINDOW_HEIGHT = 1250;
    static final int AMOUNT_OF_TICKS = 120, AMOUNT_OF_FRAMES = 120;
    public static boolean DIAGONAL = false;
    public static List<List <MapNode>> Array;
    public static CPanel cPanel;
    public static Main main;
    public static List <List<PathNode>> Nodes;
    public static List<Walker> walkerList;
    Main(){
        super("Shit!");
        setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        cPanel = new CPanel();
        cPanel.addMouseMotionListener(this);
        Nodes = new ArrayList<>();
        Array = new ArrayList<>();
        addKeyListener(this);
        for (int i = 0; i < SIZE_WIDTH; i++) {
            List<PathNode> tempPathNodeList = new ArrayList<>();
            List<MapNode> tempMapNodeList = new ArrayList<>();
            for (int j = 0; j < SIZE_HEIGHT; j++) {
                tempPathNodeList.add(new PathNode(i,j));
                tempMapNodeList.add(new MapNode(i * MapNode.nodeWidth,j * MapNode.nodeHeight,1));
            }
            Nodes.add(tempPathNodeList);
            Array.add(tempMapNodeList);
        }

        CPanel.Array = Array;

        setContentPane(cPanel);
        setVisible(true);
    }
    public static void main(String[] args) {
        main = new Main();

        walkerList = new ArrayList<>();
        walkerList.add(new Walker(Nodes,new PathNode(5,15), new PathNode(SIZE_WIDTH - 1, SIZE_HEIGHT - 1)));

        StartUpdating();
    }

    private static void StartUpdating(){
        new Thread(() -> {
            // FPS Stuff
            long lastTime = System.nanoTime();
            double nsRender = 1000000000.0 / AMOUNT_OF_FRAMES;
            double nsUpdate = 1000000000.0 / AMOUNT_OF_TICKS;
            double deltaRender = 0;
            double deltaUpdate = 0;
            int updates = 0;
            int frames = 0;
            long timer = System.currentTimeMillis();

            while(true) {
                long now = System.nanoTime();

                deltaRender += (now - lastTime) / nsRender;
                deltaUpdate += (now - lastTime) / nsUpdate;
                lastTime = now;

                if(deltaRender >= 1) {
                    deltaRender--;
                    cPanel.repaint();
                    frames++;
                }

                if (deltaUpdate >= 1) {
                    deltaUpdate--;
                    updates++;
                }

                if(System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    main.setTitle(" Ticks: " + updates + ", FPS: " + frames);
                    updates = 0;
                    frames = 0;
                }
            }
        }).start();
    }

    public static void ChangeNodeState(){
        Nodes.get(mouseCordX).get(mouseCordY).walkable = !Nodes.get(mouseCordX).get(mouseCordY).walkable;

        if (Nodes.get(mouseCordX).get(mouseCordY).walkable) {
            Array.get(mouseCordX).get(mouseCordY).cordZ = 1;
        }
        else {
            Array.get(mouseCordX).get(mouseCordY).cordZ = 2;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (mouseCordX != currentMapX || mouseCordY != currentMapY) {
                currentMapX = mouseCordX;
                currentMapY = mouseCordY;

                walkerList.get(0).setStartAndGoal(new PathNode(walkerList.get(0).currentCordX / MapNode.nodeWidth, walkerList.get(0).currentCordY / MapNode.nodeHeight), new PathNode(mouseCordX, mouseCordY));
                walkerList.get(0).Reset();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseCordX = e.getX()/MapNode.nodeWidth;
        mouseCordY = e.getY()/MapNode.nodeHeight;
        try {
            if (lastMouseCordX != mouseCordX || lastMouseCordY != mouseCordY) {
                lastMouseCordX = mouseCordX;
                lastMouseCordY = mouseCordY;
                ChangeNodeState();
            }
        }
        catch (Exception ignored){
            System.out.println("Out of map");
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseCordX = e.getX()/MapNode.nodeWidth;
        mouseCordY = e.getY()/MapNode.nodeHeight;
    }
}