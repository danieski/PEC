package es.uned.lsi.eped.pract2025_2026;
import es.uned.lsi.eped.DataStructures.GTreeIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;

public class IndexTree implements IndexIF {

    protected GTreeIF<Node> index;


    @Override
    public Seq_PSF retrieveIndex(String p) {
        return null;
    }

    @Override
    public void insertIndex(String p, String doc_id, int freq) {

    }

    @Override
    public IteratorIF<Pair_W_SeqPSF> prefixIterator(String prefix) {
        return null;
    }
}
