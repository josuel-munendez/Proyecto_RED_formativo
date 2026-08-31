package com.productos.persistence;

import com.productos.models.Producto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CAPA DE PERSISTENCIA (Acceso a datos).
 *
 * Aquí vive TODO el SQL del módulo de productos. Se conecta a MySQL
 * con JDBC puro: {@code DriverManager}, {@code Connection},
 * {@code PreparedStatement} y {@code ResultSet} (sin JPA ni JdbcTemplate),
 * igual que la capa de persistencia de Usuarios.
 *
 * Correspondencia con Django:
 *  - Esta capa hace el papel del ORM (objects.filter / save / delete).
 *
 * Claves técnicas:
 *  - PreparedStatement con parámetros `?` → previene inyección SQL.
 *  - try-with-resources → cierra solo Connection/Statement/ResultSet.
 *  - Las fechas NO se insertan desde Java: MySQL las llena sola con
 *    DEFAULT CURRENT_TIMESTAMP y ON UPDATE CURRENT_TIMESTAMP.
 */
public class ProductoPersistence {

    // Credenciales de conexión (mismas convenciones vistas en clase).
    private final String URL = "jdbc:mysql://localhost:3306/db_productos";
    private final String USER = "root";
    private final String PASSWORD = "123456";

    // 1. Guardar producto (CREATE)
    public boolean guardarProducto(Producto p) {

        String sql = """
                INSERT INTO producto (nombre, descripcion, precio_base, activo, aprobado)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, p.getNombre());
            statement.setString(2, p.getDescripcion());
            statement.setDouble(3, p.getPrecioBase());
            statement.setBoolean(4, p.isActivo());
            statement.setBoolean(5, p.isAprobado());

            if (statement.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // en producción: almacenar en un log, no imprimir
        }
        return false;
    }

    // 2. Eliminar producto (DELETE)
    public boolean eliminarProducto(Long id) {

        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            if (statement.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Actualizar producto (UPDATE)
    //    Nota: no tocamos las fechas; `fecha_actualizacion` se refresca
    //    automáticamente en MySQL gracias al ON UPDATE CURRENT_TIMESTAMP.
    public boolean actualizarProducto(Producto p) {

        String sql = """
                UPDATE producto
                SET nombre = ?, descripcion = ?, precio_base = ?, activo = ?, aprobado = ?
                WHERE id = ?
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, p.getNombre());
            statement.setString(2, p.getDescripcion());
            statement.setDouble(3, p.getPrecioBase());
            statement.setBoolean(4, p.isActivo());
            statement.setBoolean(5, p.isAprobado());
            statement.setLong(6, p.getId());

            if (statement.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Listar productos (READ)
    public List<Producto> listarProductos() {

        List<Producto> productos = new ArrayList<>();

        String sql = """
                SELECT id, nombre, descripcion, precio_base, activo, aprobado,
                       fecha_creacion, fecha_actualizacion
                FROM producto
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                productos.add(mapearFila(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    // 5. Obtener UN producto por su id (lo usa el botón "Editar" del frontend)
    public Producto obtenerProducto(Long id) {

        String sql = """
                SELECT id, nombre, descripcion, precio_base, activo, aprobado,
                       fecha_creacion, fecha_actualizacion
                FROM producto
                WHERE id = ?
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearFila(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Traduce una fila del ResultSet a un objeto Producto.
     * Evita repetir el mismo código en listarProductos() y obtenerProducto().
     *
     * @param rs fila actual del ResultSet.
     * @return el Producto construido.
     * @throws SQLException si falla la lectura de alguna columna.
     */
    private Producto mapearFila(ResultSet rs) throws SQLException {

        Producto producto = new Producto();

        producto.setId(rs.getLong("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecioBase(rs.getDouble("precio_base"));
        producto.setActivo(rs.getBoolean("activo"));
        producto.setAprobado(rs.getBoolean("aprobado"));
        producto.setFechaCreacion(rs.getString("fecha_creacion"));
        producto.setFechaActualizacion(rs.getString("fecha_actualizacion"));

        return producto;
    }
}
