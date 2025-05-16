package com.tinomaster.virtualdream.virtualdream.services;

import java.time.LocalDateTime;
import java.util.List;

import com.tinomaster.virtualdream.virtualdream.dtos.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualdream.entities.Address;
import com.tinomaster.virtualdream.virtualdream.entities.Business;
import com.tinomaster.virtualdream.virtualdream.entities.Employee;
import com.tinomaster.virtualdream.virtualdream.entities.User;
import com.tinomaster.virtualdream.virtualdream.repositories.BusinessRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.EmployeeRepository;
import com.tinomaster.virtualdream.virtualdream.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    private final AddressService addressService;

    private final ModelMapper mapper;

    public Business findOrThrow(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encuentra el business con id: " + id));
    }

    public List<Business> getBusinessesByUserId(Long userId) {
        return businessRepository.findBusinessesByOwnerId(userId);
    }

    public Business getBusinessById(Long businessId) {
        return businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("No se encuentra el business con id " + businessId));
    }

    public void deleteBusinessesByUserId(Long userId) {
        businessRepository.deleteBusinessesByUserId(userId);
    }

    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    @Transactional
    public Business saveBusiness(BusinessDto businessDto) {
        Address address = addressService.saveAddress(businessDto.getAddress());

        User user = userRepository.findById(businessDto.getOwner()).orElseThrow(
                () -> new RuntimeException("No se encuentra el user con el id: " + businessDto.getOwner()));

        Business business = Business.builder().address(address).owner(user).name(businessDto.getName())
                .phone(businessDto.getPhone()).description(businessDto.getDescription()).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();

        return businessRepository.save(business);
    }

    public Business updateBusiness(Long businessId, BusinessDto businessDto) {
        Business business = findOrThrow(businessId);

        try {
            business.setName(businessDto.getName());
            business.setPhone(businessDto.getPhone());
            business.setDescription(businessDto.getDescription());
            business.setUpdatedAt(LocalDateTime.now());
            return businessRepository.save(business);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el negocio", e);
        }
    }

    @Transactional
    public void deleteBusiness(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el negocio con id: " + businessId));

        Long ownerId = mapper.map(business, BusinessDto.class).getOwner();

        // Obtener la lista de negocios del mismo propietario, excluyendo el negocio
        // actual
        List<Business> businessListByOwnerId = businessRepository.findBusinessesByOwnerId(ownerId).stream()
                .filter(bus -> !bus.getId().equals(businessId)) // Comprobar con equals para objetos Long
                .toList();

        List<User> employessInTheBusiness = business.getUsers();

        // Verificar si los empleados están asociados a otros negocios del propietario
        for (User user : employessInTheBusiness) {
            boolean isAssociatedWithOtherBusinesses = businessListByOwnerId.stream()
                    .anyMatch(otherBusiness -> otherBusiness.getUsers().contains(user));

            // Si no está asociado a otros negocios, eliminar al usuario
            if (!isAssociatedWithOtherBusinesses) {
                Employee employee = employeeRepository.findByUserId(user.getId());
                employeeRepository.delete(employee);
            }
        }

        businessRepository.deleteById(businessId);
    }
}
