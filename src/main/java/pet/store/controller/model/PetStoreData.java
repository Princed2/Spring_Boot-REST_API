package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.PetStore;
import pet.store.entity.Customer;
import pet.store.entity.Employee;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class PetStoreData {
  private Long id;
  private String storeName;
  private String storeAddress;
  private String storeCity;
  private String storeState;
  private String storeZip;
  private String storePhone;

  // Step 1e: customers/employees now use DTO types, not entity types
  private Set<PetStoreCustomer> customers = new HashSet<>();
  private Set<PetStoreEmployee> employees = new HashSet<>();

  // Step 1f: constructor that copies from the JPA entity
  public PetStoreData(PetStore ps) {
    this.id = ps.getId();
    this.storeName = ps.getStoreName();
    this.storeAddress = ps.getStoreAddress();
    this.storeCity = ps.getStoreCity();
    this.storeState = ps.getStoreState();
    this.storeZip = ps.getStoreZip();
    this.storePhone = ps.getStorePhone();

    if (ps.getCustomers() != null) {
      ps.getCustomers().forEach(c -> this.customers.add(new PetStoreCustomer(c)));
    }
    if (ps.getEmployees() != null) {
      ps.getEmployees().forEach(e -> this.employees.add(new PetStoreEmployee(e)));
    }
  }

  // Step 1g: inner DTO classes (public & static)
  @Data @NoArgsConstructor
  public static class PetStoreCustomer {
    private Long id;
    private String firstName;
    private String lastName;
    private String customerEmail;

    // Step 1g(i): constructor that takes a Customer
    public PetStoreCustomer(Customer c) {
      this.id = c.getId();
      this.firstName = c.getFirstName();
      this.lastName = c.getLastName();
      this.customerEmail = c.getCustomerEmail();
    }
  }
  
//inner DTO class petStoreEmployee (public & static)
  @Data @NoArgsConstructor
  public static class PetStoreEmployee {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String jobTitle;

    // Step 1g(ii): constructor that takes an Employee
    public PetStoreEmployee(Employee e) {
      this.id = e.getId();
      this.firstName = e.getFirstName();
      this.lastName = e.getLastName();
      this.phone = e.getPhone();
      this.jobTitle = e.getJobTitle();
    }
  }
}

