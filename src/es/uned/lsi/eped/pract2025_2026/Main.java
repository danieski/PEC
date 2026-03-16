package es.uned.lsi.eped.pract2025_2026;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.Sequence;

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
        System.out.println("Test");
        Pair_S_F parStringFrecuencia = new Pair_S_F("Dani",1);
        parStringFrecuencia.getString();
        parStringFrecuencia.getFrequency();
        parStringFrecuencia.incFrequency();
        System.out.println(parStringFrecuencia.toString());
        Seq_PSF SequencePairSringFrequency = new Seq_PSF();

        SequencePairSringFrequency.add(parStringFrecuencia);
        System.out.println(SequencePairSringFrequency.size());
        System.out.println(SequencePairSringFrequency.isEmpty());
    }
}
