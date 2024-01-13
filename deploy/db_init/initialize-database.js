db.createUser(
    {
        user: "postgres",
        pwd: "postgres",
        roles: [
            { role: "readWrite", db: "postgres" }
        ]
    }
)