package com.hbjycl.basic.test;

import com.hbjycl.basic.dao.IUserDao;
import com.hbjycl.basic.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * Created by hbjycl on 2016/3/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestDao {
    @Inject
    private IUserDao userDao;

    @Before
    public void setUp(){
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAdd(){
        User user = new User();
        user.setUsername("新用户1");
        userDao.add(user);
    }

    @Test
    public void testDelete(){
        userDao.delete(1);
    }

    @Test
    public void testLoad(){
        User user = userDao.load(1);
        System.out.println(user.getUsername());
    }
}
