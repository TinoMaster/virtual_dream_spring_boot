package com.tinomaster.virtualdream.virtualdream.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.tinomaster.virtualdream.virtualdream.entities.ConsumableKey;
import com.tinomaster.virtualdream.virtualdream.repositories.ConsumableCostRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.ConsumableKeyRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualdream.dtos.ConsumableDto;
import com.tinomaster.virtualdream.virtualdream.entities.Consumable;
import com.tinomaster.virtualdream.virtualdream.repositories.ConsumableRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConsumableService {

    private final ConsumableRepository consumableRepository;
    private final ConsumableKeyRepository consumableKeyRepository;
    private final ConsumableCostRepository consumableCostRepository;
    private final ModelMapper mapper;

    public Consumable findOrThrow(Long id) {
        return consumableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("no se encuentra el consumable con id: " + id));
    }

    public List<Consumable> getConsumablesByBusinessId(Long businessId) {
        try {
            return consumableRepository.findLastByBusinessId(businessId);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los consumables del negocio con id: " + businessId, e);
        }
    }

    @Transactional
    public Consumable saveConsumable(ConsumableDto consumableDto) {
        try {
            ConsumableKey consumableKey = consumableKeyRepository.save(ConsumableKey.builder().build());
            Consumable consumable = mapper.map(consumableDto, Consumable.class);
            consumable.setConsumableKey(consumableKey);

            return consumableRepository.save(consumable);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el consumable", e);
        }
    }

    @Transactional
    public Consumable updateConsumable(Long id, ConsumableDto consumableDto) {
        Consumable consumable = findOrThrow(id);
        try {
            consumable.setFinishedAt(LocalDateTime.now());
            consumableRepository.save(consumable);

            Consumable consumableToSave = mapper.map(consumableDto, Consumable.class);
            consumableToSave.setId(null);
            consumableToSave.setConsumableKey(consumable.getConsumableKey());
            return consumableRepository.save(consumableToSave);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el consumable con id: " + id, e);
        }
    }

    @Transactional
    public void deleteConsumable(Long id) {
        Consumable consumable = findOrThrow(id);
        try {
            List<Consumable> consumables = consumableRepository.findAllByConsumableKey(consumable.getConsumableKey().getId());
            int consumablesSize = consumables.size();

            for (Consumable c : consumables) {
                if (!consumableCostRepository.existsCostByConsumableId(c.getId())) {
                    consumableRepository.delete(c);
                    consumablesSize--;
                } else {
                    if (c.getFinishedAt() == null) {
                        c.setFinishedAt(LocalDateTime.now());
                        consumableRepository.save(c);
                    }
                }
            }

            if (consumablesSize == 0) {
                consumableKeyRepository.deleteById(consumable.getConsumableKey().getId());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el consumable con id: " + id, e);
        }
    }
}
