package pl.edu.pw.ii.pik01.seeknresolve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.Role;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;

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
                .authoritiesByUsernameQuery("select u.login, r.roleName " +
                        "from user u " +
                        "join user_roles ur on ur.user_id = u.id " +
                        "join role r on ur.role_id = r.roleName " +
                        "where u.login = ?");
    }

    @Autowired
    public void addAdminAccount(UserRepository userRepository){
        Role role = new Role();
        role.setRoleName("ADMIN");

        User user = new User();
        user.setId(-1L);
        user.setLogin("admin");
        user.setPassword("admin");
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setEmail("admin@admin.admin");
        user.addRole(role);
        userRepository.save(user);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().usernameParameter("login").passwordParameter("password")
                .and()
                .httpBasic()
                .and()
                .csrf();
    }
}
