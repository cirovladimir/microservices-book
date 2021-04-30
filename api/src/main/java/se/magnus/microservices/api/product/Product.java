package se.magnus.microservices.api.product;

public class Product {
    private final int productId;
    private final String name;
    private final int weight;
    private final String serviceAddress;


    protected Product() {
        productId = -1;
        name = null;
        weight = -1;
        serviceAddress = null;
    }
    

    public Product(int productId, String name, int weight, String serviceAddress) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.serviceAddress = serviceAddress;
    }


    public int getProductId() {
        return this.productId;
    }


    public String getName() {
        return this.name;
    }


    public int getWeight() {
        return this.weight;
    }


    public String getServiceAddress() {
        return this.serviceAddress;
    }

}