package com.productos.models;

/**
 * CAPA DE MODELO (Entidad).
 *
 * POJO que mapea la tabla `producto` de la base de datos db_productos.
 * Es el equivalente al "Model" en Django / MVC:
 * solo guarda estado (atributos) y expone getters/setters.
 *
 * Buenas prácticas aplicadas:
 *  - Atributos `private` (encapsulamiento).
 *  - Dos constructores: vacío (lo exige Jackson para convertir JSON)
 *    y completo (comodidad para crear objetos ya llenos).
 *  - Getters de booleanos con prefijo `is` (estándar JavaBeans).
 *  - Nombres en camelCase y sin tildes/ñ (convención Java).
 */
public class Producto {

    private Long id;
    private String nombre;
    private String descripcion;
    private double precioBase;
    private boolean activo;
    private boolean aprobado;
    // Las fechas las genera MySQL (DEFAULT CURRENT_TIMESTAMP),
    // por eso se manejan como String solo para MOSTRARLAS.
    private String fechaCreacion;
    private String fechaActualizacion;

    /**
     * Constructor vacío. Obligatorio para que Jackson
     * pueda deserializar el JSON del frontend a un objeto Producto.
     */
    public Producto() {
    }

    /**
     * Constructor completo.
     *
     * @param id                 identificador (autogenerado por la BD).
     * @param nombre             nombre comercial del producto.
     * @param descripcion        descripción corta del producto.
     * @param precioBase         precio base en pesos (sin impuestos).
     * @param activo             true si está disponible para la venta.
     * @param aprobado           true si pasó la revisión de calidad.
     * @param fechaCreacion      fecha de creación (la pone la BD).
     * @param fechaActualizacion última fecha de modificación (la pone la BD).
     */
    public Producto(Long id, String nombre, String descripcion, double precioBase,
                    boolean activo, boolean aprobado,
                    String fechaCreacion, String fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.activo = activo;
        this.aprobado = aprobado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    /** Getter de booleano primitivo → prefijo "is" (JavaBeans). */
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /** Getter de booleano primitivo → prefijo "is" (JavaBeans). */
    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
