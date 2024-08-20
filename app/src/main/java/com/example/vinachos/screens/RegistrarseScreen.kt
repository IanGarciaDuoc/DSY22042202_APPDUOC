package com.example.vinachos.screens


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.vinachos.LoginActivity
import com.example.vinachos.data.UserData
import com.example.vinachos.data.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarseScreen(userPreferences: UserPreferences = UserPreferences(LocalContext.current)) {

    val context = LocalContext.current

    // Textos de entrada
    var rut by remember { mutableStateOf("") }
    var nombreCompleto by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var validarPassword by remember { mutableStateOf("") }
    var recibirNotificaciones by remember { mutableStateOf("Sí") }

    // Estados de error
    var rutError by remember { mutableStateOf<String?>(null) }
    var nombreCompletoError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var validarPasswordError by remember { mutableStateOf<String?>(null) }

    // Opciones para (DropdownMenu)
    val opcionesNotificaciones = listOf("Sí", "No")
    var expanded by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    fun vibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            } else {

                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        }
    }


    fun validateForm(): Boolean {
        val isValid: Boolean

        rutError = if (rut.isBlank()) "Campo requerido" else null
        nombreCompletoError = if (nombreCompleto.isBlank()) "Campo requerido" else null
        correoError = if (correo.isBlank()) "Campo requerido" else null
        passwordError = if (password.isBlank()) "Campo requerido" else null
        validarPasswordError = if (validarPassword.isBlank()) "Campo requerido" else if (password != validarPassword) "Las contraseñas no coinciden" else null

        isValid = rutError == null && nombreCompletoError == null && correoError == null && passwordError == null && validarPasswordError == null

        return isValid
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF0F0F0)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )


            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("Rut") },
                isError = rutError != null,
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Campo de entrada de Rut" },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (correoError != null) Color(0xFFF76359) else Color(0xFFF76359),
                    cursorColor = Color(0xFFFFFFFF)
                )
            )
            if (rutError != null) {
                Text(
                    text = rutError!!,
                    color = Color(0xFFFF5449),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = nombreCompleto,
                onValueChange = { nombreCompleto = it },
                label = { Text("Nombre Completo") },
                isError = nombreCompletoError != null,
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Campo de entrada de Nombre Completo" },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (correoError != null) Color(0xFFF76359) else Color(0xFFF76359),
                    cursorColor = Color(0xFFFFFFFF)
                )
            )
            if (nombreCompletoError != null) {
                Text(
                    text = nombreCompletoError!!,
                    color = Color(0xFFFF5449),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo Electrónico") },
                isError = correoError != null,
                modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Campo de entrada de Correo Electrónico" },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (correoError != null) Color(0xFFF76359) else Color(0xFFF76359),
                    cursorColor = Color(0xFFFFFFFF)
                )
            )
            if (correoError != null) {
                Text(
                    text = correoError!!,
                    color = Color(0xFFFF5449),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                isError = passwordError != null,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (correoError != null) Color(0xFFF76359) else Color(0xFFF76359),
                    cursorColor = Color(0xFFFFFFFF)
                )
            )
            if (passwordError != null) {
                Text(
                    text = passwordError!!,
                    color = Color(0xFFFF5449),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = validarPassword,
                onValueChange = { validarPassword = it },
                label = { Text("Validar Contraseña") },
                isError = validarPasswordError != null,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (correoError != null) Color(0xFFF76359) else Color(0xFFF76359),
                    cursorColor = Color(0xFFFFFFFF)
                )
            )
            if (validarPasswordError != null) {
                Text(
                    text = validarPasswordError!!,
                    color = Color(0xFFFF5449),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = recibirNotificaciones,
                    onValueChange = { },
                    label = { Text("¿Desea recibir notificaciones?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFFFFC107),
                        cursorColor = Color(0xFFFFC107)
                    ),
                    enabled = false
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    opcionesNotificaciones.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                recibirNotificaciones = opcion
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    var valid = true
                    if (rut.isBlank()) {
                        rutError = "El RUT no puede estar vacío"
                        valid = false
                    }
                    if (nombreCompleto.isBlank()) {
                        nombreCompletoError = "El nombre completo no puede estar vacío"
                        valid = false
                    }
                    if (correo.isBlank()) {
                        correoError = "El correo no puede estar vacío"
                        valid = false
                    }
                    if (password.isBlank()) {
                        passwordError = "La contraseña no puede estar vacía"
                        valid = false
                    } else if (password != validarPassword) {
                        validarPasswordError = "Las contraseñas no coinciden"
                        valid = false
                    } else {
                        validarPasswordError = null
                    }
                    if (recibirNotificaciones.isBlank()) {
                        var recibirNotificacionesError =
                            "La opción de notificaciones no puede estar vacía"
                        valid = false
                    }

                    if (valid) {

                        val newUser = UserData(
                            rut = rut,
                            nombreCompleto = nombreCompleto,
                            correo = correo,
                            password = password,
                            recibirNotificaciones = recibirNotificaciones
                        )


                        CoroutineScope(Dispatchers.IO).launch {
                            val currentUsers = userPreferences.userPreferencesFlow.firstOrNull() ?: mutableListOf()
                            Log.d("RegistrarseScreen", "Current users: $currentUsers")
                            val updatedUsers = currentUsers.toMutableList().apply { add(newUser) }
                            Log.d("RegistrarseScreen", "Updated users: $updatedUsers")
                            userPreferences.saveUserPreferences(updatedUsers)
                        }


                        dialogMessage = "Usuario registrado exitosamente"
                        showDialog = true
                        vibrate(context)
                    } else {
                        // Mostrar mensajes de error
                        dialogMessage = "Error al registrar usuario. Verifique los campos."
                        showDialog = true
                        vibrate(context)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF76359)
                )
            )

            {
                Text("Registrarse", color = Color(0xFF000000))
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Registro") },
                text = { Text(dialogMessage, color = Color(0xFF000000))},
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                        onClick = {
                            showDialog = false

                            context.startActivity(Intent(context, LoginActivity::class.java))
                        }
                    ) {
                        Text("Aceptar", color = Color(0xFF000000))
                    }
                },
                dismissButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }





    }
}