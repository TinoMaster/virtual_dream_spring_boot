package com.tinomaster.virtualdream.virtualdream.services.interfaces;

import com.tinomaster.virtualdream.virtualdream.dtos.DebtDto;
import com.tinomaster.virtualdream.virtualdream.entities.Debt;

import java.util.List;

public interface DebtServiceInterface {

    public Float getTotalUnpaidDebtsByBusinessId(Long businessId);

    public List<Debt> getDebtsByBusinessId(Long businessId);

    public List<Debt> getPendingDebtsByBusinessId(Long businessId);

    public Debt createDebt(DebtDto debt);

    public Debt updateDebt(DebtDto debt);

    public void deleteDebt(Long id);
}
