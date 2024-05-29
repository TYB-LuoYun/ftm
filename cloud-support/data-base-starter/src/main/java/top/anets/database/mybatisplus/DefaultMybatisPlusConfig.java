package top.anets.database.mybatisplus;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "mybatis-plus.mapper-scan-package")
@MapperScan("${mybatis-plus.mapper-scan-package}")
public class DefaultMybatisPlusConfig {
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    @Bean
    public MybatisPlusInterceptor innerInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        /**
         * 乐观锁
         */
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    @ConditionalOnMissingBean(MybatisSqlInterceptor.class)
    @Bean
    public MybatisSqlInterceptor mybatisSqlInterceptor() {
        return new MybatisSqlInterceptor();
    }

}