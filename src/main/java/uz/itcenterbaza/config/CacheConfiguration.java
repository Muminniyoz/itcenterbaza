package uz.itcenterbaza.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, uz.itcenterbaza.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, uz.itcenterbaza.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, uz.itcenterbaza.domain.User.class.getName());
            createCache(cm, uz.itcenterbaza.domain.Authority.class.getName());
            createCache(cm, uz.itcenterbaza.domain.User.class.getName() + ".authorities");
            createCache(cm, uz.itcenterbaza.domain.Regions.class.getName());
            createCache(cm, uz.itcenterbaza.domain.Center.class.getName());
            createCache(cm, uz.itcenterbaza.domain.Skill.class.getName());
            createCache(cm, uz.itcenterbaza.domain.Skill.class.getName() + ".teachers");
            createCache(cm, uz.itcenterbaza.domain.Teacher.class.getName());
            createCache(cm, uz.itcenterbaza.domain.Teacher.class.getName() + ".skills");
            createCache(cm, uz.itcenterbaza.domain.Course.class.getName());
            createCache(cm, uz.itcenterbaza.domain.Course.class.getName() + ".registereds");
            createCache(cm, uz.itcenterbaza.domain.Registered.class.getName());
            createCache(cm, uz.itcenterbaza.domain.Student.class.getName());
            createCache(cm, uz.itcenterbaza.domain.Participant.class.getName());
            createCache(cm, uz.itcenterbaza.domain.PaymentMethodConfig.class.getName());
            createCache(cm, uz.itcenterbaza.domain.PaymentMethod.class.getName());
            createCache(cm, uz.itcenterbaza.domain.PaymentMethod.class.getName() + ".confs");
            createCache(cm, uz.itcenterbaza.domain.SystemConfig.class.getName());
            createCache(cm, uz.itcenterbaza.domain.Payment.class.getName());
            createCache(cm, uz.itcenterbaza.domain.EventHistory.class.getName());
            createCache(cm, uz.itcenterbaza.domain.EventHistory.class.getName() + ".openedUsers");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
