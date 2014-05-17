package com.agilehackathon.login;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class CustomerDaoTest {

    CustomerDao customerDao = new CustomerDao();

    @Test
    public void returnsTrueForRegisteredCustomer() throws Exception {
        givenARegisteredCustomer("username");

        boolean isCustomerRegistered = customerDao.isCustomerRegistered("username");

        assertThat(isCustomerRegistered).isTrue();
    }

    @Test
    public void returnsFalseForNonRegisteredCustomer() throws Exception {
        boolean isCustomerRegistered = customerDao.isCustomerRegistered("username");

        assertThat(isCustomerRegistered).isFalse();
    }

    private void givenARegisteredCustomer(String username) {
        customerDao.registeredUsers.put(username , null);
    }
}
