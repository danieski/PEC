package es.uned.lsi.eped.pract2025_2026;

public class NodeInfo extends Node {

    private Seq_PSF seq_PSR;
    
    public NodeInfo(Pair_S_F dp) {
        this.seq_PSR = new Seq_PSF();
        this.seq_PSR.add(dp);
    }
    
    public NodeType getNodeType() {
        return Node.NodeType.INFO;
    }

    public Seq_PSF getSeqPSR() {
        return this.seq_PSR;
    }
    
    public void add(Pair_S_F dp) {
        this.seq_PSR.add(dp);
    }
}
