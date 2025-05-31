package com.tinomaster.virtualdream.virtualdream.services.interfaces;

import com.tinomaster.virtualdream.virtualdream.dtos.BusinessDto;
import com.tinomaster.virtualdream.virtualdream.entities.Business;

import java.util.List;

public interface BusinessServiceInterface {

    public Business findOrThrow(Long id);

    public List<Business> getBusinessesByUserId(Long userId);

    public List<Business> findBusinessRequests();

    public void acceptBusinessRequest(Long ownerId);

    public void rejectBusinessRequest(Long ownerId);

    public Business getBusinessById(Long businessId);

    public void deleteBusinessesByUserId(Long userId);

    public List<Business> getAllBusinesses();

    public Business saveBusiness(BusinessDto businessDto);

    public Business updateBusiness(Long businessId, BusinessDto businessDto);

    public void deleteBusiness(Long businessId);
}
