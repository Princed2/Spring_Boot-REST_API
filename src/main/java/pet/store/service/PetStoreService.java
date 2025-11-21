package pet.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
import pet.store.entity.PetStore;

import java.util.NoSuchElementException;

@Service
public class PetStoreService {

  @Autowired
  private PetStoreDao petStoreDao;

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
}
