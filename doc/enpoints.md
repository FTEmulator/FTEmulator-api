# /api estara siempre delante de los siguientes endpoints.

# /utils
- /state: Devolvera una estado 200 (ok) en caso de estar disponible.

<!-- # /auth-service
- /login: Pedira la creacion de la sesion.
- /register: Pedira la creacion de una cuenta y ademas la creacion de una sesion para la misma.
- /request-change-pass: Solicitara el permiso para el cambio de contraseña.
- /change-pass: Cambiara la contraseña. (depende de request-change-pass). -->

# /profile-service

- /status: Provides service status.
    Request: ${service-ip}:30000/api/utils/profileStatus
    Body: (Empty)

- /getuser: Provides user information.
    Request: ${service-ip}:30000/api/profile/user?userId=${userId}
    Body: {"userId": "${userId}"}