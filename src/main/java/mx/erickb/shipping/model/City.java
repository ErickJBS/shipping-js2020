package mx.erickb.shipping.model;

public class City implements Describable {
    private long id;
    private String name;
    private double tax;
    private boolean seaport;
    private boolean airport;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public boolean isSeaport() {
        return seaport;
    }

    public void setSeaport(boolean seaport) {
        this.seaport = seaport;
    }

    public boolean isAirport() {
        return airport;
    }

    public void setAirport(boolean airport) {
        this.airport = airport;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tax=" + tax +
                ", seaport=" + seaport +
                ", airport=" + airport +
                '}';
    }

    @Override
    public String describe() {
        return getName();
    }
}
