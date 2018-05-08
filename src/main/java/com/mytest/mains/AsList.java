package com.mytest.mains;


import java.util.Arrays;
import java.util.List;

/**
 * Created by 60404 on 2018/5/2.
 */
public class AsList {
    public static void main(String[] args) {
        int[] aint = {1,2,3,4,5};
        List alist = Arrays.asList(aint);
        /*预期输出应该是1,2,3,4,但实际上输出的仅仅是一个引用, 这里它把a_int当成了一个元素*/
        for (Object obj:alist){
            System.out.println("alist" + obj);
        }

        /*因为数组和列表链接在一起,所有要获取出来才能用*/
        int[] aint_list = (int[]) alist.get(0);
        for (Object obj:aint_list){
            System.out.println("aint_list:" + obj);
        }

        /*如果传进去的是对象,能直接使用*/
        Integer[] bInteger = new Integer[]{1,2,3,4,5};
        List blist = Arrays.asList(bInteger);
        for (Object obj:blist){
            System.out.println("bInteger:" + obj);
        }

        User user1 = new User("zhangsan");
        User user2 = new User("lisi");
        User[] users = new User[]{user1,user2};
        List ulist = Arrays.asList(users);
        for (Object obj:ulist){
            System.out.println(((User)obj).getUsername());
        }

        /*当更新数组或者asList之后的List,另一个将自动获得更新*/
        aint[0] = 11;
        for (Object obj:aint_list){
            System.out.println("修改aint后aint_list:"+obj);
        }

        aint_list[0] = 22;
        System.out.println("修改aint_list后aint:"+aint[0]);

        bInteger[0] = 11;
        System.out.println("修改bInteger后blist:"+blist.get(0));

        blist.set(0,22);
        System.out.println("反之:"+bInteger[0]);

        users[0].setUsername("11111111111");
        System.out.println("修改users后ulist:"+((User)ulist.get(0)).getUsername());

        ((User)ulist.get(0)).setUsername("22222222222222");
        System.out.println("反之"+users[0].getUsername());

        /*直接构造*/
        List list1 = Arrays.asList("1","2","3","4");
        //List list2 = Arrays.asList(1,2,3,4,5,6);

        for (Object obj:list1){
            System.out.println("list1:" + obj);
        }
        /*for (Object obj:list2){
            System.out.println("list2:" + obj);
        }*/

    }
}

class User{

    private String username;

    public User(String username){
        this.username = username;
    }

    String getUsername(){
        return this.username;
    }

    void setUsername(String username){
        this.username = username;
    }
}
