package pet.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.PetStore;
import pet.store.entity.Employee;
import pet.store.entity.Customer;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PetStoreService {

  @Autowired
  private PetStoreDao petStoreDao;
  
  @Autowired
  private EmployeeDao employeeDao;
  
  @Autowired
  private CustomerDao customerDao;

//saPetStore method
  // Step 5: required by the instructions
  public PetStoreData savePetStore(PetStoreData dto) {
    PetStore petStore = findOrCreatePetStore(dto.getId()); // 5a
    copyPetStoreFields(petStore, dto);                     // 5b
    PetStore saved = petStoreDao.save(petStore);           // 5c
    return new PetStoreData(saved);
  }

  // 5a
  private PetStore findOrCreatePetStore(Long id) {
    if (id == null) {
      return new PetStore(); // insert
    }
    return petStoreDao.findById(id)
        .orElseThrow(() -> new NoSuchElementException(
            "Pet store with ID=" + id + " was not found"));
  }

  // 5b – do NOT copy customers/employees
  private void copyPetStoreFields(PetStore t, PetStoreData s) {
    t.setStoreName(s.getStoreName());
    t.setStoreAddress(s.getStoreAddress());
    t.setStoreCity(s.getStoreCity());
    t.setStoreState(s.getStoreState());
    t.setStoreZip(s.getStoreZip());
    t.setStorePhone(s.getStorePhone());
  }
  
 //saveEmployee method 
  @Transactional(readOnly = false)
  public PetStoreData.PetStoreEmployee saveEmployee(
		  Long petStoreId,PetStoreData.PetStoreEmployee dto) {

      // b) find the pet store
      PetStore petStore = findPetStoreById(petStoreId);

      // c) get/create the employee
      Long employeeId = dto.getId();
      Employee employee = findOrCreateEmployee(petStoreId, employeeId);

      // d) copy fields from DTO -> entity
      copyEmployeeFields(employee, dto);

      // e–g) set relationships, add to store’s set, save
      employee.setPetStore(petStore);
      petStore.getEmployees().add(employee);
      Employee saved = employeeDao.save(employee);

      // h) return DTO built from saved entity
      return new PetStoreData.PetStoreEmployee(saved);
  }
  

  private PetStore findPetStoreById(Long id) {
	    return petStoreDao.findById(id)
	        .orElseThrow(() -> new NoSuchElementException("Pet store with ID=" + id + " was not found"));
	}
  
  
  
  
  private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
      if (employeeId == null) {
          return new Employee();                // creating a new employee
      }
      return findEmployeeById(petStoreId, employeeId);
  }

  private Employee findEmployeeById(Long petStoreId, Long employeeId) {
      Employee emp = employeeDao.findById(employeeId)
          .orElseThrow(() -> new NoSuchElementException("Employee with ID=" + employeeId + " was not found"));

      // ensure the employee belongs to the given store
      if (emp.getPetStore() == null || !emp.getPetStore().getId().equals(petStoreId)) {
          throw new IllegalArgumentException(
              "Employee " + employeeId + " does not belong to pet store " + petStoreId);
      }
      return emp;
  }

  private void copyEmployeeFields(Employee target, PetStoreData.PetStoreEmployee src) {
      target.setFirstName(src.getFirstName());
      target.setLastName(src.getLastName());
      target.setJobTitle(src.getJobTitle());
      target.setPhone(src.getPhone());
      // ID comes from DB; for updates we located the entity by ID already
  }
  
  
//saveCustomer method
  @Transactional(readOnly = false)
  public PetStoreData.PetStoreCustomer saveCustomer(
          Long petStoreId, PetStoreData.PetStoreCustomer dto) {

      // b) pet store must exist
      PetStore petStore = findPetStoreById(petStoreId);

      // c) existing or new customer
      Long customerId = dto.getId();
      Customer customer = findOrCreateCustomer(petStoreId, customerId);

      // d) copy fields
      copyCustomerFields(customer, dto);

      // e–f) set both sides of many-to-many
      customer.getPetStores().add(petStore);
      petStore.getCustomers().add(customer);

      // g) save
      Customer saved = customerDao.save(customer);

      // h) return DTO
      return new PetStoreData.PetStoreCustomer(saved);
  }

//Helpers
  private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
	    if (customerId == null) {
	        return new Customer();
	    }
	    return findCustomerById(petStoreId, customerId);
	}

	private Customer findCustomerById(Long petStoreId, Long customerId) {
	    Customer cust = customerDao.findById(customerId)
	        .orElseThrow(() -> new NoSuchElementException(
	            "Customer with ID=" + customerId + " was not found"));

	    // many-to-many: ensure the customer is linked to this store
	    boolean belongsToStore = cust.getPetStores().stream()
	        .anyMatch(ps -> ps.getId().equals(petStoreId));
	    if (!belongsToStore) {
	        throw new IllegalArgumentException(
	            "Customer " + customerId + " is not associated with pet store " + petStoreId);
	    }
	    return cust;
	}

	private void copyCustomerFields(Customer target, PetStoreData.PetStoreCustomer src) {
	    target.setFirstName(src.getFirstName());
	    target.setLastName(src.getLastName());
	    target.setCustomerEmail(src.getCustomerEmail());
	}
//retrieveAllPetStores method
	@Transactional(readOnly = true)
	public List<pet.store.controller.model.PetStoreData> retrieveAllPetStores() {
	    List<pet.store.entity.PetStore> stores = petStoreDao.findAll();

	    List<pet.store.controller.model.PetStoreData> result = new LinkedList<>();
	    for (pet.store.entity.PetStore ps : stores) {
	        pet.store.controller.model.PetStoreData dto =
	                new pet.store.controller.model.PetStoreData(ps);

	        // this allows for summary only & remove nested collections
	        dto.getCustomers().clear();
	        dto.getEmployees().clear();

	        result.add(dto);
	    }
	    return result;
	}
	
	// 1) Retrieve a single pet store (includes customers & employees)
	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
	  PetStore store = findPetStoreById(petStoreId);   
	  return new PetStoreData(store);
	}

	// 2) Delete a pet store
	@Transactional
	public void deletePetStoreById(Long petStoreId) {
	  PetStore store = findPetStoreById(petStoreId);  
	  petStoreDao.delete(store);                       
	}



}
