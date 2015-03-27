package org.springframework.boot.autoconfigure.browser;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.browser.mvc.BrowserHandlerMethodArgumentResolver;
import org.springframework.browser.mvc.BrowserResolverHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Browser
 * detection's {@link BrowserResolver}.
 *
 * @author Aurélien Baudet
 */
@Configuration
@ConditionalOnClass({ BrowserResolverHandlerInterceptor.class, BrowserHandlerMethodArgumentResolver.class })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class BrowserResolverAutoConfiguration {

	@Configuration
	@ConditionalOnWebApplication
	protected static class BrowserResolverMvcConfiguration extends WebMvcConfigurerAdapter {

		@Autowired
		private BrowserResolverHandlerInterceptor browserResolverHandlerInterceptor;

		@Bean
		@ConditionalOnMissingBean(BrowserResolverHandlerInterceptor.class)
		public BrowserResolverHandlerInterceptor browserResolverHandlerInterceptor() throws IOException {
			return new BrowserResolverHandlerInterceptor();
		}

		@Bean
		public BrowserHandlerMethodArgumentResolver browserHandlerMethodArgumentResolver() {
			return new BrowserHandlerMethodArgumentResolver();
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(this.browserResolverHandlerInterceptor);
		}

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(browserHandlerMethodArgumentResolver());
		}

	}

}