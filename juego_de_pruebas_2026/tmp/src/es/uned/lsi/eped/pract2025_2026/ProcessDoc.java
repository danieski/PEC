package es.uned.lsi.eped.pract2025_2026;

import es.uned.lsi.eped.DataStructures.IteratorIF;

/* Un objeto de esta clase permite contabilizar la frecuencia
 * de las palabras de un documento.
 */
public class ProcessDoc {
    String doc_id;
    Seq_PSF words;

    /* Constructor */
    public ProcessDoc(String did) {
        this.doc_id = did;
        this.words = new Seq_PSF();
    }

    /* Devuelve la secuencia de pares <palabra,frecuencia> */
    public Seq_PSF getSequence() {
        return this.words;
    }

    /* Incrementa en 1 la frecuencia de la palabra w en el documento */    
    public void addWord(String w) {

        IteratorIF<Pair_S_F> it = words.iterator();
        //Recorremos la secuencia
        while (it.hasNext()) {

            Pair_S_F pairAux = it.getNext();
            //Si el <id_doc> coincide aumentamos la frecuencia

            if(pairAux.getString().equals(w)){
                //System.out.println("Se repite la cadena: " + w);
                //Aumentamos la frecuencia
                pairAux.incFrequency();


                return;
            }
        }

        //En el caso
        //Se añade elementos a la lista auxiliar

        Pair_S_F targetPair = new Pair_S_F(w, 1);
        words.add(targetPair);
    }
}
