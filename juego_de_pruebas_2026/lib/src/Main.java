package es.uned.lsi.eped.pract2025_2026;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import es.uned.lsi.eped.DataStructures.IteratorIF;

public class Main {

    private static final StringBuilder sb = new StringBuilder();
    
    private static void printUsage() {
        System.err.println("Error en los parámetros de entrada:");
        System.err.println("-Primer parámetro: estructura a utilizar (SEQUENCE/TREE)");
        System.err.println("-Segundo parámetro: fichero con las operaciones a realizar sobre el índice");
        System.err.println("-Tercer parámetro: fichero donde volcar la salida de la ejecución");
    }
    
    /* Comprueba que el fichero de entrada exista y puede ser leido */
    public static Boolean checkInput(String file) {
        File f = new File(file);
        // Se comprueba que el fichero existe y es, realmente, un fichero
        if ( !f.exists() || !f.isFile() ) {
            System.out.println("ERROR: no existe el fichero de entrada "+file+".");
            return false;
        }
        // Se comprueba que el fichero puede ser leido
        if ( !f.canRead() ) {
            System.out.println("ERROR: el fichero de entrada "+file+" no puede leerse.");
            return false;
        }
        return true;
    }
    
    /* Comprueba que se puede crear el fichero de salida */
    public static Boolean checkOutput(String file) {
        File f = new File(file);
        f=f.getAbsoluteFile();
        // Se comprueba que la carpeta para escribir el fichero de salida existe
        if ( !f.getParentFile().exists() ) {
            System.out.println("ERROR: no existe la carpeta del fichero de salida "+f.getParent()+".");
            return false;
        }
        // Se comprueba que la carpeta para escribir el fichero de salida tenga permisos de escritura
        if ( !f.getParentFile().canWrite() ) {
            System.out.println("ERROR: no se puede escribir en la carpeta del fichero de salida "+f.getParent()+".");
            return false;
        }
        // Si el fichero de salida existe...
        if ( f.exists() ) {
            // Se comprueba que sea un fichero
            if ( !f.isFile() ) {
                System.out.println("ERROR: la salida "+file+" no es un fichero.");
                return false;
            }
            // Se comprueba que pueda sobreescribirse
            if ( !f.canWrite() ) {
                System.out.println("ERROR: el fichero de salida "+file+" no puede sobreescribirse");
                return false;
            }
        }
        return true;
    }
    
    /*Convierte un iterador en cadena de caracteres
     * @param it: iterador
     */
    public static <E> String toString(IteratorIF<E> it){
        StringBuilder result = new StringBuilder();
        result.append('[');
        while ( it.hasNext() ){
            result.append(it.getNext().toString());
            if( it.hasNext() ){
                result.append(" ");
            }
        }
        result.append(']');
        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        
    if ( args.length != 3 ) {
            printUsage();
            return;
        }

        /* Implementacion Índice. Posibles valores: 
         *   - SEQUENCE 
         *   - BTREE
         */
        String typeIndex = args[0]; 
        IndexIF index;
        switch (typeIndex) {
        case "SEQUENCE":
            // Índice basado en secuencias
            index = new IndexSequence();
            break;
        case "TREE":
            // Índice basado en arboles
            index = new IndexTree();
            break;
        default:
            System.out.println("ERROR: el primer argumento debe ser SEQUENCE o TREE.");
            return;
        }

        // Fichero de entrada
        String input = args[1]; 
        if ( !checkInput(input) ) { return; }    
        
        // Fichero de salida
        String output = args[2];
        if ( !checkOutput(output) ) { return; }

        // Salto de linea
        String lineFeed = System.getProperty("line.separator");
        // Se abre la lectura del fichero de entrada
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(input), "utf-8"));        
        // Se abre la escritura en el fichero de salida
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "utf-8"));
        
        // Calculo porcentajes
        Path path = Paths.get(input);
        long max = Files.lines(path).count();
        double percentage = 0;
        double old_percentage = -2;
        long count = 1;

        String line;
        String result = "";
        long t0 = System.currentTimeMillis();
        while ((line = br.readLine())!=null) {
            // Cada linea del fichero de entrada es una operacion sobre el stock.
            // Separa la linea en tokens por espacios en blancos
            StringTokenizer st = new StringTokenizer(line, " ");
            // El primer token contiene la operacion (documento/palabra/listado)
            String operation = st.nextToken().toLowerCase();
            // Ejecucion de operaciones
            switch(operation){
                // Operacion de añadir un documento al índice
                case "documento":
                    // El segundo token contiene el id del documento
                    String doc_id = st.nextToken().toLowerCase();
                    // El resto de tokens representan las palabras del documento
                    // Creamos un objeto de procesado de documento
                    ProcessDoc docWords = new ProcessDoc(doc_id);
                    // Y añadimos todas las palabras al mismo, contando así su frecuencia
                    while ( st.hasMoreElements() ) {
                        String word = st.nextToken().toLowerCase();
                        docWords.addWord(word);
                    }
                    IteratorIF<Pair_S_F> it = docWords.getSequence().iterator();
                    while ( it.hasNext() ) {
                        Pair_S_F sinf = it.getNext();
                        index.insertIndex(sinf.getString(),doc_id,sinf.getFrequency());
                    }
                    result = "Índice actualizado con todas las palabras del documento.";
                    break;

                // Operación de consulta de una palabra del índice
                case "palabra":
                    // El segundo token contiene la palabra a buscar
                    String word = st.nextToken().toLowerCase();
                    Seq_PSF dis = index.retrieveIndex(word);
                    result = toString(dis.iterator());
                    break;
                // Operacion de listado
                case "listado":
                    // El segundo token contiene el prefijo a partir del cual listar
                    // Si no hay segundo token, se considera la cadena vacía como prefijo
                    String prefix;
                    if ( st.hasMoreTokens() ) prefix = st.nextToken().toLowerCase();
                    else prefix = "";
                    // No tiene parámetros adicionales
                    result = toString(index.prefixIterator(prefix));
                    break;
            }
            // Se escribe la salida en el fichero de salida 
            bw.write(line+": ");
            bw.write(result);
            bw.write(lineFeed);
            
            // Impresion porcentaje trabajo
            percentage = (100*count)/max;
            if(percentage - old_percentage >= 2) {
                sb.setLength(0);
                for (int j = 0 ; j < percentage/2; j++) {
                    sb.append("#");
                }
                System.out.print("[" + String.format("%-50s", sb.toString()) + "] " +  percentage + "%");
                System.out.print("\r");
                old_percentage = percentage;
            }
            count++;

        } 

        System.out.println();
        long t1 = System.currentTimeMillis() - t0;
        // Muestra por consola el tiempo de ejecucion en milisegundos
        System.out.println(t1+" ms");
        // Se cierra el buffer de lectura
        br.close();
        // Se cierra el buffer de escritura
        bw.close();
    }
}
