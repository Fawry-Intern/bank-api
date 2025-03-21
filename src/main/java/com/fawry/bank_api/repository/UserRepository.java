package com.fawry.bank_api.repository;

import com.fawry.bank_api.dto.user.UserDetailsResponse;
import com.fawry.bank_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
   boolean existsByEmail(String username);
   Optional<User>findByEmail(String email);


   @Query("""
      select u
      from User u 
      join u.account a 
      where a.isActive = true
""")
   List<User> getUsersWithActiveAccounts();


   @Query("""
      select u
      from User u 
      join u.account a 
      where a.isActive = false 
""")
  List<User> getUsersWithUnActiveAccounts();

}
