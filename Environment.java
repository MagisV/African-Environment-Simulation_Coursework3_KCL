public class Environment{

    private String name;
    private int startCol;
    private int endCol;

    private int totalMutation;



    public Environment(String name, int startCol, int endCol) {
        this.name = name;
        this.startCol = startCol;
        this.endCol = endCol;
        totalMutation = 0;
    }

    public String getName(){
        return name;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndCol() {
        return endCol;
    }

    public void addMutation(int mutation) {
        totalMutation+=mutation;
    }
}
