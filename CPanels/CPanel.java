package CPanels;


import Nodes.MapNode;
import Main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CPanel extends JPanel {
    public static List<List<MapNode>> Array;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;


        for (List<MapNode> mapNodes : Array) {
            for (MapNode mapNode : mapNodes) {

                switch (mapNode.cordZ) {
                    case 1 -> {
                        switch (mapNode.variant) {
                            case 0 -> g2D.setColor(new Color(128, 201, 4));
                            case 1 -> g2D.setColor(new Color(115, 181, 4));
                            case 2 -> g2D.setColor(new Color(102, 161, 3));
                            case 3 -> g2D.setColor(new Color(90, 141, 3));
                            case 4 -> g2D.setColor(new Color(77, 121, 2));
                        }
                    }
                    case 2 -> g2D.setColor(Color.BLACK);
                    default -> this.setBackground(Color.CYAN);
                }


                g2D.fillRect(mapNode.cordX, mapNode.cordY, MapNode.nodeWidth, MapNode.nodeHeight);
                try {
                    Main.walkerList.get(0).Render(g2D);
                }
                catch (Exception ignored) {

                }
                g2D.setColor(Color.RED);
            }
        }
    }

}
