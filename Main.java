
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Main {

   private static String GetInput() {
      try {
         File myObj = new File("input.txt");

         Scanner myReader = new Scanner(myObj);

         String data = "";

         while (myReader.hasNextLine()) {
            data += myReader.nextLine() + "\n";
         }

         myReader.close();

         return data;
      } catch (FileNotFoundException e) {
         e.printStackTrace();

         return "";
      }
   }

   public static void main(String[] args) {
      String[] inputRows = GetInput().split("\n");


      if (inputRows.length>0){
         Boolean directed = inputRows[0].equals("S"); // Lida com a primeira linha, informa se o grafo é direcionado ('S') ou não ('N')
         Integer vertexCount = Integer.parseInt(inputRows[1]); // Lida com a segunda linha, informa a quantidade de vértices do grafo
         Vector<String> columns = new Vector<String>(vertexCount); // Vetor de colunas
         Vector<String> rows = new Vector<String>(vertexCount); // Vetor de linhas
         Integer matrix[][] = new Integer[vertexCount][vertexCount]; // Matriz
         Vector<String> list = new Vector<String>();
         double connections[][] = new double [vertexCount][vertexCount];

         for (int rowIndex=2; rowIndex<inputRows.length; rowIndex++) {
            if (!inputRows[rowIndex].contains(",")) {
               columns.add(inputRows[rowIndex]);
               rows.add(inputRows[rowIndex]);
            } else {
               String[] rowValues = inputRows[rowIndex].split(", ");

               Integer matrixColumnI = columns.indexOf(rowValues[1]);
               Integer matrixRowI = rows.indexOf(rowValues[0]);

               matrix[matrixRowI][matrixColumnI] = Integer.parseInt(rowValues[2]);

               if (!directed) {
                  matrix[matrixColumnI][matrixRowI] = Integer.parseInt(rowValues[2]);
               }

               list.add("from (" + rowValues[0] + ")" + " [to (" + rowValues[1] + "), " + rowValues[2] + "]");
            }
         }

         for (int i=0;i<vertexCount;i++) {
            for (int j=0;j<vertexCount;j++) {
               if (matrix[i][j] != null) {
                  connections[i][j] = matrix[i][j];
               } else {
                  connections[i][j] = 0;
               }
            }
            dijkstraAlgorithm(0,i,connections);
            System.out.print("\n");
         }
      }
   }

   private static void dijkstraAlgorithm(int startingPoint, int destination, double connections[][]) {

      double [][] distances = new double [connections.length][3];
      for (int i = 0;i<distances.length;i++) {
         distances[i][0] = Double.POSITIVE_INFINITY;
      }
      int currentNode = startingPoint;
      distances[currentNode][0]=0;
      int count =0;

      while (count<connections.length) {

         for(int i = 0;i<connections.length;i++) {
            if ((connections[currentNode][i]>0) && (connections[currentNode][i]<Double.POSITIVE_INFINITY)) {
               if ((distances[currentNode][0]+connections[currentNode][i])<distances[i][0]) {
                  distances[i][0] = distances[currentNode][0]+connections[currentNode][i];
                  distances[i][1] = currentNode;
               }
               distances[currentNode][2]=1;
            }
         }

         count +=1;
         currentNode = nextNode(connections, distances);
      }


      System.out.println("Start: " + startingPoint);
      System.out.println("Destination: " + destination);
      System.out.print("Path: ");
      showPath(startingPoint, destination, distances);
      System.out.println("\nDistance: " + distances[destination][0]);

   }


   static int nextNode(double connections[][], double distances[][]) {
      double minDistance = Double.POSITIVE_INFINITY;
      int nearestNode = Integer.MAX_VALUE ;
      for(int i = 0;i<connections.length;i++) {
         if ((distances[i][0]<minDistance && distances[i][2]!=1)) {
            minDistance = distances[i][0];
            nearestNode = i;
         }
      }
      return nearestNode;
   }

   static void showPath(int startingPoint, int destination,double distances[][]){
      if(destination!=startingPoint){
         showPath(startingPoint,(int)distances[destination][1],distances);
         System.out.print(destination + " ");

      } else {
         System.out.print(startingPoint + " ");
      }
   }

}