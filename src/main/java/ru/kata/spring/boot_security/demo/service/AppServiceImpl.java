package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AppServiceImpl implements AppService, UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AppServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    @Lazy
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void createUser(User user, List<Integer> rolesID) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRolesByIds(rolesID, user.getRoles()));

        userRepository.save(user);
    }

    @Override
    public void updateUser(User user, int id, List<Integer> rolesId, String city) {
        User existingUser = getUserById(id);

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setSex(user.getSex());
        existingUser.setCity(city);
        existingUser.setUsername(user.getUsername());
        if (!existingUser.getPassword().equals(user.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existingUser.setRoles(getRolesByIds(rolesId, existingUser.getRoles()));

        userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    public Role getRoleById(int id) {
        return roleRepository.getById(id);
    }

    @Override
    public void addOrUpdateRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public Set<Role> getRolesByIds(List<Integer> ids, Set<Role> existingRoles) {
        Set<Role> roles = existingRoles;
        if (ids != null) {
            roles = new HashSet<>();
            for (int roleId : ids) {
                roles.add(getRoleById(roleId));
            }
        }
        return roles;
    }
}
