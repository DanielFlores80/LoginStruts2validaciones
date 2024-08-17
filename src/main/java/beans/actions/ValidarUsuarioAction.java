/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Struts2/Action.java to edit this template
 */
package beans.actions;

import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 *
 * @author grenn
 */
/*
En caso de que el metodo execute() devuelva SUCCESS lanzará la vista "bienvenido.jsp"
En caso de que el metodo execute() devuelva INPUT lanzará el redireccionamiento a la misma vista "login.jsp"
*/
@Results({
    @Result(name="success", location="/WEB-INF/content/bienvenido.jsp"),
    
    //Se cambia el redireccionamiento por login.jsp para asignar texto de error a la vista
    @Result(name="input", location="/WEB-INF/content/login.jsp")
    
})
public class ValidarUsuarioAction extends ActionSupport{
    Logger log = LogManager.getLogger(ValidarUsuarioAction.class);
    private String usuario;
    private String password; 

    @Action("validarUsuario")//Variable que es llamada desde "login.jsp" como petición del form
    public String execute() throws Exception {
        if ("admin".equals(usuario)) {
            addActionMessage(getText("usuario.valido"));
            return SUCCESS;//Retorna un "success" en caso de que el usuario ingresado sea "admin"
            
        }else{ 
            return INPUT;//Retorna un "input" en caso de que no lo sea y redirecciona el archivo struts.xml
        }
    }
    
    @Override
    public void validate(){ //metodo que sobreescribe el original para validar que el usuario y la contraseña sean las correctas
        if (this.usuario==null || "".equals(this.usuario.trim())) {//Valida si el usuario es correcto
            addFieldError("usuario", getText("val.usuario"));
        } else if(!usuarioValido()){
            addActionError(getText("usuario.invalido"));
        }
        int nError = validarPassword();
        switch (nError) {
            case 1:
                addFieldError("password", getText("val.password"));//Valida si el campo usuario esta vacio o null
                break;
            case 2:
                addFieldError("password", getText("val.pass.min.lenght"));//Valida si el texto ingresado es menor a 3 caracteres
                break;
            case 3:
                addFieldError("password", getText("val.password.invalido"));//Valida si soincide la contraseña con 12345
                break;
            default:
                break;
        }
    }
    public int validarPassword(){
        int n = 0;
        if (this.password==null || "".equals(this.password.trim())) {//Valida si el campo usuario esta vacio o null
            n=1;
        } else if(this.password.length() < 3){//Valida si el texto ingresado es menor a 3 caracteres
            n=2;
        }else if(!("12345".equals(this.password))){//Valida si soincide la contraseña con 12345
            n=3;
        }
        return n;
    }
    
    public Boolean usuarioValido(){
        return "admin".equals(this.usuario);
    }
    
    //Setters y Getters sin logica para obtener las variables desde la vista "bienvenido.jsp"
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
