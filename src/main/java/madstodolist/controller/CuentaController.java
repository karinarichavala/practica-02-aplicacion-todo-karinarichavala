// src/main/java/madstodolist/controller/CuentaController.java
package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.authentication.ManagerUserSession;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CuentaController {

    @Autowired
    private ManagerUserSession managerUserSession;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/cuenta")
    public String verCuenta(Model model) {
        Long idLogeado = managerUserSession.usuarioLogeado();
        if (idLogeado == null) {
            // Si no hay nadie logueado, rediriges al login
            return "redirect:/login";
        }
        // Traes datos del usuario y los pones en el modelo
        UsuarioData currentUser = usuarioService.findById(idLogeado);
        model.addAttribute("usuario", currentUser);
        return "cuenta"; // Nombre de la plantilla Thymeleaf: cuenta.html
    }
}
