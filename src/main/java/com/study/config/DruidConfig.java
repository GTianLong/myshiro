package com.study.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
public class DruidConfig {

    private Logger logger = LoggerFactory.getLogger(DruidConfig.class);


    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.filters}")
    private String filters;

    @Value("${spring.datasource.logSlowSql}")
    private String logSlowSql;

    @Value("${druid.loginname}")
    private String druidLoginName;

    @Value("${druid.password}")
    private String druidPassWord;

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", druidLoginName);
        reg.addInitParameter("loginPassword", druidPassWord);
        reg.addInitParameter("logSlowSql", logSlowSql);
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    @Bean
    public DataSource druidDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        return datasource;
    }

    /*@Bean(name = "pageHelper")
    public PageInterceptor pageHelper(){
        PageInterceptor pageHelper = new PageInterceptor();
        Properties properties = new Properties();
        *//**默认false，设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用*//*
        properties.setProperty("offsetAsPageNum","true");
        *//**默认false，设置为true时，使用RowBounds分页会进行count查询 *//*
        properties.setProperty("rowBoundsWithCount","true");
        *//** 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据 *//*
        properties.setProperty("reasonable","true");
        *//** always总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page *//*
        properties.setProperty("returnPageInfo","check");
        *//** 支持通过Mapper接口参数来传递分页参数 *//*
        properties.setProperty("supportMethodsArguments","true");
        *//**  配置数据库的方言  *//*
        properties.setProperty("helperDialect","mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }*/

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(druidDataSource());
        //mybatis分页
        /*PageHelper pageHelper = new PageHelper();
        Properties props = new Properties();
        props.setProperty("dialect", "mysql");
        props.setProperty("reasonable", "true");
        props.setProperty("supportMethodsArguments", "true");
        props.setProperty("returnPageInfo", "check");
        props.setProperty("params", "count=countSql");
        pageHelper.setProperties(props);*/
        PageInterceptor pageHelper = new PageInterceptor();
        Properties properties = new Properties();
        /**默认false，设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用*/
        properties.setProperty("offsetAsPageNum","true");
        /**默认false，设置为true时，使用RowBounds分页会进行count查询 */
        properties.setProperty("rowBoundsWithCount","true");
        /** 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据 */
        properties.setProperty("reasonable","true");
        /** always总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page */
        properties.setProperty("returnPageInfo","check");
        properties.setProperty("params", "count=countSql");
        /** 支持通过Mapper接口参数来传递分页参数 */
        properties.setProperty("supportMethodsArguments","true");
        /**  配置数据库的方言  */
        properties.setProperty("helperDialect","mysql");
        pageHelper.setProperties(properties);
        //添加插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});
        //添加XML目录
        VFS.addImplClass(SpringBootVFS.class);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
        logger.info("dao层扫描包为:mapper/*.xml");
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(druidDataSource());
    }

}
