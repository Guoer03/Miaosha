# 项目框架搭建

## IDEA项目环境搭建

### 新建工程

![](https://gitee.com/guoer03/picimg/raw/master/20201215095949.png)
<!--more--> 
### 配置自己的Maven

![](https://gitee.com/guoer03/picimg/raw/master/20201215100212.png)

### 项目结构

![](https://gitee.com/guoer03/picimg/raw/master/20201215103344.png)

### 添加<font color=purple>springboot，mybatis，jedis，druid，fastjson，mysql</font>依赖

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.imooc</groupId>
    <artifactId>miaosha</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
    </parent>
    <name>miaosha_2</name>
    <url>http://maven.apache.org</url>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.1</version>
        </dependency>
        <!--mysql连接-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>8.0.12</scope>
        </dependency>
        <!--数据库连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.5</version>
        </dependency>
        <!--redis相关依赖-->
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
        </dependency>
        <!--将properties中的配置读到配置类中的依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <!--自动getset注解@Data-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.4</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
    <repositories><!-- 阿里云代码库 -->
        <repository>
            <id>maven-ali</id>
            <url>http://maven.aliyun.com/nexus/content/repositories/central</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
                <checksumPolicy>fail</checksumPolicy>
            </snapshots>
        </repository>
    </repositories>
</project>
```
### 配置文件application.properties

```properties
#thymeleaf
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false
spring.thymeleaf.content-type=text/html
spring.thymeleaf.enabled=true
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.mode=HTML5
# mybatis
mybatis.type-aliases-package=com.guoer03.domain
mybatis.configuration.map-underscore-to-camel-case=true
mybatis.configuration.default-fetch-size=100
mybatis.configuration.default-statement-timeout=3000
mybatis.mapperLocations = classpath:com/guoer03/dao/*.xml
# druid
spring.datasource.url=jdbc:mysql://39.105.201.210:3306/miaosha?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.filters=stat
spring.datasource.maxActive=2
spring.datasource.initialSize=1
spring.datasource.maxWait=60000
spring.datasource.minIdle=1
spring.datasource.timeBetweenEvictionRunsMillis=60000
spring.datasource.minEvictableIdleTimeMillis=300000
spring.datasource.validationQuery=select 'x'
spring.datasource.testWhileIdle=true
spring.datasource.testOnBorrow=false
spring.datasource.testOnReturn=false
spring.datasource.poolPreparedStatements=true
spring.datasource.maxOpenPreparedStatements=20
#redis
redis.host=39.105.201.210
redis.port=6379
redis.timeout=3
redis.password=123456
redis.poolMaxTotal=10
redis.poolMaxIdle=10
redis.poolMaxWait=3
```

## Result结果封装

### Result:

```java
package com.guoer03.result;
import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor//lombok中全构造方法的注解。
public class Result<T> {
    private int code;
    private String msg;
    private T data;//因为data无法知道是什么类型。Result负责封装所有的发挥数据，则使用泛型。

    public Result(T data) {
        this.code=0;
        this.data = data;
        this.msg="success";
    }
    public Result(CodeMsg cm) {
        if (cm==null){
            return;
        }
        this.code=cm.getCode();
        this.msg=cm.getMsg();
    }
    /*
    * 成功时候的调用
    * */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }
    /*
    * 失败时的调用：失败时不会返回数据，只需要返回一个code和msg错误信息，
    可以将这俩构造成一个对象，因为code和错误信息往往是成对出现的*/
    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }
}
```

### CodeMsg:

```java
package com.guoer03.result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeMsg {
    private int code;
    private String msg;
    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
    public static CodeMsg ACCESS_LIMIT_REACHED= new CodeMsg(500104, "访问太频繁！");
    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    //商品模块 5003XX
    //订单模块 5004XX
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");
    //秒杀模块 5005XX
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
    public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501, "不能重复秒杀");
    public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500502, "秒杀失败");
}
```

## Redis

### 安装Redis

[centos下redis安装][1]

[1]:https://www.cnblogs.com/zuidongfeng/p/8032505.html

可以顺便把mysql也安装了，然后记得把服务器的相应端口一起打开。

mysql默认端口：<font color=orange>3306</font>

redis默认端口：<font color=orange>6379</font>

### 目前redis下所有类的结构

![](https://gitee.com/guoer03/picimg/raw/master/20201215230526.png)

```java
package com.guoer03.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis")//把配置文件中前缀为redis的配置都读进配置类中
@Data//自动gettersetter
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;//秒
    private String password;
    private int poolMaxTotal;
    private int poolMaxIdle;
    private int poolMaxWait;//秒
}
```

### RedisPoolFactory

```java
package com.guoer03.redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
@Service
public class RedisPoolFactory {
    @Autowired
    RedisConfig redisConfig;
    @Bean
    public JedisPool JedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
        JedisPool jp = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeout()*1000, redisConfig.getPassword(), 0);
        return jp;
    }
}
```

### RedisService:

为什么会存在这个类，其每个方法的主要功能都是由jedis实现的？

因为redis只能操作key为某个字符串的键值对，无法把对象直接变成字符串亦或者把redis中字符串直接转换成对象，所以这个类实现的是<font color=green>序列化和反序列化的功能，同时还有关闭redis连接的功能</font>其本身使用了<font color=#00CED1>装饰类模式</font>，在jedis的功能上添加了客制化的内容
```java
package com.guoer03.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

@Service
public class RedisService {
	
	@Autowired
	JedisPool jedisPool;
	
	/**
	 * 获取当个对象
	 * */
	public <T> T get(KeyPrefix prefix, String key,  Class<T> clazz) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 //生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			 String  str = jedis.get(realKey);
			 T t =  stringToBean(str, clazz);
			 return t;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	/**
	 * 设置对象
	 * */
	public <T> boolean set(KeyPrefix prefix, String key,  T value) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 String str = beanToString(value);
			 if(str == null || str.length() <= 0) {
				 return false;
			 }
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			 int seconds =  prefix.expireSeconds();
			 if(seconds <= 0) {
				 jedis.set(realKey, str);
			 }else {
				 jedis.setex(realKey, seconds, str);
			 }
			 return true;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	/**
	 * 判断key是否存在
	 * */
	public <T> boolean exists(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.exists(realKey);
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	/**
	 * 删除
	 * */
	public boolean delete(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			String realKey  = prefix.getPrefix() + key;
			long ret =  jedis.del(realKey);
			return ret > 0;
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	/**
	 * 增加值
	 * */
	public <T> Long incr(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.incr(realKey);
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	/**
	 * 减少值
	 * */
	public <T> Long decr(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.decr(realKey);
		 }finally {
			  returnToPool(jedis);
		 }
	}
	
	public boolean delete(KeyPrefix prefix) {
		if(prefix == null) {
			return false;
		}
		List<String> keys = scanKeys(prefix.getPrefix());
		if(keys==null || keys.size() <= 0) {
			return true;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(keys.toArray(new String[0]));
			return true;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(jedis != null) {
				jedis.close();
			}
		}
	}
	
	public List<String> scanKeys(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> keys = new ArrayList<String>();
			String cursor = "0";
			ScanParams sp = new ScanParams();
			sp.match("*"+key+"*");
			sp.count(100);
			do{
				ScanResult<String> ret = jedis.scan(cursor, sp);
				List<String> result = ret.getResult();
				if(result!=null && result.size() > 0){
					keys.addAll(result);
				}
				//再处理cursor
				cursor = ret.getStringCursor();
			}while(!cursor.equals("0"));
			return keys;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	public static <T> String beanToString(T value) {
		if(value == null) {
			return null;
		}
		Class<?> clazz = value.getClass();
		if(clazz == int.class || clazz == Integer.class) {
			 return ""+value;
		}else if(clazz == String.class) {
			 return (String)value;
		}else if(clazz == long.class || clazz == Long.class) {
			return ""+value;
		}else {
			return JSON.toJSONString(value);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T stringToBean(String str, Class<T> clazz) {
		if(str == null || str.length() <= 0 || clazz == null) {
			 return null;
		}
		if(clazz == int.class || clazz == Integer.class) {
			 return (T)Integer.valueOf(str);
		}else if(clazz == String.class) {
			 return (T)str;
		}else if(clazz == long.class || clazz == Long.class) {
			return  (T)Long.valueOf(str);
		}else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}
    //此处可以使用redis连接池进行优化。
	private void returnToPool(Jedis jedis) {
		 if(jedis != null) {
			 jedis.close();
		 }
	}
}
```

KeyPrefix抽象类：

```java
package com.guoer03.redis;
public abstract class KeyPrefix {
    private int expireSeconds;
    private String prefix;
    public KeyPrefix(String prefix) {//0代表永不过期
        this(0, prefix);
    }
    public KeyPrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }
    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":" + prefix;
    }
}
```

之后抽象类将被继承，例如：

```java
package com.imooc.miaosha.redis;

public class OrderKey extends BasePrefix {

	public OrderKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

}
```

```java
package com.guoer03.redis;
public class UserKey extends KeyPrefix{
    public UserKey(String prefix) {
        super(prefix);
    }
    public static UserKey getById=new UserKey("id");
    public static UserKey getByName=new UserKey("name");
}
```



# 实现用户登录以及分布式session

# 秒杀功能开发以及管理后台

# 秒杀压测

# 页面级高并发秒杀优化--Redis缓存

# 服务级高并发秒杀优化(RabbitMQ)

# 图形验证码及恶意防刷

# 课程总结及重难点回顾
