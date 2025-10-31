package com.demo.service;

import com.demo.model.Address.Address;
import com.demo.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    UserService userService;

    public List<Address> findUserAddresses(Long userId){
        Optional<User> optional = userService.findUserById(userId);
        if(optional.isEmpty()){
            return new ArrayList<>();
        }
        User user = optional.get();
        return user.getAddresses();
    }

    public String addAddressToUser(Long userId, Address address){
        Optional<User> optional = userService.findUserById(userId);
        if(optional.isEmpty()){
            return "Could Not Locate Resource: User";
        }
        User user = optional.get();
        user.getAddresses().add(address);
        return "Address Added Successfully";
    }

    public String updateUserAddress(Long userId, Integer addressIndex, Address address){
        Optional<User> optional = userService.findUserById(userId);
        if(optional.isEmpty()){
            return "Could Not Locate Resource: User";
        }
        User user = optional.get();
        if(addressIndex < 0 || addressIndex >= user.getAddresses().size()){
            return "Could Not Locate Resource: Address";
        }
        user.getAddresses().set(addressIndex, address);
        userService.saveUser(user);
        return "Address Updated Successfully";
    }

    public String deleteUserAddress(Long userId, Integer addressIndex){
        Optional<User> optional = userService.findUserById(userId);
        if(optional.isEmpty()){
            return "Could Not Locate Resource: User";
        }
        User user = optional.get();
        if(addressIndex < 0 || addressIndex >= user.getAddresses().size()){
            return "Could Not Locate Resource: Address";
        }
        user.getAddresses().remove(addressIndex.intValue());
        userService.saveUser(user);
        return "Address Removed Successfully";
    }

    public String patchUserAddress(Long userId,Integer addressIndex, Address address){
        Optional<User> optional = userService.findUserById(userId);
        if(optional.isEmpty()){
            return "Could Not Locate Resource: User";
        }
        User user = optional.get();
        if(addressIndex < 0 || addressIndex >= user.getAddresses().size()){
            return "Could Not Locate Resource: Address";
        }
        Address updatedAddress = user.getAddresses().get(addressIndex);

        updatedAddress.setPincode(address.getPincode());

        if(address.getStreetAddress() != null) updatedAddress.setStreetAddress(address.getStreetAddress());

        if(address.getCity() != null) updatedAddress.setCity(address.getCity());

        if(address.getState() != null) updatedAddress.setState(address.getState());

        user.getAddresses().set(addressIndex, updatedAddress);
        userService.saveUser(user);
        return "Address Updated Successfully";
    }
}
