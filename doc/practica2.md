# Documentación Técnica - Práctica 2

Esta documentación técnica describe la evolución y las funcionalidades implementadas en la Práctica 2 del proyecto *ToDo List*, centrada en el trabajo en equipo y en la extensión de la funcionalidad base.

## Nuevas clases y métodos implementados

### Clases y DTOs

- `UsuarioData`: Se añadió el atributo `esAdmin` y `bloqueado`, con sus getters y setters correspondientes.
- `RegistroData`: Se añadió el atributo `esAdmin` para permitir el registro como administrador.

### Controladores

- `LoginController`:

  - Modificado para manejar el nuevo campo `esAdmin` durante el registro.
  - En login, redirige a `/registrados` si el usuario es administrador.
  - Detecta y notifica si el usuario está bloqueado.

- `UsuarioController`:

  - Nueva ruta `GET /registrados` para ver el listado de usuarios (solo admin).
  - Nueva ruta `GET /registrados/{id}` para ver la descripción de un usuario (solo admin).
  - Nueva ruta `POST /registrados/{id}/toggle-bloqueo` para bloquear o habilitar usuarios.

### Servicios

- `UsuarioService`:
  - `toggleBloqueoUsuario(Long id)`: Alterna el estado bloqueado de un usuario.
  - Se extendió el método `login(...)` para retornar `USER_BLOCKED` si el usuario está bloqueado.

## Plantillas Thymeleaf añadidas o modificadas

- `formRegistro.html`: Se agregó un checkbox para seleccionar si se desea registrar como administrador, solo visible si no hay ningún administrador registrado.
- `registrados.html`: Nueva plantilla para visualizar el listado de usuarios, mostrar su estado (activo/bloqueado) y permitir que el admin los habilite/bloquee.
- `descripcionUsuario.html`: Nueva plantilla para visualizar la información de un usuario registrado.

## Tests implementados

Se implementaron pruebas usando `MockMvc` para verificar las siguientes funcionalidades:

- **NavbarWebTest**: Verifica que la barra de menú aparece solo en las vistas correspondientes.
- **UsuarioListadoWebTest**: Verifica que solo el admin puede acceder a `/registrados`.
- **UsuarioDescripcionWebTest**: Verifica que solo el admin puede ver `/registrados/{id}` y que se maneja correctamente el caso de usuario no encontrado.
- **RegistroAdminWebTest**: Verifica la visualización del checkbox para el registro como admin.
- **BloqueoUsuarioWebTest**: Verifica que el admin puede bloquear/habilitar usuarios y que un usuario bloqueado no puede logearse.

## Ejemplo de código relevante

### Método `toggleBloqueoUsuario` en `UsuarioService`

```java
@Transactional
public void toggleBloqueoUsuario(Long idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new UsuarioServiceException("Usuario no encontrado"));

    boolean nuevoEstado = !Boolean.TRUE.equals(usuario.getBloqueado());
    usuario.setBloqueado(nuevoEstado);

    usuarioRepository.save(usuario);
}
```

Este método invierte el estado de bloqueo de un usuario. Si está activo, lo bloquea; si está bloqueado, lo habilita. Es utilizado por el admin desde la vista `/registrados` mediante un formulario con `POST`.

### Validación de acceso en `UsuarioController`

```java
@GetMapping("/registrados")
public String verUsuariosRegistrados(Model model) {
    Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
    if (idUsuarioLogeado == null)
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No estás logeado");

    UsuarioData usuario = usuarioService.findById(idUsuarioLogeado);
    if (!Boolean.TRUE.equals(usuario.getEsAdmin()))
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Acceso no autorizado");

    List<UsuarioData> usuarios = usuarioService.findAllUsuarios();
    model.addAttribute("usuarios", usuarios);
    return "registrados";
}
```

Este fragmento asegura que solo los administradores puedan acceder al listado de usuarios, validando primero la sesión y luego el rol.

---

Con esta implementación, la aplicación ahora permite una administración de usuarios básica con roles, protección de vistas y control de acceso, todo con pruebas automatizadas que garantizan el correcto funcionamiento del sistema.

