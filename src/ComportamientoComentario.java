/**
 * Interfaz que modela el comportamiento de los comentarios en función del tipo de cliente que lo realiza
 *
 * @author : Juan Pablo García Plaza Pérez
 * @author José Ángel Concha Carrasco
 * grupo : Wild True
 * Entrega : EC2
 * Curso : 2º GIIIS (Grupo A)
 */

public interface ComportamientoComentario {

    /**
     * Realiza el comentario de un cliente sobre un determinado producto
     *
     * @param producto Producto sobre el que se comenta
     * @param cliente Cliente que realiza el comentario
     * @return Booleano indicando si se pudo o no publicar el comentario
     */
    boolean comentar(Producto producto, Cliente cliente);

    /**
     * Realiza el cálculo asociado a la puntuación del comentario
     *
     * @param producto Producto que se emplea para conocer dicha puntuación
     * @return Puntuación resultado
     */
    int calcularPuntuacion(Producto producto);

    /**
     * Obtiene el texto asociado al producto
     *
     * @param producto Producto que se emplea para conocer dicho texto
     * @return Texto resultado
     */
    String obtenerTexto(Producto producto);

}