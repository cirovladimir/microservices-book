package se.magnus.microservices.api.composite;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(description = "REST API for composite product information")
public interface ProductCompositeService {
    
    @GetMapping(value = "/product-composite/{productId}", produces = "application/json")
    @ApiOperation(value = "${api.product-composite.get.description}", notes = "${api.product-composite.get.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
        @ApiResponse(code = 404, message = "Not Found, the specified id does not exist."),
        @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information."),
    })
    ProductAggregate getProduct(@PathVariable(value = "productId", required = true)int productId);

    @PostMapping(value = "/product-composite", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "${api.product-composite.create.description}", notes = "${api.product-composite.create.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
        @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information."),
    })
    ProductAggregate createProduct(@RequestBody ProductAggregate productAggregate);

    @DeleteMapping("/product-composite/{productId}")
    @ApiOperation(value = "${api.product-composite.delete.description}", notes = "${api.product-composite.delete.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
        @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information."),
    })
    void deleteProduct(@PathVariable(required = true)int productId);
}
