package com.tinomaster.virtualdream.virtualdream.services;

import java.time.LocalDateTime;
import java.util.List;

import com.tinomaster.virtualdream.virtualdream.entities.ServiceKey;
import com.tinomaster.virtualdream.virtualdream.repositories.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualdream.dtos.ServiceDto;
import com.tinomaster.virtualdream.virtualdream.entities.ServiceEntity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceSaleRepository serviceSaleRepository;
    private final ServiceKeyRepository serviceKeyRepository;
    private final ConsumableCostRepository consumableCostRepository;
    private final ModelMapper mapper;

    public ServiceEntity findOrThrow(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("no se encuentra el servicio con el id: " + id));
    }

    @Transactional
    public ServiceEntity saveService(ServiceDto serviceDto) {
        try {
            ServiceKey serviceKey = serviceKeyRepository.save(ServiceKey.builder().build());
            ServiceEntity service = mapper.map(serviceDto, ServiceEntity.class);
            service.setServiceKey(serviceKey);

            return serviceRepository.save(service);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el servicio", e);
        }
    }

    @Transactional
    public ServiceEntity updateService(Long id, ServiceDto serviceDto) {
        ServiceEntity service = this.findOrThrow(id);
        try {
            service.setFinishedAt(LocalDateTime.now());
            serviceRepository.save(service);

            ServiceEntity serviceToSave = mapper.map(serviceDto, ServiceEntity.class);
            serviceToSave.setId(null);
            serviceToSave.setServiceKey(service.getServiceKey());
            return serviceRepository.save(serviceToSave);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el servicio con id: " + id, e);
        }
    }

    public List<ServiceEntity> getAllServiceByBusinessId(Long businessId) {
        try {
            return serviceRepository.findLastByBusinessId(businessId);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los servicios del negocio con id: " + businessId, e);
        }
    }

    @Transactional
    public void deleteService(Long id) {
        ServiceEntity service = this.findOrThrow(id);
        try {
            List<ServiceEntity> services = serviceRepository.findAllByServiceKeyId(service.getServiceKey().getId());
            int servicesSize = services.size();

            for (ServiceEntity s : services) {
                boolean existServiceInServiceSale = serviceSaleRepository.existServiceByServiceId(s.getId());

                if (!existServiceInServiceSale) {
                    if (consumableCostRepository.existsCostByServiceId(s.getId())) {
                        deleteConsumableCosts(s.getId());
                    }
                    serviceRepository.delete(s);
                    servicesSize--;
                } else {
                    if (s.getFinishedAt() == null) {
                        s.setFinishedAt(LocalDateTime.now());
                        serviceRepository.save(s);
                    }
                }
            }

            if (servicesSize == 0) {
                serviceKeyRepository.deleteById(service.getServiceKey().getId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el servicio con id: " + id, e);
        }
    }

    @Transactional
    private void deleteConsumableCosts(Long serviceId) {
        consumableCostRepository.deleteAllByServiceId(serviceId);
    }
}
