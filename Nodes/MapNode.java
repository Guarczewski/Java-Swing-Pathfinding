package Nodes;

import Main.Main;

import java.util.Random;
public class MapNode {
    private static final Random random = new Random();
    public final static int nodeWidth = Main.WINDOW_WIDTH / Main.SIZE_WIDTH, nodeHeight = Main.WINDOW_HEIGHT / Main.SIZE_HEIGHT;
    public int cordX, cordY, cordZ;
    public int variant;
    public MapNode(int cordX, int cordY, int cordZ) {
        this.cordX = cordX;
        this.cordY = cordY;
        this.cordZ = cordZ;
        variant = random.nextInt(5);
    }
}
