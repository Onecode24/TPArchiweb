package com.episen.tparchiweb.resource;

import com.episen.tparchiweb.model.Product;
import com.episen.tparchiweb.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRessource {

    @Inject
    private ProductService productService;

    @GET
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).build();
    }

    @POST
    public Response create(Product product) {
        productService.addProduct(product);
        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    @POST
    @Path("/{id}/stock/increase")
    public Response addStock(@PathParam("id") Long id, @QueryParam("quantity") int quantity) {
        productService.increaseStock(id, quantity);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/stock/decrease")
    public Response removeStock(@PathParam("id") Long id, @QueryParam("quantity") int quantity) {
        boolean success = productService.decreaseStock(id, quantity);
        if (success) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Stock insuffisant ou produit inexistant")
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Product updatedProduct) {
        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());

        productService.updateProduct(existingProduct);
        return Response.ok(existingProduct).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        // On vérifie d'abord si le produit existe
        Product product = productService.getProductById(id);

        if (product != null) {
            // On appelle le service en passant directement l'ID
            productService.deleteProduct(id);
            return Response.noContent().build(); // Réponse 204 (Succès, pas de contenu)
        }

        return Response.status(Response.Status.NOT_FOUND).build(); // Réponse 404
    }
}
