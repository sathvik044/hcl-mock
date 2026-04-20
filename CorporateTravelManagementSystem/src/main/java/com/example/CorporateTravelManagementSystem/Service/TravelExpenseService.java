package com.example.CorporateTravelManagementSystem.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.CorporateTravelManagementSystem.Exception.TravelExpenseNotFoundException;
import com.example.CorporateTravelManagementSystem.Exception.TravelExpenseStateException;
import com.example.CorporateTravelManagementSystem.Exception.TravelRequestNotFoundException;
import com.example.CorporateTravelManagementSystem.Exception.UserNotFoundException;
import com.example.CorporateTravelManagementSystem.Repository.TravelExpenseRepository;
import com.example.CorporateTravelManagementSystem.Repository.TravelRequestRepository;
import com.example.CorporateTravelManagementSystem.Repository.UserRepository;
import com.example.CorporateTravelManagementSystem.dto.TravelExpenseRequestDTO;
import com.example.CorporateTravelManagementSystem.dto.TravelExpenseResponseDTO;
import com.example.CorporateTravelManagementSystem.entity.TravelExpense;
import com.example.CorporateTravelManagementSystem.entity.TravelRequestEntity;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.ExpenseStatus;
import com.example.CorporateTravelManagementSystem.mapper.TravelExpenseMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelExpenseService {

    private final TravelExpenseRepository expenseRepository;
    private final TravelRequestRepository requestRepository;
    private final UserRepository userRepository;

    public TravelExpenseResponseDTO create(TravelExpenseRequestDTO dto) {

        TravelRequestEntity request = requestRepository.findById(dto.getTravelRequestId())
                .orElseThrow(() -> new TravelRequestNotFoundException("Travel request not found with id: " + dto.getTravelRequestId()));

        User user = userRepository.findById(dto.getClaimedBy())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + dto.getClaimedBy()));

        if (dto.getAmount() > 1000 && (dto.getReceiptUrl() == null || dto.getReceiptUrl().isEmpty())) {
            throw new TravelExpenseStateException("Receipt required for expenses above 1000");
        }

        TravelExpense expense = TravelExpenseMapper.toEntity(dto, request, user);

        return TravelExpenseMapper.toDTO(expenseRepository.save(expense));
    }
    public List<TravelExpenseResponseDTO> getByRequest(Long requestId) {

        return expenseRepository.findByTravelRequestId(requestId)
                .stream()
                .map(TravelExpenseMapper::toDTO)
                .collect(Collectors.toList());
    }
    public TravelExpenseResponseDTO verify(Long id, Long financeUserId) {

        TravelExpense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new TravelExpenseNotFoundException("Expense not found with id: " + id));

        User finance = userRepository.findById(financeUserId)
                .orElseThrow(() -> new UserNotFoundException("Finance user not found with id: " + financeUserId));

        expense.setStatus(ExpenseStatus.VERIFIED);
        expense.setVerifiedBy(finance);

        return TravelExpenseMapper.toDTO(expenseRepository.save(expense));
    }
    public TravelExpenseResponseDTO reimburse(Long id) {

        TravelExpense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new TravelExpenseNotFoundException("Expense not found with id: " + id));

        if (expense.getStatus() != ExpenseStatus.VERIFIED) {
            throw new TravelExpenseStateException("Only verified expenses can be reimbursed");
        }

        expense.setStatus(ExpenseStatus.REIMBURSED);

        return TravelExpenseMapper.toDTO(expenseRepository.save(expense));
    }
}
