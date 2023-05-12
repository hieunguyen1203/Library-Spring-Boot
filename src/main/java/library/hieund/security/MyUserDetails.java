package library.hieund.security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import library.hieund.model.User;
import library.hieund.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	final User appUser = userRepository.findByEmail(email);

	if (appUser == null) {
	    throw new UsernameNotFoundException("User '" + email + "' not found");
	}

	return org.springframework.security.core.userdetails.User//
		.withUsername(appUser.getUsername())// email
		.password(appUser.getPassword())//
		.authorities(appUser.getAppUserRoles())//
		.accountExpired(false)//
		.accountLocked(false)//
		.credentialsExpired(false)//
		.disabled(false)//
		.build();
    }

}
