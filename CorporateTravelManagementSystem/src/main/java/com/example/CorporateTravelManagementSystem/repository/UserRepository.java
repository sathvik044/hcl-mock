package com.example.CorporateTravelManagementSystem.Repository;
import java.util.Optional; 
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.CorporateTravelManagementSystem.entity.User;
import com.example.CorporateTravelManagementSystem.enums.UserRole;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
      
    Optional<User> findByEmail(String email);


    List<User> findByRole(UserRole role);

    
    List<User> findByDepartment(String department);


    List<User> findByCostCenter(String costCenter);

    
    List<User> findByManager_Id(Long managerId);

   
    boolean existsByEmail(String email);
    
}
