package quick.pager.shiro.spring.boot;

import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.support.DefaultEventBus;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quick.pager.shiro.spring.boot.processor.LifecycleBeanPostProcessor;
import quick.pager.shiro.spring.boot.processor.ShiroEventBusBeanPostProcessor;

@Configuration
public class ShiroCommonConfiguration {


    /**
     * SessionFactory
     *
     * @return SessionFactory
     */
    @Bean
    @ConditionalOnMissingBean
    protected SessionFactory sessionFactory() {
        return new SimpleSessionFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionStorageEvaluator sessionStorageEvaluator() {
        return new DefaultWebSessionStorageEvaluator();
    }

    /**
     * SubjectDAO
     *
     * @param sessionStorageEvaluator sessionStorageEvaluator
     * @return SubjectDAO
     */
    @Bean
    @ConditionalOnMissingBean
    protected SubjectDAO subjectDAO(SessionStorageEvaluator sessionStorageEvaluator) {
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        return subjectDAO;
    }

    @Bean
    @ConditionalOnMissingBean
    protected EventBus eventBus() {
        return new DefaultEventBus();
    }

    @Bean
    @ConditionalOnMissingBean
    protected ShiroEventBusBeanPostProcessor shiroEventBusAwareBeanPostProcessor(EventBus eventBus) {
        return new ShiroEventBusBeanPostProcessor(eventBus);
    }

    @Bean
    @ConditionalOnMissingBean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

}
