package es.uned.lsi.eped.pract2025_2026;


import es.uned.lsi.eped.DataStructures.*;


public class Seq_PSF {

    private SequenceIF<Pair_S_F> seqStringFreq;
    private List<Pair_S_F> seqStringFreqList = new List<Pair_S_F>();

    public Seq_PSF() {
        seqStringFreq = new List<Pair_S_F>();
    }

    /* Devuelve un iterador de la secuencia */
    public IteratorIF<Pair_S_F> iterator() {
        return seqStringFreq.iterator();
    }
    
    /* Añade un nuevo par <cadena,frecuencia> a la secuencia */
    public void add(Pair_S_F pair) {
        seqStringFreqList.insert(seqStringFreqList.size()+1,pair);
        //La lista auxiliar se copia en la secuencia
        this.seqStringFreq = seqStringFreqList;

    }
}

