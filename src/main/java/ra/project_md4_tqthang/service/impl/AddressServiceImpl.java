package ra.project_md4_tqthang.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.project_md4_tqthang.dto.request.NewAddressRequest;
import ra.project_md4_tqthang.model.Address;
import ra.project_md4_tqthang.model.Users;
import ra.project_md4_tqthang.repository.IAddressRepository;
import ra.project_md4_tqthang.repository.IUserRepository;
import ra.project_md4_tqthang.service.IAddressService;
import ra.project_md4_tqthang.service.IUserService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressRepository addressRepository;
    @Autowired
    private IUserRepository userRepository;

    @Override
    public void addAddress(Long userId, NewAddressRequest addressRequest) {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("User not found"));
        Address address = new Address();
        address.setUser(users);
        address.setFullAddress(addressRequest.getFullAddress());
        address.setReceiveName(addressRequest.getReceiveName());
        address.setPhone(addressRequest.getPhone());

        addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("User not found"));
        Address address = addressRepository.findByAddressIdAndUser(addressId,users)
                .orElseThrow(()->new NoSuchElementException("Address not found"));
        addressRepository.delete(address);
    }

    @Override
    public List<Address> getAddressList(Long userId) {
        Users users = userRepository.findById(userId).orElseThrow(()->new NoSuchElementException("User not found"));
        return addressRepository.findByUser(users);
    }

    @Override
    public Address getAddress(Long userId, Long addressId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(()->new NoSuchElementException("User not found"));
        return addressRepository.findByAddressIdAndUser(addressId,users)
                .orElseThrow(()->new NoSuchElementException("Address not found"));
    }
}
