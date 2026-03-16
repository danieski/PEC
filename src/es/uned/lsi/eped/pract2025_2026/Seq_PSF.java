package es.uned.lsi.eped.pract2025_2026;


import es.uned.lsi.eped.DataStructures.*;


public class Seq_PSF extends Sequence<Pair_S_F> {
    private SequenceIF<Pair_S_F> seqStringFreq;
    
    /* Constructor */
    public Seq_PSF() {
        super();
        //...
    }

    /* Devuelve un iterador de la secuencia */
    public IteratorIF<Pair_S_F> iterator() {
        //...
        return null;
    }
    
    /* Añade un nuevo par <cadena,frecuencia> a la secuencia */
    public void add(Pair_S_F pair) {
        this.firstNode = pair;
        size++;
        //...
    }
}

