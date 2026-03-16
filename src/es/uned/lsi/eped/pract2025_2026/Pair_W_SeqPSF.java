package es.uned.lsi.eped.pract2025_2026;

/* Representa un par <palabra,secuencia de pares <cadena,frecuencia>> */
public class Pair_W_SeqPSF {

    private String word;
    private Seq_PSF seqPSF;
  
    /* Constructor solo con la palabra */
    public Pair_W_SeqPSF(String p) {
        this.word = p;
        this.seqPSF = new Seq_PSF();
    }
  
    /* Constructor con palabra y secuencia */
    public Pair_W_SeqPSF(String p, Seq_PSF dis) {
        this.word = p;
        this.seqPSF = dis;
    }

    /* Devuelve la palabra del índice */
    public String getWord() {
        return this.word;
    }
  
    /* Devuelve la secuencia de pares <cadena,frecuencia> */
    public Seq_PSF getSeqPSR() {
        return this.seqPSF;
    }
    
    /* Construye una cadena para representar el par */
    public String toString() {
        return "("+this.word+","+Main.toString(this.seqPSF.iterator())+")";
    }
      
    /* Añade un nuevo par doc_id,rep a la secuencia de DocInfo */
    public void add(String doc_id, int freq) {
        this.seqPSF.add(new Pair_S_F(doc_id,freq));
    }
}
