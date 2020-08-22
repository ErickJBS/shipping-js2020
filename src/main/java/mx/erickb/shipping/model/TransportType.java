package mx.erickb.shipping.model;

public class TransportType {
    private long id;
    private String description;
    private double pricePerMile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPricePerMile() {
        return pricePerMile;
    }

    public void setPricePerMile(double pricePerMile) {
        this.pricePerMile = pricePerMile;
    }

    @Override
    public String toString() {
        return "TransportType{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", pricePerMile=" + pricePerMile +
                '}';
    }
}
