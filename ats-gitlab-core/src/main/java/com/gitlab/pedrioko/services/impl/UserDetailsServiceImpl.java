package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.CrudService;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service("customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CrudService myservice;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        final PathBuilder<?> builder = myservice.getPathBuilder(Usuario.class);
        List<?> tipo1 = myservice.query().from(builder).where(builder.get("tipo", TipoUsuario.class).eq(TipoUsuario.ROLE_ADMIN)).fetch();

        if (tipo1 == null || tipo1.isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setUsername("admin");
            usuario.setTipo(TipoUsuario.ROLE_ADMIN);
            usuario.setPassword(passwordEncoder.encode("admin"));
            myservice.save(usuario);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String ssoId) throws UsernameNotFoundException {
        final PathBuilder<?> builder = myservice.getPathBuilder(Usuario.class);

        final Usuario user = (Usuario) myservice.query().from(builder).where(builder.get("username", String.class).eq(ssoId)).fetchFirst();
        System.out.println("User : " + user);
        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new User(user.getUsername(), user.getPassword(), user.getEnable(), true, true, true, getGrantedAuthorities(user));
    }


    private List<GrantedAuthority> getGrantedAuthorities(final Usuario user) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        //System.out.println("UserProfile : " + userProfile);
        authorities.add(new SimpleGrantedAuthority(user.getTipo().name()));
        System.out.print("authorities :" + authorities);
        return authorities;
    }


}