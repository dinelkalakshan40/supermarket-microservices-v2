package lk.ijse.inventoryservice.service;

import lk.ijse.inventoryservice.dto.InventoryDTO;
import lk.ijse.inventoryservice.model.Inventory;
import lk.ijse.inventoryservice.repo.InventoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        Inventory inventory = modelMapper.map(inventoryDTO, Inventory.class);
        Inventory saved =inventoryRepo.save(inventory);
        return modelMapper.map(saved, InventoryDTO.class);
    }
    public List<InventoryDTO> getAll() {
        return inventoryRepo.findAll()
                .stream()
                .map(inv -> modelMapper.map(inv, InventoryDTO.class))
                .toList();
    }
    public InventoryDTO getById(String id) {
        Inventory inventory = inventoryRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found"));
        return modelMapper.map(inventory, InventoryDTO.class);
    }
    public InventoryDTO updateInventory(String id, InventoryDTO dto) {

        Inventory existing = inventoryRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found"));

        existing.setStockId(dto.getStockId());
        existing.setProductId(dto.getProductId());
        existing.setQuantity(dto.getQuantity());
        existing.setWarehouse(dto.getWarehouse());

        Inventory updated = inventoryRepo.save(existing);
        return modelMapper.map(updated, InventoryDTO.class);
    }
    public void delete(String id) {
        if (!inventoryRepo.existsById(id)) {
            throw new RuntimeException("Inventory not found");
        }
        inventoryRepo.deleteById(id);
    }
}
