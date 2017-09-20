# sysBase
基础架构，包括缓存，jdbc，日志

### 概述
项目使用了maven, spring boot , redis, logback. 实现了数据源切换，jdbc封装，分布式共享session的redis缓存，以及日志。   
将打成jar包，引入到项目，这样项目就只需要关注业务代码了。
3.3.1 spring boot搭建
在https://start.spring.io/上可以快速生成一个spring boot项目，可以根据需要选择生成Maven或者Gradle项目，可以选择psring boot 版本，jdk版本。以及该项目需要包含的内容，如Web, Security, JPA, Actuator......。我们选择maven,java,jdk1.7,spring boot1.5.6,生成的目录结构如下表







├─.mvn
│  └─wrapper
│          maven-wrapper.jar
│          maven-wrapper.properties
│          
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─example
    │  │          └─demo
    │  │                  DemoApplication.java -- 启动类
    │  │                  
    │  └─resources
    │      │  application.properties -------------------------------------------------- 配置文件
    │      │  
    │      ├─static ---------------------------------------------------------------------- 静态资源
    │      └─templates ----------------------------------------------------------------- 静态资源
    └─test
        └─java
            └─com
                └─example
                    └─demo
                            DemoApplicationTests.java ------------------------------- 测试类

### spring boot集成日志log4j2
第一步，在pom.xml中去掉spring boot的默认日志配置，同时引入log4j2的依赖
    







<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<exclusions><!-- 去掉默认配置 -->  
	   <exclusion>  
	        <groupId>org.springframework.boot</groupId>  
	        <artifactId>spring-boot-starter-logging</artifactId>  
	   </exclusion>  
    </exclusions>  
</dependency>

<dependency> <!-- 引入log4j2依赖 -->  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-log4j2</artifactId>  
</dependency>
第二步，在application.properties中配置日志文件，application.properties是spring boot的配置文件，使用key = value的形式书写。我们这里配置
#logging
logging.config=classpath:log4j2.xml
第三步，编写log4j2文件。启动时，系统就会默认加载该项目类路径下的log4j2.xml文件。我们在log4j2.xml里面定义了Info,Warn, Error三个级别的日志输入，分别放到logs文件夹下的三个日志文件中，并使用如下代码来给每天的日志文件存档。
<PatternLayout pattern="%date{yyyy-MM-dd HH:mm:ss.SSS} %level [%thread][%file:%line] - %msg%n" />
第四步，使用日志。当需要使用日志时，在当前类下加入如下代码就可以了。
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

static final Logger logger= LogManager.getLogger(ProviderAgencyController.class);

3.3.3 spring boot集成redis缓存
第一步，添加redis的pom依赖，spring自身提供了很多外部扩展，这里我们直接使用spring提供的就可以了。





<dependency> <!-- 引入redis依赖 -->
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
第二步，在application.properties中配置数据源
# REDIS (RedisProperties)
spring.redis.database=0
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=root
spring.redis.pool.max-active=256
spring.redis.pool.max-wait=-5000
spring.redis.pool.max-idle=3
spring.redis.pool.min-idle=0
spring.redis.timeout=5000
第三步，创建redis的配置类，我们在这里暂时还没有实现缓存机制，所以定义成普通的类就好了。通过如下代码形式，可以读取到配置文件中的redis数据源的信息。
@Value("${spring.redis.host}")
private String host;

@Value("${spring.redis.port}")
private int port;

@Value("${spring.redis.timeout}")
private int timeout;

@Value("${spring.redis.pool.max-idle}")
private int maxIdle;

@Value("${spring.redis.pool.max-wait}")
private long maxWaitMillis;

@Value("${spring.redis.password}")
private String password;
通过@Bean注解来讲JedisPool交给spring容器来管，在需要使用redis连接池时，就可以直接注入了。





@Bean
public JedisPool redisPoolFactory()
{
    logger.info("JedisPool注入成功！！");
    logger.info("redis地址：" + host + ":" + port);
    
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(maxIdle);
    jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

    JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);

    return jedisPool;
}
第四步，创建redis操作工具类RedisUtil.java。这个类要使用组件注解@Component，这样就可以使用注入的方式得到这个类了。在这个类里面，使用注入得到JedisPool.
@Autowired
private JedisPool jedisPool;
然后通过jedisPool来对redis进行操作，包括
1.	缓存/获取单个值；
2.	缓存/获取map；
3.	获取map指定的key值；
4.	获取map指定key列表的值；
5.	缓存获取List；
6.	删除指定缓存对象。
第五步，使用redis。这里提供了两种方式，一种是使用上面提供的RedisUtil工具类来进行操作。另一种是使用spring data提供的StringRedisTemplate来操作，同样是通过注解的方式得到这个类。
最后，我们可以在application.xml中加入，如下配置，来实现分布式共享session.
# session config
spring.session.store-type=redis
同时，要在RedisConfig.java上加上如下注解
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=1800)

3.3.4 加载字典表到缓存
我们创建一个字典表工具类SysDictionaryUtil，并加上组件注解@Component，使它可以通过注入的方式得到。为了使字典表能在项目启动时就运行，我们实现了CommandLineRunner中的run方法。







@Override
public void run(String... args)
    throws Exception
{
    logger.info("init SYS_DIC_MAP start...");
    
    if (redisClient.getMap(SYS_DIC_MAP) != null && !redisClient.getMap(SYS_DIC_MAP).isEmpty())
    {
        logger.info("SYS_DIC_MAP already exists...");
        logger.info(redisClient.getMap(SYS_DIC_MAP));
        return;
    }
    
    initSysDicMap();
    
    logger.info("init SYS_DIC_MAP end...");
}
首先从redis中读取字典表的数据，如果不存在，再从数据库查询，并存储到redis中。
3.3.5 错误信息处理
我们在resource下创建errror_meesage.properties文件，已键值对的形式存放错误码和错误提示信息，如下
10000000=错误码未配置！
10000001=参数为空！
10000002=格式不正确！
创建ErrorCode类来存放错误码。
public class ErrorCode
{
    //错误码未配置
    public static Long ERRORCODE_NOT_CONFIGED = 10000000L;
}
创建ErrorUtil类，在里面定义通过错误码得到错误信息的方法。
对于异常的处理是创建自己的异常处理类，继承运行时异常。
3.3.6 spring boot多数据源配置与切换
第一步，在application.xml中配置数据源信息。如下
#datasource b-force
spring.datasource.bf.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.bf.url=jdbc:mysql://127.0.0.1:3306/b-force?characterEncoding=utf8&useSSL=true
spring.datasource.bf.username=root
spring.datasource.bf.password=root

spring.datasource.bfscrm.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.bfscrm.url=jdbc:mysql://127.0.0.1:3306/b-force-scrm?characterEncoding=utf8&useSSL=true
spring.datasource.bfscrm.username=root
spring.datasource.bfscrm.password=root
第二步，定义DataSource和JdbcTemplate，并交给spring容器管理。创建一个GlobalDataConfiguration类，并加上@Configuratioin注解，这样spring启动时就会扫描到这个类。我们使用了两个数据库，所以这里配置两个数据源和两个JdbcTemplate。如下是配置数据源:
@Bean(name = "bfDataSource")
@Qualifier("bfDataSource")
@Primary
@ConfigurationProperties(prefix = "spring.datasource.bf")
public DataSource primaryDataSource()
{
    log.info("-------------------- bfDataSource init ---------------------");
    return DataSourceBuilder.create().build();
}

@Bean(name = "bfscrmDataSource")
@Qualifier("bfscrmDataSource")
@ConfigurationProperties(prefix = "spring.datasource.bfscrm")
public DataSource secondaryDataSource()
{
    log.info("-------------------- bfscrmDataSource init ---------------------");
    return DataSourceBuilder.create().build();
}
这里的数据源前缀设置是什么，那么在application.xml里面数据源配置的前缀也写什么。接着配置JdbcTemplate
@Bean(name = "bfJdbcTemplate")
public JdbcTemplate bfJdbcTemplate(@Qualifier("bfDataSource") DataSource dataSource)
{
    return new JdbcTemplate(dataSource);
}

@Bean(name = "bfscrmJdbcTemplate")
public JdbcTemplate bfscrmscrmJdbcTemplate(@Qualifier("bfscrmDataSource") DataSource dataSource)
{
    return new JdbcTemplate(dataSource);
}
我们在使用JdbcTemplate操作数据库时，需要使用哪一个数据库，就注入对应的JdbcTemplate就可以了。
3.3.7 事务处理机制
第一步，在项目启动类上加上注解@EnableTransactionManagement。
第二步，在上述的GlobalDataConfiguration.java中为两种数据源加上事务管理配置
/******配置事务管理********/

@Bean
public PlatformTransactionManager bfTransactionManager(@Qualifier("bfDataSource")DataSource prodDataSource) {
 return new DataSourceTransactionManager(prodDataSource);
}
 
@Bean
public PlatformTransactionManager bfscrmTransactionManager(@Qualifier("bfscrmDataSource")DataSource sitDataSource) {
 return new DataSourceTransactionManager(sitDataSource);
}
第三步，需要使用事务时，在service里面的方法上加上注解@Transactional。
3.3.8 持久层的封装
第一步，定义一个DataRepository接口，并定义相关常用的数据库操作接口。以及数据库表名这主键名的getXX()/setXX()方法。然后分别使用两种数据源来实现这个接口。
第二步，对应表操作的使用方法。首先为这个表创建一个Repository类，并使用注解@Repository("shopLbsRepository")。这个表属于哪个数据库，就继承哪个DataRepositoryJDBC，并且定义构造器传入表名和主键名，如ShopLbsRepository中
@Repository("shopLbsRepository")
public class ShopLbsRepository extends DataRepositoryJDBCBfscrm
{
    static final Logger logger = LogManager.getLogger(ShopLbsRepository.class);

    public ShopLbsRepository()
    {
        this.masterTable = "scrm_shop_lbs";
        this.masterTablePK = "shop_id";
    }
    ...
}

3.3.8.1 新增一条记录
我们在controller或者service中注入ShopLbsRepository，直接调用其父类的doInsert方法。这个方法定义如下：
	@Override
	public Serializable doInsert(Map dataMap) {
		...
	}
如果这张表的主键是自增的int类型，则方法返回当条插入记录的主键值。dataMap是传入参数，key是表中字段名，value是字段对应的值。需要包含表里面的所有不为null的字段，主键如果是自增则可以不传。创建时间的字段名设置为reg_time,则也可以不传，日期类型字段可以传String，long和java.util.Date三种形式。
3.3.8.2 批量新增记录
批量新增记录时，调用父类doBatchInsert方法，定义如下：
	@Override
	public  int[] doBatchInsert(List<Map> dataList){
        ...
	}
与新增单条记录一致，每一条记录以Map形式存储，并添加到List中，spring的NamedParameterJdbcTemplate中提供了批量插入的方法。
3.3.8.3 根据主键删除记录
调用父类的doDelete方法，传入主键值。
3.3.8.4 根据主键获取单条记录
调用父类的doLoad方法，传入主键值。
3.3.8.5 根据查询条件获取单条记录
调用父类的findOne方法，其定义如下
@Override
public Map findOne(Map paramMap) {
	StringBuilder whereClause = new StringBuilder();
	Object[] paramObjs = ExSQLUtils.buildWhere(whereClause, paramMap);
	return this.findOne(whereClause.toString(), paramObjs);
}
@Override
public Map findOne(String whereClause, Object[] parameters) {
	
	String sql = this.getSelectSQL();
	
	if (ExStringUtils.isNotBlank(whereClause)){
		sql = sql + " and " + whereClause;
	}
	return findOneBySQL(sql, parameters);
}
我们先要传入的参数paramMap是将查询条件以键值对形式存储，如果是模糊查询，测在值的前后或者末尾加上%。例如我们需要查询名字name的模糊查询和年龄age的精确查询，则传入的paramMap的构成为
Map paramMap = new HashMap();
paramMap.put("name","李%");
paramMap.put("age",20);
3.3.8.6 不分页查询数据列表
调用父类的queryDataBySQL方法，其定义如下
@Override
public List queryDataBySQL(String sql, Object[] parameters) {
	log.debug("查询SQL=" + sql);
	List dataList = getJdbcTemplate().queryForList(sql, parameters);
	List resultList = new ArrayList();
	for(Object item : dataList){
		Map itemMap = (Map)item;
		Map rowMap = new CaseInsensitiveMap();
		rowMap.putAll(itemMap);
		resultList.add(rowMap);
	}
	return resultList;
}
调用该方法时，需要自己组建sql，和搜索条件。
3.3.8.7 分页查询数据列表
调用父类的queryData方法，其定义如下
@Override
public List queryData(String filter, Map paramMap, int pageIndex, int pageSize){
	...
}
当需要按条件搜索时，我们在paramMap里面需要设置一个"commad"="searche"，其余查询条件的规则与3.3.8.5根据查询条件获取单条记录中一致。如果需要排序则在paramMap中还需要加入"sort"= "col_1, col_2"; "order"="desc"，如果不加上这两个键值对，则默认按主键降序排列。最后在controller中使用如下代码将数据进行分页封装。
ExControllerUtils.buildTableData(dataList, pageIndex, pageSize);
3.3.8.8 更新记录
根据主键更新单条记录，则调用父类的doUpdate方法，需要传入的参数为(Serializable rowId, Map dataMap)，其中rowId为主键的值，dataMap为需要修改的数据。
第二种方式是调用doUpdate的重载方法，直接传入sql和参数，其定义如下
@Override
public int doUpdate(String sql, Object... args) {
	return this.getJdbcTemplate().update(sql, args);
}
3.3.8.9 注意点
以上都是针对的单表操作，并且查询语句中使用的是select*，查询所有字段。当多表查询或者表中数据量超大时，建议自己写sql语句。
3.3.9 打包上传至nexus
第一步，配置assembly.xml文件，这个文件的作用是将pom.xml里面的依赖包也全部打包进去。
第二步，在pom.xml中配置打包
	<build>
		<plugins>
			<plugin>
				<artifactId>maven-assembly-plugin</artifactId>
				<configuration>
					<appendAssemblyId>false</appendAssemblyId>
					<descriptorRefs>
						<descriptorRef>jar-with-dependencies</descriptorRef>
					</descriptorRefs>
					<archive>
						<manifest>
							<mainClass>cn.bforce.common.YuntuSysBaseApplication</mainClass>
						</manifest>
					</archive>
				</configuration>
				<executions>
					<execution>
						<id>make-assembly</id>
						<phase>package</phase>
						<goals>
							<goal>assembly</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
第三步，在POM中配置构件部署地址



	<distributionManagement>
		<!-- 发布版本的构件的仓库 -->
		<repository>
			<id>releases</id>
			<name>proj release repository</name>
			<url>http://192.168.18.242:8089/nexus/content/repositories/releases</url>
		</repository>
		<!-- 快照版本的仓库 -->
		<snapshotRepository>
			<id>snapshots</id>
			<name>proj snapshot repository</name>
			<url>http://192.168.18.242:8089/nexus/content/repositories/snapshots</url>
		</snapshotRepository>
	</distributionManagement>
第四步，打包。在项目更目录下打开dos窗口，使用命令mvn clean deploy。在业务系统中引用时使用依赖
		<dependency>
		  <groupId>cn.bforce</groupId>
		  <artifactId>yuntu-sysBase</artifactId>
		  <version>0.0.1-SNAPSHOT</version>
		</dependency>
要注意的地方是业务系统中的启动类要放在cn.bforce下，这样业务系统在启动时才会去扫描jar中中cn.bforce下的文件。
