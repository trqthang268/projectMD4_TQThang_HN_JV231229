package ra.project_md4_tqthang.service;

import ra.project_md4_tqthang.dto.request.CheckoutRequest;
import ra.project_md4_tqthang.exception.CustomException;

public interface IOrderService {
    void checkout(Long userId, CheckoutRequest checkoutRequest) throws CustomException;
}
