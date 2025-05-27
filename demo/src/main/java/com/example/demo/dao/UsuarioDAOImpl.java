package com.example.demo.dao;

import com.example.demo.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public class UsuarioDAOImpl implements UsuarioDAO {
    
    private List<Usuario> usuarios = new ArrayList<>();
    
    public UsuarioDAOImpl() {
        // Agrego un usuario de ejemplo para arrancar
        usuarios.add(new Usuario("usuario1", "Juan", "Perez", "usuario@viago.com", "1234", "999999999"));
    }
    
    @Override
    public List<Usuario> obtenerTodos() {
        return usuarios;
    }
    
    @Override
    public Usuario obtenerPorId(String idUsuario) {
        return usuarios.stream()
                .filter(u -> u.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElse(null);
    }

    @Override
public Usuario obtenerPorCorreo(String correo) {
    for (Usuario u : usuarios) {
        if (u.getCorreo().equals(correo)) {
            return u;
        }
    }
    return null;
}
    
    @Override
    public void agregar(Usuario usuario) {
        usuarios.add(usuario);
    }
    
    @Override
    public void eliminar(String idUsuario) {
        usuarios.removeIf(u -> u.getIdUsuario().equals(idUsuario));
    }
}
