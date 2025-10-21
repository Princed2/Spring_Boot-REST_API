package pet.store.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity @Data
public class PetStore {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String storeName;
  private String storeAddress;
  private String storeCity;
  private String storeState;
  private String storeZip;
  private String storePhone;

  @EqualsAndHashCode.Exclude @ToString.Exclude
  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(
		  name = "pet_store_customer", 
		  joinColumns = @JoinColumn(name = "pet_store_id"), 
		  inverseJoinColumns = @JoinColumn(name = "customer_id")
  )
  private Set<Customer> customers = new HashSet<>();

  @EqualsAndHashCode.Exclude @ToString.Exclude
  @OneToMany(mappedBy = "petStore", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Employee> employees = new HashSet<>();
}
