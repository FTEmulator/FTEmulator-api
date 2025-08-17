# /api estara siempre delante de los siguientes endpoints.

# /profile

- /status: Provides service status.
    Request: ${service-ip}:30000/api/utils/profileStatus
    Body: (Empty)

- /getuser: Provides user information.
    Request: ${service-ip}:30000/api/profile/user?userId=${userId}
    Body: {"userId": "${userId}"}