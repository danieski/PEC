package es.uned.lsi.eped.pract2025_2026;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.SequenceIF;

public class IndexSequence implements IndexIF {

    protected SequenceIF<Pair_W_SeqPSF> index;
    private List<Pair_W_SeqPSF> indexList = new List<Pair_W_SeqPSF>();

    public IndexSequence(){
        //El indice es una lista de Pares Palabra id freq
        this.index = new List<Pair_W_SeqPSF>();
    }

    /**
    *Busqueda del indice
    *Esta funcion recibira un string que buscara, como index es una secunencia tiene un iterador
    *podemos recorrer en bucle el iterador hasta que encontremos p o si no encontramos nada al terminar
    *de recorrer index devolvemos una secuencia nueva vacia
     */

    @Override
    public Seq_PSF retrieveIndex(String p) {

        IteratorIF<Pair_W_SeqPSF> it = index.iterator();
        //Bucle al indice
        while (it.hasNext()) {
            //tarjet es el bloque que se analiza
            Pair_W_SeqPSF target;
            target = it.getNext();
            //Si concide con el que buscamos
            if(target.getWord().equals(p)){
                //Obtenemos iterador de la lista <doc_id,freq>
                Seq_PSF targetSeq = target.getSeqPSR();
                return targetSeq;
            }
        }
        return new Seq_PSF() ;
    }
    /**
    *Inserccion en el indice
    * @PRE: No esta permitido introducir un Doc_Id que ya esté asociada a una misma palabra.
    *
    *Para poder insertar nuevos datos en el indice vamos a primero comprobar que la palabra aun no esta insertada
    *ya que cada palabra en el indice es unica. Si la palabra existe agregaremos al final de la secuencia
    *asignada a la palabra el nuevo par.
    *
    * Si no existe la palabra vamos a crear una nueva secuencia, a esta nueva secuencia le agregamos un nuevo par
    * llamando a su metodo add e insertandole los datos de parametro.
     *
     *Comprobamos donde tiene que ser insertada la palabra respetando un orden alfabetico, recorriendo el indice
     * para averiguar donde insertarlo correctamente
    *
    * Usamos una lista auxiliar indexList porque necesito usar el metodo insert y como viene indicado en la practica
    * no podemos editar el codigo de index que es del tipo SequenceIF<Pair_W_SeqPSF>.
    *
     */
    @Override
    public void insertIndex(String p, String doc_id, int freq) {
        //Comprobamos si la palabra ya existe en el indice
        //Si existe p añadimos Pair W SeqPSF al final

        IteratorIF<Pair_W_SeqPSF> it = index.iterator();

        while (it.hasNext()) {
            Pair_W_SeqPSF par = it.getNext();
            if(par.getWord().equals(p)){
                par.add(doc_id,freq);
                return;
            }
        }
        //Si no existe la palabra en el indice interto la nueva palabra
        //Creo el nuevo par
        Pair_W_SeqPSF parPSeq = new Pair_W_SeqPSF(p);
        //Le añado un par a la lista
        parPSeq.add(doc_id,freq);

        //insertamos la palabra respetando el orden alfabetico
        IteratorIF<Pair_W_SeqPSF> itList = indexList.iterator();
        int positionInsertion = 0; //Contador de posiciones
        while(itList.hasNext()){
            //Cada iteracion es una posicion mas
            positionInsertion++;
            Pair_W_SeqPSF pairA = itList.getNext();
            //Si la palabra de la lista esta en orden incorrecto se inserta en la posicion donde el orden es incorrecto
            if(pairA.getWord().compareTo(p)>0){
                indexList.insert(positionInsertion,parPSeq);
                index=indexList;
                return;
            }
            //Si no continuo el bucle
        }
        //Si he completado el bucle y esta en orden lo inserto al final.
        indexList.insert(indexList.size()+1,parPSeq);
        index=indexList;

    }

    /**
     *Busqueda de prefijo
     *
     * @RETURN: El iterador de la lista auxiliar ordenada
     *
     * Esta funcion realiza una busqueda devolviendo un iterador de la lista ordenada que contiene las parabras que empiezan por
     * este prefijo.
     *
     * Recorreremos el indice con su iterador buscando todas la palbras que empiecen por el prefijo, esto lo podemos
     * hacer gracias a la funcion de los strings .startsWith() aquellas coincidencias las guardaremos en una lista desordenada.
     * Una vez tengamos la lista llena devolvemos su iterador
     *
     */
    @Override
    public IteratorIF<Pair_W_SeqPSF> prefixIterator(String prefix) {

        //Recorremos la lista aquellas palabras que que empiezan por prefix seran agregadas a un indice
        List<Pair_W_SeqPSF> indexListAux = new List<Pair_W_SeqPSF>();

        //Recorro la lista
        IteratorIF<Pair_W_SeqPSF> it = index.iterator();
        while(it.hasNext()) {
            Pair_W_SeqPSF target = it.getNext();
            if (target.getWord().startsWith(prefix)) {
                //Agrego las coincidencias en una lista auxiliar
                indexListAux.insert(indexListAux.size() + 1, target);
            }
        }

        return indexListAux.iterator();


    }
}
