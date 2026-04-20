package com.example.CorporateTravelManagementSystem.Service;
import java.util.List;
import com.example.CorporateTravelManagementSystem.entity.TravelApproval;
public interface TravelApprovalService {
    public List<TravelApproval>getPendingApprovalsByManager(Long managerId);
    public List<TravelApproval>getPendingApprovalsByFinance();  
    public TravelApproval approveByManager(Long id);
    public TravelApproval rejectByManager(Long id);
    public TravelApproval approveByFinance(Long id);
    public TravelApproval rejectByFinance(Long id);

    
}
