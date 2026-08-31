package com.productos.businesslogic;

import com.productos.models.Producto;
import com.productos.persistence.ProductoPersistence;

import java.util.List;

/**
 * CAPA DE NEGOCIO (Business Logic).
 *
 * Es el "cerebro" del módulo de productos: aplica las VALIDACIONES y
 * reglas del negocio ANTES de tocar la base de datos.
 *
 * Separación de responsabilidades (programación por capas):
 *  - Controller     -> recibe peticiones HTTP y responde JSON.
 *  - BL (esta capa) -> valida los datos (lógica de negocio).
 *  - Persistence    -> solo se conecta a MySQL (JDBC puro).
 *
 * Regla de oro: esta capa NUNCA habla con el Controller ni con la BD
 * directamente; SOLO se comunica con ProductoPersistence
 * (equivalente al forms.py / capa de servicios en Django).
 */
public class BLProducto {

    // La capa de negocio depende (usa) de la capa de persistencia.
    private final ProductoPersistence persistence;

    /**
     * Constructor por defecto. Crea su propia instancia de persistencia.
     * (Forma sencilla para ir aprendiendo los fundamentos).
     */
    public BLProducto() {
        this.persistence = new ProductoPersistence();
    }

    /**
     * Constructor con inyección manual de dependencias.
     * Permite pasar la persistencia por fuera (facilita pruebas).
     *
     * @param persistence capa de persistencia a utilizar.
     */
    public BLProducto(ProductoPersistence persistence) {
        this.persistence = persistence;
    }

    /**
     * VALIDA un objeto Producto aplicando las reglas mínimas del negocio.
     *
     * @param p el producto a validar.
     * @return true si pasa todas las validaciones.
     */
    public boolean validarProducto(Producto p) {
        // Regla 1: el objeto no puede ser nulo.
        if (p == null) {
            System.out.println("Error: el producto es nulo.");
            return false;
        }

        // Regla 2: el nombre no puede ser nulo ni estar vacío.
        String nombre = p.getNombre();
        if (nombre == null || nombre.isBlank()) {
            System.out.println("Error: el nombre es obligatorio.");
            return false;
        }

        // Regla 3: el precio no puede ser negativo.
        if (p.getPrecioBase() < 0) {
            System.out.println("Error: el precio no puede ser negativo.");
            return false;
        }

        // Si llegamos aquí, el producto es válido.
        return true;
    }

    /**
     * Guarda un producto. Primero lo valida y luego lo persiste.
     *
     * @param p producto a guardar.
     * @return true si se guardó correctamente.
     */
    public boolean guardarProducto(Producto p) {
        if (validarProducto(p)) {
            return persistence.guardarProducto(p);
        }
        return false;
    }

    /**
     * Elimina un producto por su id.
     *
     * @param id identificador del producto.
     * @return true si se eliminó correctamente.
     */
    public boolean eliminarProducto(Long id) {
        if (id == null || id <= 0) {
            System.out.println("Error: id inválido.");
            return false;
        }
        return persistence.eliminarProducto(id);
    }

    /**
     * Actualiza un producto existente.
     *
     * @param p producto con los nuevos datos (debe traer su id).
     * @return true si se actualizó correctamente.
     */
    public boolean actualizarProducto(Producto p) {
        if (p == null || p.getId() == null) {
            System.out.println("Error: el producto y su id son obligatorios.");
            return false;
        }
        if (validarProducto(p)) {
            return persistence.actualizarProducto(p);
        }
        return false;
    }

    /**
     * Lista todos los productos.
     *
     * @return lista de productos (puede estar vacía, nunca null).
     */
    public List<Producto> listarProductos() {
        return persistence.listarProductos();
    }

    /**
     * Busca UN producto por su id.
     *
     * @param id identificador del producto.
     * @return el producto encontrado o null si no existe.
     */
    public Producto obtenerProducto(Long id) {
        if (id == null || id <= 0) {
            System.out.println("Error: id inválido.");
            return null;
        }
        return persistence.obtenerProducto(id);
    }
}
