package es.uned.lsi.eped.pract2025_2026;

import es.uned.lsi.eped.DataStructures.IteratorIF;

/* Representa un Índice en el que se indexan secuencias de pares
 * <doc_id,frecuencia> bajo una palabra formada por una cadena
 * de caracteres
 */
public interface IndexIF {

    /* Devuelve la secuencia de pares <doc_id,frecuencia>
     * asignada a la palabra p.
     * @PRE: p != ""
     * Si no existe una secuencia asignada a la palabra p,
     * devuelve una secuencia vacía.
     */
    public Seq_PSF retrieveIndex(String p);
  
    /* Asigna el par (doc_id,freq) a la palabra p.
     * @PRE: p != "", doc_id != "" y freq > 0
     * @PRE: no hay ningún par con el identificador de documento
     *       doc_id en la secuencia de pares indexados bajo la 
     *       palabra p
     * Si ya habia pares asignados a la misma palabra, el
     * nuevo par se sitúa al final de la secuencia existente.
     */
    public void insertIndex(String p, String doc_id, int freq);

    /* Devuelve un iterador de todos los pares <p,sp>
     * presentes en el índice tales que:
     * -La secuencia de pares indexada bajo el índice p es sp
     * -La palabra p comienza por la cadena prefix
     * Además, el iterador deberá estar ordenado según
     * el orden alfabético de las palabras.
     */
    public IteratorIF<Pair_W_SeqPSF> prefixIterator(String prefix);
}

