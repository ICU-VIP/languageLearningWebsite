package org.cdtu.website.config;


import jakarta.servlet.Filter;
import org.cdtu.website.filter.JwtCheckFilter;
import org.cdtu.website.filter.ValidateFilter;
import org.cdtu.website.handler.ExtraAuthenticationSuccessHandler;
import org.cdtu.website.handler.ExtraLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    ExtraAuthenticationSuccessHandler extraAuthenticationSuccessHandler;

    @Autowired
    public void setExtraAuthenticationSuccessHandler(ExtraAuthenticationSuccessHandler extraAuthenticationSuccessHandler) {
        this.extraAuthenticationSuccessHandler = extraAuthenticationSuccessHandler;
    }

    ExtraLogoutSuccessHandler extraLogoutSuccessHandler;

    @Autowired
    public void setExtraLogoutSuccessHandle(ExtraLogoutSuccessHandler extraLogoutSuccessHandler) {
        this.extraLogoutSuccessHandler = extraLogoutSuccessHandler;
    }

    ValidateFilter validateFilter;

    @Autowired
    public void setValidateFilter(ValidateFilter validateFilter) {
        this.validateFilter = validateFilter;
    }

    JwtCheckFilter jwtCheckFilter;

    @Autowired
    public void setJwtCheckFilter(JwtCheckFilter jwtCheckFilter) {
        this.jwtCheckFilter = jwtCheckFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
                //不通过Session获取SecurityContext
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/user/register", "/user/login", "/hello").permitAll()
                        //.requestMatchers("/hello").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout.logoutUrl("/user/logout")
                        .logoutSuccessHandler(extraLogoutSuccessHandler)
                        .permitAll())
                //验证码
                //.addFilterBefore(validateFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);
//

                //.formLogin(form -> form.successHandler(extraAuthenticationSuccessHandler));
        // 对于登录接口 允许匿名访问
//                .antMatchers("/testCors").hasAuthority("system:dept:list222")
        // 除上面外的所有请求全部需要鉴权认证


//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        //.requestMatchers("/code/image").permitAll()
//                        .requestMatchers("user/register").permitAll()
//                        .anyRequest().authenticated()
//
//                )
//
//
//                .logout(logout -> logout.logoutSuccessUrl("/login/toLogin")
//                        .logoutSuccessHandler(extraLogoutSuccessHandler)
//                        .permitAll()
//                )
//
//                .addFilterBefore(validateFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);
//        ;
//        //http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
//        //       .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        http
//                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
