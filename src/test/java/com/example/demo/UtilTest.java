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
        String source = "我操吸毒啊aa开*票";
        System.out.println(source);
        System.out.println(filter.filter(source));
        System.out.println(filter.isSymbol('&'));

    }
}
