package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.entity.Producto;
import com.curso.ecommerce.entity.Usuario;
import com.curso.ecommerce.repository.UsuarioRepository;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;


/**
 * This is a Java class that serves as a controller for managing products, including creating, editing,
 * updating, and deleting them, as well as handling image uploads.
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER= (Logger) LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UploadFileService upload;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * This Java function returns a view called "productos/show" and adds a list of all products to the
	 * model.
	 * 
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows the controller to add attributes to the model, which can then be
	 * accessed by the view to render the data. In this code snippet, the model is used to add a list of
	 * products
	 * @return The method is returning a string "productos/show".
	 */
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos",productoService.findAll());
		return "productos/show";
	}
	
	/**
	 * This Java function returns a string "productos/create" when the "/create" endpoint is accessed
	 * using a GET request.
	 * 
	 * @return The method is returning a String "productos/create".
	 */
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	/**
	 * This Java function saves a product with an image uploaded by a user and associates it with the
	 * current user session.
	 * 
	 * @param producto An object of the class Producto, which contains information about a product.
	 * @param file MultipartFile object representing the image file uploaded by the user.
	 * @param session HttpSession is a class in Java that provides a way to store and retrieve data
	 * between HTTP requests. It allows you to store and retrieve objects associated with a particular
	 * user session. In this code, the session parameter is used to retrieve the id of the current user
	 * who is logged in.
	 * @return The method is returning a String "redirect:/productos".
	 */
	@PostMapping("/save")
	public String save(Producto producto,@RequestParam("img") MultipartFile file,HttpSession session) throws IOException {
		
		LOGGER.info("Este es el objeto producto {}",producto);
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()) ).get();
		
		producto.setUsuario(usuario);
		
		
			String nombreImagen=upload.saveImage(file);
			producto.setImagen(nombreImagen);
	
		
		productoService.save(producto);
		
		return "redirect:/productos";
	}
	
	/**
	 * This Java function retrieves a product with a specific ID and adds it to the model for editing.
	 * 
	 * @param id The id parameter is an Integer value that is passed as a path variable in the URL. It is
	 * used to identify the specific product that needs to be edited.
	 * @param model Model is an interface in Spring MVC that provides a way to pass data between the
	 * controller and the view. It allows adding attributes to the model, which can be accessed in the
	 * view to render dynamic content. In this case, the "producto" attribute is added to the model, which
	 * contains the details
	 * @return The method is returning a string "productos/edit".
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id,Model model) {
		Producto producto=new Producto();
		Optional<Producto> optionalProducto=productoService.get(id);
		producto=optionalProducto.get();
		
		LOGGER.info("Producto buscado: {}",producto);
		model.addAttribute("producto",producto);
		return "productos/edit";
	}

	/**
	 * This function updates a product's information, including its image, and saves it to the database.
	 * 
	 * @param producto It is an object of the class "Producto" which contains the updated information of a
	 * product that needs to be updated in the database.
	 * @param file MultipartFile object representing the image file being uploaded.
	 * @return A string that redirects to the "/productos" endpoint.
	 */
	@PostMapping("/update")
	public String update(Producto producto,@RequestParam("img") MultipartFile file) throws IOException {
		
		Producto product= new Producto();
		product=productoService.get(producto.getId()).get();
		
		
		if(file.isEmpty()) {
			
			producto.setImagen(product.getImagen());
		}else {//Cuando se edita la imgane
			//Eliminar cuando la imagen del producto no sea la que tiene por default
			if(!product.getImagen().equals("default.jpg")) {
				upload.deleteImage(product.getImagen());
			}
			String nombreImagen=upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		
		producto.setUsuario(product.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}

	/**
	 * This function deletes a product and its associated image if it is not the default image.
	 * 
	 * @param id The id parameter is an Integer value that represents the unique identifier of a Producto
	 * object that needs to be deleted from the database.
	 * @return A string that redirects to the "/productos" endpoint.
	 */
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		Producto product= new Producto();
		product=productoService.get(id).get();
		
		//Eliminar cuando la imagen del producto no sea la que tiene por default
		if(!product.getImagen().equals("default.jpg")) {
			upload.deleteImage(product.getImagen());
		}
		
		productoService.delete(id);
		return "redirect:/productos";
	}
}
