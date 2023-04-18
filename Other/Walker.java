package Other;

import Nodes.MapNode;
import Nodes.PathNode;
import Main.Main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Walker {
    public final List <List<PathNode>> myNodes;
    private final List <PathNode> myNodesToVisit, correctPath;
    private PathNode myStart, myGoal;
    public int currentCordX, currentCordY, nextCordX, nextCordY;
    private boolean updatedList = false, running = false;

    public Walker(List<List<PathNode>> nodes, PathNode Start, PathNode End) {
        myNodesToVisit = new ArrayList<>();
        myNodes = new ArrayList<>();
        correctPath = new ArrayList<>();

        for (List<PathNode> tempOriginalList : nodes) {
            List<PathNode> tempList = new ArrayList<>();
            for (PathNode tempPathNode : tempOriginalList) {
                tempList.add(new PathNode(tempPathNode.cordX, tempPathNode.cordY));
            }
            myNodes.add(tempList);
        }

        this.myStart = new PathNode(Start.cordX, Start.cordY);
        this.myGoal = new PathNode(End.cordX, End.cordY);
        Reset();
    }

    public void Render(Graphics2D graphics2D){
        graphics2D.setColor(Color.PINK);
        graphics2D.fillOval(currentCordX, currentCordY, MapNode.nodeWidth, MapNode.nodeHeight);
    }

    public void setStartAndGoal(PathNode myStart, PathNode myGoal){
        this.myStart = myStart;
        this.myGoal = myGoal;
    }

    public void Reset(){
        for (int i = 0; i < Main.SIZE_WIDTH; i++) {
            for (int j = 0; j < Main.SIZE_HEIGHT; j++) {
                myNodes.get(i).get(j).ResetNode();
            }
        }


        for (PathNode pathNode : correctPath)
            Main.Array.get(pathNode.cordX).get(pathNode.cordY).cordZ = 1;


        myNodesToVisit.clear();
        myNodesToVisit.add(myNodes.get(myStart.cordX).get(myStart.cordY));
        myNodesToVisit.get(0).EvaluateCosts(myStart,myGoal);
        Start();
    }

    public void Start() {
        new Thread(() -> {
            try {
                while (myNodesToVisit.get(0).hCost != 0) {
                    PathNode temp = myNodesToVisit.get(0);
                    myNodesToVisit.remove(myNodesToVisit.get(0));
                    CheckAround(temp);

                }
                correctPath.clear();
                myNodesToVisit.get(0).GetParent(correctPath);
                updatedList = true;
                Walk();
            }
            catch (Exception ignored) {

            }
        }).start();
    }

    public void Walk(){
        if (!running) {
            running = true;
            new Thread(() -> {
                try {
                    for (int i = correctPath.size() - 1; i >= 0; i--) {
                        if (i != correctPath.size() - 1) {
                            Main.Array.get(correctPath.get(i + 1).cordX).get(correctPath.get(i + 1).cordY).cordZ = 4;
                        }
                        Main.Array.get(correctPath.get(i).cordX).get(correctPath.get(i).cordY).cordZ = 5;

                        currentCordX = correctPath.get(i).cordX * MapNode.nodeWidth;
                        currentCordY = correctPath.get(i).cordY * MapNode.nodeHeight;

                        if (i - 1 >= 0) {
                            nextCordX = correctPath.get(i - 1).cordX * MapNode.nodeWidth;
                            nextCordY = correctPath.get(i - 1).cordY * MapNode.nodeHeight;
                        }

                        // Smooth Walking
                        while (currentCordX != nextCordX || currentCordY != nextCordY) {

                            if (currentCordX < nextCordX)
                                currentCordX++;
                            else if (currentCordX > nextCordX)
                                currentCordX--;

                            if (currentCordY < nextCordY)
                                currentCordY++;
                            else if (currentCordY > nextCordY)
                                currentCordY--;


                            try {
                                Thread.sleep(5);
                            } catch (Exception ignored) {

                            }
                        }

                        if (updatedList) {
                            i = correctPath.size() - 1;
                            updatedList = false;
                        }
                        try {
                            Thread.sleep(20);
                        } catch (Exception ignored) {

                        }
                    }
                    running = false;
                }
                catch (Exception ignored) {
                    running = false;
                }
            }).start();
        }
    }
    public void CheckAround(PathNode Me){
        int myX = Me.cordX;
        int myY = Me.cordY;

        Me.EvaluateCosts(myStart,myGoal);

        if (myX -1 >= 0) {
            if (myNodes.get(myX - 1).get(myY).open && Main.Nodes.get(myX - 1).get(myY).walkable) {
                myNodes.get(myX - 1).get(myY).EvaluateCosts(myStart, myGoal);
                myNodes.get(myX - 1).get(myY).SetParent(Me);
                myNodesToVisit.add(myNodes.get(myX - 1).get(myY));
            }
        }

        if (myY - 1 >= 0){
            if (myNodes.get(myX).get(myY - 1).open && Main.Nodes.get(myX).get(myY - 1).walkable) {
                myNodes.get(myX).get(myY - 1).EvaluateCosts(myStart, myGoal);
                myNodes.get(myX).get(myY - 1).SetParent(Me);
                myNodesToVisit.add(myNodes.get(myX).get(myY - 1));
            }
        }

        if (myX + 1 < Main.SIZE_WIDTH) {
            if (myNodes.get(myX + 1).get(myY).open && Main.Nodes.get(myX + 1).get(myY).walkable) {
                myNodes.get(myX + 1).get(myY).EvaluateCosts(myStart, myGoal);
                myNodes.get(myX + 1).get(myY).SetParent(Me);
                myNodesToVisit.add(myNodes.get(myX + 1).get(myY));
            }
        }

        if (myY + 1 < Main.SIZE_HEIGHT){
            if (myNodes.get(myX).get(myY + 1).open && Main.Nodes.get(myX).get(myY + 1).walkable) {
                myNodes.get(myX).get(myY + 1).EvaluateCosts(myStart, myGoal);
                myNodes.get(myX).get(myY + 1).SetParent(Me);
                myNodesToVisit.add(myNodes.get(myX).get(myY + 1));
            }
        }

        if (Main.DIAGONAL) {

            if (myX - 1 >= 0 && myY - 1 >= 0) {
                if (myNodes.get(myX - 1).get(myY - 1).open && Main.Nodes.get(myX - 1).get(myY - 1).walkable) {
                    myNodes.get(myX - 1).get(myY - 1).EvaluateCosts(myStart, myGoal);
                    myNodes.get(myX - 1).get(myY - 1).SetParent(Me);
                    myNodesToVisit.add(myNodes.get(myX - 1).get(myY - 1));
                }
            }

            if (myX + 1 < Main.SIZE_WIDTH && myY + 1 < Main.SIZE_HEIGHT) {
                if (myNodes.get(myX + 1).get(myY + 1).open && Main.Nodes.get(myX + 1).get(myY + 1).walkable) {
                    myNodes.get(myX + 1).get(myY + 1).EvaluateCosts(myStart, myGoal);
                    myNodes.get(myX + 1).get(myY + 1).SetParent(Me);
                    myNodesToVisit.add(myNodes.get(myX + 1).get(myY + 1));
                }
            }

            if (myX - 1 >= 0 && myY + 1 < Main.SIZE_HEIGHT) {
                if (myNodes.get(myX - 1).get(myY + 1).open && Main.Nodes.get(myX - 1).get(myY + 1).walkable) {
                    myNodes.get(myX - 1).get(myY + 1).EvaluateCosts(myStart, myGoal);
                    myNodes.get(myX - 1).get(myY + 1).SetParent(Me);
                    myNodesToVisit.add(myNodes.get(myX - 1).get(myY + 1));
                }
            }

            if (myX + 1 < Main.SIZE_WIDTH && myY - 1 >= 0) {
                if (myNodes.get(myX + 1).get(myY - 1).open && Main.Nodes.get(myX + 1).get(myY - 1).walkable) {
                    myNodes.get(myX + 1).get(myY - 1).EvaluateCosts(myStart, myGoal);
                    myNodes.get(myX + 1).get(myY - 1).SetParent(Me);
                    myNodesToVisit.add(myNodes.get(myX + 1).get(myY - 1));
                }
            }
        }

        Collections.sort(myNodesToVisit);

    }


}
