package es.uned.lsi.eped.pract2025_2026;

import es.uned.lsi.eped.DataStructures.*;

public class IndexTree implements IndexIF {

    protected GTreeIF<Node> index;

    public IndexTree() {
        // No sabemos si podmemos añadir constructores a la clase
        index = new GTree<Node>();
        index.setRoot(new NodeRoot());
    }

    /**
    *Combierte un objeto string en una cola con los chars del string
    *
    * */
    private Queue<Character> characterQueueMaker(String word) {
        char[] arrayPalabra = word.toCharArray();
        // Creamos la cola de lettras
        Queue<Character> characterQueue = new Queue<Character>();
        for (char value : arrayPalabra) {
            characterQueue.enqueue(value);
        }
        return characterQueue;
    }
    /**
    * Esta es la funcion recursiva que usaremos para encontrar el indice que estamos buscando
    * Primero se identifica el caso base que es cuando la cola de letras esta vacia, devolveremos la
    * secuencia asociada al nodo info de esa busqueda
    *
    * Recorriendo la lista de hijos que pasamos de forma recursiva comprobamos
    *
    * Si alguno coincide con la letra actual, descartamos la letra
    * y hacemos una llamada recursiva poniendo como parametro los hijos
    * del nodo que contiene el caracter.
    *
    * Si no coincide un caracter antes de terminar la palabra entonces la palabra completa
    * no existe en el indice y se devolvera una secuencia vacia
    * */
    private Seq_PSF auxRetriveIndex(GTreeIF<Node> actualBranch, Queue<Character> characterQueue) {

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
            // Ej: buscando "casamiento" y el nodo 'a' tiene hijo INFO (de "casa") + hijo
            // 'm'
            if (targetBranch.getRoot().getNodeType() != Node.NodeType.INNER) {
                continue;
            }

            NodeInner targetNode = ((NodeInner) targetBranch.getRoot());

            // Recorriendo la lista si alguno de los caracteres coincide.
            if (characterQueue.getFirst() == targetNode.getLetter()) {
                // Borramos el caracter
                characterQueue.dequeue();
                // Buscamos en la siguiente
                return auxRetriveIndex(targetBranch, characterQueue);
            }

        }

        // Si termina el bucle es que no ha encontrado camino
        return new Seq_PSF();
    }

    @Override
    public Seq_PSF retrieveIndex(String p) {

        return auxRetriveIndex(index, characterQueueMaker(p));
    }
    /**
    * addLetter
    *
    * Funciona con una llamada recursiva junto con la funcion auxInsertIndex para agregar los nuevos hijos
    * con sus correspondientes letras en su root.
    *
    * En su caso base al quedarnos sin letras que agragar creamos un NodeInfo que agrega los parametro doc_id y freq
    *
    * Se comprueba tambien si en ese ultimo hijo en el caso base no existe ningun NodeInfo, si lo hubiera
    * no creamos un nuevo NodeInfo sino que agregamos la informacion del parametro al existente
    *
     */
    private void addLetter(Queue<Character> characterQueue, GTreeIF<Node> actualBranch, String doc_id, int freq) {

        GTree<Node> newBranch = new GTree<Node>();
        // Caso base: Nos quedamos sin caracteres añadimos la branch con el NodeInfo

        if (characterQueue.isEmpty()) {
            //Hay que comprobar que no hay ningun nodo info en esa rama
            ListIF<GTreeIF<Node>> actualBranchChildren = actualBranch.getChildren();
            IteratorIF itActualBranch = actualBranchChildren.iterator();
            while(itActualBranch.hasNext()){
                GTreeIF<Node> actualChild = (GTreeIF<Node>) itActualBranch.getNext();
                if(actualChild.getRoot() instanceof NodeInfo){
                    NodeInfo oldNodeInfo = (NodeInfo) actualChild.getRoot();
                    oldNodeInfo.getSeqPSR().add(new Pair_S_F(doc_id, freq));
                    return;
                }
            }

            //Al quedarnos sin letras insertamos un nodo tipo INFO sabiendo que es el unico de la rama tenemos que crear el nuevo
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
        // Llamada recursiva a la funcion con nueva rama como parametro hasta alcanzar caso base
        addLetter(characterQueue, newBranch, doc_id, freq);

    }
    /**
     *   auxInsertIndex
     *
     *   Esta funcion recibe una llamada recursiva en busca de aquellos hijos que tienen el caracter de la palabra
     *   que insertamos, va a recorrer los hijos en busca de coincidencias para colgar la palabra que queremos introducir
     *   habiendo descartado los caracteres que coincidian, para seguir la estructura de arbol Trie.
     *
     *   Llama a la funcion addLetter() que se llamara de forma recursiva para crear los nuevos hijos con sus correspondientes
     *   letras y finalmente crear y agregar un NodeInfo con los parametros doc_id y freq.
     */
    private void auxInsertIndex(GTreeIF<Node> actualBranch, Queue<Character> characterQueue, String doc_id, int freq) {

        // Caso base: Ya hemos consumido todos los caracteres de la palabra
        if (characterQueue.isEmpty()) {
            // Llamamos a addLetter para que inserte el NodeInfo de fin de palabra
            addLetter(characterQueue, actualBranch, doc_id, freq);
            return;
        }

        // Obtener el carácter actual que queremos buscar/insertar
        char actualCharacter = characterQueue.getFirst();

        // Nos pasan una lista con los hijos de la branch
        ListIF<GTreeIF<Node>> listBranch = actualBranch.getChildren();
        IteratorIF itBranch = listBranch.iterator();

        // Recorremos los hijos
        while (itBranch.hasNext()) {

            // Obtenemos el arbol que vamos a analizar
            GTreeIF<Node> targetBranch = (GTreeIF<Node>) itBranch.getNext();

            // Verificamos si es un nodo que guarda letra y si coincide con nuestro carácter
            if (targetBranch.getRoot().getNodeType() == Node.NodeType.INNER) {
                NodeInner innerNode = (NodeInner) targetBranch.getRoot();

                if (innerNode.getLetter() == actualCharacter) {
                    // Hay coincidencia, desencolamos la letra de la cola y avanzamos recursivamente
                    characterQueue.dequeue();
                    // El nodo que coincide con la letra se combierte en el nodo parametro
                    auxInsertIndex(targetBranch, characterQueue, doc_id, freq);
                    return; // Salimos, porque ya no tenemos que seguir buscando en otros hermanos
                }
            }
        }

        // Si recorremos todos los hijos del arbol y ninguno coincide, agregamos una nueva rama.
        addLetter(characterQueue, actualBranch, doc_id, freq);
    }

    @Override
    public void insertIndex(String p, String doc_id, int freq) {

        auxInsertIndex(index, characterQueueMaker(p), doc_id, freq);

    }

    /**
     * Recorre recursivamente el subárbol acumulando la palabra carácter a carácter.
     * Cuando encuentra un nodo INFO, guarda el par (palabra, secuencia) en la
     * lista.
     */
    private void collectWords(GTreeIF<Node> tree, String wordSoFar, List<Pair_W_SeqPSF> result) {
        Node root = tree.getRoot();
        if (root.getNodeType() == Node.NodeType.INFO) {
            NodeInfo nodeInfo = (NodeInfo) root;
            result.insert(result.size()+1, new Pair_W_SeqPSF(wordSoFar, nodeInfo.getSeqPSR()));
        }

        if (root.getNodeType() == Node.NodeType.INNER) {
            // Nodo interior: añadimos su letra a la palabra
            NodeInner innerNode = (NodeInner) root;
            wordSoFar = wordSoFar + innerNode.getLetter();
        }

        // Recorremos los hijos: fusionamos todos los NodeInfo en un solo Pair_W_SeqPSF
        // y recursamos en los INNER
        ListIF<GTreeIF<Node>> children = tree.getChildren();
        IteratorIF<GTreeIF<Node>> it = children.iterator();

        // Primera pasada: fusionar todos los NodeInfo hijos en un único par
        Pair_W_SeqPSF mergedPair = null;
        while (it.hasNext()) {
            GTreeIF<Node> child = it.getNext();
            if (child.getRoot().getNodeType() == Node.NodeType.INFO) {
                NodeInfo infoNode = (NodeInfo) child.getRoot();
                if (mergedPair == null) {
                    mergedPair = new Pair_W_SeqPSF(wordSoFar);
                }
                // Añadimos cada par (doc,freq) de este NodeInfo al par fusionado
                IteratorIF<Pair_S_F> pairIt = infoNode.getSeqPSR().iterator();
                while (pairIt.hasNext()) {
                    Pair_S_F pair = pairIt.getNext();
                    mergedPair.add(pair.getString(), pair.getFrequency());
                }
            }
        }
        if (mergedPair != null) {
            result.insert(result.size() + 1, mergedPair);
        }

        // Segunda pasada: recursar en los hijos INNER
        it = children.iterator();
        while (it.hasNext()) {
            GTreeIF<Node> child = it.getNext();
            if (child.getRoot().getNodeType() == Node.NodeType.INNER) {
                collectWords(child, wordSoFar, result);
            }
        }
    }
    /**
    *auxPrefix
    *
    *Busca todos los nodos que empiezan por la letra actual del prefijo que buscamos
    *Si la encuentra la descarta de la cola y busca en sus hijos haciendo una llamada recursiva
    *
    *En el caso base nos quedamos sin letras del prefijo a continuacion llamamos a la funcion collectWords
    *recorriendo todos los arboloes de esos hijos, completando la palabra que forman y guardano sus IndoNodes
    *en una lista para posteriormente ordenarla.
    *
     */
    private void auxPrefix(GTreeIF<Node> actualTree, Queue<Character> characterQueue, List<Pair_W_SeqPSF> nodeInfoList,
            String wordSoFar) {

        // Caso base: ya consumimos el prefijo recoger todas las palabras del subárbol
        if (characterQueue.isEmpty()) {
            // Iteramos los HIJOS de actualTree: su raíz ya está representada en wordSoFar
            ListIF<GTreeIF<Node>> children = actualTree.getChildren();
            IteratorIF<GTreeIF<Node>> it = children.iterator();
            while (it.hasNext()) {
                //Guardaremos todos sus InfoNodes dentro de una lista
                collectWords(it.getNext(), wordSoFar, nodeInfoList);
            }
            return;
        }

        // Aún quedan caracteres del prefijo: buscar el hijo cuya letra coincida
        char actualCharacter = characterQueue.getFirst();
        ListIF<GTreeIF<Node>> rootChildren = actualTree.getChildren();
        IteratorIF<GTreeIF<Node>> itRootChildren = rootChildren.iterator();

        // Recorreremos los hijos por si alguno coincide con el caracter actual
        while (itRootChildren.hasNext()) {

            // Analizamos cada hijo del arbol actual
            GTreeIF<Node> targetBranch = itRootChildren.getNext();
            Node actualNode = targetBranch.getRoot();

            // Si su nodo es interno podremos analizar su caracter
            if (actualNode.getNodeType() == Node.NodeType.INNER) {
                NodeInner nodeInner = (NodeInner) actualNode;

                if (nodeInner.getLetter() == actualCharacter) {
                    // Coincidencia: consumir la letra y descender al sub-árbol hijo
                    characterQueue.dequeue();
                    //El nodo que coincide con el caracter se combierte en el arbol parametro
                    auxPrefix(targetBranch, characterQueue, nodeInfoList, wordSoFar + actualCharacter);
                    return; // Solo puede haber un hijo con esa letra
                }
            }
        }
        // Si ningún hijo coincide, el prefijo no existe en el índice → no se añade nada
    }
    /**
    * prefixIterator
    *
    * Creamos una lista auxiliar que va a guardar los pares que son las palabras que empiezan por el prefijo
    * a esta lista le hemos insertado los nodos info correspondientes a cada palabra.
    *
    * Al final ordenamos por medio de bubbl sort comparando la posicion actual con la contigua si estan desordenadas
    * su posicion se intercambia.
    *
    * Finalmente llamamos al iterador de la lista auxiliar result para devolverlo.
    *
    * */
    @Override
    public IteratorIF<Pair_W_SeqPSF> prefixIterator(String prefix) {

        Queue<Character> characterQueue = characterQueueMaker(prefix);
        List<Pair_W_SeqPSF> result = new List<Pair_W_SeqPSF>();
        auxPrefix(index, characterQueue, result, "");

        // Ordenador por orden alfabetico
        for (int i = 0; i <= result.size(); i++) {

            for (int j = 0; j < result.size(); j++) {

                Pair_W_SeqPSF pairA = result.get(j);
                Pair_W_SeqPSF pairB = result.get(j + 1);
                // Obtengo par A y B su siguiente
                if (pairA.getWord().compareTo(pairB.getWord()) > 0) { // Si esto da > 0 significa que el orden es
                                                                      // incorrecto
                    result.remove(j + 1); // Borro posicion B
                    result.insert(j, pairB); // Inserto ParB en posicion A
                }

            }

        }
        return result.iterator();
    }
}
