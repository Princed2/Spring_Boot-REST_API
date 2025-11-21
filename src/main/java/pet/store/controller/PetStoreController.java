package pet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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

}
