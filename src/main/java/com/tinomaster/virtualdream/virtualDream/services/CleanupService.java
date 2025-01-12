package com.tinomaster.virtualdream.virtualDream.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.entities.Consumable;
import com.tinomaster.virtualdream.virtualDream.entities.ConsumableCost;
import com.tinomaster.virtualdream.virtualDream.entities.ServiceEntity;
import com.tinomaster.virtualdream.virtualDream.entities.ServiceSale;
import com.tinomaster.virtualdream.virtualDream.repositories.ConsumableRepository;
import com.tinomaster.virtualdream.virtualDream.repositories.ServiceSaleRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CleanupService {

	private final ServiceSaleRepository serviceSaleRepository;
	private final ConsumableRepository consumableRepository;

	/**
	 * Elimina registros donde `businessFinalSale` es NULL y han pasado más de un
	 * día.
	 */
	@Transactional
	public int deleteOldServiceSales() {
		try {
			List<ServiceSale> oldRegisters = serviceSaleRepository
					.findByBusinessFinalSaleIsNullAndCreatedAtBefore(LocalDateTime.now().minusDays(1));

			if (oldRegisters != null && !oldRegisters.isEmpty()) {
				for (ServiceSale serviceSale : oldRegisters) {
					ServiceEntity service = serviceSale.getService();
					if (service != null && service.getCosts() != null && !service.getCosts().isEmpty()) {
						for (ConsumableCost cost : service.getCosts()) {
							Consumable consumable = cost.getConsumable();
							if (consumable == null) {
								throw new IllegalArgumentException("El costo no tiene un consumable asociado.");
							}

							float totalQuantityToRestore = cost.getQuantity() * serviceSale.getQuantity();
							consumable.setStock(consumable.getStock() + totalQuantityToRestore);
							consumableRepository.save(consumable);
						}
					}
					serviceSaleRepository.deleteById(serviceSale.getId());
				}
			}

			return oldRegisters.size();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error en la funcion de borrar los registros pasados");
			return 0;
		}

	}
}
