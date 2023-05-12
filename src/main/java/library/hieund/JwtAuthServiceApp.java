package library.hieund;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import library.hieund.model.User;
import library.hieund.model.UserRole;
import library.hieund.service.UserService;

@SpringBootApplication
@RequiredArgsConstructor
public class JwtAuthServiceApp implements CommandLineRunner {

    final UserService userService;

    public static void main(String[] args) {
	SpringApplication.run(JwtAuthServiceApp.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
	ModelMapper modelMapper = new ModelMapper();
//	modelMapper.getConfiguration().setSkipNullEnabled(true);

	return modelMapper;
    }

    @Override
    public void run(String... params) throws Exception {
//    AppUser admin = new AppUser();
//    admin.setUsername("admin");
//    admin.setPassword("admin");
//    admin.setEmail("admin@email.com");
//    admin.setAppUserRoles(new ArrayList<AppUserRole>(Arrays.asList(AppUserRole.ROLE_ADMIN)));
//
//    userService.signup(admin);
//
//    AppUser client = new AppUser();
//    client.setUsername("client");
//    client.setPassword("client");
//    client.setEmail("client@email.com");
//    client.setAppUserRoles(new ArrayList<AppUserRole>(Arrays.asList(AppUserRole.ROLE_CLIENT)));
//
//    userService.signup(client);
    }

}
