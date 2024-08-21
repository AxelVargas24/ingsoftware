package com.ingenieriaweb.bdringo.service;

import com.ingenieriaweb.bdringo.model.Usuario;
import com.ingenieriaweb.bdringo.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;
    
	@Autowired
	private PasswordEncoder passwordEncoder;

// Asegúrate de que el PasswordEncoder esté inyectado

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        String role = "ROLE_" + usuario.getTipo().toUpperCase(); // Mapea el tipo a un rol
        return User.withUsername(usuario.getUsername())
                    .password(usuario.getPassword()) // La contraseña debe estar codificada
                    .roles(role) // Puedes asignar roles según el campo 'tipo'
                    .build();
    }
}
