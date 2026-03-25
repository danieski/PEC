package es.uned.lsi.eped.pract2025_2026;

/* Representa un nodo */
public abstract class Node {
    public enum NodeType {
        ROOT, INNER, INFO
    }
    
    /* Prescribe un getter que devuelve el tipo de nodo */
    public abstract NodeType getNodeType();
}
