package cn.chenlizhong.service;

import cn.chenlizhong.sinno.helper.DatabaseHelper;
import cn.chenlizhong.model.Customer;

import java.util.List;
import java.util.Map;

/**
 * @author : lizhong.chen
 * @version : 1.0
 *          Descr : 客户数据服务
 * @since : 15/12/20 下午7:04
 */
public class CustomService {
    public List<Customer> getCustomerList() {
        String sql = "select * from customer";
        return DatabaseHelper.queryEntityList(Customer.class, sql);
    }

    public Customer getCustomer(long id) {
        //TODO
        return null;
    }

    public boolean createCustomer(Map<String, Object> fieldMap) {
        //TODO
        return false;
    }

    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        //TODO
        return false;
    }

    public boolean deleteCustomer(long id) {
        //TODO
        return false;
    }
}
