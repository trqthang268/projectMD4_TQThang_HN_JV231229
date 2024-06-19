package ra.project_md4_tqthang.service;

import ra.project_md4_tqthang.dto.request.NewAddressRequest;
import ra.project_md4_tqthang.model.Address;

import java.util.List;

public interface IAddressService {
    Address addAddress(Long userId, NewAddressRequest addressRequest);
    void deleteAddress(Long userId, Long addressId);
    List<Address> getAddressList(Long userId);
    Address getAddress(Long userId, Long addressId);
}
