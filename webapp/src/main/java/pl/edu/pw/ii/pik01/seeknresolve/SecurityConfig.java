package pl.edu.pw.ii.pik01.seeknresolve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select login, password, 1 from user where login = ?")
                // TODO: to be replaced
                .authoritiesByUsernameQuery("select u.login, u.user_role from user u where u.login = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
            authorizeRequests().
                anyRequest().authenticated().
        and().
            formLogin().
                usernameParameter("login").
                passwordParameter("password").
        and().
            httpBasic().
        and().
            csrf().disable();
    }
}
