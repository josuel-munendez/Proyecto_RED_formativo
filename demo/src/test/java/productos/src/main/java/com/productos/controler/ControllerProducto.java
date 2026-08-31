package com.productos.controler;

import com.productos.businesslogic.BLProducto;
import com.productos.models.Producto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CAPA DE CONTROL (Controller).
 *
 * Es la "puerta de entrada" de la API del microservicio de productos.
 * Expone los ENDPOINTS HTTP que el frontend consume usando fetch
 * (GET, POST, PUT, DELETE).
 *
 * Flujo de la petición:
 *      Frontend (fetch)  -->  Controller  -->  BL (negocio)  -->  Persistence (JDBC)
 *
 * Equivalencia con MVC/MVT:
 *  - En Django esta clase sería la "vista" (views.py).
 *  - La "Vista" aquí es el JSON que devuelve + static/index.html.
 */
@RestController
@RequestMapping("/api/productos")
public class ControllerProducto {

    private final BLProducto bl;

    /**
     * Se crea una instancia de la capa de negocio.
     * (Inyección manual por constructor, sencilla para aprendizaje).
     */
    public ControllerProducto() {
        this.bl = new BLProducto();
    }

    /**
     * GET /api/productos
     * Obtiene la lista completa de productos.
     *
     * @return lista de productos (JSON).
     */
    @GetMapping
    public List<Producto> listar() {
        return bl.listarProductos();
    }

    /**
     * GET /api/productos/{id}
     * Obtiene UN solo producto por su id (lo usa el botón "Editar").
     *
     * @param id identificador del producto.
     * @return el producto encontrado o null si no existe.
     */
    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return bl.obtenerProducto(id);
    }

    /**
     * POST /api/productos
     * Crea un nuevo producto. El cuerpo JSON se convierte
     * automáticamente a un objeto Producto (@RequestBody).
     *
     * @param p producto recibido desde el frontend.
     * @return true/false según se haya guardado.
     */
    @PostMapping
    public boolean guardar(@RequestBody Producto p) {
        return bl.guardarProducto(p);
    }

    /**
     * PUT /api/productos
     * Actualiza un producto existente.
     *
     * @param p producto con los nuevos datos (debe traer su id).
     * @return true/false según se haya actualizado.
     */
    @PutMapping
    public boolean actualizar(@RequestBody Producto p) {
        return bl.actualizarProducto(p);
    }

    /**
     * DELETE /api/productos/{id}
     * Elimina un producto por su id.
     *
     * @param id identificador del producto a eliminar.
     * @return true/false según se haya eliminado.
     */
    @DeleteMapping("/{id}")
    public boolean eliminar(@PathVariable Long id) {
        return bl.eliminarProducto(id);
    }
}
