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

    @Override
    public void insertIndex(String p, String doc_id, int freq) {
        //Comprobamos si la palabra ya existe en el indice
        //NO ESTA PERMITIDO INTRODUCIR DOC_ID QUE YA ESTE EN EL INDICE
        //Si existe p añadimos Pair W SeqPSF al final

        IteratorIF<Pair_W_SeqPSF> it = index.iterator();
        //Revisar esta linea podria ser inecesaria
        while (it.hasNext()) {
            Pair_W_SeqPSF par = it.getNext();
            if(par.getWord().equals(p)){
                par.add(doc_id,freq);
                return;
            }
        }

        //Creo el nuevo par
        Pair_W_SeqPSF parPSeq = new Pair_W_SeqPSF(p);
        //Le añado un par a la lista
        parPSeq.add(doc_id,freq);
        indexList.insert(indexList.size()+1,parPSeq);
        index=indexList;

    }

    @Override
    public IteratorIF<Pair_W_SeqPSF> prefixIterator(String prefix) {

        //Recorremos la lista aquellas palabras que que empiezan por prefix seran agragadas a un indice
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

        //Ordenador por orden alfabetico
        for(int i = 1; i<= indexListAux.size(); i++) {

            for (int j = 1; j < indexListAux.size(); j++) {

                Pair_W_SeqPSF pairA = indexListAux.get(j);
                Pair_W_SeqPSF pairB = indexListAux.get(j + 1);
                //Obtengo par A y B su siguiente
                if (pairA.getWord().compareTo(pairB.getWord()) > 0) { //Si esto da > 0 significa que el orden es incorrecto
                    indexListAux.remove(j + 1); //Borro posicion B
                    indexListAux.insert(j, pairB); //Inserto ParB en posicion A
                }

            }

        }
        return indexListAux.iterator();


    }
}
