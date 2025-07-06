package com.flipkart.raghav.service;

import com.flipkart.raghav.model.UserPrincipal;
import com.flipkart.raghav.model.Users;
import com.flipkart.raghav.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);

        if(user == null){
            System.out.println("No User Found");
            throw new UsernameNotFoundException("No User Found!!!");
        }
        return new UserPrincipal(user);
    }
}
