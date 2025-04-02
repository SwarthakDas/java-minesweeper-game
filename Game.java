import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.lang.Math;

public class Game{
    public static int mines;
    public static int rowSize;
    public static int colSize;

    //result board generator function
    public static char[][] getBoard(char board[][],int r,int c){ 
        for(int n=0;n<mines;n++){
            int x=(int)(Math.random()*(board.length-1))+1;
            int y=(int)(Math.random()*(board[0].length-1))+1;
            while((x==r && y==c) || board[x][y]=='x'){ //re running random if its the starter cell or already mine present
                x=(int)(Math.random()*(board.length-1))+1;
                y=(int)(Math.random()*(board[0].length-1))+1;
            }

            board[x][y]='x';
            for(int i=x-1; i<=x+1; i++){
                for(int j=y-1; j<=y+1; j++){
                    if (i >= 1 && i < board.length && j >= 1 && j < board[0].length && board[i][j] != 'x') {
                        board[i][j] = Character.forDigit((board[i][j]-'0')+1, 10);
                    }
                }
            }
        }
        return board;
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Welcome to Minesweeper");
        System.out.print("Enter number of rows: ");
        rowSize=sc.nextInt();
        System.out.print("Enter number of columns: ");
        colSize=sc.nextInt();
        System.out.print("Enter number of mines: ");
        mines=sc.nextInt();
        while(mines>=(rowSize-1)*(colSize-1)){
            System.out.println("Invalid number of mines");
            System.out.print("Enter number of mines: ");
            mines=sc.nextInt();
        }
        char board[][]=new char[rowSize][colSize];
        char res[][]=new char[rowSize][colSize];

        //initializing board
        for(int i=0;i<=board.length-1;i++){
            for(int j=0;j<=board[0].length-1;j++){
                if(i==0){
                    System.out.print((j)+" ");
                    board[i][j]=Character.forDigit(j, 10);
                    res[i][j]=Character.forDigit(j, 10);
                }
                else if(j==0){
                    System.out.print((i)+" ");
                    board[i][j]=Character.forDigit(i, 10);
                    res[i][j]=Character.forDigit(i, 10);
                }
                else {
                    System.out.print("B ");
                    board[i][j]='B';
                    res[i][j]='0';
                }
            }
            System.out.println();
        }

        //starter cell
        System.out.println("Enter row and column to start:");
        int row=sc.nextInt();
        int col=sc.nextInt();
        while(row>=rowSize || col>=colSize){
            System.out.println("Out of bounds");
            System.out.println("Enter row and column to start: ");
            row=sc.nextInt();
            col=sc.nextInt();
        }
        res=getBoard(res,row,col);
        board[row][col]=res[row][col];

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});
        
        boolean[][] visited = new boolean[board.length][board[0].length];
        visited[row][col] = true;

        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1];
            board[x][y] = res[x][y];
            if (res[x][y] == '0') { // Expand only if it's a '0' cell
                for (int i = 0; i < 8; i++) {
                    int nx = x + dx[i], ny = y + dy[i];
                    if (nx >= 1 && nx < board.length && ny >= 1 && ny < board[0].length && !visited[nx][ny]) {
                        queue.add(new int[]{nx, ny});
                        visited[nx][ny] = true;
                    }
                }
            }
        }

        //main loop starts
        int count=((rowSize-1)*(colSize-1))-1; //count to check if count==no. of mines? if yes then user wins

        while(true){
            //showing user board on every run
            for(int i=0;i<=board.length-1;i++){
                for(int j=0;j<=board[0].length-1;j++){
                    if(i==0){
                        System.out.print((j)+" ");
                    }
                    else if(j==0){
                        System.out.print((i)+" ");
                    }
                    else System.out.print(board[i][j]+" ");
                }
                System.out.println();
            }

            //winning check
            if(count==mines){
                System.out.println("Congratulations! You win...");
                for(int i=0;i<=board.length-1;i++){
                    for(int j=0;j<=board[0].length-1;j++){
                        if(i==0){
                            System.out.print((j)+" ");
                        }
                        else if(j==0){
                            System.out.print((i)+" ");
                        }
                        else System.out.print(res[i][j]+" ");
                    }
                    System.out.println();
                }
                break;
            }

            //user cell choice
            System.out.println("Enter row and column: ");
            row=sc.nextInt();
            col=sc.nextInt();
            while(row>=rowSize || col>=colSize){
                System.out.println("Out of bounds");
                System.out.println("Enter row and column: ");
                row=sc.nextInt();
                col=sc.nextInt();
            }

            //calculations
            if(res[row][col]!='0' && res[row][col]!='x'){ //when digits more than 0
                board[row][col]=res[row][col];
                count--;
            }
            else if(res[row][col]=='x'){ //when mine triggered
                board[row][col]=res[row][col];
                System.out.println("Game over");
                break;
            }
            else { // when 0 or blank cell clicked
                queue.add(new int[]{row, col});
                visited[row][col] = true;

                while (!queue.isEmpty()) {
                    int[] cell = queue.poll();
                    int x = cell[0], y = cell[1];

                    board[x][y] = res[x][y];
                    count--;
                    if (res[x][y] == '0') { // Expand only if it's a '0' cell
                        for (int i = 0; i < 8; i++) {
                            int nx = x + dx[i], ny = y + dy[i];
                            if (nx >= 1 && nx < board.length && ny >= 1 && ny < board[0].length && !visited[nx][ny]) {
                                queue.add(new int[]{nx, ny});
                                visited[nx][ny] = true;
                            }
                        }
                    }
                }
            }
        }
        sc.close();
    }
}