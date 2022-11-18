import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Main {
   private static String GetInput() {
      try {
         // Lendo arquivo de texto e armazenando valor de cada linha
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
         Boolean directed = inputRows[0].equals("S");                   // Avalia primeira linha, direcionamento (S/N)
         Integer vertexCount = Integer.parseInt(inputRows[1]);          // Avalia segunda linha, qtd de vértices
         Vector<String> columns = new Vector<String>(vertexCount);      // Vetor de colunas
         Vector<String> rows = new Vector<String>(vertexCount);         // Vetor de linhas
         Integer matrix[][] = new Integer[vertexCount][vertexCount];    // Matriz original
         double connections[][] = new double [vertexCount][vertexCount];// Matriz (apenas para conversão double)

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
            }
         }

         // Encontrando o caminho mais curto do vertice 0 para todos os outros
         for (int i=0;i<vertexCount;i++) {
            for (int j=0;j<vertexCount;j++) {
               if (matrix[i][j] != null) {
                  // Alimentando a matriz de conexões com a matriz de input (convertendo de Integer para double)
                  connections[i][j] = matrix[i][j];
               } else {
                  // Se for nulo, então recebe infinito
                  connections[i][j] = Double.POSITIVE_INFINITY;
               }
            }
            // Calculando Djikstra para cada vértice
            dijkstraAlgorithm(0,i,connections);
            System.out.print("\n");
         }
      }
   }

   // Método Djikstra recebe o vertice inicial, destino e a matriz de conexões
   private static void dijkstraAlgorithm(int startingPoint, int destination, double connections[][]) {
      // Armazenando distancias em uma matriz secundaria
      double [][] distances = new double [connections.length][3];
      for (int i = 0;i<distances.length;i++) {
         distances[i][0] = Double.POSITIVE_INFINITY;
      }

      // Armazendo vértice atual
      int currentNode = startingPoint;

      // Flegando que ainda não visitamos o vértice atual
      distances[currentNode][0]=0;

      int count =0;

      while (count<connections.length) {

         for(int i = 0;i<connections.length;i++) {
            if ((connections[currentNode][i]>0) && (connections[currentNode][i]<Double.POSITIVE_INFINITY)) {
               if ((distances[currentNode][0]+connections[currentNode][i])<distances[i][0]) {
                  distances[i][0] = distances[currentNode][0]+connections[currentNode][i];
                  // Salvando a distancia do vertice anterior [i]
                  distances[i][1] = currentNode;
               }
               // Flegando que já visitamos esse vértice
               distances[currentNode][2]=1;
            }
         }

         count +=1;
         currentNode = nextNode(connections, distances);
      }


      System.out.println("Inicio: " + startingPoint);
      System.out.println("Destino: " + destination);
      System.out.print("Caminho: ");
      showPath(startingPoint, destination, distances);
      System.out.println("\nDistancia: " + distances[destination][0]);
   }

   // Retorna o valor do vértice que ainda não foi visitado e tem a menor distância do vértice inicial
   static int nextNode(double connections[][], double distances[][]) {
      double minDistance = Double.POSITIVE_INFINITY;
      int nearestNode = Integer.MAX_VALUE ;
      for(int i = 0;i<connections.length;i++) {
         // Validando a distancia e se ele ja foi visitado
         if ((distances[i][0]<minDistance && distances[i][2]!=1)) {
            minDistance = distances[i][0];
            nearestNode = i;
         }
      }
      return nearestNode;
   }

   // Método recursivo, que chama a si mesmo até chegar ao vétice destino com base no vértice anterior,
   // desde que não seja o vértice inicial.
   static void showPath(int startingPoint, int destination,double distances[][]){
      if(destination!=startingPoint){
         // Para obter a ordem do início ao destino, primeiro chamamos o método e, em seguida, imprimimos o número
         // do vértice atual, de modo que o vértice que usamos por último seja impresso primeiro.
         showPath(startingPoint,(int)distances[destination][1],distances);
         System.out.print(destination + " ");
      } else {
         System.out.print(startingPoint + " ");
      }
   }
}