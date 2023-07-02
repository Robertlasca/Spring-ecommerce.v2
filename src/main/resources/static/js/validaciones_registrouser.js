function validarCorreo(input){
    var regex= /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    !regex.test(input.value) ? (toastr.error('Ingrese un correo válido','',{timeOut:2000}),input.value='') : null;
}

function validarContrasena(input){
    var regex=/^(?=.*\d)(?=.*[\u0021-\u002b\u003c-\u0040])(?=.*[A-Z])(?=.*[a-z])\S{8,16}$/;
    
    !regex.test(input.value) ? toastr.error('Ingrese una contraseña válida','',{timeOut:2000}) : toastr.success('Contraseña válida','',{timeOut:2000});

}

function validarNombre(input){
    const regex = /^[a-zA-Z\s]*$/;
    !regex.test(input.value) ? (toastr.error('Ingrese solo letras y espacios','',{timeOut:2000}),input.value=''):null;
}

function validarDireccion(input){
    input.value.length > 80 ? (toastr.error('Demasiados caracteres','',{timeOut:200}),input.value='') : null;
    
}


const form = document.getElementById('form');

form.addEventListener("submit",(event)=>{
    const $InputNombre= document.getElementById('nombre');
    const $InputContrasena= document.getElementById('password');
    const $InputDireccion= document.getElementById('direccion');
    const $InputEmail = document.getElementById('email');

    if($InputEmail.value === "" || $InputContrasena === "" || $InputNombre ==="" || $InputDireccion === "") event.preventDefault(), toastr.error('Campos vacíos','',{timeOut:2000});
})