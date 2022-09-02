package com.example.demo;

import com.example.demo.dao.DiscussPostMapper;
import com.example.demo.entity.DiscussPost;
import com.example.demo.service.TestFoodService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;

@SpringBootTest
class DemoApplicationTests implements ApplicationContextAware {
    protected ApplicationContext context;

    // 创建容器时就会自动创建并保存容器上下文
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Test
    public void contextTest() {
//        Student student1 = context.getBean(Student.class);
//        Student student2 = context.getBean("stu", Student.class);
//        System.out.println(student1);
//        System.out.println(student2);
//        System.out.println(student1.hi());
//
        TestFoodService testFoodService = context.getBean(TestFoodService.class);
        System.out.println(testFoodService);

        Integer[] nums = new Integer[] {1, 6, 3, 5, 7, 8};
        Arrays.sort(nums, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        System.out.println(Arrays.toString(nums));  //[8, 7, 6, 5, 3, 1]
        Arrays.sort(nums);
        System.out.println(Arrays.toString(nums));  // [1, 3, 5, 6, 7, 8]
        ArrayList<Integer> list = new ArrayList<>();

    }

    @Autowired
    DiscussPostMapper discussPostMapper;
    @Test
    public void testDiscussPostSelect() {
        List<DiscussPost> discussPostsList = discussPostMapper.selectDiscussPosts(0,10, 10);
        for (DiscussPost discussPost : discussPostsList) {
            System.out.println(discussPost);
        }
        System.out.println(discussPostMapper.selectDiscussPostRows(1));
    }

    @Autowired
    UserService userService;
    @Test
    public void testUserService() {
        System.out.println(userService.findUserById(1));
    }
}
