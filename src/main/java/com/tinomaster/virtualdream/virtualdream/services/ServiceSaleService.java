package com.tinomaster.virtualdream.virtualdream.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualdream.dtos.ServiceSaleDto;
import com.tinomaster.virtualdream.virtualdream.entities.Consumable;
import com.tinomaster.virtualdream.virtualdream.entities.ConsumableCost;
import com.tinomaster.virtualdream.virtualdream.entities.Employee;
import com.tinomaster.virtualdream.virtualdream.entities.ServiceEntity;
import com.tinomaster.virtualdream.virtualdream.entities.ServiceSale;
import com.tinomaster.virtualdream.virtualdream.entities.User;
import com.tinomaster.virtualdream.virtualdream.enums.ERole;
import com.tinomaster.virtualdream.virtualdream.repositories.ConsumableRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.EmployeeRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.ServiceSaleRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServiceSaleService {
	private final ServiceSaleRepository serviceSaleRepository;
	private final ConsumableRepository consumableRepository;
	private final EmployeeRepository employeeRepository;
	private final JwtService jwtService;
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
		Employee employee = employeeRepository.findById(serviceSaleDto.getEmployee().getId()).orElseThrow(
				() -> new RuntimeException("no fue posible encontrar el employee asociado a la venta del servicio"));
		serviceSale.setEmployee(employee);

		// Obtenemos el servicio relacionado
		ServiceEntity service = serviceSale.getService();
		if (service == null) {
			throw new IllegalArgumentException("Tiene que haber un servicio asociado a la venta de servicio");
		}

		// Validamos que la cantidad de la venta sea positiva
		if (serviceSale.getQuantity() == null || serviceSale.getQuantity() <= 0) {
			throw new IllegalArgumentException("La cantidad vendida debe ser mayor a 0.");
		}

		// Actualizamos el stock de cada consumable relacionado con el servicio
		if (service.getCosts() != null || !service.getCosts().isEmpty()) {
			for (ConsumableCost cost : service.getCosts()) {
				Consumable consumable = cost.getConsumable();
				if (consumable == null) {
					throw new IllegalArgumentException("El costo no tiene un consumable asociado.");
				}

				// Calculamos la cantidad total a descontar del stock
				Float totalQuantityToDeduct = cost.getQuantity() * serviceSale.getQuantity();

				// Validamos que haya suficiente en el stock
				if (consumable.getStock() < totalQuantityToDeduct) {
					throw new IllegalArgumentException(
							"Stock insuficiente para el consumable: " + consumable.getName());
				}

				// Descontamos el stock
				consumable.setStock(consumable.getStock() - totalQuantityToDeduct);
			}

			// Guardamos los cambios en los consumables
			service.getCosts().forEach(cost -> {
				consumableRepository.save(cost.getConsumable());
			});
		}

		return serviceSaleRepository.save(serviceSale);
	}

	@Transactional
	public ServiceSale updateServiceSale(ServiceSaleDto updatedServiceSaleDto) {

		// Obtenemos la venta del servicio por su ID
		ServiceSale existingServiceSale = serviceSaleRepository.findById(updatedServiceSaleDto.getId())
				.orElseThrow(() -> new IllegalArgumentException(
						"La venta del servicio no existe con el ID: " + updatedServiceSaleDto.getId()));

		if (existingServiceSale.getBusinessFinalSale() != null) {
			throw new RuntimeException(
					"No es posible actualizar un servicio vendido ya haya sido registrado en un businessFinalSale");
		}

		User user = jwtService.getUserAuthenticated();
		ERole role = user.getRole();

		// Verificamos que el usuario este autorizado a actualizar este servicio de
		// venta
		if (role != ERole.OWNER && role != ERole.ADMIN
				&& existingServiceSale.getEmployee().getUser().getId() != user.getId()) {
			throw new IllegalArgumentException("El usuario no tiene permisos para actualizar el servicio de venta.");
		}

		// Obtenemos el servicio relacionado
		ServiceEntity service = existingServiceSale.getService();
		if (service == null) {
			throw new IllegalArgumentException("Tiene que haber un servicio asociado a una venta de servicio.");
		}

		if (service.getCosts() != null && !service.getCosts().isEmpty()) {
			if (service.getId() != updatedServiceSaleDto.getService().getId()) {
				throw new IllegalArgumentException("No puede cambiar el servicio en una actualizacion de servicio");
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
		}
		existingServiceSale.setQuantity(updatedServiceSaleDto.getQuantity());

		return serviceSaleRepository.save(existingServiceSale);
	}

	@Transactional
	public void deleteServiceSale(Long serviceSaleId, boolean force) {
		ServiceSale serviceSale = this.findOrThrow(serviceSaleId);

		if (!force && serviceSale.getBusinessFinalSale() != null) {
			throw new RuntimeException(
					"No es posible eliminar un servicio vendido ya haya sido registrado en un businessFinalSale");
		}

		User user = jwtService.getUserAuthenticated();

		if (user.getRole() != ERole.OWNER && user.getRole() != ERole.ADMIN) {
			throw new IllegalArgumentException("El usuario no tiene permisos para eliminar un servicio de venta.");
		}

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

		serviceSaleRepository.deleteById(serviceSaleId);
	}

}
