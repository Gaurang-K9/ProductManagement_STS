package com.demo.repo;

import com.demo.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Page<User> findByAddresses_Pincode(String pincode, Pageable pageable);

    Page<User> findByAddresses_City(String city, Pageable pageable);
}
