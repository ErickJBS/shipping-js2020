package mx.erickb.shipping.model;

public class PackageSize implements Describable {
    private long id;
    private String description;
    private double priceFactor;

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

    public double getPriceFactor() {
        return priceFactor;
    }

    public void setPriceFactor(double priceFactor) {
        this.priceFactor = priceFactor;
    }

    @Override
    public String toString() {
        return "PackageSize{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", priceFactor=" + priceFactor +
                '}';
    }

    @Override
    public String describe() {
        return getDescription();
    }
}
