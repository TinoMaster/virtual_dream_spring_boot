package com.tinomaster.virtualdream.virtualDream.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualDream.dtos.ServiceSaleDto;
import com.tinomaster.virtualdream.virtualDream.entities.Business;
import com.tinomaster.virtualdream.virtualDream.entities.Consumable;
import com.tinomaster.virtualdream.virtualDream.entities.ConsumableCost;
import com.tinomaster.virtualdream.virtualDream.entities.Employee;
import com.tinomaster.virtualdream.virtualDream.entities.ServiceEntity;
import com.tinomaster.virtualdream.virtualDream.entities.ServiceSale;
import com.tinomaster.virtualdream.virtualDream.repositories.BusinessFinalSaleRepository;
import com.tinomaster.virtualdream.virtualDream.repositories.ConsumableRepository;
import com.tinomaster.virtualdream.virtualDream.repositories.ServiceSaleRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServiceSaleService {
	private final ServiceSaleRepository serviceSaleRepository;
	private final ConsumableRepository consumableRepository;
	private final BusinessFinalSaleRepository businessFinalSaleRepository;
	private final ModelMapper mapper;

	public ServiceSale findOrThrow(Long id) {
		return serviceSaleRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("no se encuentra el service sale con id: " + id));
	}

	public List<ServiceSale> getServiceSalesByBusinessIdAndDate(Long businessId, LocalDateTime startDate,
			LocalDateTime endDate) {
		LocalDateTime adjustStartDate = startDate.toLocalDate().atStartOfDay();
		LocalDateTime adjustEndDate = endDate.toLocalDate().atTime(LocalTime.MAX);
		return serviceSaleRepository.findByBusinessAndDateRange(businessId, adjustStartDate, adjustEndDate);
	}

	@Transactional
	public ServiceSale saveServiceSale(ServiceSaleDto serviceSaleDto) {
		ServiceSale serviceSale = mapper.map(serviceSaleDto, ServiceSale.class);

		// Obtenemos el servicio relacionado
		ServiceEntity service = serviceSale.getService();
		if (service == null || service.getCosts() == null || service.getCosts().isEmpty()) {
			throw new IllegalArgumentException("El servicio no tiene costos asociados");
		}

		// Validamos que la cantidad de la venta sea positiva
		if (serviceSale.getQuantity() == null || serviceSale.getQuantity() <= 0) {
			throw new IllegalArgumentException("La cantidad vendida debe ser mayor a 0.");
		}

		// Actualizamos el stock de cada consumable relacionado con el servicio
		for (ConsumableCost cost : service.getCosts()) {
			Consumable consumable = cost.getConsumable();
			if (consumable == null) {
				throw new IllegalArgumentException("El costo no tiene un consumable asociado.");
			}

			// Calculamos la cantidad total a descontar del stock
			Float totalQuantityToDeduct = cost.getQuantity() * serviceSale.getQuantity();

			// Validamos que haya suficiente en el stock
			if (consumable.getStock() < totalQuantityToDeduct) {
				throw new IllegalArgumentException("Stock insuficiente para el consumable: " + consumable.getName());
			}

			// Descontamos el stock
			consumable.setStock(consumable.getStock() - totalQuantityToDeduct);
		}

		// Guardamos los cambios en los consumables
		service.getCosts().forEach(cost -> {
			consumableRepository.save(cost.getConsumable());
		});

		return serviceSaleRepository.save(serviceSale);
	}

	@Transactional
	public ServiceSale updateServiceSale(Long serviceSaleId, ServiceSaleDto updatedServiceSaleDto) {
		// Obtenemos la venta del servicio por su ID
		ServiceSale existingServiceSale = serviceSaleRepository.findById(serviceSaleId).orElseThrow(
				() -> new IllegalArgumentException("La venta del servicio no existe con el ID: " + serviceSaleId));

		// Obtenemos el servicio relacionado
		ServiceEntity service = existingServiceSale.getService();
		if (service == null || service.getCosts() == null || service.getCosts().isEmpty()) {
			throw new IllegalArgumentException("El servicio no tiene costos asociados.");
		}

		// Calculamos la diferencia en la cantidad
		int oldQuantity = existingServiceSale.getQuantity();
		int newQuantity = updatedServiceSaleDto.getQuantity();
		int quantityDifference = newQuantity - oldQuantity;

		// Ajustamos el stock de cada consumable relacionado con el servicio
		for (ConsumableCost cost : service.getCosts()) {
			Consumable consumable = cost.getConsumable();
			if (consumable == null) {
				throw new IllegalArgumentException("El costo no tiene un consumable asociado.");
			}

			// Calculamos la diferencia de stock
			float stockAdjustment = cost.getQuantity() * quantityDifference;

			// Ajustamos el stock del consumable
			consumable.setStock(consumable.getStock() - stockAdjustment);

			// Guardamos los cambios en el consumable
			consumableRepository.save(consumable);
		}

		// Actualizamos los valores en la entidad existente
		existingServiceSale.setQuantity(newQuantity);
		existingServiceSale.setEmployee(mapper.map(updatedServiceSaleDto.getEmployee(), Employee.class));
		existingServiceSale.setBusinessFinalSale(updatedServiceSaleDto.getBusinessFinalSale() != null
				? businessFinalSaleRepository.findById(updatedServiceSaleDto.getBusinessFinalSale())
						.orElseThrow(() -> new IllegalArgumentException("No existe BusinessFinalSale con el ID: "
								+ updatedServiceSaleDto.getBusinessFinalSale()))
				: null);
		existingServiceSale.setBusiness(mapper.map(updatedServiceSaleDto.getBusiness(), Business.class));

		// Guardamos los cambios en la venta del servicio
		return serviceSaleRepository.save(existingServiceSale);
	}

	@Transactional
	public void deleteServiceSale(Long serviceSaleId) {
		ServiceSale serviceSale = this.findOrThrow(serviceSaleId);

		ServiceEntity service = serviceSale.getService();
		if (service == null || service.getCosts() == null || service.getCosts().isEmpty()) {
			throw new IllegalArgumentException("El servicio no tiene costos asociados.");
		}

		// Revertimos el stock de cada consumable relacionado con el servicio
		for (ConsumableCost cost : service.getCosts()) {
			Consumable consumable = cost.getConsumable();
			if (consumable == null) {
				throw new IllegalArgumentException("El costo no tiene un consumable asociado.");
			}

			// Calculamos la cantidad total a restaurar en el stock
			float totalQuantityToRestore = cost.getQuantity() * serviceSale.getQuantity();

			// Restauramos el stock
			consumable.setStock(consumable.getStock() + totalQuantityToRestore);

			// Guardamos los cambios en el consumable
			consumableRepository.save(consumable);
		}

		// Eliminamos la venta del servicio
		serviceSaleRepository.deleteById(serviceSaleId);
	}

}
