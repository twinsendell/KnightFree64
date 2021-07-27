import java.util.ArrayList;
import java.util.Random;

//Class of methods to call from the main class
public class Methods {

    //This method generates the sequence for the horse jumps from all the cells in a chessboard
    public void generateSequence(){
        ArrayList<ArrayList<Integer>> positionList = new ArrayList<ArrayList<Integer>>();
        //read list to save the positions the knight already jumped to
        ArrayList<Integer> read = new ArrayList<Integer>();
        Random rand = new Random();
        boolean sequenceCompleted = false;
        positionList = fillPositionList(positionList);
        for(int x = 0; x < 64; x++){
            int positionNow;
            positionNow = x;
            read.clear();
            read.add(x);
            sequenceCompleted = false;
            int count = 0;
            do{ 
                //arbirary number to iterate a billion sequences, as it's almost certain for an existing sequence to be found before
                if(count > 999999999){
                    read.clear();
                    read.add(x);
                    count = 0;
                    break;                    
                }
                do{
                    //validaton to check if all the positions have been read to break the loop and extract the correct sequence
                    if (read.size() == positionList.size()){
                        sequenceCompleted = true;
                        break;
                    }
                    //validation to check if the current position has no possible jumps to restart the sequence
                    if(validateAllRead(positionList.get(positionNow), read)){
                        read.clear();
                        positionNow = x;
                        read.add(x);
                        break;
                    }
                    //loop of making a random jump
                    do{
                        int positionJump;
                        positionJump = positionList.get(positionNow).get(rand.nextInt(positionList.get(positionNow).size()));
                        //validation if the random next possible jump has already been done to skip and randomize again
                        if(validateRead(positionJump, read)){
                            continue;
                        }else{
                            positionNow = positionJump;
                            read.add(positionJump);
                            break;
                        }
                    }while(true);
                } while(true);
                count++;
            }while (!sequenceCompleted);
            System.out.println(parseSequence(read));
        }
    }

    /**this method fills the positions list which contains all the positions that the knight can jump to from each and every cell,
     * meaning it contains 64 lists of positions representing the 64 cells in a chessboard,
     * this is done by emulating the chessboard and the 8 possible knight jumps in a matrix
     * and asking if the reached position exists or if its out of bounds
    */
    private ArrayList<ArrayList<Integer>> fillPositionList(ArrayList<ArrayList<Integer>> positionList){
        int[][] matrix = new int[8][8];
        matrix = createMatrix(matrix);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++){
                positionList.add(createIntList(matrix, y, x));
            }
        }
    return positionList;    
    }
    
    //This method creates new instances of position lists to save in the list that contains all the position lists
    private ArrayList<Integer> createIntList(int[][] matrix, int y, int x){
        ArrayList<Integer> positions = new ArrayList<Integer>();
        positions = fillIntLists(positions, matrix, y, x, 2, 1);
        positions = fillIntLists(positions, matrix, y, x, 2, -1);
        positions = fillIntLists(positions, matrix, y, x, -2, 1);
        positions = fillIntLists(positions, matrix, y, x, -2, -1);
        positions = fillIntLists(positions, matrix, y, x, 1, 2);
        positions = fillIntLists(positions, matrix, y, x, 1, -2);
        positions = fillIntLists(positions, matrix, y, x, -1, 2);
        positions = fillIntLists(positions, matrix, y, x, -1, -2);                     
        return positions;
    }

    // this method fills the positions list of knight jumps from each individual cell
    private ArrayList<Integer> fillIntLists(ArrayList<Integer> positions, int[][] matrix, int y, int x, int a, int b){
        try {
            positions.add(matrix[y+a][x+b]);
        } catch (Exception e) {
            return positions;
        }
        return positions;       
    }

    //this method creates a matrix representing the chessboard cells as numbers from 0 to 63
    private int[][] createMatrix(int[][] matrix){
        int count = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++){
                matrix[y][x] = count;
                count++;
            }
        }
        return matrix;
    }

    //this method validates the read list against the list of possible jumps to restar the jumping cycle in case of no possible jumps
    private boolean validateAllRead(ArrayList<Integer> positions, ArrayList<Integer> read){
        boolean allMatch = true;
        for (Integer position: positions){
            if(!validateRead(position, read)){
                allMatch = false;
                break;
            }            
        }
        return allMatch;
    }

    //This method validates the read list against the next jump position to avoid repeating jumps
    private boolean validateRead(int position, ArrayList<Integer> read){
        boolean repeat = false;
        for (Integer readPosition: read){
            if(position == readPosition){
                repeat = true;
                break;
            }        
        }        
        return repeat;
    }

    //This method parses the board number sequence into proper chess coordinates
    private String parseSequence(ArrayList<Integer> read){
        String sequence = "";
        if (read.size() == 1){
            sequence = "Can't find a sequence for " +(char)(((read.get(0))%8)+65)+parseRow(read.get(0));
            return sequence;
        }
        for (int x : read){
            sequence += (char)((x%8)+65);
            sequence += parseRow(x) + " ";
        }
        return sequence;
    }

    //This method parses the row by asking if it's in a range of 8 numbers according to the order on the numbered board
    private String parseRow(int x){
        String row = "";
        int a = -1, b = 8;
        for (int i = 8; i>0; i--){
            if(a<x && x<b){
                row += i;
                break;
            }
            a += 8;
            b += 8;
        } 
        return row;
    }


}