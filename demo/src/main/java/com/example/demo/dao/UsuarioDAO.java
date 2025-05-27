package com.example.demo.dao;

import com.example.demo.Usuario;
import java.util.List;

public interface UsuarioDAO {
    List<Usuario> obtenerTodos();
    Usuario obtenerPorId(String idUsuario);
    Usuario obtenerPorCorreo(String correo);
    void agregar(Usuario usuario);
    void eliminar(String idUsuario);


    
}
