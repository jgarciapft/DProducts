import Identificadores.GeneradorIdentificador;
import Identificadores.Identificador;

/**
 * Clase que modela el comportamiento base de un producto. Permite consultar sus detalles base y hacer un pedido
 * del un producto en particular. Cada producto es asignado con una clave única en el ciclo de vida del programa que establece
 * una relación biunívoca entre su identididad y su identificador
 *
 * @author : Juan Pablo García Plaza Pérez
 * @author José Ángel Concha Carrasco
 * grupo : Wild True
 * Entrega : EC2
 * Curso : 2º GIIIS (Grupo A)
 */

public abstract class Producto {

    // Valores de reabasteciemiento de productos según su prioridad
    public static final int REABASTECIMIENTO_PRIORIDAD_BAJA = 25;
    public static final int REABASTECIMIENTO_PRIORIDAD_MEDIA = 75;
    public static final int REABASTECIMIENTO_PRIORIDAD_ALTA = 150;

    private String nombre;                                              // Nombre comercial del producto
    private Identificador identificador;                                // Identificador único del producto
    private int cantidad;                                               // Cantidad actual en stock del producto
    private float precio;                                               // Precio del producto
    private int stockMinimo;                                            // Cantidad mínima que siempre debe existir en stock
    private PRIORIDAD_PRODUCTO prioridad;                               // Valor directamente relacionado con la cantidad con la que se reabastece el producto
    private FABRICANTES fabricante;                                     // Fabricante del producto

    /**
     * Constructor parametrizado de la clase. Genera un producto a partir de su nombre, camtidad en stock, cantidad mínima en stock y
     * fabricante
     *
     * @param nombre      Nombre del producto
     * @param cantidad    Cantidad en stock del producto
     * @param precio      Precio del producto
     * @param stockMinimo Cantidad mínima en stock que siempre debe existir del producto
     * @param fabricante  Valor del tipo enumerado de {@code FABRICANTES}
     * @param prioridad   Valor del tipo enumerado {@code PRIORIDAD_PRODUCTO}. Representa la demanda del producto
     *                    y se tiene en cuenta al reabastecerlo
     * @throws IllegalArgumentException Si la cantidad o el precio es un entero negativo o 0 o si el stock mínimo es un entero negativo
     */
    public Producto(String nombre, int cantidad, float precio, int stockMinimo, FABRICANTES fabricante, PRIORIDAD_PRODUCTO prioridad) {
        if (!esCorrecto(cantidad, precio, stockMinimo)) throw new
                IllegalArgumentException("Parámetros inválidos. Compruebe que 'cantidad', 'precio' y 'stockMinimo' sean valores positivos" +
                " y mayores que 0 (stockMinimo sí puede ser 0)");

        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.stockMinimo = stockMinimo;
        this.fabricante = fabricante;
        this.prioridad = prioridad;

        identificador = GeneradorIdentificador.recuperarInstancia().generarIdentificador();
    }

    /**
     * Método accesor del atributo {@link Producto#nombre}
     *
     * @return Nombre del producto
     */
    protected String getNombre() {
        return nombre;
    }

    /**
     * Método mutador del atributo {@link Producto#nombre}
     *
     * @param nombre Nuevo nombre del producto
     */
    protected void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método accesor del atributo {@link Producto#identificador}
     *
     * @return Identificador del producto
     */
    protected Identificador getIdentificador() {
        return identificador;
    }

    /**
     * Método accesor del atributo {@link Producto#cantidad}
     *
     * @return Cantidad actual en stock del producto
     */
    protected int getCantidad() {
        return cantidad;
    }

    /**
     * Método accesor del atributo {@link Producto#precio}
     *
     * @return Precio del producto
     */
    protected float getPrecio() {
        return precio;
    }

    /**
     * Método accesor del atributo {@link Producto#stockMinimo}
     *
     * @return Cantidad en stock mínima del producto
     */
    protected int getStockMinimo() {
        return stockMinimo;
    }

    /**
     * Método accesor del atributo {@link Producto#prioridad}
     *
     * @return Factor de prioridad con el que debe restablecerse el stock del producto
     */
    protected PRIORIDAD_PRODUCTO getPrioridad() {
        return prioridad;
    }

    /**
     * Método mutador del atributo {@link Producto#prioridad}
     *
     * @param prioridad Nueva prioridad del producto
     */
    protected void setPrioridad(PRIORIDAD_PRODUCTO prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * Método accesor del atributo {@link Producto#fabricante}
     *
     * @return Fabricante del producto
     */
    protected FABRICANTES getFabricante() {
        return fabricante;
    }

    /**
     * Método mutador del atributo {@link Producto#fabricante}
     *
     * @param fabricante Nuevo fabricante del producto
     */
    protected void setFabricante(FABRICANTES fabricante) {
        this.fabricante = fabricante;
    }

    /**
     * Método mutador del atributo {@link Producto#precio}
     *
     * @param precio Nuevo precio del producto. Debe ser correcto
     * @return Si el cambio no fue aceptado
     */
    protected boolean setPrecio(float precio) {
        boolean esCorrecto = esCorrecto(getCantidad(), precio, getStockMinimo());
        if (esCorrecto) {
            this.precio = precio;
        }
        return esCorrecto;
    }

    /**
     * Método mutador del atributo {@link Producto#stockMinimo}
     *
     * @param stockMinimo Nuevo stock minimo del producto. Debe ser correcto
     * @return Si el cambio fue aceptado
     */
    protected boolean setStockMinimo(int stockMinimo) {
        boolean esCorrecto = esCorrecto(getCantidad(), getPrecio(), stockMinimo);
        if (esCorrecto) {
            this.stockMinimo = stockMinimo;
        }
        return esCorrecto;
    }

    /**
     * Método mutador del atributo {@link Producto#cantidad}
     *
     * @param cantidad Incremento del stock del producto
     */
    protected void varCantidad(int cantidad) {
        this.cantidad += cantidad;
    }

    /**
     * Decrementa la cantidad en stock actual por el número de unidades del pedido
     *
     * @param cantidad Número en el que decrementar el stock actual del producto. Solo se admiten valores positivos mayores que 0
     * @return Booleano indicando si se ha permitido o no el decremento del stock del producto
     */
    public boolean entregar(int cantidad) {
        // Comprueba que la cantidad sea positiva mayor que 0 y que el pedido no supere el stock actual
        if (haySuficienteStock(cantidad))
            varCantidad(-cantidad);
        else
            return false;                                               // No se pudo servir el pedido

        return true;
    }

    /**
     * Comprueba si hay suficiente stock como para cubrir un pedido de cierta cantidad
     *
     * @param cantidad Cantidad válida a cubrir por el pedido
     * @return Booleano indicando si hay suficiente stock
     */
    public boolean haySuficienteStock(int cantidad) {
        return cantidad > 0 && getCantidad() - cantidad >= 0;
    }

    /**
     * @return Si es necesario reponer el stock del producto
     */
    public boolean enStockMinimo() {
        return getCantidad() < getStockMinimo();
    }

    /**
     * @param cantidad    Cantidad de entrada en stock. Debe ser un entero positivo
     * @param precio      Precio del producto. Debe ser un real no negativo distinto de 0
     * @param stockMinimo Cantidad mínima en inventario. Debe ser un natural
     * @return Si los campos del producto son válidos
     */
    private boolean esCorrecto(int cantidad, float precio, int stockMinimo) {
        return cantidad >= 0 && precio > 0f && stockMinimo > 0;
    }

    /**
     * @return Cadena con el formato de una entrada de registro de producto y el contenido base del producto
     */
    public String aRegistro() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getIdentificador());
        stringBuilder.append(" ").append(getNombre());
        stringBuilder.append(" ").append(getCantidad()).append(" uds.");
        stringBuilder.append(" ").append(getStockMinimo()).append(" uds.");
        stringBuilder.append(" ").append(getPrecio()).append("€");

        return stringBuilder.toString();
    }

    /**
     * @return Cadena formateada de información del producto
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("PRODUCTO\t");

        stringBuilder.append(getNombre()).append("-").append(getFabricante().toString());
        stringBuilder.append("\n\t").append("Identificador : ").append(getIdentificador().toString());
        stringBuilder.append("\n\t").append("Cantidad en stock : ").append(getCantidad());
        stringBuilder.append("\n\t").append("Cantidad en stock mínima : ").append(getStockMinimo());
        stringBuilder.append("\n\t").append("Prioridad de reabastecimiento : ").append(getPrioridad().toString());

        return stringBuilder.toString() + "\n";
    }


    /**
     * @param obj Objeto con el que se ha de comparar
     * @return Devuelve verdadero si entre esta instancia y {@code obj} hay coincidencia entre todos los atributos
     * y pertenecen a la misma clase
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;                                   // Comprueba si es la misma instancia
        if (!(obj instanceof Producto)) return false;                   // si no pertenecen a la misma clase no procede

        Producto objCasteado = (Producto) obj;                          // Casteado del objeto

        return getNombre().equals(objCasteado.getNombre()) &&
                getIdentificador().equals(objCasteado.getIdentificador()) &&
                getCantidad() == objCasteado.getCantidad() &&
                getPrecio() == objCasteado.getPrecio() &&
                getStockMinimo() == objCasteado.getStockMinimo() &&
                getPrioridad().equals(objCasteado.getPrioridad()) &&
                getFabricante().equals(objCasteado.getFabricante());
    }

    /**
     * @return Valor hashCode único de instancia. Basado en productos de números primos
     */
    @Override
    public int hashCode() {
        int hashCode = super.hashCode();                                  // Hash base
        int primo = 37;                                                   // Operador primo

        hashCode += primo * getNombre().hashCode();
        hashCode += primo * getIdentificador().hashCode();
        hashCode += primo * getCantidad();
        hashCode += primo * Math.round(getPrecio());
        hashCode += primo * getStockMinimo();
        hashCode += primo * getPrioridad().hashCode();
        hashCode += primo * getFabricante().hashCode();

        return hashCode;
    }
}