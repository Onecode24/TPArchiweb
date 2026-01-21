package com.episen.tparchiweb.resource;

import com.episen.tparchiweb.model.Product;
import com.episen.tparchiweb.security.annotation.AdminOnly;
import com.episen.tparchiweb.security.annotation.Secured;
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
    @Secured
    @AdminOnly
    public Response create(Product product) {
        productService.addProduct(product);
        return Response.status(Response.Status.CREATED).entity(product).build();
    }


    @PUT
    @Path("/{id}")
    @Secured
    @AdminOnly
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
    @Secured
    @AdminOnly
    public Response delete(@PathParam("id") Long id) {
        Product product = productService.getProductById(id);

        if (product != null) {
            productService.deleteProduct(id);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
