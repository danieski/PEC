package es.uned.lsi.eped.pract2025_2026;

/* Representa un par <cadena,frecuencia> del Índice */
public class Pair_S_F {
    private String string;
    private int frequency;
  
    /* Constructor */
    public Pair_S_F(String string, int freq) {
        this.string = string;
        this.frequency = freq;
    }
  
    /* Devuelve la cadena del índice */
    public String getString() {
        return this.string;
    }
  
    /* Devuelve la frecuencia de la cadena */
    public int getFrequency() {
        return this.frequency;
    }
    
    /* Construye una cadena para representar el par */
    public String toString() {
        return "("+this.string+","+Integer.toString(this.frequency)+")";
    }

    /* Incrementa en uno la frecuencia */
    public void incFrequency( ) {
        this.frequency++;
    }
}
