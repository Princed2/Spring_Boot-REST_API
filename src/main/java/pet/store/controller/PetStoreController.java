package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;


import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.service.PetStoreService;

@RestController @RequestMapping("/pet_store") @Slf4j
public class PetStoreController {
	
	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping @ResponseStatus(HttpStatus.CREATED)
	public PetStoreData createPetStore(@RequestBody PetStoreData petStore) {
		log.info("POST /pet_store: {}", petStore);
		return petStoreService.savePetStore(petStore);
	}
	
	@PutMapping("/{petStoreId}")
	public PetStoreData updatePetStore(
			@PathVariable Long petStoreId,
			@RequestBody PetStoreData petStore) {
	  log.info("PUT /pet_store/{}: {}", petStoreId, petStore);
	  petStore.setId(petStoreId);              // take the ID from the URL
	  return petStoreService.savePetStore(petStore); // reuse service save for update
	}
	
	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(HttpStatus.CREATED)
	public PetStoreData.PetStoreEmployee addEmployee(
			@PathVariable Long petStoreId,
			@RequestBody PetStoreData.PetStoreEmployee employee) {
		log.info("POST /pet_store/{}/employee: {}", petStoreId, employee);
		return petStoreService.saveEmployee(petStoreId, employee);
	}
	
	@PostMapping("/{petStoreId}/customer")
	@ResponseStatus(HttpStatus.CREATED)
	public PetStoreData.PetStoreCustomer addCustomer(
	        @PathVariable Long petStoreId,
	        @RequestBody PetStoreData.PetStoreCustomer customer) {
	    log.info("POST /pet_store/{}/customer: {}", petStoreId, customer);
	    return petStoreService.saveCustomer(petStoreId, customer);
	}
	
	@GetMapping
	public List<PetStoreData> listPetStores() {
	    log.info("GET /pet_store (summary list)");
	    return petStoreService.retrieveAllPetStores();
	}
	
	// 1) Retrieve a single pet store by ID
	@GetMapping("/{petStoreId}")
	public PetStoreData getPetStore(@PathVariable Long petStoreId) {
	  log.info("GET /pet_store/{}", petStoreId);
	  return petStoreService.retrievePetStoreById(petStoreId);
	}

	// 2) Delete a pet store by ID
	@DeleteMapping("/{petStoreId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
	  log.info("DELETE /pet_store/{}", petStoreId);
	  petStoreService.deletePetStoreById(petStoreId);
	  return Map.of("message", "Pet store " + petStoreId + " deleted successfully");
	}




}
