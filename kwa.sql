SELECT productos.nombre_producto, tiendas.nombre_tienda, productos_has_tiendas.precio 
FROM productos_has_tiendas
INNER JOIN productos ON productos_has_tiendas.n_producto = productos.nombre_producto
INNER JOIN tiendas ON productos_has_tiendas.n_tienda = tiendas.nombre_tienda
WHERE productos.tipo = 'Tequila'