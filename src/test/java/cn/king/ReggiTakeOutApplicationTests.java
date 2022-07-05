package cn.king;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ReggiTakeOutApplicationTests {

    @Test
    void testJedis() {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set("name", "xiaoming");
        System.out.println(jedis.get("name"));

        jedis.hset("myhash", "addr", "beijing");
        // jedis.del("name");

        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

        jedis.close();

    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * opsForValue对象用于封装简单类型String的一系列方法
     **/
    @Test
    public void testString() {
        redisTemplate.opsForValue().set("city", "Beijin");// "\xac\xed\x00\x05t\x00\x04city"需要改变序列化方式
        String city = (String) redisTemplate.opsForValue().get("city");
        System.out.println(city);

        stringRedisTemplate.opsForValue().set("city123", "beijing", 10l, TimeUnit.SECONDS);

    }


    /**
     * opsForHash对象用于封装hash类型的方法
     */
    @Test
    public void testHash() {
        redisTemplate.opsForHash().put("001","addr","beijing");
        redisTemplate.opsForHash().put("001","age","20");
        redisTemplate.opsForHash().put("001","name","king");
        List values = redisTemplate.opsForHash().values("001");
        for (Object value : values) {
            System.out.println(value);
        }

        String age = (String)redisTemplate.opsForHash().get("001", "age");
        System.out.println(age);

    }
    @Test
    public void testList(){
        redisTemplate.opsForList().leftPush("mylist","a");
        redisTemplate.opsForList().leftPushAll("mylist","b","c","d");

        List mylist = redisTemplate.opsForList().range("mylist",0,-1);
        for (Object o : mylist) {
            System.out.println((String) o);
        }

        Long size = redisTemplate.opsForList().size("mylist");
        for (int i = 0; i < size; i++) {
            redisTemplate.opsForList().leftPop("mylist");
        }
    }
    @Test
    public void testSet(){
        redisTemplate.opsForSet().add("myset","a","b");
       redisTemplate.opsForSet().add("mySec","c","a");
     /*   Set myset = redisTemplate.opsForSet().members("mySec");
        for (Object o : myset) {
            System.out.println((String) o);
        }*/
        //String myset = (String)redisTemplate.opsForSet().pop("myset");
       redisTemplate.opsForSet().remove("myset","a");
        Set myset1 = redisTemplate.opsForSet().members("myset");
        for (Object o : myset1) {
            String o1 = (String) o;
            System.out.println(o1);
        }

    }
    @Test
    public void testZset(){
        redisTemplate.opsForZSet().add("myZset","xiaoming",10);
        redisTemplate.opsForZSet().add("myZset","xiaozhang",11);
        redisTemplate.opsForZSet().add("myZset","xiaohong",12);
        redisTemplate.opsForZSet().add("myZset","xiaoming",15);

        redisTemplate.opsForZSet().incrementScore("myZset","xiaozhang",10);
        Set myZset = redisTemplate.opsForZSet().range("myZset",0,-1);
        for (Object o : myZset) {
            System.out.println((String) o);
        }
    }
}
