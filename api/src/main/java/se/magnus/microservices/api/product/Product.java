package se.magnus.microservices.api.product;

public class Product {
    private int productId;
    private String name;
    private int weight;
    private String serviceAddress;

    public Product() {
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

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getServiceAddress() {
        return this.serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", productId=" + productId + ", serviceAddress=" + serviceAddress + ", weight="
                + weight + "]";
    }

}