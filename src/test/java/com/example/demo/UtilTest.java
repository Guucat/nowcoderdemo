package com.example.demo;

import com.example.demo.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author shengyi
 * @create 2022/9/17 - 9:58
 */
@SpringBootTest
public class UtilTest {
    @Autowired
    SensitiveFilter filter;
    @Test
    public void testWord() {
        String source = "这里可以%赌%博%，可以嫖娼，可以吸&&毒，哈哈哈";
        String s1 = "赌赌博博";
        System.out.println(filter.filter(filter.filter(s1)));
        System.out.println(filter.filter(source));
        assert("这里可以%***%，可以**，可以****，哈哈哈".equals(filter.filter(source)));

    }


}
