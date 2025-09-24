package com.demo.repo;

import com.demo.model.Address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

    List<Address> findByPincode(String pincode);

    List<Address> findByCity(String city);
}
