package org.argentumonline.server.aStar;import java.util.ArrayList;import java.util.Hashtable;import java.util.List;public class AStar {    double typicalCost; // default one    Hashtable<Location,Node> open;    Hashtable<Location,Node> closed;        int[][] grid;    int[] costs;    Location startLoc, goalLoc;        public AStar(int[][] grid, int costs[], Location startLoc, Location goalLoc) {        this.grid = grid;        this.costs = costs;        this.startLoc = startLoc;        this.goalLoc = goalLoc;        this.typicalCost = getTypicalCost(new Location(0, 0), new Location(grid.length - 1, grid[0].length - 1));        this.open = new Hashtable<Location,Node>(grid.length * grid[0].length);        this.closed = new Hashtable<Location,Node>(grid.length * grid[0].length);    }        private double getTypicalCost(Location startLoc, Location goalLoc) {        int left = Math.min(startLoc.x, goalLoc.x);        int top = Math.min(startLoc.y, goalLoc.y);        int right = Math.max(startLoc.x, goalLoc.x);        int bottom = Math.max(startLoc.y, goalLoc.y);                int count = 0;        int sum = 0;        for(int i = left; i <= right; i++) {            for(int j = top; j <= bottom; j++) {                int value = this.grid[i][j];                if(value != Constants.NOTHING) {                    sum += this.costs[value];                    count++;                }            }        }                return (double)sum / (double)count / 1.1;    }    public List<Node> AStarSearch(Agent agent) {            PQ openQ = new PQ();                // initialize a start node        Node startNode = new Node();        startNode.location = this.startLoc;        startNode.costFromStart = 0;        startNode.costToGoal = pathCostEstimate(this.startLoc, this.goalLoc, agent);        startNode.totalCost = startNode.costFromStart + startNode.costToGoal;        startNode.parent = null;                openQ.add(startNode);        this.open.put(startNode.location, startNode);                // process the list until success or failure        while(openQ.size() > 0) {                    Node node = openQ.pop();            this.open.remove(node.location);                        // if at a goal, we're done            if(node.location.equals(this.goalLoc)) {                return solve(node);            }             List<Node> neighbors = getNeighbors(node);            for(int i = 0; i < neighbors.size(); i++) {                Node newNode = neighbors.get(i);                double newCostEstimate = pathCostEstimate(newNode.location, this.goalLoc, agent);                double newCost = node.costFromStart + traverseCost(node, newNode, agent);                double newTotal = newCost + newCostEstimate;                                Location nnLoc = newNode.location;                Node holderO, holderC;                holderO = this.open.get(nnLoc);                holderC = this.closed.get(nnLoc);                if(holderO != null && holderO.totalCost <= newTotal) {                    continue;                } else if(holderC != null && holderC.totalCost <= newTotal) {                    continue;                } else {                    // store the new or improved info                    newNode.parent = node;                    newNode.costFromStart = newCost;                    newNode.costToGoal = newCostEstimate;                    newNode.totalCost = newNode.costFromStart + newNode.costToGoal;                    if(this.closed.get(nnLoc) != null) {                        this.closed.remove(nnLoc);                    }                    Node check = this.open.get(nnLoc);                    if(check != null) {                        openQ.remove(check);                        this.open.remove(nnLoc);                    }                    openQ.add(newNode);                    this.open.put(nnLoc, newNode);                                    } // now done with node            }            this.closed.put(node.location, node);        }        return null; // failure    }        private List<Node> getNeighbors(Node node) {        Location nodeLoc = node.location;        List<Node> neighbors = new ArrayList<Node>();        addConditional(neighbors, nodeLoc, -1, 0);        addConditional(neighbors, nodeLoc, 0, -1);        addConditional(neighbors, nodeLoc, 0, 1);        addConditional(neighbors, nodeLoc, 1, 0);        /* Don't use corners        addConditional(neighbors, nodeLoc, 1, 1);        addConditional(neighbors, nodeLoc, -1, -1);        addConditional(neighbors, nodeLoc, -1, 1);        addConditional(neighbors, nodeLoc, 1, -1);        */        return neighbors;    }        private void addConditional(List<Node> addTo, Location loc, int x, int y) {        int newX = loc.x + x, newY = loc.y + y;        if(newX < 0 || newX >= this.grid.length) {            return;        }        if(newY < 0 || newY >= this.grid[0].length) {            return;        }        if(this.grid[newX][newY] == Constants.SOLID) {            return;        }                Node newNode = new Node();        newNode.location = new Location(newX, newY);        addTo.add(newNode);    }        private double pathCostEstimate(Location start, Location goal, Agent agent) {        if(agent == null ) { // default agent            int dx = Math.abs(goal.x - start.x);            int dy = Math.abs(goal.y - start.y);            //double diff = Math.abs(dx - dy);            return this.typicalCost * (dx + dy);        }         return 1;    }        private double traverseCost(Node node, Node newNode, Agent agent) {        if(agent == null ) { // default agent            Location loc1 = node.location, loc2 = newNode.location;            int dx = Math.abs(loc1.x - loc2.x), dy = Math.abs(loc1.y - loc2.y);            return this.costs[this.grid[newNode.location.x][newNode.location.y]] + 0.1 * (dx + dy - 1);        }        return 1;    }        private List<Node> solve(Node node) {        List<Node> solution = new ArrayList<Node>();                solution.add(node);        while(node.parent != null) {            solution.add(0, node.parent);            node = node.parent;        }                return solution;    }}                