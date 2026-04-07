package es.uned.lsi.eped.pract2025_2026;

import es.uned.lsi.eped.DataStructures.*;

import static es.uned.lsi.eped.DataStructures.GTreeIF.IteratorModes.PREORDER;

public class IndexTree implements IndexIF {

    protected GTreeIF<Node> index;

    public IndexTree() {
        // No sabemos si podmemos añadir constructores a la clase
        index = new GTree<Node>();
        index.setRoot(new NodeRoot());
    }
    public IteratorIF<Node> testIterator(){
        return index.iterator(PREORDER);
    }
    private Queue<Character> characterQueueMaker(String word){
        char[] arrayPalabra = word.toCharArray();
        // Creamos la cola de lettras
        Queue<Character> characterQueue = new Queue<Character>();
        for (char value : arrayPalabra) {
            characterQueue.enqueue(value);
        }
        return characterQueue;
    }
    private Seq_PSF findSequence(GTreeIF<Node> actualBranch, Queue<Character> characterQueue) {

        // Acabamos cuando la cola esta vacia
        if (characterQueue.isEmpty()) {
            // Buscamos el hijo de tipo INFO (nodo hoja con los datos de la palabra)
            IteratorIF<GTreeIF<Node>> itResoult = actualBranch.getChildren().iterator();
            while (itResoult.hasNext()) {
                Node treeResoult = itResoult.getNext().getRoot();
                if (treeResoult.getNodeType() == Node.NodeType.INFO) {
                    // Encontrado! Devolvemos la secuencia de pares doc-frecuencia
                    NodeInfo nodeResoult = (NodeInfo) treeResoult;
                    return nodeResoult.getSeqPSR();
                }
            }
            // Si no hay hijo INFO, la palabra no estaba completa en el indice
            System.out.println("Palabra no encontrada (sin nodo INFO)");
            return new Seq_PSF();
        }

        // Obtenemos los hijos del root
        ListIF<GTreeIF<Node>> listChildren = actualBranch.getChildren();
        IteratorIF<GTreeIF<Node>> itChildren = listChildren.iterator();

        // Recorremos la lista de hijos
        while (itChildren.hasNext()) {

            // Obtengo su raiz
            GTreeIF<Node> targetBranch = (GTreeIF<Node>) itChildren.getNext();

            // Si el hijo es un nodo INFO (fin de otra palabra mas corta), lo saltamos.
            // Ej: buscando "casamiento" y el nodo 'a' tiene hijo INFO (de "casa") + hijo 'm'
            if (targetBranch.getRoot().getNodeType() != Node.NodeType.INNER) {
                continue;
            }

            NodeInner targetNode = ((NodeInner) targetBranch.getRoot());

            // Recorriendo la lista si alguno de los caracteres coincide.
            if (characterQueue.getFirst() == targetNode.getLetter()) {
                System.out.println("Caracter encontrado: " + targetNode.getLetter());
                // Borramos el caracter
                characterQueue.dequeue();
                // Buscamos en la siguiente
                return findSequence(targetBranch, characterQueue);
            }

        }

        //Si termina el bucle es que no ha encontrado camino
        System.out.println("No se ha encontrado la palabra: ");
        return new Seq_PSF();
    }
    @Override
    public Seq_PSF retrieveIndex(String p) {
        return findSequence(index,characterQueueMaker(p));
    }

    private void addLetter(Queue<Character> characterQueue, GTreeIF<Node> actualBranch, String doc_id, int freq) {

        GTree<Node> newBranch = new GTree<Node>();
        // Excepcion cuando nos quedamos sin caracteres añadimos la branch con el Info
        // Node

        if (characterQueue.isEmpty()) {
            // La hoja es una secuencia de pares
            GTree<Node> branchInfo = new GTree<Node>(); // Creamos el ultimo
            branchInfo.setRoot(new NodeInfo(new Pair_S_F(doc_id, freq))); // Le agregamos la secuencia de pares
            int pos = actualBranch.getNumChildren() + 1;
            actualBranch.addChild(pos, branchInfo);// Se lo añadimos a la ultima rama
            return;
        }

        // Obtenemos el caracter
        char actualCharacter = characterQueue.getFirst();
        // Eliminamos el caracter de la cola
        characterQueue.dequeue();
        // Obtenemos la posicion del la nueva rama
        int positionNewBranch = actualBranch.getNumChildren() + 1;
        // A la actual rama le añadimos otra
        actualBranch.addChild(positionNewBranch, newBranch);
        // Seteamos el root de la nueva rama
        newBranch.setRoot(new NodeInner(actualCharacter));
        // Volvemos a llamar a la funcion para la nueva rama hasta quedarnos sin letras
        addLetter(characterQueue, newBranch, doc_id, freq);

    }

    private void checkBranch(GTreeIF<Node> actualBranch, Queue<Character> characterQueue, String doc_id, int freq) {

        // Caso Empty: Ya hemos consumido todos los caracteres de la palabra
        if (characterQueue.isEmpty()) {
            // Llamamos a addLetter para que inserte el NodeInfo de fin de palabra
            addLetter(characterQueue, actualBranch, doc_id, freq);
            return;
        }

        // Obtener el carácter actual que queremos buscar/insertar
        char actualCharacter = characterQueue.getFirst();

        // Nos pasan una lista con los hijos de la branch
        ListIF<GTreeIF<Node>> listBranch = actualBranch.getChildren();
        IteratorIF itBranch = listBranch.iterator(); // iterador de la lista

        while (itBranch.hasNext()) { // Recorremos

            // Obtenemos el arbol que vamos a analizar
            GTreeIF<Node> targetBranch = (GTreeIF<Node>) itBranch.getNext();

            // Verificamos si es un nodo que guarda letra y si coincide con nuestro carácter
            if (targetBranch.getRoot().getNodeType() == Node.NodeType.INNER) {
                NodeInner innerNode = (NodeInner) targetBranch.getRoot();

                if (innerNode.getLetter() == actualCharacter) {
                    System.out.println("Coincide con: " + actualCharacter);
                    // Hay coincidencia! Quitamos la letra de la cola y avanzamos recursivamente
                    characterQueue.dequeue();
                    checkBranch(targetBranch, characterQueue, doc_id, freq);
                    return; // Salimos, porque ya no tenemos que seguir buscando en otros hermanos
                }
            }
        }

        // Si recorremos todos los hijos del arbol y ninguno coincide, agregamos una
        // nueva rama.
        addLetter(characterQueue, actualBranch, doc_id, freq);
    }

    @Override
    public void insertIndex(String p, String doc_id, int freq) {
        System.out.println("Analizando " + p);
        checkBranch(index, characterQueueMaker(p), doc_id, freq);

    }
    private void auxPrefix(ListIF<GTreeIF<Node>> listTree){
        
    }

    @Override
    public IteratorIF<Pair_W_SeqPSF> prefixIterator(String prefix) {
        Queue characterQueue = characterQueueMaker(prefix);
        ListIF<GTreeIF<Node>> rootChildren = index.getChildren();
        IteratorIF<GTreeIF<Node>> itRootChildren = rootChildren.iterator();


        while(itRootChildren.hasNext()){

            GTreeIF actualTree = itRootChildren.getNext();

            if(characterQueue.getFirst() == actualTree.getRoot()){
                characterQueue.dequeue();
                auxPrefix(actualTree.getChildren());
            }


        }

        return null;
    }
}
