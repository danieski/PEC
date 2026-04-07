package es.uned.lsi.eped.pract2025_2026;

import es.uned.lsi.eped.DataStructures.GTree;
import es.uned.lsi.eped.DataStructures.GTreeIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;

public class IndexTree implements IndexIF {

    protected GTreeIF<Node> index;

    public IndexTree(){
        //No sabemos si podmemos añadir constructores a la clase
        index = new GTree<Node>();
        index.setRoot(new NodeRoot());
    }

    @Override
    public Seq_PSF retrieveIndex(String p) {
        return null;
    }

    @Override
    public void insertIndex(String p, String doc_id, int freq) {
        char[] arrayPalabra = p.toCharArray();

        //Se comprueba si la primera letra coincide con alguno de los hijos de root,
        //De ser así recorremos recusivamente hasta que no coincida ahi añadiremos un hijo para completar la palabra

        //La p se va seccionar en caracteres y se va a ir guardando linealmente descendente en el arbol
        //finalmente en la hoja insertamos un nodo info con la sequencia
        GTreeIF<Node> root = index.getChild(1);

        GTree childBranch = new GTree<Node>();
        childBranch.setRoot(new NodeInner(p.charAt(0)));
        root.addChild(root.getNumChildren()+1,childBranch);

        for(int i = 0; i<arrayPalabra.length;i++){
            GTree childBranch1 = new GTree<Node>();
            childBranch1.setRoot(new NodeInner(p.charAt(i)));
        }

        index.addChild(1, childBranch);

    }

    @Override
    public IteratorIF<Pair_W_SeqPSF> prefixIterator(String prefix) {
        return null;
    }
}
