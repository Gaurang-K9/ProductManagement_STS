package com.demo.service;

import com.demo.model.Address.Address;
import com.demo.repo.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    AddressRepo addressRepo;

    public List<Address> findAllAddress(){
        return addressRepo.findAll();
    }

    public Optional<Address> findAddressById(Long id){
        return addressRepo.findById(id);
    }

    public List<Address> findAddressByPincode(String pincode){
        return addressRepo.findByPincode(pincode);
    }

    public List<Address> findAddressByCity(String city){
        return addressRepo.findByCity(city);
    }

    public Address addAddress(Address address){
        return addressRepo.save(address);
    }

    public String deleteAddressById(Long id){
        Optional<Address> optional = addressRepo.findById(id);
        if(optional.isEmpty()){
            return "Could Not Locate Resource";
        }
        addressRepo.deleteById(id);
        return "Address Removed Successfully";
    }

    public String updateAddress(Long id, Address updatedAddress) {
        Optional<Address> optional = addressRepo.findById(id);
        if(optional.isEmpty()){
            return "Could Not Locate Resource";
        }
        Address address = optional.get();

        address.setStreetAddress(updatedAddress.getStreetAddress());
        address.setPincode(updatedAddress.getPincode());
        address.setCity(updatedAddress.getCity());
        address.setState(updatedAddress.getState());
        addressRepo.save(address);
        return "Address Updated Successfully";
    }

    public String patchAddress(Long id, Address updatedFields) {
        Optional<Address> optional = addressRepo.findById(id);
        if(optional.isEmpty()){
            return "Could Not Locate Resource";
        }
        Address address = optional.get();

        if (updatedFields.getStreetAddress() != null) {
            address.setStreetAddress(updatedFields.getStreetAddress());
        }
        if (updatedFields.getCity() != null) {
            address.setCity(updatedFields.getCity());
        }
        if (updatedFields.getState() != null) {
            address.setState(updatedFields.getState());
        }
        if (updatedFields.getPincode() != null) {
            address.setPincode(updatedFields.getPincode());
        }
        addressRepo.save(address);
        return "Address Updated Successfully";
    }
}
