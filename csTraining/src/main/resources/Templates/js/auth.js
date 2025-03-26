document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("login-form");
    const errorMessage = document.getElementById("error-message");

    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        errorMessage.textContent = "";

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        const requestData = {
            email: email,
            password: password
        };

        try {
            const response = await fetch("/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestData)
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || "Error en la autenticación");
            }

            localStorage.setItem("token", data.token);
            localStorage.setItem("nombre", data.nombre);
            localStorage.setItem("oposicion", data.oposicion);
            localStorage.setItem("role", data.role);

            window.location.href = "/home.html";
        } catch (error) {
            errorMessage.textContent = error.message;
        }
    });
});

/*************************************************************************/
/*Registro*/
/*************************************************************************/
document.getElementById("registerForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const userData = {
        username: document.getElementById("username").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        role: document.getElementById("role").value,
        oposicion: document.getElementById("oposicion").value
    };

    try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            const data = await response.json();
            alert(data.message || "Error al registrar usuario");
        } else {
            const data = await response.json();
            alert("¡Registro exitoso! Puedes iniciar sesión.");
            window.location.href = "/login";
        }
    } catch (error) {
        alert("Error en la solicitud. Intenta nuevamente.");
    }
});


