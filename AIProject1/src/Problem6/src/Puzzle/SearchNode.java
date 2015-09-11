package Problem6.src.Puzzle;


public class SearchNode {
    private State currentState;
    private SearchNode parent;
    private int cost;
    private double hCost;
    private double fCost;
    public SearchNode(State currentState)
    {
        this.currentState = currentState;
        parent = null;
        cost = 0;
        hCost = 0;
        fCost = 0;
    }

    public SearchNode(State currentState, SearchNode parent, int cost, double heuristic)
    {
        this.currentState = currentState;
        this.parent = parent;
        this.cost = cost;
        this.hCost = heuristic;
        fCost = this.cost + this.hCost;
    }

    public double gethCost()
    {
        return hCost;
    }

    public  double getfCost()
    {
        return fCost;
    }

    public int getCost()
    {
        return cost;
    }

    public State getCurrentState()
    {
        return this.currentState;
    }

    public SearchNode getParent()
    {
        return parent;
    }
}
