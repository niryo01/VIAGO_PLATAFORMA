CAMBIOS PRINCIPALES EN VIAGO (Login y DAOs, nuevas implementaciones)

1. Separación en DAO y uso de Listas simulando BD
 - Se crearon interfaces y clases DAO para Admin, Conductor y Usuario.

 - Cada DAO tiene métodos para obtener usuarios por correo, id, agregar y eliminar.

 - Datos almacenados en listas internas simulando una base de datos en memoria (ideal para pruebas).




2. Inyección de dependencias en LoginController
 - Se eliminaron instancias manuales de DAO y se usó @Autowired para inyectar implementaciones.

 - Esto mejora el manejo de dependencias y facilita cambios futuros.



3. LoginController simplificado
 - Login verifica en orden: admin → conductor → usuario.

 - Guarda datos en sesión (HttpSession) según el tipo.

 - Control de acceso a páginas protegidas validando sesión.

 - Método de logout invalida la sesión.



4. Uso de Spring Boot y anotaciones
 - Se mantiene la aplicación con @SpringBootApplication y ejecución estándar.

 - Controlador con @Controller y rutas con @GetMapping y @PostMapping.



5. No se usa Spring Security
 - El manejo de autenticación y autorización es manual y básico.

 - Para producción se recomienda migrar a Spring Security para mayor seguridad.

 - Archivos principales modificados/agregados
 -  AdminDAO.java y AdminDAOImpl.java (Admin único)

- ConductorDAO.java y ConductorDAOImpl.java (Lista de conductores)

 - UsuarioDAO.java y UsuarioDAOImpl.java (Lista de usuarios)

 - LoginController.java (control de sesiones y login)

 - DemoApplication.java (clase principal Spring Boot)








COMO USARLO
1.Ejecutar DemoApplication para levantar la app.

2.Acceder a /login para iniciar sesión con alguno de los usuarios precargados:

  - Admin: admin@viago.com / admin123

  - Conductor: conductor@viago.com / conductor

  - Usuario: usuario@viago.com / 1234

Después del login, serás redirigido a la vista correspondiente según el rol.
Intentar acceder a rutas sin sesión redirige al login con alerta.
GET /logout cierra sesión.








VERSION 3:
Cambios realizados principalmente en la clase de LoginController, en ReservaController y en la vista de viajes.html

Validación de correo electrónico:
 - Se agregó EmailValidator de Apache Commons Validator para asegurarse de que el email ingresado sea válido antes de buscar en la BD.

Manejo seguro de null:
 - Se usó Optional de Google Guava para evitar errores por valores null al obtener usuarios/conductores.

Verificación de contraseña vacía:
 - Se validó que la contraseña no esté vacía antes de comparar, usando Strings.isNullOrEmpty() de Guava.

Registro de eventos de login:
 - Se integró Logger de SLF4J para registrar accesos exitosos y fallidos (auditoría y seguridad).

Reserva en formato 