package cn.chenlizhong.test;

import cn.chenlizhong.model.Customer;
import cn.chenlizhong.service.CustomService;
import cn.chenlizhong.sinno.helper.DatabaseHelper;
import org.junit.Assert;

import java.util.List;

/**
 * @author : lizhong.chen
 * @version : 1.0
 *          Descr : CustomerService单元测试
 * @since : 15/12/20 下午7:09
 */

public class CustomerServiceTest {
    private final CustomService customService;

    public CustomerServiceTest() {
        customService = new CustomService();
    }

    //    @Before
    public void init() {
        //TODO 初始化数据库
        DatabaseHelper.executeSqlFile("sql/customer_init.sql");
    }

    //    @Test
    public void getCustomerListTest() {
        List<Customer> customerList = customService.getCustomerList();
        Assert.assertEquals(2, customerList.size());
    }


}
