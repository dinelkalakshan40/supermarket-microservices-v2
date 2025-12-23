package lk.ijse.inventoryservice.controller;

import lk.ijse.inventoryservice.dto.InventoryDTO;
import lk.ijse.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public InventoryDTO saveInventory(@RequestBody InventoryDTO dto) {
        return inventoryService.createInventory(dto);
    }
    @GetMapping
    public List<InventoryDTO> getAll() {
        return inventoryService.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public InventoryDTO getById(@PathVariable String id) {
        return inventoryService.getById(id);
    }
    @PutMapping("/{id}")
    public InventoryDTO update(
            @PathVariable String id,
            @RequestBody InventoryDTO dto) {
        return inventoryService.updateInventory(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable String id) {
        inventoryService.delete(id);
        return ResponseEntity.ok("Inventory deleted successfully");
    }
}
