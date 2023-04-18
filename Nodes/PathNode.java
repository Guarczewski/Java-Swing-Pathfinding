package Nodes;

import Main.Main;

import java.util.List;

public class PathNode implements Comparable<PathNode> {
    public int cordX;
    public int cordY;
    int sCost;
    public int hCost;
    int fCost;
    public boolean walkable;
    public boolean open;
    PathNode parentPathNode;
    public PathNode(int cordX, int cordY){
        this.cordX = cordX;
        this.cordY = cordY;
        ResetNode();
    }

    public void ResetNode(){
        open = true;
        walkable = true;
        parentPathNode = null;
    }

    public void EvaluateCosts(PathNode start, PathNode goal){
        open = false;
        double sCostCalc = Math.sqrt(Math.pow(start.cordX - cordX, 2) + Math.pow(start.cordY - cordY, 2));
        double hCostCalc = Math.sqrt(Math.pow(goal.cordX - cordX, 2) + Math.pow(goal.cordY - cordY, 2));
        double fCostCalc = sCostCalc + hCostCalc;
        sCost = (int) sCostCalc;
        hCost = (int) hCostCalc;
        fCost = (int) fCostCalc;
    }

    @Override
    public int compareTo(PathNode pathNode) {
        return fCost - pathNode.fCost;
    }

    public void SetParent(PathNode parent) {
        this.parentPathNode = parent;
    }


    public String toString(){
        return " cordX: " + cordX + " cordY: " + cordY;
    }

    public void GetParent(List<PathNode> CorrectPath) {
        Main.Array.get(cordX).get(cordY).cordZ = 5;
        CorrectPath.add(this);

        if (parentPathNode != null)
            parentPathNode.GetParent(CorrectPath);

    }

}
