package customer;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String state;
    private String phone;
    private String pan;
    private String gstin;

    // Constructors
    public Customer() {}

    public Customer(String name, String address, String state, String phone, String pan, String gstin) {
        this.name = name;
        this.address = address;
        this.state = state;
        this.phone = phone;
        this.pan = pan;
        this.gstin = gstin;
    }

    public Customer(int id, String name, String address, String state, String phone, String pan, String gstin) {
        this(name, address, state, phone, pan, gstin);
        this.id = id;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getPhone() {
        return phone;
    }

    public String getPan() {
        return pan;
    }

    public String getGstin() {
        return gstin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }
}
