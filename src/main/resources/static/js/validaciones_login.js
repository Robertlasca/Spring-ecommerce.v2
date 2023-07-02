
function validarCorreo(inputCorreo){
    var regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    !regex.test(inputCorreo.value) ? (toastr.error('Ingrese un correo válido','',{timeOut:2000}),inputCorreo.value='') : null;
}


//*Se le añade el evento eventListener para validar los campos vacíos
const form= document.getElementById('form');

form.addEventListener("submit",(event)=>{
    const $InputNombre= document.getElementById('username');
    const $InputContrasena= document.getElementById('password');
    console.log("Este es valor de mi contraseña:",$InputContrasena.value);

    if($InputNombre.value === "" || $InputContrasena.value === "") event.preventDefault(), toastr.error('Campos vacíos','',{timeOut:2000});
})